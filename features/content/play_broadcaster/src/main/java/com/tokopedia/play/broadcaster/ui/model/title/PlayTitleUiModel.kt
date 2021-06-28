package com.tokopedia.play.broadcaster.ui.model.title

/**
 * Created by jegul on 29/03/21
 */
sealed class PlayTitleUiModel {

    object NoTitle : PlayTitleUiModel()
    data class HasTitle(val title: String) : PlayTitleUiModel()
}