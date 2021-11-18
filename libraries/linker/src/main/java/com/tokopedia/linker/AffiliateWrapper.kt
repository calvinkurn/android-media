package com.tokopedia.linker

import android.content.Context
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
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
        affiliateGenerateLinkInput.source = "sharing"
        affiliateGenerateLinkInput.channel = ArrayList()
        affiliateGenerateLinkInput.channel?.add(13)
        affiliateGenerateLinkInput.link = ArrayList()
        val linkData = Link()
        linkData.uRL = data.desktopUrl
        linkData.type = "pdp"
        linkData.identifier = data.id
        linkData.identifierType = 0
        linkData.additionalParams = ArrayList()
        addAdditionalParamToLinkData("og_title", data.ogTitle, linkData)
        addAdditionalParamToLinkData("og_description", data.ogDescription, linkData)
        addAdditionalParamToLinkData("og_image_url", data.ogImageUrl, linkData)
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