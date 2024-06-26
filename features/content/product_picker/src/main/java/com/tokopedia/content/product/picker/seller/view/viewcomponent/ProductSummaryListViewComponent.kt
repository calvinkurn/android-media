package com.tokopedia.content.product.picker.seller.view.viewcomponent

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.product.picker.R
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.view.adapter.ProductSummaryAdapter
import com.tokopedia.content.product.picker.seller.view.viewholder.ProductSummaryViewHolder
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on February 07, 2022
 */
internal class ProductSummaryListViewComponent(
    private val view: RecyclerView,
    listener: Listener
) : ViewComponent(view) {

    private val adapter = ProductSummaryAdapter(object : ProductSummaryViewHolder.Body.Listener {
        override fun onProductDeleteClicked(product: ProductUiModel) {
            listener.onProductDeleteClicked(product)
        }

        override fun onPinClicked(product: ProductUiModel) {
            listener.onPinClicked(product)
        }

        override fun onImpressPinnedProduct(product: ProductUiModel) {
            listener.onImpressPinnedProduct(product)
        }
    })

    init {
        view.adapter = adapter
        view.layoutManager = LinearLayoutManager(view.context)
    }

    fun setProductList(
        productSectionList: List<ProductTagSectionUiModel>,
        isEligibleForPin: Boolean,
        isProductNumerationShown: Boolean,
        selectedAccount: ContentAccountUiModel
    ) {
        var productIdx = 0 // Product Index
        val finalList = buildList {
            productSectionList.forEachIndexed { idx, section ->
                /** Don't display section title if its at the top && title is empty */
                if (idx != 0 || section.name.isNotEmpty()) {
                    add(ProductSummaryAdapter.Model.Header(section.name, section.campaignStatus))
                }

                addAll(
                    section.products.map { product ->
                        productIdx += 1 // if numeration is not available / in prep page use hard-coded
                        ProductSummaryAdapter.Model.Body(
                            product = product.copy(number = product.number.ifBlank { "$productIdx" }),
                            isEligibleForPin = isEligibleForPin,
                            isNumerationShown = isProductNumerationShown,
                            selectedAccount = selectedAccount
                        )
                    }
                )
            }
        }

        adapter.setItemsAndAnimateChanges(finalList)
    }

    fun getProductCommissionCoachMark(
        firstProductCommissionView: (view: View) -> Unit
    ) {
        view.addOneTimeGlobalLayoutListener {
            adapter.getItems().forEachIndexed { index, _ ->
                val holder = view.findViewHolderForAdapterPosition(index)
                val view = holder?.itemView?.findViewById<Typography>(R.id.tv_commission)
                if (view?.isVisible == true) {
                    firstProductCommissionView.invoke(view)
                    return@addOneTimeGlobalLayoutListener
                }
            }
        }
    }

    interface Listener {
        fun onProductDeleteClicked(product: ProductUiModel)
        fun onPinClicked(product: ProductUiModel)
        fun onImpressPinnedProduct(product: ProductUiModel)
    }
}
