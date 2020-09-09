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
                                  val addOrRemoveData: (Int, String) -> Unit,
                                  val userSession: UserSessionInterface,
                                  val formListener: OnClickFormListener,
                                  val textFormListener: TextFormListener) : RecyclerView.ViewHolder(view) {
    var positionActiveForm = 0
    var positionActiveBottomSheet = 0

    fun bind(element: Form, position: Int) {
        with(itemView) {
            positionActiveBottomSheet = textFormListener.getPositionActive()
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
                element.valuePosition = positionActiveBottomSheet.toString()
                if (element.elementType.equals(ELEMENT_TEXT)) txtValue.textFieldInput.setText(element.value)
                if (element.elementType.equals(ELEMENT_LIST)) {
                    val list = getList(element.value)
                    if (list.isNotEmpty()) {
                        txtValue.textFieldInput.apply {
                            keyListener = null
                            setText(list.get(positionActiveBottomSheet))
                            setOnTouchListener(object : View.OnTouchListener {
                                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                                    when (event?.action) {
                                        MotionEvent.ACTION_DOWN -> {
                                            formListener.clickBottomSheet(list,element.title,positionActiveForm, positionActiveBottomSheet)
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
            val key = (jsonArray.getJSONObject(i) as JSONObject).names()?.get(0)?.toString()
            listValue.add(jsonArray.getJSONObject(i).getString(key))
        }
        return listValue
    }

    interface TextFormListener{
       fun getPositionActive():Int
    }

    companion object {
        val LAYOUT_TEXT = R.layout.ent_pdp_form_edittext_item
    }
}