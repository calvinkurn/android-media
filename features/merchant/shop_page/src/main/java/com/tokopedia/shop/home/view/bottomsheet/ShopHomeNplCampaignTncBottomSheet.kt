package com.tokopedia.shop.home.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.loadLeftDrawable
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.shop.common.view.viewmodel.ShopPageFollowingStatusSharedViewModel
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeCampaignNplTncAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeNplCampaignTncBottomSheetViewModel
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowButton
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase.Companion.ACTION_FOLLOW
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase.Companion.ACTION_UNFOLLOW
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
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
        private const val KEY_RULE_ID = "key_rule_id"
        private const val RULE_ID_33 = 33

        fun createInstance(
                campaignId: String,
                statusCampaign: String,
                shopId: String,
                isOfficialStore: Boolean,
                isGoldMerchant: Boolean,
                ruleId: String
        ) = ShopHomeNplCampaignTncBottomSheet().apply {
            arguments = Bundle().apply {
                putString(KEY_CAMPAIGN_ID, campaignId)
                putString(KEY_STATUS_CAMPAIGN, statusCampaign)
                putString(KEY_SHOP_ID, shopId)
                putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
                putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
                putString(KEY_RULE_ID, ruleId)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var shopPageHomeTracking: ShopPageHomeTracking
    private var viewModel: ShopHomeNplCampaignTncBottomSheetViewModel? = null
    private var shopPageFollowingStatusSharedViewModel: ShopPageFollowingStatusSharedViewModel? = null
    private var campaignId = ""
    private var statusCampaign = ""
    private var shopId = ""
    private var isOfficialStore = false
    private var isGoldMerchant = false
    private var ruleId: String = ""
    private var shopHomeCampaignNplTncAdapter: ShopHomeCampaignNplTncAdapter? = null
    val isOwner: Boolean
        get() = ShopUtil.isMyShop(shopId, viewModel?.userSessionShopId ?: "")
    val isLoggedIn: Boolean
        get() = viewModel?.isUserLoggedIn ?: false
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private var isFollowShop: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearContentPadding = true
        initInjector()
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_shop_campaign_tnc_bottom_sheet, null)
        setChild(view)
        getArgumentsData()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeNplCampaignTncBottomSheetViewModel::class.java)
        shopPageFollowingStatusSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopPageFollowingStatusSharedViewModel::class.java)

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
        getTncBottomSheetData()
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
            when (it) {
                is Success -> {
                    onSuccessGetNplTncData(it.data)
                }
                is Fail -> {
                    onErrorGetNplTncData(it.throwable)
                }
            }
        })

        viewModel?.campaignFollowStatusLiveData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (!isLoggedIn || !isRuleId33(ruleId)) {
                        hideFollowButton()
                    } else {
                        isFollowShop = it.data.status?.userIsFollowing == true
                        showFollowButton(it.data.followButton)
                    }
                }
            }
        })

        viewModel?.followUnfollowShopLiveData?.observe(viewLifecycleOwner, Observer {
            showFollowText()
            when (it) {
                is Success -> {
                    toggleFollowButton(it.data.followShop)
                    shopPageHomeTracking.clickTncBottomSheetFollowButtonNplFollower(
                            isOwner,
                            isFollowShop,
                            shopId,
                            viewModel?.userId.orEmpty(),
                            customDimensionShopPage
                    )
                }
                is Fail -> {
                    onErrorFollowShop(it.throwable)
                }
            }
        })
    }

    private fun onErrorFollowShop(throwable: Throwable) {
        activity?.run {
            NetworkErrorHelper.showCloseSnackbar(this, ErrorHandler.getErrorMessage(this, throwable))
        }
    }

    private fun toggleFollowButton(followShop: FollowShop?) {
        isFollowShop = followShop?.isFollowing == true
        shopPageFollowingStatusSharedViewModel?.setShopPageFollowingStatus(followShop, requireContext())
        followShop?.run { refreshButtonData(this.buttonLabel) }
    }

    private fun refreshButtonData(label: String?) {
        if(!label.isNullOrBlank()) {
            tf_follow.text = label
        }
        btn_follow?.apply {
            if (isFollowShop) {
                buttonVariant = UnifyButton.Variant.GHOST
                tf_follow.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            } else {
                buttonVariant = UnifyButton.Variant.FILLED
                tf_follow.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }
        }
    }

    private fun showFollowText() {
        btn_follow?.isLoading = false
        tf_follow.bringToFront()
        tf_follow.show()
    }

    private fun showLoadingFollowButton() {
        tf_follow.hide()
        btn_follow?.isLoading = true
    }

    private fun showFollowButton(followButton: FollowButton?) {
        layout_button_follow_container?.show()

        showFollowText()
        val voucherUrl = followButton?.voucherIconURL
        followButton?.run { refreshButtonData(this.buttonLabel) }

        if (!voucherUrl.isNullOrBlank()) {
            tf_follow.loadLeftDrawable(
                    context = requireContext(),
                    url = voucherUrl,
                    convertIntoSize = 50
            )
        }

        btn_follow?.apply {
            setOnClickListener {
                showLoadingFollowButton()
                val action = if (isFollowShop) {
                    ACTION_UNFOLLOW
                } else {
                    ACTION_FOLLOW
                }
                viewModel?.updateFollowStatus(shopId, action)
                voucherUrl?.run { tf_follow.removeDrawable() }
            }
        }
    }

    private fun hideFollowButton() {
        layout_button_follow_container?.hide()
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
        recycler_view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val measuredHeight = recycler_view.measuredHeight
        val bottomSheetHeaderHeight = bottomSheetHeader.height
        if ((measuredHeight + bottomSheetHeaderHeight) > getHalfDeviceScreen()) {
            recycler_view.layoutParams.height = getHalfDeviceScreen() - bottomSheetHeaderHeight
        }
    }

    private fun setBottomSheetTitle(title: String) {
        bottomSheetTitle.text = title
    }

    private fun getTncBottomSheetData() {
        showLoading()
        viewModel?.getTncBottomSheetData(campaignId, shopId, isOwner)
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
            ruleId = it.getString(KEY_RULE_ID, "")
        }
    }

    private fun showLoading() {
        loader_unify?.show()
    }

    private fun hideLoading() {
        loader_unify?.hide()
    }

    private fun isRuleId33(ruleId: String): Boolean {
        return ruleId.toIntOrZero() == RULE_ID_33
    }

    private fun getHalfDeviceScreen(): Int = getScreenHeight() / 2
}