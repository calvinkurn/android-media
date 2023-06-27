package com.tokopedia.checkout.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemShipmentAddOnSubtotalBinding
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSubtotalModel
import com.tokopedia.checkout.view.viewholder.ShipmentAddOnSubtotalViewHolder

class ShipmentAddOnSubtotalAdapter(private val addOnSubtotalList: List<ShipmentAddOnSubtotalModel>) :
    RecyclerView.Adapter<ShipmentAddOnSubtotalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentAddOnSubtotalViewHolder =
            ShipmentAddOnSubtotalViewHolder(
            ItemShipmentAddOnSubtotalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ShipmentAddOnSubtotalViewHolder, position: Int) {
        holder.bind(addOnSubtotalList[position])
    }

    override fun getItemCount(): Int = addOnSubtotalList.size
}
