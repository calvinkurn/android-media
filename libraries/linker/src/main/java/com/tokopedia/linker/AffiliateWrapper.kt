package com.tokopedia.linker

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.linker.LinkerConstants.*
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.*
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AffiliateWrapper {

    private var handler: Handler? = null
    private var remoteConfig: RemoteConfig? = null
    private val APP_AFFILIATE_CALLBACK_TIMEOUT_KEY = "android_affiliate_callback_timeout_key"


    fun executeAffiliateUseCase(data: LinkerData, shareCallback: ShareCallback, context: Context){
        setAffiliateCallbackTimeOutFunction(
            shareCallback,
            data,
            getRemoteConfigTimeOutValue(context, APP_AFFILIATE_CALLBACK_TIMEOUT_KEY)
        )

        CoroutineScope(Dispatchers.IO).launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val affiliateUseCase = AffiliateLinkGeneratorUseCase(GraphqlInteractor.getInstance().graphqlRepository)
                val affiliateShortUrl = affiliateUseCase.apply {
                    params = AffiliateLinkGeneratorUseCase.createParam(createAffiliateRequestData(data))
                }.executeOnBackground()
                shareCallback.urlCreated(LinkerUtils.createShareResult(data.getTextContentForBranch(affiliateShortUrl), affiliateShortUrl, affiliateShortUrl))
                removeHandlerTimeoutMessage()
            }
        }, onError = {
            shareCallback.urlCreated(
                LinkerUtils.createShareResult(
                    data.textContent,
                    data.renderShareUri(),
                    data.renderShareUri()
                )
            )
            removeHandlerTimeoutMessage()
            it.printStackTrace()
        })
    }

    private fun getRemoteConfigTimeOutValue(context: Context, key: String): Long {
        return getRemoteConfig(context)?.getLong(key, 5000) ?: 5000
    }

    private fun getRemoteConfig(context: Context): RemoteConfig? {
        if (remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(context)
        }
        return remoteConfig
    }

    private fun setAffiliateCallbackTimeOutFunction(
        shareCallback: ShareCallback,
        data: LinkerData,
        timeoutDuration: Long
    ) {
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(
            {
                shareCallback.urlCreated(
                    LinkerUtils.createShareResult(
                        data.textContent,
                        data.renderShareUri(),
                        data.renderShareUri()
                    )
                )
                Timber.w("P2#AFFILIATE_LINK_TIMEOUT#error;linkdata='%s'", data.id)
            },
            timeoutDuration
        )
    }

    //Remove all the pending runnable calls
    private fun removeHandlerTimeoutMessage() {
        if (handler != null) {
            handler?.removeCallbacksAndMessages(null)
        }
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