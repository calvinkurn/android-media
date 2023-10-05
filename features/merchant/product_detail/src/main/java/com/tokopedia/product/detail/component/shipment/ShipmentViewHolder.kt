package com.tokopedia.product.detail.component.shipment

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemShipmentBinding
import com.tokopedia.product.detail.databinding.ViewShipmentInfoBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.viewholder.ProductDetailPageViewHolder

class ShipmentViewHolder(
    view: View,
    private val listener: DynamicProductDetailListener
) : ProductDetailPageViewHolder<ShipmentUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_shipment
    }

    private val binding = ItemShipmentBinding.bind(view)

    override fun bind(element: ShipmentUiModel) {
        val data = element.data ?: return
        binding.apply(data)
    }

    private fun ItemShipmentBinding.apply(data: ShipmentUiModel.Data) {
        val logo = data.logo
        pdpShipmentHeaderLogo.showIfWithBlock(logo.isNotEmpty()) {
            setImageUrl(logo)
        }

        val price = data.price
        pdpShipmentHeaderPrice.showIfWithBlock(price.isNotEmpty()) {
            text = price
        }

        val slashPrice = data.slashPrice
        pdpShipmentHeaderSlashPrice.showIfWithBlock(slashPrice.isNotEmpty()) {
            text = slashPrice
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        val background = data.background
        pdpShipmentBackground.showIfWithBlock(background.isNotEmpty()) {
            setImageUrl(background)
        }

        val appLink = data.appLink
        pdpShipmentChevron.showIfWithBlock(appLink.isNotEmpty()) {

        }

        val body = data.body
        pdpShipmentContainerBody.showIfWithBlock(body.isNotEmpty()) {
            removeAllViews()
            apply(body)
        }
    }

    private fun LinearLayout.apply(body: List<ShipmentUiModel.Info>) {
        val context = context ?: return
        body.forEach { info ->
            val view = ViewShipmentInfoBinding.inflate(LayoutInflater.from(context))
            view.apply(info)
            addView(view.root)
        }
    }


    private fun ViewShipmentInfoBinding.apply(data: ShipmentUiModel.Info) {
        val logo = data.logo
        pdpShipmentInfoLogo.showIfWithBlock(logo > -1) {
            setImage(logo)
        }

        val text = data.text
        pdpShipmentInfoText.showIfWithBlock(text.isNotEmpty()) {
            this.text = text
        }
    }
}
