package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.ui.engagement.adapter.EngagementWidgetAdapter
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.ui.engagement.viewholder.EngagementWidgetViewHolder
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.viewcomponent.ViewComponent
import kotlinx.coroutines.CoroutineScope

/**
 * @author by astidhiyaa on 24/08/22
 */
class EngagementCarouselViewComponent(
    container: ViewGroup,
    @IdRes resId: Int,
    private val listener: Listener,
    private val scope: CoroutineScope
) : ViewComponent(container, resId) {

    private val carousel : RecyclerView = (rootView as RecyclerView)

    private val carouselAdapter = EngagementWidgetAdapter(object : EngagementWidgetViewHolder.Listener{
        override fun onGiveawayEnd(giveaway: InteractiveUiModel.Giveaway) {
            //TODO("Not yet implemented")
        }

        override fun onGiveawayUpcomingEnd(giveaway: InteractiveUiModel.Giveaway) {
            //TODO("Not yet implemented")
        }

        override fun onQuizEnd(quiz: InteractiveUiModel.Quiz) {
            //TODO("Not yet implemented")
        }
    })

    init {
        carousel.apply {
            adapter = carouselAdapter
        }
    }

    fun setData(list: List<EngagementUiModel>){
        carouselAdapter.setItemsAndAnimateChanges(list)
    }

    interface Listener {

    }
}