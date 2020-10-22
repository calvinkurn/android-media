package com.tokopedia.shop.home.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeCampaignNplTncAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeNplCampaignTncBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_shop_campaign_tnc_bottom_sheet.*
import javax.inject.Inject

class ShopHomeNplCampaignTncBottomSheet : BottomSheetUnify() {

    companion object {
        private const val KEY_CAMPAIGN_ID = "key_campaign_id"
        private const val KEY_STATUS_CAMPAIGN = "key_status_campaign"
        private const val KEY_SHOP_ID = "key_shop_id"
        private const val KEY_IS_OFFICIAL_STORE = "key_is_official_store"
        private const val KEY_IS_GOLD_MERCHANT = "key_is_gold_merchant"

        fun createInstance(
                campaignId: String,
                statusCampaign: String,
                shopId: String,
                isOfficialStore: Boolean,
                isGoldMerchant: Boolean
        ) = ShopHomeNplCampaignTncBottomSheet().apply {
            arguments = Bundle().apply {
                putString(KEY_CAMPAIGN_ID, campaignId)
                putString(KEY_STATUS_CAMPAIGN, statusCampaign)
                putString(KEY_SHOP_ID, shopId)
                putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
                putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var shopPageHomeTracking: ShopPageHomeTracking
    private var viewModel: ShopHomeNplCampaignTncBottomSheetViewModel? = null
    private var campaignId = ""
    private var statusCampaign = ""
    private var shopId = ""
    private var isOfficialStore = false
    private var isGoldMerchant = false

    private var shopHomeCampaignNplTncAdapter: ShopHomeCampaignNplTncAdapter? = null
    val isOwner: Boolean
        get() = ShopUtil.isMyShop(shopId, viewModel?.userSessionShopId ?: "")
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_shop_campaign_tnc_bottom_sheet, null)
        setChild(view)
        getArgumentsData()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeNplCampaignTncBottomSheetViewModel::class.java)
    }

    private fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                    .builder()
                    .shopPageHomeModule(ShopPageHomeModule())
                    .shopComponent(ShopComponentHelper().getComponent(application, this))
                    .build()
                    .inject(this@ShopHomeNplCampaignTncBottomSheet)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeLiveData()
        getTncData()
        sendImpressionTrackerTncPage()
        setCloseClickListener {
            dismiss()
            sendClickCloseTrackerTncPage()
        }
    }

    private fun sendClickCloseTrackerTncPage() {
        shopPageHomeTracking.clickCloseTncPage(
                isOwner,
                statusCampaign,
                customDimensionShopPage
        )
    }

    private fun sendImpressionTrackerTncPage() {
        shopPageHomeTracking.impressionTncPage(
                isOwner,
                statusCampaign,
                customDimensionShopPage
        )
    }

    private fun observeLiveData() {
        viewModel?.campaignTncLiveData?.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when(it){
                is Success -> {
                    onSuccessGetNplTncData(it.data)
                }
                is Fail -> {
                    onErrorGetNplTncData(it.throwable)
                }
            }
        })
    }

    private fun onErrorGetNplTncData(error: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(context, error)
        showToasterError(errorMessage)
    }

    private fun showToasterError(message: String) {
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }


    private fun onSuccessGetNplTncData(data: ShopHomeCampaignNplTncUiModel) {
        setBottomSheetTitle(data.title)
        shopHomeCampaignNplTncAdapter?.setListMessageDat(data.listMessage)
    }

    private fun setBottomSheetTitle(title: String) {
        bottomSheetTitle.text = title
    }

    private fun getTncData() {
        showLoading()
        viewModel?.getTnc(campaignId)
    }

    private fun initRecyclerView() {
        recycler_view?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        shopHomeCampaignNplTncAdapter = ShopHomeCampaignNplTncAdapter()
        recycler_view?.adapter = shopHomeCampaignNplTncAdapter
    }

    private fun getArgumentsData() {
        arguments?.let {
            campaignId = it.getString(KEY_CAMPAIGN_ID, "")
            statusCampaign = it.getString(KEY_STATUS_CAMPAIGN, "")
            shopId = it.getString(KEY_SHOP_ID, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL_STORE, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
        }
    }

    private fun showLoading(){
        loader_unify?.show()
    }

    private fun hideLoading(){
        loader_unify?.hide()
    }
}