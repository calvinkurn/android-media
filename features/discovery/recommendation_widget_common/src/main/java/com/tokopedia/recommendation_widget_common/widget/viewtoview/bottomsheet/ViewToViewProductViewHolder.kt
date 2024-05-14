package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.databinding.ItemViewToViewBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class ViewToViewProductViewHolder(
    private val listener: ViewToViewListener,
    view: View
) : RecyclerView.ViewHolder(view) {

    private var binding: ItemViewToViewBinding? by viewBinding()

    private var recommendationItem: RecommendationItem? = null

    fun bind(data: ViewToViewDataModel) {
        val binding = binding ?: return
        this.recommendationItem = data.recommendationItem
        with(binding.root) {
            setProductModel(data.productModel)
            setOnClickListener {
                listener.onProductClicked(data, data.recommendationItem.position, className)
            }
            setAddToCartOnClickListener {
                listener.onAddToCartClicked(data, data.recommendationItem.position)
            }
            setAddVariantClickListener {
                listener.onAddToCartClicked(data, data.recommendationItem.position)
            }
            setVisibilityPercentListener(
                isTopAds = data.recommendationItem.isTopAds,
                eventListener = object : ProductConstraintLayout.OnVisibilityPercentChanged {
                    override fun onShow() {
                        data.recommendationItem.sendShowAdsByteIo(itemView.context)
                    }

                    override fun onShowOver(maxPercentage: Int) {
                        data.recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
                    }
                }
            )
            addOnImpressionListener(data.recommendationItem) {
                listener.onProductImpressed(data, data.recommendationItem.position, className)
            }
        }
    }

    fun type() = LAYOUT

    companion object {
        val LAYOUT = R.layout.item_view_to_view
        private const val className = "com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet.ViewToViewProductViewHolder"
    }
}
