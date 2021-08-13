package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayQuickReplyInfoUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayQuickReplyModelBuilder {

    fun build(
            quickReplyList: List<String> = List(5) { "abc" }
    ) = PlayQuickReplyInfoUiModel(quickReplyList = quickReplyList)
}