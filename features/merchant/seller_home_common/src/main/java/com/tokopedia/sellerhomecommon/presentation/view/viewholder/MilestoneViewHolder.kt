package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import android.view.ViewStub
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneWidgetUiModel

class MilestoneViewHolder(
    itemView: View
) : AbstractViewHolder<MilestoneWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_milestone_widget
    }

    private val onLoadingView: View by itemView.viewStubInflater(R.id.stubShcMilestoneLoading)
    private val onErrorView: View by itemView.viewStubInflater(R.id.stubShcMilestoneError)
    private val onSuccessView: View by itemView.viewStubInflater(R.id.stubShcMilestoneSuccess)

    override fun bind(element: MilestoneWidgetUiModel?) {
        when {
            element?.data == null -> {

            }
            element.data?.isError == true -> {

            }
            else -> {

            }
        }
    }

    private fun View.viewStubInflater(viewStubId: Int): Lazy<View> {
        return lazy {
            val viewStub: ViewStub = findViewById(viewStubId)
            viewStub.inflate()
        }
    }

}