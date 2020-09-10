package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.ELEMENT_TEXT
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.EMAIL_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.FULLNAME_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.PHONE_TYPE
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_pdp_form_edittext_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern


class EventPDPTextFieldViewHolder(val view: View,
                                  val addOrRemoveData: (Int, String, String) -> Unit,
                                  val userSession: UserSessionInterface,
                                  val formListener: OnClickFormListener,
                                  val textFormListener: TextFormListener) : RecyclerView.ViewHolder(view) {
    var positionActiveForm = 0
    var keyActiveBottomSheet = ""

    fun bind(element: Form, position: Int) {
        with(itemView) {
            keyActiveBottomSheet = getKeyActive(element)
            positionActiveForm = position
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

                        addOrRemoveData(position, text.toString(), "")
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
                    if (list.isNotEmpty()) {
                        txtValue.textFieldInput.apply {
                            keyListener = null

                            val value = if (keyActiveBottomSheet.isNullOrEmpty()) {
                               list.getValueByPosition(0)
                            } else list.get(keyActiveBottomSheet) ?: ""

                            val key = if (keyActiveBottomSheet.isNullOrEmpty()) {
                                list.getKeyByPosition(0)
                            } else keyActiveBottomSheet

                            setText(value)
                            addOrRemoveData(position, key, value)

                            setOnTouchListener(object : View.OnTouchListener {
                                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                                    when (event?.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            formListener.clickBottomSheet(list, element.title, positionActiveForm, keyActiveBottomSheet)
                                        }
                                    }

                                    return v?.onTouchEvent(event) ?: true
                                }
                            })
                        }
                    }
                }
            }

            if (element.elementType.equals(ELEMENT_LIST)) txtValue.setFirstIcon(R.drawable.ent_pdp_expand_arrow_down)
        }
    }

    private fun validateRegex(string: String, pattern: String): Boolean {
        try {
            if (!pattern.isNullOrEmpty()) {
                val mPattern = Pattern.compile(pattern)
                val matcher = mPattern.matcher(string)
                return matcher.matches()
            } else return true
        } catch (e: RuntimeException) {
            return false
        }
    }

    fun getList(value: String): LinkedHashMap<String, String> {
        val listValue: LinkedHashMap<String, String> = LinkedHashMap()
        val jsonArray = JSONArray(value)
        for (i in 0..jsonArray.length() - 1) {
            val key = (jsonArray.getJSONObject(i) as JSONObject).names()?.get(0)?.toString()
            key?.let {
                listValue.put(key, jsonArray.getJSONObject(i).getString(key))
            }
        }
        return listValue
    }

    fun LinkedHashMap<String, String>.getKeyByPosition(position: Int) =
            this.keys.toTypedArray()[position]


    fun LinkedHashMap<String, String>.getValueByPosition(position: Int) =
            this.values.toTypedArray()[position]

    fun getKeyActive(form:Form):String{
        return if(!textFormListener.getKeyActive().isNullOrEmpty()){
            textFormListener.getKeyActive()
        } else if (!form.valuePosition.isNullOrEmpty()){
            form.valuePosition
        } else ""
    }
    interface TextFormListener {
        fun getKeyActive(): String
    }

    companion object {
        val LAYOUT_TEXT = R.layout.ent_pdp_form_edittext_item
    }
}