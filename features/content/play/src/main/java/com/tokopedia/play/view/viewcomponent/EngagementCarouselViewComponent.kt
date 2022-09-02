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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementCarouselViewComponent(
    container: ViewGroup,
    @IdRes resId: Int,
    private val listener: Listener,
    private val scope: CoroutineScope
) : ViewComponent(container, resId) {

    private val carousel : RecyclerView = findViewById(R.id.rv_engagement_widget)

    private val carouselAdapter = EngagementWidgetAdapter(object : EngagementWidgetViewHolder.Listener{
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

    init {
        carousel.apply {
            adapter = carouselAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

        snapHelper.attachToRecyclerView(carousel)

        autoScroll()
    }

    fun setData(list: List<EngagementUiModel>){
        carouselAdapter.setItemsAndAnimateChanges(list)
    }

    private fun autoScroll(){
        scope.launch {
            //should be list size
            repeat(2){
                delay(5000L)
                carousel.snapScrollTo(it)
            }
        }
    }

    private fun RecyclerView.snapScrollTo (position: Int){
        try {
            this.scrollToPosition(position)
            this.post {
                layoutManager?.findViewByPosition(position)
                rootView?.let {
                    val snapDistance =
                        layoutManager?.let { lM -> snapHelper.calculateDistanceToFinalSnap(lM, it) }
                    if (snapDistance?.get(0) ?: 0 != 0 || snapDistance?.get(1) ?: 0 != 0) {
                        this.scrollBy(snapDistance?.get(0) ?: 0, snapDistance?.get(1) ?: 0)
                    }
                }
            }
        } catch (e: Exception){
            this.smoothScrollToPosition(position)
        }
    }

    interface Listener {
        fun onWidgetGameEnded(view: EngagementCarouselViewComponent, engagement: EngagementUiModel.Game)
        fun onWidgetClicked(view: EngagementCarouselViewComponent, engagement: EngagementUiModel)
    }
}