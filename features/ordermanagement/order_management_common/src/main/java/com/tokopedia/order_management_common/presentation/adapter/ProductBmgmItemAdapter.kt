package com.tokopedia.order_management_common.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.databinding.ItemOrderProductBmgmListItemBinding
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.adapter.diffutil.ProductBmgmItemDiffUtilCallback
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.composeItalicNote

class ProductBmgmItemAdapter(
    private val listener: ViewHolder.Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.Adapter<ProductBmgmItemAdapter.ViewHolder>() {

    private val itemList = arrayListOf<ProductBmgmSectionUiModel.ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderProductBmgmListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, listener, recyclerViewSharedPool)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList.getOrNull(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.firstOrNull()?.let {
                if (it is Pair<*, *>) {
                    val (oldItem, newItem) = it
                    if (oldItem is ProductBmgmSectionUiModel.ProductUiModel &&
                        newItem is ProductBmgmSectionUiModel.ProductUiModel
                    ) {
                        if (oldItem != newItem) {
                            holder.bind(newItem)
                        }
                    }
                }
            }
        }
    }

    fun setItems(newItemList: List<ProductBmgmSectionUiModel.ProductUiModel>) {
        val diffCallback = ProductBmgmItemDiffUtilCallback(itemList, newItemList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        itemList.clear()
        itemList.addAll(newItemList)
    }

    class ViewHolder(
        private val binding: ItemOrderProductBmgmListItemBinding,
        private val listener: Listener,
        private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(binding.root), BmgmAddOnViewHolder.Listener {

        private var element: ProductBmgmSectionUiModel.ProductUiModel? = null

        private var addOnSummaryViewHolder: BmgmAddOnSummaryViewHolder? = null

        private var partialBmgmAddonSummaryBinding: PartialBmgmAddOnSummaryBinding? = null

        fun bind(model: ProductBmgmSectionUiModel.ProductUiModel?) {
            model?.let {
                element = it
                setBmgmItemThumbnail(it.thumbnailUrl)
                setBmgmItemProductName(it.productName)
                setBmgmItemProductPriceQuantity(it.quantity, it.productPriceText)
                setupBmgmItemProductNote(it.productNote)
                setupAddonSection(it.addOnSummaryUiModel)
                setupDividerAddonSummary(it)
                setItemOnClickListener(it)
            }
        }

        private fun setupAddonSection(addOnSummaryUiModel: AddOnSummaryUiModel?) {
            val addonsViewStub: View = itemView.findViewById(R.id.itemBmgmAddonViewStub)
            if (addOnSummaryUiModel?.addonItemList?.isNotEmpty() == true) {
                if (addonsViewStub is ViewStub) addonsViewStub.inflate() else addonsViewStub.show()
                setupAddonsBinding()
                addOnSummaryViewHolder =
                    partialBmgmAddonSummaryBinding?.let {
                        BmgmAddOnSummaryViewHolder(
                            this,
                            it,
                            recyclerViewSharedPool
                        )
                    }
                addOnSummaryViewHolder?.bind(addOnSummaryUiModel)
            } else {
                addonsViewStub.hide()
            }
        }

        private fun setupAddonsBinding() {
            if (partialBmgmAddonSummaryBinding == null) {
                partialBmgmAddonSummaryBinding =
                    PartialBmgmAddOnSummaryBinding.bind(this.itemView.findViewById(R.id.itemBmgmAddonViewStub))
            }
        }

        private fun setupDividerAddonSummary(uiModel: ProductBmgmSectionUiModel.ProductUiModel?) {
            binding.dividerAddOn.showWithCondition(uiModel?.addOnSummaryUiModel != null)
        }

        private fun setBmgmItemThumbnail(thumbnailUrl: String) {
            binding.ivItemOrderBmgmThumbnail.loadImage(thumbnailUrl)
        }

        private fun setBmgmItemProductName(productName: String) {
            binding.tvItemOrderBmgmProductName.text = productName
        }

        private fun setBmgmItemProductPriceQuantity(quantity: Int, priceText: String) {
            binding.tvItemOrderBmgmProductPriceQuantity.text = itemView.context.getString(
                R.string.label_product_price_and_quantity,
                quantity,
                priceText
            )
        }

        private fun setupBmgmItemProductNote(productNote: String) {
            binding.tvItemOrderBmgmProductNote.run {
                showWithCondition(productNote.isNotBlank())
                text = composeItalicNote(productNote)
            }
        }

        private fun setItemOnClickListener(
            item: ProductBmgmSectionUiModel.ProductUiModel
        ) {
            binding.containerProductInfo.setOnClickListener {
                listener.onBmgmItemClicked(item)
            }
        }

        override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
            listener.onCopyAddOnDescription(label, description)
        }

        interface Listener {
            fun onCopyAddOnDescription(label: String, description: CharSequence)
            fun onBmgmItemClicked(item: ProductBmgmSectionUiModel.ProductUiModel)
        }
    }
}
