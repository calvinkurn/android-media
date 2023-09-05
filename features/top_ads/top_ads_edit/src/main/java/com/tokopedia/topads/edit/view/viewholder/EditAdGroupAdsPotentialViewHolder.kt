package com.tokopedia.topads.edit.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.databinding.TopadsEditItemAdsPotentialEditAdGroupBinding
import com.tokopedia.topads.edit.databinding.TopadsEditItemAdsPotentialWidgetEditAdGroupBinding
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemState

class EditAdGroupAdsPotentialViewHolder(private val viewBinding: TopadsEditItemAdsPotentialEditAdGroupBinding) : AbstractViewHolder<EditAdGroupItemAdsPotentialUiModel>(viewBinding.root) {

    override fun bind(element: EditAdGroupItemAdsPotentialUiModel) {
        viewBinding.editAdItemAdsPotentialTitle.text = element.title
        viewBinding.editAdItemAdsPotentialFooter.text = element.footer

        when(element.state){
            EditAdGroupItemState.ERROR -> setContentError()
            EditAdGroupItemState.LOADED -> setContentLoaded(element.listWidget)
            EditAdGroupItemState.LOADING -> setContentLoading()
            else -> setContentLoading()
        }

    }

    private fun setContentLoaded(list: MutableList<EditAdGroupItemAdsPotentialWidgetUiModel>) {
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
        potentialWidget: TopadsEditItemAdsPotentialWidgetEditAdGroupBinding,
        model: EditAdGroupItemAdsPotentialWidgetUiModel
    ) {
        potentialWidget.root.show()
        potentialWidget.editAdItemWidgetAdsPotentialTitle.text = model.title
        potentialWidget.editAdItemWidgetAdsPotentialRetention.text = model.retention
        potentialWidget.editAdItemWidgetAdsPotentialPercentage.text = model.percentage
    }

    companion object {
        val LAYOUT = R.layout.topads_edit_item_ads_potential_edit_ad_group
    }

}
