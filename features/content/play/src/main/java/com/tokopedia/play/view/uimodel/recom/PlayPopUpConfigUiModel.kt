package com.tokopedia.play.view.uimodel.recom

/**
 * @author by astidhiyaa on 01/11/22
 */
data class PlayPopUpConfigUiModel(
    val isEnabled: Boolean = false,
    val duration: Long = 0,
    val text: String = "",
){
    companion object {
        val Empty: PlayPopUpConfigUiModel
            get() = PlayPopUpConfigUiModel(
                isEnabled = false,
                duration = 0,
                text = "",
            )
    }
}
