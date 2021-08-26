package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.adapter.ItemPMPotentialPMProListener
import com.tokopedia.shop.score.performance.presentation.adapter.ItemProtectedParameterAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ProtectedParameterListener
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel
import kotlinx.android.synthetic.main.item_faq_shop_score.view.*
import kotlinx.android.synthetic.main.item_parameter_protected_section.view.*

class ItemProtectedParameterSectionViewHolder(
    view: View,
    private val protectedParameterListener: ProtectedParameterListener
) : AbstractViewHolder<ProtectedParameterSectionUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_parameter_protected_section
    }

    private var itemProtectedParameterAdapter: ItemProtectedParameterAdapter? = null

    override fun bind(element: ProtectedParameterSectionUiModel?) {
        with(itemView) {
            cardDescParameterRelief?.setOnClickListener {
                protectedParameterListener.onProtectedParameterChevronClicked(
                    element?.protectedParameterDate.orEmpty()
                )
            }
            icDescParameterRelief?.setOnClickListener {
                protectedParameterListener.onProtectedParameterChevronClicked(
                    element?.protectedParameterDate.orEmpty()
                )
            }
        }
        setAdapterProtectedParameter(element)
    }

    private fun setAdapterProtectedParameter(data: ProtectedParameterSectionUiModel?) {
        with(itemView) {
            itemProtectedParameterAdapter = ItemProtectedParameterAdapter()
            rv_shop_score_parameter_faq?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = itemProtectedParameterAdapter
                isNestedScrollingEnabled = false
            }
            itemProtectedParameterAdapter?.setProtectedParameterList(data?.itemProtectedParameterList)
        }
    }
}