package com.tokopedia.play.util.share

import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.universal_sharing.view.model.ShareModel
import kotlin.Exception

/**
 * Created By : Jonathan Darwin on December 10, 2021
 */
interface PlayShareExperience {

    fun isCustomSharingAllow(): Boolean

    fun setData(data: PlayShareExperienceData): PlayShareExperience

    fun setShareModel(shareModel: ShareModel): PlayShareExperience

    fun createUrl(listener: Listener)

    fun isScreenshotBottomSheet(): Boolean

    interface Listener {
        fun onUrlCreated(linkerShareData: LinkerShareResult?, shareModel: ShareModel, shareString: String)
        fun onError(e: Exception)
    }
}