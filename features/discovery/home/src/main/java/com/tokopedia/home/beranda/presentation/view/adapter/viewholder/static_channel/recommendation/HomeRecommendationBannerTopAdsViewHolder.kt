package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper.Companion.TYPE_VERTICAL_BANNER_ADS
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationBannerTopAdsDataModel
import com.tokopedia.home.databinding.ItemHomeBannerTopadsLayoutBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.recommendation_widget_common.widget.entitycard.viewholder.BaseRecommendationForYouViewHolder
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_HORIZONTAL
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_VERTICAL
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationBannerTopAdsViewHolder(
    view: View,
    private val homeRecommendationListener: HomeRecommendationListener
) : BaseRecommendationForYouViewHolder<HomeRecommendationBannerTopAdsDataModel>(
    view,
    HomeRecommendationBannerTopAdsDataModel::class.java
) {
    companion object {
        val LAYOUT = R.layout.item_home_banner_topads_layout
        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"
    }

    private val binding: ItemHomeBannerTopadsLayoutBinding? by viewBinding()

    override fun bind(element: HomeRecommendationBannerTopAdsDataModel) {
        if (isHomeRecommendationNewQuery()) {
            setImageTopAdsNewQuery(element)
        } else {
            setImageTopAdsOldQuery(element)
        }
    }

    override fun bindPayload(newItem: HomeRecommendationBannerTopAdsDataModel?) {
        newItem?.let {
            if (isHomeRecommendationNewQuery()) {
                setImageTopAdsNewQuery(it)
            } else {
                setImageTopAdsOldQuery(it)
            }
        }
    }

    private fun isHomeRecommendationNewQuery(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            RollenceKey.FOR_YOU_FEATURE_FLAG,
            ""
        ) == RollenceKey.FOR_YOU_FEATURE_FLAG
    }

    private fun setImageTopAdsNewQuery(element: HomeRecommendationBannerTopAdsDataModel) {
        loadImageTopAdsNewQuery(element)
        setBannerTopAdsClickListener(element)
    }

    private fun setImageTopAdsOldQuery(element: HomeRecommendationBannerTopAdsDataModel) {
        loadImageTopAdsOldQuery(element, homeRecommendationListener)
        setBannerTopAdsClickListener(element)
    }

    private fun loadImageTopAdsOldQuery(
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
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
                    Glide.with(itemView.context)
                        .load(topAdsImageViewModel.imageUrl)
                        .transform(RoundedCorners(8))
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
    }

    private fun loadImageTopAdsNewQuery(
        recommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsDataModel
    ) {
        recommendationBannerTopAdsDataModel.topAdsImageViewModel?.let { topAdsImageViewModel ->
            setBannerTopAdsImpressionListener(
                recommendationBannerTopAdsDataModel,
                homeRecommendationListener
            )

            binding?.homeRecomTopadsLoaderImage?.show()
            binding?.homeRecomTopadsImageView?.let {
                it.imageWidth = topAdsImageViewModel.imageWidth
                it.imageHeight = topAdsImageViewModel.imageHeight
                it.bannerType = BANNER_TYPE_VERTICAL
                loadVerticalBanner(recommendationBannerTopAdsDataModel, it)
            }
        }
    }

    private fun setBannerTopAdsImpressionListener(
        recommendationBannerTopAdsDataModel: HomeRecommendationBannerTopAdsDataModel,
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
                    listener.onBannerTopAdsImpress(
                        recommendationBannerTopAdsDataModel,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setBannerTopAdsClickListener(
        element: HomeRecommendationBannerTopAdsDataModel
    ) {
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
        recommendationBannerTopAdsDataModelDataModel: HomeRecommendationBannerTopAdsDataModel,
        appCompatImageView: AppCompatImageView
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel?.imageUrl?.let {
            // todo check image rounded, 8.toDpFloat()
            appCompatImageView.loadImageRounded(it, 16f) {
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
