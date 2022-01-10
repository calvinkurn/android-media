package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.ShareVoucherFactory
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.SocmedType

/**
 * Created By @ilhamsuaib on 28/04/20
 */

data class ShareVoucherUiModel(
        @DrawableRes val icon: Int,
        val socmedName: String,
        @SocmedType val type: Int,
        val status: Int = 0,
        val promo: Int = 0
) : Visitable<ShareVoucherFactory> {

    override fun type(typeFactory: ShareVoucherFactory): Int {
        return typeFactory.type(this)
    }
}