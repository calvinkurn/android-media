package com.tokopedia.order_management_common.presentation.viewholder

import android.view.View
import android.view.ViewStub
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.order_management_common.R
import com.tokopedia.order_management_common.constants.OrderManagementConstants
import com.tokopedia.order_management_common.databinding.ItemOrderProductBmgmSectionBinding
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.adapter.ProductBmgmItemAdapter
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel
import com.tokopedia.order_management_common.util.RecyclerViewItemDivider
import com.tokopedia.order_management_common.util.runSafely
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.order_management_common.R as order_management_commonR

class BmgmSectionViewHolder(
    view: View?,
    private val listener: Listener,
    private val recyclerViewSharedPool: RecyclerView.RecycledViewPool
) : AbstractViewHolder<ProductBmgmSectionUiModel>(view),
    ProductBmgmItemAdapter.ViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_order_product_bmgm_section

        private const val ITEM_DECORATION_VERTICAL_MARGIN = 12
        private const val ITEM_DECORATION_HORIZONTAL_MARGIN = 16
    }

    private val productBenefitListener by lazyThreadSafetyNone { ProductBenefitListener() }

    private val bmgmItemAdapter = ProductBmgmItemAdapter(this, recyclerViewSharedPool)

    private val binding = ItemOrderProductBmgmSectionBinding.bind(itemView)

    private var productBenefitViewHolder: BmgmAddOnSummaryViewHolder? = null

    private var productBenefitBinding: PartialBmgmAddOnSummaryBinding? = null

    init {
        setupBundleAdapter()
    }

    override fun bind(element: ProductBmgmSectionUiModel) {
        setupBmgmHeader(element.bmgmName, element.bmgmIconUrl)
        setupBmgmItems(element.bmgmItemList)
        setupBmgmTotalPrice(element.totalPriceText)
        setupBmgmTotalPriceReductionInfo(element.totalPriceReductionInfoText)
        binding.containerParentBmgm.setupCardDarkMode()
        setupProductBenefit(element.productBenefits)
    }

    private fun setupProductBenefit(productBenefits: AddOnSummaryUiModel?) {
        val addonsViewStub: View = itemView.findViewById(R.id.itemBmgmProductBenefitViewStub)
        if (productBenefits != null) {
            if (addonsViewStub is ViewStub) {
                productBenefitBinding = PartialBmgmAddOnSummaryBinding.bind(addonsViewStub.inflate())
            }
            productBenefitViewHolder =
                productBenefitBinding?.let {
                    BmgmAddOnSummaryViewHolder(
                        bmgmAddOnListener = productBenefitListener,
                        binding = it,
                        // don't pass recyclerViewSharedPool here for now because current
                        // recyclerViewSharedPool might contain BmgmAddOnViewHolder for AddOn so we
                        // can't share it with BmgmAddOnViewHolder for GWP
                        recyclerViewSharedPool = null
                    )
                }
            productBenefitViewHolder?.bind(productBenefits)
            binding.dividerProductBenefit.show()
        } else {
            if (addonsViewStub !is ViewStub) addonsViewStub.gone()
            binding.dividerProductBenefit.gone()
        }
    }

    override fun bind(
        element: ProductBmgmSectionUiModel?,
        payloads: MutableList<Any>
    ) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ProductBmgmSectionUiModel && newItem is ProductBmgmSectionUiModel) {
                    if (oldItem.bmgmName != newItem.bmgmName || oldItem.bmgmIconUrl != newItem.bmgmIconUrl) {
                        setupBmgmHeader(newItem.bmgmName, newItem.bmgmIconUrl)
                    }
                    if (oldItem.bmgmItemList != newItem.bmgmItemList) {
                        setupBmgmItems(newItem.bmgmItemList)
                    }
                    if (oldItem.totalPriceText != newItem.totalPriceText) {
                        setupBmgmTotalPrice(newItem.totalPriceText)
                    }
                    if (oldItem.totalPriceReductionInfoText != newItem.totalPriceReductionInfoText) {
                        setupBmgmTotalPriceReductionInfo(newItem.totalPriceReductionInfoText)
                    }
                    if (oldItem.productBenefits != newItem.productBenefits) {
                        setupProductBenefit(newItem.productBenefits)
                    }
                    return
                }
            }
        }
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
        listener.onAddOnsInfoLinkClicked(infoLink, type)
    }

    override fun onAddOnsBmgmExpand(isExpand:Boolean, addOnsIdentifier: String) {
        listener.onAddOnsBmgmExpand(isExpand, addOnsIdentifier)
    }

    override fun onCopyAddOnDescription(label: String, description: CharSequence) {
        listener.onCopyAddOnDescription(label, description)
    }

    override fun onBmgmItemClicked(item: ProductBmgmSectionUiModel.ProductUiModel) {
        if (item.orderId != OrderManagementConstants.WAITING_INVOICE_ORDER_ID) {
            listener.onBmgmItemClicked(item)
        } else {
            showToaster(getString(order_management_commonR.string.om_error_message_cant_open_snapshot_when_waiting_invoice))
        }
    }

    override fun onBmgmItemAddToCart(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        listener.onBmgmItemAddToCart(uiModel)
    }

    override fun onBmgmItemSeeSimilarProducts(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        listener.onBmgmItemSeeSimilarProducts(uiModel)
    }

    override fun onBmgmItemWarrantyClaim(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        listener.onBmgmItemWarrantyClaim(uiModel)
    }

    override fun onBmgmItemImpressed(uiModel: ProductBmgmSectionUiModel.ProductUiModel) {
        listener.onBmgmItemImpressed(uiModel)
    }

    private fun showToaster(message: String) {
        itemView.parent?.parent?.parent?.let {
            if (it is View) {
                Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    private fun setupBundleAdapter() {
        binding.rvOrderBmgm.run {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = bmgmItemAdapter
            setupRecyclerViewItemDecoration()
        }
    }

    private fun setupRecyclerViewItemDecoration() {
        binding.rvOrderBmgm.run {
            if (itemDecorationCount.isZero()) {
                val dividerDrawable = runSafely {
                    MethodChecker.getDrawable(context, R.drawable.om_detail_add_on_dash_divider)
                }
                addItemDecoration(
                    RecyclerViewItemDivider(
                        dividerDrawable,
                        ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                        ITEM_DECORATION_VERTICAL_MARGIN.toPx(),
                        ITEM_DECORATION_HORIZONTAL_MARGIN.toPx()
                    )
                )
            }
        }
    }

    private fun setupBmgmHeader(bmgmName: String, bmgmIconUrl: String) {
        binding.tvOrderBmgmName.text = bmgmName
        binding.ivOrderBmgmIcon.loadImage(bmgmIconUrl)
    }

    private fun setupBmgmItems(bmgmItemList: List<ProductBmgmSectionUiModel.ProductUiModel>) {
        bmgmItemAdapter.setItems(bmgmItemList)
    }

    private fun setupBmgmTotalPrice(price: String) {
        binding.tvOrderBmgmPriceValue.text = price
    }

    private fun setupBmgmTotalPriceReductionInfo(totalPriceReductionInfoText: String) {
        binding.tvOrderBmgmPriceMoreInfoLabel.text = totalPriceReductionInfoText
    }

    private inner class ProductBenefitListener : BmgmAddOnViewHolder.Listener {
        override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
            // noop
        }

        override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
            listener.onBmgmProductBenefitExpand(isExpand, addOnsIdentifier)
        }

        override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
            // noop
        }

        override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {
            listener.onBmgmProductBenefitClicked(addOn)
        }
    }

    interface Listener {
        fun onAddOnsInfoLinkClicked(infoLink: String, type: String)
        fun onAddOnsBmgmExpand(isExpand:Boolean, addOnsIdentifier: String)
        fun onCopyAddOnDescription(label: String, description: CharSequence)
        fun onBmgmItemClicked(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        fun onBmgmItemAddToCart(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        fun onBmgmItemSeeSimilarProducts(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        fun onBmgmItemWarrantyClaim(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        fun onBmgmItemImpressed(uiModel: ProductBmgmSectionUiModel.ProductUiModel)
        fun onBmgmProductBenefitExpand(isExpand:Boolean, identifier: String)
        fun onBmgmProductBenefitClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel)
    }
}
