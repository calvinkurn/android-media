package com.tokopedia.analytics.byteio

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import org.json.JSONObject

data class GlideTrackObject(
    val listName: String,
    val listNum: Int,
    val isUseCache: Boolean,
    val recommendedTriggerObject: RecommendedTriggerObject? = null
)

data class SlideTrackObject(
    val moduleName: String,
    val barName: String
)

data class RecommendedTriggerObject(
    val sessionId: String,
    val requestId: String,
    val moduleName: String,
    val isUnderGuide: Boolean = false
)

class VerticalTrackScrollListener(private val glideTrackObject: GlideTrackObject) :
    RecyclerView.OnScrollListener() {
    // total scroll is total offset for multiple glide.
    // for ex. User glide 3 times repeatly, means total scroll = total 3 glides.
    private var totalScroll = 0F

    // drag scroll is offset for each glide.
    private var dragScroll = 0F

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        dragScroll += dy
        super.onScrolled(recyclerView, dx, dy)
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val isStartDrag = newState == SCROLL_STATE_DRAGGING
        val isIdle = newState == SCROLL_STATE_IDLE
        val hasDragScroll = dragScroll != 0F
        // we do not track if start position == end position,
        // for example user scroll up when there is no up view
        if (hasDragScroll) {
            // idle means scrolling is stopped.
            if (isIdle) {
                // send this offset position.
                totalScroll += dragScroll
                sendScrollDrag(dragScroll)
                sendScrollEnd(totalScroll)
                totalScroll = 0F
                dragScroll = 0F
            } else if (isStartDrag) {
                // this line will occur when user trigger glide
                // when the recyclerview is still moving
                // this will send previous scroll.
                totalScroll += dragScroll
                sendScrollDrag(dragScroll)
                dragScroll = 0F
            }
        }

        super.onScrollStateChanged(recyclerView, newState)
    }

    private fun sendScrollEnd(totalScroll: Float) {
        sendVerticalScrollEnd(totalScroll, glideTrackObject)
    }

    private fun sendScrollDrag(scrollOffset: Float) {
        sendVerticalScrollTrack(totalScroll, glideTrackObject)
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
        sendHorizontalScrollTrack(scrollOffset, slideTrackObject)
    }
}

// https://bytedance.sg.larkoffice.com/docx/MSiydFty1o0xIYxUe4LltuRHgue
fun sendVerticalScrollTrack(scrollOffset: Float, glideTrackObject: GlideTrackObject) {
    AppLogAnalytics.send(EventName.GLIDE_PAGE, JSONObject().also {
        it.addPage()
        it.put(AppLogParam.ENTER_FROM, "") //TODO
        it.put(AppLogParam.GLIDE_TYPE, if (scrollOffset > 0) "more" else "less")
        it.put(AppLogParam.GLIDE_DISTANCE, scrollOffset)

        it.put(AppLogParam.LIST_NAME, glideTrackObject.listName)
        it.put(AppLogParam.LIST_NUM, glideTrackObject.listNum)
        it.put(AppLogParam.IS_USE_CACHE, glideTrackObject.isUseCache)
    })
}

fun sendVerticalScrollEnd(scrollOffset: Float, glideTrackObject: GlideTrackObject) {
    val recObject = glideTrackObject.recommendedTriggerObject ?: return
    AppLogAnalytics.send(EventName.REC_TRIGGER, JSONObject().also {
        it.addPage()
        it.put(AppLogParam.ENTER_FROM, "") //TODO
        it.put(AppLogParam.GLIDE_TYPE, if (scrollOffset > 0) "more" else "less")
        it.put(AppLogParam.GLIDE_DISTANCE, scrollOffset)

        it.put(AppLogParam.LIST_NAME, glideTrackObject.listName)
        it.put(AppLogParam.LIST_NUM, glideTrackObject.listNum)

        it.put(AppLogParam.ENTER_FROM, "") // TODO
        it.put(AppLogParam.ACTION_TYPE, ActionType.GLIDE)
        it.put(AppLogParam.MODULE_NAME, "") //TODO
        it.put(AppLogParam.REC_SESSION_ID, recObject.sessionId)
        it.put(AppLogParam.REQUEST_ID, recObject.requestId)
    })
}

fun sendHorizontalScrollTrack(scrollOffset: Float, slideTrackObject: SlideTrackObject) {
    AppLogAnalytics.send(EventName.SLIDE_BAR, JSONObject().also {
        it.addPage()
        it.put(AppLogParam.ENTER_FROM, "") //TODO
        it.put(AppLogParam.SLIDE_TYPE, if (scrollOffset > 0) "show_right" else "show_left")

        it.put(AppLogParam.MODULE_NAME, slideTrackObject.moduleName)
        it.put(AppLogParam.BAR_NAME, slideTrackObject.barName)
    })
}

/**
 * Add track listener after success fetching data from BE.
 * If we are sure this is not recommendation product, leave recommendedTriggerObject as null
 */
fun RecyclerView.addVerticalTrackListener(glideTrackObject: GlideTrackObject) {
    this.addOnScrollListener(VerticalTrackScrollListener(glideTrackObject))
}

/**
 * Add track listener after success fetching data from BE.
 */
fun RecyclerView.addHorizontalTrackListener(slideTrackObject: SlideTrackObject) {
    this.addOnScrollListener(HorizontalTrackScrollListener(slideTrackObject))
}