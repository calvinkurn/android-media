package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_hotel_autocomplete_textfield.view.*

/**
 * @author by jessica on 17/04/20
 */

class HotelAutoCompleteTextField @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.widget_hotel_autocomplete_textfield, this)
    }

    fun setLabel(label : String){
        hotel_autocomplete_label.text = label
    }

    fun setHint(hint : String){
        ac_hotel_autocomplete.hint = hint
    }

    fun setError(message: String) {
        til_hotel_autocomplete.error = message
        til_hotel_autocomplete.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Red_R500)))
    }

    fun setHelper(message: String) {
        til_hotel_autocomplete.helperText = message
        til_hotel_autocomplete.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_44)))
    }
}