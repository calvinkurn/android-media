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
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.addHorizontalTrackListener
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.databinding.ViewComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.ComparisonBpcWidgetAdapter
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.model.ComparisonBpcSeeMoreDataModel
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
import com.tokopedia.recommendation_widget_common.R as recommendation_widget_commonR

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
        val LAYOUT = recommendation_widget_commonR.layout.view_comparison_bpc_widget
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO
    private var binding: ViewComparisonBpcWidgetBinding? = null
    private val adapter: ComparisonBpcWidgetAdapter by lazy { ComparisonBpcWidgetAdapter(comparisonBpcTypeFactory) }
    private val trackingQueue: TrackingQueue by lazy { TrackingQueue(context) }
    private val userSession: UserSessionInterface by lazy { UserSession(context) }
    private val comparisonBpcTypeFactory: ComparisonBpcTypeFactory = ComparisonBpcTypeFactoryImpl(this)

    private var lifecycleOwner: LifecycleOwner? = null

    init {
        binding = ViewComparisonBpcWidgetBinding.inflate(LayoutInflater.from(context), this)
        try {
            lifecycleOwner = findViewTreeLifecycleOwner() ?: context as LifecycleOwner
        } catch (_: Exception) { }
    }

    override val layoutId: Int
        get() = LAYOUT

    override fun bind(model: RecommendationComparisonBpcModel) {
        setupRecyclerView(model.recommendationWidget)
        setComparisonWidgetData(model.recommendationWidget, model)
    }

    private fun setupRecyclerView(model: RecommendationWidget) {
        binding?.run {
            rvComparisonBpcWidget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvComparisonBpcWidget.adapter = adapter
            rvComparisonBpcWidget.addHorizontalTrackListener(
                SlideTrackObject(
                    moduleName = model.pageName,
                    barName = model.pageName,
                )
            )
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
                        comparisonBpcModel.appLogAdditionalParam,
                        context
                    )

                    val comparisonListModel = mappedModel.second.toList()
                    val productAnchor = mappedModel.first

                    if (productAnchor == null && comparisonListModel.isEmpty()) {
                        gone()
                        return@launch
                    }
                    adapter.submitList(
                        ComparisonBpcListModel(
                            pageName = recommendationWidget.pageName,
                            listData = comparisonListModel,
                            trackingModel = comparisonBpcModel.trackingModel,
                            productAnchor = productAnchor)
                    )
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

    override fun onViewAllCardClicked(element: ComparisonBpcSeeMoreDataModel) {
        adapter.showNextPage()
        AppLogAnalytics.setGlobalParams(enterMethod = ENTER_METHOD_SEE_MORE.format(element.recomPageName))
        ComparisonBpcWidgetTracking.sendClickSeeAll(element.trackingModel.androidPageName, userSession.userId, element.productAnchor?.anchorProductId.orEmpty())
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

    override fun onProductCardByteIoView(
        recommendationItem: RecommendationItem,
        appLogAdditionalParam: AppLogAdditionalParam
    ) {
        AppLogRecommendation.sendProductShowAppLog(
            recommendationItem.asProductTrackModel(
                entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
                additionalParam = appLogAdditionalParam
            )
        )
    }

    override fun onProductCardClicked(
        recommendationItem: RecommendationItem,
        trackingModel: RecommendationWidgetTrackingModel,
        anchorProductId: String,
        appLogAdditionalParam: AppLogAdditionalParam
    ) {
        // GTM
        ComparisonBpcWidgetTracking.sendClick(
            trackingModel.androidPageName,
            userSession.userId,
            recommendationItem,
            anchorProductId
        )
        // ByteIO
        AppLogRecommendation.sendProductClickAppLog(
            recommendationItem.asProductTrackModel(
                entranceForm = EntranceForm.HORIZONTAL_GOODS_CARD,
                additionalParam = appLogAdditionalParam
            )
        )

        RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, recommendationItem.productId.toString())
    }

    override fun recycle() {

    }
}
