package com.tokopedia.topupbills.telco.view.fragment

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.DigitalTopupAnalytics
import com.tokopedia.topupbills.covertContactUriToContactData
import com.tokopedia.topupbills.telco.data.TelcoFavNumber
import com.tokopedia.topupbills.telco.view.adapter.NumberListAdapter
import com.tokopedia.topupbills.telco.view.di.DigitalTopupInstance
import kotlinx.android.synthetic.main.fragment_search_number_telco.*
import java.util.*
import javax.inject.Inject

class DigitalSearchNumberFragment : BaseDaggerFragment(), NumberListAdapter.OnClientNumberClickListener {

    private lateinit var callback: OnClientNumberClickListener
    private lateinit var numberListAdapter: NumberListAdapter
    private lateinit var clientNumbers: List<TelcoFavNumber>
    private lateinit var clientNumberType: String

    private var number: String? = null
    private lateinit var inputNumberActionType: InputNumberActionType

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper
    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics


    override fun initInjector() {
        activity?.let {
            val digitalTopupComponent = DigitalTopupInstance.getComponent(it.application)
            digitalTopupComponent.inject(this)
        }
    }

    override fun getScreenName(): String? {
        return DigitalSearchNumberFragment::class.java.simpleName
    }

    override fun onAttachActivity(context: Context) {
        callback = context as OnClientNumberClickListener
    }

    fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER)
            number = arguments.getString(ARG_PARAM_EXTRA_NUMBER)
            clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search_number_telco, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setViewListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    private fun initView() {
        setClientNumberInputType()

        if (TextUtils.isEmpty(number)) {
            btnClearNumber.visibility = View.GONE
        }

        btnClearNumber.setOnClickListener {
            topupAnalytics.eventClearInputNumber()
            editTextSearchNumber.setText("")
        }
        btnContactPicker.setOnClickListener {
            inputNumberActionType = InputNumberActionType.CONTACT
            navigateContact()
        }

        editTextSearchNumber.setText(number)
        editTextSearchNumber.setSelection(number!!.length)

        numberListAdapter = NumberListAdapter(this, clientNumbers)
        rvNumberList.layoutManager = LinearLayoutManager(activity)
        rvNumberList.adapter = numberListAdapter
    }

    private fun setClientNumberInputType() {
        if (clientNumberType.equals(ClientNumberType.TYPE_INPUT_TEL, ignoreCase = true) ||
                clientNumberType.equals(ClientNumberType.TYPE_INPUT_NUMERIC, ignoreCase = true)) {
            editTextSearchNumber.inputType = InputType.TYPE_CLASS_NUMBER
            editTextSearchNumber.keyListener = DigitsKeyListener.getInstance("0123456789")
        } else {
            editTextSearchNumber.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }

    private fun setViewListener() {
        editTextSearchNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                    btnClearNumber.visibility = View.VISIBLE
                } else {
                    btnClearNumber.visibility = View.GONE
                }
                filterData(s.toString())
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        editTextSearchNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) inputNumberActionType = InputNumberActionType.MANUAL
        }

        editTextSearchNumber.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                var orderClientNumber = findNumber(textView.text.toString(), numberListAdapter.clientNumbers)
                if (orderClientNumber != null && orderClientNumber.isFavorite) {
                    inputNumberActionType = InputNumberActionType.FAVORITE
                    callback.onClientNumberClicked(orderClientNumber, inputNumberActionType)
                } else {
                    inputNumberActionType = InputNumberActionType.MANUAL
                    orderClientNumber = TelcoFavNumber(clientNumber = textView.text.toString())
                    callback.onClientNumberClicked(orderClientNumber, inputNumberActionType)
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TelcoFavNumber>()
        if (!TextUtils.isEmpty(query) and !isContain(query, clientNumbers)) {
            searchClientNumbers.add(TelcoFavNumber(query, isFavorite = false))
        }
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.contains(query)) {
                searchClientNumbers.add(orderClientNumber)
            }
        }
        numberListAdapter.setNumbers(searchClientNumbers)
        numberListAdapter.notifyDataSetChanged()
    }

    private fun isContain(number: String, clientNumbers: List<TelcoFavNumber>): Boolean {
        var found = false
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.equals(number, ignoreCase = true)) {
                found = true
                break
            }
        }
        return found
    }

    private fun findNumber(number: String, clientNumbers: List<TelcoFavNumber>): TelcoFavNumber? {
        var foundClientNumber: TelcoFavNumber? = null
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.equals(number, ignoreCase = true)) {
                foundClientNumber = orderClientNumber
                break
            }
        }
        return foundClientNumber
    }

    override fun onClientNumberClicked(orderClientNumber: TelcoFavNumber) {
        if (!::inputNumberActionType.isInitialized || inputNumberActionType != InputNumberActionType.CONTACT) {
            val checkNumber = findNumber(orderClientNumber.clientNumber, numberListAdapter.clientNumbers)
            if (checkNumber != null && checkNumber.isFavorite) {
                inputNumberActionType = InputNumberActionType.FAVORITE
            } else {
                inputNumberActionType = InputNumberActionType.MANUAL
            }
        }
        callback.onClientNumberClicked(orderClientNumber, inputNumberActionType)
    }

    fun navigateContact() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.let {
                permissionCheckerHelper.checkPermission(it,
                        PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper.onPermissionDenied(it, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                            }

                            override fun onPermissionGranted() {
                                openContactPicker()
                            }
                        }, "")
            }
        } else {
            openContactPicker()
        }
    }

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            NetworkErrorHelper.showSnackbar(activity,
                    getString(R.string.error_message_contact_not_found))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        val contactURI = data.data
                        val contact = contactURI.covertContactUriToContactData(it.contentResolver)
                        editTextSearchNumber.setText(contact.contactNumber)
                    }
                }
            }
        }
    }

    enum class InputNumberActionType {
        MANUAL, CONTACT, FAVORITE
    }

    companion object {

        private val ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST"
        private val ARG_PARAM_EXTRA_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        private val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER"

        private val REQUEST_CODE_CONTACT_PICKER = 75

        fun newInstance(clientNumberType: String, number: String,
                        numberList: List<TelcoFavNumber>): Fragment {
            val fragment = DigitalSearchNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_NUMBER, number)
            bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }

    interface OnClientNumberClickListener {
        fun onClientNumberClicked(orderClientNumber: TelcoFavNumber, inputNumberActionType: InputNumberActionType)
    }
}
