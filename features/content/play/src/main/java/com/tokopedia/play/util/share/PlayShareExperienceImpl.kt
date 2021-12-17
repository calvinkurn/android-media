package com.tokopedia.play.util.share

import android.content.Context
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.play.R
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 10, 2021
 */
class PlayShareExperienceImpl @Inject constructor(
    private val context: Context,
): PlayShareExperience {

    private lateinit var shareModel: ShareModel
    private lateinit var data: PlayShareExperienceData

    override fun isCustomSharingAllow(): Boolean =
        UniversalShareBottomSheet.isCustomSharingEnabled(context)

    override fun setShareModel(shareModel: ShareModel): PlayShareExperience {
        this.shareModel = shareModel
        return this
    }

    override fun setData(data: PlayShareExperienceData): PlayShareExperience {
        this.data = data
        return this
    }

    private fun getString(
        resId: Int
    ): String = context.resources.getString(resId)


    fun generateShareString(url: String): String {
        val description = getString(R.string.play_sharing_text_description)
        return "${data.title}\n$description\n$url"
    }

    private fun getShareTextContent(): String {
        val description = getString(R.string.play_sharing_text_description)
        return "${data.title}\n$description"
    }

    private fun generateOgTitle(): String =
        getString(R.string.play_sharing_text_og_title).format(data.partnerName)

    private fun generateOgDescription(): String =
        getString(R.string.play_sharing_text_og_description).format(data.partnerName)

    private fun generateDeepLinkPath(): String = "play/${data.id}"

    override fun createUrl(listener: PlayShareExperience.Listener) {
        val linkerData = LinkerData().apply {
            id = data.id
            name = data.title
            description = generateOgDescription()
            textContent = getShareTextContent()
            imgUri = data.coverUrl
            deepLink = generateDeepLinkPath()
            ogUrl = data.redirectUrl
            type = LinkerData.PLAY_VIEWER
            uri = data.redirectUrl

            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign

            ogTitle = generateOgTitle()
            ogDescription = generateOgDescription()
            ogImageUrl = shareModel.ogImgUrl
        }

        val linkerShareData = LinkerShareData()
        linkerShareData.linkerData = linkerData
        LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0, linkerShareData, object:
            ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                listener.onUrlCreated(linkerShareData, shareModel, generateShareString(linkerShareData?.url ?: data.redirectUrl))
            }

            override fun onError(linkerError: LinkerError?) {
                listener.onError(Exception(linkerError?.errorMessage))
            }
        }))
    }

    override fun isScreenshotBottomSheet(): Boolean =
        UniversalShareBottomSheet.getShareBottomSheetType() == UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET
}