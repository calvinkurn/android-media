package com.tokopedia.recommendation_widget_common.widget.foryou.topads.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.recommendation_widget_common.databinding.WidgetBannerOldTopadsBinding
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.BannerTopAdsListener
import com.tokopedia.recommendation_widget_common.widget.foryou.topads.model.BannerOldTopAdsModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_HORIZONTAL
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_VERTICAL
import com.tokopedia.utils.view.binding.viewBinding

class BannerOldTopAdsViewHolder constructor(
    view: View,
    private val listener: BannerTopAdsListener
) : BaseForYouViewHolder<BannerOldTopAdsModel>(
    view,
    BannerOldTopAdsModel::class.java
) {

    private val binding: WidgetBannerOldTopadsBinding? by viewBinding()

    override fun bind(element: BannerOldTopAdsModel) {
        setImageTopAdsOldQuery(element)
        setBannerTopAdsClickListener(element)
    }

    private fun setImageTopAdsOldQuery(element: BannerOldTopAdsModel) {
        loadImageTopAdsOldQuery(element, listener)
    }

    private fun loadImageTopAdsOldQuery(
        recommendationBannerTopAdsDataModelDataModel: BannerOldTopAdsModel,
        listener: BannerTopAdsListener
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

    private fun setBannerTopAdsImpressionListener(
        recommendationBannerTopAdsDataModel: BannerOldTopAdsModel,
        listener: BannerTopAdsListener
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

    private fun setBannerTopAdsClickListener(element: BannerOldTopAdsModel) {
        binding?.homeRecomTopadsImageView?.setOnClickListener {
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                this::class.java.simpleName,
                element.topAdsImageViewModel?.adClickUrl,
                "",
                "",
                element.topAdsImageViewModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            listener.onBannerTopAdsOldClick(element, bindingAdapterPosition)
        }
    }

    private fun loadVerticalBanner(
        recommendationBannerTopAdsDataModelDataModel: BannerOldTopAdsModel,
        appCompatImageView: AppCompatImageView
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageViewModel?.imageUrl?.let {
            appCompatImageView.loadImageRounded(it, TDN_BANNER_ROUNDED.toPx()) {
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

    companion object {
        val LAYOUT = R.layout.widget_banner_old_topads

        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"
        private const val TYPE_VERTICAL_BANNER_ADS = "banner_ads_vertical"
        private const val TDN_BANNER_ROUNDED = 8F
    }

}
