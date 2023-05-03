package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.di.recomwidget.DaggerRecommendationComponent
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationComponent
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import java.lang.Exception

/**
 * Created by frenzel on 11/03/23
 */
class RecommendationWidgetView : LinearLayout, LifecycleEventObserver {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val typeFactory = RecommendationTypeFactoryImpl()
    private var component: RecommendationComponent? = null

    fun bind(model: RecommendationVisitable) {
        initInjector()
        val widget = typeFactory.createView(context, model)
        val widgetView = addWidgetView(widget) ?: return
        widgetView.bind(model)
    }

    private fun addWidgetView(widgetView: BaseRecommendationWidgetView<*>): BaseRecommendationWidgetView<RecommendationVisitable>? {
        return try {
            removeAllViews()
            val widget = widgetView.apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }
            addView(widget)

            widget
        } catch (e: Exception) {
            null
        } as? BaseRecommendationWidgetView<RecommendationVisitable>
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

    private fun initInjector() {
        if (component == null) {
            val appContext = context.getActivityFromContext()?.application as BaseMainApplication
            component = DaggerRecommendationComponent.builder().baseAppComponent((appContext).baseAppComponent).build()
            component?.inject(appContext)
        }
    }
}
