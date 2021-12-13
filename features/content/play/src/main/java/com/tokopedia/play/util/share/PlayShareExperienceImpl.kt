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
    override fun generateShareString(): String {
        val description = "Coba nonton video ini ya di Tokopedia Play!"
        return "${data.title}.\n$description\n${data.redirectUrl}"
    }

    private fun generateOgTitle(): String {
        return "Tonton ${data.partnerName} di Tokopedia Play"
    }

    private fun generateOgDescription(): String {
        return "Aku punya obat anti-bosen buatmu. Ayo nonton ${data.partnerName} di Tokopedia Play!"
    }

    override fun createUrl(listener: PlayShareExperience.Listener) {
        // TODO("should check branch url active/not first?")
        try {
            val linkerData = LinkerData().apply {
                id = data.id
                name = data.title
                description = generateShareString()
                imgUri = data.coverUrl
                ogUrl = data.redirectUrl
                type = ""
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
                Log.d("<LOG>", "ogImageUrl : ${shareModel.ogImgUrl}")
                isAffiliate = true
            }

            val linkerShareData = LinkerShareData()
            linkerShareData.linkerData = linkerData

            LinkerManager.getInstance().executeShareRequest(LinkerUtils.createShareRequest(0, linkerShareData, object:
                ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    Log.d("<LOG>", "LinkerShareData : $linkerShareData")
                    listener.onUrlCreated(linkerShareData, shareModel, generateShareString())
                }

                override fun onError(linkerError: LinkerError?) {
                    Log.d("<LOG>", "LinkerShareData Error : $linkerError")
                    listener.onError(Exception(linkerError?.errorMessage), generateShareString())
                }
            }))
        }
        catch (e: Exception) {
            Log.d("<LOG>", "Catch Error : ${e.message}")
            Log.d("<LOG>", "Catch Error : ${e.localizedMessage}")
            listener.onError(e, generateShareString())
        }
    }
}