package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeSharingEducationWidgetUiModel
import kotlinx.android.synthetic.main.item_tokopedianow_home_sharing_education_widget.view.*

class HomeSharingEducationWidgetViewHolder(
    itemView: View,
    private val listener: HomeSharingEducationListener? = null
) : AbstractViewHolder<HomeSharingEducationWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_sharing_education_widget
    }

    override fun bind(element: HomeSharingEducationWidgetUiModel) {
        itemView.btnShare.setOnClickListener {
            listener?.onBtnSharingEducationClicked()
        }
    }

    interface HomeSharingEducationListener {
        fun onBtnSharingEducationClicked()
    }
}