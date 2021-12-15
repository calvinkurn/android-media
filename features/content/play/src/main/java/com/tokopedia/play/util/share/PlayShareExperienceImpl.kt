package com.tokopedia.play.util.share

import android.content.Context
import android.util.Log
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareData
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 10, 2021
 */
class PlayShareExperienceImpl @Inject constructor(
    private val context: Context
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

    // TODO("This hardcoded text should be moved")
    fun generateShareString(url: String): String {
        val description = "Coba nonton video ini ya di Tokopedia Play!"
        return "${data.title}.\n$description\n$url"
    }

    private fun getShareTextContent(): String {
        val description = "Coba nonton video ini ya di Tokopedia Play!"
        return "${data.title}.\n$description"
    }

    private fun generateOgTitle(): String {
        return "Tonton ${data.partnerName} di Tokopedia Play"
    }

    private fun generateOgDescription(): String {
        return "Aku punya obat anti-bosen buatmu. Ayo nonton ${data.partnerName} di Tokopedia Play!"
    }

    private fun generateDeepLinkPath(): String = "play/${data.id}"

    override fun createUrl(listener: PlayShareExperience.Listener) {
        // TODO("should check branch url active/not first?")
        try {
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
                isThrowOnError = true

                feature = shareModel.feature
                channel = shareModel.channel
                campaign = shareModel.campaign

                ogTitle = generateOgTitle()
                ogDescription = generateOgDescription()
                if(shareModel.ogImgUrl?.isEmpty() == false) {
                    ogImageUrl = shareModel.ogImgUrl
                }
            }

            val linkerShareData = LinkerShareData()
            linkerShareData.linkerData = linkerData
            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0, linkerShareData, object:
                ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    Log.d("<LOG>", "LinkerShareData shareContents : ${linkerShareData?.shareContents}")
                    listener.onUrlCreated(linkerShareData, shareModel, generateShareString(linkerShareData?.url ?: data.redirectUrl))
                }

                override fun onError(linkerError: LinkerError?) {
                    Log.d("<LOG>", "LinkerShareData Error : $linkerError")
                    listener.onError(Exception(linkerError?.errorMessage))
                }
            }))
        }
        catch (e: Exception) {
            Log.d("<LOG>", "Catch Error : ${e.message}")
            Log.d("<LOG>", "Catch Error : ${e.localizedMessage}")
            listener.onError(e)
        }
    }

    override fun isScreenshotBottomSheet(): Boolean =
        UniversalShareBottomSheet.getShareBottomSheetType() == UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET
}