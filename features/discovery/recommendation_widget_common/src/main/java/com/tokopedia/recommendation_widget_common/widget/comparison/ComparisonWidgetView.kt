package com.tokopedia.recommendation_widget_common.widget.comparison

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.compareditem.ComparedItemAdapter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.view_comparison_widget.view.*
import kotlinx.android.synthetic.main.view_comparison_widget.view.btn_see_more
import kotlinx.android.synthetic.main.view_comparison_widget.view.comparison_widget_container
import kotlinx.android.synthetic.main.view_comparison_widget.view.rv_compared_item
import kotlinx.android.synthetic.main.view_comparison_widget.view.rv_comparison_widget
import kotlinx.android.synthetic.main.view_comparison_widget.view.tv_header_title
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ComparisonWidgetView: FrameLayout, CoroutineScope  {

    private var specOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    private var comparisonListModel: ComparisonListModel? = null
    private var adapter: ComparedItemAdapter? = null
    private var comparedAdapter: ComparisonWidgetAdapter? = null

    private var userSessionInterface = UserSession(context)

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_comparison_widget, this)
        if (rootView.rv_comparison_widget.itemDecorationCount == 0) {
            rootView.rv_comparison_widget.addItemDecoration(ComparisonWidgetDecoration())
            rootView.rv_compared_item.addItemDecoration(ComparisonWidgetDecoration())
        }
        switchToCollapsedState(resources.getDimensionPixelSize(R.dimen.comparison_widget_collapsed_height))
    }

    fun setComparisonWidgetData(
            recommendationWidget: RecommendationWidget,
            comparisonWidgetInterface: ComparisonWidgetInterface,
            recommendationTrackingModel: RecommendationTrackingModel,
            trackingQueue: TrackingQueue?
    ) {
        launch {
            try {
                val comparisonListModel =
                        ComparisonWidgetMapper.mapToComparisonWidgetModel(recommendationWidget, context)
                if (this@ComparisonWidgetView.adapter == null) {
                    launch(Dispatchers.Main) {
                        rootView.tv_header_title.text = comparisonListModel.recommendationWidget?.title
                        if (comparisonListModel.recommendationWidget.seeMoreAppLink.isNotEmpty()) {
                            rootView.btn_see_more.visible()
                        } else {
                            rootView.btn_see_more.gone()
                        }

                        this@ComparisonWidgetView.comparisonListModel = comparisonListModel
                        this@ComparisonWidgetView.adapter = ComparedItemAdapter(
                                comparisonListModel = comparisonListModel,
                                comparisonWidgetInterface = comparisonWidgetInterface,
                                trackingQueue = trackingQueue,
                                userSessionInterface = userSessionInterface,
                                recommendationTrackingModel = recommendationTrackingModel,
                        )
                        rootView.rv_comparison_widget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rootView.rv_comparison_widget.adapter = adapter
                        rootView.btn_collapse.setOnClickListener {
                            ProductRecommendationTracking.getClickSpecDetailTracking(
                                    eventClick = recommendationTrackingModel.eventClick,
                                    eventCategory = recommendationTrackingModel.eventCategory,
                                    isLoggedIn = userSessionInterface.isLoggedIn,
                                    recomTitle = recommendationTrackingModel.headerTitle,
                                    pageName = comparisonListModel.recommendationWidget.pageName,
                                    userId = userSessionInterface.userId
                            )
                            onSpecDetailsClick(comparisonListModel)
                        }
                        rootView.comparison_widget_container.layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
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
                            recommendationTrackingModel = recommendationTrackingModel
                        )
                        rootView.rv_compared_item.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rootView.rv_compared_item.adapter = comparedAdapter
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

    private fun switchToCollapsedState(collapsedHeight: Int) {
        if (isExpandingState()) {
            val layoutParams = rootView.rv_comparison_widget.layoutParams
            layoutParams.height = collapsedHeight
            rootView.rv_comparison_widget.layoutParams = layoutParams
            adapter?.notifyDataSetChanged()

            val layoutParamsComparedItem = rootView.rv_compared_item.layoutParams
            layoutParamsComparedItem.height = collapsedHeight
            rootView.rv_compared_item.layoutParams = layoutParamsComparedItem
            comparedAdapter?.notifyDataSetChanged()
        }
    }

    private fun switchToExpandState() {
        if (!isExpandingState()) {
            val layoutParams = ConstraintLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            rootView.rv_comparison_widget.layoutParams = layoutParams
            adapter?.notifyDataSetChanged()

            val layoutParamsComparedItem = ConstraintLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            rootView.rv_compared_item.layoutParams = layoutParamsComparedItem
            rootView.btn_collapse.visibility = View.GONE
            comparedAdapter?.notifyDataSetChanged()
        }
    }

    private fun isExpandingState(): Boolean {
        return rootView.rv_comparison_widget.layoutParams.width == LinearLayout.LayoutParams.MATCH_PARENT &&
                rootView.rv_comparison_widget.layoutParams.height == LinearLayout.LayoutParams.WRAP_CONTENT
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isExpandingState()) {
            rootView.viewTreeObserver.removeOnScrollChangedListener(this.specOnScrollChangedListener)
            this.specOnScrollChangedListener = null
        }
    }
}