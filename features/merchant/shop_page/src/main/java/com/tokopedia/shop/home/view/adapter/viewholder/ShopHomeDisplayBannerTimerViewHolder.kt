package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopHomeDisplayBannerTimerBinding
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.util.DateHelper.SHOP_CAMPAIGN_BANNER_TIMER_MORE_THAN_1_DAY_DATE_FORMAT_ENDED
import com.tokopedia.shop.home.util.DateHelper.SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT
import com.tokopedia.shop.home.util.DateHelper.millisecondsToDays
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import java.math.RoundingMode
import java.util.*

class ShopHomeDisplayBannerTimerViewHolder(
    itemView: View,
    private val listener: ShopHomeDisplayBannerTimerWidgetListener
) : AbstractViewHolder<ShopWidgetDisplayBannerTimerUiModel>(itemView), CoroutineScope {

    private val viewBinding: ItemShopHomeDisplayBannerTimerBinding? by viewBinding()
    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main
    private var isRemindMe: Boolean? = null
    private val imageBanner: ImageUnify? = viewBinding?.imageBanner
    private val timerUnify: TimerUnifySingle? = viewBinding?.nplTimer
    private val timerMoreThanOneDay: Typography? = viewBinding?.textTimerMoreThan1Day
    private val textTimeDescription: Typography? = viewBinding?.nplTimerDescription
    private val buttonRemindMe: View? = viewBinding?.buttonRemindMe
    private val loaderRemindMe: View? = viewBinding?.loaderRemindMe
    private val remindMeText: Typography? = viewBinding?.textRemindMe
    private val remindMeIcon: IconUnify? = viewBinding?.ivRemindMeBell
    private val textSeeAll: Typography? = viewBinding?.textSeeAll
    private val imageTnc: ImageView? = viewBinding?.imageTnc
    private val textTitle: Typography? = viewBinding?.textTitle

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_display_banner_timer
        private const val DURATION_TO_HIDE_REMIND_ME_WORDING = 5000L
        private val TOTAL_NOTIFY_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_NN950_68
    }

    override fun bind(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        setHeader(uiModel)
        setTimer(uiModel)
        if (!GlobalConfig.isSellerApp())
            setRemindMe(uiModel)
        setBannerImage(uiModel)
        setWidgetImpressionListener(uiModel)
        checkFestivity(uiModel)
    }

    private fun setHeader(
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        val title = model.header.title
        setTitle(title)
        setTnc(title, model)
        setCta(model)
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

    private fun setTnc(title: String, model: ShopWidgetDisplayBannerTimerUiModel) {
        imageTnc?.apply {
            if (title.isEmpty()) {
                hide()
            } else {
                show()
                setOnClickListener {
                    listener.onClickTncDisplayBannerTimerWidget(model)
                }
            }
        }
    }

    private fun setCta(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        val ctaText = uiModel.header.ctaText
        val statusCampaign = uiModel.data?.status
        val isShowCta = checkIsShowCta(ctaText, statusCampaign)
        textSeeAll?.apply {
            if (!isShowCta) {
                text = ""
                hide()
            } else {
                text = ctaText
                setOnClickListener {
                    listener.onClickCtaDisplayBannerTimerWidget(bindingAdapterPosition, uiModel)
                }
                show()
            }
        }
    }

    private fun checkIsShowCta(ctaText: String, statusCampaign: StatusCampaign?): Boolean {
        return ctaText.isNotEmpty() && isStatusCampaignOngoing(statusCampaign)
    }

    private fun setTimer(
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        val statusCampaign = model.data?.status
        if (!isStatusCampaignFinished(statusCampaign)) {
            val timeDescription = model.data?.timeDescription.orEmpty()
            val timeCounter = model.data?.timeCounter.orZero()
            textTimeDescription?.text = timeDescription
            textTimeDescription?.show()
            val days = model.data?.timeCounter?.millisecondsToDays().orZero()
            if (days >= Int.ONE) {
                val formattedDate = getFormattedDate(statusCampaign, model,false)
                setTimerNonUnify(formattedDate)
            } else {
                val formattedDate = getFormattedDate(statusCampaign, model, true)
                setTimerUnify(formattedDate, timeCounter, model)
            }
        } else {
            timerUnify?.gone()
            timerMoreThanOneDay?.gone()
            val endDate = DateHelper.getDateFromString(model.data?.endDate.orEmpty(), null)
            val textTimeDescriptionFinished = MethodChecker.fromHtml(
                itemView.context.getString(
                    R.string.shop_home_tab_banner_timer_finish_date_format,
                    endDate.toString(SHOP_CAMPAIGN_BANNER_TIMER_MORE_THAN_1_DAY_DATE_FORMAT_ENDED)
                )
            )
            textTimeDescription?.apply {
                show()
                text = textTimeDescriptionFinished
            }
        }
    }

    private fun getFormattedDate(
        statusCampaign: StatusCampaign?,
        model: ShopWidgetDisplayBannerTimerUiModel,
        isUseDefaultTimeZone: Boolean
    ): Date {
        val timeZone = if (isUseDefaultTimeZone) {
            DateHelper.getDefaultTimeZone()
        } else {
            null
        }
        return when {
            isStatusCampaignUpcoming(statusCampaign) -> {
                DateHelper.getDateFromString(model.data?.startDate.orEmpty(), timeZone)
            }

            isStatusCampaignOngoing(statusCampaign) -> {
                DateHelper.getDateFromString(model.data?.endDate.orEmpty(), timeZone)
            }

            else -> {
                Date()
            }
        }
    }

    private fun setTimerUnify(
        dateCampaign: Date,
        timeCounter: Long,
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        timerMoreThanOneDay?.gone()
        if (!timeCounter.isZero()) {
            timerUnify?.apply {
                show()
                targetDate = Calendar.getInstance().apply {
                    time = dateCampaign
                }
                onFinish = {
                    listener.onTimerFinished(model)
                }
            }
        } else {
            timerUnify?.gone()
        }
    }

    private fun setTimerNonUnify(dateCampaign: Date) {
        timerUnify?.gone()
        timerMoreThanOneDay?.apply {
            val dateFormatted = dateCampaign.toString(SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT)
            text = getString(R.string.shop_widget_date_format_wib, dateFormatted)
            show()
        }
    }

    private fun checkFestivity(
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        if (model.isFestivity) {
            configFestivity()
        } else {
            configNonFestivity()
        }
    }

    private fun configFestivity() {
        val festivityTextColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        )
        textTitle?.setTextColor(festivityTextColor)
        textTimeDescription?.setTextColor(festivityTextColor)
        textSeeAll?.setTextColor(festivityTextColor)
        imageTnc?.setColorFilter(festivityTextColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_ALTERNATE
        timerMoreThanOneDay?.apply {
            background =
                MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_white_rect)
            setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.shop.common.R.color.dms_shop_festivity_timer_text_color
                )
            )
        }
    }

    private fun configNonFestivity() {
        val defaultTitleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
        val defaultSubTitleColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
        val defaultCtaColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )
        val defaultInformationIconColor = MethodChecker.getColor(
            itemView.context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN900
        )
        textTitle?.setTextColor(defaultTitleColor)
        textTimeDescription?.setTextColor(defaultSubTitleColor)
        textSeeAll?.setTextColor(defaultCtaColor)
        imageTnc?.setColorFilter(defaultInformationIconColor)
        timerUnify?.timerVariant = TimerUnifySingle.VARIANT_MAIN
        timerMoreThanOneDay?.apply {
            background =
                MethodChecker.getDrawable(itemView.context, R.drawable.bg_shop_timer_red_rect)
            setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN0
                )
            )
        }
    }

    private fun setBannerImage(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        val imageBannerUrl = uiModel.data?.imageUrl.orEmpty()
        imageBanner?.apply {
            setImageUrl(imageBannerUrl)
            setOnClickListener {
                listener.onDisplayBannerTimerClicked(bindingAdapterPosition, uiModel)
            }
        }
    }

    private fun setRemindMe(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        hideAllRemindMeLayout()
        isRemindMe = uiModel.data?.isRemindMe
        isRemindMe?.let {
            buttonRemindMe?.show()
            buttonRemindMe?.setOnClickListener {
                if (loaderRemindMe?.isVisible == false) {
                    listener.onClickRemindMe(bindingAdapterPosition, uiModel)
                }
            }
            if (it) {
                hideRemindMeText(uiModel, true)
                uiModel.data?.isHideRemindMeTextAfterXSeconds = true
            } else {
                val isHideRemindMeTextAfterXSeconds = uiModel.data?.isHideRemindMeTextAfterXSeconds.orFalse()
                if (isHideRemindMeTextAfterXSeconds) {
                    hideRemindMeText(uiModel, false)
                }else{
                    buttonRemindMe?.show()
                    launchCatchError(block = {
                        delay(DURATION_TO_HIDE_REMIND_ME_WORDING)
                        if (isRemindMe == false) {
                            hideRemindMeText(uiModel, false)
                        }
                        uiModel.data?.isHideRemindMeTextAfterXSeconds = true
                    }) {}
                }
            }
            checkRemindMeLoading(uiModel)
        }
    }

    private fun hideAllRemindMeLayout() {
        buttonRemindMe?.hide()
    }

    private fun hideRemindMeText(model: ShopWidgetDisplayBannerTimerUiModel, isRemindMe: Boolean) {
        val totalNotifyWording = model.data?.totalNotifyWording.orEmpty()
        remindMeText?.apply {
            val colorText = TOTAL_NOTIFY_TEXT_COLOR
            val iconRemindMe = if (isRemindMe) {
                IconUnify.BELL_FILLED
            } else {
                IconUnify.BELL_RING
            }
            remindMeIcon?.setImage(iconRemindMe)
            setTextColor(MethodChecker.getColor(itemView.context, colorText))
            if (totalNotifyWording.isEmpty()) {
                hide()
            } else {
                val totalNotify = model.data?.totalNotify ?: 0
                val totalNotifyFormatted = totalNotify.thousandFormatted(1, RoundingMode.DOWN)
                show()
                text = totalNotifyFormatted
            }
        }
    }

    private fun checkRemindMeLoading(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        if (uiModel.data?.showRemindMeLoading == true) {
            remindMeIcon?.hide()
            remindMeText?.hide()
            loaderRemindMe?.show()
        } else {
            remindMeIcon?.show()
            loaderRemindMe?.hide()
        }
    }

    private fun setWidgetImpressionListener(model: ShopWidgetDisplayBannerTimerUiModel) {
        model.data?.let {
            itemView.addOnImpressionListener(model.impressHolder) {
                listener.onImpressionDisplayBannerTimerWidget(
                    ShopUtil.getActualPositionFromIndex(bindingAdapterPosition),
                    model
                )
            }
        }
    }


    private fun isStatusCampaignFinished(statusCampaign: StatusCampaign?): Boolean {
        return statusCampaign == StatusCampaign.FINISHED
    }

    private fun isStatusCampaignOngoing(statusCampaign: StatusCampaign?): Boolean {
        return statusCampaign == StatusCampaign.ONGOING
    }

    private fun isStatusCampaignUpcoming(statusCampaign: StatusCampaign?): Boolean {
        return statusCampaign == StatusCampaign.UPCOMING
    }

        private fun getIndexRatio(data: ShopWidgetDisplayBannerTimerUiModel, index: Int): Int {
        return data.header.ratio.split(":").getOrNull(index).toIntOrZero()
    }

    private fun getHeightRatio(uiModel: ShopWidgetDisplayBannerTimerUiModel): Float {
        val indexZero = getIndexRatio(uiModel, 0).toFloat()
        val indexOne = getIndexRatio(uiModel, 1).toFloat()
        return (indexOne / indexZero)
    }
}
