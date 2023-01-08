package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.PostListAdapterTypeFactory
import java.util.Date

/**
 * Created By @ilhamsuaib on 20/05/20
 */

sealed class PostItemUiModel(
    open val title: String = String.EMPTY,
    open val appLink: String = String.EMPTY,
    open val url: String = String.EMPTY,
    open val featuredMediaUrl: String = String.EMPTY,
    open val subtitle: String = String.EMPTY,
    open val textEmphasizeType: Int = PostListDataUiModel.IMAGE_EMPHASIZED,
    open val isPinned: Boolean = false,
    open var isChecked: Boolean = false,
    open val postItemId: String = String.EMPTY
) : Visitable<PostListAdapterTypeFactory> {

    data class PostImageEmphasizedUiModel(
        override val title: String = String.EMPTY,
        override val appLink: String = String.EMPTY,
        override val url: String = String.EMPTY,
        override val featuredMediaUrl: String = String.EMPTY,
        override val subtitle: String = String.EMPTY,
        override val textEmphasizeType: Int = PostListDataUiModel.IMAGE_EMPHASIZED,
        override val isPinned: Boolean = false,
        override var isChecked: Boolean = false,
        override var postItemId: String = String.EMPTY,
        val countdownDate: Date? = null
    ) : PostItemUiModel(
        title,
        appLink,
        url,
        featuredMediaUrl,
        subtitle,
        textEmphasizeType,
        isPinned,
        isChecked,
        postItemId,
    ) {

        override fun type(typeFactory: PostListAdapterTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class PostTextEmphasizedUiModel(
        override val title: String = String.EMPTY,
        override val appLink: String = String.EMPTY,
        override val url: String = String.EMPTY,
        override val featuredMediaUrl: String = String.EMPTY,
        override val subtitle: String = String.EMPTY,
        override val textEmphasizeType: Int = PostListDataUiModel.TEXT_EMPHASIZED,
        override val isPinned: Boolean = false,
        override var isChecked: Boolean = false,
        override var postItemId: String = String.EMPTY,
        val stateMediaUrl: String = String.EMPTY,
        val stateText: String = String.EMPTY,
        val shouldShowUnderLine: Boolean = false
    ) : PostItemUiModel(
        title,
        appLink,
        url,
        featuredMediaUrl,
        subtitle,
        textEmphasizeType,
        isPinned,
        isChecked,
        postItemId,
    ) {

        override fun type(typeFactory: PostListAdapterTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class PostTimerDismissalUiModel(
        val totalDeletedItems: Int = Int.ZERO,
        var runningTimeInMillis: Long = Int.ZERO.toLong()
    ) : PostItemUiModel() {

        override fun type(typeFactory: PostListAdapterTypeFactory): Int {
            return typeFactory.type(this)
        }
    }
}
