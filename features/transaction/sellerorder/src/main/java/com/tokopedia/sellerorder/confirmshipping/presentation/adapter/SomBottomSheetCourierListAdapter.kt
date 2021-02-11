package com.tokopedia.sellerorder.confirmshipping.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import kotlinx.android.synthetic.main.bottomsheet_text_item.view.*

/**
 * Created by fwidjaja on 2019-11-05.
 */
class SomBottomSheetCourierListAdapter(private var listener: ActionListener): RecyclerView.Adapter<SomBottomSheetCourierListAdapter.ViewHolder>()  {
    var listCourier = mutableListOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()
    var listCourierService = mutableListOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage>()
    var isServiceCourier = false

    interface ActionListener {
        fun onChooseCourierAgent(shipmentId: Long, courierName: String)
        fun onChooseCourierService(spId:String, courierServiceName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_text_item, parent, false))
    }

    override fun getItemCount(): Int {
        return if (isServiceCourier) listCourierService.size
        else listCourier.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isServiceCourier) {
            holder.itemView.label.text = listCourierService[position].name
            holder.itemView.setOnClickListener { listener.onChooseCourierService(listCourierService[position].spId, listCourierService[position].name) }

        } else {
            holder.itemView.label.text = listCourier[position].shipmentName
            holder.itemView.setOnClickListener { listener.onChooseCourierAgent(listCourier[position].shipmentId.toLongOrZero() , listCourier[position].shipmentName) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}