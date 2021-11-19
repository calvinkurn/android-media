package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.affiliate.model.response.AffiliateGenerateLinkData
import com.tokopedia.affiliate.usecase.AffiliateGenerateLinkUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromotionBSViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        var affiliateGenerateLinkUseCase: AffiliateGenerateLinkUseCase
) : BaseViewModel() {
    private var generateLinkData = MutableLiveData<AffiliateGenerateLinkData.AffiliateGenerateLink.Data?>()
    private var errorMessage = MutableLiveData<String>()
    private var loading = MutableLiveData<Boolean>()

    fun affiliateGenerateLink(id: Int?, url: String?, identifier: String?) {
        loading.value = true
        launchCatchError(block = {
            loading.value = false
            generateLinkData.value = affiliateGenerateLinkUseCase.affiliateGenerateLink(id, url, identifier)
        }, onError = {
            loading.value = false
            errorMessage.value = it.localizedMessage
            it.printStackTrace()
        })
    }

    fun getErrorMessage(): LiveData<String> = errorMessage
    fun loading(): LiveData<Boolean> = loading
    fun generateLinkData(): LiveData<AffiliateGenerateLinkData.AffiliateGenerateLink.Data?> = generateLinkData

}