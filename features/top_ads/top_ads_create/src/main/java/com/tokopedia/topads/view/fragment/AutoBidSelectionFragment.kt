package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

private const val AUTO_BID_STATE = "auto_bid"
private const val MANUAL_BID = 10
private const val AUTO_BID = 20
private const val CLICK_SAVE_AUTO_BID = "click - lanjutkan pengaturan iklan"
private const val OTOMATIS_LEARN_MORE_LINK = "https://seller.tokopedia.com/edu/topads-otomatis/"
private const val MANUAL_LEARN_MORE_LINK = "https://seller.tokopedia.com/edu/iklan-manual-baru/"

class AutoBidSelectionFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var nextBtn: UnifyButton
    private lateinit var otomatisLayout: ConstraintLayout
    private lateinit var manualLayout: ConstraintLayout
    private lateinit var otomatisRadioBtn: RadioButtonUnify
    private lateinit var manualRadioBtn: RadioButtonUnify
//    private lateinit var tipBtn: FloatingButtonUnify
    private lateinit var otomatisLearMore: Typography
    private lateinit var manualLearMore: Typography
    private lateinit var otomatisTag: Typography
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    private lateinit var cardAutomatic: CardUnify2
    private lateinit var cardManual: CardUnify2

    var selected: Int = 0

    companion object {
        fun createInstance(): Fragment {
            val fragment = AutoBidSelectionFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.tokopedia.topads.common.R.layout.auto_bid_selection_layout,
            container,
            false
        )
        nextBtn = view.findViewById(com.tokopedia.topads.common.R.id.save_autobid)
        otomatisRadioBtn = view.findViewById(com.tokopedia.topads.common.R.id.radiobtn_otomatis)
        manualRadioBtn = view.findViewById(com.tokopedia.topads.common.R.id.radiobtn_manual)
        manualLearMore = view.findViewById(com.tokopedia.topads.common.R.id.manual_learn_more)
        otomatisLearMore = view.findViewById(com.tokopedia.topads.common.R.id.otomatis_learn_more)
        cardAutomatic = view.findViewById(com.tokopedia.topads.common.R.id.card_automatic)
        cardManual = view.findViewById(com.tokopedia.topads.common.R.id.card_manual)
        manualRadioBtn.setOnCheckedChangeListener(null)
        otomatisRadioBtn.setOnCheckedChangeListener(null)
        return view
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        if (stepperModel?.autoBidState?.isEmpty() == true) {
            stepperListener?.goToNextPage(stepperModel)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(
                CLICK_SAVE_AUTO_BID,
                getString(R.string.autobid_manual_title)
            )
        } else {
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_4, stepperModel)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsCreateEvent(
                CLICK_SAVE_AUTO_BID,
                getString(R.string.autobid_otomatis_title)
            )
        }
    }

    private fun prepareView() {
        if (stepperModel?.redirectionToSummary == true) {
            nextBtn.text = getString(R.string.topads_common_save_butt)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tipBtn.visibility = View.VISIBLE
        nextBtn.visibility = View.VISIBLE
        prepareView()

        if (selected == MANUAL_BID)
            manualRadioBtn.isChecked = true
        else
            otomatisRadioBtn.isChecked = true
        handleClick()

        nextBtn.setOnClickListener {
            gotoNextPage()
        }

        setAutoBidChosen()
    }

    private fun setAutoBidChosen() {
        otomatisRadioBtn.isChecked = true
        manualRadioBtn.isChecked = false
        stepperModel?.autoBidState = AUTO_BID_STATE
        setCardStateCondition(cardAutomatic, true)
        setCardStateCondition(cardManual, false)
    }

    private fun handleClick() {
        otomatisLearMore.setOnClickListener {
            openWebView(OTOMATIS_LEARN_MORE_LINK)
        }
        manualLearMore.setOnClickListener {
            openWebView(MANUAL_LEARN_MORE_LINK)
        }

        cardManual.setOnClickListener {
            manualRadioBtn.isChecked = true
            otomatisRadioBtn.isChecked = false
            stepperModel?.autoBidState = ""
        }

        cardAutomatic.setOnClickListener {
            otomatisRadioBtn.isChecked = true
            manualRadioBtn.isChecked = false
            stepperModel?.autoBidState = AUTO_BID_STATE
        }

        manualRadioBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                otomatisRadioBtn.isChecked = false
                stepperModel?.autoBidState = ""
                setCardStateCondition(cardAutomatic, false)
                setCardStateCondition(cardManual, true)
            }
        }
        otomatisRadioBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                manualRadioBtn.isChecked = false
                stepperModel?.autoBidState = AUTO_BID_STATE
                setCardStateCondition(cardAutomatic, true)
                setCardStateCondition(cardManual, false)
            }
        }
    }

    private fun setCardStateCondition(card: CardUnify2, isActive: Boolean) {
        if(isActive) {
            card.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_GN50)
            card.cardType = CardUnify.TYPE_BORDER_ACTIVE
        } else {
            card.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            card.cardType = CardUnify.TYPE_BORDER_DISABLED
        }
    }

    private fun openWebView(url: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, url)
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(com.tokopedia.topads.common.R.string.autobid_list_step))
    }

    override fun getScreenName(): String {
        return AutoBidSelectionFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

}
