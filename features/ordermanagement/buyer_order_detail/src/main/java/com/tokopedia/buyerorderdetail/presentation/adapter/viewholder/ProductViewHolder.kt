package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.databinding.PartialItemBuyerOrderDetailAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.imageassets.utils.loadProductImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

open class ProductViewHolder(
    itemView: View?,
    private val listener: PartialProductItemViewHolder.ProductViewListener,
    private val navigator: BuyerOrderDetailNavigator
) : BaseToasterViewHolder<ProductListUiModel.ProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_item
    }

    private var ivBuyerOrderDetailProductThumbnail: ImageUnify? = null

    protected var btnBuyerOrderDetailBuyProductAgain: UnifyButton? = null

    private var element: ProductListUiModel.ProductUiModel? = null

    private var partialProductItemViewHolder: PartialProductItemViewHolder? = null

    private var partialProductAddonViewHolder: PartialProductAddonViewHolder? = null

    private var partialProductItemViewStub: View? = null

    private var partialItemBuyerOrderDetailAddonsBinding: PartialItemBuyerOrderDetailAddonsBinding? =
        null

    override fun bind(element: ProductListUiModel.ProductUiModel?) {
        element?.let {
            this.element = it
            setupProductList(it)
            setupAddonSection(it.addonsListUiModel)
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
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupProductList(item: ProductListUiModel.ProductUiModel) {
        inflateViewStub()
        partialProductItemViewHolder = PartialProductItemViewHolder(
            itemView,
            partialProductItemViewStub,
            listener,
            navigator,
            item
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

    private fun setupAddonSection(addonsListUiModel: AddonsListUiModel?) {
        val addonsViewStub: View = itemView.findViewById(R.id.itemBomDetailAddonsViewStub)
        if (addonsListUiModel?.addonsItemList?.isNotEmpty() == true) {
            if (addonsViewStub is ViewStub) addonsViewStub.inflate() else addonsViewStub.show()
            setupAddonsBinding()
            partialProductAddonViewHolder =
                partialItemBuyerOrderDetailAddonsBinding?.let { PartialProductAddonViewHolder(it) }
            partialProductAddonViewHolder?.bindViews(addonsListUiModel)
        } else {
            addonsViewStub.hide()
        }
    }

    private fun setupAddonsBinding() {
        if (partialItemBuyerOrderDetailAddonsBinding == null) {
            partialItemBuyerOrderDetailAddonsBinding =
                PartialItemBuyerOrderDetailAddonsBinding.bind(this.itemView.findViewById(R.id.itemBomDetailAddonsViewStub))
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
