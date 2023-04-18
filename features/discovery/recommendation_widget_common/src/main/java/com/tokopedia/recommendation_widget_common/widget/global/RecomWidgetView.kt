package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
        val widget = typeFactory.createView(context, model)
        val widgetView = addWidgetView(widget) ?: return
        widgetView.bind(model)
    }

    private fun addWidgetView(widgetView: BaseRecomWidgetView<*>): BaseRecomWidgetView<RecomVisitable>? {
        return try {
            removeAllViews()
            val widget = widgetView.apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }
            addView(widget)

            widget
        } catch (e: Exception) {
            null
        } as? BaseRecomWidgetView<RecomVisitable>
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
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
