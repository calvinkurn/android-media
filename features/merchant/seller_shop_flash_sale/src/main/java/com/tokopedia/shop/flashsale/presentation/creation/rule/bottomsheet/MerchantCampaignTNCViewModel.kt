package com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flashsale.domain.usecase.GetMerchantCampaignTNCUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class MerchantCampaignTNCViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getMerchantTNCUseCase: GetMerchantCampaignTNCUseCase
) : BaseViewModel(dispatchers.main) {

    private val _merchantCampaignTNC = MutableLiveData<Result<MerchantCampaignTNC>>()
    val merchantCampaignTNC: LiveData<Result<MerchantCampaignTNC>>
        get() = _merchantCampaignTNC

    fun getMerchantCampaignTNC(
        campaignId: Long,
        isUniqueBuyer: Boolean,
        isCampaignRelation: Boolean,
        paymentType: PaymentType
    ){
        launchCatchError(
            dispatchers.io,
            block = {
                val merchantCampaignTNC = getMerchantTNCUseCase.execute(
                    campaignId = campaignId,
                    isUniqueBuyer = isUniqueBuyer,
                    isCampaignRelation = isCampaignRelation,
                    paymentType = paymentType
                )
                _merchantCampaignTNC.postValue(Success(merchantCampaignTNC))
            },
            onError = { error ->
                _merchantCampaignTNC.postValue(Fail(error))
            }
        )
    }
}
