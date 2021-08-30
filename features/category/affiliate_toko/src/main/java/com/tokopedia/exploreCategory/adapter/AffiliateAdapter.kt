package com.tokopedia.exploreCategory.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShareVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.AffiliateShimmerVHViewModel
import com.tokopedia.iconunify.IconUnify

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

    fun addShareOptions(){
        this.visitables.clear()
        addElement(AffiliateShareVHViewModel("Instagram", IconUnify.INSTAGRAM))
        addElement(AffiliateShareVHViewModel("TikTok", IconUnify.TIKTOK))
        addElement(AffiliateShareVHViewModel("Youtube", IconUnify.YOUTUBE))
        addElement(AffiliateShareVHViewModel("Facebook", IconUnify.FACEBOOK))
        addElement(AffiliateShareVHViewModel("Twitter", IconUnify.TWITTER))
        addElement(AffiliateShareVHViewModel("Blog", IconUnify.GLOBE))
        addElement(AffiliateShareVHViewModel("Whatsapp", IconUnify.WHATSAPP))
        addElement(AffiliateShareVHViewModel("Line", IconUnify.LINE))
        addElement(AffiliateShareVHViewModel("Lainnya", null))
    }
}
