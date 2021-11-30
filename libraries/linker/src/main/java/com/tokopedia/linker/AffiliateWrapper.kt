package com.tokopedia.linker

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.linker.LinkerConstants.*
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.*
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AffiliateWrapper {

    fun executeAffiliateUseCase(data: LinkerData, shareCallback: ShareCallback){
        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val affiliateUseCase = AffiliateLinkGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
                val affiliateShortUrl = affiliateUseCase.apply {
                    params = AffiliateLinkGeneratorUseCase.createParam(createAffiliateRequestData(data))
                }.executeOnBackground()
                shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContentForBranch(affiliateShortUrl), affiliateShortUrl, affiliateShortUrl))
            }
        }, onError = {
            it.printStackTrace()
        })
    }

    private fun createAffiliateRequestData(data: LinkerData):AffiliateGenerateLinkInput{
        val affiliateGenerateLinkInput = AffiliateGenerateLinkInput()
        affiliateGenerateLinkInput.source = LABEL_SHARING
        affiliateGenerateLinkInput.channel = ArrayList()
        affiliateGenerateLinkInput.channel?.add(13)
        affiliateGenerateLinkInput.link = ArrayList()
        val linkData = Link()
        linkData.uRL = data.uri
        linkData.type = PDP_LABEL
        linkData.identifier = data.id
        linkData.identifierType = 0
        linkData.additionalParams = ArrayList()
        addAdditionalParamToLinkData(KEY_OG_TITLE_LABEL, data.ogTitle, linkData)
        addAdditionalParamToLinkData(KEY_OG_DESC_LABEL, data.ogDescription, linkData)
        addAdditionalParamToLinkData(KEY_OG_IMAGE_URL_LABEL, data.ogImageUrl, linkData)
        affiliateGenerateLinkInput.link?.add(linkData)
        return affiliateGenerateLinkInput
    }

    private fun addAdditionalParamToLinkData(key: String, value: String, linkData: Link){
        var additionalParam = AdditionalParam()
        additionalParam.key = key
        additionalParam.value = value
        linkData.additionalParams?.add(additionalParam)
    }
}