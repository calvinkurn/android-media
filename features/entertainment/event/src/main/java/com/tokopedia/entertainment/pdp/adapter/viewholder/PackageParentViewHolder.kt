package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPTicketItemPackageAdapter
import com.tokopedia.entertainment.pdp.adapter.viewholder.CurrencyFormatter.getRupiahAllowZeroFormat
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import com.tokopedia.entertainment.pdp.widget.WidgetEventPDPExpandable
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket.view.*

class PackageParentViewHolder(view: View,
                              private val onBindItemTicketListener: OnBindItemTicketListener,
                              private val onCoachmarkListener: OnCoachmarkListener) : AbstractViewHolder<EventPDPTicketGroup>(view),
        WidgetEventPDPExpandable.EventWidgetExpandableListener {

    lateinit var chooseNewPackage: (String) -> Unit
    lateinit var eventPDPTracking: EventPDPTracking
    var idPackage = ""

    var eventPDPTicketAdapter = EventPDPTicketItemPackageAdapter(onBindItemTicketListener, onCoachmarkListener)

//    override fun bind(element: PackageV3) {
//        // TODO: [Misael] ini listener punya expandable (komunikasi parent n child)
////        itemView.expand_event_pdp_ticket.setListener(this)
//        idPackage = element.id
//        if (element.isRecommendationPackage) {
//            renderForRecommendationPackage(element)
//        } else {
//            renderForMainPackage(element)
//        }
//    }

    override fun bind(element: EventPDPTicketGroup?) {
        itemView.run {
            accordionEventPDPTicket.accordionData.clear()
            accordionEventPDPTicket.removeAllViews()
        }

        element?.ticketModels?.forEach {
            idPackage = it.id
            if (it.isRecommendationPackage) {
                renderForRecommendationPackage(it)
            } else {
                renderForMainPackage(it)
            }
        }
    }

    private fun mapPackageV3ToAccordionData(view: View, value: PackageV3, isRecommendation: Boolean)
            : AccordionDataUnify {
        val salesPrice = getRupiahAllowZeroFormat(value.salesPrice.toLong())
        val rvEventChildTicket = RecyclerView(view.context)
        rvEventChildTicket.layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        )
        rvEventChildTicket.apply {
            adapter = eventPDPTicketAdapter
            layoutManager = LinearLayoutManager(
                    context,
                    RecyclerView.VERTICAL, false
            )
        }
        eventPDPTicketAdapter.setList(value.packageItems, value.id, value.name, isRecommendation)
        if (!isRecommendation) {
            eventPDPTicketAdapter.eventPDPTracking = eventPDPTracking
        }

        return AccordionDataUnify(
                title = value.name,
                subtitle = "Mulai dari $salesPrice",
                expandableView = rvEventChildTicket
        )
    }

    private fun renderForMainPackage(element: PackageV3) {
        with(itemView) {
            // TODO: [Misael] ini coachmark
//            if (!element.isChoosen) {
//                if (!onCoachmarkListener.getLocalCache()) {
//                    expand_event_pdp_ticket.setExpand((position != -1 && position == 0))
//                } else {
//                    expand_event_pdp_ticket.setExpand(false)
//                }
//            }

//            eventPDPTicketAdapter.setList(element.packageItems, element.id, element.name, false)
//            eventPDPTicketAdapter.eventPDPTracking = eventPDPTracking
//            tg_event_pdp_expand_title.text = element.name
//            tg_event_pdp_expand_price_title.text = getString(R.string.ent_checkout_price_expand)
//            tg_event_pdp_expand_price.text = CurrencyFormatter.getRupiahAllowZeroFormat(element.salesPrice.toLong())
//            rv_event_parent_ticket.apply {
//                adapter = eventPDPTicketAdapter
//                layoutManager = LinearLayoutManager(
//                        this@with.context,
//                        RecyclerView.VERTICAL, false
//                )
//            }
            accordionEventPDPTicket.addGroup(
                    mapPackageV3ToAccordionData(this, element, false)
            )
        }
    }

    private fun renderForRecommendationPackage(element: PackageV3) {
        with(itemView) {
//            eventPDPTicketAdapter.setList(element.packageItems, element.id, element.name, true)
//            tg_event_pdp_expand_title.text = element.name
//            tg_event_pdp_expand_price_title.text = getString(R.string.ent_pdp_available_date_label)
//            tg_event_pdp_expand_price.text = DateUtils.dateToString(Date(element.dates[0].toLong() * SECOND_IN_MILIS), DateUtils.DEFAULT_VIEW_FORMAT)
//            rv_event_parent_ticket.apply {
//                adapter = eventPDPTicketAdapter
//                layoutManager = LinearLayoutManager(
//                        this@with.context,
//                        RecyclerView.VERTICAL, false
//                )
//            }

            // TODO: [Misael] Coachmark nih
//            if (!onCoachmarkListener.getLocalCache()) {
//                expand_event_pdp_ticket.setExpand(true)
//            }

            accordionEventPDPTicket.addGroup(
                    mapPackageV3ToAccordionData(this, element, true)
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket

    }

    override fun onParentClicked(toogle: Boolean) {
        if (toogle) {
            onBindItemTicketListener.resetPackage()
        }
        chooseNewPackage.invoke(idPackage)
    }
}