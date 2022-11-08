package com.tokopedia.home_component.customview.pullrefresh2

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.home_component.R

/**
 * Created by dhaba
 */
class LayoutIconPullRefreshView : ConstraintLayout, LayoutIconPullRefreshListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        val view = View.inflate(context, R.layout.layout_icon_pull_refresh_view, this)
    }

    override fun offsetView(offset: Float) {
        Log.d("dhabalog", "offsetIcon $offset")
    }

    override fun startRefreshing() {
        Log.d("dhabalog", "startRefreshing")
    }

    override fun stopRefreshing() {
        Log.d("dhabalog", "stopRefreshing")
    }

    override fun progressRefresh(progress: Float) {
        Log.d("dhabalog", "progress $progress")
    }
}
