package com.tokopedia.recharge_component.digital_card.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.recharge_component.databinding.ItemDigitalUnifyCardBinding
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel

open class DigitalUnifyCardAdapterTypeFactory(
    private val digitalUnifyCardCardListener: DigitalUnifyCardViewHolder.DigitalUnifyCardListener
) : BaseAdapterTypeFactory(), DigitalUnifyCardTypeFactory {

    override fun type(digitalUnifyModel: DigitalUnifyModel): Int =
        DigitalUnifyCardViewHolder.LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> =
        when (type) {
            DigitalUnifyCardViewHolder.LAYOUT -> {
                val binding = ItemDigitalUnifyCardBinding.inflate(
                    LayoutInflater.from(parent?.context),
                    parent as ViewGroup?,
                    false
                )
                DigitalUnifyCardViewHolder(binding, digitalUnifyCardCardListener)
            }
            else -> super.createViewHolder(parent, type)
        }

}