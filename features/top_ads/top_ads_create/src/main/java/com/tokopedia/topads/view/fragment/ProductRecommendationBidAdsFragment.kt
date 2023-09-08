package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.sheet.TopAdsToolTipBottomSheet
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
            (activity as StepperActivity).updateToolbarTitle("Atur Iklan di Rekomendasi")
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

                else -> {
                    Log.e("BidsInfo", "Inside setObservers $it")
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEditTextListeners()
        setupView()
//        if (stepperModel?.goToSummary == true) {
//            setRestoreValue()
//        } else {
//            viewModel.getSuggestionKeyword(productId
//                ?: "", 0, this::onSuccessSuggestionKeyword, this::onEmptySuggestion)
//        }
//        setCount()
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
                it.setTitle("Iklan di Rekomendasi")
                it.setDescription(
                    "Iklanmu akan tampil pada berbagai halaman strategis seperti di halaman home, keranjang, inbox dan detail produk.\n" +
                        "\n" +
                        "Sistem Tokopedia akan menampilkan iklan produkmu dengan kemampuan teknologi yang dapat menyesuaikan ketertarikan dan riwayat penjelajahan calon pembeli memungkinkan iklanmu menjangkau calon pembeli dengan lebih luas dan tepat.\n" +
                        "\n" +
                        "Tips:\n" +
                        "Semakin tinggi biaya iklanmu, maka semakin tinggi peluang iklanmu ditampilkan."
                )
            }.show(childFragmentManager)
        }
        binding?.infoImpressionPrediction?.setOnClickListener {
            stepperModel?.let {
                TopAdsPredictionImpressionBottomSheet.newInstance(searchPrediction = it.searchPrediction, recomPrediction = it.recomPrediction, totalPrediction = 0).apply {

                }.show(childFragmentManager)
            }

        }
    }

    private fun setEditTextListeners() {

        binding?.recommendationBudget?.editText?.addTextChangedListener(object : NumberTextWatcher(binding?.recommendationBudget?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                when {
                    result >= (stepperModel?.finalRecommendationBidPerClick ?: 0) -> {
                        finalRecomBid = result.toString()
                        binding?.recommendationBudget?.isInputError = false
                        binding?.recommendationBudget?.setMessage(MethodChecker.fromHtml(String.format("Biaya optimal ✔️", "0")))
                        stepperModel?.selectedProductIds?.let {
                            viewModel?.getPerformanceData(it, result.toFloat(), -1f, -1f)
                        }
                    }

                    else -> {
                        stepperModel?.finalRecommendationBidPerClick?.let {
                            binding?.recommendationBudget?.setMessage(getClickableString(it))
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

}
