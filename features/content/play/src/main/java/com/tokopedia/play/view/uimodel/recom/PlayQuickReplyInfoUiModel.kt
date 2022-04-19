package com.tokopedia.play.view.uimodel.recom

/**
 * Created by jegul on 21/01/21
 */
data class PlayQuickReplyInfoUiModel(
        val quickReplyList: List<String>
) {
        companion object {
                val Empty: PlayQuickReplyInfoUiModel
                        get() = PlayQuickReplyInfoUiModel(
                                quickReplyList = emptyList()
                        )
        }
}