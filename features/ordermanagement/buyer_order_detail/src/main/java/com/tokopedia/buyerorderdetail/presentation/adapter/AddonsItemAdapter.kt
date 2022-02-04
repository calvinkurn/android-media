package com.tokopedia.buyerorderdetail.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.Utils.setAddonMessageFormatted
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailAddonsListBinding
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class AddonsItemAdapter(private val addonsItemList: List<AddonsListUiModel.AddonItemUiModel>) :
    RecyclerView.Adapter<AddonsItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBuyerOrderDetailAddonsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (addonsItemList.isNotEmpty()) {
            holder.bind(addonsItemList[position])
        }
    }

    override fun getItemCount(): Int = addonsItemList.size

    class ViewHolder(private val binding: ItemBuyerOrderDetailAddonsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AddonsListUiModel.AddonItemUiModel) {
            with(binding) {
                setupTextview(item)
                setupFromMetadata(item.fromStr)
                setupToMetadata(item.toStr)
                setupMessageMetadata(item.message)
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupTextview(item: AddonsListUiModel.AddonItemUiModel) {
            tvBomDetailAddonsName.text = item.addOnsName
            ivBomDetailAddonsThumbnail.setImageUrl(item.addOnsThumbnailUrl)
            tvBomDetailAddonsPriceQuantity.text =
                root.context.getString(
                    R.string.label_product_price_and_quantity,
                    item.quantity,
                    item.priceText
                )
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupToMetadata(toStr: String) {
            if (toStr.isBlank()) {
                tvBomDetailAddonsToValue.hide()
                tvBomDetailAddonsToLabel.hide()
            } else {
                tvBomDetailAddonsToValue.text = itemView.context.getString(R.string.order_addons_to_value, toStr)
                tvBomDetailAddonsToValue.show()
                tvBomDetailAddonsToLabel.show()
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupFromMetadata(fromStr: String) {
            if (fromStr.isBlank()) {
                tvBomDetailAddonsFromLabel.hide()
                tvBomDetailAddonsFromValue.hide()
            } else {
                tvBomDetailAddonsFromValue.text = itemView.context.getString(R.string.order_addons_to_value, fromStr)
                tvBomDetailAddonsFromLabel.show()
                tvBomDetailAddonsFromValue.show()
            }
        }

        private fun ItemBuyerOrderDetailAddonsListBinding.setupMessageMetadata(message: String) {
            if (message.isBlank()) {
                tvBomDetailAddonsMessageValue.hide()
            } else {
                tvBomDetailAddonsMessageValue.show()
                tvBomDetailAddonsMessageValue.apply {
                    setAddonMessageFormatted(message, context)
                }
            }
        }
    }
}