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
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.databinding.FragmentFavoriteNumberBinding
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsFavoriteNumberListAdapter
import com.tokopedia.common.topupbills.view.listener.OnFavoriteNumberClickListener
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import java.util.ArrayList

class TopupBillsFavoriteNumberFragment : BaseDaggerFragment(), OnFavoriteNumberClickListener {

    private lateinit var numberListAdapter: TopupBillsFavoriteNumberListAdapter
    private lateinit var clientNumbers: List<TopupBillsFavNumberItem>
    private lateinit var clientNumberType: String

    private var number: String = ""
    protected lateinit var inputNumberActionType: InputNumberActionType

    private var binding: FragmentFavoriteNumberBinding? = null

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return TopupBillsFavoriteNumberFragment::class.java.simpleName
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
        binding = FragmentFavoriteNumberBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.requestFocus()
        KeyboardHandler.showSoftKeyboard(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupArguments(arguments)
    }

    fun initView() {
        setClientNumberInputType()
        if (number.isNotEmpty()) {
            binding?.run {
                commonTopupbillsSearchNumberInputView.searchBarTextField.setText(number)
                commonTopupbillsSearchNumberInputView.searchBarTextField.setSelection(number.length)
                commonTopupbillsSearchNumberInputView.searchBarIcon.clearAnimation()
                commonTopupbillsSearchNumberInputView.searchBarIcon.post {
                    commonTopupbillsSearchNumberInputView.searchBarIcon.animate().scaleX(1f).scaleY(1f).start()
                }
            }
        }
        numberListAdapter = TopupBillsFavoriteNumberListAdapter(this, clientNumbers)

        binding?.commonTopupbillsSearchNumberInputView?.run {
            searchBarTextField.addTextChangedListener(getSearchTextWatcher)
            searchBarTextField.setOnEditorActionListener(getSearchNumberListener)
            searchBarTextField.onFocusChangeListener = getFocusChangeListener
            clearListener = { onSearchReset() }
            searchBarTextField.imeOptions = EditorInfo.IME_ACTION_DONE
        }
        binding?.commonTopupbillsFavoriteNumberRv?.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = numberListAdapter
        }
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
        binding?.commonTopupbillsSearchNumberInputView
                ?.searchBarTextField?.inputType = when (clientNumberType.toLowerCase()) {
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

        searchClientNumbers.addAll(clientNumbers.filter {
            it.clientNumber.contains(query)
        })

        numberListAdapter.setNumbers(searchClientNumbers)
        numberListAdapter.notifyDataSetChanged()
    }

    private fun isContain(number: String, clientNumbers: List<TopupBillsFavNumberItem>): Boolean {
        return clientNumbers.any { it.clientNumber.equals(number, ignoreCase = true) }
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

    fun onSearchSubmitted(text: String?) {
        //do nothing
    }

    fun onSearchDone(text: String) {
        checkMatchesFavoriteNumber(text)
    }

    fun onSearchReset() {
        binding?.commonTopupbillsSearchNumberInputView?.searchBarTextField?.setText("")
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    override fun onFavoriteNumberClick(clientNumber: TopupBillsFavNumberItem) {
        if (!::inputNumberActionType.isInitialized || inputNumberActionType != InputNumberActionType.CONTACT) {
            val checkNumber = findNumber(clientNumber.clientNumber, numberListAdapter.clientNumbers)
            inputNumberActionType = if (checkNumber != null && checkNumber.isFavorite) {
                InputNumberActionType.FAVORITE
            } else {
                InputNumberActionType.MANUAL
            }
        }

        activity?.run {
            val intent = Intent()
            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, clientNumber)
            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, inputNumberActionType.ordinal)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun checkMatchesFavoriteNumber(textNumber: String) {
        val matchedFavoriteNumber = numberListAdapter.clientNumbers.findLast { it.clientNumber == textNumber }
        matchedFavoriteNumber?.let {
            onFavoriteNumberClick(it)
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
            val fragment = TopupBillsFavoriteNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_NUMBER, number)
            bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            fragment.arguments = bundle
            return fragment
        }
    }
}
