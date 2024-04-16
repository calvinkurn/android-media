package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.listener.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifyorderhistory.data.model.UohTypeData
import com.tokopedia.unifyorderhistory.databinding.UohRecommendationItemBinding
import com.tokopedia.unifyorderhistory.view.adapter.UohItemAdapter

class UohRecommendationItemViewHolder(private val binding: UohRecommendationItemBinding, private val actionListener: UohItemAdapter.ActionListener?)
    : RecyclerView.ViewHolder(binding.root), IAdsViewHolderTrackListener {
    private val productCardView: ProductCardGridView by lazy { binding.uohProductItem }

    private var visibleViewPercentage = 0
    private var recommendationItem: RecommendationItem? = null

    fun bind(item: UohTypeData, position: Int) {
        if (item.dataObject is RecommendationItem) {
            this.recommendationItem = item.dataObject
            productCardView.run {
                setProductModel(
                    item.dataObject.toProductCardModel(hasAddToCartButton = true)
                )

                setAddToCartOnClickListener {
                    actionListener?.atcRecommendationItem(item.dataObject)
                }

                setOnClickListener(object: ProductCardClickListener {
                    override fun onClick(v: View) {
                        actionListener?.trackProductClickRecommendation(item.dataObject, position)
                    }

                    override fun onAreaClicked(v: View) {
                        item.dataObject.sendRealtimeClickAdsByteIo(itemView.context, PageName.BUYER_ORDER_MANAGEMENT, AdsLogConst.Refer.AREA)
                    }

                    override fun onProductImageClicked(v: View) {
                        item.dataObject.sendRealtimeClickAdsByteIo(itemView.context, PageName.BUYER_ORDER_MANAGEMENT, AdsLogConst.Refer.COVER)
                    }

                    override fun onSellerInfoClicked(v: View) {
                        item.dataObject.sendRealtimeClickAdsByteIo(itemView.context, PageName.BUYER_ORDER_MANAGEMENT, AdsLogConst.Refer.SELLER_NAME)
                    }
                })

                setImageProductViewHintListener(
                    item.dataObject,
                    object : ViewHintListener {
                        override fun onViewHint() {
                            actionListener?.trackProductViewRecommendation(item.dataObject, position)
                        }
                    }
                )
            }
        }
    }

    override fun onViewAttachedToWindow() {
        recommendationItem?.sendShowAdsByteIo(itemView.context, PageName.BUYER_ORDER_MANAGEMENT)
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        recommendationItem?.sendShowOverAdsByteIo(itemView.context, PageName.BUYER_ORDER_MANAGEMENT, visiblePercentage)
    }

    override fun setVisiblePercentage(visiblePercentage: Int) {
        this.visibleViewPercentage = visiblePercentage
    }

    override val visiblePercentage: Int
        get() = visibleViewPercentage
}
