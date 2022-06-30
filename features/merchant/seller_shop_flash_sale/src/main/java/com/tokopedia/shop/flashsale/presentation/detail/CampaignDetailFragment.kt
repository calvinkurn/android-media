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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsCampaignDetailPerformanceLayoutBinding
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignDetailBinding
import com.tokopedia.shop.flashsale.common.constant.CampaignDetailConstant.PRODUCT_LIST_SIZE
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.showError
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignDetailMeta
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.enums.isActive
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.isAvailable
import com.tokopedia.shop.flashsale.domain.entity.enums.isCancelled
import com.tokopedia.shop.flashsale.domain.entity.enums.isFinished
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.shop.flashsale.domain.entity.enums.isUpcoming
import com.tokopedia.shop.flashsale.presentation.cancelation.CancelCampaignBottomSheet
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.MerchantCampaignTNCBottomSheet
import com.tokopedia.shop.flashsale.presentation.detail.bottomsheet.CampaignDetailMoreMenuBottomSheet
import com.tokopedia.shop.flashsale.presentation.detail.bottomsheet.CampaignDetailMoreMenuClickListener
import com.tokopedia.shop.flashsale.presentation.detail.adapter.CampaignDetailProductListAdapter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment :
    BaseSimpleListFragment<CampaignDetailProductListAdapter, SellerCampaignProductList.Product>(),
    CampaignDetailMoreMenuClickListener {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"
        private const val BUNDLE_KEY_CAMPAIGN_NAME = "campaign_name"
        private const val CAMPAIGN_ENDED_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/flash-sale-toko/ic_campaign_detail_ended.png"

        @JvmStatic
        fun newInstance(campaignId: Long, campaignName: String?): CampaignDetailFragment {
            return CampaignDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                    putString(BUNDLE_KEY_CAMPAIGN_NAME, campaignName)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val campaignId by lazy { arguments?.getLong(BUNDLE_KEY_CAMPAIGN_ID) }
    private val campaignName by lazy {
        arguments?.getString(BUNDLE_KEY_CAMPAIGN_NAME) ?: getString(R.string.campaign_detail)
    }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsFragmentCampaignDetailBinding>()
    private var errorToaster: Snackbar? = null

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
    }

    private fun observeMoreMenuEvent() {
        viewModel.moreMenuEvent.observe(viewLifecycleOwner) {
            showMoreMenuBottomSheet(it)
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
                is CancelCampaignActionResult.RegisteredEventCampaign -> showRegisteredEventCampaignCancelErrorMessage()
            }
        }
    }

    @SuppressLint("ResourcePackage")
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

    @SuppressLint("ResourcePackage")
    private fun showRegisteredEventCampaignCancelErrorMessage() {
        val binding = binding ?: return
        val errorMessage = getString(
            R.string.campaign_detail_cancel_registered_event_campaign_message
        )
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

    @SuppressLint("ResourcePackage")
    private fun showRegisteredEventCampaignEditErrorMessage() {
        val binding = binding ?: return
        val errorMessage =
            getString(R.string.campaign_detail_edit_registered_event_campaign_message)
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
        renderList(data.productList, false)
    }

    @SuppressLint("ResourcePackage")
    private fun displayCampaignDetailInformation(data: CampaignDetailMeta) {
        val binding = binding ?: return
        val campaign = data.campaign
        handleCampaignToolbar(campaign)
        handleCampaignPerformanceData(data)
        binding.tgCampaignDetailId.text = getString(
            R.string.campaign_detail_campaign_id,
            campaign.campaignId.toString()
        )
        handleCampaignStatusIndicator(campaign)
        handleEventParticipation(campaign)

        handleEditCampaignButtonVisibility(campaign)
        handleShareButtonVisibility(campaign)

        binding.tgCampaignStartDate.text =
            campaign.startDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
        binding.tgCampaignEndDate.text = campaign.endDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
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

    override fun createAdapter(): CampaignDetailProductListAdapter? {
        return campaignDetailProductListAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.rvProductList
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int {
        return PRODUCT_LIST_SIZE
    }

    override fun addElementToAdapter(list: List<SellerCampaignProductList.Product>) {
        adapter?.submit(list)
    }

    override fun loadData(page: Int) { }

    override fun clearAdapterData() {
        adapter?.clearAll()
    }

    override fun onShowLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onDataEmpty() {

    }

    override fun onGetListError(message: String) {
        view?.showError(message)
    }

    override fun onScrolled(xScrollAmount: Int, yScrollAmount: Int) {

    }
}