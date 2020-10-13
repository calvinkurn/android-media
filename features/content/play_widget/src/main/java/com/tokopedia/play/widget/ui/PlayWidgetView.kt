package com.tokopedia.play.widget.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

/**
 * Created by jegul on 08/10/20
 */
class PlayWidgetView : LinearLayout, LifecycleObserver {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onViewRemoved(child: View?) {
        when (child) {
            is PlayWidgetSmallView -> child.setListener(null)
        }
        super.onViewRemoved(child)
    }

    fun setModel(model: PlayWidgetUiModel) {
        when (model) {
            is PlayWidgetUiModel.Small -> addSmallView(model)
            is PlayWidgetUiModel.Medium -> addMediumView(model)
            PlayWidgetUiModel.Placeholder -> addPlaceholderView()
        }
    }

    private fun addSmallView(model: PlayWidgetUiModel.Small) {
        val firstChild = getFirstChild()
        val widgetView = if (firstChild !is PlayWidgetSmallView) {
            removeCurrentView()
            val smallWidget = PlayWidgetSmallView(context).apply {
                layoutParams = getChildLayoutParams()
            }
            addView(smallWidget)

            smallWidget
        } else firstChild

        widgetView.setData(model)
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
        val firstChild = getFirstChild()
        val widgetView = if (firstChild !is PlayWidgetPlaceholderView) {
            removeCurrentView()
            val placeholderWidget = PlayWidgetPlaceholderView(context).apply {
                layoutParams = getChildLayoutParams()
            }
            addView(placeholderWidget)

            placeholderWidget
        } else firstChild

        widgetView.setData()
    }

    private fun getFirstChild(): View? = getChildAt(0)

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getChildLayoutParams() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}