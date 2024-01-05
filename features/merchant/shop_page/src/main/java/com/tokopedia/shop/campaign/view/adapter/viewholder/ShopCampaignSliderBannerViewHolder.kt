package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ShopCampaignTabWidgetHeaderView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE
import com.tokopedia.shop.common.view.ShopCarouselBannerImageUnify
import com.tokopedia.shop.databinding.WidgetCampaignSliderBannerViewHolderBinding
import com.tokopedia.shop.databinding.WidgetSliderBannerItemBinding
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class ShopCampaignSliderBannerViewHolder(
    view: View?,
    private val listener: ShopHomeDisplayWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(view), CarouselUnify.OnActiveIndexChangedListener {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_campaign_slider_banner_view_holder
        const val DURATION_SLIDER_BANNER = 5000L
        private const val CORNER_RADIUS = 4f
    }
    private val viewBinding: WidgetCampaignSliderBannerViewHolderBinding? by viewBinding()
    private var viewBindingSliderBannerItem: WidgetSliderBannerItemBinding? = null
    private var carouselShopPage: CarouselUnify? = null
    private var bannerData: ShopHomeDisplayWidgetUiModel? = null
    private var carouselData: ArrayList<Any>? = null
    private var headerView: ShopCampaignTabWidgetHeaderView? = viewBinding?.headerView

    private var itmListener = { view: View, data: Any ->
        viewBindingSliderBannerItem = WidgetSliderBannerItemBinding.bind(view)
        val img: ShopCarouselBannerImageUnify? = viewBindingSliderBannerItem?.imageCarousel
        img?.cornerRadius = CORNER_RADIUS.dpToPx().toInt()
        val carouselItem = data as CarouselData
        val index = carouselData?.indexOf(carouselItem) ?: 0
        bannerData?.let { bannerData ->
            bannerData.data?.let {
                img?.setOnClickListener {
                    onClickBannerItem(
                        bannerData,
                        bannerData.data[index],
                        adapterPosition,
                        index
                    )
                }
            }
        }
        val performanceMonitoring = PerformanceMonitoring.start(SHOP_HOME_IMAGE_SLIDER_BANNER_TRACE)
        // avoid crash in ImageUnify when image url is returned as base64
        try {
            if (img?.context.isValidGlideContext()) {
                val ratio = bannerData?.let { getHeightRatio(it) } ?: 0f
                img?.heightRatio = ratio
                img?.setImageUrl(carouselItem.imageUrl, ratio)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        img?.onUrlLoaded = {
            performanceMonitoring.stopTrace()
        }
    }

    init {
        carouselShopPage = viewBinding?.carouselShopPage
        carouselShopPage?.apply {
            autoplayDuration = DURATION_SLIDER_BANNER
            indicatorPosition = CarouselUnify.INDICATOR_BL
            infinite = true
            onActiveIndexChangedListener = this@ShopCampaignSliderBannerViewHolder
            indicatorWrapper.setPadding(indicatorWrapper.paddingLeft, 0, indicatorWrapper.paddingRight, 0)
            indicatorWrapper.setMargin(0, 4.toPx(), 0, 0)
        }
    }

    override fun onActiveIndexChanged(prev: Int, current: Int) {
        itemView.isVisibleOnTheScreen({
            bannerData?.let { shopHomeDisplayWidgetUiModel ->
                shopHomeDisplayWidgetUiModel.data?.let { listDisplayWidget ->
                    if (current >= 0 && current < shopHomeDisplayWidgetUiModel.data.size) {
                        val item = listDisplayWidget[current]
                        if (!item.isInvoke) {
                            listener.onDisplayItemImpression(
                                shopHomeDisplayWidgetUiModel,
                                listDisplayWidget[current],
                                adapterPosition,
                                current
                            )
                            item.invoke()
                        }
                    }
                }
            }
        }, {})
    }

    override fun bind(shopHomeDisplayWidgetUiModel: ShopHomeDisplayWidgetUiModel) {
        bannerData = shopHomeDisplayWidgetUiModel
        carouselData = dataWidgetToCarouselData(shopHomeDisplayWidgetUiModel)
        carouselShopPage?.apply {
            stage.removeAllViews()
            carouselData?.let {
                if (stage.childCount == 0) {
                    addItems(R.layout.widget_slider_banner_item, it, itmListener)
                    Handler().post {
                        activeIndex = 0
                    }
                }
            }
        }
        setHeader(shopHomeDisplayWidgetUiModel)
        bannerData?.let {
            val widthRatio = getIndexRatio(it, 0).toString()
            val heightRatio = getIndexRatio(it, 1).toString()
            carouselShopPage?.apply {
                (layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = "$widthRatio:$heightRatio"
                post {
                    (carouselShopPage?.layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = ""
                }
            }
        }
        setWidgetImpressionListener(shopHomeDisplayWidgetUiModel)
    }

    fun pauseTimer() {
        carouselShopPage?.autoplay = false
    }

    fun resumeTimer() {
        carouselShopPage?.timer = Timer()
        carouselShopPage?.autoplay = true
    }

    private fun onClickBannerItem(
        element: ShopHomeDisplayWidgetUiModel?,
        displayWidgetItem: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem?,
        adapterPosition: Int,
        index: Int
    ) {
        displayWidgetItem?.let {
            listener.onDisplayItemClicked(
                element,
                it,
                adapterPosition,
                index
            )
            it.invoke()
        }
    }

    private fun getIndexRatio(data: ShopHomeDisplayWidgetUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(data: ShopHomeDisplayWidgetUiModel): Float {
        val indexZero = getIndexRatio(data, 0).toFloat()
        val indexOne = getIndexRatio(data, 1).toFloat()
        return (indexOne / indexZero)
    }

    private fun dataWidgetToCarouselData(element: ShopHomeDisplayWidgetUiModel): ArrayList<Any> {
        val mutableString: ArrayList<Any> = ArrayList()
        element.data?.map {
            it.imageUrl.let { img ->
                mutableString.add(CarouselData(img))
            }
        }
        return mutableString
    }

    private fun setHeader(uiModel: ShopHomeDisplayWidgetUiModel) {
        val title = uiModel.header.title
        if (title.isEmpty()) {
            headerView?.hide()
        } else {
            headerView?.show()
            headerView?.setTitle(title)
            headerView?.configColorMode(shopCampaignInterface.isCampaignTabDarkMode())
        }
    }

    private fun setWidgetImpressionListener(model: ShopHomeDisplayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            listener.onDisplayWidgetImpression(model, bindingAdapterPosition)
        }
    }

    class CarouselData(val imageUrl: String)
}
