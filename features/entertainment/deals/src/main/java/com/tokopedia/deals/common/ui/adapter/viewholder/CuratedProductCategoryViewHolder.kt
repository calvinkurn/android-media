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
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_deals_curated_product_category.view.*

class CuratedProductCategoryViewHolder(
        itemView: View,
        private val curatedProductCategoryListener: CuratedProductCategoryListener
) : BaseViewHolder(itemView), ProductCardListener {

    private var productCardAdapter: DealsProductCardAdapter = DealsProductCardAdapter(this)
    var sectionTitle: String = ""

    fun bindData(curatedProductCategory: CuratedProductCategoryDataView) {
        sectionTitle = curatedProductCategory.title
        if (!curatedProductCategory.isLoaded) {
            itemView.contentCuratedProductShimmering.show()
            itemView.content_curated_product_category_title.hide()
        } else {
            itemView.run {
                contentCuratedProductShimmering.hide()
                content_curated_product_category_title.show()

                txt_curated_product_category_title.text = curatedProductCategory.title

                if (curatedProductCategory.subtitle.isNotEmpty()) txt_curated_product_category_subtitle.text = curatedProductCategory.subtitle
                else txt_curated_product_category_subtitle.hide()

                btn_curated_product_category_see_all.apply {
                    visibility = if (curatedProductCategory.hasSeeAllButton) View.VISIBLE else View.GONE
                    setOnClickListener {
                        curatedProductCategoryListener.onSeeAllProductClicked(
                                curatedProductCategory,
                                adapterPosition
                        )
                    }
                }
                lst_curated_product_category_card.apply {
                    layoutManager = GridLayoutManager(context, PRODUCT_SPAN_COUNT)
                    adapter = productCardAdapter
                    productCardAdapter.productCards = curatedProductCategory.productCards
                }
                addOnImpressionListener(curatedProductCategory, {
                    curatedProductCategoryListener.onImpressionCuratedProduct(curatedProductCategory,position)
                })
                ViewCompat.setNestedScrollingEnabled(lst_curated_product_category_card, false)
            }
        }
    }

    override fun onImpressionProduct(productCardDataView: ProductCardDataView, productItemPosition: Int, page:Int) {
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