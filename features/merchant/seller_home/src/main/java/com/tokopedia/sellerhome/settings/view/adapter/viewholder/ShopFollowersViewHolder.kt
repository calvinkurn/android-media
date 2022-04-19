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
import com.tokopedia.unifyprinciples.Typography

class ShopFollowersViewHolder(itemView: View?,
                              private val onShopFollowersClicked: () -> Unit,
                              private val onErrorClicked: () -> Unit) :
    AbstractViewHolder<ShopFollowersWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_followers
    }

    private val successLayout: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_shop_followers_success)
    private val loadingLayout: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_shop_followers_loading)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_shop_followers)
    private val descTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_followers_desc)

    override fun bind(element: ShopFollowersWidgetUiModel) {
        when(val state = element.state) {
            is SettingResponseState.SettingSuccess -> setSuccessFollowersLayout(state.data)
            is SettingResponseState.SettingError -> setErrorFollowersLayout()
            else -> setLoadingFollowersLayout()
        }
    }

    private fun setSuccessFollowersLayout(totalFollowers: String) {
        successLayout?.show()
        descTextView?.text = totalFollowers
        loadingLayout?.gone()
        errorLayout?.gone()
        itemView.setOnClickListener {
            onShopFollowersClicked()
        }
    }

    private fun setLoadingFollowersLayout() {
        successLayout?.gone()
        loadingLayout?.show()
        errorLayout?.gone()
        itemView.setOnClickListener(null)
    }

    private fun setErrorFollowersLayout() {
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