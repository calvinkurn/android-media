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
import com.tokopedia.calendar.SubTitle
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsBottomsheetCampaignDatePickerBinding
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
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
        observe()
        setupView()
        viewModel.validateInput()
    }

    private fun setupView() {
        setupCalendar()
    }

    private fun setupCalendar() {
        val endDate = dateManager.getMaximumCampaignEndDate()
        val subtitles = arrayListOf(SubTitle(minimumDate, "Merdeka", "#EF4C60"))


        val calendar = binding?.unifyCalendar?.calendarPickerView
        calendar?.run {
            init(minimumDate, endDate, listOf(Legend(minimumDate, "Ready")))
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(previouslySelectedDate)
                .withSubTitles(subtitles)
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

    private fun observe() {
        viewModel.areInputValid.observe(viewLifecycleOwner) {

        }
    }


}