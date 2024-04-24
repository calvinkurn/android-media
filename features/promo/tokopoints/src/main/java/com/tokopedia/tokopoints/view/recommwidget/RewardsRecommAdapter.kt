package com.tokopedia.tokopoints.view.recommwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.PercentageScrollListener
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnAttachStateChangeListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.homeresponse.RecommendationWrapper
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class RewardsRecommAdapter(val list: ArrayList<RecommendationWrapper>, val listener: RewardsRecomListener) :
    RecyclerView.Adapter<RewardsRecommAdapter.ProductCardViewHolder>() {

    private val percentageScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }

    inner class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        IAdsViewHolderTrackListener {

        val productView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

        private var uiModel: RecommendationItem? = null

        private var visibleViewPercentage: Int = 0

        init {
            itemView.addOnAttachStateChangeListener(
                onViewAttachedToWindow = { onViewAttachedToWindow() },
                onViewDetachedFromWindow = { onViewDetachedFromWindow(visiblePercentage) }
            )
        }

        fun bind(model: RecommendationWrapper) {
            productView.setProductModel(model.recomData)
            val impressItem = model.recomendationItem
            uiModel = impressItem
            productView.setImageProductViewHintListener(
                model.recomendationItem, object : ViewHintListener {
                override fun onViewHint() {
                    if (impressItem.isTopAds) {
                        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                            className,
                            impressItem.trackerImageUrl,
                            impressItem.productId.toString(),
                            impressItem.name,
                            impressItem.imageUrl, "")
                    }
                    listener.onProductImpression(impressItem, position)
                }
            }
            )

            productView.setOnClickListener(object : ProductCardClickListener {
                override fun onClick(v: View) {
                    if (impressItem.isTopAds) {
                        TopAdsUrlHitter(itemView.context).hitClickUrl(
                            className,
                            impressItem.clickUrl,
                            impressItem.productId.toString(),
                            impressItem.name,
                            impressItem.imageUrl, "")
                    }
                    listener.onProductClick(impressItem, "", bindingAdapterPosition)
                }

                override fun onAreaClicked(v: View) {
                    uiModel?.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    uiModel?.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    uiModel?.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }
            })

        }

        override fun onViewAttachedToWindow() {
            uiModel?.sendShowAdsByteIo(itemView.context)
        }

        override fun onViewDetachedFromWindow(visiblePercentage: Int) {
            uiModel?.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
            setVisiblePercentage(Int.ZERO)
        }

        override fun setVisiblePercentage(visiblePercentage: Int) {
            this.visibleViewPercentage = visiblePercentage
        }

        override val visiblePercentage: Int
            get() = visibleViewPercentage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tp_layout_recomm_item, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(percentageScrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(percentageScrollListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    companion object {
        const val className = "com.tokopedia.tokopoints.view.recommwidget.RewardsRecommAdapter"
    }
}
