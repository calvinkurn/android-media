package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 20/05/20
 */

data class PostListDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = false,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val emphasizeType: Int = IMAGE_EMPHASIZED,
    val postPagers: List<PostListPagerUiModel> = emptyList(),
    val cta: PostCtaDataUiModel = PostCtaDataUiModel()
) : BaseDataUiModel, LastUpdatedDataInterface {

    companion object {
        const val IMAGE_EMPHASIZED = 0
        const val TEXT_EMPHASIZED = 1
    }

    override fun isWidgetEmpty(): Boolean {
        return postPagers.isEmpty()
    }
}