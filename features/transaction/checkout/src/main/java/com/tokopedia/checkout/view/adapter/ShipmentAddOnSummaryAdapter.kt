package com.tokopedia.checkout.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemShipmentAddOnSummaryBinding
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.checkout.view.viewholder.ShipmentAddOnSummaryViewHolder

class ShipmentAddOnSummaryAdapter(private val addOnSummaryList: List<ShipmentAddOnSummaryModel>) :
    RecyclerView.Adapter<ShipmentAddOnSummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentAddOnSummaryViewHolder =
            ShipmentAddOnSummaryViewHolder(
            ItemShipmentAddOnSummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ShipmentAddOnSummaryViewHolder, position: Int) {
        holder.bind(addOnSummaryList[position])
    }

    override fun getItemCount(): Int = addOnSummaryList.size
}
