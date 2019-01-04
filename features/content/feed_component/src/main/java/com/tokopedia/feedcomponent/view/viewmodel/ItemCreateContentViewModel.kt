package com.tokopedia.feedcomponent.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

/**
 * @author by yfsx on 04/12/18.
 */
class ItemCreateContentViewModel (
        val profileUrl: String = "",
        val title: String = "",
        val desc: String = "",
        val btnSeeProfileText: String = "",
        val btnCreateContentText: String = ""

): Visitable<DynamicFeedTypeFactory>{
    override fun type(typeFactory: DynamicFeedTypeFactory?): Int {
        return 0
    }
}