package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class PostListDataUiModel(
        override val dataKey: String = "",
        val items: List<PostUiModel> = emptyList(),
        val cta: PostCtaDataUiModel = PostCtaDataUiModel(),
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return items.isEmpty()
    }
}