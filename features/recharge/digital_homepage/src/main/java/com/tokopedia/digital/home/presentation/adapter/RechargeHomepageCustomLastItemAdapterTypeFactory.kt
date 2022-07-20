package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.databinding.ContentRechargeHomepageLastItemBinding
import com.tokopedia.digital.home.model.RechargeHomepageProductCardCustomLastItemModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.RechargeHomepageLastItemViewHolder
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardAdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder

class RechargeHomepageCustomLastItemAdapterTypeFactory(
    private val rechargeHomepageListener: RechargeHomepageItemListener,
    digitalUnifyCardCardListener: DigitalUnifyCardViewHolder.DigitalUnifyCardListener,
) : DigitalUnifyCardAdapterTypeFactory(digitalUnifyCardCardListener) {

    fun type(item: RechargeHomepageProductCardCustomLastItemModel.LastItem): Int =
        RechargeHomepageLastItemViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> =
        when (type) {
            RechargeHomepageLastItemViewHolder.LAYOUT -> {
                val binding = ContentRechargeHomepageLastItemBinding.inflate(
                    LayoutInflater.from(parent?.context),
                    parent as ViewGroup?,
                    false
                )
                RechargeHomepageLastItemViewHolder(binding, rechargeHomepageListener)
            }
            else -> super.createViewHolder(parent, type)
        }

}