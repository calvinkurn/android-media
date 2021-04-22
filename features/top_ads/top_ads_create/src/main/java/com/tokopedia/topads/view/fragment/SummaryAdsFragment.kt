package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topads.common.activity.NoCreditActivity
import com.tokopedia.topads.common.activity.SuccessActivity
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.model.AdsItem
import com.tokopedia.topads.common.data.model.Group
import com.tokopedia.topads.common.data.model.InputCreateGroup
import com.tokopedia.topads.common.data.model.KeywordsItem
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.data.response.ResponseGroupValidateName
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.SummaryViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_create_fragment_summary.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_IKLANKAN_BUTTON = "click-iklankan manual"
private const val PRODUCT_INFO = "product_id: %s; keyword_name: %s; keyword_id: %s"

const val DEBOUNCE_CONST: Long = 200
const val DAILYBUDGET_FACTOR = 1000

class SummaryAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var viewModel: SummaryViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var map = HashMap<String, Any>()
    private var input = InputCreateGroup()
    var keyword = KeywordsItem()
    var group = Group()
    private var autoBidState: MutableList<String> = mutableListOf()
    private var keywordsList: MutableList<KeywordsItem> = mutableListOf()
    private var adsItemsList: MutableList<AdsItem> = mutableListOf()
    private var selectedProductIds: MutableList<String> = mutableListOf()
    private var selectedkeywordIds: MutableList<String> = mutableListOf()
    private var selectedkeywordTags: MutableList<String> = mutableListOf()
    var isEnoughDeposit = false
    private var dailyBudget = 0
    private var suggestion = 0
    private var validation1 = true
    private var validation2 = true


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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_summary, container, false)
    }

    private fun onSuccess(data: DepositAmount) {
        isEnoughDeposit = data.amount > 0
        val intent: Intent = if (isEnoughDeposit) {
            Intent(context, SuccessActivity::class.java).apply {
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
            }
        } else {
            Intent(context, NoCreditActivity::class.java).apply {
                if (isFromPdpSellerMigration(activity?.intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(activity?.intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(activity?.intent?.extras))
                }
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun errorResponse(throwable: Throwable) {
        SnackbarManager.make(activity,
                throwable.message,
                Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(stepperModel?.autoBidState?.isEmpty() == true) {
            keyword_layout.visibility = View.VISIBLE
            budget_layout.visibility = View.VISIBLE
            autobid_layout.visibility = View.GONE
            divider2.visibility = View.GONE
            divider3.visibility = View.VISIBLE
            divider4.visibility = View.VISIBLE
        } else {
            keyword_layout.visibility = View.GONE
            budget_layout.visibility = View.GONE
            autobid_layout.visibility = View.VISIBLE
            divider3.visibility = View.GONE
            divider4.visibility = View.GONE

            goToAutobid.setOnClickListener {
                stepperListener?.getToFragment(2, stepperModel)
            }
        }
        btn_submit.setOnClickListener {
            if (groupInput?.textFieldInput?.text?.isNotEmpty() == true) {
                loading?.visibility = View.VISIBLE
                btn_submit?.isEnabled = false
                val map = convertToParam(view)
                viewModel.topAdsCreated(map, this::onSuccessActivation, this::onErrorActivation)
                sendAnalyticEvent()
            } else {
                onErrorGroupName(getString(R.string.topads_create_group_name_empty_error))
            }
        }
        setGroupName()
        suggestion = (stepperModel?.finalBidPerClick ?: 0) * MULTIPLIER
        stepperModel?.dailyBudget = suggestion
        dailyBudget = (stepperModel?.finalBidPerClick ?: 0) * 40
        daily_budget.textFieldInput.setText(dailyBudget.toString())
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                daily_budget.visibility = View.VISIBLE
                var budget = 0
                try {
                    budget = Integer.parseInt(daily_budget.textFieldInput.text.toString().removeCommaRawString())
                } catch (e: NumberFormatException) {

                }
                if (budget < suggestion && daily_budget.isVisible) {
                    daily_budget.setMessage(String.format(getString(R.string.daily_budget_error), suggestion))
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

    private fun setGroupName() {
        groupInput?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val coroutineScope = CoroutineScope(Dispatchers.Main)
                stepperModel?.groupName = s.toString()
                s?.let {
                    coroutineScope.launch {
                        delay(DEBOUNCE_CONST)
                        val text = s.toString().trim()
                        if (text.isNotEmpty()) {
                            viewModel.validateGroup(text, ::onSuccessGroupName)
                        } else {
                            onErrorGroupName(getString(R.string.topads_create_group_name_empty_error))
                        }

                    }
                }
            }
        })
    }

    private fun sendAnalyticEvent() {
        adsItemsList.forEachIndexed { index, _ ->
            selectedProductIds.add(adsItemsList[index].productID.toString())
        }

        keywordsList.forEachIndexed { index, _ ->
            selectedkeywordIds.add(keywordsList[index].keywordTypeID)
        }

        keywordsList.forEachIndexed { index, _ ->
            selectedkeywordTags.add(keywordsList[index].keywordTag)
        }

        val eventLabel = PRODUCT_INFO.format(selectedProductIds.joinToString(","), selectedkeywordTags.joinToString("::"), selectedkeywordIds.joinToString(","))
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_IKLANKAN_BUTTON, eventLabel)
    }

    private fun setCardData() {
        bidRange?.text = String.format(resources.getString(R.string.bid_range), stepperModel?.minBid.toString(), stepperModel?.maxBid.toString())
        productCount?.text = stepperModel?.selectedProductIds?.count().toString()
        keywordCount?.text = stepperModel?.selectedKeywordStage?.count().toString()
        goToProduct?.setOnClickListener {
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(1, stepperModel)
        }

        goToKeyword?.setOnClickListener {
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(2, stepperModel)
        }
        goToBudget?.setOnClickListener {
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(2, stepperModel)
        }
    }

    private fun setLink() {
        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        context?.let {
            spannableText.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, getString(R.string.more_info))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                context?.let {
                    ds.color = ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Green_G500)

                }
            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        info_text?.movementMethod = LinkMovementMethod.getInstance()
        info_text?.append(spannableText)
    }

    private fun watcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val input = number.toInt()
                if (input < (stepperModel?.finalBidPerClick ?: 0) * MULTIPLIER
                        && daily_budget.isVisible) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.daily_budget_error), suggestion))
                    validation2 = false
                    actionEnable()
                } else if (input % DAILYBUDGET_FACTOR != 0) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.topads_common_error_multiple_50), DAILYBUDGET_FACTOR))
                    validation2 = false
                    actionEnable()
                } else if (input > MAXIMUM_LIMIT && daily_budget.isVisible) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.topads_common_maximum_daily_budget), MAXIMUM_LIMIT))
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

    private fun convertToParam(view: View): HashMap<String, Any> {
        val userSession = UserSession(view.context)
        if (!toggle.isChecked) {
            input.group.groupBudget = UNLIMITED_BUDGET
        } else {
            input.group.groupBudget = DEFINED_BUDGET
            input.group.priceDaily = stepperModel?.dailyBudget?.toDouble() ?: 0.0
        }
        input.shopID = userSession.shopId
        input.group.groupName = stepperModel?.groupName ?: ""
        input.group.priceBid = stepperModel?.finalBidPerClick?.toDouble() ?: 0.0
        input.group.suggestedBidValue = stepperModel?.suggestedBidPerClick?.toDouble() ?: 0.0
        keywordsList.clear()
        adsItemsList.clear()
        if (stepperModel?.selectedKeywordStage?.count() ?: 0 > 0) {
            stepperModel?.selectedKeywordStage?.forEachIndexed { index, _ ->
                addKeywords(index)
            }
            input.keywords = keywordsList
        } else {
            input.keywords = null
        }
        if (stepperModel?.selectedProductIds?.count() ?: 0 > 0) {
            stepperModel?.selectedProductIds?.forEachIndexed { index, _ ->
                addProducts(index)
            }
            input.group.ads = adsItemsList
        }

        stepperModel?.autoBidState?.let {
            autoBidState.clear()
            autoBidState.add(it)
        }
        input.strategies = autoBidState

        map[INPUT] = input
        return map
    }

    private fun addProducts(index: Int) {
        val add = AdsItem()
        add.productID = stepperModel?.selectedProductIds?.get(index).toString()
        add.ad.adID = stepperModel?.adIds?.get(index).toString()
        add.ad.adType = "1"
        adsItemsList.add(add)
    }

    private fun addKeywords(index: Int) {
        val key = KeywordsItem()
        val type = stepperModel?.selectedKeywordStage?.get(index)?.keywordType
        val typeInt = if (type == BudgetingAdsFragment.BROAD_TYPE)
            BudgetingAdsFragment.BROAD_POSITIVE
        else
            BudgetingAdsFragment.EXACT_POSITIVE

        key.keywordTypeID = typeInt.toString()
        key.keywordTag = stepperModel?.selectedKeywordStage?.get(index)?.keyword ?: ""
        if (stepperModel?.selectedKeywordStage?.get(index)?.bidSuggest?.toDouble() ?: 0.0 != 0.0)
            key.priceBid = stepperModel?.selectedKeywordStage?.get(index)?.bidSuggest?.toDouble()
                    ?: 0.0
        else
            key.priceBid = stepperModel?.minSuggestBidKeyword?.toDouble() ?: 0.0
        keywordsList.add(key)
    }

    fun onSuccessGroupName(data: ResponseGroupValidateName.TopAdsGroupValidateName) {
        if (data.errors.isEmpty()) {
            groupInput?.setError(false)
            validation1 = true
            actionEnable()
            groupInput?.setMessage("")
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

    private fun onErrorActivation(throwable: Throwable) {
        val message = Utils.getErrorMessage(context, throwable.message ?: "")
        view?.let {
            Toaster.build(it, message,
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_ERROR,
                    getString(com.tokopedia.topads.common.R.string.topads_common_text_ok)).show()
        }
        loading?.visibility = View.GONE
        btn_submit?.isEnabled = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCardData()
    }
}