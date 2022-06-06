package com.tokopedia.shop.flash_sale.presentation.creation.campaign_information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignInformationBinding
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.domain.entity.enums.PageMode
import com.tokopedia.shop.flash_sale.presentation.creation.campaign_information.bottomsheet.CampaignDatePickerBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignInformationFragment: BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_PAGE_MODE = "page_mode"
        private const val FIRST_STEP = 1

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

    override fun getScreenName(): String = CampaignInformationFragment::class.java.canonicalName.orEmpty()
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
    }

    private fun setupToolbar() {
        binding?.header?.headerSubTitle = String.format(getString(R.string.sfs_placeholder_step_counter), FIRST_STEP)
    }


    private fun displayDatePicker() {
        val bottomSheet = CampaignDatePickerBottomSheet.newInstance()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }


}