package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.ELEMENT_TEXT
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.kotlin.extensions.view.setMargin
import kotlinx.android.synthetic.main.ent_pdp_form_edittext_item.view.*
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.FULLNAME_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.EMAIL_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.PHONE_TYPE
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutForm
import com.tokopedia.user.session.UserSessionInterface
import org.json.JSONArray
import timber.log.Timber
import java.lang.RuntimeException
import java.util.regex.Pattern

class EventPDPTextFieldViewHolder(val view: View,
                                  val addOrRemoveData: (Int, String) -> Unit,
                                  val userSession: UserSessionInterface) : RecyclerView.ViewHolder(view) {

    fun bind(element: Form, position: Int) {
        with(itemView) {
            if (position > 0) txtValue.setMargin(0, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3), context.resources.displayMetrics).toInt(), 0, 0)

            txtValue.setPlaceholder(element.helpText)
            txtValue.textFiedlLabelText.text = element.title
            if (element.elementType.equals(ELEMENT_TEXT)) {
                txtValue.textFieldInput.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {}

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (validateRegex(text.toString(), element.validatorRegex)) {
                            txtValue.setMessage("")
                            txtValue.setError(false)
                        } else {
                            txtValue.setMessage(element.errorMessage)
                            txtValue.setError(true)
                        }

                        addOrRemoveData(position, text.toString())
                    }
                })
            }

            if (userSession.isLoggedIn) {
                if (element.name == FULLNAME_TYPE) txtValue.textFieldInput.setText(userSession.name)
                else if (element.name == EMAIL_TYPE) txtValue.textFieldInput.setText(userSession.email)
                else if (element.name == PHONE_TYPE) txtValue.textFieldInput.setText(userSession.phoneNumber)
            }

            if (element.value.isNotBlank()) {
                if (element.elementType.equals(ELEMENT_TEXT)) txtValue.textFieldInput.setText(element.value)
                if (element.elementType.equals(ELEMENT_LIST)) {
                    val list = getList(element.value)
                    if (list.isNotEmpty()) txtValue.textFieldInput.setText(list.get(0))
                }
            }

            if (element.elementType.equals(ELEMENT_LIST)) txtValue.setFirstIcon(R.drawable.ent_pdp_expand_arrow_down)
        }
    }

    private fun validateRegex(string: String, pattern: String): Boolean {
        try {
            val mPattern = Pattern.compile(pattern)
            val matcher = mPattern.matcher(string)
            return matcher.matches()
        } catch (e: RuntimeException) {
            return false
        }
    }

    fun getList(value: String): List<String> {
        val listValue = mutableListOf<String>()
        val jsonArray = JSONArray(value)
        for (i in 0..jsonArray.length() - 1) {
            listValue.add(jsonArray.getJSONObject(i).getString("${i - 1}"))
        }
        return listValue
    }

    companion object {
        val LAYOUT_TEXT = R.layout.ent_pdp_form_edittext_item
    }
}