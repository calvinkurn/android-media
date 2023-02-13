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
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetCampaignDatePickerBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.doOnDelayFinished
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.localFormatTo
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.GroupedCampaign
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.Date
import javax.inject.Inject

class CampaignDatePickerBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_SELECTION_MODE = "mode"
        private const val BUNDLE_KEY_SELECTED_DATE = "selected_date"
        private const val BUNDLE_KEY_MINIMUM_DATE = "minimum_date"
        private const val BUNDLE_KEY_MAXIMUM_DATE = "maximum_date"
        private const val BUNDLE_KEY_SELECTED_VPS_PACKAGE = "selected_vps_package"
        private const val DISMISS_BOTTOM_SHEET_DELAY_IN_MILLIS: Long = 500

        @JvmStatic
        fun newInstance(
            mode: TimePickerSelectionMode,
            selectedDate: Date,
            minimumDate: Date,
            maximumDate: Date,
            vpsPackage: VpsPackageUiModel
        ): CampaignDatePickerBottomSheet {
            return CampaignDatePickerBottomSheet().apply {
                arguments = Bundle().apply {
                    putSerializable(BUNDLE_KEY_SELECTION_MODE, mode)
                    putSerializable(BUNDLE_KEY_SELECTED_DATE, selectedDate)
                    putSerializable(BUNDLE_KEY_MINIMUM_DATE, minimumDate)
                    putSerializable(BUNDLE_KEY_MAXIMUM_DATE, maximumDate)
                    putParcelable(BUNDLE_KEY_SELECTED_VPS_PACKAGE, vpsPackage)
                }
            }
        }
    }

    private var binding by autoClearedNullable<SsfsBottomsheetCampaignDatePickerBinding>()
    private var onDateTimePicked: (Date) -> Unit = {}
    private val mode by lazy {
        arguments?.getSerializable(BUNDLE_KEY_SELECTION_MODE) as? TimePickerSelectionMode ?: TimePickerSelectionMode.CAMPAIGN_START_DATE
    }
    private val selectedDate by lazy {
        arguments?.getSerializable(BUNDLE_KEY_SELECTED_DATE) as? Date ?: Date()
    }

    private val minimumDate by lazy {
        arguments?.getSerializable(BUNDLE_KEY_MINIMUM_DATE) as? Date ?: Date()
    }

    private val maximumDate by lazy {
        arguments?.getSerializable(BUNDLE_KEY_MAXIMUM_DATE) as? Date ?: Date()
    }

    private val vpsPackage by lazy {
        arguments?.getParcelable(BUNDLE_KEY_SELECTED_VPS_PACKAGE) as? VpsPackageUiModel
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
        observeCampaignQuota()
        viewModel.getUpcomingCampaigns()
        viewModel.getCampaignQuota(
            dateManager.getCurrentMonth(),
            dateManager.getCurrentYear(),
            vpsPackage?.packageId.orZero()
        )
    }

    private fun observeUpcomingCampaigns() {
        viewModel.campaigns.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()
            when (result) {
                is Success -> {
                    binding?.tpgDateDescription?.visible()
                    binding?.unifyCalendar?.visible()
                    displayCalendar(result.data)
                    displaySelectedVpsPackagePeriod(vpsPackage)
                }
                is Fail -> {
                    binding?.tpgDateDescription?.gone()
                    binding?.unifyCalendar?.gone()
                    binding?.root showError result.throwable
                }
            }
        }
    }

    private fun observeCampaignQuota() {
        viewModel.campaignQuota.observe(viewLifecycleOwner) { result ->
            binding?.loader?.gone()
            when (result) {
                is Success -> {
                    val remainingQuota = result.data
                    handleRemainingQuota(remainingQuota)
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


        val calendar = binding?.unifyCalendar?.calendarPickerView

        val normalizedMinDate = normalizeMinimumDate()
        val initializer = calendar?.init(normalizedMinDate, maximumDate, legends)
        initializer?.inMode(CalendarPickerView.SelectionMode.SINGLE)

        if (selectedDate.after(minimumDate) && selectedDate.before(maximumDate)) {
            initializer?.withSelectedDate(selectedDate)
        }

        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                showTimePicker(
                    date,
                    selectedDate,
                    minimumDate,
                    maximumDate,
                    onTimePicked = { dateTime ->
                        doOnDelayFinished(DISMISS_BOTTOM_SHEET_DELAY_IN_MILLIS) {
                            onDateTimePicked(dateTime)
                            dismiss()
                        }
                    }
                )
            }

            override fun onDateUnselected(date: Date) {

            }

        })

    }

    private fun displaySelectedVpsPackagePeriod(vpsPackage: VpsPackageUiModel?) {
        val isShopTierBenefit = vpsPackage?.isShopTierBenefit.orFalse()

        val startDate = if (isShopTierBenefit) {
            minimumDate.formatTo(DateConstant.DATE)
        } else {
            vpsPackage?.packageStartTime?.localFormatTo(DateConstant.DATE)
        }

        val endDate = if (isShopTierBenefit) {
           maximumDate.formatTo(DateConstant.DATE)
        } else {
            vpsPackage?.packageEndTime?.localFormatTo(DateConstant.DATE)
        }

        val template = getString(R.string.sfs_placeholder_date_picker_description)
        val period = String.format(template, startDate, endDate)
        binding?.tpgDateDescription?.text = period
    }

    fun setOnDateTimePicked(onDatePicked: (Date) -> Unit) {
        this.onDateTimePicked = onDatePicked
    }

    fun showTimePicker(
        selectedDateFromCalendar: Date,
        defaultDate: Date,
        minimumDate: Date,
        maximumDate: Date,
        onTimePicked: (Date) -> Unit
    ) {
        val title = getString(R.string.sfs_select_campaign_time)
        val info = String.format(
            getString(R.string.sfs_placeholder_selected_date),
            selectedDateFromCalendar.localFormatTo(DateConstant.DATE)
        )
        val buttonWording = getString(R.string.sfs_apply)
        val isVpsPackage = !vpsPackage?.isShopTierBenefit.orFalse()
        val param = TimePickerHandler.Param(
            mode,
            selectedDateFromCalendar,
            defaultDate,
            minimumDate,
            maximumDate,
            title,
            info,
            buttonWording,
            isVpsPackage
        )

        val timePickerHandler = TimePickerHandler(param)
        timePickerHandler.show(activity ?: return, childFragmentManager, onTimePicked)
    }

    private fun handleRemainingQuota(remainingQuota: Int) {
        if (remainingQuota == Constant.ZERO) {
            val monthName = dateManager.getCurrentDate().formatTo(DateConstant.MONTH)
            val emptyQuotaWording =
                String.format(getString(R.string.sfs_placeholder_empty_quota), monthName)
            binding?.tpgErrorMessage?.text = emptyQuotaWording
            binding?.tpgErrorMessage?.visible()
        }
    }

    //A workaround to prevent force close issue since unify calendar not support minimumDate bigger than maximumDate
    private fun normalizeMinimumDate() : Date {
        return if (minimumDate.after(maximumDate)) {
            maximumDate
        } else {
            minimumDate
        }
    }
}