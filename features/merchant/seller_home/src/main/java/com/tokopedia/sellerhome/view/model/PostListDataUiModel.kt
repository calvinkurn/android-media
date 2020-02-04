package com.tokopedia.sellerhome.view.model

data class PostListDataUiModel(
        val dataKey: String = "",
        val items: List<PostUiModel> = emptyList(),
        val error: String
): BaseDataUiModel