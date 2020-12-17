package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.EMPTY_TYPE
import com.tokopedia.entertainment.pdp.adapter.EventPDPFormAdapter.Companion.REGEX_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.BLANK_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_LIST
import com.tokopedia.entertainment.pdp.common.util.EventConst.ELEMENT_TEXT
import com.tokopedia.entertainment.pdp.common.util.EventConst.EMAIL_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.FAMILY_NAME_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.FIRST_NAME_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.FULLNAME_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.MOBILE_TYPE
import com.tokopedia.entertainment.pdp.common.util.EventConst.PHONE_TYPE
import com.tokopedia.entertainment.pdp.data.Form
import com.tokopedia.entertainment.pdp.data.checkout.AdditionalType
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.getFamilyName
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventFormMapper.getFirstName
import com.tokopedia.entertainment.pdp.listener.OnClickFormListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.ent_pdp_form_edittext_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern


class EventPDPTextFieldViewHolder(val view: View,
                                  val addOrRemoveData: (Int, String, String) -> Unit,
                                  val userSession: UserSessionInterface,
                                  val formListener: OnClickFormListener,
                                  private val textFormListener: TextFormListener) : RecyclerView.ViewHolder(view) {
    var positionActiveForm = 0
    var keyActiveBottomSheet = ""

    fun bind(element: Form, position: Int) {
        with(itemView) {

            keyActiveBottomSheet = getKeyActive(element)
            positionActiveForm = position
            if (position > 0) txtValue.setMargin(0, context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3).toPx().toInt(), 0, 0)

            if (element.elementType.equals(ELEMENT_TEXT) || !element.elementType.equals(ELEMENT_LIST)) {
                txtValue.textFieldWrapper.hint = element.title
                txtValue.setMessage(element.helpText)
                txtValue.textFieldInput.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {}

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if (validateRegex(text.toString(), element.validatorRegex)) {
                            txtValue.setMessage(element.helpText)
                            txtValue.setError(false)
                        } else {
                            txtValue.setMessage(element.errorMessage)
                            txtValue.setError(true)
                        }

                        addOrRemoveData(position, text.toString(), "")
                    }
                })
            }

            if (element.value.isNullOrEmpty() &&
                    userSession.isLoggedIn &&
                    textFormListener.getAdditionalType().equals(AdditionalType.NULL_DATA)) {
                if (element.name == FULLNAME_TYPE) txtValue.textFieldInput.setText(userSession.name)
                else if (element.name == FAMILY_NAME_TYPE) txtValue.textFieldInput.setText(getFamilyName(userSession.name))
                else if (element.name == FIRST_NAME_TYPE) txtValue.textFieldInput.setText(getFirstName(userSession.name))
                else if (element.name == EMAIL_TYPE) txtValue.textFieldInput.setText(userSession.email)
                else if (element.name == PHONE_TYPE || element.name == MOBILE_TYPE) txtValue.textFieldInput.setText(userSession.phoneNumber)
            }


            if (element.value.isNotBlank() && !element.value.equals(resources.getString(R.string.ent_checkout_data_nullable_form))) {
                if (element.elementType.equals(ELEMENT_TEXT) || !element.elementType.equals(ELEMENT_LIST)) txtValue.textFieldInput.setText(element.value)
                if (element.elementType.equals(ELEMENT_LIST)) {
                    txtValue.setMessage(element.title)
                    val list = getList(element.options)
                    if (list.isNotEmpty()) {

                        val value = if (keyActiveBottomSheet.isNullOrEmpty()) {
                            list.getValueByPosition(0)
                        } else list.get(keyActiveBottomSheet) ?: ""

                        val key = if (keyActiveBottomSheet.isNullOrEmpty()) {
                            list.getKeyByPosition(0)
                        } else keyActiveBottomSheet

                        if(element.required==1 && !key.equals(BLANK_LIST)){
                            txtValue.setMessage(element.helpText)
                            txtValue.setError(false)
                            element.isError = false
                        }

                        txtValue.textFieldInput.apply {
                            keyListener = null

                            setText(value)
                            addOrRemoveData(position, key, value)

                            setOnTouchListener(object : View.OnTouchListener {
                                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                                    when (event?.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            formListener.clickBottomSheet(getListRequired(list, element.required), element.title, positionActiveForm)
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

            if (element.isError) {
                if (element.errorType == EMPTY_TYPE) {
                    txtValue.setMessage(resources.getString(R.string.ent_pdp_form_error_all_msg, element.title))
                } else if (element.errorType == REGEX_TYPE) {
                    txtValue.setMessage(element.errorMessage)
                }
                txtValue.setError(true)
            }
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

    fun getListRequired(list: LinkedHashMap<String, String>, isRequired: Int): LinkedHashMap<String, String> {
        if (isRequired == 1) {
            val listValue: LinkedHashMap<String, String> = LinkedHashMap()
            list.map {
                if (!it.key.equals(BLANK_LIST)) {
                    listValue.put(it.key, it.value)
                }
            }
            return listValue
        } else {
            return list
        }
    }


    fun getList(value: String): LinkedHashMap<String, String> {
        val listValue: LinkedHashMap<String, String> = LinkedHashMap()
        if(value.isNotEmpty()) {
            val jsonArray = JSONArray(value)
            for (i in 0..jsonArray.length() - 1) {
                val key = (jsonArray.getJSONObject(i) as JSONObject).names()?.get(0)?.toString()
                key?.let {
                    listValue.put(key, jsonArray.getJSONObject(i).getString(key))
                }
            }
        }
        return listValue
    }

    fun LinkedHashMap<String, String>.getKeyByPosition(position: Int) =
            this.keys.toTypedArray()[position]


    fun LinkedHashMap<String, String>.getValueByPosition(position: Int) =
            this.values.toTypedArray()[position]

    fun getKeyActive(form: Form): String {
        val key = if (!textFormListener.getKeyActive().isNullOrEmpty()) {
            textFormListener.getKeyActive()
        } else if (!form.valuePosition.isNullOrEmpty()) {
            form.valuePosition
        } else ""
        textFormListener.resetActiveKey()
        return key
    }

    interface TextFormListener {
        fun getKeyActive(): String
        fun resetActiveKey()
        fun getAdditionalType(): AdditionalType
    }

    companion object {
        val LAYOUT_TEXT = R.layout.ent_pdp_form_edittext_item
    }
}