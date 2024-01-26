package com.tokopedia.catalogcommon.listener

import com.tokopedia.catalogcommon.uimodel.VideoUiModel

interface VideoListener {

    fun onClickVideoExpert()

    fun onVideoImpression(itemHasSaw: List<VideoUiModel.ItemVideoUiModel>, widgetName: String)

}
