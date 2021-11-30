package com.tokopedia.recharge_component.digital_card.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recharge_component.databinding.ItemDigitalUnificationCardBinding
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnificationViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnificationModel

class DigitalUnificationCardAdapterTypeFactory(
    private val digitalUnificationCardListener: DigitalUnificationViewHolder.DigitalUnificationCardListener
) : BaseAdapterTypeFactory(), DigitalUnificationCardTypeFactory {

    override fun type(digitalUnificationModel: DigitalUnificationModel): Int =
        DigitalUnificationViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> =
        when (type) {
            DigitalUnificationViewHolder.LAYOUT -> {
                val binding = ItemDigitalUnificationCardBinding.inflate(
                    LayoutInflater.from(parent?.context),
                    parent as ViewGroup?,
                    false
                )
                DigitalUnificationViewHolder(binding, digitalUnificationCardListener)
            }
            else -> super.createViewHolder(parent, type)
        }

}