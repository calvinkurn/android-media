package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.view.adapter.HeightMeasureListener
import com.tokopedia.shop.home.view.adapter.ShopCampaignFlashSaleProductCarouselAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date

class ShopHomeFlashSaleViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener
) : AbstractViewHolder<ShopHomeFlashSaleUiModel>(itemView) {

    private var uiModel: ShopHomeFlashSaleUiModel? = null
    private val flashSaleCampaignNameView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_campaign_name)
    private val tncInfoIconView: AppCompatImageView? = itemView.findViewById(R.id.iv_tnc_info_icon)
    private val ctaSeeAllView: Typography? = itemView.findViewById(R.id.tgp_cta_see_all)
    private val singleBackGroundView: View? = itemView.findViewById(R.id.bg_single)
    private val doubleBackGroundView: View? = itemView.findViewById(R.id.bg_double)
    private val multipleBackGroundView: View? = itemView.findViewById(R.id.bg_multiple)
    private val timerDescriptionView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_timer_desc)
    private val timerView: TimerUnifySingle? = itemView.findViewById(R.id.tus_flash_sale_timer)
    private val flashSaleReminderView: CardUnify2? = itemView.findViewById(R.id.flash_sale_reminder_view)
    private val reminderBellView: AppCompatImageView? = itemView.findViewById(R.id.iv_remind_me_bell)
    private val reminderCountView: Typography? = itemView.findViewById(R.id.tgp_remind_me)
    private val productCarouselView: RecyclerView? = itemView.findViewById(R.id.rv_flash_sale_product_carousel)
    private val rvContainer: ConstraintLayout? = itemView.findViewById(R.id.rv_container)
    private val productCarouselAdapter: ShopCampaignFlashSaleProductCarouselAdapter = ShopCampaignFlashSaleProductCarouselAdapter(listener)
    private val handler = Handler()
    private val flashSaleContainer: ConstraintLayout? = itemView.findViewById(R.id.flash_sale_container)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_flash_sale_widget
        private const val SINGLE = 1
        private const val DOUBLE = 2
        private const val MAX_PRODUCT_CARD_SIZE = 5
        private const val FORMAT_STRING_COLOR = "#%06x"
        private const val FORMAT_HEX_COLOR = 0xffffff
        private const val FORMAT_PREFIX_HEX_COLOR = "#"
        private const val VALUE_INT_HUNDREDS = 100
        private const val DELAY_IN_THREE_SECONDS = 3000L
        private const val NOTIFY_ME_WRAPPER_BORDER_RADIUS = 16f
        private const val BOTTOM_MARGIN = 8f
        private const val CONTENT_CONTAINER_FESTIVITY_MARGIN_BOTTOM = 10f
        private const val CONTENT_CONTAINER_DEFAULT_MARGIN_BOTTOM = 12f
        private const val RV_CONTAINER_FESTIVITY_MARGIN_TOP = 9f
        private const val RV_CONTAINER_DEFAULT_MARGIN_TOP = 12f
    }

    init {
        setupClickListener(listener)
        setupProductCardCarouselView(productCarouselView)
    }

    override fun bind(element: ShopHomeFlashSaleUiModel) {
        productCarouselAdapter.parentPosition = ShopUtil.getActualPositionFromIndex(adapterPosition)
        this.uiModel = element
        val flashSaleItem = element.data?.firstOrNull()
        val productSize = flashSaleItem?.totalProduct.orZero()
        setupWidgetImpressionListener(uiModel)
        setupHeader(element.header.title ?: "")
        setupCtaSeeAll(productSize, element.data?.firstOrNull()?.statusCampaign)
        setupFlashSaleCountDownTimer(element)
        if (!GlobalConfig.isSellerApp())
            setupFlashSaleReminder(flashSaleItem)
        setupProductCardCarousel(element)
        checkFestivity(element)
    }

    private fun checkFestivity(element: ShopHomeFlashSaleUiModel) {
        if (element.isFestivity) {
            configFestivity()
        } else {
            configNonFestivity(element)
        }
    }

    private fun configNonFestivity(element: ShopHomeFlashSaleUiModel) {
        val defaultTitleColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val defaultSubTitleColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950)
        val defaultCtaColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        val defaultInformationIconColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
        flashSaleCampaignNameView?.setTextColor(defaultTitleColor)
        timerDescriptionView?.setTextColor(defaultSubTitleColor)
        ctaSeeAllView?.setTextColor(defaultCtaColor)
        tncInfoIconView?.setColorFilter(defaultInformationIconColor)
        timerView?.timerVariant = TimerUnifySingle.VARIANT_MAIN
        val flashSaleItem = element.data?.firstOrNull()
        setupFlashSaleBackgroundView(
            productList = flashSaleItem?.productList.orEmpty(),
            startBackGroundColor = flashSaleItem?.firstBackgroundColor,
            endBackGroundColor = flashSaleItem?.secondBackgroundColor
        )
        configMarginNonFestivity()
    }

    private fun configFestivity() {
        val festivityTextColor = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
        flashSaleCampaignNameView?.setTextColor(festivityTextColor)
        timerDescriptionView?.setTextColor(festivityTextColor)
        ctaSeeAllView?.setTextColor(festivityTextColor)
        tncInfoIconView?.setColorFilter(festivityTextColor)
        timerView?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
        singleBackGroundView?.hide()
        doubleBackGroundView?.hide()
        multipleBackGroundView?.hide()
        configMarginFestivity()
    }

    private fun configMarginFestivity(){
        val rvLayoutParams = productCarouselView?.layoutParams as? ConstraintLayout.LayoutParams
        rvLayoutParams?.setMargins(
            rvLayoutParams.leftMargin,
            Int.ZERO,
            rvLayoutParams.rightMargin,
            rvLayoutParams.bottomMargin
        )
        productCarouselView?.layoutParams = rvLayoutParams
        setContainerMarginFestivity()
        setContainerRvMarginFestivity()
    }

    private fun setContainerMarginFestivity() {
        val containerLayoutParams = flashSaleContainer?.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        containerLayoutParams?.setMargins(
            containerLayoutParams.leftMargin,
            containerLayoutParams.topMargin,
            containerLayoutParams.rightMargin,
            CONTENT_CONTAINER_FESTIVITY_MARGIN_BOTTOM.dpToPx().toInt()
        )
        flashSaleContainer?.layoutParams = containerLayoutParams
    }

    private fun setContainerRvMarginFestivity() {
        val rvContainerLayoutParams = rvContainer?.layoutParams as? ConstraintLayout.LayoutParams
        rvContainerLayoutParams?.setMargins(
            rvContainerLayoutParams.leftMargin,
            RV_CONTAINER_FESTIVITY_MARGIN_TOP.dpToPx().toInt(),
            rvContainerLayoutParams.rightMargin,
            rvContainerLayoutParams.bottomMargin
        )
        rvContainer?.layoutParams = rvContainerLayoutParams
    }

    private fun configMarginNonFestivity(){
        val rvLayoutParams = productCarouselView?.layoutParams as? ConstraintLayout.LayoutParams
        rvLayoutParams?.setMargins(
            rvLayoutParams.leftMargin,
            12f.dpToPx().toInt(),
            rvLayoutParams.rightMargin,
            rvLayoutParams.bottomMargin
        )
        productCarouselView?.layoutParams = rvLayoutParams
        setContainerMarginDefault()
        setRvContainerMarginDefault()
    }

    private fun setContainerMarginDefault() {
        val containerLayoutParams = flashSaleContainer?.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        containerLayoutParams?.setMargins(
            containerLayoutParams.leftMargin,
            containerLayoutParams.topMargin,
            containerLayoutParams.rightMargin,
            CONTENT_CONTAINER_DEFAULT_MARGIN_BOTTOM.dpToPx().toInt()
        )
        flashSaleContainer?.layoutParams = containerLayoutParams
    }

    private fun setRvContainerMarginDefault() {
        val containerLayoutParams = flashSaleContainer?.layoutParams as? StaggeredGridLayoutManager.LayoutParams
        containerLayoutParams?.setMargins(
            containerLayoutParams.leftMargin,
            RV_CONTAINER_DEFAULT_MARGIN_TOP.dpToPx().toInt(),
            containerLayoutParams.rightMargin,
            CONTENT_CONTAINER_FESTIVITY_MARGIN_BOTTOM.dpToPx().toInt()
        )
        flashSaleContainer?.layoutParams = containerLayoutParams
    }

    private fun setupWidgetImpressionListener(uiModel: ShopHomeFlashSaleUiModel?) {
        uiModel?.data?.firstOrNull()?.let {
            itemView.addOnImpressionListener(uiModel.impressHolder) {
                listener.onFlashSaleWidgetImpressed(uiModel, ShopUtil.getActualPositionFromIndex(adapterPosition))
            }
        }
    }

    private fun setupClickListener(listener: ShopHomeFlashSaleWidgetListener) {
        tncInfoIconView?.setOnClickListener {
            uiModel?.run {
                listener.onClickTncFlashSaleWidget(this)
            }
        }
        ctaSeeAllView?.setOnClickListener {
            uiModel?.run {
                listener.onClickSeeAllFlashSaleWidget(this)
            }
        }
        flashSaleReminderView?.setOnClickListener {
            uiModel?.run {
                listener.onClickFlashSaleReminder(this)
            }
        }
        timerView?.onFinish = {
            uiModel?.run {
                listener.onTimerFinished(this)
            }
        }
    }

    private fun setupProductCardCarouselView(productCarouselView: RecyclerView?) {
        itemView.context?.run {
            productCarouselView?.isNestedScrollingEnabled = false
            productCarouselView?.adapter = productCarouselAdapter
            productCarouselView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        }
    }

    private fun setupHeader(campaignName: String) {
        flashSaleCampaignNameView?.text = campaignName
    }

    private fun setupCtaSeeAll(productSize: Int, statusCampaign: String?) {
        val isUpcoming = isStatusCampaignUpcoming(statusCampaign.orEmpty())
        if (productSize <= MAX_PRODUCT_CARD_SIZE || isUpcoming) ctaSeeAllView?.hide()
        else ctaSeeAllView?.show()
    }

    private fun setupFlashSaleBackgroundView(productList: List<ShopHomeProductUiModel>, startBackGroundColor: String?, endBackGroundColor: String?) {
        // set flash sale background color
        val colors = intArrayOf(
            getBackGroundColor(startBackGroundColor, R.color.clr_dms_icon_white),
            getBackGroundColor(endBackGroundColor, R.color.clr_dms_icon_white)
        )
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors)
        singleBackGroundView?.background = gradientDrawable
        doubleBackGroundView?.background = gradientDrawable
        multipleBackGroundView?.background = gradientDrawable
        // reset background visibility
        singleBackGroundView?.hide()
        doubleBackGroundView?.hide()
        multipleBackGroundView?.hide()
        // show different background based on products size
        when (productList.size) {
            SINGLE -> {
                setBottomMarginOnMainContainer()
                singleBackGroundView?.show()
            }
            DOUBLE -> { setBackgroundViewHeightAndVisible(doubleBackGroundView) }
            else -> { setBackgroundViewHeightAndVisible(multipleBackGroundView) }
        }
    }

    private fun setBackgroundViewHeightAndVisible(view: View?){
        productCarouselAdapter.setHeightMeasureListener( object : HeightMeasureListener {
            override fun setHeightListener(height: Int) {
                view?.show()
                val layoutRv = productCarouselView?.layoutParams as? ViewGroup.MarginLayoutParams
                val layout = view?.layoutParams
                layout?.height = height + layoutRv?.topMargin.orZero()
                view?.layoutParams = layout
            }
        })
    }

    private fun setBottomMarginOnMainContainer() {
        val paramsMargin = flashSaleContainer?.layoutParams as? ViewGroup.MarginLayoutParams
        paramsMargin?.bottomMargin = BOTTOM_MARGIN.dpToPx().toInt()
        flashSaleContainer?.requestLayout()
    }

    private fun setupFlashSaleCountDownTimer(model: ShopHomeFlashSaleUiModel) {
        try {
            val statusCampaign = model.data?.firstOrNull()?.statusCampaign ?: ""
            if (!isStatusCampaignFinished(statusCampaign) && statusCampaign.isNotEmpty()) {
                val timeDescription = model.data?.firstOrNull()?.timeDescription ?: ""
                val timeCounter = model.data?.firstOrNull()?.timeCounter ?: ""
                timerDescriptionView?.text = timeDescription
                if (timeCounter.toLong() != 0L) {
                    if (itemView.context.isDarkMode()) {
                        timerView?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
                    } else {
                        timerView?.timerVariant = TimerUnifySingle.VARIANT_MAIN
                    }
                    timerDescriptionView?.show()
                    timerView?.show()
                    when {
                        isStatusCampaignUpcoming(statusCampaign) -> {
                            val startDate = DateHelper.getDateFromString(model.data?.firstOrNull()?.startDate ?: "").time
                            val calendar = Calendar.getInstance()
                            calendar.time = Date(startDate)
                            timerView?.targetDate = calendar
                        }
                        isStatusCampaignOngoing(statusCampaign) -> {
                            val endDate = DateHelper.getDateFromString(model.data?.firstOrNull()?.endDate ?: "").time
                            val calendar = Calendar.getInstance()
                            calendar.time = Date(endDate)
                            timerView?.targetDate = calendar
                        }
                    }
                } else {
                    timerView?.gone()
                }
            } else {
                timerDescriptionView?.gone()
                timerView?.gone()
            }
        } catch (e: Throwable) {
            timerDescriptionView?.gone()
            timerView?.gone()
        }
    }

    private fun setupFlashSaleReminder(flashSaleItem: ShopHomeFlashSaleUiModel.FlashSaleItem?) {
        // hide reminder when campaign status is ongoing
        val statusCampaign = flashSaleItem?.statusCampaign ?: ""
        val isOngoing = isStatusCampaignOngoing(statusCampaign)
        showHideReminderButton(isOngoing)

        // set reminder wording
        val totalNotify = flashSaleItem?.totalNotify ?: 0
        val reminderWording = getTotalNotifyWording(totalNotify)
        reminderCountView?.text = reminderWording

        // set reminder bell icon
        val isRemindMe = flashSaleItem?.isRemindMe ?: false
        setupReminderIconAndWording(isRemindMe)

        // set text wording ingatkan into number of users after 3 seconds
        handler.postDelayed({
            reminderCountView?.text = reminderWording
            reminderCountView?.showWithCondition(reminderWording.isNotEmpty())
        }, DELAY_IN_THREE_SECONDS)
    }

    private fun getTotalNotifyWording(totalNotify: Int): String {
        return if (totalNotify > VALUE_INT_HUNDREDS) {
            totalNotify.thousandFormatted(1, RoundingMode.DOWN)
        } else {
            String.EMPTY
        }
    }

    private fun setupReminderIconAndWording(isRemindMe: Boolean) {
        if (isRemindMe) {
            reminderBellView?.setImageResource(R.drawable.ic_fs_remind_me_true)
            reminderCountView?.gone()
        } else {
            reminderCountView?.show()
            reminderCountView?.text = itemView.context.getString(R.string.shop_page_label_remind_me)
            reminderBellView?.setImageResource(R.drawable.ic_fs_remind_me_false)
        }
    }

    private fun showHideReminderButton(isOngoing: Boolean) {
        if (isOngoing) {
            flashSaleReminderView?.hide()
        } else {
            flashSaleReminderView?.show()
        }
    }

    private fun setupProductCardCarousel(model: ShopHomeFlashSaleUiModel) {
        val flashSaleData = model.data?.firstOrNull()
        val productList = flashSaleData?.productList?.toMutableList() ?: mutableListOf()
        // get total product and total product wording
        val totalProduct = flashSaleData?.totalProduct ?: 0
        val totalProductWording = flashSaleData?.totalProductWording ?: ""
        // add product place holder if product list size > 5 and metada is not empty
        val isUsingPlaceHolder = isUsingPlaceHolder(totalProduct, totalProductWording)
        if (isUsingPlaceHolder) {
            productList.add(
                ShopHomeProductUiModel().apply {
                    this.isProductPlaceHolder = isUsingPlaceHolder
                    this.totalProduct = totalProduct
                    this.totalProductWording = totalProductWording
                }
            )
        }
        // set flash sale ui model for click handling purpose
        productCarouselAdapter.setFsUiModel(model)
        // set product list to product carousel adapter
        productCarouselAdapter.setProductList(productList)
    }

    private fun isUsingPlaceHolder(totalProduct: Int, totalProductWording: String): Boolean {
        return totalProduct > MAX_PRODUCT_CARD_SIZE && totalProductWording.isNotBlank()
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

    private fun getBackGroundColor(color: String?, colorRes: Int): Int {
        return try {
            Color.parseColor(getStringColor(color, colorRes))
        } catch (e: Exception) {
            ContextCompat.getColor(itemView.context, colorRes)
        }
    }

    private fun getStringColor(color: String?, colorRes: Int): String {
        return if (color.isNullOrEmpty()) {
            String.format(FORMAT_STRING_COLOR, ContextCompat.getColor(itemView.context, colorRes) and FORMAT_HEX_COLOR)
        } else {
            if (!color.startsWith(FORMAT_PREFIX_HEX_COLOR)) {
                FORMAT_PREFIX_HEX_COLOR + color
            } else {
                color
            }
        }
    }
}
