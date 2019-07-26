package com.tokopedia.hotel.evoucher.presentation.widget

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.EditTextCompat
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.evoucher.presentation.adapter.HotelShareAsPdfAdapter
import kotlinx.android.synthetic.main.bottom_sheets_share_as_pdf.view.*

/**
 * @author by furqan on 16/05/19
 */
class HotelSharePdfBottomSheets : BottomSheets(), HotelShareAsPdfAdapter.ShareAsPdfListener {

    val emailList = mutableListOf<String>()
    lateinit var adapter: HotelShareAsPdfAdapter
    lateinit var listener: SharePdfBottomSheetsListener

    lateinit var recyclerView: RecyclerView
    lateinit var divider: View
    lateinit var evEmail: EditTextCompat
    lateinit var btnSend: ButtonCompat
    lateinit var evError: TextViewCompat
    lateinit var containerEmail: View

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_share_as_pdf

    override fun initView(view: View) {
        adapter = HotelShareAsPdfAdapter(emailList, this)

        with(view) {
            recyclerView = rv_email_list
            divider = divider_list
            evEmail = ev_email
            btnSend = btn_send_email
            evError = ev_error_email
            containerEmail = container_add_email

            evError.setTextColor(ContextCompat.getColor(context, R.color.red_500))

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            recyclerView.isNestedScrollingEnabled = false
            recyclerView.adapter = adapter

            evEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    configSendButton()
                    evError.visibility = View.GONE
                }
            })

            containerEmail.setOnClickListener {
                if (validateEmail(evEmail.text.toString().trim())) {

                    emailList.add(evEmail.text.toString().trim())
                    evEmail.setText("")
                    evError.visibility = View.GONE

                    configDivider()
                    configRecyclerView()
                    configSendButton()
                    updateHeight()

                    adapter.notifyDataSetChanged()
                }
            }

            btnSend.setOnClickListener {
                if ((evEmail.text ?: "").isNotEmpty() && validateEmail(evEmail.text.toString().trim())) {
                    emailList.add(evEmail.text.toString().trim())
                    evError.visibility = View.GONE
                }

                if (emailList.isNotEmpty() && ::listener.isInitialized) {
                    listener.sendPdf(emailList)
                }
            }

            configDivider()
            configRecyclerView()
        }
    }

    override fun title(): String = getString(R.string.hotel_share_as_pdf)

    override fun onDelete(email: String) {
        emailList.remove(email)
        adapter.notifyDataSetChanged()
        configDivider()
        configRecyclerView()
        configSendButton()
        updateHeight()
    }

    private fun validateEmail(email: String): Boolean {
        var valid = true
        when {
            email.isEmpty() -> {
                valid = false
                evError.text = getString(R.string.hotel_share_empty_email_error)
                evError.visibility = View.VISIBLE
            }
            !StringUtils.isValidEmail(email) || !isEmailWithoutProhibitSymbol(email) -> {
                valid = false
                evError.text = getString(R.string.hotel_share_format_email_error)
                evError.visibility = View.VISIBLE
            }
        }
        return valid
    }

    private fun isEmailWithoutProhibitSymbol(contactEmail: String): Boolean =
            !contactEmail.contains("+")

    private fun configDivider() {
        if (emailList.isEmpty()) {
            divider.visibility = View.GONE
        } else {
            divider.visibility = View.VISIBLE
        }
    }

    private fun configRecyclerView() {
        if (emailList.isEmpty()) {
            recyclerView.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun configSendButton() {
        btnSend.isEnabled = !((evEmail.text ?: "").isEmpty() && emailList.isEmpty())
    }

    interface SharePdfBottomSheetsListener {
        fun sendPdf(emailList: MutableList<String>)
    }

}