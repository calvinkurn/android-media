package com.tokopedia.exploreCategory.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.adapter.AffiliateAdapterTypeFactory

class AffiliateProductShimmerVHViewModel : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}