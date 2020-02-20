package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.unifycomponents.CardUnify

class AddressListCardView(context: Context, attrs: AttributeSet) : CardUnify(context, attrs) {

    init {
        View.inflate(context, R.layout.card_address_list, this)
    }

}