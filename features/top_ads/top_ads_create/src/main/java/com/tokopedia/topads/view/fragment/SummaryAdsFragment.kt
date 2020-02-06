package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.*
import com.tokopedia.topads.data.response.*
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.activity.SuccessActivity
import com.tokopedia.topads.view.model.SummaryViewModel
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_fragment_summary.*
import java.util.ArrayList
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */
class SummaryAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var viewModel: SummaryViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var createAdModel = CreateAdModel()
    var createAdItemModel = CreateAdItemModel()
    var createAdGroupModel = CreateAdGroupModel()
    var createKeywordItemModel = CreateKeywordItemModel()
    var createAdResponseModel = CreateAdResponseModel()
    var map = HashMap<String, Any>()
    private var input = InputCreateGroup()
    var keyword = KeywordsItem()
    var group = Group()
    private var keywordsList:MutableList<KeywordsItem> = mutableListOf()
    private var adsItemsList:MutableList<AdsItem> = mutableListOf()


    companion object {
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

    fun onSuccess(data: TopAdsDepositResponse.Data) {

    }

    fun errorResponse(throwable: Throwable) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        btn_submit.setOnClickListener {

            viewModel.getTopAdsDeposit(this::onSuccess, this::errorResponse)
            var map = convertToParam(view)
            if (true) {
                var intent = Intent(context, SuccessActivity::class.java)
                startActivity(intent)

            }

            //TODO check credits and redirect

//            startActivity(RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT))
//            activity!!.finish()
//
//            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL)


            viewModel.topAdsCreated(map, this::onSuccessActivation, this::onErrorActivation)
        }
        toggle.setOnClickListener {
            if (toggle.isChecked) {
                daily_budget.visibility = View.VISIBLE
                daily_budget.setText(((stepperModel?.suggestedBidPerClick!!)*40).toString())
                stepperModel?.dailyBudget = Integer.parseInt(daily_budget.text.toString())
            } else
                daily_budget.visibility = View.GONE
        }
        daily_budget.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var input = 0
                if (s.isNotEmpty()) {
                    input = Integer.parseInt(s.toString())
                    stepperModel?.dailyBudget = input

                }
                if (input < 25) {
                    error_text.visibility = View.VISIBLE
                } else
                    error_text.visibility = View.GONE

            }
        })

        var spannableText = SpannableString(" Info Selengkapnya")
        var startIndex = 0
        var endIndex = spannableText.length
        spannableText.setSpan(resources.getColor(R.color.tkpd_main_green), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                RouteManager.route(context, "https://seller.tokopedia.com/edu/cara-topads-mendeteksi-klik-tampil-yang-tidak-valid/")

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        info_text.movementMethod = LinkMovementMethod.getInstance()
        info_text.append(spannableText)

        bid_range.text = String.format(resources.getString(R.string.bid_range),stepperModel?.minBid,stepperModel?.maxBid)
    }

    private fun convertToParam(view: View): HashMap<String, Any> {
        var userSession = UserSession(view.context)

        input.shopID = userSession.shopId
        input.group.groupName = stepperModel?.groupName?:""
        input.group.priceBid = stepperModel?.suggestedBidPerClick?:0
        input.group.priceDaily = stepperModel?.dailyBudget?:0

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
                var add = AdsItem()
                add.productID = stepperModel!!.selectedProductIds[index].toString()
                adsItemsList.add(add)
            }
            input.group.ads = adsItemsList
        }

        map.put("input", input)
        return map
    }

    private fun onSuccessActivation(data: ResponseCreateGroup) {
       var group= data.topadsCreateGroupAds.data.group
        mapper(group.ads)


    }

    private fun mapper(ads: List<ResponseCreateGroup.AdsItem>) {
        val adsItemList= ArrayList<AdsItem>()
        adsItemList.clear()
        try {
            for (item in ads) {
                val ad = Ad()
                var adsItem = AdsItem()
                ad.adType = item.ad.adType
                ad.adID = item.ad.adID
                adsItem.ad = ad
                adsItemList.add(adsItem)

            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }


        input.group.ads = adsItemList

    }

    private fun onErrorActivation(throwable: Throwable) {

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