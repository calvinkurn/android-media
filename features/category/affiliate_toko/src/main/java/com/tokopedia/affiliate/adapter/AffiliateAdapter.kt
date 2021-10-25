package com.tokopedia.affiliate.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredShimmerModel

class AffiliateAdapter(affiliateAdapterFactory: AffiliateAdapterFactory)
    : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory) {

    fun startShimmer(isStaggered : Boolean = false){
        // TODO Optimize
       if (isStaggered){
           this.visitables.clear()
           addElement(AffiliateStaggeredShimmerModel())
           addElement(AffiliateStaggeredShimmerModel())
           addElement(AffiliateStaggeredShimmerModel())
           addElement(AffiliateStaggeredShimmerModel())
       }else {
           this.visitables.clear()
           addElement(AffiliateShimmerModel())
           addElement(AffiliateShimmerModel())
           addElement(AffiliateShimmerModel())
           addElement(AffiliateShimmerModel())
       }
    }

    fun stopShimmer(){
        this.visitables.clear()
    }

}
