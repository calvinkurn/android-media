package com.tokopedia.exploreCategory.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateProductShimmerVHViewModel

class AffiliateAdapter(affiliateAdapterFactory: AffiliateAdapterFactory)
    : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory) {

    fun startShimmer(){
        this.visitables.clear()
        addElement(AffiliateProductShimmerVHViewModel())
        addElement(AffiliateProductShimmerVHViewModel())
        addElement(AffiliateProductShimmerVHViewModel())
        addElement(AffiliateProductShimmerVHViewModel())
    }

    fun stopShimmer(){
        this.visitables.clear()
    }
}