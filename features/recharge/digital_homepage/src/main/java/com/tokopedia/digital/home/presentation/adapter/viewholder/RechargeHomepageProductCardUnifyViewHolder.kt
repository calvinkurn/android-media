package com.tokopedia.digital.home.presentation.adapter.viewholder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewRechargeHomeProductCardsUnifyBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.model.RechargeProductCardUnifyModel
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.digital_card.presentation.adapter.DigitalUnifyCardAdapterTypeFactory
import com.tokopedia.recharge_component.digital_card.presentation.adapter.viewholder.DigitalUnifyCardViewHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel

class RechargeHomepageProductCardUnifyViewHolder(
    private val binding: ViewRechargeHomeProductCardsUnifyBinding,
    private val listener: RechargeHomepageItemListener
) : AbstractViewHolder<RechargeProductCardUnifyModel>(binding.root) {

    private val digitalUnifyCardListener =
        object : DigitalUnifyCardViewHolder.DigitalUnifyCardListener {
            override fun onItemClicked(item: DigitalUnifyModel, index: Int) {
                if (section.items.size > index)
                    listener.onRechargeSectionItemClicked(section.items[index])
            }
        }

    lateinit var section: RechargeHomepageSections.Section

    override fun bind(element: RechargeProductCardUnifyModel) {
        section = element.section
        with(binding) {
            if (section.items.isNotEmpty()) {
                viewRechargeHomeProductCardsUnifyContainer.show()
                viewRechargeHomeProductCardsUnifyShimmering.root.hide()

                rvRechargeHomeProductCardsUnify.layoutManager =
                    LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)

                rvRechargeHomeProductCardsUnify.adapter = BaseAdapter(
                    DigitalUnifyCardAdapterTypeFactory(digitalUnifyCardListener),
                    element.digitalUnifyItems
                )

                tvRechargeHomeProductCardsUnifyTitle.text = section.title
                root.addOnImpressionListener(section) {
                    listener.onRechargeSectionItemImpression(section)
                }

            } else {
                viewRechargeHomeProductCardsUnifyContainer.hide()
                viewRechargeHomeProductCardsUnifyShimmering.root.show()

                listener.loadRechargeSectionData(element.visitableId())
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_recharge_home_product_cards_unify
    }

}