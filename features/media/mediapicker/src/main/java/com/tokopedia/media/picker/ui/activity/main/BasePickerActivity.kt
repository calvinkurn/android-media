package com.tokopedia.media.picker.ui.activity.main

import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.media.R
import com.tokopedia.media.common.uimodel.MediaUiModel
import com.tokopedia.media.picker.ui.PickerUiConfig
import com.tokopedia.media.picker.ui.uimodel.hasVideoBy

abstract class BasePickerActivity : BaseActivity(), PickerActivityListener {

    protected val param by lazy { PickerUiConfig.pickerParam() }
    protected val medias = arrayListOf<MediaUiModel>()

    abstract fun onShowToaster(messageId: Int, param: Number)

    override fun mediaSelected(): List<MediaUiModel> {
        return medias
    }

    override fun hasVideoLimitReached(): Boolean {
        return medias.hasVideoBy(param.maxVideoCount())
    }

    override fun hasMediaLimitReached(): Boolean {
        return medias.size == param.maxMediaAmount()
    }

    override fun isMinVideoDuration(model: MediaUiModel): Boolean {
        return model.getVideoDuration(applicationContext) <= param.minVideoDuration()
    }

    override fun isMaxVideoDuration(model: MediaUiModel): Boolean {
        return model.getVideoDuration(applicationContext) > param.maxVideoDuration()
    }

    override fun isMaxVideoSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.maxVideoSize())
    }

    override fun isMinImageResolution(model: MediaUiModel): Boolean {
        return model.isMinImageRes(param.minImageResolution())
    }

    override fun isMaxImageResolution(model: MediaUiModel): Boolean {
        return model.isMaxImageRes(param.maxImageResolution())
    }

    override fun isMaxImageSize(model: MediaUiModel): Boolean {
        return model.isMaxFileSize(param.maxImageSize())
    }

    override fun onShowMediaLimitReachedToast() {
        onShowToaster(R.string.picker_selection_limit_message, param.maxMediaAmount())
    }

    override fun onShowVideoLimitReachedToast() {
        onShowToaster(R.string.picker_selection_limit_video, param.maxVideoCount())
    }

    override fun onShowVideoMinDurationToast() {
        onShowToaster(R.string.picker_video_duration_min_limit, param.minVideoDuration())
    }

    override fun onShowVideoMaxDurationToast() {
        onShowToaster(R.string.picker_video_duration_max_limit, param.maxVideoDuration())
    }

    override fun onShowVideoMaxFileSizeToast() {
        onShowToaster(R.string.picker_video_max_size, param.maxVideoSize())
    }

    override fun onShowImageMinResToast() {
        onShowToaster(R.string.picker_image_res_min_limit, param.maxImageResolution())
    }

    override fun onShowImageMaxResToast() {
        onShowToaster(R.string.picker_image_res_max_limit, param.minImageResolution())
    }

    override fun onShowImageMaxFileSizeToast() {
        onShowToaster(R.string.picker_image_max_size, param.maxImageSize())
    }

}