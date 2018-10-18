package com.tokopedia.promocheckout.list

import com.tokopedia.abstraction.base.view.adapter.Visitable

class PromoCheckoutListModel : Visitable<PromoCheckoutListAdapterFactory> {
    override fun type(typeFactory: PromoCheckoutListAdapterFactory?): Int {
        return typeFactory?.type(this)?:0
    }
}
