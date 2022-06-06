package com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.bottomsheet

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
import com.tokopedia.shop.flash_sale.common.util.DateManager
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class CampaignDatePickerBottomSheet : BottomSheetUnify() {

    companion object {
        private const val BUNDLE_KEY_TITLE = "title"

        @JvmStatic
        fun newInstance(): CampaignDatePickerBottomSheet {
            return CampaignDatePickerBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(BUNDLE_KEY_TITLE, "")
                }
            }
        }

    }

    private val title by lazy { arguments?.getString(BUNDLE_KEY_TITLE).orEmpty() }
    private var binding by autoClearedNullable<SsfsBottomsheetCampaignDatePickerBinding>()


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
        observeSelectedDate()
        setupView()
        viewModel.validateInput()
    }

    private fun setupView() {
        setupCalendar()


    }

    private fun setupCalendar() {
        val startDate = dateManager.getMinimumCampaignStartDate()
        val endDate = dateManager.getMaximumCampaignEndDate()
        val subtitles = arrayListOf(SubTitle(startDate, "Merdeka"))


        val calendar = binding?.unifyCalendar?.calendarPickerView
        calendar?.run {
            init(startDate, endDate, listOf(Legend(startDate, "Ready")))
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withSelectedDate(Date())
                .withSubTitles(subtitles)
        }

    }

    private fun observeSelectedDate() {
        viewModel.areInputValid.observe(viewLifecycleOwner) {

        }
    }


}