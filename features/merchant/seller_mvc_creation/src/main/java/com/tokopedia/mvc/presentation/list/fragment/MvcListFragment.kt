package com.tokopedia.mvc.presentation.list.fragment

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp.SELLER_MVC_CREATE
import com.tokopedia.campaign.delegates.HasPaginatedList
import com.tokopedia.campaign.delegates.HasPaginatedListImpl
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.campaign.utils.extension.routeToUrl
import com.tokopedia.campaign.utils.extension.showToaster
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.campaign.utils.extension.slideDown
import com.tokopedia.campaign.utils.extension.slideUp
import com.tokopedia.campaign.utils.extension.toDate
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.applyUnifyBackgroundColor
import com.tokopedia.kotlin.extensions.view.attachOnScrollListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.PaginationConstant.INITIAL_PAGE
import com.tokopedia.mvc.common.util.PaginationConstant.PAGE_SIZE
import com.tokopedia.mvc.common.util.SharedPreferencesUtil
import com.tokopedia.mvc.common.util.UrlConstant.URL_MAIN_ARTICLE
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListBinding
import com.tokopedia.mvc.databinding.SmvcFragmentMvcListFooterBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.ShareComponentMetaData
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationQuota
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.FilterVoucherStatusBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.OtherPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.changequota.ChangeQuotaBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.editperiod.VoucherEditPeriodBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.EduCenterBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.EduCenterClickListener
import com.tokopedia.mvc.presentation.bottomsheet.educenterbottomsheet.model.EduCenterMenuModel
import com.tokopedia.mvc.presentation.bottomsheet.moremenu.MoreMenuBottomSheet
import com.tokopedia.mvc.presentation.detail.VoucherDetailActivity
import com.tokopedia.mvc.presentation.download.DownloadVoucherImageBottomSheet
import com.tokopedia.mvc.presentation.intro.MvcIntroActivity
import com.tokopedia.mvc.presentation.list.adapter.VoucherAdapterListener
import com.tokopedia.mvc.presentation.list.adapter.VouchersAdapter
import com.tokopedia.mvc.presentation.list.constant.PageState
import com.tokopedia.mvc.presentation.list.dialog.CallTokopediaCareDialog
import com.tokopedia.mvc.presentation.list.dialog.StopVoucherConfirmationDialog
import com.tokopedia.mvc.presentation.list.helper.MvcListPageStateHelper
import com.tokopedia.mvc.presentation.list.model.DeleteVoucherUiEffect
import com.tokopedia.mvc.presentation.list.model.FilterModel
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.presentation.list.viewmodel.MvcListViewModel
import com.tokopedia.mvc.presentation.quota.QuotaInfoActivity
import com.tokopedia.mvc.presentation.share.LinkerDataGenerator
import com.tokopedia.mvc.presentation.share.ShareComponentInstanceBuilder
import com.tokopedia.mvc.presentation.share.ShareCopyWritingGenerator
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.SharingUtil
import com.tokopedia.mvc.util.tracker.StopVoucherTracker
import com.tokopedia.mvc.util.tracker.VoucherListActionTracker
import com.tokopedia.mvc.util.tracker.VoucherListTracker
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.universal_sharing.constants.BroadcastChannelType
import com.tokopedia.universal_sharing.view.bottomsheet.ClipboardHandler
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

class MvcListFragment :
    BaseDaggerFragment(),
    HasPaginatedList by HasPaginatedListImpl(),
    VoucherAdapterListener,
    FilterVoucherStatusBottomSheet.FilterVoucherStatusBottomSheetListener,
    FilterVoucherBottomSheet.FilterVoucherBottomSheetListener,
    OtherPeriodBottomSheet.OtherPeriodBottomSheetListener,
    EduCenterClickListener {

    companion object {
        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"
    }

    private val filterList = ArrayList<SortFilterItem>()
    private val filterItemStatus by lazy {
        SortFilterItem(
            getString(R.string.smvc_bottomsheet_filter_voucher_all)
        )
    }
    private var binding by autoClearedNullable<SmvcFragmentMvcListBinding>()
    private var moreMenuBottomSheet: MoreMenuBottomSheet? = null
    private var voucherEditPeriodBottomSheet: VoucherEditPeriodBottomSheet? = null
    private var otherPeriodBottomSheet: OtherPeriodBottomSheet? = null
    private var eduCenterBottomSheet: EduCenterBottomSheet? = null
    private var stopVoucherDialog: StopVoucherConfirmationDialog? = null

    @Inject
    lateinit var shareCopyWritingGenerator: ShareCopyWritingGenerator

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    @Inject
    lateinit var shareComponentInstanceBuilder: ShareComponentInstanceBuilder

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModel: MvcListViewModel

    @Inject
    lateinit var sharedPreferencesUtil: SharedPreferencesUtil

    @Inject
    lateinit var tracker: StopVoucherTracker

    @Inject
    lateinit var voucherListTracker: VoucherListTracker

    @Inject
    lateinit var stopVoucherTracker: StopVoucherTracker

    @Inject
    lateinit var voucherListActionTracker: VoucherListActionTracker

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentMvcListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyUnifyBackgroundColor()
        binding?.setupView()
        setEduCenterBottomSheet()
        setupStopConfirmationDialog()
        setupObservables()
        setupObserveDeleteUiEffect()
    }

    override fun onResume() {
        super.onResume()
        loadInitialDataList()
        displayUploadResult()
    }

    override fun onVoucherListMoreMenuClicked(voucher: Voucher) {
        showMoreMenuBottomSheet(voucher, pageSource = MvcListFragment::class.java.simpleName)
    }

    private fun showMoreMenuBottomSheet(
        voucher: Voucher,
        title: String = voucher.name,
        pageSource: String = ""
    ) {
        activity?.let {
            moreMenuBottomSheet =
                MoreMenuBottomSheet.newInstance(
                    it,
                    voucher,
                    title,
                    pageSource = pageSource
                )
            moreMenuBottomSheet?.setOnMenuClickListener { menu ->
                onClickListenerForMoreMenu(menu, voucher)
            }
            moreMenuBottomSheet?.show(childFragmentManager, "")
        }
        voucherListTracker.sendClickDotsOnEachVoucherEvent()
    }

    private fun onClickListenerForMoreMenu(menuUiModel: MoreMenuUiModel, voucher: Voucher) {
        moreMenuBottomSheet?.dismiss()
        when (menuUiModel) {
            is MoreMenuUiModel.Coupon -> {
                showUpdateQuotaBottomSheet(voucher)
                voucherListActionTracker.sendClickUbahQuotaEvent(voucher)
            }
            is MoreMenuUiModel.EditPeriod -> {
                showEditPeriodBottomSheet(voucher)
                voucherListActionTracker.sendClickUbahPeriodEvent(voucher)
            }
            is MoreMenuUiModel.Edit -> {
                redirectToEditPage(voucher)
                voucherListActionTracker.sendClickUbahEvent(voucher)
            }
            is MoreMenuUiModel.Clipboard -> {
                VoucherDetailActivity.start(context ?: return, voucher.id)
                voucherListActionTracker.sendClickLihatDetailEvent(voucher)
            }
            is MoreMenuUiModel.Broadcast -> {
                SharingUtil.shareToBroadCastChat(context ?: return, voucher.id)
                voucherListActionTracker.sendClickBroadcastChatEvent(voucher)
            }
            is MoreMenuUiModel.Download -> {
                showDownloadVoucherBottomSheet(voucher)
                voucherListActionTracker.sendClickDownloadEvent(voucher)
            }
            is MoreMenuUiModel.Clear -> {
                deleteVoucher(voucher)
                voucherListActionTracker.sendClickBatalkanEvent(voucher)
            }
            is MoreMenuUiModel.Share -> {
                viewModel.generateShareComponentMetaData(voucher)
                voucherListActionTracker.sendClickBagikanEvent(voucher)
            }
            is MoreMenuUiModel.Stop -> {
                deleteVoucher(voucher)
                voucherListActionTracker.sendClickHentikanEvent(voucher)
            }
            is MoreMenuUiModel.Copy -> {
                redirectToDuplicatePage(voucher.id)
                voucherListActionTracker.sendClickDuplikatEvent(voucher)
            }
            is MoreMenuUiModel.TermsAndConditions -> {
                /*no-op*/
            }
            else -> {
                /*no-op*/
            }
        }
    }

    private fun showEditPeriodBottomSheet(voucher: Voucher) {
        activity?.let {
            voucherEditPeriodBottomSheet =
                VoucherEditPeriodBottomSheet.newInstance(
                    voucher,
                    onSuccessUpdateVoucherPeriod,
                    onFailureUpdateVoucherPeriod
                )
            voucherEditPeriodBottomSheet?.show(childFragmentManager, "")
        }
    }

    private var onSuccessUpdateVoucherPeriod: (Voucher?) -> Unit = { voucher ->
        loadInitialDataList()
        context?.resources?.let {
            binding?.footer?.root?.showToaster(
                it.getString(
                    R.string.edit_period_success_edit_period,
                    voucher?.name.toBlankOrString()
                ).toBlankOrString(),
                it.getString(R.string.edit_period_button_text).toBlankOrString()
            )
        }
    }

    private var onFailureUpdateVoucherPeriod: (String) -> Unit = { message ->
        val errorMessage = if (message.isNotBlank()) {
            message
        } else {
            context?.getString(R.string.smvc_general_error).toBlankOrString()
        }
        view?.showToasterError(errorMessage)
    }

    private fun showDownloadVoucherBottomSheet(voucher: Voucher) {
        activity?.let {
            if (!isVisible) return

            val bottomSheet = DownloadVoucherImageBottomSheet.newInstance(
                voucher.id,
                voucher.image,
                voucher.imageSquare,
                voucher.imagePortrait
            )
            bottomSheet.setOnDownloadSuccess {
                otherPeriodBottomSheet?.dismiss()
                binding?.footer?.root?.showToaster(
                    getString(
                        R.string.smvc_placeholder_download_voucher_image_success,
                        voucher.name
                    ),
                    getString(R.string.smvc_ok)
                )
            }
            bottomSheet.setOnDownloadError {
                binding?.footer?.root?.showToaster(
                    getString(R.string.smvc_download_voucher_image_failed),
                    getString(R.string.smvc_ok)
                )
            }
            bottomSheet.show(childFragmentManager, bottomSheet.tag)
        }
    }

    private fun displayShareBottomSheet(shareComponentMetaData: ShareComponentMetaData) {
        val voucher = shareComponentMetaData.voucher
        val voucherStartTime = voucher.startTime.toDate(
            DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
        )
        val voucherEndTime = voucher.finishTime.toDate(
            DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
        )
        val promoType = PromoType.values().firstOrNull { value -> value.text == voucher.typeFormatted }
            ?: PromoType.FREE_SHIPPING

        val shareComponentParam = getShareComponentData(
            shareComponentMetaData,
            voucherStartTime,
            voucherEndTime,
            promoType
        )

        val formattedShopName = MethodChecker.fromHtml(shareComponentParam.shopName).toString()

        val titleTemplate = getTitleForShareComponent(shareComponentParam)

        val title = String.format(
            titleTemplate,
            formattedShopName
        )

        val copyWritingParam = ShareCopyWritingGenerator.Param(
            voucherStartDate = voucherStartTime,
            voucherEndDate = voucherEndTime,
            shopName = shareComponentMetaData.shopData.name,
            discountAmount = voucher.discountAmt.toLong(),
            discountAmountMax = voucher.discountAmtMax.toLong(),
            discountPercentage = voucher.discountAmt
        )

        createAndShowUniversalShareBottomSheet(
            voucher,
            shareComponentParam,
            copyWritingParam,
            title,
            promoType
        )
    }

    private fun getShareComponentData(
        shareComponentMetaData: ShareComponentMetaData,
        voucherStartTime: Date,
        voucherEndTime: Date,
        promoType: PromoType
    ): ShareComponentInstanceBuilder.Param {
        val voucher = shareComponentMetaData.voucher
        val productImageUrls = if (voucher.isLockToProduct) {
            shareComponentMetaData.topSellingProductImageUrls
        } else {
            emptyList()
        }
        return voucher.let {
            ShareComponentInstanceBuilder.Param(
                isVoucherProduct = it.isLockToProduct,
                voucherId = it.id,
                isPublic = it.isPublic,
                voucherCode = it.code,
                voucherStartDate = voucherStartTime,
                voucherEndDate = voucherEndTime,
                promoType = promoType,
                benefitType = if (it.discountUsingPercent) BenefitType.PERCENTAGE else BenefitType.NOMINAL,
                shopLogo = shareComponentMetaData.shopData.logo,
                shopName = shareComponentMetaData.shopData.name,
                shopDomain = shareComponentMetaData.shopData.domain,
                discountAmount = it.discountAmt.toLong(),
                discountAmountMax = it.discountAmtMax.toLong(),
                productImageUrls = productImageUrls,
                discountPercentage = it.discountAmt,
                targetBuyer = it.targetBuyer
            )
        }
    }

    private fun getTitleForShareComponent(shareComponentParam: ShareComponentInstanceBuilder.Param): String {
        return if (shareComponentParam.isVoucherProduct) {
            getString(
                R.string.smvc_placeholder_share_component_outgoing_title_product_voucher,
                shareComponentParam.shopName
            )
        } else {
            getString(
                R.string.smvc_placeholder_share_component_outgoing_title_shop_voucher,
                shareComponentParam.shopName
            )
        }
    }

    private fun createAndShowUniversalShareBottomSheet(
        voucher: Voucher,
        shareComponentParam: ShareComponentInstanceBuilder.Param,
        copyWritingParam: ShareCopyWritingGenerator.Param,
        title: String,
        promoType: PromoType
    ) {
        val description = shareCopyWritingGenerator.findOutgoingDescription(
            isProductVoucher = voucher.isLockToProduct,
            voucherTarget = voucher.targetBuyer,
            promoType = promoType,
            benefitType = if (voucher.discountUsingPercent) BenefitType.PERCENTAGE else BenefitType.NOMINAL,
            param = copyWritingParam
        )

        universalShareBottomSheet = shareComponentInstanceBuilder.build(
            shareComponentParam,
            title,
            onShareOptionsClicked = { shareModel ->
                handleShareOptionSelection(
                    voucher.isLockToProduct,
                    shareComponentParam.voucherId,
                    shareModel,
                    title,
                    description,
                    shareComponentParam.shopDomain
                )
            },
            onBottomSheetClosed = {}
        )

        universalShareBottomSheet?.setBroadcastChannel(
            activity ?: return,
            BroadcastChannelType.BLAST_PROMO,
            voucher.code
        )

        universalShareBottomSheet?.show(childFragmentManager, universalShareBottomSheet?.tag)
    }

    private fun handleShareOptionSelection(
        isProductVoucher: Boolean,
        voucherId: Long,
        shareModel: ShareModel,
        title: String,
        description: String,
        shopDomain: String
    ) {
        val shareCallback = object : ShareCallback {
            override fun urlCreated(linkerShareData: LinkerShareResult?) {
                val wording = "$description ${linkerShareData?.shareUri.orEmpty()}"
                com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil.executeShareIntent(
                    shareModel,
                    linkerShareData,
                    activity,
                    view,
                    wording
                )
                universalShareBottomSheet?.dismiss()
            }

            override fun onError(linkerError: LinkerError?) {}
        }

        val outgoingDescription = if (isProductVoucher) {
            getString(R.string.smvc_share_component_outgoing_text_description_product_voucher)
        } else {
            getString(R.string.smvc_share_component_outgoing_text_description_shop_voucher)
        }

        val linkerDataGenerator = LinkerDataGenerator()
        val linkerShareData = linkerDataGenerator.generate(
            voucherId,
            userSession.shopId,
            shopDomain,
            shareModel,
            title,
            outgoingDescription
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                Int.ZERO,
                linkerShareData,
                shareCallback
            )
        )
    }

    override fun onVoucherListCopyCodeClicked(voucher: Voucher) {
        activity?.let { ClipboardHandler().copyToClipboard(it, voucher.code) }
        binding?.footer?.root.showToaster(
            getString(R.string.smvc_voucherlist_copy_to_clipboard_message)
        )
    }

    override fun onVoucherListMultiPeriodClicked(voucher: Voucher) {
        viewModel.getVoucherListChild(voucher.id, voucher.parentId)
    }

    override fun onVoucherListClicked(voucher: Voucher) {
        VoucherDetailActivity.start(context ?: return, voucher.id)
    }

    override fun onFilterVoucherStatusChanged(status: List<VoucherStatus>, statusText: String) {
        filterItemStatus.title = statusText
        filterItemStatus.selectedItem = arrayListOf(statusText)
        viewModel.setFilterStatus(status)
        loadInitialDataList()
    }

    override fun onFilterVoucherChanged(filter: FilterModel) {
        viewModel.filter = filter
        loadInitialDataList()
        binding?.sortFilter?.indicatorCounter = viewModel.getFilterCount()
    }

    override fun onOtherPeriodMoreMenuClicked(dialog: OtherPeriodBottomSheet, voucher: Voucher) {
        dialog.dismiss()
        val startDate = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
            DateUtil.DEFAULT_VIEW_FORMAT,
            voucher.startTime
        )
        val finishDate = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD_T_HH_MM_SS_Z,
            DateUtil.DEFAULT_VIEW_FORMAT,
            voucher.finishTime
        )
        val title =
            context?.getString(R.string.smvc_voucherlist_voucher_date_format, startDate, finishDate)
                .toBlankOrString()
        showMoreMenuBottomSheet(voucher, title, pageSource = OtherPeriodBottomSheet::class.java.simpleName)
    }

    private fun setupObservables() {
        viewModel.voucherList.observe(viewLifecycleOwner) { vouchers ->
            val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
            adapter?.addDataList(vouchers)
            notifyLoadResult(vouchers.size >= PAGE_SIZE)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding?.loaderPage?.gone()
            view?.showToasterError(it)
        }
        viewModel.voucherQuota.observe(viewLifecycleOwner) {
            binding?.footer?.setupVoucherQuota(it)
        }
        viewModel.voucherChilds.observe(viewLifecycleOwner) {
            otherPeriodBottomSheet = OtherPeriodBottomSheet.newInstance(it)
            otherPeriodBottomSheet?.setListener(this)
            otherPeriodBottomSheet?.show(this, it.size)
            voucherListTracker.sendClickArrowOnJadwalLainEvent()
        }
        viewModel.pageState.observe(viewLifecycleOwner) {
            when (it) {
                PageState.NO_DATA_PAGE -> displayNoData()
                PageState.NO_DATA_SEARCH_PAGE -> displayNoDataSearch()
                else -> displayList()
            }
        }

        viewModel.generateShareComponentMetaData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    displayShareBottomSheet(result.data)
                }
                is Fail -> {
                }
            }
        }
    }

    private fun setupObserveDeleteUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.deleteUIEffect.collect {
                when (it) {
                    is DeleteVoucherUiEffect.SuccessDeletedVoucher -> {
                        stopVoucherDialog?.let { dialog ->
                            dialog.setLoadingProses(false)
                            dialog.setDismissDialog()
                        }
                        val successMessage = getStringSuccessStopVoucher(it.voucherStatus, it.name)
                        showSuccessToaster(successMessage)
                        loadInitialDataList()
                    }

                    is DeleteVoucherUiEffect.ShowToasterErrorDelete -> {
                        stopVoucherDialog?.let { dialog ->
                            dialog.setLoadingProses(false)
                            dialog.setDismissDialog()
                        }
                        val errorMessage = getStringFailedStopVoucher(it.voucherStatus, it.name)
                        view?.showToasterError(errorMessage, getString(R.string.smvc_ok))
                    }

                    is DeleteVoucherUiEffect.OnProgressToDeletedVoucherList -> {
                        stopVoucherDialog?.setLoadingProses(true)
                        stopVoucherDialog?.setCancelable(false)
                    }
                }
            }
        }
    }

    private fun SmvcFragmentMvcListBinding.setupView() {
        header.setupHeader()
        searchBar.setupSearchBar()
        rvVoucher.setupRvVoucher()
        rvVoucher.setupListScroll()
        sortFilter.setupFilter()
    }

    private fun SmvcFragmentMvcListFooterBinding.setupVoucherQuota(
        voucherCreationQuota: VoucherCreationQuota
    ) {
        tfQuotaCounter.text = MethodChecker.fromHtml(
            getString(
                R.string.smvc_voucherlist_quota_usage_format,
                voucherCreationQuota.remaining,
                voucherCreationQuota.total
            )
        )
        iconInfo.setOnClickListener {
            redirectToQuotaVoucherPage(voucherCreationQuota)
        }
        btnAddCoupon.setOnClickListener {
            if (voucherCreationQuota.quotaErrorMessage.isEmpty()) {
                redirectToCreateVoucherPage()
            } else {
                val actionText = getString(R.string.smvc_voucherlist_toaster_actiontext)
                it.showToasterError(voucherCreationQuota.quotaErrorMessage, actionText)
            }
        }
    }

    private fun HeaderUnify.setupHeader() {
        val colorIcon = MethodChecker.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN950
        )
        title = context.getString(R.string.smvc_voucherlist_page_title)
        addRightIcon(com.tokopedia.iconunify.R.drawable.iconunify_menu_kebab_horizontal).apply {
            setColorFilter(colorIcon, PorterDuff.Mode.MULTIPLY)
            setOnClickListener {
                eduCenterBottomSheet?.show(childFragmentManager)
                voucherListTracker.sendClickDotsOnUpperSideEvent()
            }
        }
        setNavigationOnClickListener {
            activity?.finish()
        }
    }

    private fun SearchBarUnify.setupSearchBar() {
        clearListener = {
            viewModel.setFilterKeyword("")
            loadInitialDataList()
        }
        searchBarTextField.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setFilterKeyword(textView.text.toString())
                loadInitialDataList()
                KeyboardHandler.hideSoftKeyboard(activity)
            }
            return@setOnEditorActionListener false
        }
    }

    private fun RecyclerView.setupListScroll() {
        binding?.footer?.root?.let {
            attachOnScrollListener(
                { it.slideDown() },
                { it.slideUp() }
            )
        }
    }

    private fun RecyclerView.setupRvVoucher() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = VouchersAdapter(this@MvcListFragment)
        val config = HasPaginatedList.Config(
            pageSize = PAGE_SIZE,
            onLoadNextPage = {
                // TODO: Implement loading
            },
            onLoadNextPageFinished = {
                // TODO: Implement loading
            }
        )
        attachPaging(this, config) { page, _ ->
            viewModel.getVoucherList(page, PAGE_SIZE)
        }
    }

    private fun SortFilter.setupFilter() {
        val quickFilterItems = setupQuickFilterItems()
        filterList.add(filterItemStatus)
        filterList.addAll(quickFilterItems)
        addItem(filterList)
        parentListener = {
            filterItemStatus.selectedItem = arrayListOf()
            val bottomSheet = FilterVoucherBottomSheet.newInstance(viewModel.filter)
            bottomSheet.setListener(this@MvcListFragment)
            bottomSheet.show(childFragmentManager, "")
        }
        dismissListener = parentListener

        filterItemStatus.listener = {
            val bottomSheet = FilterVoucherStatusBottomSheet()
            bottomSheet.setSelected(filterItemStatus.selectedItem)
            bottomSheet.setListener(this@MvcListFragment)
            bottomSheet.show(childFragmentManager, "")
        }
        filterItemStatus.refChipUnify.setChevronClickListener { filterItemStatus.listener.invoke() }
    }

    private fun setupQuickFilterItems(): List<SortFilterItem> {
        val voucherTypes = context?.resources?.getStringArray(R.array.type_items).orEmpty()
        val filterVoucherTypeItems = voucherTypes.map { SortFilterItem(it) }
        filterVoucherTypeItems.firstOrNull()?.listener = {
            filterVoucherTypeItems.firstOrNull()?.toggleSelected()
            applyQuickFilter(filterVoucherTypeItems)
        }
        filterVoucherTypeItems.lastOrNull()?.listener = {
            filterVoucherTypeItems.lastOrNull()?.toggleSelected()
            applyQuickFilter(filterVoucherTypeItems)
        }
        return filterVoucherTypeItems
    }

    private fun applyQuickFilter(filterVoucherTypeItems: List<SortFilterItem>) {
        val isShopVoucherSelected = filterVoucherTypeItems.firstOrNull()?.isSelected().orFalse()
        val isProductVoucherSelected = filterVoucherTypeItems.lastOrNull()?.isSelected().orFalse()
        viewModel.setFilterType(VoucherServiceType.SHOP_VOUCHER, isShopVoucherSelected)
        viewModel.setFilterType(VoucherServiceType.PRODUCT_VOUCHER, isProductVoucherSelected)
        loadInitialDataList()
        resetPaging()
    }

    fun SortFilterItem.isSelected(): Boolean {
        return type == ChipsUnify.TYPE_SELECTED
    }

    private fun loadInitialDataList() {
        val adapter = binding?.rvVoucher?.adapter as? VouchersAdapter
        resetPaging()
        adapter?.clearDataList()
        binding?.loaderPage?.show()
        viewModel.getVoucherList(INITIAL_PAGE, PAGE_SIZE)
        viewModel.getVoucherQuota()
    }

    private fun displayNoDataSearch() {
        binding?.apply {
            loaderPage.gone()
            rvVoucher.gone()
            errorPageSmall.show()
            errorPageLarge.gone()
        }
    }

    private fun displayNoData() {
        binding?.apply {
            loaderPage.gone()
            rvVoucher.gone()
            errorPageSmall.gone()
            errorPageLarge.show()
            val statusName = MvcListPageStateHelper.getStatusName(context, viewModel.filter)
            errorPageLarge.emptyStateTitleID.text = getString(
                R.string.smvc_voucherlist_empty_data_title_text,
                statusName
            )
            errorPageLarge.setPrimaryCTAClickListener {
                binding?.footer?.btnAddCoupon?.performClick()
            }
        }
    }

    private fun displayList() {
        binding?.apply {
            loaderPage.gone()
            rvVoucher.show()
            errorPageSmall.gone()
            errorPageLarge.gone()
        }
    }

    private fun redirectToCreateVoucherPage() {
        RouteManager.route(context, SELLER_MVC_CREATE)
        voucherListTracker.sendClickBuatKuponEvent()
    }

    private fun setEduCenterBottomSheet() {
        eduCenterBottomSheet = EduCenterBottomSheet.createInstance().apply {
            initRecyclerView(this@MvcListFragment.context ?: return, this@MvcListFragment)
        }
    }

    private fun setupStopConfirmationDialog() {
        stopVoucherDialog = StopVoucherConfirmationDialog(context ?: return)
    }

    override fun onMenuClicked(menu: EduCenterMenuModel) {
        when (menu.urlRoute) {
            URL_MAIN_ARTICLE -> {
                routeToUrl(menu.urlRoute.toString())
            }
            else -> {
                val introPage = Intent(context, MvcIntroActivity::class.java)
                startActivity(introPage)
            }
        }
    }

    private fun deleteVoucher(voucher: Voucher) {
        if (voucher.isSubsidy) {
            showCallTokopediaCareDialog(voucher.status)
        } else {
            showConfirmationStopVoucherDialog(voucher)
        }
    }

    private fun showCallTokopediaCareDialog(voucherStatus: VoucherStatus) {
        context?.let {
            val title = getTitleTokopediaCareDialog(voucherStatus)
            val desc = getDescTokopediaCareDialog(voucherStatus)
            CallTokopediaCareDialog(it).apply {
                setTitle(title)
                setDescription(desc)
                setOnPositiveConfirmed {
                    goToTokopediaCare()
                }
                show(getString(R.string.smvc_call_tokopedia_care), getString(R.string.smvc_back))
            }
        }
    }

    private fun getTitleTokopediaCareDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_cannot_deleted_call_tokopedia_care_title_dialog)
        } else {
            getString(R.string.smvc_cannot_canceled_call_tokopedia_care_title_dialog)
        }
    }

    private fun getDescTokopediaCareDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_cannot_deleted_call_tokopedia_care_desc_dialog)
        } else {
            getString(R.string.smvc_cannot_canceled_call_tokopedia_care_desc_dialog)
        }
    }

    private fun showConfirmationStopVoucherDialog(voucher: Voucher) {
        val voucherStatus = voucher.status
        stopVoucherDialog?.let { dialog ->
            with(dialog) {
                setOnPositiveConfirmed {
                    viewModel.stopVoucher(voucher)
                    sendTrackerOnPositiveButton(voucher)
                }
                setOnNegativeConfirmed {
                    sendTrackerOnNegativeButton(voucher)
                }

                show(
                    getTitleStopVoucherDialog(voucherStatus),
                    getStringDescStopVoucherDialog(voucherStatus, voucher.name),
                    getStringPositiveCtaStopVoucherDialog(voucherStatus)
                )
            }
        }
    }

    private fun getTitleStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_delete_voucher_confirmation_title_of_dialog)
        } else {
            getString(R.string.smvc_canceled_voucher_confirmation_title_of_dialog)
        }
    }

    private fun getStringDescStopVoucherDialog(voucherStatus: VoucherStatus, voucherName: String): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_delete_voucher_confirmation_body_dialog)
        } else {
            getString(R.string.smvc_canceled_voucher_confirmation_body_dialog, voucherName)
        }
    }

    private fun sendTrackerOnPositiveButton(voucher: Voucher) {
        if (voucher.status == VoucherStatus.NOT_STARTED) {
            stopVoucherTracker.sendClickYesCancelEvent(createLabelOnTracker(voucher))
        } else {
            stopVoucherTracker.sendClickYesStopEvent(createLabelOnTracker(voucher))
        }
    }

    private fun sendTrackerOnNegativeButton(voucher: Voucher) {
        if (voucher.status == VoucherStatus.NOT_STARTED) {
            stopVoucherTracker.sendClickNoCancelEvent(createLabelOnTracker(voucher))
        } else {
            stopVoucherTracker.sendClickNoStopEvent(createLabelOnTracker(voucher))
        }
    }

    private fun createLabelOnTracker(voucher: Voucher): String {
        return getString(R.string.smvc_tracker_stop_voucher_lable, voucher.id.toString(), "0")
    }

    private fun getStringPositiveCtaStopVoucherDialog(voucherStatus: VoucherStatus): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_yes_deleted_voucher)
        } else {
            getString(R.string.smvc_yes_canceled_voucher)
        }
    }

    private fun getStringSuccessStopVoucher(voucherStatus: VoucherStatus, voucherName: String): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_success_to_deleted_voucher, voucherName)
        } else {
            getString(R.string.smvc_success_to_cancel_voucher, voucherName)
        }
    }

    private fun getStringFailedStopVoucher(voucherStatus: VoucherStatus, voucherName: String): String {
        return if (voucherStatus == VoucherStatus.NOT_STARTED) {
            getString(R.string.smvc_failed_to_deleted_voucher, voucherName)
        } else {
            getString(R.string.smvc_failed_to_cancel_voucher, voucherName)
        }
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            activity,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun showSuccessToaster(message: String) {
        view?.let { view ->
            Toaster.build(
                view,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                getString(R.string.smvc_ok)
            ) { }.show()
        }
    }

    private fun showUpdateQuotaBottomSheet(voucher: Voucher) {
        val bottomSheet = ChangeQuotaBottomSheet.newInstance(
            getString(R.string.smvc_title_bottom_sheet_change_quota),
            voucher.id
        )

        bottomSheet.setOnSuccessUpdateQuotaListener { message ->
            showSuccessToaster(message)
            loadInitialDataList()
        }

        bottomSheet.setOnFailedQuotaListener { message ->
            view?.showToasterError(message, getString(R.string.smvc_ok))
        }

        bottomSheet.show(childFragmentManager)
    }

    private fun displayUploadResult() {
        context?.let {
            val message = sharedPreferencesUtil.getUploadResult(it)
            if (message.isNotEmpty()) {
                sharedPreferencesUtil.clearUploadResult(it)
                binding?.footer?.root.showToaster(message, getString(R.string.smvc_ok))
            }
        }
    }

    private fun redirectToQuotaVoucherPage(voucherCreationQuota: VoucherCreationQuota) {
        QuotaInfoActivity.start(context, voucherCreationQuota)
        voucherListTracker.sendClickInfoOnSisaKuotaEvent()
    }

    private fun redirectToEditPage(voucher: Voucher) {
        val intent = SummaryActivity.buildEditModeIntent(requireContext(), voucher.id)
        startActivity(intent)
    }

    private fun redirectToDuplicatePage(voucherId: Long) {
        val intent = SummaryActivity.buildDuplicateModeIntent(context, voucherId)
        startActivity(intent)
    }
}
