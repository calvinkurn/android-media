package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Html
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPTicketItemPackageAdapter
import com.tokopedia.entertainment.pdp.data.PackageV3
import com.tokopedia.entertainment.pdp.listener.OnBindItemTicketListener
import kotlinx.android.synthetic.main.custom_event_expandable_parent.view.*
import kotlinx.android.synthetic.main.item_event_pdp_parent_ticket.view.*

class PackageParentViewHolder (view: View,
                               onBindItemTicketListener: OnBindItemTicketListener): AbstractViewHolder<PackageV3>(view){

    lateinit var chooseNewPackage: (String) -> Unit

    var eventPDPTicketAdapter = EventPDPTicketItemPackageAdapter(onBindItemTicketListener)
    override fun bind(element: PackageV3) {
        with(itemView){
            eventPDPTicketAdapter.setList(element.packageItems, element.id)
            eventPDPTicketAdapter.chooseNewPackage = ::chooseNewPackage
            tg_event_pdp_expand_title.text = element.name
            tg_event_pdp_expand_price.text = Html.fromHtml(context.resources.getString(R.string.ent_checkout_price_expand,
                    CurrencyFormatter.getRupiahFormat(element.salesPrice.toLong())))
            rv_event_parent_ticket.apply {
                adapter = eventPDPTicketAdapter
                layoutManager = LinearLayoutManager(
                        this@with.context,
                        RecyclerView.VERTICAL, false
                )
            }
        }
    }

    fun chooseNewPackage(idPackage: String){
        chooseNewPackage.invoke(idPackage)
    }

    companion object {
        val LAYOUT = R.layout.item_event_pdp_parent_ticket
    }
}