package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
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
import com.tokopedia.kotlin.extensions.view.setTextColorCompat
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
    private val viewBinding: ItemShopCampaignDisplayBannerTimerBinding? by viewBinding()
    private val contentContainer: CardUnify2? = viewBinding?.contentContainer
    private val imageBanner: ImageUnify? = viewBinding?.imageBanner
    private val timerContainer: ConstraintLayout? = viewBinding?.timerContainer
    private val timerUnify: TimerUnifyHighlight? = viewBinding?.timer
    private val timerMoreThanOneDay: Typography? = viewBinding?.textTimerMoreThan1Day
    private val textTimeDescription: Typography? = viewBinding?.timerDescription
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
        textTimeDescription?.setTextColorCompat(UNIFY_NN950_LIGHT)
        timerMoreThanOneDay?.setTextColorCompat(UNIFY_NN950_LIGHT)
        timerUnify?.variant = VARIANT_DARK_RED
    }

    private fun configDarkModeColor() {
        contentContainer?.setCardUnifyBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                STATIC_BLACK_COLOR
            )
        )
        textTimeDescription?.setTextColorCompat(UNIFY_NN950_DARK)
        timerMoreThanOneDay?.setTextColorCompat(UNIFY_NN950_DARK)
        timerUnify?.variant = VARIANT_ALTERNATE
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
            timerUnify?.gone()
            textTimeDescription?.gone()
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

    private fun configTimerUnifyPosition(statusCampaign: StatusCampaign?) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(timerContainer)
        if (isStatusCampaignUpcoming(statusCampaign)) {
            constraintSet.clear(timerUnify?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.clear(timerUnify?.id.orZero(), ConstraintSet.RIGHT)
            constraintSet.clear(timerUnify?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(
                timerUnify?.id.orZero(),
                ConstraintSet.BOTTOM,
                timerContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.connect(
                timerUnify?.id.orZero(),
                ConstraintSet.LEFT,
                timerContainer?.id.orZero(),
                ConstraintSet.LEFT,
                0
            )
            constraintSet.connect(
                timerUnify?.id.orZero(),
                ConstraintSet.TOP,
                textTimeDescription?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
        } else {
            constraintSet.clear(timerUnify?.id.orZero(), ConstraintSet.BOTTOM)
            constraintSet.clear(timerUnify?.id.orZero(), ConstraintSet.LEFT)
            constraintSet.clear(timerUnify?.id.orZero(), ConstraintSet.TOP)
            constraintSet.connect(
                timerUnify?.id.orZero(),
                ConstraintSet.BOTTOM,
                timerContainer?.id.orZero(),
                ConstraintSet.BOTTOM,
                0
            )
            constraintSet.connect(
                timerUnify?.id.orZero(),
                ConstraintSet.RIGHT,
                timerContainer?.id.orZero(),
                ConstraintSet.RIGHT,
                0
            )
            constraintSet.connect(
                timerUnify?.id.orZero(),
                ConstraintSet.TOP,
                timerContainer?.id.orZero(),
                ConstraintSet.TOP,
                0
            )
        }
        constraintSet.applyTo(timerContainer)
    }

    private fun setTimerNonUnify(dateCampaign: Date) {
        timerUnify?.gone()
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
        uiModel.data?.isRemindMe?.let { isRemindMe ->
            buttonRemindMe?.show()
            buttonRemindMe?.setOnClickListener {
                if (loaderRemindMe?.isVisible == false) {
                    listener.onClickRemindMe(uiModel)
                }
            }
            if (isRemindMe) {
                hideRemindMeText(uiModel, true)
                uiModel.data.isHideRemindMeTextAfterXSeconds = true
            } else {
                val isHideRemindMeTextAfterXSeconds = uiModel.data.isHideRemindMeTextAfterXSeconds
                if (isHideRemindMeTextAfterXSeconds) {
                    hideRemindMeText(uiModel, false)
                } else {
                    buttonRemindMe?.show()
                    launchCatchError(block = {
                        delay(DURATION_TO_HIDE_REMIND_ME_WORDING)
                        hideRemindMeText(uiModel, false)
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
            val colorText = if (isRemindMe) {
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
