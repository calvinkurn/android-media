package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.ShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.base.LoadableViewHolder
import kotlinx.android.synthetic.main.setting_shop_info_layout.view.*

class ShopInfoViewHolder(itemView: View) : LoadableViewHolder<ShopInfoUiModel>(itemView){

    companion object {
        val LAYOUT = R.layout.setting_shop_info_layout

        private const val FOLLOWERS = "Followers"
    }

    override fun bind(element: ShopInfoUiModel) {
        observeUiState<ShopInfoUiModel>(element)
    }

    @SuppressLint("SetTextI18n")
    override fun renderSuccessLayout(uiModel: ShopInfoUiModel) {
        with(itemView) {
            shopName.text = uiModel.shopName
            shopFollowing.text = "${uiModel.followerCount} $FOLLOWERS"
        }
    }

    override fun renderLoadingLayout() {

    }

    override fun renderNoDataLayout() {

    }

    override fun renderErrorLayout() {

    }
}