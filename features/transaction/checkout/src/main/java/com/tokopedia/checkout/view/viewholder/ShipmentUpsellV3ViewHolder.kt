package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ShipmentUpsellV3ViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {


    companion object {

        @JvmField
        val LAYOUT = R.layout.item_upsell_v3
    }

    private val checkoutUpsellContainer: ConstraintLayout =
        itemView.findViewById(R.id.checkout_upsell_container)
    private val checkoutUpsellImage: ImageUnify =
        itemView.findViewById(R.id.checkout_upsell_image)
    private val checkoutUpsellLogoContainer: CardUnify2 =
        itemView.findViewById(R.id.checkout_upsell_logo_container)
    private val checkoutUpsellTitle: Typography =
        itemView.findViewById(R.id.checkout_upsell_title)
    private val checkoutUpsellDescription: Typography =
        itemView.findViewById(R.id.checkout_upsell_description)


    fun bind(data: ShipmentNewUpsellModel) {
        checkoutUpsellContainer.setBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                R.color.Unify_Background
            )
        )
        checkoutUpsellImage.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                R.drawable.checkout_module_upsell_v3_background
            )
        )
        checkoutUpsellImage.cornerRadius = Int.ZERO
    }
}
