package com.tokopedia.feedcomponent.view.viewmodel.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title
import com.tokopedia.feedcomponent.data.pojo.template.Template
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

class TopAdsBannerModel(var topAdsBannerList: ArrayList<TopAdsImageViewModel> = ArrayList(),
                        var title: Title = Title(),
                        var template: Template = Template()) : Visitable<DynamicFeedTypeFactory> {

    override fun type(typeFactory: DynamicFeedTypeFactory): Int {
        return typeFactory.type(this)
    }
}
