package com.tokopedia.recommendation_widget_common.widget.comparison

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.ProductRecommendationTracking
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleInterface
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleModel
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleModelList
import com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle.StickyTitleView
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.view_comparison_widget.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ComparisonWidgetView: FrameLayout, CoroutineScope, ComparisonWidgetScrollInterface  {

    private var specOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null
    private var comparisonListModel: ComparisonListModel? = null
    private var stickyTitleViewBinded: StickyTitleView? = null
    private var stickyTitleInterface: StickyTitleInterface? = null
    private var adapter: ComparisonWidgetAdapter? = null
    private var disableScrollTemp: Boolean = false

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
        }
        switchToCollapsedState(resources.getDimensionPixelSize(R.dimen.comparison_widget_collapsed_height))
    }

    fun setComparisonWidgetData(
            recommendationWidget: RecommendationWidget,
            stickyTitleView: StickyTitleView?,
            comparisonWidgetInterface: ComparisonWidgetInterface,
            recommendationTrackingModel: RecommendationTrackingModel,
            stickyTitleInterface: StickyTitleInterface,
            trackingQueue: TrackingQueue?
    ) {
        launch {
            try {
                val comparisonListModel =
                        ComparisonWidgetMapper.mapToComparisonWidgetModel(recommendationWidget, context)
                if (this@ComparisonWidgetView.adapter == null) {
                    launch(Dispatchers.Main) {
                        rootView.tv_header_title.text = comparisonListModel?.recommendationWidget?.title
                        if (comparisonListModel?.recommendationWidget?.seeMoreAppLink?.isNotEmpty() == true) {
                            rootView.btn_see_more.visible()
                        } else {
                            rootView.btn_see_more.gone()
                        }

                        this@ComparisonWidgetView.comparisonListModel = comparisonListModel
                        this@ComparisonWidgetView.adapter = ComparisonWidgetAdapter(
                                comparisonListModel = comparisonListModel,
                                comparisonWidgetInterface = comparisonWidgetInterface,
                                trackingQueue = trackingQueue,
                                userSessionInterface = userSessionInterface,
                                recommendationTrackingModel = recommendationTrackingModel
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
                        stickyTitleView?.let {
                            this@ComparisonWidgetView.stickyTitleViewBinded = stickyTitleView
                            bindStickyTitleView(stickyTitleInterface)
                        }
                    }
                }
            } catch (e: NullPointerException) {
                this@ComparisonWidgetView.gone()
            }
        }
    }

    fun bindStickyTitleView(stickyTitleInterface: StickyTitleInterface) {
        stickyTitleViewBinded?.let { stickyTitleView ->
            comparisonListModel?.let { comparisonListModel ->
                stickyTitleView.setStickyModelListData(
                        StickyTitleModelList(
                                comparisonListModel.comparisonData.map {
                                    StickyTitleModel(
                                            title = it.productCardModel.productName,
                                            recommendationItem = it.recommendationItem
                                    )
                                }
                        ),
                        stickyTitleInterface,
                        this
                )

                rv_comparison_widget.clearOnScrollListeners()
                rv_comparison_widget.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dx != 0) {
                            this@ComparisonWidgetView.disableScrollTemp = true
                            stickyTitleView.scrollX(dx)
                            this@ComparisonWidgetView.disableScrollTemp = false
                        }
                    }
                })
                this.stickyTitleViewBinded = stickyTitleView
                this.stickyTitleInterface = stickyTitleInterface
            }
        }
    }

    private fun onSpecDetailsClick(comparisonListModel: ComparisonListModel) {
        if (isExpandingState()) {
            switchToCollapsedState(comparisonListModel.comparisonWidgetConfig.collapsedHeight)
        } else {
            switchToExpandState()
            setStickyHeaderScrollListener()
        }
    }

    private fun setStickyHeaderScrollListener() {
        comparisonListModel?.let {
            val specOnScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
                val location = IntArray(2)
                this@ComparisonWidgetView.getLocationOnScreen(location)
                val X = location[0]
                val Y = location[1]
                val elapsedProductCardHeight = -((it.comparisonWidgetConfig.productCardHeight)) + calculateActionBar()
                if (Y < elapsedProductCardHeight) {
                    stickyTitleViewBinded?.showStickyTitle()
                    stickyTitleInterface?.onStickyTitleShow(true)
                } else {
                    stickyTitleViewBinded?.hideStickyTitle()
                    stickyTitleInterface?.onStickyTitleShow(false)
                }
            }

            if (this.specOnScrollChangedListener == null) {
                this.specOnScrollChangedListener = specOnScrollChangedListener
                rootView.viewTreeObserver.addOnScrollChangedListener(this.specOnScrollChangedListener)
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

    private fun switchToCollapsedState(collapsedHeight: Int) {
        if (isExpandingState()) {
            val layoutParams = rootView.rv_comparison_widget.layoutParams
            layoutParams.height = collapsedHeight
            rootView.rv_comparison_widget.layoutParams = layoutParams
            adapter?.notifyDataSetChanged()
        }
    }

    private fun switchToExpandState() {
        if (!isExpandingState()) {
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            rootView.rv_comparison_widget.layoutParams = layoutParams
            rootView.btn_container.visibility = View.GONE
            adapter?.notifyDataSetChanged()
        }
    }

    private fun isExpandingState(): Boolean {
        return rootView.rv_comparison_widget.layoutParams.width == LinearLayout.LayoutParams.MATCH_PARENT &&
                rootView.rv_comparison_widget.layoutParams.height == LinearLayout.LayoutParams.WRAP_CONTENT
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isExpandingState()) {
            setStickyHeaderScrollListener()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isExpandingState()) {
            rootView.viewTreeObserver.removeOnScrollChangedListener(this.specOnScrollChangedListener)
            this.specOnScrollChangedListener = null
            stickyTitleViewBinded?.hideStickyTitle()
        }
    }

    override fun scrollX(x: Int) {
        if (!disableScrollTemp) rv_comparison_widget.scrollBy(x, 0)
    }
}