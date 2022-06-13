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

    fun generateShareString(url: String): String {
        return if(data.textDescription.contains("${'$'}{url}")) data.textDescription.replace("${'$'}{url}", url)
                else "${data.textDescription}\n$url"
    }

    override fun createUrl(listener: PlayShareExperience.Listener) {
        val linkerData = LinkerData().apply {
            id = data.id
            name = data.title
            description = data.title
            textContent = data.textDescription
            imgUri = data.coverUrl
            type = LinkerData.PLAY_VIEWER
            uri = data.redirectUrl

            feature = shareModel.feature
            channel = shareModel.channel
            campaign = shareModel.campaign

            ogTitle = data.metaTitle
            ogDescription = data.metaDescription
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