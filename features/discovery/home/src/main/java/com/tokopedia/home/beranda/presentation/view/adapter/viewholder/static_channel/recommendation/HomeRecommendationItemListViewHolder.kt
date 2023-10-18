package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.databinding.HomeFeedItemListBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemListViewHolder(itemView: View) : SmartAbstractViewHolder<HomeRecommendationItemDataModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_list
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        HomeFeedItemListBinding.bind(itemView)
    }

    override fun bind(element: HomeRecommendationItemDataModel, listener: SmartListener) {
        setLayout(element, listener as HomeRecommendationListener)
    }

    override fun bind(element: HomeRecommendationItemDataModel, listener: SmartListener, payloads: List<Any>) {
        binding.productCardView.setThreeDotsOnClickListener {
            (listener as HomeRecommendationListener).onProductThreeDotsClick(element, bindingAdapterPosition)
        }
    }

    private fun setLayout(element: HomeRecommendationItemDataModel, listener: HomeRecommendationListener) {
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
}
