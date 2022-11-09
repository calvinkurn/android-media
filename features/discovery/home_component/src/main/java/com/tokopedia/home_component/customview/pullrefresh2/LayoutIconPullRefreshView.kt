package com.tokopedia.home_component.customview.pullrefresh2

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home_component.R
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toDp
import kotlin.math.roundToInt

/**
 * Created by dhaba
 */
class LayoutIconPullRefreshView : ConstraintLayout, LayoutIconPullRefreshListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var containerIconPullRefresh: ConstraintLayout? = null
    private var contentChildView: SimpleSwipeRefreshLayout.ChildView? = null
    private var loaderPullRefresh: LoaderUnify? = null
    private var maxOffset : Int = 0
    private var progressRefresh : Float = 0.0f
    private var offsetY : Float = 0.0f
    private var pullRefreshIcon: ProgressBar? = null
    companion object {
        private const val MAXIMUM_HEIGHT_SCROLL = 120
    }

    init {
        val view = View.inflate(context, R.layout.layout_icon_pull_refresh_view, this)
        containerIconPullRefresh = view.findViewById(R.id.container_icon_pull_refresh)
        pullRefreshIcon = view.findViewById(R.id.progress_pull_refresh)
        containerIconPullRefresh?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        contentChildView = SimpleSwipeRefreshLayout.ChildView(getChildAt(0))
        loaderPullRefresh = view.findViewById(R.id.loader_pull_refresh)
//        progressPullRefresh = view.findViewById(R.id.progress_pull_refresh)
    }

    override fun maxOffsetTop(maxOffsetTop: Int) {
        maxOffset = maxOffsetTop
    }

    override fun offsetView(offset: Float) {
        offsetY = offset
        positionChildren()
        loaderPullRefresh?.gone()
        pullRefreshIcon?.show()
//        Log.d("dhabalog", "offsetIcon $offset")
    }

    override fun startRefreshing() {
        Log.d("dhabalog", "startRefreshing")
        containerIconPullRefresh?.visible()

        val layoutParams = containerIconPullRefresh?.layoutParams
        layoutParams?.height = 64
        containerIconPullRefresh?.layoutParams = layoutParams
        loaderPullRefresh?.visible()
        pullRefreshIcon?.gone()
    }

    override fun stopRefreshing(isAfterRefresh: Boolean) {
        Log.d("dhabalog", "stopRefreshing")
        if ((offsetY < maxOffset && progressRefresh < 1) || isAfterRefresh) {
            val layoutParams = containerIconPullRefresh?.layoutParams
            layoutParams?.height = 1
            containerIconPullRefresh?.layoutParams = layoutParams
            containerIconPullRefresh?.gone()
        }
    }

    override fun progressRefresh(progress: Float) {
        progressRefresh = progress
        val p = (progress * 100).roundToInt()
        pullRefreshIcon?.progress = p
        Log.d("dhabalog", "progress $p")
    }

    private fun positionChildren() {

        if (offsetY > 0) {
//            contentChildView?.view?.visible()
//            val layoutHeight = contentChildView?.positionAttr?.top?.plus(offset)!!
//            Log.d("dhabalog", "layoutHeight $layoutHeight")
//            contentChildView?.view?.y = offset // contentChildView?.positionAttr?.top?.plus(offset)!!

            val heightLayoutScroll = ((offsetY / maxOffset) * MAXIMUM_HEIGHT_SCROLL).roundToInt()
            Log.d("dhabalog", "heightLayoutScroll $heightLayoutScroll")

            containerIconPullRefresh?.visible()
            val layoutParams = containerIconPullRefresh?.layoutParams
            layoutParams?.height = heightLayoutScroll
            containerIconPullRefresh?.layoutParams = layoutParams
        } else {
//            containerIconPullRefresh?.gone()
        }

//        contentChildViewPullRefresh?.let {
//            if (!overlay) {
//                it.view.y = it.positionAttr.top + offset
//            }
//        }
    }
}
