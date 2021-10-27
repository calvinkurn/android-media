package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.view.adapter.ShopCampaignFlashSaleProductCarouselAdapter
import com.tokopedia.shop.home.view.listener.ShopHomeFlashSaleWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeFlashSaleUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class ShopHomeFlashSaleViewHolder(
    itemView: View,
    private val listener: ShopHomeFlashSaleWidgetListener
) : AbstractViewHolder<ShopHomeFlashSaleUiModel>(itemView) {

    private var uiModel: ShopHomeFlashSaleUiModel? = null
    private val flashSaleCampaignNameView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_campaign_name)
    private val tncInfoIconView: AppCompatImageView? = itemView.findViewById(R.id.iv_tnc_info_icon)
    private val ctaSeeAllView: Typography? = itemView.findViewById(R.id.tgp_cta_see_all)
    private val backGroundView: View? = itemView.findViewById(R.id.bg_flash_sale)
    private val countDownLayout: View? = itemView.findViewById(R.id.flash_sale_count_down_layout)
    private val timerDescriptionView: Typography? = itemView.findViewById(R.id.tgp_flash_sale_timer_desc)
    private val timerView: TimerUnifySingle? = itemView.findViewById(R.id.tus_flash_sale_timer)
    private val flashSaleReminderView: View? = itemView.findViewById(R.id.flash_sale_reminder_view)
    private val reminderBellView: AppCompatImageView? = itemView.findViewById(R.id.iv_remind_me_bell)
    private val reminderCountView: Typography? = itemView.findViewById(R.id.tgp_remind_me)
    private val productCarouselView: RecyclerView? = itemView.findViewById(R.id.rv_flash_sale_product_carousel)
    private val productCarouselAdapter: ShopCampaignFlashSaleProductCarouselAdapter = ShopCampaignFlashSaleProductCarouselAdapter()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_flash_sale_widget
        private const val SINGLE = 1
        private const val DOUBLE = 2
        private const val SINGLE_PRODUCT_BACKGROUND_HEIGHT = 198
        private const val DOUBLE_PRODUCT_BACKGROUND_HEIGHT = 212
        private const val MULTIPLE_PRODUCT_BG_HEIGHT = 184
        private const val ONE = 1
        private const val ONE_HUNDRED = 100
        private const val ONE_THOUSAND = 1000
        private const val ONE_MILLION = 1000000
    }

    init { setupClickListener(listener) }

    override fun bind(element: ShopHomeFlashSaleUiModel) {
        this.uiModel = element
        val flashSaleItem = element.data?.firstOrNull()
        val productSize = flashSaleItem?.productList?.size ?: 0
        setupHeader(element.header.title ?: "")
        setupCtaSeeAll(productSize)
        setupFlashSaleBackgroundView(flashSaleItem?.productList ?: listOf())
        setupFlashSaleCountDownTimer(element)
        setupFlashSaleReminder(flashSaleItem?.isRemindMe?:false, flashSaleItem?.totalNotify?:0)
        setupProductCardCarousel(element)
        setupWidgetImpressionListener(element)
        // todo set placeholder
    }

    private fun setupWidgetImpressionListener(uiModel: ShopHomeFlashSaleUiModel) {
        uiModel.data?.firstOrNull()?.let {
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
    }

    private fun setupHeader(campaignName: String) {
        flashSaleCampaignNameView?.text = campaignName
    }

    private fun setupCtaSeeAll(productSize: Int) {
        if (productSize == SINGLE) ctaSeeAllView?.hide()
    }

    private fun setupFlashSaleBackgroundView(productList: List<ShopHomeProductUiModel>) {
        when(productList.size) {
            SINGLE -> {
                backGroundView?.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    SINGLE_PRODUCT_BACKGROUND_HEIGHT
                )
            }
            DOUBLE -> {
                backGroundView?.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    DOUBLE_PRODUCT_BACKGROUND_HEIGHT
                )
            }
            else -> {
                backGroundView?.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    MULTIPLE_PRODUCT_BG_HEIGHT
                )
            }
        }
    }

    private fun setupFlashSaleCountDownTimer(model: ShopHomeFlashSaleUiModel) {
        try {
            val statusCampaign = model.data?.firstOrNull()?.statusCampaign ?: ""
            if (!isStatusCampaignFinished(statusCampaign) && statusCampaign.isNotEmpty()) {
                val timeDescription = model.data?.firstOrNull()?.timeDescription ?: ""
                val timeCounter = model.data?.firstOrNull()?.timeCounter ?: ""
                timerDescriptionView?.text = timeDescription
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

    private fun setupFlashSaleReminder(isRemindMe: Boolean, totalNotify: Int) {
        // set reminder bell icon
        if (isRemindMe) reminderBellView?.setImageResource(R.drawable.ic_fs_remind_me_true)
        else reminderBellView?.setImageResource(R.drawable.ic_fs_remind_me_false)
        // set reminder wording
        val reminderWording = getTotalNotifyWording(totalNotify)
        reminderCountView?.text = reminderWording
    }

    private fun getTotalNotifyWording(reminder: Int): String {
        return when {
            // reminder < 100 => direct number
            reminder < ONE_HUNDRED -> reminder.toString()
            reminder / ONE_MILLION >= ONE -> {
                // reminder in million => e.g. 2 jt
                (reminder / ONE_MILLION).toString() + " " + getString(R.string.shop_page_label_million)
            }
            else -> {
                // reminder in thousand => e.g. 20 rb
                (reminder / ONE_THOUSAND).toString() + " " + getString(R.string.shop_page_label_thousand)
            }
        }
    }

    private fun setupProductCardCarousel(model: ShopHomeFlashSaleUiModel) {
        val productList = model.data?.firstOrNull()?.productList ?: listOf()
        productCarouselAdapter.setProductList(productList)
        productCarouselView?.adapter = productCarouselAdapter
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