package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Html
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPTicketItemPackageAdapter
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import com.tokopedia.entertainment.pdp.widget.WidgetEventPDPExpandable
import kotlinx.android.synthetic.main.custom_event_expandable_parent.view.*
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket.view.*

class PackageParentViewHolder (view: View,
                               private val onBindItemTicketListener: OnBindItemTicketListener,
                               private val onCoachmarkListener: OnCoachmarkListener): AbstractViewHolder<PackageV3>(view),
        WidgetEventPDPExpandable.EventWidgetExpandableListener{

    lateinit var chooseNewPackage: (String) -> Unit
    lateinit var eventPDPTracking: EventPDPTracking
    var idPackage = ""

    var eventPDPTicketAdapter = EventPDPTicketItemPackageAdapter(onBindItemTicketListener, onCoachmarkListener)
    override fun bind(element: PackageV3) {
        itemView.expand_event_pdp_ticket.setListener(this)
        with(itemView){
            idPackage = element.id
            if(!element.isChoosen){
                if (onCoachmarkListener.getLocalCache()) {
                    expand_event_pdp_ticket.setExpand((position != -1 && position == 0))
                }else{
                    expand_event_pdp_ticket.setExpand(false)
                }
            }

            eventPDPTicketAdapter.setList(element.packageItems, element.id, element.name)
            eventPDPTicketAdapter.eventPDPTracking = eventPDPTracking
            tg_event_pdp_expand_title.text = element.name
            tg_event_pdp_expand_price.text = Html.fromHtml(context.resources.getString(R.string.ent_checkout_price_expand,
                    CurrencyFormatter.getRupiahAllowZeroFormat(element.salesPrice.toLong())))
            rv_event_parent_ticket.apply {
                adapter = eventPDPTicketAdapter
                layoutManager = LinearLayoutManager(
                        this@with.context,
                        RecyclerView.VERTICAL, false
                )
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket
    }

    override fun onParentClicked(toogle: Boolean) {
        if(toogle){
            onBindItemTicketListener.resetPackage()
        }
        chooseNewPackage.invoke(idPackage)
    }
}