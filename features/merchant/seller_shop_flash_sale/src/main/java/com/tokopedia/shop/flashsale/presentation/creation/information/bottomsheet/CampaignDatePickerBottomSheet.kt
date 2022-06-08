package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetCampaignDatePickerBinding
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.GroupedCampaign
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class CampaignDatePickerBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTED_DATE = "selected_date"
        private const val BUNDLE_KEY_MINIMUM_DATE = "minimum_date"

        @JvmStatic
        fun newInstance(
            previouslySelectedDate: Date,
            minimumDate: Date
        ): CampaignDatePickerBottomSheet {
            return CampaignDatePickerBottomSheet().apply {
                arguments = Bundle().apply {
                    putSerializable(BUNDLE_KEY_SELECTED_DATE, previouslySelectedDate)
                    putSerializable(BUNDLE_KEY_MINIMUM_DATE, minimumDate)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SsfsBottomsheetCampaignDatePickerBinding>()
    private var onDatePicked: (Date) -> Unit = {}
    private val previouslySelectedDate by lazy {
        arguments?.getSerializable(BUNDLE_KEY_SELECTED_DATE) as? Date ?: Date()
    }

    private val minimumDate by lazy {
        arguments?.getSerializable(BUNDLE_KEY_MINIMUM_DATE) as? Date ?: Date()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDataPickerViewModel::class.java) }


    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
    }


    private fun setupDependencyInjection() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = SsfsBottomsheetCampaignDatePickerBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        setTitle(getString(R.string.sfs_select_campaign_date))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUpcomingCampaigns()
        viewModel.getUpcomingCampaigns()
    }

    private fun observeUpcomingCampaigns() {
        viewModel.campaigns.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()

            when (result) {
                is Success -> {
                    println()
                    displayCalendar(result.data)
                }
                is Fail -> {
                    binding?.root showError result.throwable
                }
            }
        }
    }


    private fun displayCalendar(campaigns: List<GroupedCampaign>) {
        val legends = campaigns.map { campaign ->
            val campaignCountWording =
                String.format(getString(R.string.sfs_placeholder_campaign_count), campaign.count)
            Legend(campaign.date, campaignCountWording)
        }

        val endDate = dateManager.getMaximumCampaignEndDate()
        val calendar = binding?.unifyCalendar?.calendarPickerView
        calendar?.run {
            init(minimumDate, endDate, legends)
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(previouslySelectedDate)
        }


        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                onDatePicked(date)
                dismiss()
            }

            override fun onDateUnselected(date: Date) {

            }

        })

    }

    fun setOnDatePicked(onDatePicked: (Date) -> Unit) {
        this.onDatePicked = onDatePicked
    }


}