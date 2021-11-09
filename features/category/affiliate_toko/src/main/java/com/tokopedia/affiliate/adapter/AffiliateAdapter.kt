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
    fun addShimmer(){
        addElement(AffiliateShimmerModel())
        addElement(AffiliateShimmerModel())
        addElement(AffiliateShimmerModel())
        addElement(AffiliateShimmerModel())
    }
    fun resetList(){
        this.visitables.clear()
    }

    fun removeShimmer(listSize: Int) {
        if(itemCount >= listSize+3) {
            this.visitables.removeAt(listSize + 3)
            this.visitables.removeAt(listSize + 2)
            this.visitables.removeAt(listSize + 1)
            this.visitables.removeAt(listSize)
            notifyItemRangeRemoved(listSize,4)
        }
    }

}
