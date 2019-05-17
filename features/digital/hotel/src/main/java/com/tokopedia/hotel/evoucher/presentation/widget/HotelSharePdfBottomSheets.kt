package com.tokopedia.hotel.evoucher.presentation.widget

import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.bottom_sheets_share_as_pdf.view.*

/**
 * @author by furqan on 16/05/19
 */
class HotelSharePdfBottomSheets : BottomSheets() {

    val emailList = mutableListOf<String>()

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_share_as_pdf

    override fun initView(view: View) {
        with(view) {
            ev_error_email.setTextColor(ContextCompat.getColor(context, R.color.red_500))

            ev_email.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    btn_send_email.isEnabled = !(s.isEmpty() && emailList.isEmpty())
                    ev_error_email.visibility = View.GONE
                }
            })

            container_add_email.setOnClickListener {
                if (validateEmail(ev_error_email, ev_email.text.toString())) {
                    emailList.add(ev_email.text.toString())
                    ev_error_email.visibility = View.GONE
                }
            }

            if (emailList.isEmpty()) {
                divider_list.visibility = View.GONE
            }
        }
    }

    override fun title(): String = getString(R.string.hotel_share_as_pdf)

    private fun validateEmail(tvError: TextViewCompat, email: String): Boolean {
        var valid = true
        when {
            email.isEmpty() -> {
                valid = false
                tvError.text = getString(R.string.hotel_share_empty_email_error)
                tvError.visibility = View.VISIBLE
            }
            isValidEmail(email) || isEmailWithoutProhibitSymbol(email) -> {
                valid = false
                tvError.text = getString(R.string.hotel_share_format_email_error)
                tvError.visibility = View.VISIBLE
            }
        }
        return valid
    }

    private fun isValidEmail(contactEmail: String): Boolean =
            Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                    !contactEmail.contains(".@") && !contactEmail.contains("@.")

    private fun isEmailWithoutProhibitSymbol(contactEmail: String): Boolean =
            !contactEmail.contains("+")

}