package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView

import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.BannerType
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel.Companion.TOTAL_NOTIFY_WORDING_FORMAT_FOR_REPLACED
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_shop_home_new_product_launch_campaign.view.*
import kotlinx.android.synthetic.main.layout_shop_home_npl_remind_me.view.*
import kotlinx.android.synthetic.main.layout_shop_home_npl_timer.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeNplCampaignViewHolder(
        itemView: View,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView), CoroutineScope {

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_new_product_launch_campaign
    }

    private var productListCampaignAdapter: ShopCampaignCarouselProductAdapter? = null
    private var model: ShopHomeNewProductLaunchCampaignUiModel? = null

    init {
        itemView.rv_product_carousel?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                model?.data?.firstOrNull()?.let {
                    it.rvState = recyclerView.layoutManager?.onSaveInstanceState()
                }
            }
        })
    }

    override fun bind(model: ShopHomeNewProductLaunchCampaignUiModel) {
        this.model = model
        setHeader(model)
        setTimer(model)
        setRemindMe(model)
        setBannerImage(model)
        setProductCarousel(model)
        setWidgetImpressionListener(model)
    }

    private fun setProductCarousel(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val productList = model.data?.firstOrNull()?.productList?.reversed() ?: listOf()
        productListCampaignAdapter = ShopCampaignCarouselProductAdapter(
                ShopCampaignCarouselProductAdapterTypeFactory(
                        model,
                        shopHomeCampaignNplWidgetListener,
                        adapterPosition
                )
        )
        itemView.rv_product_carousel?.apply {
            launch {
                try {
                    val rvState = model.data?.firstOrNull()?.rvState
                    if (null != rvState) {
                        itemView.rv_product_carousel?.layoutManager?.onRestoreInstanceState(rvState)
                    } else {
                        layoutManager?.scrollToPosition(productList.size - 1)
                    }
                    productListCampaignAdapter?.setElement(productList)
                    adapter = productListCampaignAdapter
                    setHeightBasedOnProductCardMaxHeight(productList.map {
                        ShopPageHomeMapper.mapToProductCardCampaignModel(
                                isHasAddToCartButton = false,
                                hasThreeDots = false,
                                shopHomeProductViewModel = it
                        )
                    })
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)
        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    private suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_145)
        return productCardModelList.getMaxHeightForGridView(itemView.context, Dispatchers.Default, productCardWidth)
    }

    private fun setBannerImage(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
        val selectedBannerType = when {
            isStatusCampaignUpcoming(statusCampaign) -> BannerType.UPCOMING.bannerType
            isStatusCampaignOngoing(statusCampaign) -> BannerType.LIVE.bannerType
            isStatusCampaignFinished(statusCampaign) -> BannerType.FINISHED.bannerType
            else -> ""
        }
        val bannerUrl = model.data?.firstOrNull()?.bannerList?.firstOrNull {
            it.bannerType.equals(selectedBannerType, true)
        }?.imageUrl.orEmpty()
        itemView.banner_background?.apply {
            layoutParams.height = if (isStatusCampaignFinished(statusCampaign)) {
                adjustViewBounds = true
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            } else {
                adjustViewBounds = false
                0
            }
            setImageUrl(bannerUrl)
        }
    }

    private fun setRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val isRemindMe = model.data?.firstOrNull()?.isRemindMe
        isRemindMe?.let {
            itemView.layout_remind_me?.show()
            itemView.layout_remind_me?.setOnClickListener {
                if (itemView.loader_remind_me?.isVisible == false) {
                    shopHomeCampaignNplWidgetListener.onClickRemindMe(model)
                }
            }
            if (it) {
                itemView.layout_remind_me?.background = MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.bg_rounded_rect_shop_home_npl_remind_me_true
                )
                itemView.image_notification?.setImageDrawable(MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.ic_npl_remind_me_true
                ))
                itemView.text_remind_me?.hide()
            } else {
                itemView.layout_remind_me?.background = MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.bg_rounded_rect_shop_home_npl_remind_me_false
                )
                itemView.image_notification?.setImageDrawable(MethodChecker.getDrawable(
                        itemView.context,
                        R.drawable.ic_npl_remind_me_false
                ))
                itemView.text_remind_me?.show()
            }
            checkRemindMeLoading(model)
        }
    }

    private fun checkRemindMeLoading(model: ShopHomeNewProductLaunchCampaignUiModel) {
        if (model.data?.firstOrNull()?.showRemindMeLoading == true) {
            itemView.image_notification?.hide()
            itemView.text_remind_me?.hide()
            itemView.loader_remind_me?.show()
        } else {
            itemView.image_notification?.show()
            itemView.loader_remind_me?.hide()
        }
    }

    private fun setWidgetImpressionListener(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            itemView.addOnImpressionListener(model) {
                shopHomeCampaignNplWidgetListener.onImpressionCampaignNplWidget(adapterPosition, model)
            }
        }
    }

    private fun setTimer(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign ?: ""
        if (!isStatusCampaignFinished(statusCampaign) && statusCampaign.isNotEmpty()) {
            val timeDescription = model.data?.firstOrNull()?.timeDescription ?: ""
            val timeCounter = model.data?.firstOrNull()?.timeCounter ?: ""
            itemView.text_time_description?.text = timeDescription
            val currentTime = System.currentTimeMillis()
            itemView.layout_timer?.show()
            if (timeCounter.toLong() != 0L) {
                val remainingMilliseconds = when {
                    isStatusCampaignUpcoming(statusCampaign) -> {
                        val startDate = DateHelper.getDateFromString(model.data?.firstOrNull()?.startDate
                                ?: "").time
                        startDate - currentTime
                    }
                    isStatusCampaignOngoing(statusCampaign) -> {
                        val endDate = DateHelper.getDateFromString(model.data?.firstOrNull()?.endDate
                                ?: "").time
                        endDate - currentTime
                    }
                    else -> {
                        0
                    }
                }
                setTimerData(remainingMilliseconds, model)
            } else {
                itemView.timer?.gone()
            }
        }
    }

    private fun setTimerData(remainingMilliseconds: Long, model: ShopHomeNewProductLaunchCampaignUiModel) {
        itemView.timer?.apply {
            this.remainingMilliseconds = remainingMilliseconds
            (hourView as? Typography)?.setWeight(Typography.BOLD)
            (hourView as? Typography)?.setType(Typography.SMALL)
            (minuteView as? Typography)?.setWeight(Typography.BOLD)
            (minuteView as? Typography)?.setType(Typography.SMALL)
            (secondView as? Typography)?.setWeight(Typography.BOLD)
            (secondView as? Typography)?.setType(Typography.SMALL)
            (colonHourView as? Typography)?.setWeight(Typography.BOLD)
            (colonHourView as? Typography)?.setType(Typography.SMALL)
            (colonMinuteView as? Typography)?.setWeight(Typography.BOLD)
            (colonMinuteView as? Typography)?.setType(Typography.SMALL)
            onFinish = {
                shopHomeCampaignNplWidgetListener.onTimerFinished(model)
            }
            show()
        }
    }

    private fun setCampaignInterest(
            totalNotify: Int,
            totalNotifyWording: String,
            statusCampaign: String
    ) {
        if (totalNotifyWording.isEmpty() || isStatusCampaignFinished(statusCampaign)) {
            itemView.text_description?.text = ""
            itemView.text_description?.hide()
        } else {
            val totalCampaignInterestString = totalNotifyWording.replace(
                    TOTAL_NOTIFY_WORDING_FORMAT_FOR_REPLACED,
                    totalNotify.thousandFormatted(1, RoundingMode.DOWN)
            )
            itemView.text_description?.text = totalCampaignInterestString
            itemView.text_description?.show()
        }
    }

    private fun setCta(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val ctaText = model.header.ctaText
        itemView.text_see_all?.apply {
            val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
            if (ctaText.isEmpty() || isStatusCampaignFinished(statusCampaign)) {
                text = ""
                hide()
            } else {
                text = ctaText
                setOnClickListener {
                    shopHomeCampaignNplWidgetListener.onClickCtaCampaignNplWidget(model)
                }
                show()
            }
        }
    }

    private fun setHeader(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val title = model.header.title
        val totalNotify = model.data?.firstOrNull()?.totalNotify ?: 0
        val totalNotifyWording = model.data?.firstOrNull()?.totalNotifyWording.orEmpty()
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
        setTitle(title)
        setTnc(title, model)
        setCta(model)
        setCampaignInterest(totalNotify, totalNotifyWording, statusCampaign)
    }

    private fun setTnc(title: String, model: ShopHomeNewProductLaunchCampaignUiModel) {
        itemView.image_tnc?.apply {
            val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
            if (title.isEmpty() || isStatusCampaignFinished(statusCampaign)) {
                hide()
            } else {
                show()
                setOnClickListener {
                    shopHomeCampaignNplWidgetListener.onClickTncCampaignNplWidget(model)
                }
            }
        }
    }

    private fun setTitle(title: String) {
        if (title.isEmpty()) {
            itemView.text_title?.text = ""
            itemView.text_title?.hide()
            itemView.image_tnc?.hide()
        } else {
            itemView.text_title?.text = title
            itemView.text_title?.show()
            itemView.image_tnc?.show()
        }
    }

    private fun isStatusCampaignFinished(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.FINISHED.statusCampaign, true)
    }

    private fun isStatusCampaignOngoing(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.ONGOING.statusCampaign, true)
    }

    private fun isStatusCampaignUpcoming(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.UPCOMING.statusCampaign, true)
    }

}