package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewStub
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel
import kotlinx.android.synthetic.main.shc_milestone_widget_error.view.*
import kotlinx.android.synthetic.main.shc_milestone_widget_loading.view.*
import kotlinx.android.synthetic.main.shc_milestone_widget_success.view.*

class MilestoneViewHolder(
    itemView: View
) : AbstractViewHolder<MilestoneWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_milestone_widget
    }

    private val onLoadingView: View by itemView.viewStubInflater(R.id.stubShcMilestoneLoading)
    private val onErrorView: View by itemView.viewStubInflater(R.id.stubShcMilestoneError)
    private val onSuccessView: View by itemView.viewStubInflater(R.id.stubShcMilestoneSuccess)

    override fun bind(element: MilestoneWidgetUiModel) {
        val data = element.data
        when {
            data == null -> showLoadingState()
            data.error.isNotBlank() -> showErrorState(element)
            else -> showLoadingState()// setOnSuccess(element)
        }
    }

    private fun setOnSuccess(element: MilestoneWidgetUiModel) {
        onSuccessView.containerShcMilestoneSuccess.visible()
        onErrorView.containerShcMilestoneError.gone()
        onLoadingView.containerShcMilestoneLoading.gone()
    }

    private fun showErrorState(element: MilestoneWidgetUiModel) {
        onSuccessView.containerShcMilestoneSuccess.gone()
        onErrorView.containerShcMilestoneError.visible()
        onLoadingView.containerShcMilestoneLoading.gone()
    }

    private fun showLoadingState() {
        onSuccessView.containerShcMilestoneSuccess.gone()
        onErrorView.containerShcMilestoneError.gone()
        onLoadingView.containerShcMilestoneLoading.visible()
    }

    private fun View.viewStubInflater(viewStubId: Int): Lazy<View> {
        return lazy {
            val viewStub: ViewStub = findViewById(viewStubId)
            viewStub.inflate()
        }
    }
}