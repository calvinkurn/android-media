package com.tokopedia.shop.flashsale.presentation.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsCampaignDetailPerformanceLayoutBinding
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignDetailBinding
import com.tokopedia.shop.flashsale.common.constant.BundleConstant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.setFragmentToUnifyBgColor
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.common.share_component.ShareComponentInstanceBuilder
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignDetailMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponent
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.entity.enums.isActive
import com.tokopedia.shop.flashsale.domain.entity.enums.isAvailable
import com.tokopedia.shop.flashsale.domain.entity.enums.isCancelled
import com.tokopedia.shop.flashsale.domain.entity.enums.isFinished
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.shop.flashsale.domain.entity.enums.isUpcoming
import com.tokopedia.shop.flashsale.presentation.cancelation.CancelCampaignBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.MerchantCampaignTNCBottomSheet
import com.tokopedia.shop.flashsale.presentation.detail.adapter.CampaignDetailProductListAdapter
import com.tokopedia.shop.flashsale.presentation.detail.bottomsheet.CampaignDetailMoreMenuBottomSheet
import com.tokopedia.shop.flashsale.presentation.detail.bottomsheet.CampaignDetailMoreMenuClickListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment(),
    CampaignDetailMoreMenuClickListener {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_NAME = "campaign_name"
        private const val CAMPAIGN_ENDED_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_campaign_detail_ended.png"

        @JvmStatic
        fun newInstance(campaignId: Long, campaignName: String?): CampaignDetailFragment {
            return CampaignDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putString(BUNDLE_KEY_CAMPAIGN_NAME, campaignName)
                }
            }
        }
    }

    @Inject
    lateinit var shareComponentInstanceBuilder: ShareComponentInstanceBuilder
    private var shareComponentBottomSheet: UniversalShareBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val campaignId by lazy { arguments?.getLong(BundleConstant.BUNDLE_KEY_CAMPAIGN_ID) }
    private val campaignName by lazy {
        arguments?.getString(BUNDLE_KEY_CAMPAIGN_NAME) ?: getString(R.string.campaign_detail)
    }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsFragmentCampaignDetailBinding>()
    private var errorToaster: Snackbar? = null

    private val loaderDialog by lazy { LoaderDialog(requireActivity()) }
    private val campaignDetailProductListAdapter by lazy {
        CampaignDetailProductListAdapter()
    }

    private val btnMoreClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            viewModel.onMoreMenuClicked()
        }
    }

    override fun getScreenName(): String =
        CampaignDetailFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentToUnifyBgColor()
        initCampaignDetail()
        setUpView()
    }

    private fun initCampaignDetail() {
        val campaignId = campaignId ?: return
        binding?.loader?.visible()
        viewModel.getCampaignDetail(campaignId)
    }

    private fun setUpView() {
        setUpToolbar()
        setUpClickListeners()
        observeCampaign()
        observeTNCClickEvent()
        observeEditCampaignEvent()
        observeCancelCampaignEvent()
        observeMoreMenuEvent()
        observeActiveDialog()
        observeShareComponent()
    }

    private fun observeMoreMenuEvent() {
        viewModel.moreMenuEvent.observe(viewLifecycleOwner) {
            showMoreMenuBottomSheet(it)
        }
    }

    private fun observeShareComponent() {
        viewModel.shareCampaignActionEvent.observe(viewLifecycleOwner) { result ->
            dismissLoaderDialog()
            when (result) {
                is Success -> {
                    displayShareBottomSheet(result.data)
                }
                is Fail -> {
                    binding?.cardButtonWrapper showError result.throwable
                }
            }
        }
    }

    private fun observeActiveDialog() {
        childFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                when (f) {
                    is CampaignDetailMoreMenuBottomSheet -> {
                        f.setClickListener(this@CampaignDetailFragment)
                    }
                }
            }
        }, false)
    }

    private fun setUpToolbar() {
        binding?.header?.apply {
            headerTitle = campaignName
            setNavigationOnClickListener { activity?.finish() }
        }
    }

    private fun setUpClickListeners() {
        val binding = binding ?: return

        binding.btnEditCampaign.setOnClickListener {
            viewModel.onEditCampaignClicked()
        }
        binding.btnSeeTnc.setOnClickListener {
            viewModel.onTNCButtonClicked()
        }
        binding.btnShareCampaign.setOnClickListener {
            showLoaderDialog()
            viewModel.onShareButtonClicked()
        }
    }

    private fun displayShareBottomSheet(shareComponent: ShareComponent) {
        val param = ShareComponentInstanceBuilder.Param(
            shareComponent.metaData.banner.shop.name,
            shareComponent.metaData.banner.shop.logo,
            shareComponent.metaData.shop.isPowerMerchant,
            shareComponent.metaData.shop.isOfficial,
            shareComponent.metaData.banner.shop.domain,
            shareComponent.metaData.banner.campaignStatusId,
            shareComponent.metaData.banner.campaignId,
            shareComponent.metaData.banner.startDate,
            shareComponent.metaData.banner.endDate,
            shareComponent.metaData.banner.products.size,
            shareComponent.metaData.banner.products,
            shareComponent.metaData.banner.maxDiscountPercentage
        )

        shareComponentBottomSheet = shareComponentInstanceBuilder.build(
            shareComponent.thumbnailImageUrl,
            param,
            onShareOptionClick = ::handleShareOptionClick,
            onCloseOptionClicked = {}
        )
        shareComponentBottomSheet?.show(childFragmentManager, shareComponentBottomSheet?.tag)
    }

    private fun handleShareOptionClick(
        shareModel: ShareModel,
        linkerShareResult: LinkerShareResult,
        outgoingText: String
    ) {
        SharingUtil.executeShareIntent(
            shareModel,
            linkerShareResult,
            activity ?: return,
            view ?: return,
            outgoingText
        )
        shareComponentBottomSheet?.dismiss()
    }

    private fun observeEditCampaignEvent() {
        viewModel.editCampaignActionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is EditCampaignActionResult.Allowed -> routeToEditCampaignPage(result.campaignId)
                is EditCampaignActionResult.RegisteredEventCampaign -> showRegisteredEventCampaignEditErrorMessage()
            }
        }
    }

    private fun observeCancelCampaignEvent() {
        viewModel.cancelCampaignActionResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is CancelCampaignActionResult.ActionAllowed -> showCancelCampaignDialog(result.campaign)
                is CancelCampaignActionResult.RegisteredEventCampaign -> showRegisteredEventCampaignCancelErrorMessage(
                    result.campaign
                )
            }
        }
    }

    private fun showCancelCampaignDialog(campaign: CampaignUiModel) {
        val bottomSheet = CancelCampaignBottomSheet(
            campaign.campaignId,
            campaign.campaignName,
            campaign.status
        ) {
            val toasterMessage = if (campaign.status.isOngoing()) {
                getString(R.string.campaign_detail_campaign_stopped_message, campaign.campaignName)
            } else {
                getString(
                    R.string.campaign_detail_campaign_cancelled_message,
                    campaign.campaignName
                )
            }
            routeToCampaignListPage(toasterMessage)
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun routeToCampaignListPage(toasterMessage: String = "") {
        val result = Intent().apply {
            putExtra(
                CampaignDetailActivity.BUNDLE_KEY_CAMPAIGN_CANCELLATION_MESSAGE,
                toasterMessage
            )
        }
        activity?.run {
            setResult(Activity.RESULT_OK, result)
            finish()
        }

    }

    private fun showRegisteredEventCampaignCancelErrorMessage(campaign: CampaignUiModel) {
        val binding = binding ?: return
        val errorMessage = findCancelCampaignErrorWording(campaign.status)
        val toaster = Toaster.build(
            binding.root,
            errorMessage,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            getString(R.string.sfs_ok)
        ) {}.apply {
            val anchor = if (binding.cardButtonWrapper.isVisible) {
                binding.cardButtonWrapper
            } else {
                binding.root
            }
            anchorView = anchor
        }
        toaster.show()
    }

    private fun routeToEditCampaignPage(campaignId: Long) {
        val context = context ?: return
        CampaignInformationActivity.startUpdateMode(context, campaignId)
    }

    private fun showRegisteredEventCampaignEditErrorMessage() {
        val binding = binding ?: return
        val errorMessage = getString(R.string.sfs_cannot_edit_campaign)
        val toaster = Toaster.build(
            binding.root,
            errorMessage,
            Snackbar.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            getString(R.string.sfs_ok)
        ) {}.apply {
            anchorView = binding.cardButtonWrapper
        }
        toaster.show()
    }

    private fun observeCampaign() {
        viewModel.campaign.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> showCampaignDetailContent(result.data)
                is Fail -> showError(result.throwable)
            }
        }
    }

    private fun showCampaignDetailContent(data: CampaignDetailMeta) {
        val binding = binding ?: return
        displayCampaignDetailInformation(data)
        showProductList(data.productList)
        binding.wrapperCampaignDetailContent.visible()
        binding.loader.hide()
    }

    private fun showProductList(data: SellerCampaignProductList) {
        binding?.apply {
            rvProductList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            campaignDetailProductListAdapter.clearAll()
            campaignDetailProductListAdapter.submit(data.productList)
            rvProductList.adapter = campaignDetailProductListAdapter
        }
    }

    private fun displayCampaignDetailInformation(data: CampaignDetailMeta) {
        val campaign = data.campaign
        handleCampaignToolbar(campaign)
        handleCampaignInformation(campaign)
        handlePackageInfo(campaign)
        handleCampaignPerformanceData(data)
        handleCampaignStatusIndicator(campaign)
        handleEventParticipation(campaign)
        setProductListAdapterCampaignStatus(campaign)
        handleEditCampaignButtonVisibility(campaign)
        handleShareButtonVisibility(campaign)

    }

    private fun handleCampaignInformation(campaign: CampaignUiModel) {
        val binding = binding ?: return

        binding.tgCampaignDetailId.text = getString(
            R.string.campaign_detail_campaign_id,
            campaign.campaignId.toString()
        )
        binding.tgCampaignStartDate.text =
            campaign.startDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
        binding.tgCampaignEndDate.text = campaign.endDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
    }

    private fun handlePackageInfo(campaign: CampaignUiModel) {
        val binding = binding ?: return

        binding.tgPackageInfo.text =
            getString(R.string.package_info_placeholder, campaign.packageInfo.packageName)
        binding.tgPackageInfo.isVisible = campaign.packageInfo.packageName.isNotEmpty()
        binding.tgPackageInfoLabel.isVisible = campaign.packageInfo.packageName.isNotEmpty()
    }

    private fun handleCampaignPerformanceData(data: CampaignDetailMeta) {
        if (data.campaign.status.isFinished()) {
            handleCampaignFinished(data)
        } else if (data.campaign.status.isOngoing()) {
            handleCampaignOngoing(data)
        }
    }

    private fun handleCampaignOngoing(
        data: CampaignDetailMeta
    ) {
        val binding = binding ?: return
        binding.groupCampaignPerformance.visible()
        val inflatedView = binding.vsCampaignPerformance.inflate()
        val campaignDetailPerformanceBinding =
            SsfsCampaignDetailPerformanceLayoutBinding.bind(inflatedView)
        campaignDetailPerformanceBinding.run {
            tgPerformanceTotalProductSold.text = data.productList.totalProductSold.toString()
            tgPerformanceTotalIncome.text = data.productList.totalIncome.convertRupiah()
            tgPerformanceTotalProductQuantity.text = data.productList.totalProductQty.toString()
        }
    }

    private fun handleCampaignFinished(
        data: CampaignDetailMeta
    ) {
        val binding = binding ?: return
        binding.imgCampaignEnd.loadImage(CAMPAIGN_ENDED_IMAGE_URL)
        binding.groupCampaignEnded.visible()
        val inflatedView = binding.vsCampaignEndPerformance.inflate()
        val campaignDetailPerformanceBinding =
            SsfsCampaignDetailPerformanceLayoutBinding.bind(inflatedView)
        campaignDetailPerformanceBinding.run {
            tgPerformanceTotalProductSold.text = data.productList.totalProductSold.toString()
            tgPerformanceTotalIncome.text = data.productList.totalIncome.convertRupiah()
            tgPerformanceTotalProductQuantity.text = data.productList.totalProductQty.toString()
        }
    }

    private fun handleCampaignToolbar(data: CampaignUiModel) {
        val binding = binding ?: return
        binding.header.title = data.campaignName
        if (data.status.isActive()) {
            val ivMore = binding.header.addRightIcon(R.drawable.ic_sfs_more)
            ivMore.setOnClickListener(btnMoreClickListener)
        }
    }

    private fun handleEventParticipation(data: CampaignUiModel) {
        val binding = binding ?: return
        if (data.thematicParticipation) {
            binding.groupCampaignEventName.visible()
            binding.tgCampaignEventName.text = data.thematicInfo.name
            binding.tickerCampaignEventParticipation.setTextDescription(
                getString(
                    R.string.campaign_detail_ticker_event_participation_message,
                    data.reviewEndDate.formatTo(DateConstant.DATE)
                )
            )
            binding.tickerCampaignEventParticipation.visible()
        } else {
            binding.groupCampaignEventName.hide()
            binding.tickerCampaignEventParticipation.hide()
        }
    }

    private fun handleEditCampaignButtonVisibility(data: CampaignUiModel) {
        val binding = binding ?: return
        if (data.status.isAvailable()) {
            binding.btnEditCampaign.visible()
        } else {
            binding.btnEditCampaign.hide()
        }
    }

    private fun handleShareButtonVisibility(data: CampaignUiModel) {
        val binding = binding ?: return
        if (data.status.isActive()) {
            binding.cardButtonWrapper.visible()
        } else {
            binding.cardButtonWrapper.hide()
        }
    }

    @SuppressLint("ResourcePackage")
    private fun handleCampaignStatusIndicator(campaign: CampaignUiModel) {
        val binding = binding ?: return
        when {
            campaign.status.isUpcoming() -> {
                if (campaign.thematicParticipation) {
                    binding.tgCampaignStatus.setStatus(R.string.sfs_selection)
                } else {
                    binding.tgCampaignStatus.setStatus(R.string.sfs_upcoming)
                }
                binding.tgCampaignStatus.textColor(R.color.Unify_YN400)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_upcoming)
            }
            campaign.status.isAvailable() -> {
                binding.tgCampaignStatus.setStatus(R.string.sfs_available)
                binding.tgCampaignStatus.textColor(R.color.Unify_NN600)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_available)
            }
            campaign.status.isOngoing() -> {
                binding.tgCampaignStatus.setStatus(R.string.sfs_ongoing)
                binding.tgCampaignStatus.textColor(R.color.Unify_GN500)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_ongoing)
            }
            campaign.status.isFinished() -> {
                binding.tgCampaignStatus.setStatus(R.string.sfs_finished)
                binding.tgCampaignStatus.textColor(R.color.Unify_NN400)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_finished)
            }
            campaign.status.isCancelled() -> {
                binding.tgCampaignStatus.setStatus(R.string.sfs_cancelled)
                binding.tgCampaignStatus.textColor(R.color.Unify_RN500)
                binding.imgCampaignStatusIndicator.setImageResource(R.drawable.ic_sfs_campaign_indicator_cancelled)
            }
        }
    }

    private fun setProductListAdapterCampaignStatus(campaign: CampaignUiModel) {
        campaignDetailProductListAdapter.campaignStatus = campaign.status
    }

    private fun showError(throwable: Throwable) {
        val binding = binding ?: return
        binding.cardButtonWrapper.hide()
        binding.wrapperCampaignDetailContent.hide()
        binding.loader.hide()
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        errorToaster = Toaster.build(
            binding.root,
            text = errorMessage,
            duration = Toaster.LENGTH_INDEFINITE,
            type = Toaster.TYPE_ERROR,
            actionText = getString(com.tokopedia.abstraction.R.string.title_try_again),
            clickListener = { onCampaignErrorActionClicked() }
        ).also {
            it.show()
        }
    }

    private fun onCampaignErrorActionClicked() {
        binding?.loader?.visible()
        viewModel.reFetchCampaignDetail()
    }

    private fun Typography.setStatus(@StringRes resourceId: Int) {
        val status = context.getString(resourceId)
        this.text = status
    }

    private fun Typography.textColor(@ColorRes resourceId: Int) {
        val color = ContextCompat.getColor(context, resourceId)
        this.setTextColor(color)
    }

    private fun observeTNCClickEvent() {
        viewModel.tncClickEvent.observe(viewLifecycleOwner) { request ->
            showTNCBottomSheet(request)
        }
    }

    private fun showTNCBottomSheet(request: MerchantCampaignTNC.TncRequest) {
        MerchantCampaignTNCBottomSheet.createInstance(
            showTickerAndButton = false,
            tncRequest = request
        )
            .show(childFragmentManager)
    }

    private fun showMoreMenuBottomSheet(campaign: CampaignUiModel) {
        CampaignDetailMoreMenuBottomSheet.newInstance(campaign.campaignName, campaign.status)
            .show(childFragmentManager)
    }

    override fun onMenuCancelCampaignClicked() {
        viewModel.onCampaignCancelMenuClicked()
    }

    private fun showLoaderDialog() {
        loaderDialog.setLoadingText(getString(R.string.sfs_please_wait))
        loaderDialog.show()
    }

    private fun dismissLoaderDialog() {
        loaderDialog.dialog.dismiss()
    }

    private fun findCancelCampaignErrorWording(campaignStatus: CampaignStatus): String {
        return if (campaignStatus.isOngoing()) {
            getString(R.string.sfs_cannot_stop_campaign)
        } else {
            getString(R.string.sfs_cannot_cancel_campaign)
        }
    }
}