package com.tokopedia.recommendation_widget_common.infinite.foryou.topads

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.media.loader.loadImageRounded
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetBannerTopadsBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.topads.sdk.widget.BANNER_TYPE_VERTICAL
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding

class BannerTopAdsViewHolder constructor(
    view: View,
    private val listener: BannerTopAdsListener
) : BaseRecommendationViewHolder<BannerTopAdsModel>(
    view,
    BannerTopAdsModel::class.java
), AppLogRecTriggerInterface {

    private val binding: WidgetBannerTopadsBinding? by viewBinding()

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(element: BannerTopAdsModel) {
        setRecTriggerObject(element)
        setImageTopAdsNewQuery(element)
        setBannerTopAdsClickListener(element)
    }

    private fun setImageTopAdsNewQuery(element: BannerTopAdsModel) {
        loadImageTopAdsNewQuery(element)
    }

    private fun loadImageTopAdsNewQuery(
        recommendationBannerTopAdsDataModel: BannerTopAdsModel
    ) {
        recommendationBannerTopAdsDataModel.topAdsImageUiModel?.let { topAdsImageViewModel ->
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
                        recommendationBannerTopAdsUiModel.topAdsImageUiModel?.adViewUrl,
                        "",
                        "",
                        recommendationBannerTopAdsUiModel.topAdsImageUiModel?.imageUrl,
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
                element.topAdsImageUiModel?.adClickUrl,
                "",
                "",
                element.topAdsImageUiModel?.imageUrl,
                HOME_RECOM_TAB_BANNER
            )
            listener.onBannerTopAdsClick(element, bindingAdapterPosition)
        }
    }

    private fun loadVerticalBanner(
        recommendationBannerTopAdsDataModelDataModel: BannerTopAdsModel,
        appCompatImageView: AppCompatImageView
    ) {
        recommendationBannerTopAdsDataModelDataModel.topAdsImageUiModel?.imageUrl?.let {
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

    private fun setRecTriggerObject(model: BannerTopAdsModel) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
            listName = model.tabName,
            listNum = model.tabIndex,
        )
    }

    companion object {
        val LAYOUT = R.layout.widget_banner_topads

        private const val HOME_RECOM_TAB_BANNER = "home_recom_tab_banner"
        private const val TDN_BANNER_ROUNDED = 8F
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
