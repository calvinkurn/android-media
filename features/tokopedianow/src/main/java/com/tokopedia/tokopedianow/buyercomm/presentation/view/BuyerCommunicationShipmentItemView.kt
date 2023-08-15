package com.tokopedia.tokopedianow.buyercomm.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopedianow.buyercomm.presentation.data.ShipmentOptionData
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowBuyerCommunicationShipmentBinding

class BuyerCommunicationShipmentItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val FIRST_ITEM_INDEX = 0
        private const val ITEM_MARGIN_TOP = 10
        private const val DETAIL_FIRST_INDEX = 0
        private const val DETAIL_SECOND_INDEX = 1
        private const val ONE_LINE_SHIPMENT_DETAIL = 1
    }

    private var binding: ItemTokopedianowBuyerCommunicationShipmentBinding? = null

    init {
        binding = ItemTokopedianowBuyerCommunicationShipmentBinding
            .inflate(LayoutInflater.from(context), this, true)
    }

    fun bind(shipment: ShipmentOptionData) {
        setupLayout(shipment)
        showTitleText(shipment)
        showDescriptionText(shipment)
        showDetailText(shipment)
        setTextColor(shipment)
    }

    private fun setupLayout(shipment: ShipmentOptionData) {
        binding?.apply {
            if (shipment.index != FIRST_ITEM_INDEX) {
                val topMargin = context.dpToPx(ITEM_MARGIN_TOP).orZero().toInt()
                val layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )
                layoutParams.topMargin = topMargin
                root.layoutParams = layoutParams
            }
        }
    }

    private fun setTextColor(shipment: ShipmentOptionData) {
        binding?.apply {
            if (!shipment.available) {
                textTitle.setTextColor(
                    getColor(
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
                textDescription.setTextColor(
                    getColor(
                        com.tokopedia.unifyprinciples.R.color.Unify_NN400
                    )
                )
            }
        }
    }

    private fun showTitleText(shipment: ShipmentOptionData) {
        binding?.textTitle?.text = shipment.name
    }

    private fun showDescriptionText(shipment: ShipmentOptionData) {
        binding?.apply {
            val shipmentDetails = shipment.details
            shipmentDetails.getOrNull(DETAIL_FIRST_INDEX)?.let { description ->
                if (shipmentDetails.count() == ONE_LINE_SHIPMENT_DETAIL) {
                    textDescription.setTextColor(
                        getColor(
                            com.tokopedia.unifyprinciples.R.color.Unify_NN600
                        )
                    )
                }
                textDescription.text = description
                textDescription.show()
            }
        }
    }

    private fun showDetailText(shipment: ShipmentOptionData) {
        binding?.apply {
            val shipmentDetails = shipment.details
            shipmentDetails.getOrNull(DETAIL_SECOND_INDEX)?.let { detail ->
                textDetail.text = detail
                textDetail.show()
            }
        }
    }

    private fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }
}
