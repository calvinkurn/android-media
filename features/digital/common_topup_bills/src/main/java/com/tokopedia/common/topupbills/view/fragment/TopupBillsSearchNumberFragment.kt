package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.InputType
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
import com.tokopedia.common.topupbills.view.adapter.NumberListAdapter
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.design.text.SearchInputView
import kotlinx.android.synthetic.main.view_digital_component_list.*
import java.util.*
import javax.inject.Inject

open class TopupBillsSearchNumberFragment : BaseDaggerFragment(),
        NumberListAdapter.OnClientNumberClickListener,
        SearchInputView.Listener,
        SearchInputView.FocusChangeListener,
        SearchInputView.ResetListener {

    private lateinit var numberListAdapter: NumberListAdapter
    private lateinit var clientNumbers: List<TopupBillsFavNumberItem>
    private lateinit var clientNumberType: String

    protected lateinit var searchInputNumber: SearchInputView
    protected lateinit var favNumberRecyclerView: RecyclerView

    private var number: String = ""
    protected lateinit var inputNumberActionType: InputNumberActionType

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return TopupBillsSearchNumberFragment::class.java.simpleName
    }

    private fun setupArguments(arguments: Bundle?) {
        arguments?.run {
            clientNumberType = arguments.getString(ARG_PARAM_EXTRA_CLIENT_NUMBER, "")
            number = arguments.getString(ARG_PARAM_EXTRA_NUMBER, "")
            clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST) ?: listOf()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_number, container, false)
        searchInputNumber = view.findViewById(R.id.siv_search_number)
        favNumberRecyclerView = view.findViewById(R.id.rvNumberList)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        setViewListener()
        searchInputNumber.searchTextView.requestFocus()
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    open fun initView() {
        setClientNumberInputType()
        if (number.isNotEmpty()) {
            searchInputNumber.searchTextView.setText(number)
            searchInputNumber.searchTextView.setSelection(number.length)
        }
        searchInputNumber.setListener(this)
        searchInputNumber.setFocusChangeListener(this)
        searchInputNumber.setResetListener(this)

        numberListAdapter = NumberListAdapter(this, clientNumbers)
        favNumberRecyclerView.layoutManager = LinearLayoutManager(activity)
        favNumberRecyclerView.adapter = numberListAdapter

        while (favNumberRecyclerView.itemDecorationCount > 0) favNumberRecyclerView.removeItemDecorationAt(0)
        favNumberRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private fun setClientNumberInputType() {
        if (clientNumberType.equals(ClientNumberType.TYPE_INPUT_TEL, ignoreCase = true) ||
                clientNumberType.equals(ClientNumberType.TYPE_INPUT_NUMERIC, ignoreCase = true)) {
            searchInputNumber.searchTextView.inputType = InputType.TYPE_CLASS_NUMBER
            searchInputNumber.searchTextView.keyListener = DigitsKeyListener.getInstance("0123456789")
        } else {
            searchInputNumber.searchTextView.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }

//    private fun setViewListener() {
//        searchInputNumber.searchTextView.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                var orderClientNumber = findNumber(textView.text.toString(), numberListAdapter.clientNumbers)
//                if (orderClientNumber != null && orderClientNumber.isFavorite) {
//                    inputNumberActionType = InputNumberActionType.FAVORITE
//                    callback.onClientNumberClicked(orderClientNumber, inputNumberActionType)
//                } else {
//                    inputNumberActionType = InputNumberActionType.MANUAL
//                    orderClientNumber = TopupBillsFavNumberItem(clientNumber = textView.text.toString())
//                    callback.onClientNumberClicked(orderClientNumber, inputNumberActionType)
//                }
//                return@OnEditorActionListener true
//            }
//            false
//        })
//    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TopupBillsFavNumberItem>()
        if (!TextUtils.isEmpty(query) and !isContain(query, clientNumbers)) {
            searchClientNumbers.add(TopupBillsFavNumberItem(query, isFavorite = false))
        }
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.contains(query)) {
                searchClientNumbers.add(orderClientNumber)
            }
        }
        numberListAdapter.setNumbers(searchClientNumbers)
        numberListAdapter.notifyDataSetChanged()
    }

    private fun isContain(number: String, clientNumbers: List<TopupBillsFavNumberItem>): Boolean {
        var found = false
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.equals(number, ignoreCase = true)) {
                found = true
                break
            }
        }
        return found
    }

    private fun findNumber(number: String, clientNumbers: List<TopupBillsFavNumberItem>): TopupBillsFavNumberItem? {
        var foundClientNumber: TopupBillsFavNumberItem? = null
        for (orderClientNumber in clientNumbers) {
            if (orderClientNumber.clientNumber.equals(number, ignoreCase = true)) {
                foundClientNumber = orderClientNumber
                break
            }
        }
        return foundClientNumber
    }

    override fun onSearchSubmitted(text: String?) {

    }

    override fun onSearchTextChanged(text: String?) {
        text?.let { filterData(it) }
    }

    override fun onFocusChanged(hasFocus: Boolean) {
        if (hasFocus) inputNumberActionType = InputNumberActionType.MANUAL
    }

    override fun onSearchReset() {

    }

    override fun onClientNumberClicked(orderClientNumber: TopupBillsFavNumberItem) {
        if (!::inputNumberActionType.isInitialized || inputNumberActionType != InputNumberActionType.CONTACT) {
            val checkNumber = findNumber(orderClientNumber.clientNumber, numberListAdapter.clientNumbers)
            inputNumberActionType = if (checkNumber != null && checkNumber.isFavorite) {
                InputNumberActionType.FAVORITE
            } else {
                InputNumberActionType.MANUAL
            }
        }

        activity?.run {
            val intent = Intent()
            intent.putExtra(EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
            intent.putExtra(EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, inputNumberActionType.ordinal)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    enum class InputNumberActionType {
        MANUAL, CONTACT, FAVORITE, LATEST_TRANSACTION, CONTACT_HOMEPAGE
    }

    companion object {

        const val ARG_PARAM_EXTRA_NUMBER_LIST = "ARG_PARAM_EXTRA_NUMBER_LIST"
        const val ARG_PARAM_EXTRA_NUMBER = "ARG_PARAM_EXTRA_NUMBER"
        const val ARG_PARAM_EXTRA_CLIENT_NUMBER = "ARG_PARAM_EXTRA_CLIENT_NUMBER"

        fun newInstance(clientNumberType: String, number: String,
                        numberList: List<TopupBillsFavNumberItem>): Fragment {
            val fragment = TopupBillsSearchNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_NUMBER, number)
            bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }
}
