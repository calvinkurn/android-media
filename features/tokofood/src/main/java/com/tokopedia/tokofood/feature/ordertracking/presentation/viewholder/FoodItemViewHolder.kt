package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodOrderTrackingFoodItemBinding
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.AddonVariantAdapter
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.RecyclerViewPollerListener
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.AddonVariantItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel

class FoodItemViewHolder(view: View,
                         private val recylerViewPoolListener: RecyclerViewPollerListener
) : AbstractViewHolder<FoodItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_order_tracking_food_item
    }

    private val binding = ItemTokofoodOrderTrackingFoodItemBinding.bind(itemView)

    override fun bind(element: FoodItemUiModel) {
        with(binding) {
            setFoodName(element.foodName, element.quantity)
            setPriceQuantity(element.priceStr)
            setupAddonVariantAdapter(element.addOnVariantList)
            setupNote(element.notes)
        }
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setFoodName(foodName: String, quantity: String) {
        tvFoodItemName.text = root.context.getString(
            com.tokopedia.tokofood.R.string.order_detail_qty_food_value,
            quantity, foodName
        )
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setPriceQuantity(
        price: String
    ) {
        tvFoodItemQtyPrice.text = price
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

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setupAddonVariantAdapter(
        addOnVariantList: List<AddonVariantItemUiModel>
    ) {
        if (addOnVariantList.isNotEmpty()) {
            rvVariantAddon.run {
                show()
                setHasFixedSize(true)
                setRecycledViewPool(recylerViewPoolListener.parentPool)
                layoutManager = LinearLayoutManager(context)
                adapter = AddonVariantAdapter(addOnVariantList)
            }
        } else {
            rvVariantAddon.hide()
        }
    }
}