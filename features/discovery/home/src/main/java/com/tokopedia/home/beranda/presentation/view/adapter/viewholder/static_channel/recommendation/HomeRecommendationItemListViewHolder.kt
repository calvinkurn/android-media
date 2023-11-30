package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.databinding.HomeFeedItemListBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemListViewHolder(
    itemView: View,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<HomeRecommendationItemDataModel>(
    itemView,
    HomeRecommendationItemDataModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_list
    }

    private val binding = HomeFeedItemListBinding.bind(itemView)

    private var item: HomeRecommendationItemDataModel? = null

    override fun bind(element: HomeRecommendationItemDataModel) {
        item = element
        setLayout(element)
        productCardImpressionListener()
        setItemProductCardClickListener()
        setItemThreeDotsClickListener()
    }

    override fun bindPayload(newItem: HomeRecommendationItemDataModel?) {
        newItem?.let {
            item = it
            setLayout(it)
        }
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel
    ) {
        binding.productCardView.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener() {
        item?.let { productCardItem ->
            binding.productCardView.setImageProductViewHintListener(
                productCardItem,
                object : ViewHintListener {
                    override fun onViewHint() {
                        homeRecommendationListener.onProductImpression(
                            productCardItem,
                            bindingAdapterPosition
                        )
                    }
                }
            )
        }
    }

    private fun setItemProductCardClickListener() {
        item?.let { productCardItem ->
            binding.productCardView.setOnClickListener {
                homeRecommendationListener.onProductClick(
                    productCardItem,
                    bindingAdapterPosition
                )
            }
        }
    }

    private fun setItemThreeDotsClickListener() {
        item?.let { productCardItem ->
            binding.productCardView.setThreeDotsOnClickListener {
                homeRecommendationListener.onProductThreeDotsClick(
                    productCardItem,
                    bindingAdapterPosition
                )
            }
        }
    }
}
