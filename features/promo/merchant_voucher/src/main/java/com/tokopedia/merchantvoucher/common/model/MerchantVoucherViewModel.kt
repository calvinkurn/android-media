package com.tokopedia.merchantvoucher.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory

/**
 * Created by hendry on 01/10/18.
 */
class MerchantVoucherViewModel : Visitable<MerchantVoucherAdapterTypeFactory>{

    override fun type(typeFactory: MerchantVoucherAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}