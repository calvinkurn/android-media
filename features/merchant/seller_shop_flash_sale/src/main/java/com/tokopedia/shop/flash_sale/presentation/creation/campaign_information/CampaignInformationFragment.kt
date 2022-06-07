package com.tokopedia.shop.flash_sale.presentation.creation.campaign_information

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignInformationBinding
import com.tokopedia.shop.flash_sale.common.util.DateManager
import com.tokopedia.shop.flash_sale.common.util.doOnTextChanged
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.domain.entity.Gradient
import com.tokopedia.shop.flash_sale.domain.entity.enums.PageMode
import com.tokopedia.shop.flash_sale.presentation.campaign_list.list.CampaignListViewModel
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.adapter.GradientColorAdapter
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class CampaignInformationFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val FIRST_STEP = 1
        private const val SPAN_COUNT = 6

        @JvmStatic
        fun newInstance(pageMode: PageMode): CampaignInformationFragment {
            val fragment = CampaignInformationFragment()
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_KEY_PAGE_MODE, pageMode)
            fragment.arguments = bundle
            return fragment
        }

    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignInformationBinding>()
    private val pageMode by lazy { arguments?.getParcelable(BUNDLE_KEY_PAGE_MODE) as? PageMode }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dateManager: DateManager

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignInformationViewModel::class.java) }

    @SuppressLint("UnsupportedDarkModeColor")
    private val colors = listOf(
        Gradient("#26A116", "#60BB55"),
        Gradient("#019751", "#00AA5B"),
        Gradient("#00615B", "#04837E"),
        Gradient("#2B4A62", "#3E6786"),
        Gradient("#2059A1", "#2F89FC"),
        Gradient("#1C829E", "#28B9E1"),

        Gradient("#EF4C60", "#E96E7D"),
        Gradient("#AE1720", "#D74049"),
        Gradient("#850623", "#B31D40"),
        Gradient("#B8266C", "#CD5D99"),
        Gradient("#4F2DC0", "#7F66D1"),
        Gradient("#58197E", "#854C9E"),

        Gradient("#685447", "#836857"),
        Gradient("#DD8F00", "#F8A400"),
    )

    override fun getScreenName(): String =
        CampaignInformationFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
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
        binding = SsfsFragmentCampaignInformationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        displayDatePicker()
    }

    private fun setupView() {
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupTextFields()
    }

    private fun setupRecyclerView() {
        val recyclerViewAdapter = GradientColorAdapter()
        binding?.recyclerView?.apply {
            layoutManager = GridLayoutManager(requireActivity(), SPAN_COUNT)
            adapter = recyclerViewAdapter
        }
        recyclerViewAdapter.submit(colors)
        recyclerViewAdapter.setOnGradientClicked { gradient ->

        }
    }

    private fun setupToolbar() {
        binding?.header?.headerSubTitle =
            String.format(getString(R.string.sfs_placeholder_step_counter), FIRST_STEP)
    }

    private fun setupTextFields() {
        binding?.run {
            tauCampaignName.textInputLayout.editText?.doOnTextChanged { text ->

                println(text)
            }
        }
    }

    private fun setupClickListeners() {
        binding?.run {
            btnDraft.setOnClickListener {

            }
            btnCreateCampaign.setOnClickListener {
                val startDate = Date()
                val endDate = Date()
                val teaserDate = dateManager.decreaseHourBy(startDate, 4)

                val selection = CampaignInformationViewModel.Selection(
                    binding?.tauCampaignName?.editText?.text.toString().trim(),
                    startDate,
                    endDate,
                    binding?.switchTeaser?.isChecked.orFalse(),
                    teaserDate,
                    "",
                    ""
                )

                viewModel.validateInput(selection)
            }
        }
    }

    private fun displayDatePicker() {
        val bottomSheet = CampaignDatePickerBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


}