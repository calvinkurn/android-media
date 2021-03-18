package com.tokopedia.recommendation_widget_common.widget.comparison

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.recommendation_widget_common.R
import kotlinx.android.synthetic.main.view_comparison_widget.view.*

class ComparisonWidgetView: FrameLayout {

    private var adapter: ComparisonWidgetAdapter? = null
    private val masterJob = SupervisorJob()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: ComparisonWidgetViewModel

    constructor(context: Context) : super(context) { }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) { }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_comparison_widget, this)
        if (rootView.rv_comparison_widget.itemDecorationCount == 0) {
            rootView.rv_comparison_widget.addItemDecoration(ComparisonWidgetDecoration())
        }
        switchToCollapsedState(0)
    }

    fun setComparisonWidgetData(comparisonListModel: ComparisonListModel) {
        this.adapter = ComparisonWidgetAdapter(comparisonListModel)

        rootView.tv_header_title.text = comparisonListModel.headerTitle
        if (comparisonListModel.seeMoreApplink.isNotEmpty()) {
            rootView.btn_see_more.visible()
        } else {
            rootView.btn_see_more.gone()
        }

        rootView.rv_comparison_widget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rootView.rv_comparison_widget.adapter = adapter
        rootView.comparison_widget_container.layoutTransition
                .enableTransitionType(LayoutTransition.CHANGING);

        if (isExpandingState()) {
            switchToExpandState()
        } else {
            switchToCollapsedState(comparisonListModel.comparisonWidgetConfig.collapsedHeight)
        }

        rootView.btn_collapse.setOnClickListener {
            if (isExpandingState()) {
                switchToCollapsedState(comparisonListModel.comparisonWidgetConfig.collapsedHeight)
            } else {
                switchToExpandState()
            }
        }
    }

    private fun switchToCollapsedState(collapsedHeight: Int) {
        val layoutParams = rootView.rv_comparison_widget.layoutParams
        layoutParams.height = collapsedHeight
        rootView.rv_comparison_widget.layoutParams = layoutParams
        adapter?.notifyDataSetChanged()
    }

    private fun switchToExpandState() {
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        rootView.rv_comparison_widget.layoutParams = layoutParams
        rootView.btn_container.visibility = View.GONE
        adapter?.notifyDataSetChanged()
    }

    private fun isExpandingState(): Boolean {
        return rootView.rv_comparison_widget.layoutParams.width == LinearLayout.LayoutParams.MATCH_PARENT &&
                rootView.rv_comparison_widget.layoutParams.height == LinearLayout.LayoutParams.WRAP_CONTENT
    }
}