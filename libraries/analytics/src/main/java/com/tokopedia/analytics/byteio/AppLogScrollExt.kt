package com.tokopedia.analytics.byteio

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import org.json.JSONObject

/**
 * Model for tiktokec_glide_page
 * Use this to track vertical scrolling on page level
 */
data class GlidePageTrackObject(
    val listName: String = "",
    val listNum: Int = 0,
    val isUseCache: Boolean = false,
)

/**
 * Model for tiktokec_slide_bar
 * USe this to track horizontal scrolling (ex: carousel)
 */
data class SlideTrackObject(
    val moduleName: String,
    val barName: String,
    val shopId: String,
)

/**
 * Model for tiktokec_rec_trigger
 * Use this to track vertical scrolling on recommendation section (usually infinite)
 */
data class RecommendationTriggerObject(
    val sessionId: String = "",
    val requestId: String = "",
    val moduleName: String = "",
    val isUnderGuide: Boolean = false,
    val listName: String = "",
    val listNum: Int = 0,
    val viewHolders: List<Class<*>> = emptyList(),
) {
    val viewHolderMap: Map<Class<*>, Boolean> = viewHolders.associateWith { true }
}

class VerticalTrackScrollListener(
    private val glidePageTrackObject: GlidePageTrackObject?,
    private val recommendationTriggerObject: RecommendationTriggerObject?
) : RecyclerView.OnScrollListener() {
    // total scroll is total offset for multiple glide.
    // for ex. User glide 3 times repeatly, means total scroll = total 3 glides.
    private var totalScroll = 0F

    // drag scroll is offset for each glide.
    private var dragScroll = 0F

    // drag is started from recom section or not, to determine should send rec_trigger or not
    private var dragFromRecom = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        dragScroll += dy
        super.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val isStartDrag = newState == SCROLL_STATE_DRAGGING
        val isIdle = newState == SCROLL_STATE_IDLE
        val hasDragScroll = dragScroll != 0F

        // when user start dragging, rec_trigger is not sent yet,
        // but mark dragFromRecom flag to determine whether to send the event or not (when idle)
        if(isStartDrag) {
            dragFromRecom = recyclerView.shouldTriggerGlideRecommendation()
        }

        // we do not track if start position == end position,
        // for example user scroll up when there is no up view
        if (hasDragScroll) {
            // idle means scrolling is stopped.
            if (isIdle) {
                // send this offset position.
                totalScroll += dragScroll
                sendGlidePage(dragScroll)
                // Possible scenarios to send rec_trigger:
                // 1. Drag from recom, stop outside recom (dragFromRecom = true, shouldTriggerGlideRecommendation = false)
                // 2. Drag from outside recom, stop on recom (dragFromRecom = false, shouldTriggerGlideRecommendation = true)
                // 3. Drag and stop on recom (dragFromRecom = true, shouldTriggerGlideRecommendation = true)
                if(dragFromRecom || recyclerView.shouldTriggerGlideRecommendation()) {
                    sendGlideRecommendation(totalScroll)
                }
                totalScroll = 0F
                dragScroll = 0F
            } else if (isStartDrag) {
                // this line will occur when user trigger glide
                // when the recyclerview is still moving
                // this will send previous scroll.
                totalScroll += dragScroll
                sendGlidePage(dragScroll)
                dragScroll = 0F
            }
        }

        super.onScrollStateChanged(recyclerView, newState)
    }

    private fun RecyclerView.shouldTriggerGlideRecommendation(): Boolean {
        recommendationTriggerObject ?: return false
        if(recommendationTriggerObject.viewHolders.isEmpty()) return true

        val visibleItemPositionRange = layoutManager?.getVisibleItemPositionRange() ?: return false

        loop@ for(i in visibleItemPositionRange.first .. visibleItemPositionRange.second) {
            val viewHolder = findViewHolderForAdapterPosition(i) ?: break@loop
            if(recommendationTriggerObject.viewHolderMap[viewHolder.javaClass] == true) {
                return true
            }
        }
        return false
    }

    private fun LayoutManager.getVisibleItemPositionRange(): Pair<Int, Int>? {
        val firstVisible = when(this) {
            is LinearLayoutManager -> findFirstVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val firstVisibleItemPositions = IntArray(spanCount)
                findFirstVisibleItemPositions(firstVisibleItemPositions)
                firstVisibleItemPositions.min()
            }
            else -> RecyclerView.NO_POSITION
        }

        val lastVisible = when(this) {
            is LinearLayoutManager -> findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = IntArray(spanCount)
                findLastVisibleItemPositions(lastVisibleItemPositions)
                lastVisibleItemPositions.max()
            }
            else -> RecyclerView.NO_POSITION
        }

        return if(firstVisible != RecyclerView.NO_POSITION && lastVisible != RecyclerView.NO_POSITION) {
            firstVisible to lastVisible
        } else null
    }

    private fun sendGlideRecommendation(totalScroll: Float) {
        recommendationTriggerObject ?: return
        sendGlideRecommendationTrack(totalScroll, recommendationTriggerObject)
    }

    private fun sendGlidePage(scrollOffset: Float) {
        glidePageTrackObject ?: return
        sendGlidePageTrack(scrollOffset, glidePageTrackObject)
    }
}

class HorizontalTrackScrollListener(private val slideTrackObject: SlideTrackObject) :
    RecyclerView.OnScrollListener() {
    // drag scroll is offset for each glide.
    private var dragScroll = 0F

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        dragScroll += dx
        super.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val isStartDrag = newState == SCROLL_STATE_DRAGGING
        val isIdle = newState == SCROLL_STATE_IDLE
        val hasDragScroll = dragScroll != 0F
        // we do not track if start position == end position,
        // for example user scroll up when there is no up view
        if (hasDragScroll && (isIdle || isStartDrag)) {
            sendScrollDrag(dragScroll)
            dragScroll = 0F
        }

        super.onScrollStateChanged(recyclerView, newState)
    }

    private fun sendScrollDrag(scrollOffset: Float) {
        sendHorizontalSlideTrack(scrollOffset, slideTrackObject)
    }
}

// https://bytedance.sg.larkoffice.com/docx/MSiydFty1o0xIYxUe4LltuRHgue
fun sendGlidePageTrack(scrollOffset: Float, model: GlidePageTrackObject) {
    AppLogAnalytics.send(EventName.GLIDE_PAGE, JSONObject().also {
        it.addPage()
        it.put(AppLogParam.ENTER_FROM, "") //TODO
        it.put(AppLogParam.GLIDE_TYPE, if (scrollOffset > 0) "more" else "less")
        it.put(AppLogParam.GLIDE_DISTANCE, scrollOffset)

        it.put(AppLogParam.LIST_NAME, model.listName)
        it.put(AppLogParam.LIST_NUM, model.listNum)
        it.put(AppLogParam.IS_USE_CACHE, if (model.isUseCache) 1 else 0)
    })
}

fun sendGlideRecommendationTrack(scrollOffset: Float, model: RecommendationTriggerObject) {
    AppLogAnalytics.send(EventName.REC_TRIGGER, JSONObject().also {
        it.addPage()
        it.put(AppLogParam.ENTER_FROM, "") //TODO
        it.put(AppLogParam.GLIDE_TYPE, if (scrollOffset > 0) "more" else "less")
        it.put(AppLogParam.GLIDE_DISTANCE, scrollOffset)

        it.put(AppLogParam.LIST_NAME, model.listName)
        it.put(AppLogParam.LIST_NUM, model.listNum)

        it.put(AppLogParam.ACTION_TYPE, ActionType.GLIDE)
        it.put(AppLogParam.MODULE_NAME, model.moduleName)
        it.put(AppLogParam.REC_SESSION_ID, model.sessionId)
        it.put(AppLogParam.REQUEST_ID, model.requestId)
    })
}

fun sendHorizontalSlideTrack(scrollOffset: Float, model: SlideTrackObject) {
    AppLogAnalytics.send(EventName.SLIDE_BAR, JSONObject().also {
        it.addPage()
        it.put(AppLogParam.ENTER_FROM, "") //TODO
        it.put(AppLogParam.SLIDE_TYPE, if (scrollOffset > 0) "show_right" else "show_left")

        it.put(AppLogParam.MODULE_NAME, model.moduleName)
        it.put(AppLogParam.BAR_NAME, model.barName)
        it.put(AppLogParam.SHOP_ID, model.shopId)
    })
}

/**
 * event: tiktokec_rec_trigger & tiktokec_glide_page
 * Add track listener after success fetching data from BE.
 * If no recommendation on the page, leave recommendedTriggerObject as null
 */
fun RecyclerView.addVerticalTrackListener(
    glidePageTrackObject: GlidePageTrackObject? = null,
    recommendationTriggerObject: RecommendationTriggerObject? = null
) {
    this.addOnScrollListener(VerticalTrackScrollListener(glidePageTrackObject, recommendationTriggerObject))
}

/**
 * event: tiktokec_slide_bar
 * Add track listener after success fetching data from BE.
 */
fun RecyclerView.addHorizontalTrackListener(slideTrackObject: SlideTrackObject) {
    this.addOnScrollListener(HorizontalTrackScrollListener(slideTrackObject))
}
