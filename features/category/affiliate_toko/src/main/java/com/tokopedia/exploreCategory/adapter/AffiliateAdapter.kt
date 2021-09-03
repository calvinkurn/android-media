package com.tokopedia.exploreCategory.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShimmerVHViewModel

class AffiliateAdapter(affiliateAdapterFactory: AffiliateAdapterFactory)
    : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory) {

    fun startShimmer(){
        this.visitables.clear()
        addElement(AffiliateShimmerVHViewModel())
        addElement(AffiliateShimmerVHViewModel())
        addElement(AffiliateShimmerVHViewModel())
        addElement(AffiliateShimmerVHViewModel())
    }

    fun stopShimmer(){
        this.visitables.clear()
    }
}
