package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.ui.engagement.adapter.EngagementWidgetAdapter
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.CoroutineScope
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

    private val carousel : RecyclerView = findViewById(R.id.rv_engagement_widget)

    private val carouselAdapter = EngagementWidgetAdapter(object : EngagementWidgetViewHolder.Listener{
        override fun onWidgetGameEnded(engagement: EngagementUiModel.Game) {
            listener.onWidgetGameEnded(this@EngagementCarouselViewComponent, engagement)
        }

        override fun onWidgetClicked(engagement: EngagementUiModel) {
            listener.onWidgetClicked(this@EngagementCarouselViewComponent, engagement)
        }
    })

    init {
        carousel.apply {
            adapter = carouselAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
        autoScroll()
    }

    fun setData(list: List<EngagementUiModel>){
        carouselAdapter.setItemsAndAnimateChanges(list)
    }

    private fun autoScroll(){
        scope.launch {
            //should be list size
            repeat(carouselAdapter.itemCount){
                delay(5000L)
                carousel.smoothScrollToPosition(it)
            }
        }
    }

    interface Listener {
        fun onWidgetGameEnded(view: EngagementCarouselViewComponent, engagement: EngagementUiModel.Game)
        fun onWidgetClicked(view: EngagementCarouselViewComponent, engagement: EngagementUiModel)
    }
}