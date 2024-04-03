package com.tokopedia.tokopoints.view.recommwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.analytics.byteio.topads.provider.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.topads.util.getVisibleSizePercentage
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.homeresponse.RecommendationWrapper
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class RewardsRecommAdapter(val list: ArrayList<RecommendationWrapper>, val listener : RewardsRecomListener) :
    RecyclerView.Adapter<RewardsRecommAdapter.ProductCardViewHolder>() {

    private var recyclerView: RecyclerView? = null

    inner class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), IAdsViewHolderTrackListener {

        val productView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

        private var uiModel: RecommendationItem? = null

        private var viewVisiblePercentage = "0"

        fun bind(model: RecommendationWrapper) {
            productView.setProductModel(model.recomData)
            val impressItem = model.recomendationItem
            uiModel = impressItem
            productView.setImageProductViewHintListener(
                model.recomendationItem,object : ViewHintListener {
                    override fun onViewHint() {
                        if (impressItem.isTopAds) {
                            TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                                className,
                                impressItem.trackerImageUrl,
                                impressItem.productId.toString(),
                                impressItem.name,
                                impressItem.imageUrl,"")
                        }
                        listener.onProductImpression(impressItem,position)
                    }
                }
            )

            productView.setOnClickListener(object : ProductCardClickListener{
                override fun onClick(v: View) {
                    if (impressItem.isTopAds) {
                        TopAdsUrlHitter(itemView.context).hitClickUrl(
                            className,
                            impressItem.clickUrl,
                            impressItem.productId.toString(),
                            impressItem.name,
                            impressItem.imageUrl,"")
                    }
                    listener.onProductClick(impressItem,"",bindingAdapterPosition)
                }

                override fun onAreaClicked(v: View) {
                    sendClickAdsByteIO(impressItem, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    sendClickAdsByteIO(impressItem, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    sendClickAdsByteIO(impressItem, AdsLogConst.Refer.SELLER_NAME)
                }
            })

        }

        private fun sendClickAdsByteIO(recommendationItem: RecommendationItem?, refer: String) {
            if (recommendationItem?.isTopAds == true) {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.REWARD,
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

        override fun onViewAttachedToWindow(recyclerView: RecyclerView?) {
            if (uiModel?.isTopAds == true) {
                viewVisiblePercentage = getVisibleSizePercentage(recyclerView, itemView)

                AppLogTopAds.sendEventShow(
                    itemView.context,
                    PageName.REWARD,
                    AdsLogShowModel(
                        // todo this value from BE
                        0,
                        // todo this value from BE
                        0,
                        AdsLogShowModel.AdExtraData(
                            productId = uiModel?.productId.orZero().toString(),
                        )
                    )
                )
            }
        }

        override fun onViewDetachedToWindow(recyclerView: RecyclerView?) {
            if (uiModel?.isTopAds == true) {
                AppLogTopAds.sendEventShowOver(
                    itemView.context,
                    PageName.REWARD,
                    AdsLogShowOverModel(
                        // todo this value from BE
                        0,
                        // todo this value from BE
                        0,
                        AdsLogShowOverModel.AdExtraData(
                            productId = uiModel?.productId.orZero().toString(),
                            sizePercent = viewVisiblePercentage
                        )
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tp_layout_recomm_item, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun onViewAttachedToWindow(holder: ProductCardViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow(recyclerView)
    }

    override fun onViewDetachedFromWindow(holder: ProductCardViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedToWindow(recyclerView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    companion object{
        const val className = "com.tokopedia.tokopoints.view.recommwidget.RewardsRecommAdapter"
    }
}
