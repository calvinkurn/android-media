package com.tokopedia.recharge_component.listener

import com.tokopedia.common.topupbills.view.model.TopupBillsAutoCompleteContactModel

interface ClientNumberAutoCompleteListener {
    fun onClickAutoComplete(favorite: TopupBillsAutoCompleteContactModel)
}