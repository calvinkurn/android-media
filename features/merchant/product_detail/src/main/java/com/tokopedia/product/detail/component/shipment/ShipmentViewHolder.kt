package com.tokopedia.product.detail.component.shipment

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.ItemShipmentBinding
import com.tokopedia.product.detail.databinding.ViewShipmentErrorBinding
import com.tokopedia.product.detail.databinding.ViewShipmentFailedBinding
import com.tokopedia.product.detail.databinding.ViewShipmentInfoBinding
import com.tokopedia.product.detail.databinding.ViewShipmentSuccessBinding
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
        element.state.render()
    }

    private fun ShipmentUiModel.State.render() {
        when (this) {
            is ShipmentUiModel.Success -> render()
            is ShipmentUiModel.Loading -> render()
            is ShipmentUiModel.Error -> render()
            is ShipmentUiModel.Failed -> render()
        }
    }

    private fun ShipmentUiModel.Success.render() {
        val view = binding.pdpShipmentStateSuccess.inflate()
        ViewShipmentSuccessBinding.bind(view).apply(this)

        binding.pdpShipmentStateSuccess.show()
        binding.pdpShipmentStateError.hide()
        binding.pdpShipmentStateLoading.hide()
        binding.pdpShipmentStateFailed.hide()
    }

    private fun ShipmentUiModel.Error.render() {
        val view = binding.pdpShipmentStateError.inflate()
        ViewShipmentErrorBinding.bind(view).apply(this)

        binding.pdpShipmentStateSuccess.hide()
        binding.pdpShipmentStateError.show()
        binding.pdpShipmentStateLoading.hide()
        binding.pdpShipmentStateFailed.hide()
    }

    private fun ShipmentUiModel.Loading.render() {
        binding.pdpShipmentStateLoading.inflate()

        binding.pdpShipmentStateSuccess.hide()
        binding.pdpShipmentStateError.hide()
        binding.pdpShipmentStateLoading.show()
        binding.pdpShipmentStateFailed.hide()
    }

    private fun ShipmentUiModel.Failed.render() {
        val view = binding.pdpShipmentStateError.inflate()
        ViewShipmentFailedBinding.bind(view).apply()

        binding.pdpShipmentStateSuccess.hide()
        binding.pdpShipmentStateLoading.hide()
        binding.pdpShipmentStateError.show()
    }

    private fun ViewShipmentSuccessBinding.apply(data: ShipmentUiModel.Success) {
        val logo = data.logo
        pdpShipmentHeaderLogo.showIfWithBlock(logo.isNotEmpty()) {
            setImageUrl(logo)
        }

        val title = data.title
        pdpShipmentHeaderPrice.showIfWithBlock(title.isNotEmpty()) {
            text = title
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

    private fun ViewShipmentErrorBinding.apply(data: ShipmentUiModel.Error) {
        val title = data.title
        pdpShipmentErrorTitle.showIfWithBlock(title.isNotEmpty()) {
            text = title
        }

        val subtitle = data.subtitle
        pdpShipmentErrorSubtitle.showIfWithBlock(subtitle.isNotEmpty()) {
            text = subtitle
        }

        val background = data.background
        pdpShipmentBackground.showIfWithBlock(background.isNotEmpty()) {
            setImageUrl(background)
        }
    }

    private fun ViewShipmentFailedBinding.apply() {
        pdpShipmentLocalLoad.progressState = false
        pdpShipmentLocalLoad.refreshBtn?.setOnClickListener {
            if (!pdpShipmentLocalLoad.progressState) {
                pdpShipmentLocalLoad.progressState = true
                listener.refreshPage()
            }
        }
    }

}
