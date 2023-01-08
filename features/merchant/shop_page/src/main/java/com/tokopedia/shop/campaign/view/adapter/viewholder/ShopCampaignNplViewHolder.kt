package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.view.ShopCarouselBannerImageUnify
import com.tokopedia.shop.databinding.ItemShopCampaignNewProductLaunchBinding
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.BannerType
import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopCampaignNplViewHolder(
    itemView: View,
    private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener,
    private val widgetConfigListener: WidgetConfigListener
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView), CoroutineScope {

    private val viewBinding: ItemShopCampaignNewProductLaunchBinding? by viewBinding()
    private val masterJob = SupervisorJob()
    private var isRemindMe: Boolean? = null
    private val layoutRemindMe: View?
        get() = if (isRemindMe == true)
            viewBinding?.layoutRemindMeNotified?.root
        else
            viewBinding?.layoutRemindMeUnNotified?.root
    private val loaderRemindMe: View?
        get() = if (isRemindMe == true)
            viewBinding?.layoutRemindMeNotified?.loaderRemindMeNotified
        else
            viewBinding?.layoutRemindMeUnNotified?.loaderRemindMeUnNotified
    private val imageNotification: ImageView?
        get() = if (isRemindMe == true)
            viewBinding?.layoutRemindMeNotified?.imageNotificationNotified
        else
            viewBinding?.layoutRemindMeUnNotified?.imageNotificationUnNotified
    private val textRemindMe: Typography?
        get() = if (isRemindMe == true)
            viewBinding?.layoutRemindMeNotified?.textRemindMeNotified
        else
            viewBinding?.layoutRemindMeUnNotified?.textRemindMeUnNotified
    private val rvProductCarousel: RecyclerView? = viewBinding?.rvProductCarousel
    private val bannerClickableArea: View? = viewBinding?.bannerClickableArea
    private val bannerBackground: ShopCarouselBannerImageUnify? = viewBinding?.bannerBackground
    private val layoutRemindMeNotified: View? = viewBinding?.layoutRemindMeNotified?.root
    private val layoutRemindMeUnNotified: View? = viewBinding?.layoutRemindMeUnNotified?.root
    private val textTimeDescription: Typography? = viewBinding?.layoutTimer?.textTimeDescription
    private val layoutTimer: View? = viewBinding?.layoutTimer?.root
    private val timerUnify: TimerUnifySingle? = viewBinding?.layoutTimer?.nplTimer
    private val textDescription: Typography? = viewBinding?.textDescription
    private val textSeeAll: Typography? = viewBinding?.textSeeAll
    private val imageTnc: ImageView? = viewBinding?.imageTnc
    private val textTitle: Typography? = viewBinding?.textTitle
    override val coroutineContext = masterJob + Dispatchers.Main

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_new_product_launch
        private const val TITLE_MARGIN_FIRST_ITEM = 16
        private const val DURATION_TO_HIDE_REMIND_ME_WORDING = 5000L
        private const val PADDING_LEFT_PERCENTAGE = 0.47f
    }

    private var productListCampaignAdapter: ShopCampaignCarouselProductAdapter? = null
    private var model: ShopHomeNewProductLaunchCampaignUiModel? = null

    init {
        rvProductCarousel?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        bannerClickableArea?.apply {
            if (productList.isNotEmpty()) {
                layoutParams?.width = clickableBannerAreaWidth
                setOnClickListener {
                    shopHomeCampaignNplWidgetListener.onClickCampaignBannerAreaNplWidget(model, adapterPosition)
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
        rvProductCarousel?.apply {
            launch {
                try {
                    val rvState = model.data?.firstOrNull()?.rvState
                    if (null != rvState) {
                        rvProductCarousel?.layoutManager?.onRestoreInstanceState(rvState)
                    }
                    val clickableBannerAreaWidth = (getScreenWidth() * PADDING_LEFT_PERCENTAGE).toInt()
                    productListCampaignAdapter?.clearAllElements()
                    if (productList.isNotEmpty())
                        productListCampaignAdapter?.addElement(ShopHomeCampaignCarouselClickableBannerAreaUiModel(clickableBannerAreaWidth))
                    productListCampaignAdapter?.addElement(productList)
                    isNestedScrollingEnabled = false
                    adapter = productListCampaignAdapter
                    setHeightBasedOnProductCardMaxHeight(
                        productList.map {
                            ShopPageHomeMapper.mapToProductCardCampaignModel(
                                isHasAddToCartButton = false,
                                hasThreeDots = false,
                                shopHomeProductViewModel = it,
                                widgetName = model.name,
                                statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
                            )
                        }
                    )
                } catch (throwable: Exception) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
        productCardModelList: List<ProductCardModel>
    ) {
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
        bannerBackground?.apply {
            try {
                if (context.isValidGlideContext())
                    if (DeviceScreenInfo.isTablet(context)) {
                        setImageUrlTileMode(bannerUrl)
                    } else {
                        setImageUrl(bannerUrl, heightRatio = 1f)
                    }
            } catch (e: Exception) { }
        }
    }

    private fun setRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        hideAllRemindMeLayout()
        isRemindMe = model.data?.firstOrNull()?.isRemindMe
        isRemindMe?.let {
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
                if (isHideRemindMeTextAfterXSeconds) {
                    hideRemindMeText(model, it)
                } else {
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
        layoutRemindMeNotified?.hide()
        layoutRemindMeUnNotified?.hide()
    }

    private fun hideRemindMeText(model: ShopHomeNewProductLaunchCampaignUiModel, isRemindMe: Boolean) {
        val totalNotifyWording = model.data?.firstOrNull()?.totalNotifyWording.orEmpty()
        textRemindMe?.apply {
            val colorText = if (isRemindMe) {
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_N700_68
            }
            setTextColor(MethodChecker.getColor(itemView.context, colorText))
            if (totalNotifyWording.isEmpty()) {
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
            itemView.addOnImpressionListener(model.impressHolder) {
                shopHomeCampaignNplWidgetListener.onImpressionCampaignNplWidget(adapterPosition, model)
            }
        }
    }

    private fun setTimer(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign ?: ""
        if (!isStatusCampaignFinished(statusCampaign) && statusCampaign.isNotEmpty()) {
            val timeDescription = model.data?.firstOrNull()?.timeDescription ?: ""
            val timeCounter = model.data?.firstOrNull()?.timeCounter ?: ""
            textTimeDescription?.text = timeDescription
            layoutTimer?.show()
            if (timeCounter.toLong() != 0L) {
                timerUnify?.apply {
                    when {
                        isStatusCampaignUpcoming(statusCampaign) -> {
                            val startDate = DateHelper.getDateFromString(model.data?.firstOrNull()?.startDate ?: "").time
                            val calendar = Calendar.getInstance()
                            calendar.time = Date(startDate)
                            targetDate = calendar
                        }
                        isStatusCampaignOngoing(statusCampaign) -> {
                            val endDate = DateHelper.getDateFromString(model.data?.firstOrNull()?.endDate ?: "").time
                            val calendar = Calendar.getInstance()
                            calendar.time = Date(endDate)
                            targetDate = calendar
                        }
                    }
                    isShowClockIcon = false
                    onFinish = {
                        shopHomeCampaignNplWidgetListener.onTimerFinished(model)
                    }
                    show()
                }
            } else {
                timerUnify?.gone()
            }
        } else {
            layoutTimer?.gone()
        }
    }

    private fun setCampaignDescription(description: String, statusCampaign: String) {
        if (description.isEmpty() || isStatusCampaignFinished(statusCampaign)) {
            textDescription?.text = ""
            textDescription?.hide()
        } else {
            textDescription?.text = MethodChecker.fromHtml(description)
            textDescription?.show()
        }
    }

    private fun setCta(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val ctaText = model.header.ctaText
        textSeeAll?.apply {
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
            setTextColor(widgetConfigListener.getWidgetTextColor())
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
        imageTnc?.apply {
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
            textTitle?.text = ""
            textTitle?.hide()
            imageTnc?.hide()
        } else {
            textTitle?.apply {
                text = title
                setTextColor(widgetConfigListener.getWidgetTextColor())
                show()
            }
            imageTnc?.show()
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
