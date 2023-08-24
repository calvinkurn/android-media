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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.analytic.ShopPageHomeTracking
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowButton
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase.Companion.ACTION_FOLLOW
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase.Companion.ACTION_UNFOLLOW
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.loadLeftDrawable
import com.tokopedia.shop.common.util.removeDrawable
import com.tokopedia.shop.common.view.viewmodel.ShopPageFollowingStatusSharedViewModel
import com.tokopedia.shop.databinding.FragmentShopCampaignTncBottomSheetBinding
import com.tokopedia.shop.home.di.component.DaggerShopPageHomeComponent
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeCampaignNplTncAdapter
import com.tokopedia.shop.home.view.model.ShopHomeCampaignNplTncUiModel
import com.tokopedia.shop.home.view.viewmodel.ShopHomeNplCampaignTncBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

//TODO need to combine this bottomsheet with ShopHomeCampaignTncBottomSheet in the future, since both of them have the same purpose
class ShopHomeNplCampaignTncBottomSheet : BottomSheetUnify() {

    companion object {
        private const val KEY_CAMPAIGN_ID = "key_campaign_id"
        private const val KEY_STATUS_CAMPAIGN = "key_status_campaign"
        private const val KEY_SHOP_ID = "key_shop_id"
        private const val KEY_IS_OFFICIAL_STORE = "key_is_official_store"
        private const val KEY_IS_GOLD_MERCHANT = "key_is_gold_merchant"
        private const val KEY_LIST_RULE_ID = "key_list_rule_id"
        private const val RULE_ID_FOLLOWERS_ONLY = 33

        fun createInstance(
            campaignId: String,
            statusCampaign: String,
            shopId: String,
            isOfficialStore: Boolean,
            isGoldMerchant: Boolean,
            listRuleId: List<String>
        ) = ShopHomeNplCampaignTncBottomSheet().apply {
            arguments = Bundle().apply {
                putString(KEY_CAMPAIGN_ID, campaignId)
                putString(KEY_STATUS_CAMPAIGN, statusCampaign)
                putString(KEY_SHOP_ID, shopId)
                putBoolean(KEY_IS_OFFICIAL_STORE, isOfficialStore)
                putBoolean(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
                putStringArrayList(KEY_LIST_RULE_ID, ArrayList(listRuleId))
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
    private var listRuleId: List<String> = listOf()
    private var shopHomeCampaignNplTncAdapter: ShopHomeCampaignNplTncAdapter? = null
    val isOwner: Boolean
        get() = ShopUtil.isMyShop(shopId, viewModel?.userSessionShopId ?: "")
    val isLoggedIn: Boolean
        get() = viewModel?.isUserLoggedIn ?: false
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficialStore, isGoldMerchant)
    }
    private var isFollowShop: Boolean = false
    private var tfFollow: Typography? = null
    private var btnFollow: UnifyButton? = null
    private var layoutButtonFollowContainer: View? = null
    private var recyclerView: RecyclerView? = null
    private var loaderUnify: LoaderUnify? = null
    private var viewBinding by autoClearedNullable<FragmentShopCampaignTncBottomSheetBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearContentPadding = true
        initInjector()
        viewBinding = FragmentShopCampaignTncBottomSheetBinding.inflate(LayoutInflater.from(context))
        initView()
        setChild(viewBinding?.root)
        getArgumentsData()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopHomeNplCampaignTncBottomSheetViewModel::class.java)
        shopPageFollowingStatusSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShopPageFollowingStatusSharedViewModel::class.java)
    }

    private fun initView() {
        tfFollow = viewBinding?.tfFollow
        btnFollow = viewBinding?.btnFollow
        layoutButtonFollowContainer = viewBinding?.layoutButtonFollowContainer
        recyclerView = viewBinding?.recyclerView
        loaderUnify = viewBinding?.loaderUnify
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
        viewModel?.campaignTncLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                hideLoading()
                when (it) {
                    is Success -> {
                        onSuccessGetNplTncData(it.data)
                    }
                    is Fail -> {
                        onErrorGetNplTncData(it.throwable)
                    }
                }
            }
        )

        viewModel?.campaignFollowStatusLiveData?.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (!isLoggedIn || !isRuleIdFollowersOnly(listRuleId)) {
                            hideFollowButton()
                        } else {
                            isFollowShop = it.data.status?.userIsFollowing == true
                            showFollowButton(it.data.followButton)
                        }
                    }
                    else -> {
                        //no-op
                    }
                }
            }
        )

        viewModel?.followUnfollowShopLiveData?.observe(
            viewLifecycleOwner,
            Observer {
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
            }
        )
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
        if (!label.isNullOrBlank()) {
            tfFollow?.text = label
        }
        btnFollow?.apply {
            if (isFollowShop) {
                buttonVariant = UnifyButton.Variant.GHOST
                tfFollow?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
            } else {
                buttonVariant = UnifyButton.Variant.FILLED
                tfFollow?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background))
            }
        }
    }

    private fun showFollowText() {
        btnFollow?.isLoading = false
        tfFollow?.bringToFront()
        tfFollow?.show()
    }

    private fun showLoadingFollowButton() {
        tfFollow?.hide()
        btnFollow?.isLoading = true
    }

    private fun showFollowButton(followButton: FollowButton?) {
        layoutButtonFollowContainer?.show()

        showFollowText()
        val voucherUrl = followButton?.voucherIconURL
        followButton?.run { refreshButtonData(this.buttonLabel) }

        if (!voucherUrl.isNullOrBlank()) {
            tfFollow?.loadLeftDrawable(
                context = requireContext(),
                url = voucherUrl,
                convertIntoSize = 50
            )
        }

        btnFollow?.apply {
            setOnClickListener {
                showLoadingFollowButton()
                val action = if (isFollowShop) {
                    ACTION_UNFOLLOW
                } else {
                    ACTION_FOLLOW
                }
                viewModel?.updateFollowStatus(shopId, action)
                voucherUrl?.run { tfFollow?.removeDrawable() }
            }
        }
    }

    private fun hideFollowButton() {
        layoutButtonFollowContainer?.hide()
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
        recyclerView?.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
        val measuredHeight = recyclerView?.measuredHeight.orZero()
        val bottomSheetHeaderHeight = bottomSheetHeader.height
        if ((measuredHeight + bottomSheetHeaderHeight) > getHalfDeviceScreen()) {
            recyclerView?.layoutParams?.height = getHalfDeviceScreen() - bottomSheetHeaderHeight
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
        recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        shopHomeCampaignNplTncAdapter = ShopHomeCampaignNplTncAdapter()
        recyclerView?.adapter = shopHomeCampaignNplTncAdapter
    }

    private fun getArgumentsData() {
        arguments?.let {
            campaignId = it.getString(KEY_CAMPAIGN_ID, "")
            statusCampaign = it.getString(KEY_STATUS_CAMPAIGN, "")
            shopId = it.getString(KEY_SHOP_ID, "")
            isOfficialStore = it.getBoolean(KEY_IS_OFFICIAL_STORE, false)
            isGoldMerchant = it.getBoolean(KEY_IS_GOLD_MERCHANT, false)
            listRuleId = it.getStringArrayList(KEY_LIST_RULE_ID)?.toList().orEmpty()
        }
    }

    private fun showLoading() {
        loaderUnify?.show()
    }

    private fun hideLoading() {
        loaderUnify?.hide()
    }

    private fun isRuleIdFollowersOnly(listRuleId: List<String>): Boolean {
        return listRuleId.any { it.toIntOrZero() == RULE_ID_FOLLOWERS_ONLY }
    }

    private fun getHalfDeviceScreen(): Int = getScreenHeight() / 2
}
