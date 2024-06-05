package com.tokopedia.wishlist.detail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.detail.data.model.WishlistTypeLayoutData
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter
import com.tokopedia.wishlist.collection.util.WishlistCollectionUtils.clickWithDebounce
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding

class WishlistRecommendationItemViewHolder(
    private val binding: WishlistRecommendationItemBinding,
    private val actionListener: WishlistAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root), AppLogRecTriggerInterface {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    private var recTriggerObject = RecommendationTriggerObject()

    fun bind(item: WishlistTypeLayoutData, adapterPosition: Int) {
        setRecTriggerObject(item.recommItem)
        if (item.dataObject is ProductCardModel) {
            cardView.run {
                setProductModel(item.dataObject)

                setOnClickListener(object: ProductCardClickListener{

                    override fun onClick(v: View) {
                        v.clickWithDebounce {
                            AppLogRecommendation.sendProductClickAppLog(
                                item.recommItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                            )
                            actionListener?.onRecommendationItemClick(item.recommItem, adapterPosition)
                        }
                    }

                    override fun onAreaClicked(v: View) {
                        item.recommItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                    }

                    override fun onProductImageClicked(v: View) {
                        item.recommItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                    }

                    override fun onSellerInfoClicked(v: View) {
                        item.recommItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
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

                setVisibilityPercentListener(
                    isTopAds = item.recommItem.isTopAds,
                    eventListener = object : ProductConstraintLayout.OnVisibilityPercentChanged {
                        override fun onShow() {
                            item.recommItem.sendShowAdsByteIo(itemView.context)
                        }

                        override fun onShowOver(maxPercentage: Int) {
                            item.recommItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
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
