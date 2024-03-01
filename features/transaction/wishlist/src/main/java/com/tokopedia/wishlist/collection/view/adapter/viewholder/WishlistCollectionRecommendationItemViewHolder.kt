package com.tokopedia.wishlist.collection.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.asProductTrackModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.wishlist.collection.data.model.WishlistCollectionTypeLayoutData
import com.tokopedia.wishlist.collection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlist.databinding.WishlistRecommendationItemBinding

class WishlistCollectionRecommendationItemViewHolder(
    private val binding: WishlistRecommendationItemBinding,
    private val actionListener: WishlistCollectionAdapter.ActionListener?
) : RecyclerView.ViewHolder(binding.root), AppLogRecTriggerInterface {
    private val cardView: ProductCardGridView by lazy { binding.wishlistProductItem }

    private var recTriggerObject = RecommendationTriggerObject()

    fun bind(item: WishlistCollectionTypeLayoutData, adapterPosition: Int) {
        setRecTriggerObject(item.recommItem)
        if (item.dataObject is ProductCardModel) {
            cardView.run {
                setProductModel(item.dataObject)

                setOnClickListener {
                    AppLogRecommendation.sendProductClickAppLog(
                        item.recommItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                    )
                    actionListener?.onRecommendationItemClick(item.recommItem, adapterPosition)
                }

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
