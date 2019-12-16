package com.tokopedia.rechargegeneral.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralProductItemData
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.OnInputListener
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder


/**
 * @author by resakemal on 12/08/19
 */

class RechargeGeneralAdapterFactory(val listener: OnInputListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent.layoutParams = layoutParams
        return when (type) {
            R.layout.view_digital_product_input_holder -> RechargeGeneralInputViewHolder(parent, listener)
            R.layout.view_digital_product_select_holder -> RechargeGeneralProductSelectViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(item: RechargeGeneralProductInput): Int {
        return R.layout.view_digital_product_input_holder
    }

    fun type(item: RechargeGeneralProductItemData): Int {
        return R.layout.view_digital_product_select_holder
    }

}