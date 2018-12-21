package com.tokopedia.common_digital.product.presentation.model

/**
 * Created by Rizky on 31/08/18.
 */
class InputFieldModel(val name: String, val type: String, val text: String, val placeholder: String, val default: String,
                      val validation: List<Validation>) {
    var additionalButton: AdditionalButton? = null

    companion object {

        val NAME_OPERATOR_ID = "operator_id"
        val NAME_PRODUCT_ID = "product_id"

        val TYPE_TEXT = "text"
        val TYPE_NUMERIC = "numeric"
        val TYPE_TEL = "tel"
        val TYPE_SELECT = "select"
        val TYPE_RADIO = "radio"
        val TYPE_CHECKBOX = "checkbox"
        val TYPE_SELECT_CARD = "select_card"
        val TYPE_SELECT_LIST = "select_list"
        val TYPE_NUMERIC_STICKY = "numeric_sticky"
    }

}
