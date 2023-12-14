package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsUiModel
import com.tokopedia.home.databinding.ItemHomeBannerTopadsLayoutBinding
import com.tokopedia.home_component.util.toDpFloat
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_VERTICAL
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationBannerTopAdsViewHolder(
    view: View,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<HomeRecommendationBannerTopAdsUiModel>(
    view,
    HomeRecommendationBannerTopAdsUiModel::class.java
) {
    companion object {
        val LAYOUT = R.layout.item_home_banner_topads_layout
        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"

        private const val TDN_BANNER_ROUNDED = 8F
    }

    private val binding: ItemHomeBannerTopadsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationBannerTopAdsUiModel) {
        setImageTopAdsNewQuery(element)
        setBannerTopAdsClickListener(element)
    }

    private fun setImageTopAdsNewQuery(element: HomeRecommendationBannerTopAdsUiModel) {
        loadImageTopAdsNewQuery(element)
    }

    private fun loadImageTopAdsNewQuery(
        recommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsUiModel
    ) {
        recommendationBannerTopAdsDataModel.topAdsImageViewModel?.let { topAdsImageViewModel ->
            setBannerTopAdsImpressionListener(
                recommendationBannerTopAdsDataModel,
                homeRecommendationListener
            )

            if (topAdsImageViewModel.imageUrl?.isNotBlank() == true) {
                binding?.homeRecomTopadsLoaderImage?.show()
                binding?.homeRecomTopadsImageView?.let {
                    it.imageWidth = topAdsImageViewModel.imageWidth
                    it.imageHeight = topAdsImageViewModel.imageHeight
                    it.bannerType = BANNER_TYPE_VERTICAL
                    loadVerticalBanner(recommendationBannerTopAdsDataModel, it)
                }
            }
        }
    }

    private fun setBannerTopAdsImpressionListener(
        recommendationBannerTopAdsUiModel: HomeRecommendationBannerTopAdsUiModel,
        listener: HomeRecommendationListener
    ) {
        itemView.addOnImpressionListener(
            recommendationBannerTopAdsUiModel,
            object : ViewHintListener {
                override fun onViewHint() {
                    TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                        this::class.java.simpleName,
                        recommendationBannerTopAdsUiModel.topAdsImageViewModel?.adViewUrl,
                        "",
                        "",
                        recommendationBannerTopAdsUiModel.topAdsImageViewModel?.imageUrl,
                        HOME_RECOM_TAB_BANNER
                    )
                    listener.onBannerTopAdsImpress(
                        recommendationBannerTopAdsUiModel,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setBannerTopAdsClickListener(element: HomeRecommendationBannerTopAdsUiModel) {
        binding?.homeRecomTopadsImageView?.setOnClickListener {
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                this::class.java.simpleName,
                element.topAdsImageViewModel?.adClickUrl,
                "",
                "",
                element.topAdsImageViewModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            homeRecommendationListener.onBannerTopAdsClick(element, bindingAdapterPosition)
        }
    }

    private fun loadVerticalBanner(
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsUiModel,
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
