package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.databinding.HomeFeedItemListBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.widget.entrypointcard.viewholder.BaseRecommendationForYouViewHolder

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

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        HomeFeedItemListBinding.bind(itemView)
    }

    override fun bind(element: HomeRecommendationItemDataModel) {
        setLayout(element)
        setItemThreeDotsClickListener(element)
    }

    override fun bindPayload(newItem: HomeRecommendationItemDataModel?) {
        newItem?.let {
            setLayout(it)
            setItemThreeDotsClickListener(it)
        }
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel
    ) {
        binding.productCardView.run {
            setProductModel(element.productCardModel)

            setImageProductViewHintListener(
                element,
                object : ViewHintListener {
                    override fun onViewHint() {
                        homeRecommendationListener.onProductImpression(
                            element,
                            bindingAdapterPosition
                        )
                    }
                }
            )
            setOnClickListener {
                homeRecommendationListener.onProductClick(
                    element,
                    bindingAdapterPosition
                )
            }

            setThreeDotsOnClickListener {
                homeRecommendationListener.onProductThreeDotsClick(element, bindingAdapterPosition)
            }
        }
    }

    private fun setItemThreeDotsClickListener(
        element: HomeRecommendationItemDataModel
    ) {
        binding.productCardView.setThreeDotsOnClickListener {
            homeRecommendationListener.onProductThreeDotsClick(
                element,
                bindingAdapterPosition
            )
        }
    }
}
