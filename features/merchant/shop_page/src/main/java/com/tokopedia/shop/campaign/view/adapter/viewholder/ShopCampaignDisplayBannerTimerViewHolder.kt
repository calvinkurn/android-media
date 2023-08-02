package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopCampaignDisplayBannerTimerBinding
import com.tokopedia.shop.home.util.DateHelper
import com.tokopedia.shop.home.util.DateHelper.SHOP_CAMPAIGN_BANNER_TIMER_MORE_THAN_1_DAY_DATE_FORMAT
import com.tokopedia.shop.home.util.DateHelper.millisecondsToDays
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayBannerTimerWidgetListener
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight.Companion.VARIANT_ALTERNATE
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight.Companion.VARIANT_DARK_RED
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.view.binding.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import java.math.RoundingMode
import java.util.*

class ShopCampaignDisplayBannerTimerViewHolder(
    itemView: View,
    private val listener: ShopHomeDisplayBannerTimerWidgetListener,
    private val shopCampaignInterface: ShopCampaignInterface
) : AbstractViewHolder<ShopWidgetDisplayBannerTimerUiModel>(itemView), CoroutineScope {

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main
    private var isRemindMe: Boolean? = null
    private val viewBinding: ItemShopCampaignDisplayBannerTimerBinding? by viewBinding()
    private val contentContainer: CardUnify2? = viewBinding?.contentContainer
    private val imageBanner: ImageUnify? = viewBinding?.imageBanner
    private val timerSectionContainer: ConstraintLayout? = viewBinding?.timerSectionContainer
    private val timerContainer: ViewGroup? = viewBinding?.timerContainer
    private val timerUnify: TimerUnifyHighlight? = viewBinding?.timer
    private val timeDescriptionContainer: ViewGroup? = viewBinding?.timeDescriptionContainer
    private val timerMoreThanOneDay: Typography? = viewBinding?.textTimerMoreThan1Day
    private val textTimerDescription: Typography? = viewBinding?.timerDescription
    private val buttonRemindMe: View? = viewBinding?.buttonRemindMe
    private val loaderRemindMe: View? = viewBinding?.loaderRemindMe
    private val remindMeText: Typography? = viewBinding?.textRemindMe
    private val remindMeIcon: IconUnify? = viewBinding?.ivRemindMeBell

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_display_banner_timer
        private const val DURATION_TO_HIDE_REMIND_ME_WORDING = 5000L
        private val STATIC_BLACK_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
        private val STATIC_WHITE_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_Static_White
        private val UNIFY_NN950_DARK = R.color.dms_static_Unify_NN950_dark
        private val UNIFY_NN950_LIGHT = R.color.dms_static_Unify_NN950_light
        private val TOTAL_NOTIFY_TEXT_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_N700_68
    }

    override fun bind(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        setBannerImage(uiModel)
        setTimer(uiModel)
        if (!GlobalConfig.isSellerApp())
            setRemindMe(uiModel)
        setWidgetImpressionListener(uiModel)
        setItemClickListener(uiModel)
        configColorMode()
    }

    private fun setItemClickListener(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        itemView.setOnClickListener {
            listener.onDisplayBannerTimerClicked(bindingAdapterPosition, uiModel)
        }
    }

    private fun configColorMode() {
        if (shopCampaignInterface.isCampaignTabDarkMode()) {
            configDarkModeColor()
        } else {
            configLightModeColor()
        }
    }

    private fun configLightModeColor() {
        contentContainer?.setCardUnifyBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                STATIC_WHITE_COLOR
            )
        )
        textTimerDescription?.setTextColorCompat(UNIFY_NN950_LIGHT)
        timerMoreThanOneDay?.setTextColorCompat(UNIFY_NN950_LIGHT)
        timerUnify?.variant = VARIANT_DARK_RED
        setTimeDescriptionColor(UNIFY_NN950_LIGHT)
    }

    private fun setTimeDescriptionColor(color: Int) {
        for(i in Int.ZERO until timeDescriptionContainer?.childCount.orZero()) {
            val child = timeDescriptionContainer?.getChildAt(i)
            if (child is Typography) {
                child.setTextColorCompat(color)
            }
        }
    }

    private fun configDarkModeColor() {
        contentContainer?.setCardUnifyBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                STATIC_BLACK_COLOR
            )
        )
        textTimerDescription?.setTextColorCompat(UNIFY_NN950_DARK)
        timerMoreThanOneDay?.setTextColorCompat(UNIFY_NN950_DARK)
        timerUnify?.variant = VARIANT_ALTERNATE
        setTimeDescriptionColor(UNIFY_NN950_DARK)
    }

    private fun setTimer(
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        val statusCampaign = model.data?.status
        if (!isStatusCampaignFinished(statusCampaign)) {
            val timeDescription = model.data?.timeDescription.orEmpty()
            val timeCounter = model.data?.timeCounter.orZero()
            textTimerDescription?.text = timeDescription
            textTimerDescription?.show()
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
                setTimerUnify(dateCampaign, timeCounter, statusCampaign, model)
            }
        } else {
            timerContainer?.gone()
            textTimerDescription?.gone()
            timerMoreThanOneDay?.gone()
        }
    }

    private fun setTimerUnify(
        dateCampaign: Date,
        timeCounter: Long,
        statusCampaign: StatusCampaign?,
        model: ShopWidgetDisplayBannerTimerUiModel
    ) {
        timerMoreThanOneDay?.gone()
        if (!timeCounter.isZero()) {
            configTimerUnifyPosition(statusCampaign)
            timerContainer?.apply {
                show()
                timerUnify?.targetDate = Calendar.getInstance().apply {
                    time = dateCampaign
                }
                timerUnify?.onFinish = {
                    listener.onTimerFinished(model)
                }
            }
        } else {
            timerContainer?.gone()
        }
    }

    private fun configTimerUnifyPosition(statusCampaign: StatusCampaign?) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(timerSectionContainer)
        if (isStatusCampaignUpcoming(statusCampaign)) {
            constraintSet.clear(timerContainer?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.clear(timerContainer?.id.orZero(), ConstraintSet.RIGHT)
            constraintSet.clear(timerContainer?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(
                timerContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                timerSectionContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.connect(
                timerContainer?.id.orZero(),
                ConstraintSet.LEFT,
                timerSectionContainer?.id.orZero(),
                ConstraintSet.LEFT,
                0
            )
            constraintSet.connect(
                timerContainer?.id.orZero(),
                ConstraintSet.TOP,
                textTimerDescription?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.setVerticalBias(textTimerDescription?.id.orZero(), 0f)
        } else {
            constraintSet.clear(timerContainer?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.clear(timerContainer?.id.orZero(), ConstraintSet.LEFT)
            constraintSet.clear(timerContainer?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(
                timerContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                timerSectionContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.connect(
                timerContainer?.id.orZero(),
                ConstraintSet.RIGHT,
                timerSectionContainer?.id.orZero(),
                ConstraintSet.RIGHT,
                0
            )
            constraintSet.connect(
                timerContainer?.id.orZero(),
                ConstraintSet.TOP,
                timerSectionContainer?.id.orZero(),
                ConstraintSet.TOP,
                0
            )
            constraintSet.connect(
                textTimerDescription?.id.orZero(),
                ConstraintSet.TOP,
                timerContainer?.id.orZero(),
                ConstraintSet.TOP,
                0
            )
            constraintSet.connect(
                textTimerDescription?.id.orZero(),
                ConstraintSet.BOTTOM,
                timerContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.setVerticalBias(textTimerDescription?.id.orZero(), 0.5f)
        }
        constraintSet.applyTo(timerSectionContainer)
    }

    private fun setTimerNonUnify(dateCampaign: Date) {
        timerContainer?.gone()
        timerMoreThanOneDay?.apply {
            val dateStringFormatted = dateCampaign.toString(
                SHOP_CAMPAIGN_BANNER_TIMER_MORE_THAN_1_DAY_DATE_FORMAT
            )
            text = getString(R.string.shop_campaign_tab_banner_timer_date_format, dateStringFormatted)
            show()
        }
    }

    private fun setBannerImage(uiModel: ShopWidgetDisplayBannerTimerUiModel) {
        val imageBannerUrl = uiModel.data?.imageUrl.orEmpty()
        imageBanner?.setImageUrl(imageBannerUrl)
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

}
