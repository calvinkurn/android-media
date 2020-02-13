package com.tokopedia.sellerhome.view.model

data class PostListDataUiModel(
        val dataKey: String = "",
        val items: List<PostUiModel> = emptyList(),
        override var error: String = ""
): BaseDataUiModel