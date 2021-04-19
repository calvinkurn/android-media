package com.tokopedia.home.account.presentation.uimodel

import com.tokopedia.home.account.R

data class MediaQualityUIModel(
        var title: Int = 0,
        var subtitle: Int = 0
) {

    companion object {
        fun settingsMenu(): List<MediaQualityUIModel> {
            return mutableListOf<MediaQualityUIModel>().apply {
                add(MediaQualityUIModel(
                        R.string.image_quality_auto_title,
                        R.string.image_quality_auto_subtitle
                ))
                add(MediaQualityUIModel(
                        R.string.image_quality_low_title,
                        R.string.image_quality_low_subtitle
                ))
                add(MediaQualityUIModel(
                        R.string.image_quality_high_title,
                        R.string.image_quality_high_subtitle
                ))
            }
        }
    }

}