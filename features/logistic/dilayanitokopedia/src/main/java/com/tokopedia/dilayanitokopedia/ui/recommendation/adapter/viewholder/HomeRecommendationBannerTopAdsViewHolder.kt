package com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.viewholder

import android.view.View
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.databinding.ItemDtHomeBannerTopadsLayoutBinding
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.HomeRecommendationListener
import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
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
                element.topAdsImageUiModel?.adClickUrl,
                "",
                "",
                element.topAdsImageUiModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            listener.onBannerTopAdsClick(element, adapterPosition)
        }
    }

    private fun loadImageTopAds(
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        listener: HomeRecommendationListener
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel?.let {
            itemView.addOnImpressionListener(
                recommendationBannerTopAdsDataModelDataModel,
                object : ViewHintListener {
                    override fun onViewHint() {
                        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                            this::class.java.simpleName,
                            recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel.adViewUrl,
                            "",
                            "",
                            recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel.imageUrl,
                            HOME_RECOM_TAB_BANNER
                        )
                        listener.onBannerTopAdsImpress(recommendationBannerTopAdsDataModelDataModel, adapterPosition)
                    }
                }
            )
            binding?.homeRecomTopadsLoaderImage?.show()
            binding?.homeRecomTopadsImageView?.let {
                val overrideHeight = getHeight(
                    recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel.imageWidth,
                    recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel.imageHeight
                )

                it.loadImage(recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel.imageUrl) {
                    transform(RoundedCorners(roundedCorners))
                    overrideSize(Resize(itemView.context.resources.displayMetrics.widthPixels, overrideHeight))
                    fitCenter()
                    listener(
                        onSuccess = { bitmap, mediaDataSource ->
                            it.show()
                            binding?.homeRecomTopadsLoaderImage?.hide()
                        },
                        onError = { _ ->
                            it.hide()
                            binding?.homeRecomTopadsLoaderImage?.hide()
                        }
                    )
                }
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
