package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.listener.OwocRecyclerviewPoolListener
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.ImageUnify

class OwocProductViewHolder(
    itemView: View?,
    private val recyclerviewPoolListener: OwocRecyclerviewPoolListener
) : AbstractViewHolder<OwocProductListUiModel.ProductUiModel>(itemView),
    BmgmAddOnViewHolder.Listener,
    BmgmAddOnSummaryViewHolder.Delegate.Mediator,
    BmgmAddOnSummaryViewHolder.Delegate by BmgmAddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_item
    }

    private var ivBuyerOrderDetailProductThumbnail: ImageUnify? = null

    private var element: OwocProductListUiModel.ProductUiModel? = null

    private var owocPartialProductItemViewHolder: OwocPartialProductItemViewHolder? = null

    private var partialProductItemViewStub: View? = null

    private val addOnDivider: DividerUnify? = itemView?.findViewById(R.id.dividerBomDetailProduct)

    private val productCardContainer: CardUnify? = itemView?.findViewById(R.id.cardBuyerOrderDetailProduct)

    init {
        registerAddOnSummaryDelegate(this)
    }

    override fun bind(element: OwocProductListUiModel.ProductUiModel?) {
        element?.let {
            this.element = it
            setupProductList(it)
            bindAddonSummary(it.addOnSummaryUiModel)
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
                        bindAddonSummary(newItem.addOnSummaryUiModel)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onCopyAddOnDescriptionClicked(label: String, description: CharSequence) {
    }

    override fun onAddOnsBmgmExpand(isExpand: Boolean, addOnsIdentifier: String) {
    }

    override fun onAddOnsInfoLinkClicked(infoLink: String, type: String) {
    }

    override fun onAddOnClicked(addOn: AddOnSummaryUiModel.AddonItemUiModel) {}

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(R.id.itemOwocAddonsViewStub)
    }

    override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
        return recyclerviewPoolListener.parentPool
    }

    override fun getAddOnSummaryListener(): BmgmAddOnViewHolder.Listener {
        return this
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

    private fun setupProductThumbnail(productThumbnailUrl: String) {
        ivBuyerOrderDetailProductThumbnail?.setImageUrl(productThumbnailUrl)
    }
}
