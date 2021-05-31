package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.annotation.LayoutRes
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
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_shop_home_new_product_launch_campaign.view.*
import kotlinx.android.synthetic.main.layout_shop_home_npl_remind_me_notified.view.*
import kotlinx.android.synthetic.main.layout_shop_home_npl_remind_me_un_notified.view.*
import kotlinx.android.synthetic.main.layout_shop_home_npl_timer.view.*
import kotlinx.coroutines.*
import java.math.RoundingMode

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeNplCampaignViewHolder(
        itemView: View,
        private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView), CoroutineScope {

    private val masterJob = SupervisorJob()
    private var isRemindMe: Boolean? = null
    private val layoutRemindMe: View?
        get() = if(isRemindMe == true)
            itemView.layout_remind_me_notified
        else
            itemView.layout_remind_me_un_notified
    private val loaderRemindMe: View?
        get() = if(isRemindMe == true)
            itemView.loader_remind_me_notified
        else
            itemView.loader_remind_me_un_notified
    private val imageNotification: ImageView?
        get() = if(isRemindMe == true)
            itemView.image_notification_notified
        else
            itemView.image_notification_un_notified
    private val textRemindMe: Typography?
        get() = if(isRemindMe == true)
            itemView.text_remind_me_notified
        else
            itemView.text_remind_me_un_notified

    override val coroutineContext = masterJob + Dispatchers.Main

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_new_product_launch_campaign
        private const val TITLE_MARGIN_FIRST_ITEM = 16
        private const val DURATION_TO_HIDE_REMIND_ME_WORDING = 5000L
        private const val PADDING_LEFT_PERCENTAGE = 0.47f
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
        if (!GlobalConfig.isSellerApp())
            setRemindMe(model)
        setBannerImage(model)
        setProductCarousel(model)
        setWidgetImpressionListener(model)
        setBannerClickableArea(model)
    }

    private fun setBannerClickableArea(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val productList = model.data?.firstOrNull()?.productList ?: listOf()
        val clickableBannerAreaWidth = (getScreenWidth() * PADDING_LEFT_PERCENTAGE).toInt()
        itemView.banner_clickable_area?.apply {
            if (productList.isNotEmpty()) {
                layoutParams?.width = clickableBannerAreaWidth
                setOnClickListener {
                    shopHomeCampaignNplWidgetListener.onClickCampaignBannerAreaNplWidget(model)
                }
                show()
            } else {
                hide()
            }
        }
    }

    private fun setProductCarousel(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val productList = model.data?.firstOrNull()?.productList ?: listOf()
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
                    }
                    val clickableBannerAreaWidth = (getScreenWidth() *  PADDING_LEFT_PERCENTAGE).toInt()
                    if(productList.isNotEmpty())
                        productListCampaignAdapter?.addElement(ShopHomeCampaignCarouselClickableBannerAreaUiModel(clickableBannerAreaWidth))
                    productListCampaignAdapter?.addElement(productList)
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
        val productCardWidth = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_145)
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
            try {
                if(context.isValidGlideContext())
                    setImageUrl(bannerUrl, heightRatio = 1f)
            } catch (e: Throwable) { }
        }
    }

    private fun setRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        isRemindMe = model.data?.firstOrNull()?.isRemindMe
        isRemindMe?.let {
            hideAllRemindMeLayout()
            layoutRemindMe?.show()
            layoutRemindMe?.setOnClickListener {
                if (loaderRemindMe?.isVisible == false) {
                    shopHomeCampaignNplWidgetListener.onClickRemindMe(model)
                }
            }
            if (it) {
                hideRemindMeText(model, it)
                model.data?.firstOrNull()?.isHideRemindMeTextAfterXSeconds = true
            } else {
                val isHideRemindMeTextAfterXSeconds = model.data?.firstOrNull()?.isHideRemindMeTextAfterXSeconds ?: false
                if(isHideRemindMeTextAfterXSeconds){
                    hideRemindMeText(model, it)
                }else{
                    textRemindMe?.show()
                    launchCatchError(block = {
                        delay(DURATION_TO_HIDE_REMIND_ME_WORDING)
                        if (isRemindMe == false) {
                            hideRemindMeText(model, isRemindMe ?: false)
                        }
                        model.data?.firstOrNull()?.isHideRemindMeTextAfterXSeconds = true
                    }) {}
                }
            }
            checkRemindMeLoading(model)
        }
    }

    private fun hideAllRemindMeLayout() {
        itemView.layout_remind_me_notified.hide()
        itemView.layout_remind_me_un_notified.hide()
    }

    private fun hideRemindMeText(model: ShopHomeNewProductLaunchCampaignUiModel, isRemindMe: Boolean) {
        val totalNotifyWording = model.data?.firstOrNull()?.totalNotifyWording.orEmpty()
        textRemindMe?.apply {
            val colorText = if(isRemindMe){
                com.tokopedia.unifyprinciples.R.color.Unify_N0
            }else{
                com.tokopedia.unifyprinciples.R.color.Unify_N700_68
            }
            setTextColor(MethodChecker.getColor(itemView.context, colorText))
            if(totalNotifyWording.isEmpty()) {
                hide()
            } else {
                val totalNotify = model.data?.firstOrNull()?.totalNotify ?: 0
                val totalNotifyFormatted = totalNotify.thousandFormatted(1, RoundingMode.DOWN)
                show()
                text = totalNotifyFormatted
            }
        }
    }

    private fun checkRemindMeLoading(model: ShopHomeNewProductLaunchCampaignUiModel) {
        if (model.data?.firstOrNull()?.showRemindMeLoading == true) {
            imageNotification?.hide()
            textRemindMe?.hide()
            loaderRemindMe?.show()
        } else {
            imageNotification?.show()
            loaderRemindMe?.hide()
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
        }else{
            itemView.layout_timer?.gone()
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

    private fun setCampaignDescription(description: String, statusCampaign: String) {
        if (description.isEmpty() || isStatusCampaignFinished(statusCampaign)) {
            itemView.text_description?.text = ""
            itemView.text_description?.hide()
        } else {
            itemView.text_description?.text = MethodChecker.fromHtml(description)
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
        val dynamicRuleDescription = model.data?.firstOrNull()?.dynamicRule?.descriptionHeader.orEmpty()
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
        setTitle(title)
        setTnc(title, model)
        setCta(model)
        setCampaignDescription(dynamicRuleDescription, statusCampaign)
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
            itemView.text_title?.apply {
                (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                    val topMargin = if (adapterPosition == 0) {
                        TITLE_MARGIN_FIRST_ITEM.toPx()
                    } else {
                        this.topMargin
                    }
                    setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
                }
                text = title
                show()
            }
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