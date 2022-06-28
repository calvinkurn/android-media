package com.tokopedia.shop.flashsale.presentation.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignDetailBinding
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.enums.isAvailable
import com.tokopedia.shop.flashsale.domain.entity.enums.isCancelled
import com.tokopedia.shop.flashsale.domain.entity.enums.isFinished
import com.tokopedia.shop.flashsale.domain.entity.enums.isOngoing
import com.tokopedia.shop.flashsale.domain.entity.enums.isUpcoming
import com.tokopedia.shop.flashsale.presentation.creation.information.CampaignInformationActivity
import com.tokopedia.shop.flashsale.presentation.creation.rule.bottomsheet.MerchantCampaignTNCBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class CampaignDetailFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_ID = "campaign_id"

        @JvmStatic
        fun newInstance(campaignId: Long): CampaignDetailFragment {
            return CampaignDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(BUNDLE_KEY_CAMPAIGN_ID, campaignId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val campaignId by lazy { arguments?.getLong(BUNDLE_KEY_CAMPAIGN_ID) }
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignDetailViewModel::class.java) }

    private var binding by autoClearedNullable<SsfsFragmentCampaignDetailBinding>()
    private var errorToaster: Snackbar? = null

    private val btnMoreClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            // TODO(*) : Show bottom sheet
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
    }

    private fun setUpToolbar() {
        binding?.header?.setNavigationOnClickListener { activity?.finish() }
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

    private fun showCampaignDetailContent(data: CampaignUiModel) {
        val binding = binding ?: return
        displayCampaignDetailInformation(data)
        binding.wrapperCampaignDetailContent.visible()
        binding.loader.hide()
    }

    @SuppressLint("ResourcePackage")
    private fun displayCampaignDetailInformation(data: CampaignUiModel) {
        val binding = binding ?: return
        binding.header.title = data.campaignName
        val ivMore = binding.header.addRightIcon(R.drawable.ic_sfs_more)
        ivMore.setOnClickListener(btnMoreClickListener)
        binding.tgCampaignDetailId.text = getString(
            R.string.campaign_detail_campaign_id,
            data.campaignId.toString()
        )
        handleCampaignStatusIndicator(data)

        handleEditCampaignButtonVisibility(data)
        handleShareButtonVisibility(data)

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

        binding.tgCampaignStartDate.text = data.startDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
        binding.tgCampaignEndDate.text = data.endDate.formatTo(DateConstant.DATE_TIME_WITH_DAY)
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
        if (data.status.isFinished()) {
            binding.cardButtonWrapper.hide()
        } else {
            binding.cardButtonWrapper.visible()
        }
    }

    @SuppressLint("ResourcePackage")
    private fun handleCampaignStatusIndicator(campaign: CampaignUiModel) {
        val binding = binding ?: return
        when {
            campaign.status.isUpcoming() -> {
                binding.tgCampaignStatus.setStatus(R.string.sfs_upcoming)
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
}