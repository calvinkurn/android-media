package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.accordion.AccordionUnify
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.ItemEventPdpParentTicketBinding
import com.tokopedia.entertainment.home.utils.DateUtils
import com.tokopedia.entertainment.home.utils.DateUtils.SECOND_IN_MILIS
import com.tokopedia.entertainment.pdp.adapter.EventPDPTicketItemPackageAdapter
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.EventPDPTicketGroup
import com.tokopedia.entertainment.pdp.data.PackageItem
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding
import java.util.Date

class PackageParentViewHolder(
        view: View,
        private val onBindItemTicketListener: OnBindItemTicketListener)
    : AbstractViewHolder<EventPDPTicketGroup>(view) {

    lateinit var eventPDPTracking: EventPDPTracking
    private var ticketItemGrouplist: MutableList<PackageV3> = mutableListOf()

    private val binding: ItemEventPdpParentTicketBinding? by viewBinding()

    override fun bind(element: EventPDPTicketGroup?) {
        binding?.accordionEventPDPTicket?.run {
            accordionData.clear()
            removeAllViews()
            onItemClick = { position, isExpanded ->
                if (isExpanded) {
                    val rvTicketItem = getChildAt(position).findViewById<RecyclerView>(R.id.rv_accordion_expandable)
                    for (i in Int.ZERO until rvTicketItem.childCount) {
                        val adapter = rvTicketItem.adapter
                        adapter?.notifyItemChanged(i)
                    }
                } else {
                    onBindItemTicketListener.onCollapseAccordion()
                }
                onBindItemTicketListener.resetPackage()
            }
        }
        element?.ticketModels?.forEach {
            ticketItemGrouplist.add(it)
            if (it.isRecommendationPackage) {
                renderForRecommendationPackage(it)
            } else {
                renderForMainPackage(it)
            }
        }
    }

    private fun mapPackageV3ToAccordionData(value: PackageV3, isRecommendation: Boolean)
            : AccordionDataUnify {
        val eventPDPTicketAdapter = EventPDPTicketItemPackageAdapter(onBindItemTicketListener)
        val expandableLayout = View.inflate(itemView.context, R.layout.ent_ticket_rv_expandable_item, null)
        expandableLayout.findViewById<RecyclerView>(R.id.rv_accordion_expandable).apply {
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

        val subtitle = when (isRecommendation) {
            true -> Html.fromHtml("${getString(R.string.ent_pdp_available_date_label)}  " +
                        "<b>${DateUtils.dateToString(
                            Date(value.dates[Int.ZERO].toLong() * SECOND_IN_MILIS),
                        DateUtils.DEFAULT_VIEW_FORMAT)}</b>")
            false -> getSubtitle(value.packageItems)
        }

        return AccordionDataUnify(
                title = value.name,
                subtitle = subtitle,
                expandableView = expandableLayout
        )
    }

    private fun renderForMainPackage(element: PackageV3) {
        binding?.run{
            accordionEventPDPTicket.type = AccordionUnify.TYPE_OR
            accordionEventPDPTicket.addGroup(
                    mapPackageV3ToAccordionData(element, false)
            )
        }
    }

    private fun renderForRecommendationPackage(element: PackageV3) {
        binding?.run {
            accordionEventPDPTicket.addGroup(
                    mapPackageV3ToAccordionData(element, true)
            )
        }
    }

    private fun getSubtitle(list: List<PackageItem>): Spanned{
        val sortedList = list.filter {
            it.salesPrice.toIntSafely() != ZERO_PRICE
        }.sortedBy {
            it.salesPrice.toIntSafely()
        }

        return if (sortedList.isNullOrEmpty())
            Html.fromHtml("<b>${getString(R.string.ent_free_price)} </b>")
        else {
            val salesPrice = sortedList.first().salesPrice
            val priceFormatted = CurrencyFormatHelper.convertToRupiah(salesPrice)
            Html.fromHtml("${getString(R.string.ent_checkout_price_expand)} <b>Rp $priceFormatted</b>")
        }

    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket
        const val ZERO_PRICE = 0
    }
}
