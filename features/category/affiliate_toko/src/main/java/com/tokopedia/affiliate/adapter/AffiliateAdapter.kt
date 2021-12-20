package com.tokopedia.affiliate.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateDataPlatformShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShimmerModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateStaggeredShimmerModel

class AffiliateAdapter(affiliateAdapterFactory: AffiliateAdapterFactory)
    : BaseAdapter<AffiliateAdapterFactory>(affiliateAdapterFactory) {

    private val shimmerItemCount = 4

    fun addShimmer(isStaggered : Boolean = false){
        for (i in 1..shimmerItemCount) {
            if(isStaggered) addElement(AffiliateStaggeredShimmerModel())
            else addElement(AffiliateShimmerModel())
        }
    }
    fun resetList(){
        this.visitables.clear()
        notifyDataSetChanged()
    }

    fun removeShimmer(listSize: Int) {
        if(itemCount >= (listSize + (shimmerItemCount - 1))) {
            for(i in (shimmerItemCount - 1) downTo 0){
                this.visitables.removeAt(listSize + i)
            }
            notifyItemRangeRemoved(listSize,shimmerItemCount)
        }
    }

    fun addDataPlatformShimmer() {
        addElement(AffiliateDataPlatformShimmerModel())
        for (i in 1..shimmerItemCount) {
             addElement(AffiliateShimmerModel())
        }
    }
}
