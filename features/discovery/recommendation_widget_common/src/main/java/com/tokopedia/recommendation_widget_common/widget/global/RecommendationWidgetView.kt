package com.tokopedia.recommendation_widget_common.widget.global

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET
import com.tokopedia.recommendation_widget_common.di.recomwidget.DaggerRecommendationComponent
import com.tokopedia.recommendation_widget_common.di.recomwidget.RecommendationComponent
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel
import java.lang.Exception

/**
 * Created by frenzel on 11/03/23
 */
class RecommendationWidgetView : LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val typeFactory = RecommendationTypeFactoryImpl()
    private var component: RecommendationComponent? = null

    fun bind(model: RecommendationWidgetModel) {
        val recommendationWidget = model.widget

        val recommendationVisitable =
            if (recommendationWidget.layoutType == TYPE_COMPARISON_BPC_WIDGET)
                RecommendationComparisonBpcModel.from(
                    model.metadata,
                    model.trackingModel,
                    recommendationWidget,
                )
            else
                RecommendationCarouselModel.from(model.metadata, model.trackingModel)

        bind(recommendationVisitable)
    }

    private fun bind(model: RecommendationVisitable) {
        initInjector()
        val widget = typeFactory.createView(context, model)
        val widgetView = addWidgetView(widget) ?: return
        widgetView.bind(model)
    }

    private fun addWidgetView(widgetView: ViewGroup): IRecommendationWidgetView<RecommendationVisitable>? {
        return try {
            removeAllViews()
            val widget = widgetView.apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }
            addView(widget)

            widget
        } catch (e: Exception) {
            null
        } as? IRecommendationWidgetView<RecommendationVisitable>
    }

    private fun initInjector() {
        if (component == null) {
            val appContext = context.getActivityFromContext()?.application as BaseMainApplication
            component = DaggerRecommendationComponent.builder().baseAppComponent((appContext).baseAppComponent).build()
            component?.inject(appContext)
        }
    }
}
