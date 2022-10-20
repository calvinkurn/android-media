package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.ui.engagement.adapter.EngagementItemDecoration
import com.tokopedia.play.ui.engagement.adapter.EngagementWidgetAdapter
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.PageControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.concurrent.TimeUnit

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementCarouselViewComponent(
    container: ViewGroup,
    @IdRes resId: Int,
    private val listener: Listener,
    private val scope: CoroutineScope
) : ViewComponent(container, resId) {

    private val carousel: RecyclerView = findViewById(R.id.rv_engagement_widget)
    private val indicator: PageControl = findViewById(R.id.play_engagement_indicator)

    private var job: Job? = null

    private val carouselAdapter =
        EngagementWidgetAdapter(object : EngagementWidgetViewHolder.Listener {
            override fun onWidgetGameEnded(engagement: EngagementUiModel.Game) {
                listener.onWidgetGameEnded(this@EngagementCarouselViewComponent, engagement)
            }

            override fun onWidgetClicked(engagement: EngagementUiModel) {
                listener.onWidgetClicked(this@EngagementCarouselViewComponent, engagement)
            }

            override fun onWidgetTimerTick(engagement: EngagementUiModel.Game, timeInMillis: Long) {
                val diff = TimeUnit.MILLISECONDS.toSeconds(timeInMillis)
                if (diff < STOP_SCROLL_TIME) stopAutoScroll() else return
            }
        })

    private val snapHelper by lazy(LazyThreadSafetyMode.NONE) {
        PagerSnapHelper()
    }

    private val linearLayoutManager by lazy(LazyThreadSafetyMode.NONE) {
        LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val view = snapHelper.findSnapView(linearLayoutManager) ?: return
                val position = linearLayoutManager.getPosition(view)
                indicator.setCurrentIndicator(position)
            }
        }
    }

    private val rvSize: Int
        get() = carouselAdapter.itemCount

    init {
        carousel.apply {
            adapter = carouselAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(EngagementItemDecoration(this.context))
            addOnScrollListener(scrollListener)
        }

        snapHelper.attachToRecyclerView(carousel)
    }

    fun setData(list: List<EngagementUiModel>) {
        carouselAdapter.setItemsAndAnimateChanges(list)
        handleAutoScroll()
        setupView()
    }

    private fun setupView() {
        indicator.showWithCondition(rvSize > 1)
        indicator.setIndicator(rvSize)

        if (rvSize == 1)
            carousel.updateLayoutParams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
    }

    private fun handleAutoScroll() {
        if (rvSize <= 1) return
        job?.cancel()
        job = scope.launchCatchError(block = {
            var count = 0
            repeat(Int.MAX_VALUE) {
                if (isActive) {
                    delay(AUTO_SCROLL_DELAY)
                    count = (count + 1) % rvSize
                    carousel.smoothScrollToPosition(count)
                }
            }
        }, onError = {})
    }

    private fun stopAutoScroll() {
        if (job?.isActive == true) {
            job?.cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        stopAutoScroll()
        carousel.removeOnScrollListener(scrollListener)
    }

    interface Listener {
        fun onWidgetGameEnded(
            view: EngagementCarouselViewComponent,
            engagement: EngagementUiModel.Game
        )

        fun onWidgetClicked(view: EngagementCarouselViewComponent, engagement: EngagementUiModel)
    }

    companion object {
        private const val AUTO_SCROLL_DELAY = 5000L
        private const val STOP_SCROLL_TIME = 15L
    }
}
