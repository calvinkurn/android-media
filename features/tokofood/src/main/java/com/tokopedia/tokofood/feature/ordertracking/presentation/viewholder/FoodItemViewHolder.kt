package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingFoodItemBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel

class FoodItemViewHolder(view: View) : AbstractViewHolder<FoodItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_food_item
    }

    private val binding = ItemTokofoodOrderTrackingFoodItemBinding.bind(itemView)

    override fun bind(element: FoodItemUiModel) {

    }

    override fun bind(element: FoodItemUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)

        if (payloads.isNullOrEmpty() || element == null) return


    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setFoodName(element: FoodItemUiModel) {
        tvFoodItemName.text = if (element.isExpand) {
            element.foodName
        } else {
            if (element.addOnVariantList.isEmpty()) {
                element.foodName
            } else {
                val nn600Color = com.tokopedia.unifyprinciples.R.color.Unify_NN600.toString()
                MethodChecker.fromHtml(
                    itemView.context.getString(
                        R.string.order_detail_food_addon_item_value,
                        element.foodName,
                        nn600Color,
                        element.addOnVariantList.size.toString()
                    )
                )
            }
        }
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setQuantityPrice(
        quantity: String,
        price: String
    ) {
        tvFoodItemName.text = itemView.context.getString(
            R.string.order_detail_qty_price_value,
            quantity, price
        )
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setupNote(note: String) {
        if (note.isBlank()) {
            icFoodNote.hide()
            tvFoodItemNote.hide()
        } else {
            icFoodNote.show()
            tvFoodItemNote.text = note
            tvFoodItemNote.show()
        }
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setupAddonVariantAdapter() {

    }
}