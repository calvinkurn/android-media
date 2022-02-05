package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.SellerHomeCommonUtils
import com.tokopedia.sellerhomecommon.databinding.ShcMilestoneWidgetBinding
import com.tokopedia.sellerhomecommon.databinding.ShcMilestoneWidgetErrorBinding
import com.tokopedia.sellerhomecommon.databinding.ShcMilestoneWidgetLoadingBinding
import com.tokopedia.sellerhomecommon.databinding.ShcMilestoneWidgetSuccessBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.MilestoneMissionAdapter
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneFinishMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneProgressbarUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewhelper.MilestoneMissionItemDecoration
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*
import java.util.concurrent.TimeUnit

class MilestoneViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MilestoneWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_milestone_widget

        private const val MARGIN_IN_DP = 8
        private const val PROGRESS_BAR_MIN_VALUE = 0
        private const val PROGRESS_BAR_MAX_VALUE = 100
        private const val FIRST_INDEX = 0
        private const val LAST_ONE = 1
        private const val ONE_CONST = 1L
        private const val FOUR_CONST = 4L
        private const val NINE_CONST = 9L
        private const val ANIMATION_PROGRESS_DURATION = 300L
        private const val ANIMATION_SCALE_0 = 0f
        private const val ANIMATION_SCALE_1 = 1f
    }

    private val binding by lazy { ShcMilestoneWidgetBinding.bind(itemView) }
    private val loadingStateBinding by lazy {
        val view = binding.stubShcMilestoneLoading.inflate()
        ShcMilestoneWidgetLoadingBinding.bind(view)
    }
    private val errorStateBinding by lazy {
        val view = binding.stubShcMilestoneError.inflate()
        ShcMilestoneWidgetErrorBinding.bind(view)
    }
    private val successStateBinding by lazy {
        val view = binding.stubShcMilestoneSuccess.inflate()
        ShcMilestoneWidgetSuccessBinding.bind(view)
    }

    override fun bind(element: MilestoneWidgetUiModel) {
        val data = element.data
        when {
            data == null || element.showLoadingState -> showLoadingState(element)
            data.error.isNotBlank() -> showErrorState(element)
            else -> setOnSuccess(element)
        }
    }

    private fun setOnSuccess(element: MilestoneWidgetUiModel) {
        errorStateBinding.containerShcMilestoneError.gone()
        loadingStateBinding.containerShcMilestoneLoading.gone()
        with(successStateBinding) {
            val data = element.data ?: return@with

            containerShcMilestoneSuccess.visible()
            tvTitleMilestoneWidget.text = data.title
            tvDescMilestoneWidget.text = data.subTitle
            setTagNotification(element.tag)

            val milestoneValueFmt = data.milestoneProgress.percentageFormatted.parseAsHtml()
            tvProgressValueMilestoneWidget.text = milestoneValueFmt

            iconShcToggleMission.setOnClickListener {
                setOnToggleMissionClicked(element)
            }

            showMilestoneBackground(data.backgroundImageUrl)
            setupMilestoneProgress(data.milestoneProgress)
            setupTooltip(tvTitleMilestoneWidget, element)
            btnShcCloseMission.setOnClickListener {
                setOnCloseWidgetClicked(element)
            }
            showCloseWidgetButton(element)
            setupLastUpdatedInfo(element)

            itemView.addOnImpressionListener(element.impressHolder) {
                listener.sendMilestoneWidgetImpressionEvent(element)
                setupMilestoneList(element)
                setupCountDownTimer(element)
            }
        }
    }

    private fun setupLastUpdatedInfo(element: MilestoneWidgetUiModel) {
        with(successStateBinding.luvShcMilestone) {
            element.data?.lastUpdated?.let { lastUpdated ->
                isVisible = lastUpdated.isEnabled
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.needToUpdated)
                setRefreshButtonClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun refreshWidget(element: MilestoneWidgetUiModel) {
        listener.onReloadWidget(element)
    }

    private fun setOnCloseWidgetClicked(element: MilestoneWidgetUiModel) {
        element.data?.milestoneMissions?.let { missions ->
            val finishMissionCard = missions.firstOrNull { it is MilestoneFinishMissionUiModel }
            finishMissionCard?.let {
                val position = missions.indexOf(it)
                listener.onMilestoneMissionActionClickedListener(element, it, position)
            }
        }
    }

    private fun setOnToggleMissionClicked(element: MilestoneWidgetUiModel) {
        with(successStateBinding) {
            if (rvShcMissionMilestone.isVisible) {
                rvShcMissionMilestone.gone()
                tvShcMilestoneCta.gone()
                showProgressWithAnimation()
                iconShcToggleMission.setImage(IconUnify.CHEVRON_UP)
                showCloseWidgetButton(element)
            } else {
                rvShcMissionMilestone.visible()
                setupSeeMoreCta(element)
                hideProgressWithAnimation()
                iconShcToggleMission.setImage(IconUnify.CHEVRON_DOWN)
                btnShcCloseMission.gone()
            }
            if (!element.isAlreadyMinimized) {
                element.isAlreadyMinimized = true
                listener.sendMilestoneWidgetMinimizeClickEvent()
            }
        }
    }

    private fun showCloseWidgetButton(element: MilestoneWidgetUiModel) {
        with(successStateBinding) {
            val isAllMissionFinished = element.data?.milestoneMissions
                ?.any { it is MilestoneFinishMissionUiModel }
                .orFalse()
            btnShcCloseMission.isVisible = isAllMissionFinished
        }
    }

    private fun hideProgressWithAnimation() {
        with(successStateBinding) {
            val animation = ScaleAnimation(
                ANIMATION_SCALE_1, ANIMATION_SCALE_0,
                ANIMATION_SCALE_1, ANIMATION_SCALE_1,
                Animation.RELATIVE_TO_SELF, ANIMATION_SCALE_0,
                Animation.RELATIVE_TO_SELF, ANIMATION_SCALE_1
            ).apply {
                fillAfter = true
                duration = ANIMATION_PROGRESS_DURATION
            }

            root.post {
                progressBarShcMilestone.invisible()
                progressBarShcMilestone.startAnimation(animation)
                tvProgressValueMilestoneWidget.animate()
                    .x(guidelineShcMilestoneStart.x)
                    .setDuration(ANIMATION_PROGRESS_DURATION)
                    .start()
            }
        }
    }

    private fun showProgressWithAnimation() {
        with(successStateBinding) {
            val animation = ScaleAnimation(
                ANIMATION_SCALE_0, ANIMATION_SCALE_1,
                ANIMATION_SCALE_1, ANIMATION_SCALE_1,
                Animation.RELATIVE_TO_SELF, ANIMATION_SCALE_0,
                Animation.RELATIVE_TO_SELF, ANIMATION_SCALE_1
            ).apply {
                fillAfter = true
                duration = ANIMATION_PROGRESS_DURATION
            }

            root.post {
                progressBarShcMilestone.visible()
                progressBarShcMilestone.startAnimation(animation)
                tvProgressValueMilestoneWidget.animate()
                    .x(viewShcProgressBarEnd.x)
                    .setDuration(ANIMATION_PROGRESS_DURATION)
                    .start()
            }
        }
    }

    private fun setupCountDownTimer(element: MilestoneWidgetUiModel) {
        val data = element.data ?: return
        val now = Date().time
        val diffMillis = data.deadlineMillis.minus(now)
        val nineDaysMillis = TimeUnit.DAYS.toMillis(NINE_CONST)
        val fourDaysMillis = TimeUnit.DAYS.toMillis(FOUR_CONST)
        val oneDaysMillis = TimeUnit.DAYS.toMillis(ONE_CONST)

        when {
            diffMillis < oneDaysMillis -> setupCountDownTimer(data.deadlineMillis)
            diffMillis in oneDaysMillis until fourDaysMillis -> showTimerLastFourDays(data.deadlineMillis)
            diffMillis in (fourDaysMillis.plus(LAST_ONE)) until nineDaysMillis -> {
                showTimerLastNineDays(data.deadlineMillis)
            }
            diffMillis > nineDaysMillis -> showTimerMoreThanNineDays(data.deadlineMillis)
        }
    }

    private fun showTimerMoreThanNineDays(deadlineMillis: Long) {
        with(successStateBinding) {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_T100)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_DAY
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun showTimerLastNineDays(deadlineMillis: Long) {
        with(successStateBinding) {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_Y400)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_DAY
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun showTimerLastFourDays(deadlineMillis: Long) {
        with(successStateBinding) {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_R500)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_DAY
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun setupCountDownTimer(deadlineMillis: Long) {
        with(successStateBinding) {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_R500)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_HOUR
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(successStateBinding) {
            notifTagMilestone.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagMilestone.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun showMilestoneBackground(imageUrl: String) {
        if (imageUrl.isNotBlank()) {
            successStateBinding.imgShcBgMilestone.loadImage(imageUrl)
        }
    }

    private fun setupSeeMoreCta(element: MilestoneWidgetUiModel) {
        with(successStateBinding) {
            val appLink = element.getSeeMoreCtaApplink()
            val ctaText = element.getSeeMoreCtaText()

            val isCtaVisible = appLink.isNotBlank() && ctaText.isNotBlank()
            tvShcMilestoneCta.isVisible = isCtaVisible

            if (isCtaVisible) {
                tvShcMilestoneCta.text = ctaText
                tvShcMilestoneCta.setOnClickListener {
                    RouteManager.route(itemView.context, appLink)
                    listener.sendMilestoneWidgetCtaClickEvent()
                }
                val iconColor = root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_G400
                )
                val iconWidth = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                val iconHeight = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
                )
                tvShcMilestoneCta.setUnifyDrawableEnd(
                    IconUnify.CHEVRON_RIGHT,
                    iconColor,
                    iconWidth,
                    iconHeight
                )
            }
        }
    }

    private fun setupMilestoneList(element: MilestoneWidgetUiModel) {
        with(successStateBinding) {
            val mission = element.data ?: return
            rvShcMissionMilestone.layoutManager = object : LinearLayoutManager(
                root.context, HORIZONTAL, false
            ) {
                override fun canScrollVertically(): Boolean = false
            }

            try {
                rvShcMissionMilestone.removeItemDecorationAt(FIRST_INDEX)
                rvShcMissionMilestone.removeItemDecorationAt(
                    mission.milestoneMissions.size.minus(LAST_ONE)
                )
            } catch (e: IndexOutOfBoundsException) {
                //do nothing
            }
            rvShcMissionMilestone.addItemDecoration(
                MilestoneMissionItemDecoration(root.context.dpToPx(MARGIN_IN_DP).toInt())
            )
            rvShcMissionMilestone.adapter = MilestoneMissionAdapter(
                mission,
                object : MilestoneMissionAdapter.Listener {

                    override fun onMissionActionClick(
                        mission: BaseMilestoneMissionUiModel,
                        position: Int
                    ) {
                        listener.onMilestoneMissionActionClickedListener(element, mission, position)
                    }

                    override fun onMissionImpressionListener(
                        mission: BaseMilestoneMissionUiModel,
                        position: Int
                    ) {
                        listener.sendMilestoneMissionImpressionEvent(mission, position)
                    }
                }
            )
        }
    }

    private fun setupMilestoneProgress(milestoneProgress: MilestoneProgressbarUiModel) {
        with(successStateBinding) {
            val valuePerIndicator = try {
                PROGRESS_BAR_MAX_VALUE / milestoneProgress.totalTask
            } catch (e: ArithmeticException) {
                PROGRESS_BAR_MIN_VALUE
            }
            progressBarShcMilestone.progressBarHeight = ProgressBarUnify.SIZE_MEDIUM
            val isCompleted = milestoneProgress.taskCompleted == milestoneProgress.totalTask
            val progressValue = if (isCompleted) {
                PROGRESS_BAR_MAX_VALUE
            } else {
                milestoneProgress.taskCompleted.times(valuePerIndicator)
            }
            progressBarShcMilestone.setValue(progressValue)
        }
    }

    private fun showErrorState(element: MilestoneWidgetUiModel) {
        successStateBinding.containerShcMilestoneSuccess.gone()
        loadingStateBinding.containerShcMilestoneLoading.gone()
        errorStateBinding.run {
            containerShcMilestoneError.visible()
            tvShcMilestoneErrorStateTitle.text = element.title
            btnMilestoneError.setOnClickListener {
                listener.onReloadWidget(element)
            }

            imgMilestoneOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)

            setupTooltip(tvShcMilestoneErrorStateTitle, element)
        }
    }

    private fun showLoadingState(element: MilestoneWidgetUiModel) {
        successStateBinding.containerShcMilestoneSuccess.gone()
        errorStateBinding.containerShcMilestoneError.gone()
        loadingStateBinding.run {
            containerShcMilestoneLoading.visible()
            tvShcMilestoneErrorTitle.text = element.title
            setupTooltip(tvShcMilestoneErrorTitle, element)
        }
    }

    private fun setupTooltip(textView: TextView, element: MilestoneWidgetUiModel) {
        SellerHomeCommonUtils.setupWidgetTooltip(textView, element) {
            listener.onTooltipClicked(it)
        }
    }

    interface Listener : BaseViewHolderListener {

        fun onMilestoneMissionActionClickedListener(
            element: MilestoneWidgetUiModel,
            mission: BaseMilestoneMissionUiModel,
            missionPosition: Int
        ) {
        }

        fun sendMilestoneWidgetImpressionEvent(element: MilestoneWidgetUiModel) {}

        fun sendMilestoneMissionImpressionEvent(
            mission: BaseMilestoneMissionUiModel,
            position: Int
        ) {
        }

        fun sendMilestoneWidgetCtaClickEvent() {}

        fun sendMilestoneWidgetMinimizeClickEvent() {}
    }
}