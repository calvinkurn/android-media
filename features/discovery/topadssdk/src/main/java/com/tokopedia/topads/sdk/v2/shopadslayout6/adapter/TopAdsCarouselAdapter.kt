package com.tokopedia.topads.sdk.v2.shopadslayout6.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.v2.shopadslayout6.listener.TopAdsCarouselListener
import com.tokopedia.topads.sdk.v2.shopadslayout6.uimodel.TopAdsCarouselModel
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifyprinciples.Typography

private const val PRODUCT_INDEX_ZERO = 0
private const val PRODUCT_INDEX_ONE = 1
class TopAdsCarouselAdapter(private val topAdsCarouselListener: TopAdsCarouselListener) : RecyclerView.Adapter<TopAdsCarouselAdapter.TopAdsCarouselViewHolder>() {

    private val topAdsCarouselItemList = arrayListOf<TopAdsCarouselModel.TopAdsCarouselItem>()

    fun setList(list: List<TopAdsCarouselModel.TopAdsCarouselItem>) {
        val itemCount = itemCount
        this.topAdsCarouselItemList.clear()
        notifyItemRangeRemoved(0, itemCount)

        this.topAdsCarouselItemList.addAll(list)
        notifyItemRangeInserted(0, this.topAdsCarouselItemList.size)
    }

    inner class TopAdsCarouselViewHolder(itemView: View, private val topAdsCarouselListener: TopAdsCarouselListener) : RecyclerView.ViewHolder(itemView) {
        private val imageFirst = itemView.findViewById<ImageView>(R.id.carouselItemImageFirst)
        private val imageSecond = itemView.findViewById<ImageView>(R.id.carouselItemImageTwo)
        private val brandIcon = itemView.findViewById<ImageView>(R.id.brandIcon)
        private val brandBadge = itemView.findViewById<ImageView>(R.id.brandBadge)
        private val carouselItemName = itemView.findViewById<Typography>(R.id.carouselItemName)
        private val topAdsCarouselRoot = itemView.findViewById<CardUnify>(R.id.topAdsCarouselRoot)


        fun bind(topAdsCarouselItem: TopAdsCarouselModel.TopAdsCarouselItem) {

            imageFirst.loadImage(topAdsCarouselItem.imageUrlOne)
            imageSecond.loadImage(topAdsCarouselItem.imageUrlTwo)
            brandIcon.loadImageCircle(topAdsCarouselItem.brandIcon)
            brandBadge.loadImage(topAdsCarouselItem.brandBadge)
            loadBadge(topAdsCarouselItem)
            carouselItemName.text = topAdsCarouselItem.brandName

            topAdsCarouselItem.impressHolder?.let { impressHolder ->
                topAdsCarouselRoot.addOnImpressionListener(impressHolder) {
                    topAdsCarouselListener.onItemImpressed(topAdsCarouselItem.position)
                }
            }

            topAdsCarouselRoot.setOnClickListener {
                topAdsCarouselListener.onItemClicked(
                    topAdsCarouselItem.position
                )
            }
            imageFirst.setOnClickListener {
                topAdsCarouselListener.onProductItemClicked(
                        PRODUCT_INDEX_ZERO,
                    topAdsCarouselItem.position
                )
            }
            imageSecond.setOnClickListener {
                topAdsCarouselListener.onProductItemClicked(
                        PRODUCT_INDEX_ONE,
                    topAdsCarouselItem.position
                )
            }

        }

        private fun loadBadge(topAdsCarouselItem: TopAdsCarouselModel.TopAdsCarouselItem) {
            val isImageShopBadgeVisible = getIsImageShopBadgeVisible(topAdsCarouselItem)
            brandBadge.shouldShowWithAction(isImageShopBadgeVisible) {
                when {
                    topAdsCarouselItem.isOfficial -> brandBadge.loadImage(R.drawable.ic_official_store)
                    topAdsCarouselItem.isPMPro -> brandBadge.loadImage(com.tokopedia.shopwidget.R.drawable.shopwidget_ic_pm_pro)
                    topAdsCarouselItem.isGoldShop -> brandBadge.loadImage(com.tokopedia.gm.common.R.drawable.ic_power_merchant)
                }
            }
        }

        private fun getIsImageShopBadgeVisible(topAdsCarouselItem: TopAdsCarouselModel.TopAdsCarouselItem): Boolean {
            return topAdsCarouselItem.isOfficial
                    || topAdsCarouselItem.isPMPro
                    || topAdsCarouselItem.isGoldShop
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAdsCarouselViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.topads_carousel_layout_item, parent, false)
        return TopAdsCarouselViewHolder(view, topAdsCarouselListener)
    }


    override fun onBindViewHolder(holder: TopAdsCarouselViewHolder, position: Int) {
        holder.bind(topAdsCarouselItemList[position])
    }

    override fun getItemCount(): Int {
        return topAdsCarouselItemList.count()
    }

}
