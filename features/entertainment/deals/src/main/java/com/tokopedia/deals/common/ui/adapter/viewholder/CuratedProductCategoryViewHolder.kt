package com.tokopedia.deals.common.ui.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.common.listener.CuratedProductCategoryListener
import com.tokopedia.deals.common.listener.ProductCardListener
import com.tokopedia.deals.common.ui.adapter.DealsProductCardAdapter
import com.tokopedia.deals.common.ui.dataview.CuratedProductCategoryDataView
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.deals.databinding.ItemDealsCuratedProductCategoryBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

class CuratedProductCategoryViewHolder(
    itemView: View,
    private val curatedProductCategoryListener: CuratedProductCategoryListener
) : BaseViewHolder(itemView), ProductCardListener {

    private var productCardAdapter: DealsProductCardAdapter = DealsProductCardAdapter(this)
    var sectionTitle: String = ""

    fun bindData(curatedProductCategory: CuratedProductCategoryDataView) {
        val binding = ItemDealsCuratedProductCategoryBinding.bind(itemView)
        sectionTitle = curatedProductCategory.title
        if (!curatedProductCategory.isLoaded) {
            binding.contentCuratedProductShimmering.root.show()
            binding.contentCuratedProductCategoryTitle.hide()
        } else {
            binding.run {
                contentCuratedProductShimmering.root.hide()
                contentCuratedProductCategoryTitle.show()

                txtCuratedProductCategoryTitle.text = curatedProductCategory.title

                if (curatedProductCategory.subtitle.isNotEmpty())
                    txtCuratedProductCategorySubtitle.text = curatedProductCategory.subtitle
                else txtCuratedProductCategorySubtitle.hide()

                btnCuratedProductCategorySeeAll.apply {
                    visibility =
                        if (curatedProductCategory.hasSeeAllButton) View.VISIBLE else View.GONE
                    setOnClickListener {
                        curatedProductCategoryListener.onSeeAllProductClicked(
                            curatedProductCategory,
                            adapterPosition
                        )
                    }
                }
                lstCuratedProductCategoryCard.apply {
                    layoutManager = GridLayoutManager(context, PRODUCT_SPAN_COUNT)
                    adapter = productCardAdapter
                    productCardAdapter.productCards = curatedProductCategory.productCards
                }
                binding.root.addOnImpressionListener(curatedProductCategory, {
                    curatedProductCategoryListener.onImpressionCuratedProduct(
                        curatedProductCategory,
                        position
                    )
                })
                ViewCompat.setNestedScrollingEnabled(lstCuratedProductCategoryCard, false)
            }
        }
    }

    override fun onImpressionProduct(
        productCardDataView: ProductCardDataView,
        productItemPosition: Int,
        page: Int
    ) {
        /* do nothing*/
    }

    override fun onProductClicked(
        itemView: View,
        productCardDataView: ProductCardDataView,
        position: Int
    ) {
        curatedProductCategoryListener.onProductClicked(productCardDataView, position, sectionTitle)
    }

    companion object {
        private const val PRODUCT_SPAN_COUNT = 2
        val LAYOUT = R.layout.item_deals_curated_product_category
    }
}