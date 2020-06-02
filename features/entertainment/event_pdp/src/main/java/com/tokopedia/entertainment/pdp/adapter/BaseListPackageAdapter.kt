package com.tokopedia.entertainment.pdp.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactoryImp
import com.tokopedia.entertainment.pdp.adapter.viewholder.CurrencyFormatter
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageViewHolder
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.data.Package
import javax.inject.Inject

class BaseListPackageAdapter(packageTypeFactoryImp: PackageTypeFactoryImp,
                             val setTotalAndPackageId: (String,String, String, String, Boolean, String, String, String)->Unit,
                             val eventPDPTracking: EventPDPTracking
                             ):
        BaseListAdapter<EventPDPTicketModel, PackageTypeFactory>(packageTypeFactoryImp) {

    private var lastClickedIndex: Int = -1

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if(holder is PackageViewHolder){
            holder.quantityEditorValueButtonClicked = ::quantitiyEditorValueButtonListener
            holder.pilihbuttonClicked = ::pilihButtonClicked
            holder.eventPDPTracking = eventPDPTracking
        }
        super.onBindViewHolder(holder, position)
    }

    private fun quantitiyEditorValueButtonListener(idPackages: String, idGroup:String, price: String, qty: Int, isError: Boolean, product_name: String, product_id: String){
        setTotalAndPackageId(idPackages,idGroup, CurrencyFormatter.getRupiahFormat(price.toLong()*qty), qty.toString(), isError, product_name, product_id, price)
    }

    private fun pilihButtonClicked(idClicked: String){
        if(lastClickedIndex != -1){
            data.get(lastClickedIndex).isClicked = false
            notifyItemChanged(lastClickedIndex)
        }

        data.forEachIndexed{index, it ->
            if(it is Package) {
                if(it.id == idClicked){
                    it.isClicked = true
                    notifyItemChanged(index)
                    lastClickedIndex = index
                    return
                }
            }
        }
    }
}