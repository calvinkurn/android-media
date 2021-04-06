package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewStub
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.RecommendationWidgetUiModel
import kotlinx.android.synthetic.main.shc_recommendation_widget_error.view.*
import kotlinx.android.synthetic.main.shc_recommendation_widget_loading.view.*
import kotlinx.android.synthetic.main.shc_recommendation_widget_success.view.*

/**
 * Created By @ilhamsuaib on 05/04/21
 */

class RecommendationViewHolder(itemView: View) : AbstractViewHolder<RecommendationWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_recommendation_widget
    }

    private val onLoadingView: View by itemView.viewStubInflater(R.id.stubShcRecommendationLoading)
    private val onErrorView: View by itemView.viewStubInflater(R.id.stubShcRecommendationError)
    private val onSuccessView: View by itemView.viewStubInflater(R.id.stubShcRecommendationSuccess)

    override fun bind(element: RecommendationWidgetUiModel) {
        val data = element.data
        when {
            data == null -> showLoadingState()
            data.error.isNotBlank() -> showErrorState(element.title)
            else -> setOnSuccess(element)
        }
    }

    private fun setOnSuccess(element: RecommendationWidgetUiModel) {
        with(onSuccessView) {
            containerShcRecommendationSuccess.visible()
            onLoadingView.containerShcRecommendationLoading.gone()
            onErrorView.containerShcRecommendationError.gone()
        }
    }

    private fun showErrorState(title: String) = with(onErrorView) {
        containerShcRecommendationError.visible()
        onSuccessView.containerShcRecommendationSuccess.gone()
        onLoadingView.containerShcRecommendationLoading.gone()

        tvShcRecommendationErrorStateTitle.text = title
    }

    private fun showLoadingState() = with(onLoadingView) {
        containerShcRecommendationLoading.visible()
    }

    private fun View.viewStubInflater(viewStubId: Int): Lazy<View> {
        return lazy {
            val viewStub: ViewStub = findViewById(viewStubId)
            viewStub.inflate()
        }
    }
}