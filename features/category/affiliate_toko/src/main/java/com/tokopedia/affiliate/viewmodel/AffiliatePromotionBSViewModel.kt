package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.affiliate.model.response.AffiliateGenerateLinkData
import com.tokopedia.affiliate.usecase.AffiliateGenerateLinkUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class AffiliatePromotionBSViewModel @Inject constructor(
    var affiliateGenerateLinkUseCase: AffiliateGenerateLinkUseCase
) : BaseViewModel() {
    private var generateLinkData =
        MutableLiveData<AffiliateGenerateLinkData.AffiliateGenerateLink.Data?>()
    private var errorMessage = MutableLiveData<String>()
    private var loading = MutableLiveData<Boolean>()

    fun affiliateGenerateLink(
        id: Int?,
        pageType: String,
        itemId: String,
        url: String?,
        identifier: String?,
        type: String
    ) {
        loading.value = true
        launchCatchError(
            block = {
                loading.value = false
                generateLinkData.value = affiliateGenerateLinkUseCase.affiliateGenerateLink(
                    id,
                    pageType,
                    itemId,
                    url,
                    identifier,
                    type
                )
            },
            onError = {
                loading.value = false
                errorMessage.value = it.localizedMessage
                it.printStackTrace()
            }
        )
    }

    fun getErrorMessage(): LiveData<String> = errorMessage
    fun loading(): LiveData<Boolean> = loading
    fun generateLinkData(): LiveData<AffiliateGenerateLinkData.AffiliateGenerateLink.Data?> =
        generateLinkData
}
