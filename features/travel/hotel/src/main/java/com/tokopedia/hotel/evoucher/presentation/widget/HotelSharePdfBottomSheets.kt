package com.tokopedia.hotel.evoucher.presentation.widget

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.HotelStringUtils
import com.tokopedia.hotel.databinding.BottomSheetsShareAsPdfBinding
import com.tokopedia.hotel.evoucher.presentation.adapter.HotelShareAsPdfAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by furqan on 16/05/19
 */
class HotelSharePdfBottomSheets : BottomSheetUnify(), HotelShareAsPdfAdapter.ShareAsPdfListener {

    private var binding by autoClearedNullable<BottomSheetsShareAsPdfBinding>()

    private val emailList = mutableListOf<String>()
    lateinit var adapter: HotelShareAsPdfAdapter
    lateinit var listener: SharePdfBottomSheetsListener

    init {
        isFullpage = false
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initBottomSheet() {
        binding = BottomSheetsShareAsPdfBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    @SuppressLint("PII Data Exposure")
    fun initView() {
        adapter = HotelShareAsPdfAdapter(emailList, this)

        binding?.run {
            evErrorEmail.setTextColor(ContextCompat.getColor(requireContext(), unifyprinciplesR.color.Unify_RN500))

            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvEmailList.layoutManager = layoutManager
            rvEmailList.setHasFixedSize(true)
            rvEmailList.isNestedScrollingEnabled = false
            rvEmailList.adapter = adapter

            evEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    configSendButton()
                    evErrorEmail.visibility = View.GONE
                }
            })

            containerAddEmail.setOnClickListener {
                if (validateEmail(evEmail.text.toString().trim())) {

                    emailList.add(evEmail.text.toString().trim())
                    evEmail.setText("")
                    evErrorEmail.visibility = View.GONE

                    configDivider()
                    configRecyclerView()
                    configSendButton()

                    adapter.notifyDataSetChanged()
                }
            }

            btnSendEmail.setOnClickListener {
                if ((evEmail.text
                                ?: "").isNotEmpty() && validateEmail(evEmail.text.toString().trim())) {
                    emailList.add(evEmail.text.toString().trim())
                    evErrorEmail.visibility = View.GONE
                }

                if (emailList.isNotEmpty() && ::listener.isInitialized) {
                    listener.sendPdf(emailList)
                }
            }

            configDivider()
            configRecyclerView()
            setTitle(getString(R.string.hotel_share_as_pdf))
        }
    }

    @SuppressLint("PII Data Exposure")
    override fun onDelete(email: String) {
        emailList.remove(email)
        adapter.notifyDataSetChanged()
        configDivider()
        configRecyclerView()
        configSendButton()
    }

    @SuppressLint("PII Data Exposure")
    private fun validateEmail(email: String): Boolean {
        var valid = true
        when {
            email.isEmpty() -> {
                valid = false
                binding?.evErrorEmail?.text = getString(R.string.hotel_share_empty_email_error)
                binding?.evErrorEmail?.visibility = View.VISIBLE
            }
            !HotelStringUtils.isValidEmail(email) || !isEmailWithoutProhibitSymbol(email) -> {
                valid = false
                binding?.evErrorEmail?.text = getString(R.string.hotel_share_format_email_error)
                binding?.evErrorEmail?.visibility = View.VISIBLE
            }
        }
        return valid
    }

    private fun isEmailWithoutProhibitSymbol(contactEmail: String): Boolean =
            !contactEmail.contains("+")

    @SuppressLint("PII Data Exposure")
    private fun configDivider() {
        binding?.run {
            if (emailList.isEmpty()) {
                dividerList.visibility = View.GONE
            } else {
                dividerList.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun configRecyclerView() {
        binding?.run {
            if (emailList.isEmpty()) {
                rvEmailList.visibility = View.GONE
            } else {
                rvEmailList.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private fun configSendButton() {
        binding?.run {
            btnSendEmail.isEnabled = !((evEmail.text ?: "").isEmpty() && emailList.isEmpty())
        }
    }

    interface SharePdfBottomSheetsListener {
        fun sendPdf(emailList: MutableList<String>)
    }

}
