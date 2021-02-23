package com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohTypeData
import com.tokopedia.buyerorder.unifiedhistory.list.view.adapter.UohItemAdapter
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohRecommendationItemViewHolder(itemView: View, private val actionListener: UohItemAdapter.ActionListener?) : UohItemAdapter.BaseViewHolder<UohTypeData>(itemView) {
    private val productCardView: ProductCardGridView by lazy { itemView.findViewById<ProductCardGridView>(R.id.uoh_product_item) }

    override fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is RecommendationItem) {
            productCardView.run {
                setProductModel(
                    item.dataObject.toProductCardModel(hasAddToCartButton = true)
                )

                setAddToCartOnClickListener {
                    actionListener?.atcRecommendationItem(item.dataObject)
                }

                setOnClickListener {
                    actionListener?.trackProductClickRecommendation(item.dataObject, position)
                }

                setImageProductViewHintListener(
                        item.dataObject,
                        object : ViewHintListener {
                            override fun onViewHint() {
                                actionListener?.trackProductViewRecommendation(
                                        item.dataObject, position
                                )
                            }
                        }
                )
            }
        }
    }
}