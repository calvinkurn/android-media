package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohRecommendationItemBinding
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter

/**
 * Created by fwidjaja on 22/07/20.
 */
class UohRecommendationItemViewHolder(private val binding: UohRecommendationItemBinding, private val actionListener: UohItemAdapter.ActionListener?) : RecyclerView.ViewHolder(binding.root) {
    private val productCardView: ProductCardGridView by lazy { binding.uohProductItem }

    fun bind(item: UohTypeData, position: Int) {
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
                                item.dataObject,
                                position
                            )
                        }
                    }
                )
            }
        }
    }
}
