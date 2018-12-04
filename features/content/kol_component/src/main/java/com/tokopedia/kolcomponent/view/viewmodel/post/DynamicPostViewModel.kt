package com.tokopedia.kolcomponent.view.viewmodel.post

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kolcomponent.view.adapter.post.DynamicPostTypeFactory

/**
 * @author by milhamj on 28/11/18.
 */
data class DynamicPostViewModel(
        val metaTitle: String = "",
        val authorImage: String = "",
        val authorTitle: String = "",
        val authorSubtitle: String = "",
        val actionTitle: String = "",
        val caption: String = "",
        val footerActionLink: String = "",
        val footerActionText: String = "",
        val shareText: String = "",
        var likeCount: String = "",
        var commentCount: String = "",
        var isLiked: Boolean = false,
        var contentList: MutableList<Any>

) : Visitable<DynamicPostTypeFactory> {
    
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}