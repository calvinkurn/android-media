package com.tokopedia.kolcomponent.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kolcomponent.view.adapter.post.DynamicPostTypeFactory

/**
 * @author by yfsx on 04/12/18.
 */
class ItemCreateContentViewModel (
        val profileUrl: String = "",
        val title: String = "",
        val desc: String = "",
        val btnSeeProfileText: String = "",
        val btnCreateContentText: String = ""

): Visitable<DynamicPostTypeFactory>{
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return 0
    }
}