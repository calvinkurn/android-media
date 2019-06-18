package com.tokopedia.topupbills.telco.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoFavNumber
import com.tokopedia.topupbills.telco.data.constant.TelcoFavNumberType
import com.tokopedia.topupbills.telco.view.adapter.NumberListAdapter
import com.tokopedia.topupbills.telco.view.model.DigitalFavNumber
import java.util.*

/**
 * @author rizkyfadillah on 10/4/2017.
 */

class DigitalSearchNumberFragment : BaseDaggerFragment(), NumberListAdapter.OnClientNumberClickListener {

    private lateinit var rvNumberList: RecyclerView
    private lateinit var editTextSearchNumber: EditText
    private lateinit var btnClearNumber: Button

    private lateinit var numberListAdapter: NumberListAdapter

    private lateinit var clientNumbers: List<TelcoFavNumber>

    private lateinit var clientNumber: DigitalFavNumber
    private var number: String? = null

    private lateinit var callback: OnClientNumberClickListener

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onAttachActivity(context: Context) {
        callback = context as OnClientNumberClickListener
    }

    fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumber = arguments.getParcelable(ARG_PARAM_EXTRA_CLIENT_NUMBER)
            number = arguments.getString(ARG_PARAM_EXTRA_NUMBER)
            clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search_number_digital, container, false)
        initView(view)
        setViewListener()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    private fun initView(view: View) {
        rvNumberList = view.findViewById(R.id.recyclerview_number_list)
        editTextSearchNumber = view.findViewById(R.id.edittext_search_number)
        btnClearNumber = view.findViewById(R.id.btn_clear_number)

        setClientNumberInputType()

        if (TextUtils.isEmpty(number)) {
            btnClearNumber.visibility = View.GONE
        }

        btnClearNumber.setOnClickListener { editTextSearchNumber.setText("") }

        editTextSearchNumber.setText(number)
        editTextSearchNumber.setSelection(number!!.length)

        numberListAdapter = NumberListAdapter(this, clientNumbers)
        rvNumberList.layoutManager = LinearLayoutManager(activity)
        rvNumberList.adapter = numberListAdapter
    }

    private fun setClientNumberInputType() {
        if (clientNumber.type.equals(TelcoFavNumberType.TYPE_INPUT_TEL, ignoreCase = true) ||
                clientNumber.type.equals(TelcoFavNumberType.TYPE_INPUT_NUMERIC, ignoreCase = true)) {
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

        editTextSearchNumber.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                val orderClientNumber = findNumber(textView.text.toString(),
                        numberListAdapter.clientNumbers)
                orderClientNumber?.let {
                    if (it != null) {
                        callback.onClientNumberClicked(orderClientNumber)
                    } else {
                        it.clientNumber = textView.text.toString()
                        callback.onClientNumberClicked(it)
                    }
                    return@OnEditorActionListener true
                }
            }
            false
        })
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TelcoFavNumber>()
        if (!TextUtils.isEmpty(query) and !isContain(query, clientNumbers)) {
            searchClientNumbers.add(TelcoFavNumber(query))
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
        callback.onClientNumberClicked(orderClientNumber)
    }

    companion object {

        private val ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST"
        private val ARG_PARAM_EXTRA_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        private val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER"

        fun newInstance(clientNumber: DigitalFavNumber, number: String): Fragment {
            val fragment = DigitalSearchNumberFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumber)
            bundle.putString(ARG_PARAM_EXTRA_NUMBER, number)
            fragment.arguments = bundle
            return fragment
        }
    }

    interface OnClientNumberClickListener {
        fun onClientNumberClicked(orderClientNumber: TelcoFavNumber)
    }
}
