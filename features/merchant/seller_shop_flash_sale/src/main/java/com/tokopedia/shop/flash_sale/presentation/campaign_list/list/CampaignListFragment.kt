package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListBinding
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.presentation.campaign_list.container.CampaignListContainerViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class CampaignListFragment: BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_NAME = "status_name"
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_ID = "status_id"
        private const val BUNDLE_KEY_PRODUCT_COUNT = "product_count"
        private const val PAGE_SIZE = 10
        private const val MAX_PRODUCT_SELECTION = 5
        private const val ONE_PRODUCT = 1
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 300
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance(
            campaignStatusName : String,
            campaignStatusId: Int,
            productCount : Int,
        ): CampaignListFragment {
            val fragment = CampaignListFragment()
            fragment.arguments = Bundle().apply {
                putString(BUNDLE_KEY_CAMPAIGN_STATUS_NAME, campaignStatusName)
                putInt(BUNDLE_KEY_CAMPAIGN_STATUS_ID, campaignStatusId)
                putInt(BUNDLE_KEY_PRODUCT_COUNT, productCount)
            }
            return fragment
        }

    }

    private val campaignStatusName by lazy {
        arguments?.getString(BUNDLE_KEY_CAMPAIGN_STATUS_NAME).orEmpty()
    }

    private val campaignStatusId by lazy {
        arguments?.getInt(BUNDLE_KEY_CAMPAIGN_STATUS_ID).orZero()
    }

    private val productCount by lazy {
        arguments?.getInt(BUNDLE_KEY_PRODUCT_COUNT).orZero()
    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignListBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListViewModel::class.java) }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}
    
    override fun getScreenName(): String = CampaignListFragment::class.java.canonicalName.orEmpty()
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
        binding = SsfsFragmentCampaignListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeCampaigns()
        observeCampaignAttribute()
        observeCampaignCreation()
        viewModel.getCampaigns(10, 1, emptyList(), "", false)
        viewModel.getCampaignAttribute(5, 2022)
        /*viewModel.createCampaign(
            "campaign-test",
            GregorianCalendar(2022, 7, 10, 13, 0, 0).time,
            GregorianCalendar(2022, 7, 15, 13, 0, 0).time
        )*/
    }

    private fun setupView() {

    }

    private fun observeCampaigns() {
        viewModel.campaigns.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val campaigns = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeCampaignAttribute() {
        viewModel.campaignAttribute.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val attribute = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeCampaignCreation() {
        viewModel.campaignCreation.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val creationResult = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
    }

}