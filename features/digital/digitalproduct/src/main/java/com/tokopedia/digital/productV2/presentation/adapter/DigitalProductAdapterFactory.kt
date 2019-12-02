package com.tokopedia.digital.productV2.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.productV2.model.DigitalProductInput
import com.tokopedia.digital.productV2.model.DigitalProductItemData
import com.tokopedia.digital.productV2.presentation.adapter.viewholder.DigitalProductInputViewHolder
import com.tokopedia.digital.productV2.presentation.adapter.viewholder.DigitalProductSelectViewHolder
import com.tokopedia.digital.productV2.presentation.adapter.viewholder.OnInputListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * @author by resakemal on 12/08/19
 */

class DigitalProductAdapterFactory(val listener: OnInputListener): BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        val layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent.layoutParams = layoutParams
        return when (type) {
            1 -> DigitalProductInputViewHolder(parent, listener)
            2 -> DigitalProductSelectViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(digitalProductInput: DigitalProductInput): Int {
        return 1
    }

    fun type(digitalProductItemData: DigitalProductItemData): Int {
        return 2
    }

}