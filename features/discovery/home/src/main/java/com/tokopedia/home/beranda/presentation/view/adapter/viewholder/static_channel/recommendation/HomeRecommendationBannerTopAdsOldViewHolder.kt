package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_VERTICAL_BANNER_ADS
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsOldDataModel
import com.tokopedia.home.databinding.ItemHomeBannerTopadsOldLayoutBinding
import com.tokopedia.home_component.util.toDpFloat
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_HORIZONTAL
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_VERTICAL
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationBannerTopAdsOldViewHolder(
    view: View,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<HomeRecommendationBannerTopAdsOldDataModel>(
    view,
    HomeRecommendationBannerTopAdsOldDataModel::class.java
) {
    companion object {
        val LAYOUT = R.layout.item_home_banner_topads_old_layout
        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"

        private const val TDN_BANNER_ROUNDED = 8F
    }

    private val binding: ItemHomeBannerTopadsOldLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationBannerTopAdsOldDataModel) {
        setImageTopAdsOldQuery(element)
        setBannerTopAdsClickListener(element)
    }

    private fun setImageTopAdsOldQuery(element: HomeRecommendationBannerTopAdsOldDataModel) {
        loadImageTopAdsOldQuery(element, homeRecommendationListener)
    }

    private fun loadImageTopAdsOldQuery(
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        listener: HomeRecommendationListener
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel?.let { topAdsImageViewModel ->

            setBannerTopAdsImpressionListener(
                recommendationBannerTopAdsDataModelDataModel,
                listener
            )

            binding?.homeRecomTopadsLoaderImage?.show()
            binding?.homeRecomTopadsImageView?.let {
                it.imageWidth =
                    topAdsImageViewModel.imageWidth
                it.imageHeight =
                    topAdsImageViewModel.imageHeight
                if (recommendationBannerTopAdsDataModelDataModel.bannerType == TYPE_VERTICAL_BANNER_ADS) {
                    it.bannerType = BANNER_TYPE_VERTICAL
                    loadVerticalBanner(recommendationBannerTopAdsDataModelDataModel, it)
                } else {
                    it.bannerType = BANNER_TYPE_HORIZONTAL

                    it.loadImage(topAdsImageViewModel.imageUrl) {
                        transform(RoundedCorners(8))
                        fitCenter()
                        listener(
                            onSuccess = { _, _ ->
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
    }

    private fun setBannerTopAdsImpressionListener(
        recommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        listener: HomeRecommendationListener
    ) {
        itemView.addOnImpressionListener(
            recommendationBannerTopAdsDataModel,
            object : ViewHintListener {
                override fun onViewHint() {
                    TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                        this::class.java.simpleName,
                        recommendationBannerTopAdsDataModel.topAdsImageViewModel?.adViewUrl,
                        "",
                        "",
                        recommendationBannerTopAdsDataModel.topAdsImageViewModel?.imageUrl,
                        HOME_RECOM_TAB_BANNER
                    )
                    listener.onBannerTopAdsOldImpress(
                        recommendationBannerTopAdsDataModel,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setBannerTopAdsClickListener(element: HomeRecommendationBannerTopAdsOldDataModel) {
        binding?.homeRecomTopadsImageView?.setOnClickListener {
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                this::class.java.simpleName,
                element.topAdsImageViewModel?.adClickUrl,
                "",
                "",
                element.topAdsImageViewModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            homeRecommendationListener.onBannerTopAdsOldClick(element, bindingAdapterPosition)
        }
    }

    private fun loadVerticalBanner(
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsOldDataModel,
        appCompatImageView: AppCompatImageView
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel?.imageUrl?.let {
            appCompatImageView.loadImageRounded(it, TDN_BANNER_ROUNDED.toDpFloat()) {
                fitCenter()
                listener(onSuccess = { _, _ ->
                    appCompatImageView.show()
                    binding?.homeRecomTopadsLoaderImage?.hide()
                }, onError = {
                        appCompatImageView.hide()
                        binding?.homeRecomTopadsLoaderImage?.hide()
                    })
            }
        }
    }
}
