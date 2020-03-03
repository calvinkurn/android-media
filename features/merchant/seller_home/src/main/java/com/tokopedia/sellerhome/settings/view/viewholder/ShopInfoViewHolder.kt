package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.ShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.state.BaseUiModelState
import kotlinx.android.synthetic.main.setting_shop_info_layout.view.*

class ShopInfoViewHolder(itemView: View) : AbstractViewHolder<ShopInfoUiModel>(itemView){

    companion object {
        val LAYOUT = R.layout.setting_shop_info_layout

        private const val FOLLOWERS = "Followers"
    }

    override fun bind(element: ShopInfoUiModel?) {
        itemView.observeUiState(element)
    }

    private fun View.observeUiState(element: ShopInfoUiModel?) {
        element?.let {
            when(it.uiState) {
                BaseUiModelState.Loading ->
                    //Show shimmer
                    return
                BaseUiModelState.Success ->
                    showSuccessData(it)
                //Show data
                else -> return
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun View.showSuccessData(element: ShopInfoUiModel) {
        shopName.text = element.shopName
        shopFollowing.text = "${element.followerCount}$FOLLOWERS"
    }
}