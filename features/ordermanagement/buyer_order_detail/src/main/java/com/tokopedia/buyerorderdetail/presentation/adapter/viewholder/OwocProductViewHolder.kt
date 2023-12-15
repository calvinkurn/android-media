package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.order_management_common.databinding.PartialBmgmAddOnSummaryBinding
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify

class OwocProductViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator?,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : AbstractViewHolder<OwocProductListUiModel.ProductUiModel>(itemView),
    BmgmAddOnViewHolder.Listener {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_item
    }

    private var ivBuyerOrderDetailProductThumbnail: ImageUnify? = null

    private var element: OwocProductListUiModel.ProductUiModel? = null

    private var owocPartialProductItemViewHolder: OwocPartialProductItemViewHolder? = null

    private var partialProductItemViewStub: View? = null

    private var addOnSummaryViewHolder: BmgmAddOnSummaryViewHolder? = null

    private var partialAddonSummaryBinding: PartialBmgmAddOnSummaryBinding? = null

    private val addOnContainer: FrameLayout? = itemView?.findViewById(R.id.itemOwocAddonsContainer)

    private val addOnDivider: DividerUnify? = itemView?.findViewById(R.id.dividerBomDetailProduct)

    private val productCardContainer: CardUnify? =
        itemView?.findViewById(R.id.cardBuyerOrderDetailProduct)

    override fun bind(element: OwocProductListUiModel.ProductUiModel?) {
        element?.let {
            this.element = it
            setupProductList(it)
            setupAddonSection(it.addOnSummaryUiModel)
            setupDividerAddonSummary(it.addOnSummaryUiModel)
            productCardContainer?.setupCardDarkMode()
        }
    }

    override fun bind(element: OwocProductListUiModel.ProductUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is OwocProductListUiModel.ProductUiModel && newItem is OwocProductListUiModel.ProductUiModel) {
                    if (oldItem.productThumbnailUrl != newItem.productThumbnailUrl) {
                        setupProductThumbnail(newItem.productThumbnailUrl)
                    }
                    if (oldItem.orderDetailId != newItem.orderDetailId) {
                        setupProductList(newItem)
                    }
                    if (oldItem.addOnSummaryUiModel != newItem.addOnSummaryUiModel) {
                        setupAddonSection(newItem.addOnSummaryUiModel)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupDividerAddonSummary(addOnSummaryUiModel: AddOnSummaryUiModel?) {
        addOnDivider?.showWithCondition(
            addOnSummaryUiModel != null
                    && addOnSummaryUiModel.addonItemList.isNotEmpty()
        )
    }

    private fun setupProductList(item: OwocProductListUiModel.ProductUiModel) {
        val productListViewStub: View = itemView.findViewById(R.id.itemOwocProductViewStub)

        if (item.orderDetailId.isEmpty()) {
            productListViewStub.hide()
        } else {
            inflateViewStub(productListViewStub)
            owocPartialProductItemViewHolder = OwocPartialProductItemViewHolder(
                itemView,
                partialProductItemViewStub,
                item
            )
            setupProductThumbnail(item.productThumbnailUrl)
        }
    }

    private fun inflateViewStub(productListViewStub: View) {
        if (productListViewStub is ViewStub) {
            partialProductItemViewStub = productListViewStub.inflate()
            ivBuyerOrderDetailProductThumbnail =
                partialProductItemViewStub?.findViewById(R.id.ivOwocProductThumbnail)
        } else {
            productListViewStub.show()
        }
    }

    private fun setupAddonSection(addOnSummaryUiModel: AddOnSummaryUiModel?) {
        val addonsViewStub: View = itemView.findViewById(R.id.itemOwocAddonsViewStub)
        if (addOnSummaryUiModel?.addonItemList?.isNotEmpty() == true) {
            if (addonsViewStub is ViewStub) addonsViewStub.inflate() else addonsViewStub.show()
            setupAddonsBinding()
            addOnSummaryViewHolder =
                partialAddonSummaryBinding?.let {
                    BmgmAddOnSummaryViewHolder(
                        this,
                        it,
                        recyclerviewPoolListener.parentPool
                    )
                }
            addOnContainer?.show()
            addOnSummaryViewHolder?.bind(addOnSummaryUiModel)
        } else {
            addOnContainer?.hide()
            addonsViewStub.hide()
        }
    }

    private fun setupAddonsBinding() {
        if (partialAddonSummaryBinding == null) {
            partialAddonSummaryBinding =
                PartialBmgmAddOnSummaryBinding.bind(this.itemView.findViewById(R.id.itemOwocAddonsViewStub))
        }
    }

    private fun setupProductThumbnail(productThumbnailUrl: String) {
        ivBuyerOrderDetailProductThumbnail?.apply {
            setImageUrl(productThumbnailUrl)
        }
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}
}
