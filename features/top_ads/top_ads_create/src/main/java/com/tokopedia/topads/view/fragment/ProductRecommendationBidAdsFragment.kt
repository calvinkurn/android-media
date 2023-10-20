package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.R as topadscommonR
import com.tokopedia.topads.common.sheet.TopAdsToolTipBottomSheet
import com.tokopedia.topads.common.view.sheet.CreatePotentialPerformanceSheet
import com.tokopedia.topads.create.databinding.TopadsCreateFragmentRecommendationBudgetBinding
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.RecommendationBidViewModel
import com.tokopedia.topads.view.sheet.TopAdsPredictionImpressionBottomSheet
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.dashboard.R as topadsdashboardR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.topads.create.R as topadscreateR


class ProductRecommendationBidAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private var binding: TopadsCreateFragmentRecommendationBudgetBinding? = null
    private var finalRecomBid: String = stepperModel?.suggestedBidPerClick ?: "0"

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null
    private var viewModel: RecommendationBidViewModel? = null

    companion object {

        fun createInstance(): Fragment {
            val fragment = ProductRecommendationBidAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setupView() {
        binding?.recommendationBudget?.editText?.setText(stepperModel?.suggestedBidPerClick.toString())
        binding?.txtInfoRecommendation?.text = MethodChecker.fromHtml(getString(topadscreateR.string.top_ads_create_text_info_recom_bid))
    }


    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}


    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(topadscreateR.string.topads_ads_browse_bid_title))
    }

    override fun getScreenName(): String {
        return BudgetingAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = TopadsCreateFragmentRecommendationBudgetBinding.inflate(inflater, container, false)
        viewModel = viewModelFactory?.let { ViewModelProvider(this, it)[RecommendationBidViewModel::class.java] }
        return binding?.root
    }


    private fun setObservers() {
        viewModel?.performanceData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    stepperModel?.recomPrediction = it.data.umpGetImpressionPrediction.impressionPredictionData.impression.finalImpression
                    binding?.impressionPerformanceValue?.text = String.format("%sx", it.data.umpGetImpressionPrediction.impressionPredictionData.impression.finalImpression)
                }

                else -> {}
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEditTextListeners()
        setupView()
        setObservers()
        setClicksOnViews()

    }

    private fun setClicksOnViews() {
        binding?.btnNext?.setOnClickListener {
            stepperModel?.finalRecommendationBidPerClick = finalRecomBid.toIntOrZero()
            gotoNextPage()
        }
        binding?.txtInfoRecommendation?.setOnClickListener {
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(getString(topadscreateR.string.topads_ads_browse_bid_tooltip_title))
                it.setDescription(getString(topadscreateR.string.topads_ads_browse_bid_tooltip_description))
            }.show(childFragmentManager)
        }
        binding?.infoImpressionPrediction?.setOnClickListener {
            stepperModel?.let {
                TopAdsPredictionImpressionBottomSheet.newInstance(searchPrediction = it.searchPrediction, recomPrediction = it.recomPrediction, totalPrediction = 0).apply {

                }.show(childFragmentManager)
            }

        }

        binding?.infoImpressionPrediction?.setOnClickListener {
            CreatePotentialPerformanceSheet.newInstance(
                stepperModel?.searchPrediction
                    ?: 0,
                stepperModel?.recomPrediction ?: 0
            ).show(childFragmentManager)
        }
    }

    private fun setEditTextListeners() {

        binding?.recommendationBudget?.editText?.addTextChangedListener(object : NumberTextWatcher(binding?.recommendationBudget?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                when {
                    result >= (stepperModel?.finalRecommendationBidPerClick ?: Int.ZERO) -> {
                        if (result > stepperModel?.maxBid.toDoubleOrZero() && stepperModel?.maxBid.toIntOrZero() != 0) {
                            setMessageErrorField(getString(topadscommonR.string.max_bid_error_new), stepperModel?.maxBid
                                ?: "", true)
                            binding?.btnNext?.isEnabled = false
                        } else {
                            if (number % 50 == 0.0) {
                                finalRecomBid = result.toString()
                                setMessageErrorField(getString(topadscommonR.string.topads_ads_optimal_bid), Int.ZERO.toString(), false)
                                stepperModel?.selectedProductIds?.let {
                                    viewModel?.getPerformanceData(it, result.toFloat(), -1f, -1f)
                                }
                                binding?.btnNext?.isEnabled = true
                            } else {
                                setMessageErrorField(getString(topadscommonR.string.topads_ads_error_multiple_fifty), Int.ZERO.toString(), true)
                                binding?.btnNext?.isEnabled = false
                            }
                        }
                    }

                    result < (stepperModel?.minBid?.toDoubleOrZero()
                        ?: 0f.toDouble()) && stepperModel?.maxBid.toIntOrZero() != Int.ZERO -> {
                        setMessageErrorField(getString(topadscommonR.string.min_bid_error_new), stepperModel?.minBid
                            ?: String.EMPTY, true)
                        binding?.btnNext?.isEnabled = false
                    }

                    else -> {
                        finalRecomBid = result.toString()
                        binding?.recommendationBudget?.setMessage(getClickableString(stepperModel?.finalRecommendationBidPerClick
                            ?: 0))
                        binding?.btnNext?.isEnabled = true
                        stepperModel?.selectedProductIds?.let {
                            viewModel?.getPerformanceData(it, result.toFloat(), -1f, -1f)
                        }
                    }
                }

            }
        })
    }

    private fun getClickableString(
        bid: Int,
    ): SpannableString {
        val msg = String.format(
            getString(topadsdashboardR.string.topads_insight_recommended_bid_apply),
            bid
        )
        val ss = SpannableString(msg)
        val cs = object : ClickableSpan() {
            override fun onClick(p0: View) {
                binding?.recommendationBudget?.editText?.setText(bid.toString())
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
                if (context != null) {
                    ds.color = ContextCompat.getColor(
                        context!!,
                        unifyprinciplesR.color.Unify_GN500
                    )
                }

                ds.isFakeBoldText = true
            }
        }
        ss.setSpan(cs, msg.length - 8, msg.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        binding?.recommendationBudget?.isInputError = bool
        binding?.recommendationBudget?.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
    }

}
