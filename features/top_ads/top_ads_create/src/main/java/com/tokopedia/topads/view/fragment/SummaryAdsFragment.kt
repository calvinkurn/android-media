package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_POSITIVE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BROAD_TYPE
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.EXACT_POSITIVE
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION_CREATE
import com.tokopedia.topads.common.data.internal.ParamObject.ADDED_PRODUCTS
import com.tokopedia.topads.common.data.internal.ParamObject.BID_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.NAME_EDIT
import com.tokopedia.topads.common.data.internal.ParamObject.POSITIVE_CREATE
import com.tokopedia.topads.common.data.internal.ParamObject.STRATEGIES
import com.tokopedia.topads.common.data.model.Group
import com.tokopedia.topads.common.data.model.InputCreateGroup
import com.tokopedia.topads.common.data.model.KeywordsItem
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.sheet.TopAdsOutofCreditSheet
import com.tokopedia.topads.common.view.sheet.TopAdsSuccessSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.SummaryViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_create_fragment_summary.*
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_IKLANKAN_BUTTON = "click-iklankan manual"
private const val PRODUCT_INFO = "product_id: %s; keyword_name: %s; keyword_id: %s"
private const val CLICK_PRODUCT_EDIT = "click - edit produk di ringkasan iklan"
private const val CLICK_KATA_KUNCI_EDIT = "click - edit kata kunci di ringkasan iklan"
private const val CLICK_BIAYA_EDIT = "lick - edit biaya iklan di ringkasan iklan"

const val DEBOUNCE_CONST: Long = 200
const val DAILYBUDGET_FACTOR = 1000
private const val AUTOBID_DEFUALT_BUDGET = 16000

class SummaryAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var viewModel: SummaryViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var map = HashMap<String, Any>()
    private var input = InputCreateGroup()
    var keyword = KeywordsItem()
    var group = Group()
    private var strategies: MutableList<String> = mutableListOf()
    private var keywordsList: MutableList<KeySharedModel> = mutableListOf()
    private var adsItemsList: ArrayList<GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem> = arrayListOf()
    private var selectedProductIds: MutableList<String> = mutableListOf()
    private var selectedkeywordIds: MutableList<String> = mutableListOf()
    private var selectedkeywordTags: MutableList<String> = mutableListOf()
    private var bidTypeData: ArrayList<TopAdsBidSettingsModel>? = arrayListOf()
    var isEnoughDeposit = false
    private var dailyBudget = 0
    private var suggestion = 0
    private var validation1 = true
    private var validation2 = true
    var minBudget: Int = 0
    private val job = SupervisorJob()
    val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        private const val MORE_INFO = " Info Selengkapnya"
        private const val MULTIPLIER = 40
        private const val MAXIMUM_LIMIT = 10000000
        private const val UNLIMITED_BUDGET = "0"
        private const val DEFINED_BUDGET = "1"
        private const val INPUT = "input"
        fun createInstance(): Fragment {

            val fragment = SummaryAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.summary_page_step))
    }

    override fun getScreenName(): String {
        return SummaryAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SummaryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.topads_create_fragment_summary, container, false)
    }

    private fun onSuccess(data: DepositAmount) {
        isEnoughDeposit = data.amount > 0
        if (isEnoughDeposit) {
            val sheet = TopAdsSuccessSheet()
            sheet.overlayClickDismiss = false
            sheet.show(childFragmentManager)
        } else {
            val sheet = TopAdsOutofCreditSheet()
            sheet.overlayClickDismiss = false
            sheet.show(childFragmentManager)
        }
    }

    private fun errorResponse(throwable: Throwable) {
        SnackbarManager.make(
            activity,
            throwable.message,
            Snackbar.LENGTH_LONG
        )
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAutoBidState()
        btn_submit.setOnClickListener {
            if (groupInput?.textFieldInput?.text?.isNotEmpty() == true) {
                loading?.visibility = View.VISIBLE
                btn_submit?.isEnabled = false
//                val map = convertToParam(view)
                viewModel.topAdsCreated(getProductData(), getKeywordData(), getGroupData(), this::onSuccessActivation, this::onErrorActivation)
                sendAnalyticEvent()
            } else {
                onErrorGroupName(getString(R.string.topads_create_group_name_empty_error))
            }
        }
        setUpInitialValues()
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                daily_budget.visibility = View.VISIBLE
                var budget = 0
                try {
                    budget = Integer.parseInt(
                        daily_budget.textFieldInput.text.toString().removeCommaRawString()
                    )
                } catch (e: NumberFormatException) {
                }
                if (budget < suggestion && daily_budget.isVisible) {
                    daily_budget.setMessage(
                        String.format(
                            getString(R.string.topads_common_minimum_daily_budget),
                            minBudget
                        )
                    )
                    daily_budget.setError(true)
                    validation2 = false
                    actionEnable()
                }
            } else {
                daily_budget.visibility = View.GONE
                daily_budget.setError(false)
                daily_budget.setMessage("")
                validation2 = true
                actionEnable()
            }
        }
        daily_budget.textFieldInput.addTextChangedListener(watcher())
        setLink()
    }

    private fun setAutoBidState() {
        if (stepperModel?.autoBidState?.isEmpty() == true) {
            group_non_auto?.visible()
            group_auto?.gone()
            divider2.gone()
            divider3.visible()
            divider4.visible()
        } else {
            group_non_auto?.gone()
            group_auto?.visible()
            divider3.gone()
            divider4.gone()
        }
    }

    private fun setUpInitialValues() {
        suggestion = (stepperModel?.finalBidPerClick ?: 0) * MULTIPLIER
        minBudget = if (stepperModel?.autoBidState?.isEmpty() != true)
            AUTOBID_DEFUALT_BUDGET
        else
            suggestion
        stepperModel?.dailyBudget = suggestion
        dailyBudget = if (stepperModel?.autoBidState?.isEmpty() == true)
            (stepperModel?.finalBidPerClick ?: 0) * MULTIPLIER
        else
            AUTOBID_DEFUALT_BUDGET
        daily_budget.textFieldInput.setText(dailyBudget.toString())
        groupInput?.textFieldInput?.imeOptions = EditorInfo.IME_ACTION_DONE
        groupInput?.textFieldInput?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Utils.dismissKeyboard(context, view)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        groupInput?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                setGroupName()
            } else {
                groupInput.textFieldInput.setText(stepperModel?.groupName)
                groupInput?.setError(false)
                validation1 = true
                actionEnable()
                groupInput?.setMessage(getString(R.string.topads_create_group_name_message))
            }
        }
    }

    private fun setGroupName() {
        groupInput?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                stepperModel?.groupName = s.toString()
                s?.let {
                    coroutineScope.launch {
                        delay(DEBOUNCE_CONST)
                        if (activity != null && isAdded) {
                            val text = s.toString().trim()
                            if (text.isNotEmpty()) {
                                viewModel.validateGroup(text, ::onSuccessGroupName)
                            } else {
                                onErrorGroupName(getString(R.string.topads_create_group_name_empty_error))
                            }
                        }

                    }
                }
            }
        })
    }

    private fun sendAnalyticEvent() {
        adsItemsList.forEachIndexed { index, _ ->
            selectedProductIds.add(adsItemsList[index].itemID)
        }

        keywordsList.forEachIndexed { index, _ ->
            selectedkeywordIds.add(keywordsList[index].id)
        }

        keywordsList.forEachIndexed { index, _ ->
            keywordsList[index].name?.let { selectedkeywordTags.add(it) }
        }

        val eventLabel = PRODUCT_INFO.format(
            selectedProductIds.joinToString(","),
            selectedkeywordTags.joinToString("::"),
            selectedkeywordIds.joinToString(",")
        )
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(
            CLICK_IKLANKAN_BUTTON,
            eventLabel
        )
    }

    private fun setCardData() {
        bidRange?.text = String.format(
            resources.getString(R.string.bid_range),
            stepperModel?.minBid.toString(),
            stepperModel?.maxBid.toString()
        )
        productCount?.text = stepperModel?.selectedProductIds?.count().toString()
        keywordCount?.text = stepperModel?.selectedKeywordStage?.count().toString()

        goToProduct?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_PRODUCT_EDIT, "")
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_1, stepperModel)
        }

        goToKeyword?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_KATA_KUNCI_EDIT, "")
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_3, stepperModel)
        }
        goToBudget?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(CLICK_BIAYA_EDIT, "")
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_3, stepperModel)
        }
        goToAutobid.setOnClickListener {
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_2, stepperModel)
        }
    }

    private fun setLink() {
        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        context?.let {
            spannableText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        it,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                ), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, getString(R.string.more_info))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                context?.let {
                    ds.color =
                        ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Green_G500)

                }
            }
        }
        spannableText.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        info_text?.movementMethod = LinkMovementMethod.getInstance()
        info_text?.append(spannableText)
    }

    private fun isMinValidation(input: Int): Boolean {
        return (input < (stepperModel?.finalBidPerClick
            ?: 0) * MULTIPLIER && stepperModel?.autoBidState?.isEmpty() == true) ||
                (input < minBudget && stepperModel?.autoBidState?.isEmpty() != true) && daily_budget.isVisible
    }

    private fun watcher(): NumberTextWatcher {
        return object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val input = number.toInt()
                if (isMinValidation(input)) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(
                        String.format(
                            getString(com.tokopedia.topads.common.R.string.angarran_harrian_min_bid_error),
                            Utils.convertToCurrency(minBudget.toLong())
                        )
                    )
                    validation2 = false
                    actionEnable()
                } else if (input % DAILYBUDGET_FACTOR != 0) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(
                        String.format(
                            getString(R.string.topads_common_error_multiple_50),
                            DAILYBUDGET_FACTOR
                        )
                    )
                    validation2 = false
                    actionEnable()
                } else if (input > MAXIMUM_LIMIT.toDouble() && daily_budget.isVisible) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(
                        String.format(
                            getString(com.tokopedia.topads.common.R.string.angarran_harrian_max_bid_error),
                            Utils.convertToCurrency(MAXIMUM_LIMIT.toLong())
                        )
                    )
                    validation2 = false
                    actionEnable()
                } else {
                    stepperModel?.dailyBudget = input
                    daily_budget.setMessage("")
                    daily_budget.setError(false)
                    validation2 = true
                    actionEnable()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.runOnUiThread {
            daily_budget.textFieldInput.setText(dailyBudget.toString())
            daily_budget.refreshDrawableState()
        }
    }

    private fun getGroupData() : HashMap<String, Any?> {
        var dataMap = HashMap<String, Any?>()

        bidTypeData?.add(TopAdsBidSettingsModel("product_search", stepperModel?.finalBidPerClick?.toFloat()))
        bidTypeData?.add(TopAdsBidSettingsModel("product_browse", stepperModel?.finalBidPerClick?.toFloat()))
        dataMap[ParamObject.GROUP_NAME] = stepperModel?.groupName ?: ""
        dataMap[BID_TYPE] =  bidTypeData
        dataMap[GROUPID] = ""
        dataMap[NAME_EDIT] = true
        dataMap[ParamObject.ACTION_TYPE] = ACTION_CREATE
        if (stepperModel?.autoBidState?.isNotEmpty() == true) {
            strategies.clear()
            strategies.add(stepperModel?.autoBidState!!)
        }

        dataMap[STRATEGIES] = strategies
        return dataMap

    }

    private fun getKeywordData() : HashMap<String, Any?> {

        val dataKeyword = HashMap<String, Any?>()
        keywordsList.clear()

        if (stepperModel?.autoBidState?.isEmpty() == true && stepperModel?.selectedKeywordStage?.count() ?: 0 > 0) {
            stepperModel?.selectedKeywordStage?.forEachIndexed { index, _ ->
                addKeywords(index)
            }
        }
        dataKeyword[POSITIVE_CREATE] = keywordsList
        return dataKeyword
    }

    private fun getProductData() : Bundle {
        val datProduct = Bundle()
        adsItemsList.clear()
        if (stepperModel?.selectedProductIds?.count() ?: 0 > 0) {
            stepperModel?.selectedProductIds?.forEachIndexed { index, _ ->
                addProducts(index)
            }
        }
        datProduct.putParcelableArrayList(ADDED_PRODUCTS, adsItemsList)
        return datProduct
    }

    private fun convertToParam(view: View): HashMap<String, Any> {
//        val userSession = UserSession(view.context)
//        if (!toggle.isChecked) {
//            input.group.groupBudget = UNLIMITED_BUDGET
//        } else {
//            input.group.groupBudget = DEFINED_BUDGET
//            input.group.priceDaily = stepperModel?.dailyBudget?.toDouble() ?: 0.0
//        }
//        input.shopID = userSession.shopId
//        input.group.groupName = stepperModel?.groupName ?: ""
//        if (stepperModel?.autoBidState?.isEmpty() == true) {
//            input.group.priceBid = stepperModel?.finalBidPerClick?.toDouble() ?: 0.0
//        } else {
//            input.group.priceBid = stepperModel?.minBid?.toDouble() ?: 0.0
//        }
//        input.group.suggestedBidValue = stepperModel?.suggestedBidPerClick?.toDouble() ?: 0.0
//        keywordsList.clear()
//        adsItemsList.clear()
//        if (stepperModel?.autoBidState?.isEmpty() == true && stepperModel?.selectedKeywordStage?.count() ?: 0 > 0) {
//            stepperModel?.selectedKeywordStage?.forEachIndexed { index, _ ->
//                addKeywords(index)
//            }
//            input.keywords = keywordsList
//        } else {
//            input.keywords = null
//        }
//        if (stepperModel?.selectedProductIds?.count() ?: 0 > 0) {
//            stepperModel?.selectedProductIds?.forEachIndexed { index, _ ->
//                addProducts(index)
//            }
//            input.group.ads = adsItemsList
//        }

//        if (stepperModel?.autoBidState?.isNotEmpty() == true) {
//            strategies.clear()
//            strategies.add(stepperModel?.autoBidState!!)
//        }
//        input.group.strategies = strategies

        map[INPUT] = input
        return map
    }

    private fun addProducts(index: Int) {
        var id = stepperModel?.selectedProductIds?.get(index).toString()
        adsItemsList.add(GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem(id))
    }

    private fun addKeywords(index: Int) {
        val key = KeySharedModel()
        val type = stepperModel?.selectedKeywordStage?.get(index)?.keywordType
        val typeInt = if (type == BROAD_TYPE)
            BROAD_POSITIVE
        else
            EXACT_POSITIVE

        key.id = typeInt.toString()
        key.name = stepperModel?.selectedKeywordStage?.get(index)?.keyword ?: ""
        if (stepperModel?.selectedKeywordStage?.get(index)?.bidSuggest?.toDouble() ?: 0.0 != 0.0)
            key.priceBid = stepperModel?.selectedKeywordStage?.get(index)?.bidSuggest
                ?: "0"
        else
            key.priceBid = stepperModel?.minSuggestBidKeyword ?: "0"
        keywordsList.add(key)
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateName) {
        if (data.errors.isEmpty()) {
            groupInput?.setError(false)
            validation1 = true
            actionEnable()
            groupInput?.setMessage(getString(R.string.topads_create_group_name_message))
        } else {
            onErrorGroupName(data.errors[0].detail)
        }
    }

    private fun actionEnable() {
        btn_submit?.isEnabled = validation1 && validation2
    }

    private fun onErrorGroupName(error: String) {
        groupInput?.setError(true)
        validation1 = false
        actionEnable()
        groupInput?.setMessage(error)
        if (error == resources.getString(R.string.topads_create_group_name_error_wrong))
            groupInput?.setMessage(resources.getString(R.string.topads_create_group_name_error))
        else
            groupInput?.setMessage(error)
    }

    private fun onSuccessActivation() {
        viewModel.getTopAdsDeposit(this::onSuccess, this::errorResponse)
    }

    private fun onErrorActivation(error: String?) {
        val message = Utils.getErrorMessage(context, error ?: "")
        view?.let {
            Toaster.build(
                it, message,
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)
            ).show()
        }
        loading?.visibility = View.GONE
        btn_submit?.isEnabled = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCardData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }
}