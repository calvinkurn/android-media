package com.tokopedia.feedcomponent.view.viewmodel.relatedpost

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.relatedpost.RelatedPostTypeFactory

/**
 * @author by milhamj on 2019-08-12.
 */
data class RelatedPostViewModel @JvmOverloads constructor(
        val relatedPostList: List<RelatedPostItemViewModel>, var title: String? = "")
    : Visitable<RelatedPostTypeFactory> {
    override fun type(typeFactory: RelatedPostTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}