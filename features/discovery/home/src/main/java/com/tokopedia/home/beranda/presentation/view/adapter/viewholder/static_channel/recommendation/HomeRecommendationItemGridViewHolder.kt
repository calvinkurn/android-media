package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.databinding.HomeFeedItemGridBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder.BaseRecommendationForYouViewHolder

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemGridViewHolder(
    itemView: View,
    private val listener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<HomeRecommendationItemDataModel>(
    itemView,
    HomeRecommendationItemDataModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_grid
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        HomeFeedItemGridBinding.bind(itemView)
    }

    override fun bind(element: HomeRecommendationItemDataModel) {
        setLayout(element, listener)
        setThreeDotsOnClickListener(element)
    }

    override fun bindPayload(newItem: HomeRecommendationItemDataModel?) {
        newItem?.let {
            setLayout(it, listener)
            setThreeDotsOnClickListener(it)
        }
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel,
        listener: HomeRecommendationListener
    ) {
        binding.productCardView.run {
            setProductModel(element.productCardModel)
            setImageProductViewHintListener(
                element,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener.onProductImpression(element, bindingAdapterPosition)
                    }
                }
            )
            setOnClickListener { listener.onProductClick(element, bindingAdapterPosition) }

            setThreeDotsOnClickListener {
                listener.onProductThreeDotsClick(element, bindingAdapterPosition)
            }
        }
    }

    private fun setThreeDotsOnClickListener(
        element: HomeRecommendationItemDataModel
    ) {
        binding.productCardView.setThreeDotsOnClickListener {
            listener.onProductThreeDotsClick(
                element,
                bindingAdapterPosition
            )
        }
    }
}
