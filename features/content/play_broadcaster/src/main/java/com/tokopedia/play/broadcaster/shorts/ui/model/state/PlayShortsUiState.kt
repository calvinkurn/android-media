package com.tokopedia.play.broadcaster.shorts.ui.model.state

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel
import com.tokopedia.content.common.ui.model.ContentAccountUiModel

/**
 * Created By : Jonathan Darwin on November 08, 2022
 */
data class PlayShortsUiState(
    val shortsId: String,
    val mediaUri: String,
    val accountList: List<ContentAccountUiModel>,
    val selectedAccount: ContentAccountUiModel,
)

data class PlayShortsFormUiState(
    val title: String,
    val products: List<ProductUiModel>,
)
