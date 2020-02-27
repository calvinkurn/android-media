package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
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
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.*
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.NoCreditActivity
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.activity.SuccessActivity
import com.tokopedia.topads.view.model.SummaryViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_fragment_summary.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */
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
    var isEnoughDeposit = false
    private var dailyBudget = 0


    companion object {
        private const val MORE_INFO = " Info Selengkapnya"
        private const val MULTIPLIER = 40
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
            var map = convertToParam(view)
            viewModel.topAdsCreated(map, this::onSuccessActivation, this::onErrorActivation)
        }
        var suggestion = (stepperModel?.finalBidPerClick!!) * MULTIPLIER
        stepperModel?.dailyBudget = suggestion
        error_text.text = String.format(getString(R.string.daily_budget_error), suggestion)
        dailyBudget = stepperModel?.finalBidPerClick!! * 40
        daily_budget.setText(dailyBudget.toString())
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                daily_budget.visibility = View.VISIBLE
                daily_budget_butt.text = "Anggaran harian Dibatasi"
                var budget = 0
                try {
                    budget = Integer.parseInt(daily_budget.textWithoutPrefix.toString())
                } catch (e: NumberFormatException) {

                }
                if (budget < suggestion && daily_budget.isVisible) {
                    error_text.visibility = View.VISIBLE
                    btn_submit.isEnabled = false

                }
            } else {
                daily_budget_butt.text = "Tidak dibatasi"
                daily_budget.visibility = View.GONE
                error_text.visibility = View.GONE
                btn_submit.isEnabled = true

            }

        }
        daily_budget.addTextChangedListener(watcher())
        val spannableText = SpannableString(MORE_INFO)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(resources.getColor(R.color.tkpd_main_green), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, getString(R.string.more_info))

            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        info_text.movementMethod = LinkMovementMethod.getInstance()
        info_text.append(spannableText)

        bid_range.text = String.format(resources.getString(R.string.bid_range), stepperModel?.minBid, stepperModel?.maxBid)
    }

    private fun watcher(): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    var input = 0
                    var s = s.toString().replaceFirst("Rp ".toRegex(), "").trim()
                    if (s.isNotEmpty()) {
                        input = Integer.parseInt(s.toString())
                    }
                    if (input < stepperModel?.finalBidPerClick!! * MULTIPLIER
                            && daily_budget.isVisible) {
                        error_text.visibility = View.VISIBLE
                        btn_submit.isEnabled = false
                    } else {
                        stepperModel?.dailyBudget = input
                        error_text.visibility = View.GONE
                        btn_submit.isEnabled = true
                    }

                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        }

    }

    override fun onResume() {
        super.onResume()
        activity?.runOnUiThread {
            daily_budget.setText(dailyBudget.toString())
            daily_budget.refreshDrawableState()
        }

    }

    private fun convertToParam(view: View): HashMap<String, Any> {
        val userSession = UserSession(view.context)
        input.shopID = userSession.shopId
        input.group.groupName = stepperModel?.groupName ?: ""
        input.group.priceBid = stepperModel?.finalBidPerClick ?: 0
        input.group.priceDaily = stepperModel?.dailyBudget ?: 0
        input.group.suggestedBidValue = stepperModel?.suggestedBidPerClick ?: 0
        keywordsList.clear()
        adsItemsList.clear()

        if (stepperModel!!.selectedKeywords.count() > 0) {
            stepperModel!!.selectedKeywords.forEachIndexed { index, _ ->
                var key = KeywordsItem()
                key.keywordTag = stepperModel?.selectedKeywords!![index]
                key.priceBid = stepperModel?.selectedSuggestBid!![index]
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
        map["input"] = input
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