package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import android.view.ViewStub
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.PartialItemOwocAddonsBinding
import com.tokopedia.buyerorderdetail.presentation.model.AddonsListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify

class OwocProductViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator
) : BaseToasterViewHolder<ProductListUiModel.ProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_owoc_product_list_item
    }

    private var ivBuyerOrderDetailProductThumbnail: ImageUnify? = null

    private var element: ProductListUiModel.ProductUiModel? = null

    private var owocPartialProductItemViewHolder: OwocPartialProductItemViewHolder? = null

    private var owocPartialProductAddonViewHolder: OwocPartialProductAddonViewHolder? = null

    private var partialProductItemViewStub: View? = null

    private var partialItemOwocAddonsBinding: PartialItemOwocAddonsBinding? =
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
                    owocPartialProductItemViewHolder?.bindProductItemPayload(oldItem, newItem)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    private fun setupProductList(item: ProductListUiModel.ProductUiModel) {
        inflateViewStub()
        owocPartialProductItemViewHolder = OwocPartialProductItemViewHolder(
            itemView,
            partialProductItemViewStub,
            navigator,
            item
        )
        setupProductThumbnail(item.productThumbnailUrl)
    }

    private fun inflateViewStub() {
        val productListViewStub: View = itemView.findViewById(R.id.itemOwocProductViewStub)
        if (productListViewStub is ViewStub) {
            partialProductItemViewStub = productListViewStub.inflate()
            ivBuyerOrderDetailProductThumbnail =
                partialProductItemViewStub?.findViewById(R.id.ivOwocProductThumbnail)
        } else {
            productListViewStub.show()
        }
    }

    private fun setupAddonSection(addonsListUiModel: AddonsListUiModel?) {
        val addonsViewStub: View = itemView.findViewById(R.id.itemOwocAddonsViewStub)
        if (addonsListUiModel?.addonsItemList?.isNotEmpty() == true) {
            if (addonsViewStub is ViewStub) addonsViewStub.inflate() else addonsViewStub.show()
            setupAddonsBinding()
            owocPartialProductAddonViewHolder =
                partialItemOwocAddonsBinding?.let { OwocPartialProductAddonViewHolder(it) }
            owocPartialProductAddonViewHolder?.bindViews(addonsListUiModel)
        } else {
            addonsViewStub.hide()
        }
    }

    private fun setupAddonsBinding() {
        if (partialItemOwocAddonsBinding == null) {
            partialItemOwocAddonsBinding =
                PartialItemOwocAddonsBinding.bind(this.itemView.findViewById(R.id.itemOwocAddonsViewStub))
        }
    }

    private fun setupProductThumbnail(productThumbnailUrl: String) {
        ivBuyerOrderDetailProductThumbnail?.apply {
            setImageUrl(productThumbnailUrl)
        }
    }
}
