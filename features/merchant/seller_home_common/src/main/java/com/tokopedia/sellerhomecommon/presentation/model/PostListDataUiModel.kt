package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class PostListDataUiModel(
        override var dataKey: String = "",
        override var error: String = "",
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = false,
        val emphasizeType: Int = IMAGE_EMPHASIZED,
        val postPagers: List<PostListPagerUiModel> = emptyList(),
        val cta: PostCtaDataUiModel = PostCtaDataUiModel()
) : BaseDataUiModel {

    companion object {
        const val IMAGE_EMPHASIZED = 0
        const val TEXT_EMPHASIZED = 1
    }

    fun isEmptyPost(): Boolean = postPagers.isEmpty()

    override fun shouldRemove(): Boolean {
        return isEmptyPost()
    }
}