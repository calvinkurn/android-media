package com.tokopedia.topads.edit.view.sheet

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.edit.databinding.TopadsEditSheetEditAdGroupRecommendationBidBinding
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.viewmodel.EditAdGroupViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.text.currency.NumberTextWatcher
import javax.inject.Inject
import com.tokopedia.topads.edit.R as topadseditR

class EditAdGroupRecommendationBidBottomSheet : BottomSheetUnify() {

    private var productListIds: MutableList<String> = mutableListOf()
    private var binding: TopadsEditSheetEditAdGroupRecommendationBidBinding? = null
    private var priceBid: Float? = 0f
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
        setListeners()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel?.browsePerformanceData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val data = it.data.umpGetImpressionPrediction.impressionPredictionData.impression
                    binding?.amount?.text = String.format("%dx", data.finalImpression)
                    if (data.increment != 0) {
                        binding?.percentage?.text = String.format("%d %% meningkat", data.increment)
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
                if (number == priceBid?.toDouble()) {
                    binding?.editAdGroupNameCta?.isEnabled = false
                } else {
                    binding?.editAdGroupNameCta?.isEnabled = true
                    priceBid?.let { viewModel?.getBrowsePerformanceData(productListIds, number.toFloat(), it, 0f) }
                }

            }
        })

        binding?.editAdGroupNameCta?.setOnClickListener {
            clickListener?.invoke(binding?.textField?.editText?.text.toString())
            dismiss()
        }
    }

    private fun initView() {
        context?.let {
            binding?.textField?.editText?.setText(priceBid.toString())
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
        fun newInstance(priceBid: Float?, productListIds: MutableList<String>): EditAdGroupRecommendationBidBottomSheet = EditAdGroupRecommendationBidBottomSheet().apply {
            this.priceBid = priceBid
            this.productListIds = productListIds
        }
    }
}
