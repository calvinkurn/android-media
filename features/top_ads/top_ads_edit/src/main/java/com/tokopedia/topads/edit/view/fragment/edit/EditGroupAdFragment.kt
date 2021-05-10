package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.BUDGET_LIMITED
import com.tokopedia.topads.edit.utils.Constants.DAILY_BUDGET
import com.tokopedia.topads.edit.utils.Constants.DEBOUNCE_CONST
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.GROUP_NAME
import com.tokopedia.topads.edit.utils.Constants.IS_DATA_CHANGE
import com.tokopedia.topads.edit.utils.Constants.MAXIMUM_LIMIT
import com.tokopedia.topads.edit.utils.Constants.MULTIPLIER
import com.tokopedia.topads.edit.utils.Constants.NAME_EDIT
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_edit_activity_edit_form_ad.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val AUTOBID_DEFUALT_BUDGET = 16000
class EditGroupAdFragment : BaseDaggerFragment() {

    private var btnState: Boolean = true
    private var buttonStateCallback: SaveButtonStateCallBack? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var validation1 = true
    private var validation2 = true
    private var validation3 = true
    private var currentBudget = 0
    private var groupId: Int? = 0
    private var priceDaily = 0
    private var groupName: String = ""
    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(EditFormDefaultViewModel::class.java)
    }
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }
    private var initialDailyBudget = 0


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


    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        groupName = data.groupName
        group_name?.textFieldInput?.setText(data.groupName)
        sharedViewModel.setBudget(data.priceBid)
        priceDaily = data.priceDaily
        if (priceDaily != 0) {
            toggle?.isChecked = true
            daily_budget?.visible()
            daily_budget?.textFieldInput?.setText(data.priceDaily.toString())
        } else {
            daily_budget?.gone()
            setCurrentDailyBudget((MULTIPLIER * data.priceBid).toString())
        }
        progressbar?.visibility = View.GONE
        saveInitialChoices()
    }

    private fun getCurrentTitle() = group_name?.textFieldInput?.text?.toString()

    private fun getCurrentDailyBudget(): Int {
        return daily_budget?.textFieldInput?.text.toString().removeCommaRawString().toInt()
    }

    private fun setCurrentDailyBudget(data: String) {
        daily_budget?.textFieldInput?.setText(data)
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateName) {
        if (data.errors.isEmpty()) {
            group_name?.setError(false)
            validation1 = true
            actionEnable()
            group_name.setMessage("")
        } else {
            onErrorGroupName(data.errors[0].detail)
        }
    }

    private fun onErrorGroupName(error: String) {
        if (getCurrentTitle() != groupName) {
            group_name?.setError(true)
            validation1 = false
            actionEnable()
            group_name?.setMessage(error)
            if (error == resources.getString(R.string.topads_edit_duplicate_group_name_error_wrong))
                group_name?.setMessage(resources.getString(R.string.topads_edit_duplicate_group_name_error))
            else
                group_name?.setMessage(error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getDailyBudget().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            setCurrentDailyBudget(it.toString())
            currentBudget = it
        })
        if (arguments?.getString(GROUP_ID)?.isNotEmpty()!!) {
            groupId = arguments?.getString(GROUP_ID)?.toInt()
            sharedViewModel.setGroupId(arguments?.getString(GROUP_ID)?.toInt() ?: 0)
        }
        toggle.setOnCheckedChangeListener { buttonView, isChecked ->
            if (toggle?.isChecked == true) {
                daily_budget?.visibility = View.VISIBLE
            } else {
                daily_budget?.visibility = View.GONE
                validation3 = true
                actionEnable()
            }
        }

        group_name?.textFieldInput?.addTextChangedListener(object : TextWatcher {
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

        daily_budget?.textFieldInput?.addTextChangedListener(object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                when {
                    number < currentBudget -> {
                        daily_budget?.setError(true)
                        daily_budget?.setMessage(String.format(getString(R.string.min_bid_error), currentBudget))
                        validation3 = false
                        actionEnable()

                    }
                    number > MAXIMUM_LIMIT.removeCommaRawString().toDouble() -> {
                        daily_budget?.setError(true)
                        daily_budget?.setMessage(String.format(getString(R.string.topads_common_maximum_daily_budget), MAXIMUM_LIMIT))
                        validation3 = false
                        actionEnable()
                    }
                    else -> {
                        validation3 = true
                        daily_budget?.setError(false)
                        daily_budget?.setMessage("")
                        actionEnable()
                    }
                }
            }
        })
    }

    private fun saveInitialChoices() {
        initialDailyBudget = getCurrentDailyBudget()
    }

    private fun actionEnable() {
        btnState = validation1 == true && validation2 == true && validation3 == true
        buttonStateCallback?.setButtonState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getGroupInfo(groupId.toString(), this::onSuccessGroupInfo)
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
            dataMap[IS_DATA_CHANGE] = checkDataChanged()
            dataMap[GROUP_NAME] = getCurrentTitle()
            dataMap[DAILY_BUDGET] = getCurrentDailyBudget()
            dataMap[GROUP_ID] = groupId
            dataMap[BUDGET_LIMITED] = toggle?.isChecked
            dataMap[NAME_EDIT] = getCurrentTitle() != groupName
        } catch (e: NumberFormatException) {
        }
        return dataMap
    }

    private fun checkDataChanged(): Boolean {
        return initialDailyBudget != getCurrentDailyBudget() || groupName != getCurrentTitle()
    }

    override fun onDetach() {
        buttonStateCallback = null
        super.onDetach()
    }
}
