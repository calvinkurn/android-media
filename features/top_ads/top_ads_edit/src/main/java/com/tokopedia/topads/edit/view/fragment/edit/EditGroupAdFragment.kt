package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_NAME
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.BUDGET_LIMITED
import com.tokopedia.topads.edit.utils.Constants.DAILY_BUDGET
import com.tokopedia.topads.edit.utils.Constants.DEBOUNCE_CONST
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.utils.Constants.IS_DATA_CHANGE
import com.tokopedia.topads.edit.utils.Constants.MAXIMUM_LIMIT
import com.tokopedia.topads.edit.utils.Constants.NAME_EDIT
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

private const val AUTOBID_DEFUALT_BUDGET = 16000

class EditGroupAdFragment : BaseDaggerFragment() {

    private var progressbar: LoaderUnify? = null
    private var txtGroupName: TextFieldUnify? = null
    private var toggle: SwitchUnify? = null
    private var dailyBudget: TextFieldUnify? = null

    private var btnState: Boolean = true
    private var buttonStateCallback: SaveButtonStateCallBack? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var validation1 = true
    private var validation2 = true
    private var validation3 = true
    private var currentBudget = 0
    private var groupId: String? = "0"
    private var priceDaily = 0.0F
    private var groupName: String = ""
    private var currentAutoBidState = ""
    private var initialToggleState = false
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
    private val job = SupervisorJob()
    val coroutineScope = CoroutineScope(Dispatchers.Main + job)

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            context?.resources?.getLayout(R.layout.topads_edit_activity_edit_form_ad), container, false
        )
        progressbar = view.findViewById(R.id.progressbar)
        txtGroupName = view.findViewById(R.id.group_name)
        toggle = view.findViewById(R.id.toggle)
        dailyBudget = view.findViewById(R.id.daily_budget)
        return view
    }

    private fun onSuccessGroupInfo(data: GroupInfoResponse.TopAdsGetPromoGroup.Data) {
        groupName = data.groupName
        txtGroupName?.textFieldInput?.setText(data.groupName)
        val bidSettingsList: MutableList<TopAdsBidSettingsModel> = mutableListOf()
        data.bidSettings?.forEach {
            val topAdsBidSettingsModel = TopAdsBidSettingsModel()
            topAdsBidSettingsModel.bidType = it.bidType
            topAdsBidSettingsModel.priceBid = it.priceBid
            bidSettingsList.add(topAdsBidSettingsModel)
        }
        sharedViewModel.setBidSettings(bidSettingsList)
        priceDaily = data.daiyBudget
        if (priceDaily != 0.0F) {
            dailyBudget?.visible()
            setCurrentDailyBudget((priceDaily).toInt().toString())
            toggle?.setOnCheckedChangeListener(null)
            toggle?.isChecked = true
            setToggleCheckedListener()
        } else {
            dailyBudget?.gone()
        }
        progressbar?.visibility = View.GONE
        saveInitialChoices()
    }

    private fun getCurrentTitle() = txtGroupName?.textFieldInput?.text?.toString()

    private fun getCurrentDailyBudget(): Int {
        return dailyBudget?.textFieldInput?.text.toString().removeCommaRawString().toIntOrZero()
    }

    private fun setCurrentDailyBudget(data: String) {
        dailyBudget?.textFieldInput?.setText(data)
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateNameV2) {
        if (data.errors.isEmpty()) {
            txtGroupName?.setError(false)
            validation1 = true
            actionEnable()
            txtGroupName?.setMessage("")
        } else {
            onErrorGroupName(data.errors[0].detail)
        }
    }

    private fun onErrorGroupName(error: String) {
        if (getCurrentTitle() != groupName) {
            txtGroupName?.setError(true)
            validation1 = false
            actionEnable()
            txtGroupName?.setMessage(error)
            if (error == context?.resources?.getString(R.string.topads_edit_duplicate_group_name_error_wrong))
                txtGroupName?.setMessage(context?.resources?.getString(R.string.topads_edit_duplicate_group_name_error) ?: "")
            else
                txtGroupName?.setMessage(error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        if (arguments?.getString(GROUP_ID)?.isNotEmpty()!!) {
            groupId = arguments?.getString(GROUP_ID)
            sharedViewModel.setGroupId(arguments?.getString(GROUP_ID)?.toInt() ?: 0)
        }
        setToggleCheckedListener()

        txtGroupName?.textFieldInput?.imeOptions = EditorInfo.IME_ACTION_DONE
        txtGroupName?.textFieldInput?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Utils.dismissKeyboard(context, view)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        setGroupNameWatcher()
        setDailyBudgetWatcher()
    }

    private fun setToggleCheckedListener () {
        toggle?.setOnCheckedChangeListener { _, _ ->
            if (toggle?.isChecked == true) {
                dailyBudget?.visibility = View.VISIBLE
                checkErrorsDailyBudgetTF(getCurrentDailyBudget().toDouble())
            } else {
                dailyBudget?.visibility = View.GONE
                validation3 = true
                actionEnable()
            }
        }
    }

    private fun updateValidation3IfDailyBudgetTFVisible(state: Boolean) = dailyBudget?.let {
        if(!it.isVisible) return@let
        validation3 = false
    }

    private fun setDailyBudgetWatcher() {
        dailyBudget?.textFieldInput?.let {
            it.addTextChangedListener(object : NumberTextWatcher(it, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    checkErrorsDailyBudgetTF(number)
                }
            })
        }
    }

    private fun checkErrorsDailyBudgetTF(number : Double) = when {
        number < AUTOBID_DEFUALT_BUDGET && currentAutoBidState.isNotEmpty() -> {
            dailyBudget?.setError(true)
            dailyBudget?.setMessage(
                String.format(
                    getString(com.tokopedia.topads.common.R.string.angarran_harrian_min_bid_error),
                    Utils.convertToCurrency(AUTOBID_DEFUALT_BUDGET.toLong())
                )
            )
            updateValidation3IfDailyBudgetTFVisible(false)
            actionEnable()
        }
        number < currentBudget && currentAutoBidState.isEmpty() -> {
            dailyBudget?.setError(true)
            dailyBudget?.setMessage(
                String.format(
                    getString(com.tokopedia.topads.common.R.string.topads_common_minimum_daily_budget),
                    currentBudget
                )
            )
            updateValidation3IfDailyBudgetTFVisible(false)
            actionEnable()
        }
        number > MAXIMUM_LIMIT.removeCommaRawString().toDouble() -> {
            dailyBudget?.setError(true)
            dailyBudget?.setMessage(
                String.format(
                    getString(com.tokopedia.topads.common.R.string.topads_common_maximum_daily_budget),
                    MAXIMUM_LIMIT
                )
            )
            updateValidation3IfDailyBudgetTFVisible(false)
            actionEnable()
        }
        else -> {
            validation3 = true
            dailyBudget?.setError(false)
            dailyBudget?.setMessage("")
            actionEnable()
        }
    }

    private fun setGroupNameWatcher() {

        txtGroupName?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    coroutineScope.launch {
                        delay(DEBOUNCE_CONST)
                        if (activity != null && isAdded) {
                            val text = s.toString().trim()
                            viewModel.validateGroup(
                                text,
                                this@EditGroupAdFragment::onSuccessGroupName
                            )
                        }

                    }
                }
            }
        })

    }

    private fun setObservers() {
        sharedViewModel.getMaxBudget().observe(viewLifecycleOwner, {
            setCurrentDailyBudget(it.toString())
            currentBudget = it
        })
        sharedViewModel.getAutoBidStatus().observe(viewLifecycleOwner, {
            currentAutoBidState = it
            if (currentAutoBidState.isNotEmpty()) {
                setCurrentDailyBudget(AUTOBID_DEFUALT_BUDGET.toString())
                actionEnable()
            }
        })
    }

    private fun saveInitialChoices() {
        initialDailyBudget = getCurrentDailyBudget()
        initialToggleState = toggle?.isChecked ?: false
    }

    private fun actionEnable() {
        btnState = validation1 == true && validation2 == true && validation3 == true
        buttonStateCallback?.setButtonState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getGroupInfo(groupId ?: "0", this::onSuccessGroupInfo)
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
            dataMap[ACTION_TYPE] = ParamObject.ACTION_EDIT
            dataMap[IS_DATA_CHANGE] = checkDataChanged()
            dataMap[GROUP_NAME] = getCurrentTitle()
            dataMap[DAILY_BUDGET] = getCurrentDailyBudget()
            dataMap[GROUPID] = groupId
            dataMap[BUDGET_LIMITED] = toggle?.isChecked
            dataMap[NAME_EDIT] = getCurrentTitle() != groupName
        } catch (e: NumberFormatException) {
        }
        return dataMap
    }

    private fun checkDataChanged(): Boolean {
        return initialDailyBudget != getCurrentDailyBudget() || groupName != getCurrentTitle() || initialToggleState != toggle?.isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    override fun onDetach() {
        buttonStateCallback = null
        super.onDetach()
    }
}
