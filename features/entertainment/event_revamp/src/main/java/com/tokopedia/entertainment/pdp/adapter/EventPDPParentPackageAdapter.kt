package com.tokopedia.entertainment.pdp.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactory
import com.tokopedia.entertainment.pdp.adapter.factory.PackageTypeFactoryImp
import com.tokopedia.entertainment.pdp.adapter.viewholder.PackageParentViewHolder
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.data.EventPDPTicketModel
import com.tokopedia.entertainment.pdp.data.PackageV3

class EventPDPParentPackageAdapter (packageTypeFactoryImp: PackageTypeFactoryImp,
                                    val eventPDPTracking: EventPDPTracking):
        BaseListAdapter<EventPDPTicketModel, PackageTypeFactory>(packageTypeFactoryImp){

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if(holder is PackageParentViewHolder){
            holder.chooseNewPackage = ::chooseNewPackage
            holder.eventPDPTracking = eventPDPTracking
        }
        super.onBindViewHolder(holder, position)

    }

    private fun chooseNewPackage(idPackage: String){
        data.forEachIndexed{index, it ->
            if(it is PackageV3) {
                if(it.id.equals(idPackage)){
                    it.isChoosen = true
                    notifyItemChanged(index)
                } else {
                    it.isChoosen = false
                    notifyItemChanged(index)
                }
            }
        }
    }
}