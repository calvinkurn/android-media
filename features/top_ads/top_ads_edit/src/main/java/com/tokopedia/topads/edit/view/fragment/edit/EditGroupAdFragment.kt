package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.GroupInfoResponse
import com.tokopedia.topads.edit.data.response.ResponseBidInfo
import com.tokopedia.topads.edit.data.response.ResponseGroupValidateName
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.BUDGET_LIMITED
import com.tokopedia.topads.edit.utils.Constants.DAILY_BUDGET
import com.tokopedia.topads.edit.utils.Constants.DEBOUNCE_CONST
import com.tokopedia.topads.edit.utils.Constants.GROUPKEY
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.GROUP_NAME
import com.tokopedia.topads.edit.utils.Constants.MULTIPLIER
import com.tokopedia.topads.edit.utils.Constants.NAME_EDIT
import com.tokopedia.topads.edit.utils.Constants.PRICE_BID
import com.tokopedia.topads.edit.utils.Constants.PRODUCT
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_edit_activity_edit_form_ad.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class EditGroupAdFragment : BaseDaggerFragment() {

    private var btnState: Boolean = true
    private var buttonStateCallback: SaveButtonStateCallBack? = null
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var minBid = 0
    private var maxBid = 0
    private var suggestBidPerClick = 0
    private var validation1 = true
    private var validation2 = true
    private var validation3 = true
    private var currentBudget = 0
    private var productId: MutableList<String> = mutableListOf()
    private var groupId: Int? = 0
    private var priceDaily = 0
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }
    private val sharedViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
    }

    companion object {

        fun newInstance(bundle: Bundle?): EditGroupAdFragment {
            val fragment = EditGroupAdFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return EditGroupAdFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_activity_edit_form_ad), container, false)
    }

    private fun getLatestBid() {
        val dummyId: MutableList<Int> = mutableListOf()
        productId.forEach {
            dummyId.add(it.toInt())
        }
        val suggestionsDefault = ArrayList<DataSuggestions>()
        suggestionsDefault.add(DataSuggestions(PRODUCT, dummyId))
        viewModel.getBidInfoDefault(suggestionsDefault, this::onBidSuccessSuggestion)
        viewModel.getGroupInfo(groupId.toString(), this::onSuccessGroupInfo, this::onError)
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        group_name.textFieldInput.setText(arguments?.getString(GROUPKEY))
        budget.textFieldInput.setText(data.priceBid.toString())
        priceDaily = data.priceDaily
        if (priceDaily != 0) {
            daily_budget.textFieldInput.setText(data.priceDaily.toString())
            radio2.isChecked = true
        } else {
            daily_budget.textFieldInput.setText((MULTIPLIER * data.priceBid).toString())
        }
        progressbar.visibility = View.GONE

    }

    private fun onError(it: Throwable) {
        it.printStackTrace()
    }

    private fun onBidSuccessSuggestion(data: List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) {
        suggestBidPerClick = data[0].suggestionBid
        minBid = data[0].minBid
        maxBid = data[0].maxBid
        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)

    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateName) {
        if (data.errors.isEmpty()) {
            group_name.setError(false)
            validation1 = true
            actionEnable()
            group_name.setMessage("")
        } else {
            onErrorGroupName(data.errors[0].detail)
        }
    }

    private fun onErrorGroupName(error: String) {
        if (group_name.textFieldInput.text.toString() != arguments?.getString(GROUPKEY)) {
            group_name.setError(true)
            validation1 = false
            actionEnable()
            group_name.setMessage(error)
            if (error == resources.getString(R.string.topads_edit_duplicate_group_name_error_wrong))
                group_name.setMessage(resources.getString(R.string.topads_edit_duplicate_group_name_error))
            else
                group_name.setMessage(error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupId = arguments?.getString(GROUP_ID)?.toInt()
        sharedViewModel.setGroupName(arguments?.getString(GROUPKEY) ?: " ")
        sharedViewModel.setGroupId(arguments?.getString(GROUP_ID)?.toInt() ?: 0)
        if (radio1.isChecked) {
            daily_budget.visibility = View.GONE

        }
        radio_group.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == radio1.id) {
                daily_budget.visibility = View.GONE
                validation3 = true
                actionEnable()
            } else {
                daily_budget.visibility = View.VISIBLE
            }
        }

        group_name.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                s?.let {
                    coroutineScope.launch {
                        delay(DEBOUNCE_CONST)
                        val text = s.toString().trim()
                        viewModel.validateGroup(text, this@EditGroupAdFragment::onSuccessGroupName)

                    }
                }
            }
        })

        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                currentBudget = number.toInt()
                val result = number.toInt()
                daily_budget.textFieldInput.setText((MULTIPLIER * result).toString())
                when {
                    minBid == 0 || maxBid == 0 -> {
                        return
                    }
                    result < minBid -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                        validation2 = false
                        actionEnable()
                    }
                    result > maxBid -> {
                        validation2 = false
                        actionEnable()
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }
                    else -> {
                        validation2 = true
                        actionEnable()
                        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestBidPerClick, false)
                    }
                }
            }

        })
        daily_budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                if (number < MULTIPLIER * currentBudget) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.min_bid_error), MULTIPLIER * currentBudget))
                    validation3 = false
                    actionEnable()

                } else {
                    validation3 = true
                    daily_budget.setError(false)
                    daily_budget.setMessage("")
                    actionEnable()
                }
            }
        })
    }

    private fun setMessageErrorField(error: String, bid: Int, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(String.format(error, bid))
    }

    private fun actionEnable() {
        btnState = validation1 == true && validation2 == true && validation3 == true
        buttonStateCallback?.setButtonState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel.getProuductIds().observe(viewLifecycleOwner, Observer {
            productId = it
            getLatestBid()
        })
    }

    override fun onAttach(context: Context) {
        if (context is SaveButtonStateCallBack) {
            buttonStateCallback = context
        }
        super.onAttach(context)
    }

    fun getButtonState(): Boolean {
        return btnState
    }

    fun sendData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        try {
            dataMap[GROUP_NAME] = group_name.textFieldInput.text.toString()
            dataMap[PRICE_BID] = Integer.parseInt(budget.textFieldInput.text.toString().replace(",", ""))
            dataMap[DAILY_BUDGET] = Integer.parseInt(daily_budget.textFieldInput.text.toString().replace(",", ""))
            dataMap[GROUP_ID] = groupId
            dataMap[BUDGET_LIMITED] = radio1.isChecked
            dataMap[NAME_EDIT] = group_name.textFieldInput.text.toString() != arguments?.getString(GROUPKEY)
        } catch (e: NumberFormatException) {
        }
        return dataMap
    }

    override fun onDetach() {
        buttonStateCallback = null
        super.onDetach()
    }
}
