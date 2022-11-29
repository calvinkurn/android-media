package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.data.Section
import com.tokopedia.smartbills.databinding.ViewSmartBillsItemAccordionBinding
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.ACTION_TYPE
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment.Companion.PAID_TYPE
import com.tokopedia.smartbills.util.RechargeSmartBillsAccordionView.getAccordionwithAction
import com.tokopedia.smartbills.util.RechargeSmartBillsAccordionView.getAccordionwithPaid
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.addAccordionData
import com.tokopedia.utils.view.binding.viewBinding

class SmartBillsAccordionViewHolder(
    view: View,
    private val checkableListener: BaseCheckableViewHolder.CheckableInteractionListener,
    private val detailListener: SmartBillsViewHolder.DetailListener,
    private val accordionListener: SBMAccordionListener
) : AbstractViewHolder<Section>(view) {

    private val binding: ViewSmartBillsItemAccordionBinding? by viewBinding()

    override fun bind(element: Section) {
        binding?.run {
            if (element.type == ACTION_TYPE) {
                Handler().run {
                    postDelayed(
                        {
                            accordionSmartBills.expandGroup(0)
                        },
                        SmartBillsFragment.EXPAND_ACCORDION_START_DELAY
                    )
                }
                smartBillsViewMargin.show()
            }

            val view = when (element.type) {
                ACTION_TYPE -> getAccordionwithAction(
                    itemView,
                    element,
                    checkableListener,
                    detailListener,
                    accordionListener,
                    ACTION_TYPE
                )
                PAID_TYPE -> getAccordionwithPaid(itemView, element, checkableListener, detailListener, PAID_TYPE)
                else -> getAccordionwithPaid(itemView, element, checkableListener, detailListener, PAID_TYPE)
            }
            accordionSmartBills.apply {
                accordionData.clear()
                removeAllViews()
                addGroup(addAccordionData(view, element))
                onItemClick = { position, isExpanded ->
                    if (isExpanded) {
                        accordionListener.onExpandAccordion(element.title)
                    } else {
                        accordionListener.onCollapseAccordion(element.title)
                    }
                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_item_accordion
    }

    interface SBMAccordionListener {
        fun onRefreshAccordion(titleAccordion: String)
        fun onCollapseAccordion(titleAccordion: String)
        fun onExpandAccordion(titleAccordion: String)
    }
}
