package com.tokopedia.shareexperience.data.repository

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.repository.ShareExShortLinkRepository
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ShareExBranchLinkRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ShareExShortLinkRepository {

    override suspend fun generateShortLink(): Flow<ShareExResult<String>> {
        return callbackFlow {
            val branchUniversalObject = generateBranchUniversalObject()
            val linkProperties = generateLinkProperties()
            branchUniversalObject.generateShortUrl(
                context,
                linkProperties,
                getBranchListener(this)
            )
            awaitClose { channel.close() }
        }
    }

    private fun generateBranchUniversalObject(): BranchUniversalObject {
        return BranchUniversalObject()
            .setCanonicalIdentifier("content/12345")
            .setTitle("My Content Title")
            .setContentDescription("My Content Description")
            .setContentImageUrl("https://lorempixel.com/400/400")
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setContentMetadata(ContentMetadata().addCustomMetadata("key1", "value1"))
    }

    /**
     * Be careful when creating link properties!
     * Branch will validate each of the value
     * For example, if the url is not valid url, branch will trigger error
     */
    private fun generateLinkProperties(): LinkProperties {
        return LinkProperties()
            .setChannel("facebook")
            .setFeature("sharing")
            .setCampaign("content 123 launch")
            .addControlParameter("\$og_title", "Kitchin Sukēru P2P-001 - Rp600.001")
            .addControlParameter("\$og_description", "Kitchin Sukēru P2P-001 - Rp600.001")
            .addControlParameter("\$og_url", "https://images.tokopedia.net/img/generator/RKdhUE/c7d0a9e45c851fe4212b5b9845f3697e.jpg")
            .addControlParameter("\$og_image_url", "https://images.tokopedia.net/img/generator/RKdhUE/c7d0a9e45c851fe4212b5b9845f3697e.jpg")
            .addControlParameter("an_min_version", "")
            .addControlParameter("ios_min_version", "")
            .addControlParameter("\$android_url", "https://www.tokopedia.com/")
            .addControlParameter("\$ios_url", "https://www.tokopedia.com/")
            .addControlParameter("\$desktop_url", "https://www.tokopedia.com/")
            .addControlParameter("\$android_deeplink_path", "tokopedia://home")
            .addControlParameter("\$ios_deeplink_path", "tokopedia://home")
            .addControlParameter("source", "android")
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
