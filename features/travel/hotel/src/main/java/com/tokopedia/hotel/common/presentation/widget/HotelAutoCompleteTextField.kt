package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.databinding.WidgetHotelAutocompleteTextfieldBinding
import com.tokopedia.travel.passenger.presentation.widget.InstantAutoCompleteTextView
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 17/04/20
 */

class HotelAutoCompleteTextField @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetHotelAutocompleteTextfieldBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setLabel(label: String) {
        binding.hotelAutocompleteLabel.text = label
    }

    fun setHint(hint: String) {
        binding.acHotelAutocomplete.hint = hint
    }

    fun setEditableText(text: String) {
        binding.acHotelAutocomplete.setText(text)
    }

    fun setInputType(type: Int) {
        binding.acHotelAutocomplete.inputType = type
    }

    fun getAutoCompleteTextView(): InstantAutoCompleteTextView = binding.acHotelAutocomplete

    fun getEditableValue(): String = binding.acHotelAutocomplete.editableText.toString()

    fun setError(message: String) {
        with(binding) {
            tilHotelAutocomplete.error = message
            tilHotelAutocomplete.setErrorTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_RN500)))
            tilHotelAutocomplete.isErrorEnabled = true
        }
    }

    fun setHelper(message: String) {
        with(binding) {
            tilHotelAutocomplete.helperText = message
            tilHotelAutocomplete.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN950_44)))
            tilHotelAutocomplete.isErrorEnabled = false
        }
    }
}
