package com.tokopedia.affiliate.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel

class AffiliateAdapter(affiliateAdapterFactory: AffiliateAdapterFactory)
    : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory) {

    fun startShimmer(){
        this.visitables.clear()
        addElement(AffiliateShimmerModel())
        addElement(AffiliateShimmerModel())
        addElement(AffiliateShimmerModel())
        addElement(AffiliateShimmerModel())
    }

    fun stopShimmer(){
        this.visitables.clear()
    }
}
