package com.tokopedia.order_management_common.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.constants.OrderManagementConstants
import com.tokopedia.order_management_common.databinding.ItemOrderProductBmgmListItemBinding
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.adapter.diffutil.ProductBmgmItemDiffUtilCallback
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.composeItalicNote
import com.tokopedia.order_management_common.util.mapButtonType
import com.tokopedia.order_management_common.util.mapButtonVariant

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
            super.onBindViewHolder(holder, position, payloads)
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
                setupInsurance(it.insurance)
                setBmgmItemThumbnail(it.thumbnailUrl)
                setBmgmItemProductName(it.productName)
                setBmgmItemProductPriceQuantity(it.quantity, it.productPriceText)
                setupBmgmItemProductNote(it.productNote)
                setupAddonSection(it.addOnSummaryUiModel)
                setupDividerAddonSummary(it)
                setItemOnClickListener(it)
                setupBmgmItemButton(it.button, it.isProcessing == true)
                setupImpressionListener(it)
            }
        }

        private fun setupImpressionListener(model: ProductBmgmSectionUiModel.ProductUiModel) {
            itemView.addOnImpressionListener(model.impressHolder) {
                listener.onBmgmItemImpressed(model)
            }
        }

        private fun setupBmgmItemButton(actionButton: ActionButtonsUiModel.ActionButton?, processing: Boolean) {
            binding.btnItemBomDetailBmgmAction.run {
                if (actionButton == null) {
                    gone()
                } else {
                    isLoading = processing
                    text = actionButton.label
                    buttonVariant = mapButtonVariant(actionButton.variant)
                    buttonType = mapButtonType(actionButton.type)
                    showWithCondition(actionButton.label.isNotBlank())
                    setOnClickListener {
                        onItemActionClicked(actionButton.key)
                    }
                }
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

        private fun setupInsurance(insurance: ProductBmgmSectionUiModel.ProductUiModel.Insurance?) {
            with(binding) {
                if (insurance == null) {
                    ivItemBomDetailBmgmInsuranceLogo.gone()
                    tvItemBomDetailBmgmInsuranceLabel.gone()
                } else {
                    ivItemBomDetailBmgmInsuranceLogo.run {
                        loadImage(insurance.logoUrl)
                        show()
                    }
                    tvItemBomDetailBmgmInsuranceLabel.run {
                        text = insurance.label
                        show()
                    }
                }
            }
        }

        private fun setItemOnClickListener(
            item: ProductBmgmSectionUiModel.ProductUiModel
        ) {
            binding.containerProductInfo.setOnClickListener {
                listener.onBmgmItemClicked(item)
            }
        }

        private fun onItemActionClicked(key: String) {
            element?.let {
                when (key) {
                    OrderManagementConstants.OrderDetailActionButtonKey.BUY_AGAIN -> {
                        listener.onBmgmItemAddToCart(it)
                    }
                    OrderManagementConstants.OrderDetailActionButtonKey.SEE_SIMILAR_PRODUCTS -> {
                        listener.onBmgmItemSeeSimilarProducts(it)
                    }
                    OrderManagementConstants.OrderDetailActionButtonKey.WARRANTY_CLAIM -> {
                        listener.onBmgmItemWarrantyClaim(it)
                    }
                    else -> {}
                }
            }
        }

        override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
            listener.onCopyAddOnDescription(label, description)
        }

        interface Listener {
            fun onCopyAddOnDescription(label: String, description: CharSequence)
            fun onBmgmItemClicked(item: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemAddToCart(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemSeeSimilarProducts(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemWarrantyClaim(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemImpressed(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        }
    }
}
