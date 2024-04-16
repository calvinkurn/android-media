package com.tokopedia.wishlist.collection.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
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
                        recommendationItem?.sendRealtimeClickAdsByteIo(itemView.context, PageName.WISHLIST, AdsLogConst.Refer.AREA)
                    }

                    override fun onProductImageClicked(v: View) {
                        recommendationItem?.sendRealtimeClickAdsByteIo(itemView.context, PageName.WISHLIST, AdsLogConst.Refer.COVER)
                    }

                    override fun onSellerInfoClicked(v: View) {
                        recommendationItem?.sendRealtimeClickAdsByteIo(itemView.context, PageName.WISHLIST, AdsLogConst.Refer.SELLER_NAME)
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

    override fun onViewAttachedToWindow() {
        recommendationItem?.sendShowAdsByteIo(itemView.context, PageName.WISHLIST)
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        recommendationItem?.sendShowOverAdsByteIo(itemView.context, PageName.WISHLIST, visiblePercentage)
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
