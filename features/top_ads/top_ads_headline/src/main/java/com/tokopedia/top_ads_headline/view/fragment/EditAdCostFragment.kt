package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.activity.OnMinBidChangeListener
import com.tokopedia.top_ads_headline.view.activity.SaveButtonState
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.adapter.viewpager.KeywordEditPagerAdapter
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.fragment_edit_ad_cost.*
import javax.inject.Inject

private const val POSITION_KEYWORD = 0
private const val POSITION_NEGATIVE_KEYWORD = 1

class EditAdCostFragment : BaseDaggerFragment() {

    private var sharedEditHeadlineViewModel: SharedEditHeadlineViewModel? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var stepperModel: HeadlineAdStepperModel? = null
    private val saveButtonState by lazy {
        activity as? SaveButtonState
    }

    private val onMinBidChange by lazy {
        activity as? OnMinBidChangeListener
    }

    private var groupId: Int = 0
    override fun getScreenName(): String {
        return EditAdCostFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    companion object {
        fun newInstance(groupId: String): EditAdCostFragment = EditAdCostFragment().apply {
            arguments = Bundle().apply {
                putString(GROUP_ID, groupId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            groupId = getString(GROUP_ID).toIntOrZero()
        }
        activity?.let {
            sharedEditHeadlineViewModel = ViewModelProvider(it, viewModelFactory).get(SharedEditHeadlineViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_ad_cost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        renderViewPager()
        setUpToolTipButton()
    }

    private fun setUpObservers() {
        sharedEditHeadlineViewModel?.getEditHeadlineAdLiveData()?.observe(viewLifecycleOwner, Observer {
            stepperModel = it
            setAdvertisingCost(it.adBidPrice)
        })
    }

    private fun setAdvertisingCost(adBidPrice: Double) {
        val cost = Utils.convertToCurrency(adBidPrice.toLong())
        stepperModel?.currentBid = adBidPrice
        advertisingCost.textFieldInput.setText(cost)
        advertisingCost.textFieldInput.addTextChangedListener(advertisingCostTextWatcher())
    }

    private fun advertisingCostTextWatcher(): NumberTextWatcher? {
        return object : NumberTextWatcher(advertisingCost.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                when {
                    number < stepperModel?.minBid?.toDouble() ?: 0.0 -> {
                        advertisingCost.setError(true)
                        advertisingCost.setMessage(String.format(getString(R.string.topads_headline_min_budget_cost_error), Utils.convertToCurrency(stepperModel?.minBid?.toLong()
                                ?: 0)))
                        saveButtonState?.setButtonState(false)
                    }
                    number > stepperModel?.maxBid?.toDouble() ?: 0.0 -> {
                        advertisingCost.setError(true)
                        advertisingCost.setMessage(String.format(getString(R.string.topads_headline_max_budget_cost_error), Utils.convertToCurrency(stepperModel?.maxBid?.toLong()
                                ?: 0)))
                        saveButtonState?.setButtonState(false)
                    }
                    else -> {
                        stepperModel?.adBidPrice = number
                        stepperModel?.dailyBudget = (number * MULTIPLIER).toFloat()
                        stepperModel?.currentBid = number
                        onMinBidChange?.onMinBidChange(number)
                        advertisingCost.setMessage("")
                        advertisingCost.setError(false)
                        saveButtonState?.setButtonState(true)
                    }
                }
            }
        }
    }

    private fun renderViewPager() {
        keyword.chipType = ChipsUnify.TYPE_SELECTED
        keyword.setOnClickListener {
            keyword.chipType = ChipsUnify.TYPE_SELECTED
            neg_keyword.chipType = ChipsUnify.TYPE_NORMAL
            view_pager.currentItem = POSITION_KEYWORD
        }
        neg_keyword.setOnClickListener {
            neg_keyword.chipType = ChipsUnify.TYPE_SELECTED
            keyword.chipType = ChipsUnify.TYPE_NORMAL
            view_pager.currentItem = POSITION_NEGATIVE_KEYWORD
        }
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    private fun getViewPagerAdapter(): KeywordEditPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(HeadlineEditKeywordFragment.getInstance(KEYWORD_POSITIVE, groupId))
        //since we are hiding negative keyword for now in app
//        list.add(HeadlineEditKeywordFragment.getInstance(KEYWORD_NEGATIVE, groupId))
        val adapter = KeywordEditPagerAdapter(childFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    private fun setUpToolTipButton() {
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            val tvToolTipText = findViewById<Typography>(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.topads_headline_ad_cost_tip)
            val imgTooltipIcon = findViewById<ImageUnify>(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(context?.getResDrawable(R.drawable.topads_ic_tips))
        }
        tooltipBtn.addItem(tooltipView)
        tooltipBtn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.topads_headline_ad_cost_row1, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_ad_cost_row2, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.topads_headline_ad_cost_row3, R.drawable.topads_create_ic_checklist))
            }
            val tipsSortListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsSortListSheet?.showHeader = true
            tipsSortListSheet?.showKnob = false
            tipsSortListSheet?.setTitle(getString(R.string.topads_headline_ad_cost_title))
            tipsSortListSheet?.show(childFragmentManager, "")
        }
    }

    fun onClickSubmit() {
        val fragments = (view_pager.adapter as KeywordEditPagerAdapter).list
        stepperModel?.keywordOperations?.clear()
        stepperModel?.minBid = advertisingCost.textFieldInput.text.toString().removeCommaRawString()
        for (fragment in fragments) {
            when (fragment) {
                is HeadlineEditKeywordFragment -> {
                    stepperModel?.keywordOperations?.addAll(fragment.getKeywordOperations())
                }
            }
        }
        stepperModel?.let { sharedEditHeadlineViewModel?.saveKeywordOperation(it) }
    }
}