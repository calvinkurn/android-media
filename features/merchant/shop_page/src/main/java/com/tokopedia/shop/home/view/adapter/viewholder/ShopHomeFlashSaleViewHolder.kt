package com.tokopedia.shop.home.view.adapter.viewholder

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.view.adapter.ShopCampaignFlashSaleProductCarouselAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.math.RoundingMode
import java.util.*

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
    private val countDownLayout: View? = itemView.findViewById(R.id.flash_sale_count_down_layout)
    private val timerDescriptionView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_timer_desc)
    private val timerView: TimerUnifySingle? = itemView.findViewById(R.id.tus_flash_sale_timer)
    private val flashSaleReminderView: View? = itemView.findViewById(R.id.flash_sale_reminder_view)
    private val reminderBellView: AppCompatImageView? = itemView.findViewById(R.id.iv_remind_me_bell)
    private val reminderCountView: Typography? = itemView.findViewById(R.id.tgp_remind_me)
    private val productCarouselView: RecyclerView? = itemView.findViewById(R.id.rv_flash_sale_product_carousel)
    private val productCarouselAdapter: ShopCampaignFlashSaleProductCarouselAdapter = ShopCampaignFlashSaleProductCarouselAdapter(listener)
    private val handler = Handler()

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
    }

    init {
        setupClickListener(listener)
        setupProductCardCarouselView(productCarouselView)
        setupWidgetImpressionListener(uiModel)
    }

    override fun bind(element: ShopHomeFlashSaleUiModel) {
        this.uiModel = element
        val flashSaleItem = element.data?.firstOrNull()
        val productSize = flashSaleItem?.productList?.size ?: 0
        setupHeader(element.header.title ?: "")
        setupCtaSeeAll(productSize)
        setupFlashSaleBackgroundView(
            productList = flashSaleItem?.productList.orEmpty(),
            startBackGroundColor = flashSaleItem?.firstBackgroundColor,
            endBackGroundColor = flashSaleItem?.secondBackgroundColor,
        )
        setupFlashSaleCountDownTimer(element)
        if (!GlobalConfig.isSellerApp())
            setupFlashSaleReminder(flashSaleItem)
        setupProductCardCarousel(element)
    }

    private fun setupWidgetImpressionListener(uiModel: ShopHomeFlashSaleUiModel?) {
        uiModel?.data?.firstOrNull()?.let {
            itemView.addOnImpressionListener(uiModel.impressHolder) {
                listener.onFlashSaleWidgetImpressed(uiModel, adapterPosition)
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

    private fun setupCtaSeeAll(productSize: Int) {
        if (productSize == SINGLE) ctaSeeAllView?.hide()
        else ctaSeeAllView?.show()
    }

    private fun setupFlashSaleBackgroundView(productList: List<ShopHomeProductUiModel>, startBackGroundColor: String?, endBackGroundColor: String?) {
        // set flash sale background color
        val colors = intArrayOf(
            getBackGroundColor(startBackGroundColor, R.color.fs_toko_bg_start_dms_gradient_color),
            getBackGroundColor(endBackGroundColor, R.color.fs_toko_bg_end_dms_gradient_color)
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
        when(productList.size) {
            SINGLE -> { singleBackGroundView?.show() }
            DOUBLE -> { doubleBackGroundView?.show() }
            else -> { multipleBackGroundView?.show() }
        }
    }

    private fun setupFlashSaleCountDownTimer(model: ShopHomeFlashSaleUiModel) {
        try {
            val statusCampaign = model.data?.firstOrNull()?.statusCampaign ?: ""
            if (!isStatusCampaignFinished(statusCampaign) && statusCampaign.isNotEmpty()) {
                val timeDescription = model.data?.firstOrNull()?.timeDescription ?: ""
                val timeCounter = model.data?.firstOrNull()?.timeCounter ?: ""
                timerDescriptionView?.text = timeDescription
                countDownLayout?.show()
                if (timeCounter.toLong() != 0L) {
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
                countDownLayout?.hide()
            }
        } catch (e: Throwable) {
            countDownLayout?.hide()
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
        }, DELAY_IN_THREE_SECONDS)
    }

    private fun getTotalNotifyWording(totalNotify: Int): String {
        return if (totalNotify > VALUE_INT_HUNDREDS) {
            totalNotify.thousandFormatted(1, RoundingMode.DOWN)
        } else {
            ""
        }
    }

    private fun setupReminderIconAndWording(isRemindMe: Boolean) {
        if (isRemindMe) {
            reminderBellView?.setImageResource(R.drawable.ic_fs_remind_me_true)
        } else {
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
            productList.add(ShopHomeProductUiModel().apply {
                this.isProductPlaceHolder = isUsingPlaceHolder
                this.totalProduct = totalProduct
                this.totalProductWording = totalProductWording
            })
            // set flash sale ui model for click handling purpose
            productCarouselAdapter.setFsUiModel(model)
        }
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