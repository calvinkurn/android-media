package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener

class DigitalHomePageCategoryViewHolder(itemView : View?, val onItemBindListener: OnItemBindListener) : AbstractViewHolder<DigitalHomePageCategoryModel>(itemView) {

    private val adapterCategory  by lazy {
        DigitalItemCategoryAdapter()
    }

    override fun bind(element: DigitalHomePageCategoryModel?) {
        if(element?.isLoaded?:false){

        }else{
            onItemBindListener.onCategoryItemDigitalBind()
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_digital_home_category
    }
}