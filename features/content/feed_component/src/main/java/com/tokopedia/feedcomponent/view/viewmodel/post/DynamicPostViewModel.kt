package com.tokopedia.feedcomponent.view.viewmodel.post

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicPostTypeFactory
import com.tokopedia.feedcomponent.view.viewmodel.CardTitle

/**
 * @author by milhamj on 28/11/18.
 */
data class DynamicPostViewModel(
        val title: CardTitle = CardTitle(),
        val authorImage: String = "",
        val authorTitle: String = "",
        val authorSubtitle: String = "",
        val actionTitle: String = "",
        val caption: String = "",
        val footerActionLink: String = "",
        val footerActionText: String = "",
        val shareText: String = "",
        var likeCountNumber: Int = 0,
        var likeCountText: String = "",
        var commentCountNumber: Int = 0,
        var commentCountText: String = "",
        var isLiked: Boolean = false,
        var contentList: MutableList<BasePostViewModel>

) : Visitable<DynamicPostTypeFactory> {
    
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}