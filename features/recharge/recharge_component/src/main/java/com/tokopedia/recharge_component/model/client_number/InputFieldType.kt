package com.tokopedia.recharge_component.model.client_number

import android.text.InputType
import android.view.View
import com.tokopedia.iconunify.IconUnify

enum class InputFieldType(
    val inputType: Int,
    val autoFillHintsType: String?,
    val iconUnifyId: Int,
    val hasOperatorIcon: Boolean
) {
    Telco(
        InputType.TYPE_CLASS_TEXT,
        View.AUTOFILL_HINT_PHONE,
        IconUnify.CONTACT,
        true
    ),
    Listrik(
        InputType.TYPE_CLASS_TEXT,
        null,
        IconUnify.QR_CODE,
        false
    ),
    Emoney(
        InputType.TYPE_CLASS_NUMBER,
        null,
        IconUnify.CAMERA,
        false
    ),
    CreditCard(
        InputType.TYPE_CLASS_PHONE,
        View.AUTOFILL_HINT_CREDIT_CARD_NUMBER,
        0,
        false
    )
}
