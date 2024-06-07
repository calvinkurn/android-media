package com.tokopedia.shareexperience.data.repository

import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import com.tokopedia.shareexperience.domain.util.ShareExConstants
import com.tokopedia.shareexperience.domain.util.ShareExResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareExBranchRepositoryImpl @Inject constructor() : ShareExBranchRepository {
    override fun generateBranchUniversalObject(
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
                ShareExConstants.ShortLinkKey.CUSTOM_META_TAGS,
                bouRequest.customMetaTags
            )
        )
        return bou
    }

    /**
     * Be careful when creating link properties!
     * Branch will validate each of the value
     * For example, if the url is not valid url, branch will trigger error
     */
    override fun generateLinkProperties(
        params: ShareExBranchLinkPropertiesRequest
    ): LinkProperties {
        val linkerRequest = params.linkerPropertiesRequest
        val linkProperties = LinkProperties()
            .setChannel(params.channelEnum.label)
            .setFeature(linkerRequest.feature.featureName)
            .setCampaign(linkerRequest.campaign)
            .addControlParameter(ShareExConstants.ShortLinkKey.OG_TITLE, linkerRequest.ogTitle)
            .addControlParameter(ShareExConstants.ShortLinkKey.OG_DESCRIPTION, linkerRequest.ogDescription)
            .addControlParameter(ShareExConstants.ShortLinkKey.OG_URL, linkerRequest.desktopUrl)
            .addControlParameter(ShareExConstants.ShortLinkKey.OG_IMAGE_URL, linkerRequest.ogImageUrl)
            .addControlParameter(ShareExConstants.ShortLinkKey.ANDROID_URL, linkerRequest.androidUrl)
            .addControlParameter(ShareExConstants.ShortLinkKey.IOS_URL, linkerRequest.iosUrl)
            .addControlParameter(ShareExConstants.ShortLinkKey.DESKTOP_URL, linkerRequest.desktopUrl)
            .addControlParameter(ShareExConstants.ShortLinkKey.ANDROID_DEEPLINK_PATH, linkerRequest.androidDeeplinkPath)
            .addControlParameter(ShareExConstants.ShortLinkKey.IOS_DEEPLINK_PATH, linkerRequest.iosDeeplinkPath)
            .addControlParameter(ShareExConstants.ShortLinkKey.SOURCE, ShareExConstants.ShortLinkValue.SOURCE)

        if (linkerRequest.anMinVersion.isNotBlank()) {
            linkProperties.addControlParameter(ShareExConstants.ShortLinkKey.AN_MIN_VERSION, linkerRequest.anMinVersion)
        }
        return linkProperties
    }

    override fun getBranchListener(
        scope: ProducerScope<ShareExResult<String>>
    ): Branch.BranchLinkCreateListener {
        return Branch.BranchLinkCreateListener { url, error ->
            scope.launch {
                if (error == null) {
                    scope.send(ShareExResult.Success(url))
                } else {
                    scope.send(ShareExResult.Error(Throwable(error.message)))
                }
                scope.close()
            }
        }
    }
}
