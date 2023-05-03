package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.recommendation_widget_common.databinding.ViewComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.ComparisonBpcWidgetAdapter
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcWidgetDecoration
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcWidgetMapper
import com.tokopedia.recommendation_widget_common.widget.global.BaseRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetAnalyticListener
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Created by frenzel on 27/03/23
 */
class ComparisonBpcWidgetView : BaseRecommendationWidgetView<RecommendationComparisonBpcModel>, CoroutineScope, RecommendationWidgetAnalyticListener {
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
        try {
            lifecycleOwner = findViewTreeLifecycleOwner() ?: context as LifecycleOwner
        } catch (_: Exception) { }
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO
    private var binding: ViewComparisonBpcWidgetBinding? = null
    private val adapter: ComparisonBpcWidgetAdapter by lazy { ComparisonBpcWidgetAdapter(comparisonBpcTypeFactory) }
    private val trackingQueue: TrackingQueue by lazy { TrackingQueue(context) }
    private val userSession: UserSessionInterface by lazy { UserSession(context) }
    private val comparisonBpcTypeFactory: ComparisonBpcTypeFactory = ComparisonBpcTypeFactoryImpl(this)

    private var lifecycleOwner: LifecycleOwner? = null

    override fun bind(model: RecommendationComparisonBpcModel) {
        setupRecyclerView()
        setComparisonWidgetData(model.recommendationWidget, model)
    }

    private fun setupRecyclerView() {
        binding?.run {
            rvComparisonBpcWidget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvComparisonBpcWidget.adapter = adapter
            if (rvComparisonBpcWidget.itemDecorationCount == 0) {
                rvComparisonBpcWidget.addItemDecoration(ComparisonBpcWidgetDecoration())
            }
            comparisonBpcWidgetContainer.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        }
    }

    private fun setComparisonWidgetData(
        recommendationWidget: RecommendationWidget,
        comparisonBpcModel: RecommendationComparisonBpcModel
    ) {
        try {
            launch(Dispatchers.Main) {
                binding?.run {
                    tvHeaderTitle.text = recommendationWidget.title

                    val comparisonListModel = ComparisonBpcWidgetMapper.mapToComparisonWidgetModel(
                        recommendationWidget,
                        comparisonBpcModel.trackingModel,
                        context
                    ).toList()

                    adapter.submitList(ComparisonBpcListModel(listData = comparisonListModel))
                }
            }
        } catch (_: Exception) {
            gone()
        }
    }

    override fun onViewAllCardClicked(applink: String?) {
        adapter.showNextPage()
    }
}
