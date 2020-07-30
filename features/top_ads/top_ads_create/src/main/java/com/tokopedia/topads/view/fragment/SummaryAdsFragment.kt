package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topads.common.activity.NoCreditActivity
import com.tokopedia.topads.common.activity.SuccessActivity
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.param.AdsItem
import com.tokopedia.topads.data.param.Group
import com.tokopedia.topads.data.param.InputCreateGroup
import com.tokopedia.topads.data.param.KeywordsItem
import com.tokopedia.topads.data.response.ResponseCreateGroup
import com.tokopedia.topads.data.response.TopAdsDepositResponse
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.SummaryViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_fragment_summary.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_IKLANKAN_BUTTON = "click-iklankan"
private const val PRODUCT_INFO = "product_id: %s; keyword_name: %s; keyword_id: %s"

class SummaryAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var viewModel: SummaryViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var map = HashMap<String, Any>()
    private var input = InputCreateGroup()
    var keyword = KeywordsItem()
    var group = Group()
    private var keywordsList: MutableList<KeywordsItem> = mutableListOf()
    private var adsItemsList: MutableList<AdsItem> = mutableListOf()
    private var selectedProductIds: MutableList<String> = mutableListOf()
    private var selectedkeywordIds: MutableList<String> = mutableListOf()
    private var selectedkeywordTags: MutableList<String> = mutableListOf()
    var isEnoughDeposit = false
    private var dailyBudget = 0
    private var suggestion = 0


    companion object {
        private const val MORE_INFO = " Info Selengkapnya"
        private const val MULTIPLIER = 40
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

    override fun populateView(stepperModel: CreateManualAdsStepperModel) {
    }

    override fun getScreenName(): String {
        return SummaryAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SummaryViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_summary, container, false)
    }

    private fun onSuccess(data: TopAdsDepositResponse.Data) {
        isEnoughDeposit = data.topadsDashboardDeposits.data.amount > 0
        val intent: Intent = if (isEnoughDeposit) {
            Intent(context, SuccessActivity::class.java)
        } else {
            Intent(context, NoCreditActivity::class.java)
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
        btn_submit.setOnClickListener {
            val map = convertToParam(view)
            viewModel.topAdsCreated(map, this::onSuccessActivation, this::onErrorActivation)

            adsItemsList.forEachIndexed { index, adsItem ->
                selectedProductIds.add(adsItemsList[index].productID)
            }

            keywordsList.forEachIndexed { index, keywordsItem ->
                selectedkeywordIds.add(keywordsList[index].keywordTypeID)
            }

            keywordsList.forEachIndexed { index, keywordsItem ->
                selectedkeywordTags.add(keywordsList[index].keywordTag)
            }

            var eventLabel = PRODUCT_INFO.format(selectedProductIds.joinToString(","), selectedkeywordTags.joinToString("::"), selectedkeywordIds.joinToString(","))
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_IKLANKAN_BUTTON, eventLabel)
        }
        suggestion = (stepperModel?.finalBidPerClick!!) * MULTIPLIER
        stepperModel?.dailyBudget = suggestion
        dailyBudget = stepperModel?.finalBidPerClick!! * 40
        daily_budget.textFieldInput.setText(dailyBudget.toString())
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                daily_budget.visibility = View.VISIBLE
                daily_budget_butt.text = getString(R.string.anggaran_dibatasi)
                var budget = 0
                try {
                    budget = Integer.parseInt(daily_budget.textFieldInput.text.toString().replace(",", ""))
                } catch (e: NumberFormatException) {

                }
                if (budget < suggestion && daily_budget.isVisible) {
                    daily_budget.setMessage(String.format(getString(R.string.daily_budget_error), suggestion))
                    daily_budget.setError(true)
                    btn_submit.isEnabled = false

                }
            } else {
                daily_budget_butt.text = getString(R.string.tidak_dibatasi)
                daily_budget.visibility = View.GONE
                daily_budget.setError(false)
                daily_budget.setMessage("")
                btn_submit.isEnabled = true
            }
        }
        daily_budget.textFieldInput.addTextChangedListener(watcher())
        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(resources.getColor(com.tokopedia.design.R.color.tkpd_main_green), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, getString(R.string.more_info))

            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        info_text.movementMethod = LinkMovementMethod.getInstance()
        info_text.append(spannableText)
        bid_range.text = String.format(resources.getString(R.string.bid_range), String.format("%,d", stepperModel?.minBid), String.format("%,d", stepperModel?.maxBid))
    }

    private fun watcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(daily_budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                var input = number.toInt()
                if (input < stepperModel?.finalBidPerClick!! * MULTIPLIER
                        && daily_budget.isVisible) {
                    daily_budget.setError(true)
                    daily_budget.setMessage(String.format(getString(R.string.daily_budget_error), suggestion))
                    btn_submit.isEnabled = false
                } else {
                    stepperModel?.dailyBudget = input
                    btn_submit.isEnabled = true
                    daily_budget.setMessage("")
                    daily_budget.setError(false)
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
            input.group.priceDaily = stepperModel?.dailyBudget ?: 0
        }
        input.shopID = userSession.shopId
        input.group.groupName = stepperModel?.groupName ?: ""
        input.group.priceBid = stepperModel?.finalBidPerClick ?: 0
        input.group.suggestedBidValue = stepperModel?.suggestedBidPerClick ?: 0
        keywordsList.clear()
        adsItemsList.clear()

        if (stepperModel!!.selectedKeywords.count() > 0) {
            stepperModel!!.selectedKeywords.forEachIndexed { index, _ ->
                val key = KeywordsItem()
                key.keywordTag = stepperModel?.selectedKeywords!![index]
                if (stepperModel?.selectedSuggestBid!![index] > 0) {
                    key.priceBid = stepperModel?.selectedSuggestBid!![index]
                } else
                    key.priceBid = stepperModel?.minSuggestBidKeyword ?: 0
                keywordsList.add(key)
            }
            input.keywords = keywordsList

        }
        if (stepperModel!!.selectedProductIds.count() > 0) {
            stepperModel!!.selectedProductIds.forEachIndexed { index, _ ->
                val add = AdsItem()
                add.productID = stepperModel!!.selectedProductIds[index].toString()
                add.ad.adID = stepperModel!!.adIds[index].toString()
                add.ad.adType = "1"
                adsItemsList.add(add)
            }
            input.group.ads = adsItemsList
        }
        map[INPUT] = input
        return map
    }

    private fun onSuccessActivation(data: ResponseCreateGroup) {
        viewModel.getTopAdsDeposit(this::onSuccess, this::errorResponse)
    }

    private fun onErrorActivation(throwable: Throwable) {
        SnackbarManager.make(activity,
                throwable.message,
                Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        product_count.text = stepperModel?.selectedProductIds?.count().toString()
        keyword_count.text = stepperModel?.selectedKeywords?.count().toString()
        group_name.text = stepperModel?.groupName
    }

    override fun updateToolBar() {
        (activity as StepperActivity).updateToolbarTitle(getString(R.string.summary_page_step))
    }
}