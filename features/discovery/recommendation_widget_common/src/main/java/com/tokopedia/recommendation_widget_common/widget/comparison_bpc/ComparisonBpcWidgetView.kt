package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.recommendation_widget_common.databinding.ViewComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.global.*
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by frenzel on 27/03/23
 */
class ComparisonBpcWidgetView : BaseRecomWidgetView<RecomComparisonBpcModel>, CoroutineScope {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        val LAYOUT = com.tokopedia.recommendation_widget_common.R.layout.view_comparison_bpc_widget
    }

    init {
        binding = ViewComparisonBpcWidgetBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO
    private var binding: ViewComparisonBpcWidgetBinding? = null

    override fun bind(model: RecomComparisonBpcModel) {
    }
}
