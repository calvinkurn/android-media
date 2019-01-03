package com.tokopedia.feedcomponent.view.viewmodel.banner

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.post.DynamicPostTypeFactory

/**
 * @author by milhamj on 08/05/18.
 */

class BannerViewModel(val itemViewModels: List<BannerItemViewModel>)
    : Visitable<DynamicPostTypeFactory> {

    override fun type(typeFactory: DynamicPostTypeFactory): Int {
        return typeFactory.type(this)
    }
}
