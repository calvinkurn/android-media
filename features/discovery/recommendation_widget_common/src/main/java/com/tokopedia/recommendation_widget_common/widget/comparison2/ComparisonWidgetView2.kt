package com.tokopedia.recommendation_widget_common.widget.comparison2

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.comparison.*
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.view_comparison_widget2.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.Exception

class ComparisonWidgetView2: ConstraintLayout, CoroutineScope, ComparisonWidgetScrollInterface {

    private var comparisonListModel: ComparisonListModel? = null
    private var adapter: ComparedItemAdapter? = null
    private var comparedAdapter: ComparedItemAdapter? = null

    private var userSessionInterface = UserSession(context)

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_comparison_widget2, this)
        if (rootView.rv_comparison_widget.itemDecorationCount == 0) {
            rootView.rv_comparison_widget.addItemDecoration(ComparisonWidgetDecoration())
            rootView.rv_compared_item.addItemDecoration(ComparisonWidgetDecoration())
        }
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
                    ComparisonWidgetMapper.mapToComparisonWidgetCompareItemModel(recommendationWidget, context)
                if (this@ComparisonWidgetView2.adapter == null) {
                    launch(Dispatchers.Main) {
                        rootView.tv_header_title.text = comparisonListModel.recommendationWidget.title
                        if (comparisonListModel.recommendationWidget.seeMoreAppLink.isNotEmpty()) {
                            rootView.btn_see_more.visible()
                        } else {
                            rootView.btn_see_more.gone()
                        }

                        this@ComparisonWidgetView2.comparisonListModel = comparisonListModel
                        this@ComparisonWidgetView2.adapter = ComparedItemAdapter(
                            comparisonListModel = comparisonListModel,
                            comparisonWidgetInterface = comparisonWidgetInterface,
                            trackingQueue = trackingQueue,
                            userSessionInterface = userSessionInterface,
                            recommendationTrackingModel = recommendationTrackingModel
                        )
                        rootView.rv_comparison_widget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rootView.rv_comparison_widget.adapter = adapter
                    }
                }
                else if (this@ComparisonWidgetView2.comparedAdapter == null) {
                    launch(Dispatchers.Main) {
                        rootView.tv_header_title.text = comparisonListModel.recommendationWidget.title
                        if (comparisonListModel.recommendationWidget.seeMoreAppLink.isNotEmpty()) {
                            rootView.btn_see_more.visible()
                        } else {
                            rootView.btn_see_more.gone()
                        }

                        this@ComparisonWidgetView2.comparisonListModel = comparisonListModel
                        this@ComparisonWidgetView2.comparedAdapter = ComparedItemAdapter(
                            comparisonListModel = comparisonListModel,
                            comparisonWidgetInterface = comparisonWidgetInterface,
                            trackingQueue = trackingQueue,
                            userSessionInterface = userSessionInterface,
                            recommendationTrackingModel = recommendationTrackingModel,
                            isComparedItem = true
                        )
                        rootView.rv_compared_item.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        rootView.rv_compared_item.adapter = comparedAdapter
                    }
                }
            } catch (e: Exception) {
                this@ComparisonWidgetView2.gone()
            }
        }
    }

    private fun calculateActionBar(): Int {
        // Calculate ActionBar height
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        return 0
    }

    private fun isExpandingState(): Boolean {
        return rootView.rv_comparison_widget.layoutParams.width == LinearLayout.LayoutParams.MATCH_PARENT &&
                rootView.rv_comparison_widget.layoutParams.height == LinearLayout.LayoutParams.WRAP_CONTENT
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        if (isExpandingState()) {
//            setStickyHeaderScrollListener()
//        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        if (isExpandingState()) {
//            rootView.viewTreeObserver.removeOnScrollChangedListener(this.specOnScrollChangedListener)
//            this.specOnScrollChangedListener = null
//            stickyTitleViewBinded?.hideStickyTitle()
//        }
    }

    override fun scrollX(x: Int) {
        if (x==0) adapter?.notifyDataSetChanged()
    }
}