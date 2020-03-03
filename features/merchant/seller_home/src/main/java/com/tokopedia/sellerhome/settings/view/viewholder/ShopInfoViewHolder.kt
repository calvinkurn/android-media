package com.tokopedia.sellerhome.settings.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.ShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.LoadableViewHolder
import kotlinx.android.synthetic.main.setting_shop_info_layout.view.*

class ShopInfoViewHolder(itemView: View) : LoadableViewHolder<ShopInfoUiModel>(itemView){

    companion object {
        val LAYOUT = R.layout.setting_shop_info_layout

        private const val FOLLOWERS = "Followers"
    }

    override fun bind(element: ShopInfoUiModel) {
        observeUiState<ShopInfoUiModel>(element)
    }

    override fun renderSuccessLayout(uiModel: ShopInfoUiModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderLoadingLayout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderNoDataLayout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun renderErrorLayout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @SuppressLint("SetTextI18n")
    private fun View.showSuccessData(element: ShopInfoUiModel) {
        shopName.text = element.shopName
        shopFollowing.text = "${element.followerCount}$FOLLOWERS"
    }
}