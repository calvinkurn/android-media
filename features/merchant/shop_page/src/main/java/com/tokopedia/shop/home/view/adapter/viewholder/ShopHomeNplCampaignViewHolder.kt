package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.ShopCarouselBannerImageUnify
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeNewProductLaunchCampaignBinding
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.util.DateHelper.SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT
import com.tokopedia.shop.home.util.DateHelper.millisecondsToDays
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.BannerType
import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.*
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeNplCampaignViewHolder(
    itemView: View,
    private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView), CoroutineScope {

    private val viewBinding: ItemShopHomeNewProductLaunchCampaignBinding? by viewBinding()
    private val masterJob = SupervisorJob()
    private var isRemindMe: Boolean? = null
    private val rvProductCarousel: RecyclerView? = viewBinding?.rvProductCarousel
    private val bannerBackground: ShopCarouselBannerImageUnify? = viewBinding?.bannerBackground
    private val timerUnify: TimerUnifySingle? = viewBinding?.nplTimer
    private val timerMoreThanOneDay: Typography? = viewBinding?.textTimerMoreThan1Day
    private val textTimeDescription: Typography? = viewBinding?.nplTimerDescription
    private val textSeeAll: Typography? = viewBinding?.textSeeAll
    private val loaderRemindMe: LoaderUnify? = viewBinding?.loaderRemindMe
    private val nplReminderView: CardUnify2? = viewBinding?.nplReminderView
    private val remindMeText: Typography? = viewBinding?.tvNplRemindMe
    private val imageRemindMeNotification: IconUnify? = viewBinding?.ivRemindMeBell
    private val imageTnc: ImageView? = viewBinding?.imageTnc
    private val textTitle: Typography? = viewBinding?.textTitle
    private val textFollowersOnly: Typography? = viewBinding?.tvExclusiveFollower
    private val nplPromoOfferContainer: ConstraintLayout? = viewBinding?.nplPromoOfferContainer
    private val textVoucherWording: Typography? = viewBinding?.nplOfferText
    private val containerProductList: View? = viewBinding?.containerProductList
    override val coroutineContext = masterJob + Dispatchers.Main

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_new_product_launch_campaign
        private const val DURATION_TO_HIDE_REMIND_ME_WORDING = 5000L
        private const val PADDING_LEFT_PERCENTAGE = 0.4f
        private const val NPL_RULE_ID_FOLLOWERS_ONLY = "33"
        private const val RV_CAROUSEL_MARGIN_TOP = 24f
        private const val BANNER_IMAGE_RATION_EMPTY_PRODUCT = "1:1"
        private val SHOP_RE_IMAGINE_MARGIN = 16f.dpToPx()
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
        setFollowersOnlyView(model)
        setVoucherPromoOffer(model)
        configColorTheme(model)
    }

    private fun configColorTheme(model: ShopHomeNewProductLaunchCampaignUiModel) {
        if (model.isFestivity) {
            configFestivity()
        } else {
            if (shopHomeListener.isShopHomeTabHasFestivity()) {
                configDefaultColor()
            } else {
                if (model.header.isOverrideTheme) {
                    configReimaginedColor(model.header.colorSchema)
                } else {
                    configDefaultColor()
                }
            }
        }
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        val subTitleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
        val ctaColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.CTA_TEXT_LINK_COLOR)
        val informationIconColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_CTA_LINK_COLOR)
        textTitle?.setTextColor(titleColor)
        textTimeDescription?.setTextColor(subTitleColor)
        textSeeAll?.setTextColor(ctaColor)
        imageTnc?.setColorFilter(informationIconColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_MAIN
        timerMoreThanOneDay?.apply {
            background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_red_rect)
            setTextColor(titleColor)
        }
        setShopReimaginedContainerMargin()
    }

    private fun configFestivity() {
        val festivityTextColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        textTitle?.setTextColor(festivityTextColor)
        textTimeDescription?.setTextColor(festivityTextColor)
        textSeeAll?.setTextColor(festivityTextColor)
        imageTnc?.setColorFilter(festivityTextColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
        timerMoreThanOneDay?.apply {
            background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_white_rect)
            setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.shop.common.R.color.dms_shop_festivity_timer_text_color))
        }
    }

    private fun configDefaultColor() {
        val defaultTitleColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val defaultSubTitleColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val defaultCtaColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val defaultInformationIconColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
        textTitle?.setTextColor(defaultTitleColor)
        textTimeDescription?.setTextColor(defaultSubTitleColor)
        textSeeAll?.setTextColor(defaultCtaColor)
        imageTnc?.setColorFilter(defaultInformationIconColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_MAIN
        timerMoreThanOneDay?.apply {
            background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_red_rect)
            setTextColor(MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
        }
        setShopReimaginedContainerMargin()
    }

    private fun setShopReimaginedContainerMargin() {
        containerProductList?.let {
            it.clipToOutline = true
            it.background = MethodChecker.getDrawable(itemView.context, com.tokopedia.shop_widget.R.drawable.bg_shop_reimagined_rounded)
            (it.layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart = SHOP_RE_IMAGINE_MARGIN.toInt()
            (it.layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd = SHOP_RE_IMAGINE_MARGIN.toInt()
        }
    }

    private fun setVoucherPromoOffer(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val promoVoucherText = model.data?.firstOrNull()?.voucherWording.orEmpty()
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
        if (!isStatusCampaignFinished(statusCampaign) && promoVoucherText.isNotEmpty()) {
            nplPromoOfferContainer?.show()
            textVoucherWording?.text = MethodChecker.fromHtml(promoVoucherText)
        } else {
            nplPromoOfferContainer?.hide()

            // set rvCarousel margin top if voucher layout is hidden
            val constraintSet = ConstraintSet()
            constraintSet.clone(viewBinding?.nplWidgetParentView)
            val marginTop = itemView.context.getDimension(RV_CAROUSEL_MARGIN_TOP)
            constraintSet.setMargin(viewBinding?.rvProductCarousel?.id.orZero(), ConstraintSet.TOP, marginTop)
            constraintSet.applyTo(viewBinding?.nplWidgetParentView)
        }
    }

    private fun setFollowersOnlyView(model: ShopHomeNewProductLaunchCampaignUiModel) {
        textFollowersOnly?.showWithCondition(isExclusiveForFollowers(model))
    }

    private fun isExclusiveForFollowers(model: ShopHomeNewProductLaunchCampaignUiModel): Boolean {
        val dynamicRoleData = model.data?.firstOrNull()?.dynamicRule?.listDynamicRoleData
        return (dynamicRoleData?.any {
            it.ruleID == NPL_RULE_ID_FOLLOWERS_ONLY && it.isActive
        }).orFalse()
    }

    private fun setProductCarousel(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val productList = model.data?.firstOrNull()?.productList ?: listOf()
        productListCampaignAdapter = ShopCampaignCarouselProductAdapter(
            ShopCampaignCarouselProductAdapterTypeFactory(
                model,
                shopHomeCampaignNplWidgetListener,
                ShopUtil.getActualPositionFromIndex(adapterPosition)
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
                        productListCampaignAdapter?.addElement(
                            ShopHomeCampaignCarouselClickableBannerAreaUiModel(clickableBannerAreaWidth)
                        )
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

    private fun Context.getDimension(value: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            this.resources.displayMetrics
        ).toInt()
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
                if (context.isValidGlideContext()) {
                    if (DeviceScreenInfo.isTablet(context)) {
                        setImageUrlTileMode(bannerUrl)
                    } else {
                        setImageUrl(bannerUrl, isOverrideScaleType = false)
                    }
                }
            } catch (e: Exception) { }
        }
    }

    private fun setRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        hideAllRemindMeLayout()
        isRemindMe = model.data?.firstOrNull()?.isRemindMe
        isRemindMe?.let {
            nplReminderView?.show()
            nplReminderView?.setOnClickListener {
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
                }else{
                    remindMeText?.show()
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
        nplReminderView?.hide()
    }

    private fun hideRemindMeText(model: ShopHomeNewProductLaunchCampaignUiModel, isRemindMe: Boolean) {
        val totalNotifyWording = model.data?.firstOrNull()?.totalNotifyWording.orEmpty()
        remindMeText?.apply {
            val colorText = if(isRemindMe){
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
            }
            val iconRemindMe = if (isRemindMe) {
                IconUnify.BELL_FILLED
            } else {
                IconUnify.BELL_RING
            }
            imageRemindMeNotification?.setImage(iconRemindMe)
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
            imageRemindMeNotification?.hide()
            remindMeText?.hide()
            loaderRemindMe?.show()
        } else {
            imageRemindMeNotification?.show()
            loaderRemindMe?.hide()
        }
    }

    private fun setWidgetImpressionListener(model: ShopHomeNewProductLaunchCampaignUiModel) {
        model.data?.firstOrNull()?.let {
            itemView.addOnImpressionListener(model.impressHolder) {
                shopHomeCampaignNplWidgetListener.onImpressionCampaignNplWidget(ShopUtil.getActualPositionFromIndex(adapterPosition), model)
            }
        }
    }

    private fun setTimer(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign ?: ""
        if (!isStatusCampaignFinished(statusCampaign) && statusCampaign.isNotEmpty()) {
            val timeDescription = model.data?.firstOrNull()?.timeDescription ?: ""
            val timeCounter = model.data?.firstOrNull()?.timeCounter ?: ""
            textTimeDescription?.text = timeDescription
            textTimeDescription?.show()
            val days = model.data?.firstOrNull()?.timeCounter?.millisecondsToDays().orZero()
            val dateCampaign = when {
                isStatusCampaignUpcoming(statusCampaign) -> {
                    DateHelper.getDateFromString(model.data?.firstOrNull()?.startDate.orEmpty())
                }
                isStatusCampaignOngoing(statusCampaign) -> {
                    DateHelper.getDateFromString(model.data?.firstOrNull()?.endDate.orEmpty())
                }
                else -> {
                    Date()
                }
            }
            if (days >= Int.ONE) {
                timerUnify?.gone()
                timerMoreThanOneDay?.apply {
                    text =
                        dateCampaign.toString(SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT)
                    show()
                }
            } else {
                timerMoreThanOneDay?.gone()
                if (!timeCounter.toLong().isZero()) {
                    timerUnify?.apply {
                        show()
                        targetDate = Calendar.getInstance().apply {
                            time = dateCampaign
                        }
                        onFinish = {
                            shopHomeCampaignNplWidgetListener.onTimerFinished(model)
                        }
                    }
                } else {
                    timerUnify?.gone()
                }
            }
        } else {
            timerUnify?.gone()
            textTimeDescription?.gone()
            timerMoreThanOneDay?.gone()
        }
    }

    private fun setCta(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val ctaText = model.header.ctaText
        val productList = model.data?.firstOrNull()?.productList ?: listOf()
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
        val isShowCta = (ctaText.isNotEmpty()) && (isStatusCampaignOngoing(statusCampaign)) && (productList.size > Int.ONE)
        textSeeAll?.apply {
            if (!isShowCta) {
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
        val title = model.data?.firstOrNull()?.name.orEmpty()
        setTitle(title)
        setTnc(title, model)
        setCta(model)
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
