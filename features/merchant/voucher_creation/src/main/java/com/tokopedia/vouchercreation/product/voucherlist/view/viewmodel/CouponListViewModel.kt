package com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.NonNullLiveData
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.common.domain.usecase.CancelVoucherUseCase
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.CouponImageWithShop
import com.tokopedia.vouchercreation.product.share.domain.usecase.GenerateImageWithStatisticsFacadeUseCase
import com.tokopedia.vouchercreation.product.voucherlist.view.bottomsheet.CouponFilterBottomSheet
import com.tokopedia.vouchercreation.product.voucherlist.view.constant.CouponListConstant.LIST_COUPON_PER_PAGE
import com.tokopedia.vouchercreation.product.voucherlist.view.mapper.CouponModelMapper.mapToTarget
import com.tokopedia.vouchercreation.product.voucherlist.view.mapper.CouponModelMapper.mapToType
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherStatus
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetVoucherListUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getVoucherListUseCase: GetVoucherListUseCase,
    private val cancelVoucherUseCase: CancelVoucherUseCase,
    private val generateImageFacadeUseCase: GenerateImageWithStatisticsFacadeUseCase,
    private val getCouponDetailUseCase: GetCouponDetailUseCase
) : BaseViewModel(dispatchers.main) {

    private val _couponList = MutableLiveData<Result<List<VoucherUiModel>>>()
    private val _cancelCoupon = MutableLiveData<Result<Int>>()
    private val _stopCoupon = MutableLiveData<Result<Int>>()
    private val _shopBasicData = MutableLiveData<Result<ShopBasicDataResult>>()
    private val _selectedFilterType = NonNullLiveData(CouponFilterBottomSheet.FilterType.NOT_SELECTED)
    private val _selectedFilterTarget = NonNullLiveData(CouponFilterBottomSheet.FilterTarget.NOT_SELECTED)
    private var _couponStatusFilter: String = VoucherStatus.NOT_STARTED_AND_ONGOING
    private var _couponSearchKeyword: String? = null
    private var coupon: CouponUiModel? = null

    val couponList: LiveData<Result<List<VoucherUiModel>>>
        get() = _couponList
    val cancelCoupon: LiveData<Result<Int>>
        get() = _cancelCoupon
    val stopCoupon: LiveData<Result<Int>>
        get() = _stopCoupon
    val shopBasicData: LiveData<Result<ShopBasicDataResult>>
        get() = _shopBasicData
    val selectedFilterType: LiveData<CouponFilterBottomSheet.FilterType>
        get() = _selectedFilterType
    val selectedFilterTarget: LiveData<CouponFilterBottomSheet.FilterTarget>
        get() = _selectedFilterTarget
    val couponSearchKeyword: String?
        get() = _couponSearchKeyword
    val couponStatusFilter: String
        get() = _couponStatusFilter

    private val _couponImageWithShop = MutableLiveData<Result<CouponImageWithShop>>()
    val couponImageWithShop: LiveData<Result<CouponImageWithShop>> = _couponImageWithShop

    private val _couponDetail = MutableLiveData<Result<CouponUiModel>>()
    val couponDetail: LiveData<Result<CouponUiModel>>
        get() = _couponDetail


    fun setStatusFilter(@VoucherStatus couponStatus: String) {
        _couponStatusFilter = couponStatus
    }

    fun setCouponSearchKeyword(keyword: String) {
        _couponSearchKeyword = keyword
    }

    fun setSelectedFilterType(selectedType: CouponFilterBottomSheet.FilterType) {
        _selectedFilterType.value = selectedType
    }

    fun setSelectedFilterTarget(selectedTarget: CouponFilterBottomSheet.FilterTarget) {
        _selectedFilterTarget.value = selectedTarget
    }

    fun setCoupon(coupon: CouponUiModel) {
        this.coupon = coupon
    }

    fun getCoupon(): CouponUiModel? {
        return coupon
    }

    fun getCouponList(page: Int) {
        launchCatchError(block = {
            val ongoingVoucherRequestParam = VoucherListParam.createParamCouponList(
                type = mapToType(_selectedFilterType.value),
                target = mapToTarget(_selectedFilterTarget.value),
                status = _couponStatusFilter,
                page = page,
                perPage = LIST_COUPON_PER_PAGE,
                voucherName = _couponSearchKeyword
            )
            _couponList.value = Success(withContext(dispatchers.io) {
                getVoucherListUseCase.params = GetVoucherListUseCase.createRequestParam(ongoingVoucherRequestParam)
                getVoucherListUseCase.executeOnBackground()
            })

        }, onError = {
            _couponList.value = Fail(it)
        })
    }

    fun cancelCoupon(couponId: Int, @CancelVoucherUseCase.CancelStatus status: String) {
        val liveData = if (status == CancelVoucherUseCase.CancelStatus.STOP) {
            _stopCoupon
        } else {
            _cancelCoupon
        }

        launchCatchError(block = {
            cancelVoucherUseCase.params = CancelVoucherUseCase.createRequestParam(couponId, status)
            liveData.value = Success(withContext(dispatchers.io) {
                cancelVoucherUseCase.executeOnBackground()
            })
        }, onError = {
            liveData.value = Fail(it)
        })
    }


    fun getCouponDetail(couponId : Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId)
                    getCouponDetailUseCase.executeOnBackground()
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

    fun generateImage(coupon : CouponUiModel) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    generateImageFacadeUseCase.execute(
                        this,
                        ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                        coupon
                    )
                }
                _couponImageWithShop.value = Success(result)
            },
            onError = {
                _couponImageWithShop.setValue(Fail(it))
            }
        )
    }

}