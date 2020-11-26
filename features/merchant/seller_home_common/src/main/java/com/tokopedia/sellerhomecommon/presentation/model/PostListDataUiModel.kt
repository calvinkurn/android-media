package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class PostListDataUiModel(
        override val dataKey: String = "",
        val items: List<PostUiModel> = emptyList(),
        override var error: String = ""
): BaseDataUiModel