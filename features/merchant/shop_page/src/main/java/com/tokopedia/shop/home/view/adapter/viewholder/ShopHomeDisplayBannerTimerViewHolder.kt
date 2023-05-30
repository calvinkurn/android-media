package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
import com.tokopedia.shop.home.util.DateHelper.SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT
import com.tokopedia.shop.home.util.DateHelper.millisecondsToDays
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.StatusCampaignInt
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
            val statusCampaign = model.data?.status ?: -1
            if (title.isEmpty() || isStatusCampaignFinished(statusCampaign)) {
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
        val statusCampaign = uiModel.data?.status ?: -1
        val isShowCta = checkIsShowCta(ctaText, statusCampaign)
        textSeeAll?.apply {
            if (!isShowCta) {
                text = ""
                hide()
            } else {
                text = ctaText
                setOnClickListener {
                    listener.onClickCtaDisplayBannerTimerWidget(uiModel)
                }
                show()
            }
        }
    }

    private fun checkIsShowCta(ctaText: String, statusCampaign: Int): Boolean {
        return ctaText.isNotEmpty() && isStatusCampaignOngoing(statusCampaign)
    }

    private fun setTimer(
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        val statusCampaign = model.data?.status ?: -1
        if (!isStatusCampaignFinished(statusCampaign)) {
            val timeDescription = model.data?.timeDescription.orEmpty()
            val timeCounter = model.data?.timeCounter.orZero()
            textTimeDescription?.text = timeDescription
            textTimeDescription?.show()
            val days = model.data?.timeCounter?.millisecondsToDays().orZero()
            val dateCampaign = when {
                isStatusCampaignUpcoming(statusCampaign) -> {
                    DateHelper.getDateFromString(model.data?.startDate.orEmpty())
                }
                isStatusCampaignOngoing(statusCampaign) -> {
                    DateHelper.getDateFromString(model.data?.endDate.orEmpty())
                }
                else -> {
                    Date()
                }
            }
            if (days >= Int.ONE) {
                setTimerNonUnify(dateCampaign)
            } else {
                setTimerUnify(dateCampaign, timeCounter, model)
            }
        } else {
            timerUnify?.gone()
            textTimeDescription?.gone()
            timerMoreThanOneDay?.gone()
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
            text =
                dateCampaign.toString(SHOP_NPL_CAMPAIGN_WIDGET_MORE_THAT_1_DAY_DATE_FORMAT)
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
            com.tokopedia.unifyprinciples.R.color.Unify_G500
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
        imageBanner?.setImageUrl(imageBannerUrl, heightRatio = getHeightRatio(uiModel))
    }

    private fun setRemindMe(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        hideAllRemindMeLayout()
        uiModel.data?.isRemindMe?.let { isRemindMe ->
            buttonRemindMe?.show()
            buttonRemindMe?.setOnClickListener {
                if (loaderRemindMe?.isVisible == false) {
                    listener.onClickRemindMe(uiModel)
                }
            }
            if (isRemindMe) {
                hideRemindMeText(uiModel, isRemindMe)
                uiModel.data.isHideRemindMeTextAfterXSeconds = true
            } else {
                val isHideRemindMeTextAfterXSeconds = uiModel.data.isHideRemindMeTextAfterXSeconds
                if (isHideRemindMeTextAfterXSeconds) {
                    hideRemindMeText(uiModel, isRemindMe)
                }else{
                    buttonRemindMe?.show()
                    launchCatchError(block = {
                        delay(DURATION_TO_HIDE_REMIND_ME_WORDING)
                        hideRemindMeText(uiModel, isRemindMe)
                        uiModel.data.isHideRemindMeTextAfterXSeconds = true
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
            val colorText = if(isRemindMe){
                com.tokopedia.unifyprinciples.R.color.Unify_Background
            } else {
                com.tokopedia.unifyprinciples.R.color.Unify_N700_68
            }
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


    private fun isStatusCampaignFinished(statusCampaign: Int): Boolean {
        return statusCampaign == StatusCampaignInt.FINISHED.statusCampaign
    }

    private fun isStatusCampaignOngoing(statusCampaign: Int): Boolean {
        return statusCampaign == StatusCampaignInt.ONGOING.statusCampaign
    }

    private fun isStatusCampaignUpcoming(statusCampaign: Int): Boolean {
        return statusCampaign == StatusCampaignInt.UPCOMING.statusCampaign
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
