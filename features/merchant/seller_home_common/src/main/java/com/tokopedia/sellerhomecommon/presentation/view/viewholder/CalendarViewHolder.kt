package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.databinding.ShcCalendarWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.CalendarEventPagerAdapter
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.unifycomponents.NotificationUnify
import timber.log.Timber
import java.util.*

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

class CalendarViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<CalendarWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_calendar_widget
    }

    private val binding by lazy { ShcCalendarWidgetBinding.bind(itemView) }
    private val loadingStateBinding by lazy { binding.shcCalendarWidgetLoadingState }
    private val emptyStateBinding by lazy { binding.shcCalendarWidgetEmptyState }

    private val pagerAdapter by lazy {
        CalendarEventPagerAdapter()
    }
    private val layoutManager by lazy {
        return@lazy object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
    }
    private var highestHeight = Int.ZERO

    override fun bind(element: CalendarWidgetUiModel) {
        observeState(element)
        setShadowBackground()
        showTag(element)
    }

    private fun showTag(element: CalendarWidgetUiModel) {
        if (element.tag.isBlank()) {
            binding.notifShcTagCalendar.gone()
        } else {
            binding.notifShcTagCalendar.visible()
            binding.notifShcTagCalendar.setNotification(
                element.tag,
                NotificationUnify.TEXT_TYPE,
                NotificationUnify.COLOR_TEXT_TYPE
            )
        }
    }

    private fun setShadowBackground() {
        binding.viewShcCalendarShadow.run {
            try {
                layoutParams.height = context.resources.getDimensionPixelSize(
                    R.dimen.shc_dimen_22dp
                )
                setBackgroundResource(R.drawable.bg_shc_calendar_shadow)
            } catch (e: Exception) {
                layoutParams.height = context.resources.getDimensionPixelSize(
                    R.dimen.shc_dimen_1dp
                )
                setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN50))
            }
            requestLayout()
        }
    }

    private fun observeState(element: CalendarWidgetUiModel) {
        bindViewData(element)
        when {
            element.data == null || element.showLoadingState -> showLoadingState()
            !element.data?.error.isNullOrBlank() -> showErrorState(element)
            else -> showSuccessState(element)
        }
    }

    private fun showSuccessState(element: CalendarWidgetUiModel) {
        loadingStateBinding.viewShcCalendarLoading.gone()
        binding.shcCalendarWidgetErrorState.gone()
        emptyStateBinding.commonWidgetErrorState.gone()

        with(binding) {
            tvShcCalendarTitle.visible()
            tvShcCalendarDate.visible()
            icShcCalendarDate.visible()
            luvShcCalendar.visible()
            rvShcCalendar.visible()

            showEvents(element)

            val isEmpty = element.data?.eventGroups.isNullOrEmpty()
            if (isEmpty) {
                showEmptyState()
            }

            root.addOnImpressionListener(element.impressHolder) {
                listener.sendCalendarImpressionEvent(element)
            }

            setupLastUpdated(element)
        }
    }

    private fun setupLastUpdated(element: CalendarWidgetUiModel) {
        with(binding.luvShcCalendar) {
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

    private fun refreshWidget(element: CalendarWidgetUiModel) {
        listener.onReloadWidget(element)
    }

    private fun showEmptyState() {
        binding.rvShcCalendar.gone()
        with(emptyStateBinding) {
            commonWidgetErrorState.visible()
            imgWidgetOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_bandwidth)
            tvShcErrorMessage.text = root.context.getString(R.string.shc_calendar_empty_state)
            btnShcReloadErrorState.gone()
        }
    }

    private fun showErrorState(element: CalendarWidgetUiModel) {
        binding.shcCalendarWidgetErrorState.run {
            visible()
            setOnReloadClicked {
                listener.onReloadWidget(element)
            }
        }
        emptyStateBinding.commonWidgetErrorState.gone()
        loadingStateBinding.viewShcCalendarLoading.gone()
        with(binding) {
            luvShcCalendar.gone()
            rvShcCalendar.gone()
            pageControlShcCalendar.gone()
        }
    }

    private fun showLoadingState() {
        loadingStateBinding.viewShcCalendarLoading.visible()
        binding.shcCalendarWidgetErrorState.gone()
        emptyStateBinding.commonWidgetErrorState.gone()
        with(binding) {
            luvShcCalendar.gone()
            rvShcCalendar.gone()
            pageControlShcCalendar.gone()
        }
    }

    private fun showEvents(element: CalendarWidgetUiModel) {
        with(binding) {
            val pages = element.data?.eventGroups.orEmpty()
            if (pages.size > ShcConst.INT_1) {
                pageControlShcCalendar.visible()
                pageControlShcCalendar.setIndicator(pages.size)
            } else {
                pageControlShcCalendar.invisible()
            }

            pagerAdapter.setOnItemClicked {
                val routing = RouteManager.route(itemView.context, it.appLink)
                if (routing) {
                    listener.sendCalendarItemClickEvent(element, it)
                }
            }

            pagerAdapter.eventPages = pages
            rvShcCalendar.layoutManager = layoutManager
            rvShcCalendar.adapter = pagerAdapter
            rvShcCalendar.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    if (currentPosition != RecyclerView.NO_POSITION && pages.size > ShcConst.INT_1) {
                        pageControlShcCalendar.setCurrentIndicator(currentPosition)
                        layoutManager.findViewByPosition(currentPosition)?.let { view ->
                            adjustEventsListViewHeight(view)
                        }
                    }
                }
            })

            scrollToClosestEvent(element)

            try {
                PagerSnapHelper().attachToRecyclerView(rvShcCalendar)
            } catch (e: Exception) {
                Timber.e(e)
            }

            resetEventsListViewHeight()
        }
    }

    private fun scrollToClosestEvent(element: CalendarWidgetUiModel) {
        val filterEndDateMillis = DateTimeUtil.getTimeInMillis(
            element.filter.getDateRange().endDate, DateTimeUtil.FORMAT_DD_MM_YYYY
        )
        val todayInMillis = Date().time
        val canAutoScroll = todayInMillis < filterEndDateMillis

        if (canAutoScroll) {
            val pages = element.data?.eventGroups.orEmpty()
            val closestEventIndex = pages.indexOfFirst { it.autoScrollToHere }
            if (closestEventIndex != RecyclerView.NO_POSITION) {
                binding.rvShcCalendar.scrollToPosition(closestEventIndex)
                binding.pageControlShcCalendar.setCurrentIndicator(closestEventIndex)
            }
        }
    }

    private fun resetEventsListViewHeight() {
        highestHeight = Int.ZERO
        binding.rvShcCalendar.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.rvShcCalendar.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        binding.rvShcCalendar.requestLayout()
    }

    private fun bindViewData(element: CalendarWidgetUiModel) {
        with(binding) {
            tvShcCalendarTitle.text = element.title
            tvShcCalendarDate.text = getDateFilterRangeFmt(element)
            icShcCalendarDate.isVisible = true
            arrayOf(tvShcCalendarTitle, tvShcCalendarDate, icShcCalendarDate)
                .forEach {
                    it.setOnClickListener {
                        listener.showCalendarWidgetDateFilter(element)
                    }
                }
        }
    }

    private fun getDateFilterRangeFmt(element: CalendarWidgetUiModel): String {
        val filter = element.filter
        val startDateFmt = DateTimeUtil.format(
            filter.getDateRange().startDate,
            DateTimeUtil.FORMAT_DD_MM_YYYY,
            DateTimeUtil.FORMAT_DD_MMM
        )
        val endDateFmt = DateTimeUtil.format(
            filter.getDateRange().endDate,
            DateTimeUtil.FORMAT_DD_MM_YYYY,
            DateTimeUtil.FORMAT_DD_MMM
        )
        return itemView.context.getString(
            R.string.shc_calendar_date_range, startDateFmt, endDateFmt
        )
    }

    private fun adjustEventsListViewHeight(view: View) {
        binding.run {
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (rvShcCalendar.layoutParams?.height != view.measuredHeight) {
                rvShcCalendar.layoutParams =
                    (rvShcCalendar.layoutParams as? ConstraintLayout.LayoutParams)
                        ?.also { lp ->
                            if (view.measuredHeight > highestHeight) {
                                highestHeight = view.measuredHeight
                                lp.height = view.measuredHeight
                                lp.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                            }
                        }
            }
        }
    }

    interface Listener : BaseViewHolderListener {
        fun showCalendarWidgetDateFilter(element: CalendarWidgetUiModel) {}

        fun sendCalendarImpressionEvent(element: CalendarWidgetUiModel) {}

        fun sendCalendarItemClickEvent(
            element: CalendarWidgetUiModel,
            event: CalendarEventUiModel
        ) {
        }
    }
}