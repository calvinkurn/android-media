package com.tokopedia.feedcomponent.view.viewmodel.post

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicPostTypeFactory

/**
 * @author by milhamj on 28/11/18.
 */
data class DynamicPostViewModel(
        val title: Title = Title(),
        val header: Header = Header(),
        val footer: Footer = Footer(),
        var contentList: MutableList<BasePostViewModel> = ArrayList(),
        val template: Template = Template()

) : Visitable<DynamicPostTypeFactory> {
    
    override fun type(typeFactory: DynamicPostTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}