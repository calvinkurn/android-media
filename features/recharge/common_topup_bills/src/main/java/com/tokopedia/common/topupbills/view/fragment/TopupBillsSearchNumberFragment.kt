package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
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
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
import com.tokopedia.common.topupbills.view.adapter.NumberListAdapter
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.unifycomponents.SearchBarUnify
import java.util.*

open class TopupBillsSearchNumberFragment : BaseDaggerFragment(), NumberListAdapter.OnClientNumberClickListener {

    private lateinit var numberListAdapter: NumberListAdapter
    private lateinit var clientNumbers: List<TopupBillsFavNumberItem>
    private lateinit var clientNumberType: String

    protected lateinit var searchInputNumber: SearchBarUnify
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
            clientNumbers = arguments.getParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST)
                    ?: listOf()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search_number, container, false)
        searchInputNumber = view.findViewById(R.id.topupbills_search_input_view_number)
        favNumberRecyclerView = view.findViewById(R.id.topupbills_search_number_rv)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        searchInputNumber.searchBarTextField.requestFocus()
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    open fun initView() {
        setClientNumberInputType()
        if (number.isNotEmpty()) {
            searchInputNumber.searchBarTextField.setText(number)
            searchInputNumber.searchBarTextField.setSelection(number.length)
            searchInputNumber.searchBarIcon.clearAnimation()
            searchInputNumber.searchBarIcon.post {
                searchInputNumber.searchBarIcon.animate().scaleX(1f).scaleY(1f).start()
            }
        }
        searchInputNumber.searchBarTextField.addTextChangedListener(getSearchTextWatcher)
        searchInputNumber.searchBarTextField.setOnEditorActionListener(getSearchNumberListener)
        searchInputNumber.searchBarTextField.onFocusChangeListener = getFocusChangeListener
        searchInputNumber.clearListener  = { onSearchReset() }
        searchInputNumber.searchBarTextField.imeOptions = EditorInfo.IME_ACTION_DONE

        numberListAdapter = NumberListAdapter(this, clientNumbers)
        favNumberRecyclerView.layoutManager = LinearLayoutManager(activity)
        favNumberRecyclerView.adapter = numberListAdapter

        while (favNumberRecyclerView.itemDecorationCount > 0) favNumberRecyclerView.removeItemDecorationAt(0)
        favNumberRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    private val getFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) inputNumberActionType = InputNumberActionType.MANUAL
    }

    private val getSearchNumberListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(textView: TextView, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchSubmitted(textView.text.toString())
                return true
            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardHandler.hideSoftKeyboard(activity)
                onSearchDone(textView.text.toString())
                return true
            }
            return false
        }
    }

    private val getSearchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            text?.let { filterData(text.toString()) }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
           //do nothing
        }
    }

    private fun setClientNumberInputType() {
        searchInputNumber.searchBarTextField.inputType = when (clientNumberType.toLowerCase()) {
            ClientNumberType.TYPE_INPUT_TEL -> InputType.TYPE_CLASS_PHONE
            ClientNumberType.TYPE_INPUT_NUMERIC -> InputType.TYPE_CLASS_NUMBER
            ClientNumberType.TYPE_INPUT_ALPHANUMERIC -> InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        }
    }

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

    open fun onSearchSubmitted(text: String?) {
        //do nothing
    }

    open fun onSearchDone(text: String) {
        //do nothing
    }

    open fun onSearchReset() {
        searchInputNumber.searchBarTextField.setText("")
        KeyboardHandler.hideSoftKeyboard(activity)
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

    fun pickOrderClientNumber(textNumber: String) {
        val orderClientNumber = numberListAdapter.clientNumbers.findLast { it.clientNumber == textNumber }
        orderClientNumber?.let {
            onClientNumberClicked(it)
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
