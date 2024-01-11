package com.tokopedia.shareexperience.data.repository

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shareexperience.domain.ShareExConstants
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.ANDROID_DEEPLINK_PATH
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.ANDROID_URL
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.AN_MIN_VERSION
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.CUSTOM_META_TAGS
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.DESKTOP_URL
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.IOS_DEEPLINK_PATH
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.IOS_MIN_VERSION
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.IOS_URL
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.OG_DESCRIPTION
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.OG_IMAGE_URL
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.OG_TITLE
import com.tokopedia.shareexperience.domain.ShareExConstants.BranchKey.OG_URL
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.repository.ShareExShortLinkRepository
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import javax.inject.Inject

class ShareExBranchLinkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ShareExShortLinkRepository {

    override suspend fun generateShortLink(
        params: ShareExBranchLinkPropertiesRequest
    ): Flow<ShareExResult<String>> {
        return callbackFlow {
            val branchUniversalObject = generateBranchUniversalObject(params.branchUniversalObjectRequest)
            val linkProperties = generateLinkProperties(params)
            branchUniversalObject.generateShortUrl(
                context,
                linkProperties,
                getBranchListener(this)
            )
            awaitClose { channel.close() }
        }
    }

    private fun generateBranchUniversalObject(
        bouRequest: ShareExBranchUniversalObjectRequest
    ): BranchUniversalObject {
        val bou = BranchUniversalObject()
            .setCanonicalIdentifier(bouRequest.canonicalId)
            .setTitle(bouRequest.title)
            .setContentDescription(bouRequest.description)
            .setContentImageUrl(bouRequest.contentImageUrl)
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
        bou.setContentMetadata(
            ContentMetadata().addCustomMetadata(
                CUSTOM_META_TAGS,
                JSONObject(bouRequest.contentMetadataMap).toString()
            )
        )
        return bou
    }

    /**
     * Be careful when creating link properties!
     * Branch will validate each of the value
     * For example, if the url is not valid url, branch will trigger error
     */
    private fun generateLinkProperties(
        params: ShareExBranchLinkPropertiesRequest
    ): LinkProperties {
        val linkerRequest = params.linkerPropertiesRequest
        val linkProperties = LinkProperties()
            .setChannel(params.channelEnum.label)
            .setFeature(linkerRequest.feature.featureName)
            .setCampaign(linkerRequest.campaign)
            .addControlParameter(OG_TITLE, linkerRequest.ogTitle)
            .addControlParameter(OG_DESCRIPTION, linkerRequest.ogDescription)
            .addControlParameter(OG_URL, linkerRequest.desktopUrl)
            .addControlParameter(OG_IMAGE_URL, linkerRequest.ogImageUrl)
            .addControlParameter(ANDROID_URL, linkerRequest.androidUrl)
            .addControlParameter(IOS_URL, linkerRequest.iosUrl)
            .addControlParameter(DESKTOP_URL, linkerRequest.desktopUrl)
            .addControlParameter(ANDROID_DEEPLINK_PATH, linkerRequest.androidDeeplinkPath)
            .addControlParameter(IOS_DEEPLINK_PATH, linkerRequest.iosDeeplinkPath)
            .addControlParameter(ShareExConstants.BranchKey.SOURCE, ShareExConstants.BranchValue.SOURCE)

        val androidMinVersion = params.branchUniversalObjectRequest.contentMetadataMap[AN_MIN_VERSION]
        if (androidMinVersion != null) {
            linkProperties.addControlParameter(AN_MIN_VERSION, androidMinVersion)
        }
        val iosMinVersion = params.branchUniversalObjectRequest.contentMetadataMap[IOS_MIN_VERSION]
        if (iosMinVersion != null) {
            linkProperties.addControlParameter(IOS_MIN_VERSION, iosMinVersion)
        }
        return linkProperties
    }

    private fun getBranchListener(
        scope: ProducerScope<ShareExResult<String>>
    ): Branch.BranchLinkCreateListener {
        return Branch.BranchLinkCreateListener { url, error ->
            if (error == null) {
                scope.trySend(ShareExResult.Success(url))
            } else {
                scope.trySend(ShareExResult.Error(Throwable(error.message)))
            }
        }
    }
}
