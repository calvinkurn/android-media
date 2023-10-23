package com.tokopedia.topads.edit.view.sheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.view.sheet.CreatePotentialPerformanceSheet
import com.tokopedia.topads.common.view.sheet.TopAdsToolTipBottomSheet
import com.tokopedia.topads.edit.databinding.TopadsEditSheetEditAdGroupRecommendationBidBinding
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.viewmodel.EditAdGroupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.edit.R as topadseditR
import com.tokopedia.topads.common.R as topadscommonR

class EditAdGroupRecommendationBidBottomSheet : BottomSheetUnify() {

    private var dailyBudgetInput: Float = Int.ZERO.toFloat()
    private var performanceData: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel> = mutableListOf()
    private var maxBid: String = Int.ZERO.toString()
    private var minBid: String = Int.ZERO.toString()
    private var productListIds: MutableList<String> = mutableListOf()
    private var binding: TopadsEditSheetEditAdGroupRecommendationBidBinding? = null
    private var priceBid: Float? = Int.ZERO.toFloat()
    private var clickListener: ((priceBid: String) -> Unit)? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider[EditAdGroupViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        DaggerTopAdsEditComponent.builder().baseAppComponent(
            ((activity as Activity).application as BaseMainApplication)
                .baseAppComponent).topAdEditModule(context?.let { TopAdEditModule(it) })
            .build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val viewBinding = TopadsEditSheetEditAdGroupRecommendationBidBinding.inflate(inflater, container, false)
        binding = viewBinding
        isHideable = true
        showCloseIcon = true
        setChild(viewBinding.root)
        setTitle(getString(topadseditR.string.top_ads_edit_ad_group_recommendation_bid_bottom_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getInitialData()
        setListeners()
        observeLiveData()
    }

    private fun getInitialData() {
        priceBid?.let { viewModel.getBrowsePerformanceData(productListIds, it, it, dailyBudgetInput) }
    }

    private fun observeLiveData() {
        viewModel.browsePerformanceData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val data = it.data.umpGetImpressionPrediction.impressionPredictionData.impression
                    binding?.amount?.text = String.format(getString(topadseditR.string.top_ads_recommendation_performance), data.finalImpression)
                    if (data.increment != 0) {
                        binding?.percentage?.text = String.format(getString(topadseditR.string.top_ads_performance_increment_text_int), data.increment)
                        binding?.percentage?.show()
                    } else {
                        binding?.percentage?.hide()
                    }
                }

                else -> {}
            }
        }
    }

    private fun setListeners() {
        binding?.textField?.editText?.addTextChangedListener(object : NumberTextWatcher(binding?.textField?.editText!!) {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                when {
                    number > maxBid.toDoubleOrZero() -> {
                        setMessageErrorField(getString(topadscommonR.string.max_bid_error_new), maxBid, true)
                        binding?.editAdGroupNameCta?.isEnabled = false
                    }

                    number < minBid.toDoubleOrZero() -> {
                        setMessageErrorField(getString(topadscommonR.string.min_bid_error_new), minBid, true)
                        binding?.editAdGroupNameCta?.isEnabled = false
                    }

                    else -> {
                        if (number % 50 == 0.0) {
                            setMessageErrorField(getString(topadscommonR.string.topads_ads_optimal_bid), Int.ZERO.toString(), false)
                            priceBid?.let { viewModel.getBrowsePerformanceData(productListIds, number.toFloat(), it, dailyBudgetInput) }
                            binding?.editAdGroupNameCta?.isEnabled = true
                        } else {
                            setMessageErrorField(getString(topadscommonR.string.topads_ads_error_multiple_fifty), Int.ZERO.toString(), true)
                            binding?.editAdGroupNameCta?.isEnabled = false
                        }
                    }

                }
            }
        })

        binding?.editAdGroupNameCta?.setOnClickListener {
            clickListener?.invoke(binding?.textField?.editText?.text.toString())
            dismiss()
        }
        binding?.browseTxtInfo?.setOnClickListener {
            TopAdsToolTipBottomSheet.newInstance().also {
                it.setTitle(getString(topadseditR.string.edit_ad_item_title_ads_recommendation))
                it.setDescription(getString(topadseditR.string.top_ads_browse_bid_tooltip_desxription))
            }.show(childFragmentManager)
        }

        binding?.icon?.setOnClickListener {
            CreatePotentialPerformanceSheet.newInstance(
                performanceData.firstOrNull()?.retention.toIntOrZero(),
                performanceData.getOrNull(Int.ONE)?.retention.toIntOrZero()
            ).show(childFragmentManager)
        }
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        binding?.textField?.isInputError = bool
        binding?.textField?.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
    }

    private fun initView() {
        context?.let {
            binding?.textField?.editText?.setText(CurrencyFormatHelper.convertToRupiah(priceBid?.toInt().toString()))
            binding?.browseTxtInfo?.text = MethodChecker.fromHtml(getString(topadseditR.string.top_ads_text_info_browse_bid))
        }
    }

    fun show(
        fragmentManager: FragmentManager,
        clickListener: (priceBid: String) -> Unit
    ) {
        show(fragmentManager, TOPADS_BOTTOM_SHEET_ACTION_TAG)
        this.clickListener = clickListener
    }

    companion object {
        private const val TOPADS_BOTTOM_SHEET_ACTION_TAG = "EDIT_AD_GROUP_RECOMMENDATION_BID_BOTTOM_SHEET_TAG"
        fun newInstance(priceBid: Float?, productListIds: MutableList<String>,
                        minBid: String, maxBid: String,
                        performanceData: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>,
                        dailyBudgetInput: Float): EditAdGroupRecommendationBidBottomSheet = EditAdGroupRecommendationBidBottomSheet().apply {
            this.priceBid = priceBid
            this.productListIds = productListIds
            this.minBid = minBid
            this.maxBid = maxBid
            this.performanceData = performanceData
            this.dailyBudgetInput = dailyBudgetInput
        }

    }
}
