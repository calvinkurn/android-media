package com.tokopedia.rechargegeneral.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.rechargegeneral.model.RechargeGeneralInput
import com.tokopedia.rechargegeneral.model.RechargeGeneralItemData
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.OnInputListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.rechargegeneral.R


/**
 * @author by resakemal on 12/08/19
 */

class RechargeGeneralAdapterFactory(val listener: OnInputListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent.layoutParams = layoutParams
        return when (type) {
            R.layout.view_digital_product_input_holder -> RechargeGeneralProductInputViewHolder(parent, listener)
            R.layout.view_digital_product_select_holder -> RechargeGeneralProductSelectViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(rechargeGeneralInput: RechargeGeneralInput): Int {
        return R.layout.view_digital_product_input_holder
    }

    fun type(rechargeGeneralItemData: RechargeGeneralItemData): Int {
        return R.layout.view_digital_product_select_holder
    }

}