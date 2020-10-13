package com.tokopedia.play.widget.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 08/10/20
 */
class PlayWidgetView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mListener: PlayWidgetListener? = null

    override fun onViewRemoved(child: View?) {
        when (child) {
            is PlayWidgetSmallView -> child.setListener(null)
        }
        super.onViewRemoved(child)
    }

    fun setModel(model: PlayWidgetUiModel, isFromAutoRefresh: Boolean = false) {
        when (model) {
            is PlayWidgetUiModel.Small -> addSmallView(model, isFromAutoRefresh)
            is PlayWidgetUiModel.Medium -> addMediumView(model)
            PlayWidgetUiModel.Placeholder -> addPlaceholderView()
        }
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
    }

    private fun addSmallView(model: PlayWidgetUiModel.Small, isFromAutoRefresh: Boolean) {
        val firstChild = getFirstChild()
        if (firstChild is PlayWidgetSmallView) {
            if (isFromAutoRefresh) firstChild.setData(model)
            return
        }

        removeCurrentView()
        val smallWidget = PlayWidgetSmallView(context).apply {
            layoutParams = getChildLayoutParams()
        }
        smallWidget.setListener(mListener)
        addView(smallWidget)
        smallWidget.setData(model)
    }

    private fun addMediumView(model: PlayWidgetUiModel.Medium) {
        if (getFirstChild() is PlayWidgetMediumView) return
        removeCurrentView()
        val mediumWidget = PlayWidgetMediumView(context).apply {
            layoutParams = getChildLayoutParams()
        }
        addView(mediumWidget)
        mediumWidget.setData(model)
    }

    private fun addPlaceholderView() {
        if (getFirstChild() is PlayWidgetPlaceholderView) return
        removeCurrentView()
        val placeholderWidget = PlayWidgetPlaceholderView(context).apply {
            layoutParams = getChildLayoutParams()
        }
        addView(placeholderWidget)
        placeholderWidget.setData()
    }

    private fun getFirstChild() = getChildAt(0)

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getChildLayoutParams() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}