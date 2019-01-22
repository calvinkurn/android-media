package com.tokopedia.feedcomponent.view.viewmodel.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory

/**
 * @author by milhamj on 08/05/18.
 */

data class BannerViewModel(val itemViewModels: MutableList<BannerItemViewModel> = ArrayList(),
                           val title: Title = Title(),
                           val template: Template = Template())
    : Visitable<DynamicFeedTypeFactory> {

    override fun type(typeFactory: DynamicFeedTypeFactory): Int {
        return typeFactory.type(this)
    }
}
