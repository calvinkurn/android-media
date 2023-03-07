package com.tokopedia.checkout.view.viewholder

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ShipmentNewUpsellViewHolder(
    itemView: View,
    private val shipmentAdapterActionListener: ShipmentAdapterActionListener
) : RecyclerView.ViewHolder(itemView) {

    private val container: ContainerUnify = itemView.findViewById(R.id.checkout_upsell_container)
    private val frame: FrameLayout = itemView.findViewById(R.id.checkout_upsell_frame)
    private val image: ImageUnify = itemView.findViewById(R.id.checkout_upsell_image)
    private val title: Typography = itemView.findViewById(R.id.checkout_upsell_title)
    private val description: Typography = itemView.findViewById(R.id.checkout_upsell_description)
    private val button: UnifyButton = itemView.findViewById(R.id.checkout_upsell_button)

    fun bind(data: ShipmentNewUpsellModel) {
        frame.setBackgroundResource(R.drawable.checkout_module_upsell_background)

        image.setImageUrl(data.image)
        title.text = HtmlLinkHelper(description.context, data.description).spannedString
        button.text = data.buttonText

        if (data.isSelected) {
            container.setContainerColor(ContainerUnify.GREEN)
            description.gone()
            button.buttonVariant = UnifyButton.Variant.GHOST
            button.setOnClickListener {
                shipmentAdapterActionListener.onClickCancelNewUpsellCard(data)
            }
            button.layoutParams.width = BUTTON_CANCEL_WIDTH.toPx()
        } else {
            container.setContainerColor(ContainerUnify.GREY)
            val spannedString = SpannableString("${data.priceWording}/${data.duration}")
            spannedString.setSpan(
                RelativeSizeSpan(WORDING_DURATION_SIZE),
                spannedString.lastIndexOf("/") + 1,
                spannedString.length,
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
            )
            description.text = spannedString
            description.visible()
            button.buttonVariant = UnifyButton.Variant.FILLED
            button.setOnClickListener {
                shipmentAdapterActionListener.onClickApplyNewUpsellCard(data)
            }
            button.layoutParams.width = BUTTON_APPLY_WIDTH.toPx()
        }

        if (!data.hasSeenUpsell) {
            data.hasSeenUpsell = true
            shipmentAdapterActionListener.onViewNewUpsellCard(data)
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_UPSELL = R.layout.item_upsell_new

        private const val BUTTON_CANCEL_WIDTH = 82
        private const val BUTTON_APPLY_WIDTH = 104

        private const val WORDING_DURATION_SIZE = 0.875f
    }
}
