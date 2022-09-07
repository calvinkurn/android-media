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
import com.tokopedia.play.R
import com.tokopedia.play.ui.engagement.adapter.EngagementWidgetAdapter
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private val linearLayoutManager =
        LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)

    private val size: Int
        get() = carouselAdapter.itemCount

    init {
        carousel.apply {
            adapter = carouselAdapter
            layoutManager = linearLayoutManager
        }

        snapHelper.attachToRecyclerView(carousel)
    }

    fun setData(list: List<EngagementUiModel>) {
        carouselAdapter.setItemsAndAnimateChanges(list)
        startAutoScroll()
        resize()
    }

    private fun resize() {
        if (size == 1)
            carousel.updateLayoutParams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
    }

    private fun startAutoScroll() {
        if (size <= 1) return
        job?.cancel()
        job = scope.launchCatchError(block = {
            var count = 0
            repeat(Int.MAX_VALUE) {
                delay(AUTO_SCROLL_DELAY)
                when {
                    count == size - 1 -> count = 0
                    count >= 0 -> count++
                    else -> count--
                }
                carousel.smoothScrollToPosition(count)
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