package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcCalendarWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.CalendarEventPagerAdapter
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.unifycomponents.NotificationUnify

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
        private const val ONE = 1
    }

    private val binding by lazy { ShcCalendarWidgetBinding.bind(itemView) }
    private val loadingStateBinding by lazy { binding.shcCalendarWidgetLoadingState }
    private val errorStateBinding by lazy { binding.shcCalendarWidgetErrorState }
    private val emptyStateBinding by lazy { binding.shcCalendarWidgetEmptyState }

    private val pagerAdapter by lazy {
        CalendarEventPagerAdapter()
    }
    private val layoutManager by lazy {
        return@lazy object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
    }

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
        binding.viewShcCalendarShadow.setBackgroundResource(R.drawable.bg_shc_calendar_shadow)
    }

    private fun observeState(element: CalendarWidgetUiModel) {
        bindViewData(element)
        when {
            element.data == null -> showLoadingState()
            !element.data?.error.isNullOrBlank() -> showErrorState(element)
            else -> showSuccessState(element)
        }
    }

    private fun showSuccessState(element: CalendarWidgetUiModel) {
        loadingStateBinding.viewShcCalendarLoading.gone()
        errorStateBinding.viewShcCalendarError.gone()
        emptyStateBinding.commonWidgetErrorState.gone()

        with(binding) {
            tvShcCalendarTitle.visible()
            tvShcCalendarDate.visible()
            icShcCalendarDate.visible()
            rvShcCalendar.visible()

            showEvents(element)

            val isEmpty = element.data?.eventGroups.isNullOrEmpty()
            if (isEmpty) {
                showEmptyState()
            }
        }
    }

    private fun showEmptyState() {
        binding.rvShcCalendar.gone()
        with(emptyStateBinding) {
            commonWidgetErrorState.visible()
            imgWidgetOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
            tvShcErrorMessage.text = root.context.getString(R.string.shc_calendar_empty_state)
        }
    }

    private fun showErrorState(element: CalendarWidgetUiModel) {
        errorStateBinding.run {
            viewShcCommonLayout.imgWidgetOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
            viewShcCalendarError.visible()
            btnShcCalendarReload.setOnClickListener {
                listener.reloadCalendarWidget(element)
            }
        }
        emptyStateBinding.commonWidgetErrorState.gone()
        loadingStateBinding.viewShcCalendarLoading.gone()
        with(binding) {
            rvShcCalendar.gone()
            pageControlShcCalendar.gone()
        }
    }

    private fun showLoadingState() {
        loadingStateBinding.viewShcCalendarLoading.visible()
        errorStateBinding.viewShcCalendarError.gone()
        emptyStateBinding.commonWidgetErrorState.gone()
        with(binding) {
            rvShcCalendar.gone()
            pageControlShcCalendar.gone()
        }
    }

    private fun showEvents(element: CalendarWidgetUiModel) {
        with(binding) {
            val pages = element.data?.eventGroups.orEmpty()
            if (pages.size > ONE) {
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
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        pageControlShcCalendar.setCurrentIndicator(currentPosition)
                    }
                }
            })
            try {
                PagerSnapHelper().attachToRecyclerView(rvShcCalendar)
            } catch (_: Exception) {
            }
        }
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
            filter.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_DD_MMM
        )
        val endDateFmt = DateTimeUtil.format(
            filter.endDate, DateTimeUtil.FORMAT_DD_MM_YYYY, DateTimeUtil.FORMAT_DD_MMM
        )
        return itemView.context.getString(
            R.string.shc_calendar_date_range, startDateFmt, endDateFmt
        )
    }

    interface Listener : BaseViewHolderListener {
        fun showCalendarWidgetDateFilter(element: CalendarWidgetUiModel) {}

        fun reloadCalendarWidget(element: CalendarWidgetUiModel) {}

        fun sendCalendarImpressionEvent(element: CalendarWidgetUiModel) {}

        fun sendCalendarItemClickEvent(
            element: CalendarWidgetUiModel,
            event: CalendarEventUiModel
        ) {
        }

        fun sendCalendarFilterClickEvent(element: CalendarWidgetUiModel) {}
    }
}