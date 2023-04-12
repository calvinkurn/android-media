package com.tokopedia.recommendation_widget_common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.recommendation_widget_common.widget.global.*
import java.lang.Exception

/**
 * Created by frenzel on 11/03/23
 */
class RecomWidgetView : LinearLayout, LifecycleEventObserver {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val typeFactory = RecomTypeFactoryImpl()

    fun bind(model: RecomVisitable) {
        val type = model.type(typeFactory)
        val widget = typeFactory.createView(context, type)

        val widgetView = addWidgetView { widget } ?: return
        widgetView.bind(model)
    }

    private inline fun <reified T : View> addWidgetView(creator: () -> T): T? {
        val firstChild = getFirstChild()
        return try {
            if (firstChild !is T) {
                removeCurrentView()
                val widget = creator().apply {
                    layoutParams = getChildLayoutParams()
                }
                addView(widget)

                widget
            } else firstChild
        } catch (e: Exception) {
            null
        }
    }

    private fun getFirstChild(): View? = getChildAt(0)

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getChildLayoutParams() =
        LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event) {
            Lifecycle.Event.ON_CREATE -> { }
            Lifecycle.Event.ON_START -> { }
            Lifecycle.Event.ON_PAUSE -> { }
            Lifecycle.Event.ON_RESUME -> { }
            Lifecycle.Event.ON_STOP -> { }
            Lifecycle.Event.ON_DESTROY -> { }
            else -> { }
        }
    }
}
