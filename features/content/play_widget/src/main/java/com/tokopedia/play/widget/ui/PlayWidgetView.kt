package com.tokopedia.play.widget.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 08/10/20
 */
class PlayWidgetView : LinearLayout, LifecycleObserver, IPlayWidgetView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mAnalyticListener: PlayWidgetAnalyticListener? = null
    private var mWidgetListener: PlayWidgetListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    override fun onViewRemoved(child: View?) {
        when (child) {
            is PlayWidgetSmallView -> child.setAnalyticListener(null)
        }
        super.onViewRemoved(child)
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        mWidgetInternalListener = listener
        when (val child = getFirstChild()) {
            is PlayWidgetSmallView -> child.setWidgetInternalListener(listener)
        }
    }

    fun setModel(model: PlayWidgetUiModel) {
        when (model) {
            is PlayWidgetUiModel.Small -> addSmallView(model)
            is PlayWidgetUiModel.Medium -> addMediumView(model)
            PlayWidgetUiModel.Placeholder -> addPlaceholderView()
        }
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
        when (val child = getFirstChild()) {
            is PlayWidgetSmallView -> child.setAnalyticListener(listener)
        }
    }

    fun setWidgetListener(listener: PlayWidgetListener?) {
        mWidgetListener = listener
        when (val child = getFirstChild()) {
            is PlayWidgetSmallView -> child.setWidgetListener(listener)
            is PlayWidgetMediumView -> child.setWidgetListener(listener)
        }
    }

    private fun addSmallView(model: PlayWidgetUiModel.Small) {
        val widgetView = addWidgetView { PlayWidgetSmallView(context) }

        widgetView.setData(model)
        widgetView.setAnalyticListener(mAnalyticListener)
        widgetView.setWidgetListener(mWidgetListener)
        widgetView.setWidgetInternalListener(mWidgetInternalListener)
    }

    private fun addMediumView(model: PlayWidgetUiModel.Medium) {
        val widgetView = addWidgetView { PlayWidgetMediumView(context) }

        widgetView.setData(model)
        widgetView.setWidgetListener(mWidgetListener)
        widgetView.setWidgetInternalListener(mWidgetInternalListener)
    }

    private fun addPlaceholderView() {
        val widgetView = addWidgetView { PlayWidgetPlaceholderView(context) }

        widgetView.setData()
    }

    private inline fun <reified T: View> addWidgetView(creator: () -> T): T {
        val firstChild = getFirstChild()
        return if (firstChild !is T) {
            removeCurrentView()
            val widget = creator().apply {
                layoutParams = getChildLayoutParams()
            }
            addView(widget)

            widget
        } else firstChild
    }

    private fun getFirstChild(): View? = getChildAt(0)

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getChildLayoutParams() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}