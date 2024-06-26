package com.tokopedia.shopdiscount.subsidy.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel
import com.tokopedia.shopdiscount.subsidy.domain.DoOptOutSubsidyUseCase
import com.tokopedia.shopdiscount.subsidy.domain.SubsidyEngineGetSellerOutReasonListUseCase
import com.tokopedia.shopdiscount.subsidy.model.mapper.DoOptOutSubsidyRequestMapper
import com.tokopedia.shopdiscount.subsidy.model.mapper.ShopDiscountOptOutReasonUiModelMapper
import com.tokopedia.shopdiscount.subsidy.model.mapper.SubsidyEngineGetSellerOutReasonListRequestMapper
import com.tokopedia.shopdiscount.subsidy.model.response.DoOptOutResponse
import com.tokopedia.shopdiscount.subsidy.model.response.SubsidyEngineGetSellerOutReasonListResponse
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountOptOutReasonUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopDiscountOptOutReasonBottomSheetViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val subsidyEngineGetSellerOutReasonListUseCase: SubsidyEngineGetSellerOutReasonListUseCase,
    private val doOptOutSubsidyUseCase: DoOptOutSubsidyUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatcherProvider.main) {

    val listOptOutReasonLiveData: LiveData<Result<List<ShopDiscountOptOutReasonUiModel>>>
        get() = _listOptOutReasonLiveData
    private val _listOptOutReasonLiveData =
        MutableLiveData<Result<List<ShopDiscountOptOutReasonUiModel>>>()

    val doOptOutSubsidyResultLiveData: LiveData<Result<Boolean>>
        get() = _doOptOutSubsidyResultLiveData
    private val _doOptOutSubsidyResultLiveData = MutableLiveData<Result<Boolean>>()

    fun getListOptOutReason() {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getOptOutReasonListResponse()
            val mappedModel = ShopDiscountOptOutReasonUiModelMapper.map(response)
            _listOptOutReasonLiveData.postValue(Success(mappedModel))
        }) {
            _listOptOutReasonLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getOptOutReasonListResponse(): SubsidyEngineGetSellerOutReasonListResponse {
        subsidyEngineGetSellerOutReasonListUseCase.setParams(
            SubsidyEngineGetSellerOutReasonListRequestMapper.map(
                userId = userSession.userId
            )
        )
        return subsidyEngineGetSellerOutReasonListUseCase.executeOnBackground()
    }

    fun doOptOutProductSubsidy(
        selectedProductToOptOut: MutableList<ShopDiscountProductDetailUiModel.ProductDetailData>,
        listEventId: List<String>,
        selectedReason: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = doOptOutReasonListResponse(listEventId, selectedReason)
            if (response.doSellerOutProgram.responseHeader.isSuccess) {
                selectedProductToOptOut.forEach { productDetailData ->
                    productDetailData.isSubsidy = false
                }
                _doOptOutSubsidyResultLiveData.postValue(Success(true))
            } else {
                _doOptOutSubsidyResultLiveData.postValue(Fail(MessageErrorException()))
            }
        }) {
            _doOptOutSubsidyResultLiveData.postValue(Fail(it))
        }
    }

    private suspend fun doOptOutReasonListResponse(
        listEventId: List<String>,
        selectedReason: String
    ): DoOptOutResponse {
        doOptOutSubsidyUseCase.setParams(
            DoOptOutSubsidyRequestMapper.map(
                listPromotionId = listEventId,
                listReasonOptOut = listOf(selectedReason)
            )
        )
        return doOptOutSubsidyUseCase.executeOnBackground()
    }

}
