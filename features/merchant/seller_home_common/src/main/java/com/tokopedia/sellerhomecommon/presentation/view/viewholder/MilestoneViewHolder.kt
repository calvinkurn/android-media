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
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
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

    private var binding: ShcMilestoneWidgetBinding? = null
    private var loadingStateBinding: ShcMilestoneWidgetLoadingBinding? = null
    private var errorStateBinding: ShcMilestoneWidgetErrorBinding? = null
    private var successStateBinding: ShcMilestoneWidgetSuccessBinding? = null

    override fun bind(element: MilestoneWidgetUiModel) {
        initViewBinding()

        val data = element.data
        when {
            data == null || element.showLoadingState -> showLoadingState(element)
            data.error.isNotBlank() -> showErrorState(element)
            else -> setOnSuccess(element)
        }
    }

    private fun initViewBinding() {
        if (binding == null) {
            binding = ShcMilestoneWidgetBinding.bind(itemView)

            binding?.let {

                val loadingStateViewStub = it.stubShcMilestoneLoading.inflate()
                loadingStateBinding = ShcMilestoneWidgetLoadingBinding.bind(loadingStateViewStub)

                val errorStateViewStub = it.stubShcMilestoneError.inflate()
                errorStateBinding = ShcMilestoneWidgetErrorBinding.bind(errorStateViewStub)

                val successStateViewStub = it.stubShcMilestoneSuccess.inflate()
                successStateBinding = ShcMilestoneWidgetSuccessBinding.bind(successStateViewStub)
            }
        }
    }

    private fun setOnSuccess(element: MilestoneWidgetUiModel) {
        errorStateBinding?.containerShcMilestoneError?.gone()
        loadingStateBinding?.containerShcMilestoneLoading?.gone()
        successStateBinding?.run {
            val data = element.data ?: return@run

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

            horLineShcMilestoneBtm.isVisible = luvShcMilestone.isVisible
                    || tvShcMilestoneCta.isVisible

            itemView.addOnImpressionListener(element.impressHolder) {
                listener.sendMilestoneWidgetImpressionEvent(element)
                setupMilestoneList(element)
                setupCountDownTimer(element)
            }
        }
    }

    private fun setupLastUpdatedInfo(element: MilestoneWidgetUiModel) {
        successStateBinding?.luvShcMilestone?.run {
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
        successStateBinding?.run {
            if (rvShcMissionMilestone.isVisible) {
                rvShcMissionMilestone.gone()
                tvShcMilestoneCta.gone()
                showProgressWithAnimation()
                iconShcToggleMission.setImage(IconUnify.CHEVRON_DOWN)
                showCloseWidgetButton(element)
            } else {
                rvShcMissionMilestone.visible()
                setupSeeMoreCta(element)
                hideProgressWithAnimation()
                iconShcToggleMission.setImage(IconUnify.CHEVRON_UP)
                btnShcCloseMission.gone()
            }
            if (!element.isAlreadyMinimized) {
                element.isAlreadyMinimized = true
                listener.sendMilestoneWidgetMinimizeClickEvent()
            }
        }
    }

    private fun showCloseWidgetButton(element: MilestoneWidgetUiModel) {
        successStateBinding?.run {
            val isAllMissionFinished = element.data?.milestoneMissions
                ?.any { it is MilestoneFinishMissionUiModel }
                .orFalse()
            btnShcCloseMission.isVisible = isAllMissionFinished
        }
    }

    private fun hideProgressWithAnimation() {
        successStateBinding?.run {
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
        successStateBinding?.run {
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
        successStateBinding?.run {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_TN50)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_DAY
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun showTimerLastNineDays(deadlineMillis: Long) {
        successStateBinding?.run {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_YN400)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_DAY
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun showTimerLastFourDays(deadlineMillis: Long) {
        successStateBinding?.run {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_DAY
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun setupCountDownTimer(deadlineMillis: Long) {
        successStateBinding?.run {
            val timerBackground =
                root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_RN500)
            timerShcMilestone.setBackgroundColor(timerBackground)
            timerShcMilestone.timerFormat = TimerUnifySingle.FORMAT_HOUR
            timerShcMilestone.targetDate = Calendar.getInstance().apply {
                time = Date(deadlineMillis)
            }
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        successStateBinding?.run {
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
            successStateBinding?.imgShcBgMilestone?.loadImage(imageUrl)
        }
    }

    private fun setupSeeMoreCta(element: MilestoneWidgetUiModel) {
        successStateBinding?.run {
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
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
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
        successStateBinding?.run {
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
        successStateBinding?.run {
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
        successStateBinding?.containerShcMilestoneSuccess?.gone()
        loadingStateBinding?.containerShcMilestoneLoading?.gone()
        errorStateBinding?.run {
            containerShcMilestoneError.visible()
            tvShcMilestoneErrorStateTitle.text = element.title
            shcMilestoneErrorStateView.setOnReloadClicked {
                listener.onReloadWidget(element)
            }
            setupTooltip(tvShcMilestoneErrorStateTitle, element)
        }
    }

    private fun showLoadingState(element: MilestoneWidgetUiModel) {
        successStateBinding?.containerShcMilestoneSuccess?.gone()
        errorStateBinding?.containerShcMilestoneError?.gone()
        loadingStateBinding?.run {
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