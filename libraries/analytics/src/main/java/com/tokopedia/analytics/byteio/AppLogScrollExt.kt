package com.tokopedia.analytics.byteio

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.analytics.byteio.AppLogAnalytics.addEnterFrom
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.analytics.byteio.recommendation.zeroAsEmpty
import com.tokopedia.analytics.byteio.util.underscoredParam
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import org.json.JSONObject

/**
 * Model for tiktokec_glide_page
 * Use this to track vertical scrolling on page level
 */
data class GlidePageTrackObject(
    val listName: String = "",
    val listNum: Int = -1,
    val isUseCache: Boolean = false,
    val distanceToTop: Int = 0,
)

/**
 * Model for tiktokec_slide_bar
 * USe this to track horizontal scrolling (ex: carousel)
 */
data class SlideTrackObject(
    val moduleName: String = "",
    val barName: String = "",
    val shopId: String = "",
    val additionalParams: AppLogAdditionalParam = AppLogAdditionalParam.None,
)

/**
 * Model for tiktokec_rec_trigger
 * Use this to track vertical scrolling on recommendation section (usually infinite)
 */
data class RecommendationTriggerObject(
    val sessionId: String = "",
    val requestId: String = "",
    val moduleName: String = "",
    val listName: String = "",
    val listNum: Int = -1,
    val additionalParam: AppLogAdditionalParam = AppLogAdditionalParam.None,
)

class VerticalTrackScrollListener(
    private val glidePageTrackCallback: () -> GlidePageTrackObject?,
) : RecyclerView.OnScrollListener() {
    // total scroll is total offset for multiple glide.
    // for ex. User glide 3 times repeatly, means total scroll = total 3 glides.
    private var totalScroll = 0F

    // drag scroll is offset for each glide.
    private var dragScroll = 0F

    // drag is started from recom section or not, to determine should send rec_trigger or not
    private var dragFromRecom = false

    private var recommendationTriggerObject = RecommendationTriggerObject()

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
        val visibleItemPositionRange = layoutManager?.getVisibleItemPositionRange() ?: return false

        loop@ for(i in visibleItemPositionRange.first .. visibleItemPositionRange.second) {
            val viewHolder = findViewHolderForAdapterPosition(i) ?: break@loop
            (viewHolder as? AppLogRecTriggerInterface)?.let {
                if(it.isEligibleToTrack()) {
                    recommendationTriggerObject = it.getRecommendationTriggerObject() ?: return true
                    return true
                }
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
        sendGlideRecommendationTrack(totalScroll, recommendationTriggerObject)
    }

    private fun sendGlidePage(scrollOffset: Float) {
        val glidePageTrackObject = glidePageTrackCallback.invoke() ?: return
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
        it.addEnterFrom()
        it.put(AppLogParam.GLIDE_TYPE, if (scrollOffset > 0) "more" else "less")
        it.put(AppLogParam.GLIDE_DISTANCE, scrollOffset)
        it.put(AppLogParam.DISTANCE_TO_TOP, model.distanceToTop)
        it.put(AppLogParam.LIST_NAME, model.listName.underscoredParam())
        it.put(AppLogParam.LIST_NUM, model.listNum.inc().zeroAsEmpty())
        it.put(AppLogParam.IS_USE_CACHE, if (model.isUseCache) 1 else 0)
    })
}

fun sendGlideRecommendationTrack(scrollOffset: Float, model: RecommendationTriggerObject) {
    val additionalParams = model.additionalParam.parameters
    AppLogAnalytics.send(EventName.REC_TRIGGER, JSONObject(additionalParams).also {
        it.addPage()
        it.addEnterFrom()
        it.put(AppLogParam.LIST_NAME, model.listName.underscoredParam())
        it.put(AppLogParam.LIST_NUM, model.listNum.inc().zeroAsEmpty())
        it.put(AppLogParam.ACTION_TYPE, ActionType.GLIDE)
        it.put(AppLogParam.MODULE_NAME, model.moduleName)
        it.put(AppLogParam.GLIDE_DISTANCE, scrollOffset)
        it.put(AppLogParam.REC_SESSION_ID, model.sessionId)
        it.put(AppLogParam.REQUEST_ID, model.requestId)
    })
}

fun sendHorizontalSlideTrack(scrollOffset: Float, model: SlideTrackObject) {
    val additionalParams = model.additionalParams.parameters
    AppLogAnalytics.send(EventName.SLIDE_BAR, JSONObject(additionalParams).also {
        it.addPage()
        it.addEnterFrom()
        it.put(AppLogParam.SLIDE_TYPE, if (scrollOffset > 0) "show_right" else "show_left")

        it.put(AppLogParam.MODULE_NAME, model.moduleName)
        it.put(AppLogParam.BAR_NAME, model.barName)
        it.put(AppLogParam.SHOP_ID, model.shopId.zeroAsEmpty())
    })
}

/**
 * event: tiktokec_rec_trigger (recommendation) & tiktokec_glide_page (page level)
 * Listener must be passed at beginning, no need to wait for data.
 * For pages that needs to track glide page (page level), need to pass GlidePageTrackObject as lambda,
 * which will be invoked every scroll stops
 */
fun RecyclerView.addVerticalTrackListener(
    glidePageTrackCallback: () -> GlidePageTrackObject? = { null },
) {
    addOneTimeGlobalLayoutListener {
        this.addOnScrollListener(VerticalTrackScrollListener(glidePageTrackCallback))
    }
}

/**
 * event: tiktokec_slide_bar
 * Add track listener after success fetching data from BE.
 */
fun RecyclerView.addHorizontalTrackListener(slideTrackObject: SlideTrackObject = SlideTrackObject()) {
    addOneTimeGlobalLayoutListener {
        this.addOnScrollListener(HorizontalTrackScrollListener(slideTrackObject))
    }
}
