package com.tokopedia.logisticseller.ui.confirmshipping.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.databinding.BottomsheetTextItemBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by fwidjaja on 2019-11-05.
 */
class BottomSheetCourierListAdapter(private var listener: ActionListener): RecyclerView.Adapter<BottomSheetCourierListAdapter.ViewHolder>()  {
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
        holder.bind(listCourier.getOrNull(position) to listCourierService.getOrNull(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding by viewBinding<BottomsheetTextItemBinding>()

        fun bind(element: Pair<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment?, SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage?>) {
            binding?.run {
                if (isServiceCourier) {
                    label.text = element.second?.name.orEmpty()
                    root.setOnClickListener {
                        listener.onChooseCourierService(
                            element.second?.spId ?: "0", element.second?.name.orEmpty()
                        )
                    }
                } else {
                    label.text = element.first?.shipmentName.orEmpty()
                    root.setOnClickListener {
                        listener.onChooseCourierAgent(
                            element.first?.shipmentId.toLongOrZero(),
                            element.first?.shipmentName.orEmpty()
                        )
                    }
                }
            }
        }
    }
}
