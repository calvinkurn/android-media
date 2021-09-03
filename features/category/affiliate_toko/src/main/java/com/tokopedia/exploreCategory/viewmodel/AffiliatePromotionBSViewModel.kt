package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.model.AffiliateGenerateLinkData
import com.tokopedia.exploreCategory.usecase.AffiliateGenerateLinkUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class AffiliatePromotionBSViewModel @Inject constructor(
        var affiliateGenerateLinkUseCase: AffiliateGenerateLinkUseCase
) : BaseViewModel() {
    private var generateLinkData = MutableLiveData<AffiliateGenerateLinkData>()
    private var errorMessage = MutableLiveData<String>()
    private var progressBar = MutableLiveData<Boolean>()

    fun affiliateGenerateLink() {
        launchCatchError(block = {
            progressBar.value = false
            generateLinkData.value = affiliateGenerateLinkUseCase.affiliateGenerateLink("")
        }, onError = {
            progressBar.value = false
            it.printStackTrace()
            errorMessage.value = it.localizedMessage
        })
    }

    fun getErrorMessage(): LiveData<String> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar
    fun generateLinkData(): LiveData<AffiliateGenerateLinkData> = generateLinkData

}