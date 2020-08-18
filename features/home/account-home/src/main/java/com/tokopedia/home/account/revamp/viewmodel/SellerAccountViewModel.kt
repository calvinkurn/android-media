package com.tokopedia.home.account.revamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.account.presentation.util.dispatchers.DispatcherProvider
import com.tokopedia.home.account.revamp.domain.usecase.GetSellerAccountDataUseCase
import com.tokopedia.notifications.common.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user_identification_common.KYCConstant
import javax.inject.Inject

class SellerAccountViewModel @Inject constructor(
        private val getSellerAccountUseCase: GetSellerAccountDataUseCase,
        dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    private val _sellerData = MutableLiveData<Result<GraphqlResponse>>()
    val sellerData: LiveData<Result<GraphqlResponse>>
        get() = _sellerData

    fun getSellerData(shopIds: IntArray) {
        launchCatchError(block = {
            val data = getSellerAccountUseCase.apply {
                params = mapOf(
                        GetSellerAccountDataUseCase.SHOP_IDS to shopIds,
                        GetSellerAccountDataUseCase.MERCHANT_ID to shopIds[0],
                        GetSellerAccountDataUseCase.PROJECT_ID to KYCConstant.KYC_PROJECT_ID
                )
            }.executeOnBackground()
            _sellerData.postValue(Success(data))
        }, onError = {
            _sellerData.postValue(Fail(it))
        })
    }
}