package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.Context
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.databinding.ItemShopHomeNewProductLaunchCampaignBinding
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.util.DateHelper.SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT
import com.tokopedia.shop.home.util.DateHelper.millisecondsToDays
import com.tokopedia.shop.home.util.RecyclerviewPoolListener
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapter
import com.tokopedia.shop.home.view.adapter.ShopCampaignCarouselProductAdapterTypeFactory
import com.tokopedia.shop.home.view.listener.ShopHomeCampaignNplWidgetListener
import com.tokopedia.shop.home.view.model.BannerType
import com.tokopedia.shop.home.view.model.ShopHomeCampaignCarouselClickableBannerAreaUiModel
import com.tokopedia.shop.home.view.model.ShopHomeNewProductLaunchCampaignUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.*
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date
import com.tokopedia.shop.common.R as shopcommonR
import com.tokopedia.shop_widget.R as shop_widgetR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by alvarisi on 12/12/17.
 */

class ShopHomeNplCampaignViewHolder(
    itemView: View,
    private val shopHomeCampaignNplWidgetListener: ShopHomeCampaignNplWidgetListener,
    private val recyclerviewPoolListener: RecyclerviewPoolListener
) : AbstractViewHolder<ShopHomeNewProductLaunchCampaignUiModel>(itemView), CoroutineScope {

    private val viewBinding: ItemShopHomeNewProductLaunchCampaignBinding? by viewBinding()
    private val masterJob = SupervisorJob()
    private var isRemindMe: Boolean? = null
    private val rvProductCarousel: RecyclerView? = viewBinding?.rvProductCarousel
    private val bannerBackground: ImageUnify? = viewBinding?.bannerBackground
    private val timerUnify: TimerUnifySingle? = viewBinding?.nplTimer
    private val timerMoreThanOneDay: Typography? = viewBinding?.textTimerMoreThan1Day
    private val textTimeDescription: Typography? = viewBinding?.nplTimerDescription
    private var iconCtaChevron: IconUnify? = viewBinding?.iconCtaChevron
    private val loaderRemindMe: LoaderUnify? = viewBinding?.loaderRemindMe
    private val nplReminderView: CardUnify2? = viewBinding?.nplReminderView
    private val layoutContainerRemindMe: LinearLayout? = viewBinding?.layoutContainerRemindMe
    private val remindMeText: Typography? = viewBinding?.tvNplRemindMe
    private val imageRemindMeNotification: IconUnify? = viewBinding?.ivRemindMeBell
    private val imageTnc: ImageView? = viewBinding?.imageTnc
    private val textTitle: Typography? = viewBinding?.textTitle
    private val textFollowersOnly: Typography? = viewBinding?.tvExclusiveFollower
    private val nplPromoOfferContainer: ConstraintLayout? = viewBinding?.nplPromoOfferContainer
    private val textVoucherWording: Typography? = viewBinding?.nplOfferText
    private val containerBackground: View? = viewBinding?.containerBackground
    override val coroutineContext = masterJob + Dispatchers.Main

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_new_product_launch_campaign
        private const val DURATION_TO_HIDE_REMIND_ME_WORDING = 5000L
        private const val PADDING_LEFT_PERCENTAGE = 0.4f
        private const val NPL_RULE_ID_FOLLOWERS_ONLY = "33"
        private const val RV_CAROUSEL_MARGIN_TOP = 24f
        private const val BANNER_IMAGE_RATIO_EMPTY_PRODUCT = "1:1"
        private val SHOP_RE_IMAGINE_MARGIN = 16f.dpToPx()
        private val REMINDER_BUTTON_RADIUS = 20f.dpToPx()
        private val REMINDER_BUTTON_PADDING_NO_TEXT = 4.toPx()
        private val REMINDER_BUTTON_PADDING_WITH_TEXT = 8.toPx()
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
        if (!GlobalConfig.isSellerApp()) {
            setRemindMe(model)
        }
        setBannerImage(model)
        setProductCarousel(model)
        setWidgetImpressionListener(model)
        setFollowersOnlyView(model)
        setVoucherPromoOffer(model)
        configColorTheme(model)
        setShopReimaginedContainerMargin()

        handleRemindMe(model)
    }

    private fun handleRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        //If campaign is ongoing, hide Remind Me bell
        
        if (GlobalConfig.isSellerApp()) {
            val campaignStatus = model.data?.firstOrNull()?.statusCampaign.orEmpty().lowercase()
            if (campaignStatus == StatusCampaign.ONGOING.statusCampaign) {
                hideAllRemindMeLayout()
            }
        }
    }

    private fun configColorTheme(model: ShopHomeNewProductLaunchCampaignUiModel) {
        if (model.isFestivity) {
            configFestivity()
        } else {
            if (model.header.isOverrideTheme) {
                configReimaginedColor(model.header.colorSchema)
            } else {
                configDefaultColor()
            }
        }
    }

    private fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val titleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        val subTitleColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
        val ctaColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR)
        val informationIconColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR)
        
        textTitle?.setTextColor(titleColor)
        textTimeDescription?.setTextColor(subTitleColor)
        iconCtaChevron?.setColorFilter(ctaColor, PorterDuff.Mode.SRC_ATOP)
        imageTnc?.setColorFilter(informationIconColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_MAIN
        timerMoreThanOneDay?.apply {
            background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_red_rect)
            setTextColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White))
        }
        nplReminderView?.setCardUnifyBackgroundColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White))
        imageRemindMeNotification?.setColorFilter(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_Black_68), PorterDuff.Mode.SRC_IN)
        remindMeText?.setTextColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_Black_68))
    }

    private fun configFestivity() {
        val festivityTextColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White)
        textTitle?.setTextColor(festivityTextColor)
        textTimeDescription?.setTextColor(festivityTextColor)
        iconCtaChevron?.setColorFilter(festivityTextColor, PorterDuff.Mode.SRC_ATOP)
        imageTnc?.setColorFilter(festivityTextColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
        timerMoreThanOneDay?.apply {
            background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_white_rect)
            setTextColor(MethodChecker.getColor(itemView.context, shopcommonR.color.dms_shop_festivity_timer_text_color))
        }
        nplReminderView?.setCardUnifyBackgroundColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White))
        imageRemindMeNotification?.setColorFilter(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_Black_68), PorterDuff.Mode.SRC_IN)
        remindMeText?.setTextColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_Black_68))
    }

    private fun configDefaultColor() {
        val defaultTitleColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950)
        val defaultSubTitleColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN950)
        val defaultCtaColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN900)
        val defaultInformationIconColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN900)
        textTitle?.setTextColor(defaultTitleColor)
        textTimeDescription?.setTextColor(defaultSubTitleColor)
        iconCtaChevron?.setColorFilter(defaultCtaColor, PorterDuff.Mode.SRC_ATOP)
        imageTnc?.setColorFilter(defaultInformationIconColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_MAIN
        timerMoreThanOneDay?.apply {
            background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_red_rect)
            setTextColor(MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_Static_White))
        }
    }

    private fun setShopReimaginedContainerMargin() {
        containerBackground?.let {
            it.clipToOutline = true
            it.background = MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_reimagined_rounded)
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
        return (
            dynamicRoleData?.any {
                it.ruleID == NPL_RULE_ID_FOLLOWERS_ONLY && it.isActive
            }
            ).orFalse()
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
                    if (productList.isNotEmpty()) {
                        productListCampaignAdapter?.addElement(
                            ShopHomeCampaignCarouselClickableBannerAreaUiModel(clickableBannerAreaWidth)
                        )
                    }
                    productListCampaignAdapter?.addElement(productList)
                    isNestedScrollingEnabled = false
                    adapter = productListCampaignAdapter
                    setRecycledViewPool(recyclerviewPoolListener.parentPool)
                    setHeightBasedOnProductCardMaxHeight(
                        productList.map {
                            ShopPageHomeMapper.mapToProductCardCampaignModel(
                                isHasAddToCartButton = false,
                                hasThreeDots = false,
                                shopHomeProductViewModel = it,
                                widgetName = model.name,
                                statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty(),
                                forceLightModeColor = shopHomeCampaignNplWidgetListener.isForceLightModeColorOnCampaignNplWidget()
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
        val isProductListEmpty = model.data?.firstOrNull()?.productList?.isEmpty().orFalse()
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
            val dimensionRatio = if (isProductListEmpty) {
                BANNER_IMAGE_RATIO_EMPTY_PRODUCT
            } else {
                ""
            }
            (layoutParams as? ConstraintLayout.LayoutParams)?.dimensionRatio = dimensionRatio
            loadImage(bannerUrl)
        }
    }

    private fun setRemindMe(model: ShopHomeNewProductLaunchCampaignUiModel) {
        hideAllRemindMeLayout()
        isRemindMe = model.data?.firstOrNull()?.isRemindMe
        isRemindMe?.let {
            nplReminderView?.show()
            nplReminderView?.radius = REMINDER_BUTTON_RADIUS
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
                } else {
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
            val colorText = if (isRemindMe) {
                unifyprinciplesR.color.Unify_Background
            } else {
                unifyprinciplesR.color.Unify_NN950_68
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
                layoutContainerRemindMe?.setPadding(
                    REMINDER_BUTTON_PADDING_NO_TEXT,
                    0,
                    REMINDER_BUTTON_PADDING_NO_TEXT,
                    0
                )
            } else {
                val totalNotify = model.data?.firstOrNull()?.totalNotify ?: 0
                val totalNotifyFormatted = totalNotify.thousandFormatted(1, RoundingMode.DOWN)
                show()
                layoutContainerRemindMe?.setPadding(
                    REMINDER_BUTTON_PADDING_WITH_TEXT,
                    0,
                    REMINDER_BUTTON_PADDING_WITH_TEXT,
                    0
                )
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
            if (days >= Int.ONE) {
                timerUnify?.gone()
                timerMoreThanOneDay?.apply {
                    val dateFormatted = getFormattedDate(statusCampaign, model, false).toString(SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT)
                    text = getString(R.string.shop_widget_date_format_wib, dateFormatted)
                    show()
                }
            } else {
                timerMoreThanOneDay?.gone()
                if (!timeCounter.toLong().isZero()) {
                    timerUnify?.apply {
                        show()
                        targetDate = Calendar.getInstance().apply {
                            time = getFormattedDate(statusCampaign, model, true)
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

    private fun getFormattedDate(
        statusCampaign: String,
        model: ShopHomeNewProductLaunchCampaignUiModel,
        isUseDefaultTimeZone: Boolean
    ): Date {
        val timeZone = if (isUseDefaultTimeZone) {
            DateHelper.getDefaultTimeZone()
        } else {
            null
        }
        return when {
            isStatusCampaignUpcoming(statusCampaign) -> {
                DateHelper.getDateFromString(model.data?.firstOrNull()?.startDate.orEmpty(), timeZone)
            }

            isStatusCampaignOngoing(statusCampaign) -> {
                DateHelper.getDateFromString(model.data?.firstOrNull()?.endDate.orEmpty(), timeZone)
            }

            else -> {
                Date()
            }
        }
    }

    private fun setCta(model: ShopHomeNewProductLaunchCampaignUiModel) {
        val ctaText = model.header.ctaText
        val productList = model.data?.firstOrNull()?.productList ?: listOf()
        val statusCampaign = model.data?.firstOrNull()?.statusCampaign.orEmpty()
        val isShowCta = (ctaText.isNotEmpty()) && (isStatusCampaignOngoing(statusCampaign)) && (productList.size > Int.ONE)
        iconCtaChevron?.apply {
            if (!isShowCta) {
                hide()
            } else {
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
