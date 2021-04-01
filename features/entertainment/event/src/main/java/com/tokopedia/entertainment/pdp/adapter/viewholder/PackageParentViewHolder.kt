package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.graphics.Typeface.BOLD
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.utils.DateUtils
import com.tokopedia.entertainment.home.utils.DateUtils.SECOND_IN_MILIS
import com.tokopedia.entertainment.pdp.adapter.EventPDPTicketItemPackageAdapter
import com.tokopedia.entertainment.pdp.adapter.viewholder.CurrencyFormatter.getRupiahAllowZeroFormat
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.entertainment.pdp.listener.OnCoachmarkListener
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket.view.*
import java.util.*

class PackageParentViewHolder(
        view: View,
        private val onBindItemTicketListener: OnBindItemTicketListener,
        private val onCoachmarkListener: OnCoachmarkListener)
    : AbstractViewHolder<EventPDPTicketGroup>(view) {

    lateinit var eventPDPTracking: EventPDPTracking
    var idPackage = ""

    var eventPDPTicketAdapter = EventPDPTicketItemPackageAdapter(onBindItemTicketListener, onCoachmarkListener)

    override fun bind(element: EventPDPTicketGroup?) {
        itemView.accordionEventPDPTicket.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = { position: Int, _ ->
                onBindItemTicketListener.resetPackage()
                val rvTicketItem = itemView.accordionEventPDPTicket.getChildAt(position).findViewById<RecyclerView>(rvId)
                for (i in 0 until rvTicketItem.childCount) {
                    val vh = rvTicketItem.findViewHolderForAdapterPosition(i)
                            as EventPDPTicketItemPackageAdapter.EventPDPTicketItemPackageViewHolder
                    vh.resetQuantities()
                }
            }
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
        rvEventChildTicket.id = rvId
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

        val subtitle: String = when (isRecommendation) {
            true -> "${getString(R.string.ent_pdp_available_date_label)} " +
                        DateUtils.dateToString(Date(value.dates[0].toLong() * SECOND_IN_MILIS),
                        DateUtils.DEFAULT_VIEW_FORMAT)
            false -> "Mulai dari $salesPrice"
        }

        return AccordionDataUnify(
                title = value.name,
                subtitle = subtitle,
                expandableView = rvEventChildTicket
        )
    }

    private fun renderForMainPackage(element: PackageV3) {
        with(itemView) {
            accordionEventPDPTicket.type = AccordionUnify.TYPE_OR
            accordionEventPDPTicket.addGroup(
                    mapPackageV3ToAccordionData(this, element, false)
            )
        }
    }

    private fun renderForRecommendationPackage(element: PackageV3) {
        with(itemView) {
            accordionEventPDPTicket.addGroup(
                    mapPackageV3ToAccordionData(this, element, true)
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket
        // TODO: [Misael] ini ID dibikin disini ato di tempat lain ya?
        val rvId = View.generateViewId()

    }
}