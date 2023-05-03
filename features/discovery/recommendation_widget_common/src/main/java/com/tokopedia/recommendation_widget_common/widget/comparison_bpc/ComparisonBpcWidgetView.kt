package com.tokopedia.recommendation_widget_common.widget.comparison_bpc

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recommendation_widget_common.widget.global.BaseRecommendationWidgetView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.recommendation_widget_common.databinding.ViewComparisonBpcWidgetBinding
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.presenter.RecomWidgetV2ViewModel
import com.tokopedia.recommendation_widget_common.viewutil.doSuccessOrFail
import com.tokopedia.recommendation_widget_common.viewutil.getActivityFromContext
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactory
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.typefactory.ComparisonBpcTypeFactoryImpl
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.adapter.ComparisonBpcWidgetAdapter
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcWidgetDecoration
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.util.ComparisonBpcWidgetMapper
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
/**
 * Created by frenzel on 27/03/23
 */
class ComparisonBpcWidgetView : BaseRecommendationWidgetView<RecommendationComparisonBpcModel>, CoroutineScope {
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
            val viewModelStoreOwner = findViewTreeViewModelStoreOwner() ?: context.getActivityFromContext() as ViewModelStoreOwner
            viewModel = ViewModelProvider(viewModelStoreOwner).get(RecomWidgetV2ViewModel::class.java)
        } catch (_: Exception) { }
    }

    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.IO
    private var binding: ViewComparisonBpcWidgetBinding? = null
    private val adapter: ComparisonBpcWidgetAdapter by lazy { ComparisonBpcWidgetAdapter(comparisonBpcTypeFactory) }
    private var trackingQueue: TrackingQueue? = null
    private val userSession: UserSessionInterface by lazy { UserSession(context) }
    private val comparisonBpcTypeFactory: ComparisonBpcTypeFactory = ComparisonBpcTypeFactoryImpl(this)

    private var lifecycleOwner: LifecycleOwner? = null
    private var viewModel: RecomWidgetV2ViewModel? = null

    override fun bind(model: RecommendationComparisonBpcModel) {
        trackingQueue = model.trackingQueue
        setupRecyclerView()
        viewModel?.loadRecommendation(model)
        lifecycleOwner?.lifecycleScope?.launch {
            viewModel?.getRecommendationByPageName(model.metadata.pageName)?.collect { res ->
                res.doSuccessOrFail({
                    setComparisonWidgetData(
                        it.data,
                        model
                    )
                }) {

                }
            }
        }
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
        launch {
            try {
                launch(Dispatchers.Main) {
                    binding?.run {
                        tvHeaderTitle.text = recommendationWidget.title

                        val comparisonListModel = ComparisonBpcWidgetMapper.mapToComparisonWidgetModel(
                            recommendationWidget,
                            comparisonBpcModel.recomWidgetTrackingModel,
                            context
                        ).toMutableList()

                        adapter.submitList(ComparisonBpcListModel(listData = comparisonListModel))
                    }
                }
            } catch (_: Exception) {
                this@ComparisonBpcWidgetView.gone()
            }
        }
    }

    override fun onViewAllCardClicked(applink: String?) {
        adapter.showNextPage()
    }
}
