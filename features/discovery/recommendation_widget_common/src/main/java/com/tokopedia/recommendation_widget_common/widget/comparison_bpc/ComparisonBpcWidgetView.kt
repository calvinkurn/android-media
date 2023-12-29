package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.recommendation_widget_common.databinding.ViewComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.ComparisonBpcWidgetAdapter
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking.ComparisonBpcAnalyticListener
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking.ComparisonBpcWidgetTracking
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcWidgetDecoration
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcWidgetMapper
import com.tokopedia.recommendation_widget_common.widget.global.IRecommendationWidgetView
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetTrackingModel
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
class ComparisonBpcWidgetView :
    ConstraintLayout,
    IRecommendationWidgetView<RecommendationComparisonBpcModel>,
    CoroutineScope,
    ComparisonBpcAnalyticListener,
    LifecycleEventObserver {
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
        binding = ViewComparisonBpcWidgetBinding.inflate(LayoutInflater.from(context), this)
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

    override val layoutId: Int
        get() = LAYOUT

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

                    val mappedModel = ComparisonBpcWidgetMapper.mapToComparisonWidgetModel(
                        recommendationWidget,
                        comparisonBpcModel.trackingModel,
                        context
                    )

                    val comparisonListModel = mappedModel.second.toList()
                    val productAnchor = mappedModel.first

                    if (productAnchor == null && comparisonListModel.isEmpty()) {
                        gone()
                        return@launch
                    }
                    adapter.submitList(ComparisonBpcListModel(listData = comparisonListModel, trackingModel = comparisonBpcModel.trackingModel, productAnchor = productAnchor))
                }
            }
        } catch (_: Exception) {
            gone()
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                trackingQueue.sendAll()
            }
            else -> { }
        }
    }

    override fun onViewAllCardClicked(trackingModel: RecommendationWidgetTrackingModel, productAnchorId: String) {
        adapter.showNextPage()
        ComparisonBpcWidgetTracking.sendClickSeeAll(trackingModel.androidPageName, userSession.userId, productAnchorId)
    }

    override fun onProductCardImpressed(recommendationItem: RecommendationItem, trackingModel: RecommendationWidgetTrackingModel, anchorProductId: String, widgetTitle: String) {
        ComparisonBpcWidgetTracking.putImpressionToQueue(
            trackingQueue = trackingQueue,
            recommendationItem = recommendationItem,
            androidPageName = trackingModel.androidPageName,
            anchorProductId = anchorProductId,
            userId = userSession.userId,
            widgetTitle = widgetTitle
        )
    }

    override fun onProductCardClicked(recommendationItem: RecommendationItem, trackingModel: RecommendationWidgetTrackingModel, anchorProductId: String) {
        ComparisonBpcWidgetTracking.sendClick(
            trackingModel.androidPageName,
            userSession.userId,
            recommendationItem,
            anchorProductId
        )
        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, recommendationItem.productId.toString())
    }

    override fun recycle() {

    }
}
