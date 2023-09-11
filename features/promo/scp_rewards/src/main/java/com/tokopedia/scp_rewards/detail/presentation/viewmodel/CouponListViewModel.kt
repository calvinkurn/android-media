package com.tokopedia.scp_rewards.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.scp_rewards.common.constants.SUCCESS_CODE
import com.tokopedia.scp_rewards.common.utils.launchCatchError
import com.tokopedia.scp_rewards.detail.domain.CouponAutoApplyUseCase
import com.tokopedia.scp_rewards.detail.domain.GetMedalBenefitUseCase
import com.tokopedia.scp_rewards.detail.domain.model.ScpRewardsCouponAutoApply
import com.tokopedia.scp_rewards.detail.mappers.MedalBenefitMapper
import com.tokopedia.scp_rewards_common.MergerLiveData
import com.tokopedia.scp_rewards_widgets.constants.CouponStatus
import com.tokopedia.scp_rewards_widgets.model.FilterModel
import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel
import javax.inject.Inject

class CouponListViewModel @Inject constructor(
    private val getMedalBenefitUseCase: GetMedalBenefitUseCase,
    private val couponAutoApplyUseCase: CouponAutoApplyUseCase
) : ViewModel() {

    var couponPageStatus: String? = ""
        private set

    var filtersList: List<FilterModel>? = null
        private set

    private var _filterLiveData: MutableLiveData<Long> = MutableLiveData(-1L)
    var totalItemsCount: Int = 0
        private set

    private val _couponListLiveData: MutableLiveData<CouponState> =
        MutableLiveData(CouponState.Loading)

    val couponListLiveData: LiveData<CouponState> = MergerLiveData.Two(
        firstSource = _filterLiveData,
        secondSource = _couponListLiveData,
        merging = { filter, couponState ->
            when (couponState) {
                CouponState.ActiveTabEmpty -> CouponState.ActiveTabEmpty
                CouponState.HistoryTabEmpty -> CouponState.HistoryTabEmpty
                is CouponState.Error -> CouponState.Error(couponState.error, couponState.errorCode)
                CouponState.Loading -> CouponState.Loading
                is CouponState.Success -> {

                    var list = couponState.list?.filter { benefit ->
                        benefit.categoryIds?.any { it == filter } ?: false
                    }

                    if (list.isNullOrEmpty()) {
                        list = couponState.list
                    }

                    CouponState.Success(list)
                }
            }
        }
    )

    private val _autoApplyCoupon: MutableLiveData<AutoApplyState> = MutableLiveData()
    val autoApplyCoupon: LiveData<AutoApplyState> = _autoApplyCoupon

    fun getCouponList(medaliSlug: String = "", sourceName: String, pageName: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val response = getMedalBenefitUseCase.getMedalBenefits(
                    medaliSlug = medaliSlug,
                    sourceName = sourceName,
                    pageName = pageName,
                    type = couponPageStatus.orEmpty()
                )

                when (val responseCode = response.scpRewardsMedaliBenefitList?.resultStatus?.code) {
                    SUCCESS_CODE -> {
                        val list =
                            MedalBenefitMapper.mapBenefitApiResponseToBenefitModelList(response.scpRewardsMedaliBenefitList.medaliBenefitList?.benefitList)
                        val benefitStatus = list?.firstOrNull()?.status
                        when (benefitStatus) {
                            CouponStatus.EMPTY -> {
                                _couponListLiveData.postValue(CouponState.HistoryTabEmpty)
                            }
                            CouponStatus.ERROR -> {
                                _couponListLiveData.postValue(CouponState.Error(Throwable()))
                            }
                            else -> {
                                setTotalItemCount(list?.size.orZero())
                                setCouponsList(list)
                            }
                        }
                    }

                    else -> {
                        _couponListLiveData.postValue(
                            CouponState.Error(
                                Throwable(),
                                responseCode.orEmpty()
                            )
                        )
                    }
                }
            },
            onError = {
                _couponListLiveData.postValue(CouponState.Error(it))
            }
        )
    }

    fun applyCoupon(
        benefitModel: MedalBenefitModel,
        shopId: Int? = null,
        couponCode: String,
        position: Int
    ) {
        viewModelScope.launchCatchError(
            block = {
                _autoApplyCoupon.postValue(AutoApplyState.Loading(benefitModel, position))
                val response = couponAutoApplyUseCase.applyCoupon(shopId, couponCode)
                if (response.data != null && response.data.resultStatus.code == SUCCESS_CODE) {
                    if (response.data.couponAutoApply?.isSuccess == true) {
                        _autoApplyCoupon.postValue(
                            AutoApplyState.SuccessCouponApplied(
                                response.data,
                                benefitModel,
                                position
                            )
                        )
                    } else {
                        _autoApplyCoupon.postValue(
                            AutoApplyState.SuccessCouponFailed(
                                response.data,
                                benefitModel,
                                position
                            )
                        )
                    }
                } else {
                    throw Throwable()
                }
            },
            onError = {
                _autoApplyCoupon.postValue(AutoApplyState.Error(it, benefitModel, position))
            }
        )
    }

    fun setCouponsList(list: List<MedalBenefitModel>?) {
        when (couponPageStatus) {
            CouponStatus.EMPTY -> _couponListLiveData.postValue(CouponState.ActiveTabEmpty)
            else -> {
                setTotalItemCount(list?.size.orZero())
                _couponListLiveData.postValue(CouponState.Success(list))
            }
        }
    }

    fun setFilters(filters: List<FilterModel>?) {
        filtersList = filters
        getFilteredData(filters?.find { it.isSelected } ?: FilterModel(-1L))
    }

    fun setPageStatus(status: String?) {
        couponPageStatus = status
    }

    private fun setTotalItemCount(count: Int) {
        totalItemsCount = count
    }

    fun getFilteredData(filter: FilterModel) {
        this._filterLiveData.postValue(filter.id ?: -1L)
    }

    sealed class CouponState {
        class Success(val list: List<MedalBenefitModel>?) : CouponState()
        class Error(val error: Throwable, val errorCode: String = "") : CouponState()
        object ActiveTabEmpty : CouponState()
        object HistoryTabEmpty : CouponState()
        object Loading : CouponState()
    }

    sealed class AutoApplyState {
        data class Loading(val benefit: MedalBenefitModel, val position: Int) : AutoApplyState()
        data class SuccessCouponApplied(
            val data: ScpRewardsCouponAutoApply?,
            val benefit: MedalBenefitModel,
            val position: Int
        ) : AutoApplyState()

        data class SuccessCouponFailed(
            val data: ScpRewardsCouponAutoApply?,
            val benefit: MedalBenefitModel,
            val position: Int
        ) : AutoApplyState()

        data class Error(
            val throwable: Throwable,
            val benefit: MedalBenefitModel,
            val position: Int
        ) : AutoApplyState()
    }
}
