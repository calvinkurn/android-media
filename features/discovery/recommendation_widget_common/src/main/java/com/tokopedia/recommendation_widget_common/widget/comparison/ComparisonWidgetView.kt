package com.tokopedia.recommendation_widget_common.widget.comparison

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.SlideTrackObject
import com.tokopedia.analytics.byteio.addHorizontalTrackListener
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.listener.AdsItemClickListener
import com.tokopedia.recommendation_widget_common.listener.AdsViewListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.viewutil.parseColorHex
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.compareditem.ComparedItemAdapter
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ComparisonWidgetView : FrameLayout, CoroutineScope {

    private var specOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    private var comparisonListModel: ComparisonListModel? = null
    private var adapter: ComparedItemAdapter? = null
    private var comparedAdapter: ComparisonWidgetAdapter? = null
    private var isAnchorClickable: Boolean = false
    private var shouldUseReimagineCard: Boolean = true

    private var userSessionInterface = UserSession(context)

    private var adsViewListener: AdsViewListener? = null
    private var adsItemClickListener: AdsItemClickListener? = null

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private var rv_comparison_widget: RecyclerView? = null
    private var rv_compared_item: RecyclerView? = null
    private var tv_header_title: TextView? = null
    private var btn_see_more: TextView? = null
    private var btn_collapse: LinearLayout? = null
    private var comparison_widget_container: LinearLayout? = null
    private var tvCollapse: TextView? = null
    private var chevronCollapse: IconUnify? = null

    private fun init(attrs: AttributeSet? = null) {
        initAttributes(attrs)

        LayoutInflater.from(context).inflate(R.layout.view_comparison_widget, this)
        rv_comparison_widget = rootView.findViewById(R.id.rv_comparison_widget)
        rv_compared_item = rootView.findViewById(R.id.rv_compared_item)
        tv_header_title = rootView.findViewById(R.id.tv_header_title)
        btn_see_more = rootView.findViewById(R.id.btn_see_more)
        btn_collapse = rootView.findViewById(R.id.btn_collapse)
        tvCollapse = rootView.findViewById(R.id.tv_collapse)
        chevronCollapse = rootView.findViewById(R.id.iv_chevron_down_see_more)
        comparison_widget_container = rootView.findViewById(R.id.comparison_widget_container)

        if (rv_comparison_widget?.itemDecorationCount == 0) {
            rv_comparison_widget?.addItemDecoration(ComparisonWidgetDecoration())
            rv_compared_item?.addItemDecoration(ComparisonWidgetAnchorDecoration())
        }
        switchToCollapsedState(resources.getDimensionPixelSize(R.dimen.comparison_widget_collapsed_height))
    }

    private fun initAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ComparisonWidgetView, 0, 0)

        try {
            isAnchorClickable = typedArray.getBoolean(R.styleable.ComparisonWidgetView_clickableAnchor, false)
            shouldUseReimagineCard = typedArray.getBoolean(R.styleable.ComparisonWidgetView_useReimagine, true)
        } finally {
            typedArray.recycle()
        }
    }

    fun setComparisonWidgetData(
        recommendationWidget: RecommendationWidget,
        comparisonWidgetInterface: ComparisonWidgetInterface,
        adsViewListener: AdsViewListener,
        adsItemClickListener: AdsItemClickListener,
        recommendationTrackingModel: RecommendationTrackingModel,
        trackingQueue: TrackingQueue?,
        isAnchorClickable: Boolean? = null,
        comparisonColorConfig: ComparisonColorConfig = ComparisonColorConfig(),
        appLogAdditionalParam: AppLogAdditionalParam = AppLogAdditionalParam.None,
        shopId: String? = null,
    ) {
        this.adsViewListener = adsViewListener
        this.adsItemClickListener = adsItemClickListener

        launch {
            try {
                isAnchorClickable?.let { this@ComparisonWidgetView.isAnchorClickable = it }

                val comparisonListModel =
                    ComparisonWidgetMapper.mapToComparisonWidgetModel(
                        recommendationWidget,
                        context,
                        this@ComparisonWidgetView.isAnchorClickable,
                        comparisonColorConfig,
                        shouldUseReimagineCard,
                        appLogAdditionalParam
                    )

                if (this@ComparisonWidgetView.adapter == null) {
                    launch(Dispatchers.Main) {
                        tv_header_title?.text = comparisonListModel.recommendationWidget?.title
                        if (comparisonListModel.recommendationWidget.seeMoreAppLink.isNotEmpty()) {
                            btn_see_more?.visible()
                        } else {
                            btn_see_more?.gone()
                        }

                        val textColor = comparisonListModel.comparisonColorConfig.textColor.parseColorHex(
                            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950_96)
                        )
                        tv_header_title?.setTextColor(textColor)

                        val ctaColor = comparisonListModel.comparisonColorConfig.ctaTextColor.parseColorHex(
                            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
                        )
                        tvCollapse?.setTextColor(ctaColor)
                        chevronCollapse?.setColorFilter(ctaColor)
                        btn_see_more?.setBackgroundColor(ctaColor)

                        this@ComparisonWidgetView.comparisonListModel = comparisonListModel
                        this@ComparisonWidgetView.adapter = ComparedItemAdapter(
                            comparisonListModel = comparisonListModel,
                            comparisonWidgetInterface = comparisonWidgetInterface,
                            trackingQueue = trackingQueue,
                            userSessionInterface = userSessionInterface,
                            recommendationTrackingModel = recommendationTrackingModel,
                            shouldUseReimagineCard = shouldUseReimagineCard,
                            adsItemClickListener = adsItemClickListener,
                            adsViewListener = adsViewListener
                        )
                        rv_comparison_widget?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rv_comparison_widget?.adapter = adapter
                        rv_comparison_widget?.addHorizontalTrackListener(
                            SlideTrackObject(
                                moduleName = recommendationWidget.pageName,
                                barName = recommendationWidget.pageName,
                                shopId = shopId.orEmpty(),
                            )
                        )
                        btn_collapse?.setOnClickListener {
                            val tracking = ProductRecommendationTracking.getClickSpecDetailTracking(
                                eventClick = recommendationTrackingModel.eventClick,
                                eventCategory = recommendationTrackingModel.eventCategory,
                                isLoggedIn = userSessionInterface.isLoggedIn,
                                recomTitle = recommendationTrackingModel.headerTitle,
                                pageName = comparisonListModel.recommendationWidget.pageName,
                                userId = userSessionInterface.userId
                            )
                            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(tracking.second, tracking.first)
                            onSpecDetailsClick(comparisonListModel)
                        }
                        comparison_widget_container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    }
                }
                if (this@ComparisonWidgetView.comparedAdapter == null) {
                    launch(Dispatchers.Main) {
                        this@ComparisonWidgetView.comparisonListModel = comparisonListModel
                        this@ComparisonWidgetView.comparedAdapter = ComparisonWidgetAdapter(
                            comparisonListModel = comparisonListModel,
                            comparisonWidgetInterface = comparisonWidgetInterface,
                            trackingQueue = trackingQueue,
                            userSessionInterface = userSessionInterface,
                            recommendationTrackingModel = recommendationTrackingModel,
                            shouldUseReimagineCard = shouldUseReimagineCard,
                            adsItemClickListener = adsItemClickListener,
                            adsViewListener = adsViewListener
                        )
                        rv_compared_item?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rv_compared_item?.adapter = comparedAdapter
                    }
                }
            } catch (e: NullPointerException) {
                this@ComparisonWidgetView.gone()
            }
        }
    }

    private fun onSpecDetailsClick(comparisonListModel: ComparisonListModel) {
        if (isExpandingState()) {
            switchToCollapsedState(comparisonListModel.comparisonWidgetConfig.collapsedHeight)
        } else {
            switchToExpandState()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun switchToCollapsedState(collapsedHeight: Int) {
        if (isExpandingState()) {
            val layoutParamsComparedItem = rv_compared_item?.layoutParams
            layoutParamsComparedItem?.height = collapsedHeight
            rv_compared_item?.layoutParams = layoutParamsComparedItem
            comparedAdapter?.notifyDataSetChanged()
            adapter?.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun switchToExpandState() {
        if (!isExpandingState()) {
            val layoutParamsComparedItem = rv_compared_item?.layoutParams
            layoutParamsComparedItem?.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            rv_compared_item?.layoutParams = layoutParamsComparedItem
            btn_collapse?.visibility = View.GONE
            comparedAdapter?.notifyDataSetChanged()
            adapter?.notifyDataSetChanged()
        }
    }

    private fun isExpandingState(): Boolean {
        return rv_compared_item?.layoutParams?.height == ConstraintLayout.LayoutParams.WRAP_CONTENT
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isExpandingState()) {
            rootView.viewTreeObserver.removeOnScrollChangedListener(this.specOnScrollChangedListener)
            this.specOnScrollChangedListener = null
            this.adsViewListener = null
            this.adsItemClickListener = null
        }
    }
}
