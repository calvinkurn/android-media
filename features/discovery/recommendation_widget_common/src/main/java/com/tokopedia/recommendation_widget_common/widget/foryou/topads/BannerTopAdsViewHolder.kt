package com.tokopedia.recommendation_widget_common.widget.foryou.topads

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetBannerTopadsBinding
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_VERTICAL
import com.tokopedia.utils.view.binding.viewBinding

class BannerTopAdsViewHolder constructor(
    view: View,
    private val listener: BannerTopAdsListener
) : BaseForYouViewHolder<BannerTopAdsModel>(
    view,
    BannerTopAdsModel::class.java
) {

    private val binding: WidgetBannerTopadsBinding? by viewBinding()

    override fun bind(element: BannerTopAdsModel) {
        setImageTopAdsNewQuery(element)
        setBannerTopAdsClickListener(element)
    }

    private fun setImageTopAdsNewQuery(element: BannerTopAdsModel) {
        loadImageTopAdsNewQuery(element)
    }

    private fun loadImageTopAdsNewQuery(
        recommendationBannerTopAdsDataModel: BannerTopAdsModel
    ) {
        recommendationBannerTopAdsDataModel.topAdsImageViewModel?.let { topAdsImageViewModel ->
            setBannerTopAdsImpressionListener(
                recommendationBannerTopAdsDataModel,
                listener
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
        recommendationBannerTopAdsUiModel: BannerTopAdsModel,
        listener: BannerTopAdsListener
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

    private fun setBannerTopAdsClickListener(element: BannerTopAdsModel) {
        binding?.homeRecomTopadsImageView?.setOnClickListener {
            TopAdsUrlHitter(itemView.context).hitClickUrl(
                this::class.java.simpleName,
                element.topAdsImageViewModel?.adClickUrl,
                "",
                "",
                element.topAdsImageViewModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            listener.onBannerTopAdsClick(element, bindingAdapterPosition)
        }
    }

    private fun loadVerticalBanner(
        recommendationBannerTopAdsDataModelDataModel: BannerTopAdsModel,
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
        val LAYOUT = R.layout.widget_banner_topads

        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"
        private const val TDN_BANNER_ROUNDED = 8F
    }
}
