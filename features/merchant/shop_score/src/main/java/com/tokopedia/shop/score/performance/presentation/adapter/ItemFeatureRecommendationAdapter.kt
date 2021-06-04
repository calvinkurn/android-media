package com.tokopedia.shop.score.performance.presentation.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreConstant
import com.tokopedia.shop.score.performance.presentation.model.SectionShopRecommendationUiModel
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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

                cardContent?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))

                loadImageWithTarget(context, ShopScoreConstant.IC_SQUIRCLE_RECOMMENDATION_URL, {}, MediaTarget(
                        findViewById<AppCompatImageView>(R.id.ivRecommendedPromo),
                        onReady = { recommendedPromoView, resourceBitmap ->
                            val bitmapRecommended = BitmapDrawable(resources, resourceBitmap)
                            if (context.isDarkMode()) {
                                val bgColorRecommendPromo = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                                bitmapRecommended.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(bgColorRecommendPromo, BlendModeCompat.SRC_ATOP)
                            }
                            recommendedPromoView.background = bitmapRecommended
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