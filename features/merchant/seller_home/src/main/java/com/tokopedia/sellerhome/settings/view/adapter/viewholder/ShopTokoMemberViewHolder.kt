package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingResponseState
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.ItemSahNewOtherTotalTokomemberBinding
import com.tokopedia.sellerhome.settings.analytics.SettingTokoMemberTracker
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopFollowersWidgetUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.TokoMemberWidgetUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class ShopTokoMemberViewHolder(
    itemView: View?,
    private val onTokoMemberClicked: () -> Unit,
    private val onErrorClicked: () -> Unit,
    private val onImpressionTokoMember: () -> Unit
) :
    AbstractViewHolder<TokoMemberWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_total_tokomember
    }

    var binding: ItemSahNewOtherTotalTokomemberBinding? by viewBinding()

    private val successLayout: Group? = binding?.groupSahNewOtherTokomemberSuccess
    private val loadingLayout: Group? = binding?.groupSahNewOtherTokomemberLoading
    private val errorLayout: ConstraintLayout? = binding?.errorStateSahNewOtherTokomember?.root
    private val descTextView: Typography? = binding?.tvSahNewOtherTokomemberDesc

    override fun bind(element: TokoMemberWidgetUiModel) {
        when (val state = element.state) {
            is SettingResponseState.SettingSuccess -> {
                setSuccessTotalTokomemberLayout(state.data, element.impressHolder)
            }
            is SettingResponseState.SettingError -> setErrorTotalTokomemberLayout()
            else -> setLoadingTotalTokomemberLayout()
        }
    }

    private fun setSuccessTotalTokomemberLayout(
        totalTokomember: String,
        impressHolder: ImpressHolder
    ) {

        successLayout?.show()
        descTextView?.text = totalTokomember

        loadingLayout?.gone()
        errorLayout?.gone()
        itemView.setOnClickListener {
            onTokoMemberClicked()
        }
        itemView.addOnImpressionListener(impressHolder) {
            onImpressionTokoMember()
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
