package com.tokopedia.digital.home.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

class DigitalHomePageBannerViewHolder(val itemView : View?, val onItemBindListener: OnItemBindListener) : AbstractViewHolder<DigitalHomePageBannerModel>(itemView) {

    private val adapterBanner  by lazy {
        DigitalItemBannerAdapter()
    }

    override fun bind(element: DigitalHomePageBannerModel?) {
        if(element?.isLoaded?:false){

        }else{
            onItemBindListener.onBannerItemDigitalBind()
        }
    }

    companion object{
        val LAYOUT =  R.layout.layout_digital_home_banner
    }
}