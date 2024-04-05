package com.tokopedia.wishlist.collection.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.listener.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.collection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding

class WishlistCollectionRecommendationItemViewHolder(
    private val binding: WishlistRecommendationItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root), AppLogRecTriggerInterface, IAdsViewHolderTrackListener {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    private var recTriggerObject = RecommendationTriggerObject()

    private var visibleViewPercentage: Int = 0
    private var recommendationItem: RecommendationItem? = null

    fun bind(item: WishlistCollectionTypeLayoutData, adapterPosition: Int) {
        setRecTriggerObject(item.recommItem)
        if (item.dataObject is ProductCardModel) {
            recommendationItem = item.recommItem
            cardView.run {
                setProductModel(item.dataObject)

                setOnClickListener(object : ProductCardClickListener {
                    override fun onClick(v: View) {
                        AppLogRecommendation.sendProductClickAppLog(
                            item.recommItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                        )
                        actionListener?.onRecommendationItemClick(item.recommItem, adapterPosition)
                    }

                    override fun onAreaClicked(v: View) {
                        sendClickAdsByteIO(item.recommItem, AdsLogConst.Refer.AREA)
                    }

                    override fun onProductImageClicked(v: View) {
                        sendClickAdsByteIO(item.recommItem, AdsLogConst.Refer.COVER)
                    }

                    override fun onSellerInfoClicked(v: View) {
                        sendClickAdsByteIO(item.recommItem, AdsLogConst.Refer.SELLER_NAME)
                    }
                })

                setImageProductViewHintListener(
                    item.recommItem,
                    object : ViewHintListener {
                        override fun onViewHint() {
                            actionListener?.onRecommendationItemImpression(
                                item.recommItem,
                                adapterPosition
                            )
                        }
                    }
                )

                addOnImpression1pxListener(item.recommItem.appLogImpressHolder) {
                    AppLogRecommendation.sendProductShowAppLog(
                        item.recommItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                    )
                }
            }
        }
    }

    private fun sendClickAdsByteIO(recommendationItem: RecommendationItem?, refer: String) {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventRealtimeClick(
                itemView.context,
                PageName.WISHLIST,
                AdsLogRealtimeClickModel(
                    refer,
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogRealtimeClickModel.AdExtraData(
                        productId = recommendationItem.productId.orZero().toString()
                    )
                )
            )
        }
    }

    override fun onViewAttachedToWindow() {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShow(
                itemView.context,
                PageName.WISHLIST,
                AdsLogShowModel(
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogShowModel.AdExtraData(
                        productId = recommendationItem?.productId.orZero().toString(),
                    )
                )
            )
        }
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShowOver(
                itemView.context,
                PageName.WISHLIST,
                AdsLogShowOverModel(
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogShowOverModel.AdExtraData(
                        productId = recommendationItem?.productId.orZero().toString(),
                        sizePercent = visiblePercentage.toString()
                    )
                )
            )
        }
    }

    override fun setVisiblePercentage(visiblePercentage: Int) {
        this.visibleViewPercentage = visiblePercentage
    }

    override val visiblePercentage: Int
        get() = visibleViewPercentage


    private fun setRecTriggerObject(model: RecommendationItem) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
        )
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
