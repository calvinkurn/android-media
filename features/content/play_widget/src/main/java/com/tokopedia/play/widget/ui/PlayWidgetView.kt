package com.tokopedia.play.widget.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
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


    fun setModel(model: PlayWidgetUiModel) {
        when (model) {
            is PlayWidgetUiModel.Small -> addSmallView()
            is PlayWidgetUiModel.Medium -> addMediumView()
        }
    }

    private fun addSmallView() {
        if (getFirstChild() is PlayWidgetSmallView) return
        removeCurrentView()
        addView(PlayWidgetSmallView(context).apply {
            layoutParams = getChildLayoutParams()
        })
    }

    private fun addMediumView() {
        if (getFirstChild() is PlayWidgetMediumView) return
        removeCurrentView()
        addView(PlayWidgetMediumView(context).apply {
            layoutParams = getChildLayoutParams()
        })
    }

    private fun getFirstChild() = getChildAt(0)

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getChildLayoutParams() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}