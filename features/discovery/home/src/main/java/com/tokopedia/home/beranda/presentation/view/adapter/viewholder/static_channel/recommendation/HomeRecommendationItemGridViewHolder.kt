package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.listener.ImpressionRecommendationItemListener
import com.tokopedia.home.analytics.byteio.sendEventRealtimeClickAdsByteIo
import com.tokopedia.home.analytics.byteio.sendEventShow
import com.tokopedia.home.analytics.byteio.sendEventShowOver
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemGridViewHolder(
    view: View,
    private val listener: ImpressionRecommendationItemListener,
    private val globalListener: GlobalRecomListener
) : BaseRecommendationViewHolder<HomeRecommendationItemDataModel>(
    view,
    HomeRecommendationItemDataModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_grid
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    override fun bind(element: HomeRecommendationItemDataModel) {
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
        productCardView.setVisibilityPercentListener(element.recommendationProductItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
            override fun onShow() {
                sendEventShow(itemView.context, element)
            }

            override fun onShowOver(maxPercentage: Int) {
                sendEventShowOver(itemView.context, element, maxPercentage)
            }
        })
    }

    override fun bindPayload(newItem: HomeRecommendationItemDataModel?) {
        newItem?.let {
            setItemThreeDotsClickListener(it)
        }
    }

    private fun setLayout(
        element: HomeRecommendationItemDataModel
    ) {
        productCardView.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: HomeRecommendationItemDataModel) {
        productCardView.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductCardImpressed(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: HomeRecommendationItemDataModel) {
        productCardView.setOnClickListener(object : ProductCardClickListener{
            override fun onClick(v: View) {
                globalListener.onProductCardClicked(
                    element.toModel(),
                    bindingAdapterPosition
                )
            }

            override fun onAreaClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationProductItem, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationProductItem, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationProductItem, AdsLogConst.Refer.SELLER_NAME)
            }
        })
    }

    private fun setItemThreeDotsClickListener(productCardItem: HomeRecommendationItemDataModel) {
        productCardView.setThreeDotsOnClickListener {
            globalListener.onProductCardThreeDotsClicked(
                productCardItem.toModel(),
                bindingAdapterPosition
            )
        }
    }
}
