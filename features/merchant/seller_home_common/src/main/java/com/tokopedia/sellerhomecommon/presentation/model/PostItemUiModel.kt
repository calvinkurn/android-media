package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.PostListAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 20/05/20
 */

sealed class PostItemUiModel(
        open val title: String = "",
        open val appLink: String = "",
        open val url: String = "",
        open val featuredMediaUrl: String = "",
        open val subtitle: String = ""
) : Visitable<PostListAdapterTypeFactory> {

    data class PostImageEmphasizedUiModel(
            override val title: String = "",
            override val appLink: String = "",
            override val url: String = "",
            override val featuredMediaUrl: String = "",
            override val subtitle: String = ""
    ) : PostItemUiModel(title, appLink, url, featuredMediaUrl, subtitle) {

        override fun type(typeFactory: PostListAdapterTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class PostTextEmphasizedUiModel(
            override val title: String = "",
            override val appLink: String = "",
            override val url: String = "",
            override val featuredMediaUrl: String = "",
            override val subtitle: String = "",
            val stateMediaUrl: String = "",
            val stateText: String = "",
            val shouldShowUnderLine: Boolean = false
    ) : PostItemUiModel(title, appLink, url, featuredMediaUrl, subtitle) {

        override fun type(typeFactory: PostListAdapterTypeFactory): Int {
            return typeFactory.type(this)
        }
    }
}