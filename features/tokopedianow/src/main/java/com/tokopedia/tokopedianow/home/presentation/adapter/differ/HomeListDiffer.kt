package com.tokopedia.tokopedianow.home.presentation.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingWidgetUiModel.HomeSharingReferralWidgetUiModel

class HomeListDiffer : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return if (oldItem is HomeProductRecomUiModel && newItem is HomeProductRecomUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeProductCarouselChipsUiModel && newItem is HomeProductCarouselChipsUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeLeftCarouselAtcUiModel && newItem is HomeLeftCarouselAtcUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
        } else if (oldItem is TokoNowRepurchaseUiModel && newItem is TokoNowRepurchaseUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is TokoNowCategoryMenuUiModel && newItem is TokoNowCategoryMenuUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeSharingReferralWidgetUiModel && newItem is HomeSharingReferralWidgetUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is HomeHeaderUiModel && newItem is HomeHeaderUiModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return oldItem.equals(newItem)
    }

    override fun getChangePayload(oldItem: Visitable<*>, newItem: Visitable<*>): Any? {
        return if (oldItem is HomeLayoutUiModel && newItem is HomeLayoutUiModel) {
            oldItem.getChangePayload(newItem)
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}
