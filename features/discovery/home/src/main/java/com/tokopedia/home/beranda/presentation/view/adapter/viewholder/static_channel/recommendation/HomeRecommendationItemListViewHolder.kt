package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.ForYouDataMapper.toModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.listener.ImpressionRecommendationItemListener
import com.tokopedia.home.util.asAdsLogShowModel
import com.tokopedia.home.util.asAdsLogShowOverModel
import com.tokopedia.home.util.sendEventRealtimeClickAdsByteIo
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnAttachStateChangeListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.GlobalRecomListener

/**
 * Created by Lukas on 2019-07-15
 */

class HomeRecommendationItemListViewHolder(
    itemView: View,
    private val listener: ImpressionRecommendationItemListener,
    private val homeRecommendationListener: GlobalRecomListener
) : BaseRecommendationViewHolder<HomeRecommendationItemDataModel>(
    itemView,
    HomeRecommendationItemDataModel::class.java
) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item_list
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    init {
        itemView.addOnAttachStateChangeListener(
            onViewAttachedToWindow = { onViewAttachedToWindow(elementItem) },
            onViewDetachedFromWindow = { onViewDetachedFromWindow(elementItem, visiblePercentage) }
        )
    }

    override fun bind(element: HomeRecommendationItemDataModel) {
        this.elementItem = element
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
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
        productCardView.setOnClickListener(object : ProductCardClickListener {
            override fun onClick(v: View) {
                homeRecommendationListener.onProductCardClicked(
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

    override fun onViewAttachedToWindow(element: HomeRecommendationItemDataModel?) {
        element?.let {
            if (it.recommendationProductItem.isTopAds) {
                AppLogTopAds.sendEventShow(
                    itemView.context,
                    it.recommendationProductItem.asAdsLogShowModel()
                )
            }
        }
    }

    override fun onViewDetachedFromWindow(element: HomeRecommendationItemDataModel?, visiblePercentage: Int) {
        element?.let {
            if (it.recommendationProductItem.isTopAds) {
                AppLogTopAds.sendEventShowOver(
                    itemView.context,
                    it.recommendationProductItem.asAdsLogShowOverModel(visibilityPercentage)
                )
                setVisiblePercentage(Int.ZERO)
            }
        }
    }

    private fun setItemThreeDotsClickListener(element: HomeRecommendationItemDataModel) {
        productCardView.setThreeDotsOnClickListener {
            homeRecommendationListener.onProductCardThreeDotsClicked(
                element.toModel(),
                bindingAdapterPosition
            )
        }
    }
}
