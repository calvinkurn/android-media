package com.tokopedia.shop_widget.common.customview

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.uimodel.DynamicHeaderUiModel
import com.tokopedia.shop_widget.common.util.DateHelper
import com.tokopedia.shop_widget.common.util.StatusCampaign
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import java.util.Calendar
import java.util.Date
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class DynamicHeaderCustomView : FrameLayout {

    private var listener: HeaderCustomViewListener? = null
    private var itemView: View?
    private var headerContainer: ConstraintLayout? = null
    private var tusCountDown: TimerUnifySingle? = null
    private var tpSeeAll: Typography? = null
    private var tpTitle: Typography? = null
    private var tpSubtitle: Typography? = null
    private var iconCtaChevron: IconUnify? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_header_custom_view, this)
        this.itemView = view
    }

    fun setModel(model: DynamicHeaderUiModel, listener: HeaderCustomViewListener? = null) {
        this.listener = listener
        setHeaderComponent(model)
    }

    private fun setHeaderComponent(model: DynamicHeaderUiModel) {
        setupUi()
        setTitle(model.title)
        setSubtitle(model.subTitle, model.statusCampaign, model.endDate)
        setSeeAllAppLink(model.ctaText, model.ctaTextLink)
    }

    private fun setupUi() {
        headerContainer = itemView?.findViewById(R.id.header_container)
        tusCountDown = itemView?.findViewById(R.id.tus_count_down)
        tpTitle = itemView?.findViewById(R.id.tp_title)
        tpSubtitle = itemView?.findViewById(R.id.tp_subtitle)
        tpSeeAll = itemView?.findViewById(R.id.tp_see_all)
        iconCtaChevron = itemView?.findViewById(R.id.icon_cta_chevron)
    }

    private fun setTitle(title: String) {
        if (title.isNotBlank()) {
            headerContainer?.show()
            tpTitle?.text = title
            tpTitle?.show()
        } else {
            headerContainer?.gone()
        }
    }

    private fun setSubtitle(subtitle: String, statusCampaign: String, endDate: String) {
        if (subtitle.isNotBlank()) {
            tpSubtitle?.text = subtitle
            handleCountDownTimer(statusCampaign, endDate)
        } else {
            tusCountDown?.gone()
            tpSubtitle?.gone()
        }
    }

    private fun setSeeAllAppLink(ctaText: String, ctaTextLink: String) {
        if (ctaTextLink.isNotBlank()) {
            tpSeeAll?.text = if (ctaText.isNotBlank()) {
                ctaText
            } else {
                itemView?.context?.getString(R.string.thematic_widget_see_all)
            }
            tpSeeAll?.setTextColor(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500))
            tpSeeAll?.setOnClickListener {
                listener?.onSeeAllClick(ctaTextLink)
            }
            tpSeeAll?.show()
        } else {
            tpSeeAll?.hide()
        }
    }

    private fun handleCountDownTimer(statusCampaign: String, endDate: String) {
        try {
            tusCountDown?.apply {
                isShowClockIcon = false
                timerFormat = TimerUnifySingle.FORMAT_AUTO
                timerVariant = if (itemView?.context?.isDarkMode() == true) {
                    TimerUnifySingle.VARIANT_ALTERNATE
                } else {
                    TimerUnifySingle.VARIANT_MAIN
                }
                onFinish = {
                    listener?.onTimerFinish()
                }
            }
            checkStatusCampaign(statusCampaign, endDate)
        } catch (e: Throwable) {
            tusCountDown?.gone()
            tpSubtitle?.gone()
        }
    }

    private fun checkStatusCampaign(statusCampaign: String, endDate: String) {
        when {
            isStatusCampaignOngoing(statusCampaign) -> setStatusCampaignOngoing(endDate)
            else -> setStatusCampaignFinished()
        }
    }

    private fun setStatusCampaignFinished() {
        tusCountDown?.gone()
        tpSubtitle?.gone()
    }

    private fun setStatusCampaignOngoing(endDate: String) {
        val calendar = Calendar.getInstance()
        val actualEndDate = DateHelper.getDateFromString(endDate).time
        calendar.time = Date(actualEndDate)
        tusCountDown?.targetDate = calendar
    }

    private fun isStatusCampaignOngoing(statusCampaign: String): Boolean {
        return statusCampaign.equals(StatusCampaign.ONGOING.statusCampaign, true)
    }

    fun configShopPageFestivityColor() {
        val festivityTextColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White)
        tpTitle?.setTextColor(festivityTextColor)
        tpSubtitle?.setTextColor(festivityTextColor)
        iconCtaChevron?.setColorFilter(festivityTextColor, PorterDuff.Mode.SRC_ATOP)
        tusCountDown?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
    }

    fun configDefaultColor() {
        val defaultTitleColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950)
        val defaultSubTitleColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN950)
        val defaultCtaColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_NN900)
        tpTitle?.setTextColor(defaultTitleColor)
        tpSubtitle?.setTextColor(defaultSubTitleColor)
        iconCtaChevron?.setColorFilter(defaultCtaColor, PorterDuff.Mode.SRC_ATOP)
        tusCountDown?.timerVariant = TimerUnifySingle.VARIANT_MAIN
    }

    fun configReimaginedColor(colorSchema: ShopPageColorSchema) {
        val highEmphasizeColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_HIGH_EMPHASIS)
        val lowEmphasizeColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.TEXT_LOW_EMPHASIS)
        val ctaTextColor = colorSchema.getColorIntValue(ShopPageColorSchema.ColorSchemaName.ICON_ENABLED_HIGH_COLOR)
        tpTitle?.setTextColor(highEmphasizeColor)
        tpSubtitle?.setTextColor(lowEmphasizeColor)
        iconCtaChevron?.setColorFilter(ctaTextColor, PorterDuff.Mode.SRC_ATOP)
        tusCountDown?.timerVariant = TimerUnifySingle.VARIANT_MAIN
    }

    fun setShopPageCta(model: DynamicHeaderUiModel) {
        val ctaTextLink = model.ctaTextLink
        tpSeeAll?.hide()
        if (ctaTextLink.isNotBlank()) {
            iconCtaChevron?.setOnClickListener {
                listener?.onSeeAllClick(ctaTextLink)
            }
            iconCtaChevron?.show()
        } else {
            iconCtaChevron?.hide()
        }
    }

    interface HeaderCustomViewListener {
        fun onSeeAllClick(appLink: String)
        fun onTimerFinish()
    }
}
