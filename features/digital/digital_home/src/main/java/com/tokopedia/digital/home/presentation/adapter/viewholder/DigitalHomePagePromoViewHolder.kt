package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePagePromoModel
import com.tokopedia.digital.home.presentation.adapter.adapter.DigitalItemPromoAdapter
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

class DigitalHomePagePromoViewHolder(itemView : View?, val onItemBindListener: OnItemBindListener) : AbstractViewHolder<DigitalHomePagePromoModel>(itemView) {

    private val adapterPromo by lazy {
        DigitalItemPromoAdapter()
    }

    override fun bind(element: DigitalHomePagePromoModel?) {
        if(element?.isLoaded?:false){

        }else{
            onItemBindListener.onPromoItemDigitalBind()
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_digital_home_promo
    }
}