package com.tokopedia.recharge_component.model.client_number

import android.text.InputType
import com.tokopedia.iconunify.IconUnify

enum class InputFieldType(
    val inputType: Int,
    val iconUnifyId: Int,
    val hasOperatorIcon: Boolean
) {
    Telco(InputType.TYPE_CLASS_TEXT, IconUnify.CONTACT, true),
    Listrik(InputType.TYPE_CLASS_TEXT, IconUnify.QR_CODE, false),
    Emoney(InputType.TYPE_CLASS_NUMBER, IconUnify.CAMERA, false),
    CreditCard(InputType.TYPE_CLASS_PHONE, 0, false),
}