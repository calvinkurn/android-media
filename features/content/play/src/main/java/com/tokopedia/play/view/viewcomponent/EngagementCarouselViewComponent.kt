package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.ui.engagement.adapter.EngagementWidgetAdapter
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        })

    private val snapHelper by lazy(LazyThreadSafetyMode.NONE) {
        PagerSnapHelper()
    }

    private val linearLayoutManager =
        LinearLayoutManager(rootView.context, LinearLayoutManager.VERTICAL, false)

    init {
        carousel.apply {
            adapter = carouselAdapter
            layoutManager = linearLayoutManager
        }

        snapHelper.attachToRecyclerView(carousel)
    }

    fun setData(list: List<EngagementUiModel>) {
        carouselAdapter.setItemsAndAnimateChanges(list)
    }

    fun startAutoScroll(size: Int){
        job = scope.launch {
            repeat(size) {
                delay(AUTO_SCROLL_DELAY)
                carousel.smoothScrollToPosition(it)
            }
        }
    }

    fun stopAutoScroll(){
        job?.cancel()
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
    }
}