package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            setFoodName(element.foodName)
            setPriceQuantity(element.quantity, element.priceStr)
            setupAddonVariantAdapter(element.addOnVariantList)
            setupNote(element.notes)
        }
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setFoodName(foodName: String) {
        tvFoodItemName.text = foodName
    }

    private fun ItemTokofoodOrderTrackingFoodItemBinding.setPriceQuantity(
        quantity: String,
        price: String
    ) {
        tvFoodItemQtyPrice.text = itemView.context.getString(
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

    interface Listener {
        fun getRecyclerViewPoolFoodItem(): RecyclerView.RecycledViewPool
    }
}