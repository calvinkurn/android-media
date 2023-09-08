package com.tokopedia.topads.common.view.adapter.createedit.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.common.databinding.TopadsCreateEditItemAdsPotentialAdGroupBinding
import com.tokopedia.topads.common.databinding.TopadsCreateEditItemAdsPotentialWidgetAdGroupBinding
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemState

class CreateEditAdGroupAdsPotentialViewHolder(private val viewBinding: TopadsCreateEditItemAdsPotentialAdGroupBinding) : AbstractViewHolder<CreateEditAdGroupItemAdsPotentialUiModel>(viewBinding.root) {

    override fun bind(element: CreateEditAdGroupItemAdsPotentialUiModel) {
        viewBinding.editAdItemAdsPotentialTitle.text = element.title
        viewBinding.editAdItemAdsPotentialFooter.text = element.footer

        when(element.state){
            CreateEditAdGroupItemState.ERROR -> setContentError()
            CreateEditAdGroupItemState.LOADED -> setContentLoaded(element.listWidget)
            CreateEditAdGroupItemState.LOADING -> setContentLoading()
            else -> setContentLoading()
        }

    }

    private fun setContentLoaded(list: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>) {
        viewBinding.groupShimmer.hide()
        viewBinding.groupWidget.show()
        viewBinding.btnLoadMorePotential.hide()
        when(list.size){
            1 -> {
                setDataPotentialWidget(viewBinding.potentialWidget1, list[0])
            }
            2 -> {
                setDataPotentialWidget(viewBinding.potentialWidget1, list[0])
                setDataPotentialWidget(viewBinding.potentialWidget2, list[1])
            }
            3 -> {
                setDataPotentialWidget(viewBinding.potentialWidget1, list[0])
                setDataPotentialWidget(viewBinding.potentialWidget2, list[1])
                setDataPotentialWidget(viewBinding.potentialWidget3, list[2])
            }
        }
    }

    private fun setContentLoading() {
        viewBinding.groupShimmer.show()
        viewBinding.groupWidget.hide()
        viewBinding.btnLoadMorePotential.hide()
    }
    private fun setContentError() {
        viewBinding.groupShimmer.hide()
        viewBinding.groupWidget.hide()
        viewBinding.btnLoadMorePotential.show()
    }

    private fun setDataPotentialWidget(
        potentialWidget: TopadsCreateEditItemAdsPotentialWidgetAdGroupBinding,
        model: CreateEditAdGroupItemAdsPotentialWidgetUiModel
    ) {
        potentialWidget.root.show()
        potentialWidget.editAdItemWidgetAdsPotentialTitle.text = model.title
        potentialWidget.editAdItemWidgetAdsPotentialRetention.text = String.format("%sx/minggu", model.retention)
        potentialWidget.editAdItemWidgetAdsPotentialPercentage.text = model.percentage
    }

    companion object {
        val LAYOUT = R.layout.topads_create_edit_item_ads_potential_ad_group
    }

}
