package com.tokopedia.kol.feature.post.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory

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

) : Visitable<KolPostTypeFactory> {
    
    override fun type(typeFactory: KolPostTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}