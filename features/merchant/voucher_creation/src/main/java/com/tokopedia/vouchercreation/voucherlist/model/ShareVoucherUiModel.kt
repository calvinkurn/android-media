package com.tokopedia.vouchercreation.voucherlist.model

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.ShareVoucherFactory
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.SocmedType

/**
 * Created By @ilhamsuaib on 28/04/20
 */

data class ShareVoucherUiModel(
        @DrawableRes val icon: Int,
        val socmedName: String,
        @SocmedType val type: Int
) : Visitable<ShareVoucherFactory> {

    override fun type(typeFactory: ShareVoucherFactory): Int {
        return typeFactory.type(this)
    }
}