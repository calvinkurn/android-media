package com.tokopedia.exploreCategory.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.adapter.ECServiceTypeFactory

class ECShimmerVHViewModel : Visitable<ECServiceTypeFactory> {

    override fun type(typeFactory: ECServiceTypeFactory): Int {
        return typeFactory.type(this)
    }

}