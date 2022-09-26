package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopFollowersWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoMemberWidgetUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopTokoMemberViewHolder(itemView: View?,
                               private val onTokoMemberClicked: () -> Unit,
                               private val onErrorClicked: () -> Unit) :
    AbstractViewHolder<TokoMemberWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_total_tokomember
    }

    private val successLayout: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_tokomember_success)
    private val loadingLayout: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_tokomember_loading)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_tokomember)
    private val descTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_tokomember_desc)

    override fun bind(element: TokoMemberWidgetUiModel) {
        when(val state = element.state) {
            is SettingResponseState.SettingSuccess -> setSuccessTotalTokomemberLayout(state.data)
            is SettingResponseState.SettingError -> setErrorTotalTokomemberLayout()
            else -> setLoadingTotalTokomemberLayout()
        }
    }

    private fun setSuccessTotalTokomemberLayout(totalTokomember: String) {

        successLayout?.show()
        descTextView?.text = totalTokomember

        loadingLayout?.gone()
        errorLayout?.gone()
        itemView.setOnClickListener {
            onTokoMemberClicked()
        }
    }

    private fun setLoadingTotalTokomemberLayout() {
        successLayout?.gone()
        loadingLayout?.show()
        errorLayout?.gone()
        itemView.setOnClickListener(null)
    }

    private fun setErrorTotalTokomemberLayout() {
        successLayout?.gone()
        loadingLayout?.gone()
        errorLayout?.run {
            show()
            setOnClickListener {
                onErrorClicked()
            }
        }
        itemView.setOnClickListener(null)
    }

}
