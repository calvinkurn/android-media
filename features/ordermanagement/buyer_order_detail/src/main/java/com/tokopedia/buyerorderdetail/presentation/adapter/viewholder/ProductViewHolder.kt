package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.order_management_common.presentation.uimodel.ActionButtonsUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnSummaryViewHolder
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder
import com.tokopedia.order_management_common.util.setupCardDarkMode
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

open class ProductViewHolder(
    itemView: View?,
    private val listener: PartialProductItemViewHolder.ProductViewListener,
    private val bottomSheetListener: PartialProductItemViewHolder.ShareProductBottomSheetListener,
    private val addOnListener: BmgmAddOnViewHolder.Listener,
    private val navigator: BuyerOrderDetailNavigator,
) : BaseToasterViewHolder<ProductListUiModel.ProductUiModel>(itemView),
    BmgmAddOnSummaryViewHolder.Delegate.Mediator,
    BmgmAddOnSummaryViewHolder.Delegate by BmgmAddOnSummaryViewHolder.Delegate.Impl() {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_item
    }

    private var ivBuyerOrderDetailProductThumbnail: ImageUnify? = null
    private var element: ProductListUiModel.ProductUiModel? = null
    private var partialProductItemViewHolder: PartialProductItemViewHolder? = null
    private var partialProductItemViewStub: View? = null
    private val cardContainer: CardUnify? = itemView?.findViewById(R.id.cardBuyerOrderDetailProduct)
    protected var btnBuyerOrderDetailBuyProductAgain: UnifyButton? = null

    override fun bind(element: ProductListUiModel.ProductUiModel?) {
        element?.let {
            this.element = it
            setupProductList(it)
            registerAddOnSummaryDelegate(this)
            bindAddonSummary(it.addOnSummaryUiModel)
            cardContainer?.setupCardDarkMode()
        }
    }

    override fun bind(element: ProductListUiModel.ProductUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ProductListUiModel.ProductUiModel && newItem is ProductListUiModel.ProductUiModel) {
                    if (oldItem.productThumbnailUrl != newItem.productThumbnailUrl) {
                        setupProductThumbnail(newItem.productThumbnailUrl)
                    }
                    partialProductItemViewHolder?.bindProductItemPayload(oldItem, newItem)
                    if (oldItem.button != newItem.button || oldItem.isProcessing != newItem.isProcessing) {
                        setupButton(newItem.button, newItem.isProcessing)
                    }
                    if (oldItem.addOnSummaryUiModel != newItem.addOnSummaryUiModel) {
                        registerAddOnSummaryDelegate(this)
                        bindAddonSummary(newItem.addOnSummaryUiModel)
                    }
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun getAddOnSummaryLayout(): View? {
        return itemView.findViewById(R.id.layout_bom_detail_product_add_on)
    }

    override fun getRecycleViewSharedPool(): RecyclerView.RecycledViewPool? {
        return null
    }

    override fun getAddOnSummaryListener(): BmgmAddOnViewHolder.Listener {
        return addOnListener
    }

    private fun setupProductList(item: ProductListUiModel.ProductUiModel) {
        inflateViewStub()
        partialProductItemViewHolder = PartialProductItemViewHolder(
            itemView,
            partialProductItemViewStub,
            listener,
            navigator,
            item,
            bottomSheetListener
        )
        setupProductThumbnail(item.productThumbnailUrl)
        setupButton(item.button, item.isProcessing)
    }

    private fun inflateViewStub() {
        val productListViewStub: View = itemView.findViewById(R.id.itemBomDetailProductViewStub)
        if (productListViewStub is ViewStub) {
            partialProductItemViewStub = productListViewStub.inflate()
            ivBuyerOrderDetailProductThumbnail =
                partialProductItemViewStub?.findViewById(R.id.ivBuyerOrderDetailProductThumbnail)
            btnBuyerOrderDetailBuyProductAgain =
                partialProductItemViewStub?.findViewById(R.id.btnBuyerOrderDetailBuyProductAgain)
        } else {
            productListViewStub.show()
        }
    }

    protected open fun setupProductThumbnail(productThumbnailUrl: String) {
        ivBuyerOrderDetailProductThumbnail?.apply {
            loadProductImage(
                url = productThumbnailUrl,
                archivedUrl = TokopediaImageUrl.IMG_ARCHIVED_PRODUCT_SMALL
            )
        }
    }

    protected open fun setupButton(
        showBuyAgainButton: ActionButtonsUiModel.ActionButton,
        processing: Boolean
    ) {
        btnBuyerOrderDetailBuyProductAgain?.apply {
            isLoading = processing
            text = showBuyAgainButton.label
            buttonVariant = Utils.mapButtonVariant(showBuyAgainButton.variant)
            buttonType = Utils.mapButtonType(showBuyAgainButton.type)
            showWithCondition(showBuyAgainButton.label.isNotBlank())
        }
    }
}
