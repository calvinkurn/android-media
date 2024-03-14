package com.tokopedia.order_management_common.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.constants.OrderManagementConstants
import com.tokopedia.order_management_common.databinding.ItemOrderProductBmgmListItemBinding
import com.tokopedia.order_management_common.presentation.adapter.diffutil.ProductBmgmItemDiffUtilCallback
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.presentation.viewholder.AddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder
import com.tokopedia.order_management_common.util.composeItalicNote
import com.tokopedia.order_management_common.util.mapButtonType
import com.tokopedia.order_management_common.util.mapButtonVariant

class ProductBmgmItemAdapter(
    private val productListener: ViewHolder.Listener,
    private val addOnListener: AddOnViewHolder.Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : RecyclerView.Adapter<ProductBmgmItemAdapter.ViewHolder>() {

    private val itemList = arrayListOf<ProductBmgmSectionUiModel.ProductUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderProductBmgmListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, productListener, addOnListener, recyclerViewSharedPool)
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
        private val productListener: Listener,
        private val addOnListener: AddOnViewHolder.Listener,
        private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(binding.root),
        AddOnSummaryViewHolder.Delegate.Mediator,
        AddOnSummaryViewHolder.Delegate by AddOnSummaryViewHolder.Delegate.Impl() {

        private var element: ProductBmgmSectionUiModel.ProductUiModel? = null

        init {
            registerAddOnSummaryDelegate(this)
        }

        override fun getAddOnSummaryLayout(): View? {
            return itemView.findViewById(R.id.itemBmgmAddonViewStub)
        }

        override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
            return recyclerViewSharedPool
        }

        override fun getAddOnSummaryListener(): AddOnViewHolder.Listener {
            return addOnListener
        }

        fun bind(model: ProductBmgmSectionUiModel.ProductUiModel?) {
            model?.let {
                element = it
                setupInsurance(it.insurance)
                setBmgmItemThumbnail(it.thumbnailUrl)
                setBmgmItemProductName(it.productName)
                setBmgmItemProductPriceQuantity(it.quantity, it.productPriceText)
                setupBmgmItemProductNote(it.productNote)
                bindAddonSummary(it.addOnSummaryUiModel)
                setItemOnClickListener(it)
                setupBmgmItemButton(it.button, it.isProcessing == true)
                setupImpressionListener(it)
            }
        }

        private fun setupImpressionListener(model: ProductBmgmSectionUiModel.ProductUiModel) {
            itemView.addOnImpressionListener(model.impressHolder) {
                productListener.onBmgmItemImpressed(model)
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
                productListener.onBmgmItemClicked(item)
            }
        }

        private fun onItemActionClicked(key: String) {
            element?.let {
                when (key) {
                    OrderManagementConstants.OrderDetailActionButtonKey.BUY_AGAIN -> {
                        productListener.onBmgmItemAddToCart(it)
                    }
                    OrderManagementConstants.OrderDetailActionButtonKey.SEE_SIMILAR_PRODUCTS -> {
                        productListener.onBmgmItemSeeSimilarProducts(it)
                    }
                    OrderManagementConstants.OrderDetailActionButtonKey.WARRANTY_CLAIM -> {
                        productListener.onBmgmItemWarrantyClaim(it)
                    }
                    else -> {}
                }
            }
        }

        interface Listener {
            fun onBmgmItemClicked(item: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemAddToCart(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemSeeSimilarProducts(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemWarrantyClaim(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
            fun onBmgmItemImpressed(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        }
    }
}
