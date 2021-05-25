package com.tokopedia.shop.score.performance.presentation.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.performance.presentation.model.SectionShopRecommendationUiModel
import kotlinx.android.synthetic.main.item_promo_creation_shop_performance.view.*


class ItemFeatureRecommendationAdapter(private val itemRecommendationFeatureListener: ItemRecommendationFeatureListener) :
        RecyclerView.Adapter<ItemFeatureRecommendationAdapter.ItemFeatureRecommendationViewHolder>() {

    private val itemRecommendationList = mutableListOf<SectionShopRecommendationUiModel.ItemShopRecommendationUiModel>()

    fun setItemRecommendationList(itemRecommendationList: List<SectionShopRecommendationUiModel.ItemShopRecommendationUiModel>) {
        if (itemRecommendationList.isNullOrEmpty()) return
        this.itemRecommendationList.clear()
        this.itemRecommendationList.addAll(itemRecommendationList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFeatureRecommendationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.item_promo_creation_shop_performance, parent, false)
        return ItemFeatureRecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemFeatureRecommendationViewHolder, position: Int) {
        val data = itemRecommendationList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = itemRecommendationList.size

    inner class ItemFeatureRecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: SectionShopRecommendationUiModel.ItemShopRecommendationUiModel) {
            with(itemView) {
                tvItemRecommendedTitle?.text = data.titleRecommendation
                tvItemRecommendedDescription?.text = data.descRecommendation

                ivRecommendedPromo?.loadImage(data.iconRecommendationUrl)

                loadImageWithTarget(context, ShopScoreConstant.IC_SQUIRCLE_RECOMMENDATION_URL, {}, MediaTarget(
                        findViewById<ImageView>(R.id.ivRecommendedPromo),
                        onReady = { badgeView, resourceBitmap ->
                            val bitmapRecommended = BitmapDrawable(resources, resourceBitmap)
                            badgeView.background = bitmapRecommended
                        },
                        onFailed = { _, _ ->
                            ivRecommendedPromo?.hide()
                        }
                ))


                setOnClickListener {
                    itemRecommendationFeatureListener.onItemClickedRecommendationFeature(data.appLinkRecommendation, data.identifier)
                }
                itemRecommendationFeatureListener.onItemImpressRecommendationFeature(data.identifier)
            }
        }
    }
}