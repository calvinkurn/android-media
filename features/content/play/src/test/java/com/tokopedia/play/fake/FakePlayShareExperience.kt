package com.tokopedia.play.fake

import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.util.share.PlayShareExperienceData
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created By : Jonathan Darwin on December 16, 2021
 */
class FakePlayShareExperience: PlayShareExperience {

    private var isCustomSharingAllow = true
    private var isScreenshotBottomSheet = true
    private var isThrow = false

    private lateinit var shareModel: ShareModel
    private lateinit var data: PlayShareExperienceData


    fun setCustomSharingAllow(isCustomSharingAllow: Boolean) {
        this.isCustomSharingAllow = isCustomSharingAllow
    }

    fun setScreenshotBottomSheet(isScreenshotBottomSheet: Boolean) {
        this.isScreenshotBottomSheet = isScreenshotBottomSheet
    }

    fun setThrowException(isThrow: Boolean) {
        this.isThrow = isThrow
    }

    override fun isCustomSharingAllow(): Boolean = isCustomSharingAllow

    override fun setData(data: PlayShareExperienceData): PlayShareExperience {
        this.data = data
        return this
    }

    override fun setShareModel(shareModel: ShareModel): PlayShareExperience {
        this.shareModel = shareModel
        return this
    }

    override fun createUrl(listener: PlayShareExperience.Listener) {
        if(isThrow)
            listener.onError(Exception("Something went wrong"))
        else
            listener.onUrlCreated(null, shareModel, "")
    }

    override fun isScreenshotBottomSheet(): Boolean = isScreenshotBottomSheet
}