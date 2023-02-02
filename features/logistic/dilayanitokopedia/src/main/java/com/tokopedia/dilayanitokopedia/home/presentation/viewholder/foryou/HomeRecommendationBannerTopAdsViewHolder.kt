package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.foryou

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.databinding.ItemDtHomeBannerTopadsLayoutBinding
import com.tokopedia.dilayanitokopedia.home.presentation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.recommendationforyou.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationBannerTopAdsViewHolder(view: View) :
    SmartAbstractViewHolder<HomeRecommendationBannerTopAdsDataModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_dt_home_banner_topads_layout
        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"

        private const val roundedCorners = 8
    }

    private var binding: ItemDtHomeBannerTopadsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationBannerTopAdsDataModel, listener: SmartListener) {
        loadImageTopAds(element, listener as HomeRecommendationListener)
        binding?.homeRecomTopadsImageView?.setOnClickListener {
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                this::class.java.simpleName,
                element.topAdsImageViewModel?.adClickUrl,
                "",
                "",
                element.topAdsImageViewModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            listener.onBannerTopAdsClick(element, adapterPosition)
        }
    }


    private fun loadImageTopAds(
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        listener: HomeRecommendationListener
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel?.let {
            itemView.addOnImpressionListener(
                recommendationBannerTopAdsDataModelDataModel,
                object : ViewHintListener {
                    override fun onViewHint() {
                        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                            this::class.java.simpleName,
                            recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.adViewUrl,
                            "",
                            "",
                            recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageUrl,
                            HOME_RECOM_TAB_BANNER
                        )
                        listener.onBannerTopAdsImpress(recommendationBannerTopAdsDataModelDataModel, adapterPosition)
                    }
                }
            )
            binding?.homeRecomTopadsLoaderImage?.show()
            binding?.homeRecomTopadsImageView?.let {
                Glide.with(itemView.context)
                    .load(recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageUrl)
                    .transform(RoundedCorners(roundedCorners))
                    .override(
                        itemView.context.resources.displayMetrics.widthPixels,
                        getHeight(
                            recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageWidth,
                            recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel.imageHeight
                        )
                    )
                    .fitCenter()
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            it.hide()
                            binding?.homeRecomTopadsLoaderImage?.hide()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            it.show()
                            binding?.homeRecomTopadsLoaderImage?.hide()
                            return false
                        }
                    })
                    .into(it)
            }
        }
    }

    private fun getHeight(width: Int, height: Int): Int {
        val metrics = itemView.context.resources.displayMetrics
        val deviceWidth = metrics.widthPixels.toFloat()
        val widthRatio = deviceWidth / width.toFloat()
        return (widthRatio * height).toInt()
    }
}
