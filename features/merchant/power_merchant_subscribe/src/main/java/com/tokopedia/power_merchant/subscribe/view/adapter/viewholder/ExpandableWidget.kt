package com.tokopedia.power_merchant.subscribe.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactory
import com.tokopedia.power_merchant.subscribe.view.adapter.ExpandableAdapterFactoryImpl
import com.tokopedia.power_merchant.subscribe.view.model.WidgetExpandableUiModel
import kotlinx.android.synthetic.main.widget_pm_expandable.view.*

/**
 * Created By @ilhamsuaib on 04/03/21
 */

class ExpandableWidget(itemView: View) : AbstractViewHolder<WidgetExpandableUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.widget_pm_expandable
    }

    override fun bind(element: WidgetExpandableUiModel) {
        with(itemView) {
            setupExpandableItem(element)

            tvPmExpandableTitle.text = element.title
            tvPmExpandableTitle.setOnClickListener {
                handleExpandableView()
            }
            icPmExpandableTitleIcon.setOnClickListener {
                handleExpandableView()
            }
        }
    }

    private fun handleExpandableView() {
        with(itemView) {
            val isExpanded = rvPmExpandableItem.visibility == View.VISIBLE
            if (isExpanded) {
                icPmExpandableTitleIcon.setImage(IconUnify.CHEVRON_RIGHT)
                rvPmExpandableItem.gone()
                viewPmExpandableHorLine.gone()
            } else {
                icPmExpandableTitleIcon.setImage(IconUnify.CHEVRON_DOWN)
                rvPmExpandableItem.visible()
                viewPmExpandableHorLine.visible()
            }
        }
    }

    private fun setupExpandableItem(element: WidgetExpandableUiModel) {
        val expandableAdapter = BaseListAdapter<Visitable<ExpandableAdapterFactory>, ExpandableAdapterFactoryImpl>(ExpandableAdapterFactoryImpl())

        with(itemView.rvPmExpandableItem) {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
            adapter = expandableAdapter
        }

        expandableAdapter.data.clear()
        expandableAdapter.data.addAll(element.items)
        expandableAdapter.notifyDataSetChanged()
    }
}