package com.tokopedia.vouchercreation.shop.voucherlist.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.datepicker.toZeroIfNull
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.universal_sharing.constants.BroadcastChannelType
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Click
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventAction.Impression
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant.EventLabel
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.consts.ShareComponentConstant
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst.JPEG_EXT
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringListener
import com.tokopedia.vouchercreation.common.tracker.SharingComponentTracker
import com.tokopedia.vouchercreation.common.utils.*
import com.tokopedia.vouchercreation.databinding.FragmentMvcVoucherListBinding
import com.tokopedia.vouchercreation.shop.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.shop.detail.view.activity.VoucherDetailActivity
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherSort
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.VoucherTargetBuyer
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.*
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.shop.voucherlist.view.activity.VoucherListActivity.Companion.SUCCESS_VOUCHER_ID_KEY
import com.tokopedia.vouchercreation.shop.voucherlist.view.activity.VoucherListActivity.Companion.UPDATE_VOUCHER_KEY
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.VoucherListAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.VoucherListAdapterFactoryImpl
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder.VoucherViewHolder
import com.tokopedia.vouchercreation.shop.voucherlist.view.viewmodel.VoucherListViewModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.*
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.filterbottomsheet.FilterBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.filterbottomsheet.FilterBy
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.headerchips.ChipType
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sortbottomsheet.SortBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sortbottomsheet.SortBy
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListFragment :
    BaseListFragment<BaseVoucherListUiModel, VoucherListAdapterFactoryImpl>(),
    VoucherViewHolder.Listener, DownloadHelper.DownloadHelperListener {

    companion object {
        const val KEY_IS_ACTIVE_VOUCHER = "is_active_voucher"
        private val MENU_VOUCHER_ACTIVE_ID = R.id.menuMvcShowVoucherActive
        private val MENU_VOUCHER_HISTORY_ID = R.id.menuMvcShowVoucherHistory

        const val INVALID_VOUCHER_ID = 0
        const val IS_SUCCESS_VOUCHER = "is_success"
        const val IS_UPDATE_VOUCHER = "is_update"
        const val VOUCHER_ID_KEY = "voucher_id"

        private const val GET_VOUCHER_LIST_ERROR = "Get voucher list error"
        private const val GET_VOUCHER_DETAIL_ERROR = "Get voucher list error"
        private const val GET_SHOP_BASIC_DATA_ERROR = "Get shop basic data error - voucher list"
        private const val CANCEL_VOUCHER_ERROR = "Cancel voucher error - voucher list"
        private const val STOP_VOUCHER_ERROR = "Stop voucher error - voucher list"

        fun newInstance(): VoucherListFragment = VoucherListFragment()
    }

    private var fragmentListener: Listener? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharingComponentTracker: SharingComponentTracker

    private var binding by autoClearedNullable<FragmentMvcVoucherListBinding>()

    private val mViewModel: VoucherListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(VoucherListViewModel::class.java)
    }
    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance(isActiveVoucher)
    }
    private val sortBottomSheet: SortBottomSheet? by lazy {
        return@lazy SortBottomSheet.createInstance()
    }
    private val filterBottomSheet: FilterBottomSheet? by lazy {
        return@lazy FilterBottomSheet.createInstance()
    }
    private var shareVoucherBottomSheet: ShareVoucherBottomSheet? = null

    private val sortItems: MutableList<SortUiModel> by lazy {
        val ctx = context ?: return@lazy mutableListOf<SortUiModel>()
        return@lazy SortBottomSheet.getMvcSortItems(ctx)
    }
    private val filterItems: MutableList<BaseFilterUiModel> by lazy {
        val ctx = context ?: return@lazy mutableListOf<BaseFilterUiModel>()
        return@lazy FilterBottomSheet.getMvcFilterItems(ctx)
    }

    private val isActiveVoucher by lazy { getBooleanArgs(KEY_IS_ACTIVE_VOUCHER, true) }

    private val isNeedToShowSuccessDialog by lazy { getBooleanArgs(IS_SUCCESS_VOUCHER, false) }
    private val isNeedToShowSuccessUpdateDialog by lazy { getBooleanArgs(IS_UPDATE_VOUCHER, false) }

    private val successVoucherId by lazy { getIntArgs(VOUCHER_ID_KEY, 0) }

    private var isToolbarAlreadyLoaded = false

    private var shopBasicData: ShopBasicDataResult? = null

    private var universalBottomSheet: UniversalShareBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMvcVoucherListBinding.inflate(LayoutInflater.from(context), container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MvcPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
        setHasOptionsMenu(true)

        // will return current time by default
        val voucherListFirstTimeVisit =
            SharedPreferencesUtil.getVoucherListFirstTimeVisit(requireActivity())

        // to determine if the ticker was ever closed by the user
        val isBcTickerClosed = SharedPreferencesUtil.isBcTickerClosed(requireActivity())
        if (!isBcTickerClosed) {
            // will return true by default
            val isVoucherListFirstTimeVisit =
                SharedPreferencesUtil.isVoucherListFirstTimeVisit(requireActivity())
            if (isVoucherListFirstTimeVisit) {
                // update the first time visit state
                SharedPreferencesUtil.setIsVoucherListFirstTimeVisit(requireActivity(), false)
                // record the time stamp
                SharedPreferencesUtil.setVoucherListFirstTimeVisit(requireActivity(), Date())
            } else {
                // check if the broadcast chat ticker duration is not over
                val isBroadCastChatTickerExpired =
                    mViewModel.isBroadCastChatTickerExpired(voucherListFirstTimeVisit)
                // show the broadcast chat ticker if the period is not expired
                mViewModel.setShowBroadCastChatTicker(!isBroadCastChatTickerExpired)
            }
        } else mViewModel.setShowBroadCastChatTicker(false)
        VoucherCreationTracking.sendOpenScreenTracking(
            if (isActiveVoucher) {
                VoucherCreationAnalyticConstant.ScreenName.VoucherList.ACTIVE
            } else {
                VoucherCreationAnalyticConstant.ScreenName.VoucherList.HISTORY
            },
            userSession.isLoggedIn,
            userSession.userId
        )

        setupView()
        observeLiveData()
    }

    private fun showUniversalBottomSheet(voucherUiModel: VoucherUiModel) {
        universalBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            setOgImageUrl(voucherUiModel.imageSquare)
            this@VoucherListFragment.context?.let {
                setBroadcastChannel(it, BroadcastChannelType.BLAST_PROMO, voucherUiModel.id.toString())
            }

            init(object: ShareBottomsheetListener {
                override fun onShareOptionClicked(shareModel: ShareModel) {
                    context?.let {
                        onItemShareClick(shareModel, voucherUiModel, shopBasicData?.shopDomain ?: "")
                    }

                }

                override fun onCloseOptionClicked() {
                    sharingComponentTracker.sendShareVoucherBottomSheetDismissClickEvent(voucherUiModel.id.toString(), VoucherCreationAnalyticConstant.Values.TRACKER_ID_DISMISS_SHARE)
                }

            })
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.MVC_PRODUCT)
            setMetaData(voucherUiModel.name, voucherUiModel.imageSquare)
            voucherUiModel.addParamImageGenerator(this, shopBasicData?.shopName ?: "", shopBasicData?.logo ?: "")
        }
        universalBottomSheet?.show(childFragmentManager, "")

        sharingComponentTracker.sendShareVoucherBottomSheetDisplayedEvent(voucherUiModel.id.toString(), VoucherCreationAnalyticConstant.Values.TRACKER_ID_VIEW_SHARE)

    }

    private fun onItemShareClick(shareModel: ShareModel, voucher: VoucherUiModel, shopDomain: String) {
        val formattedShopName = MethodChecker.fromHtml(shopBasicData?.shopName ?: "").toString()
        val shareUrl = "${TokopediaUrl.getInstance().WEB}$shopDomain"
        val linkerShareData = DataMapper.getLinkerShareData(
            LinkerData().apply {
            type = LinkerData.MERCHANT_VOUCHER
            uri = shareUrl
            id = voucher.id.toString()
            ogTitle = String.format(getString(R.string.placeholder_share_voucher_component_outgoing_title), formattedShopName)
            ogDescription = getString(R.string.share_component_voucher_outgoing_text_description)
            if (shareModel.ogImgUrl != null && shareModel.ogImgUrl!!.isNotEmpty()) {
                ogImageUrl = shareModel.ogImgUrl
            }
            deepLink = UriUtil.buildUri(ApplinkConst.SHOP, userSession.shopId)
        }
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0, linkerShareData,
                object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    linkerShareData?.url?.let { url ->
                        context?.let { context ->
                            shareModel.subjectName = voucher.name
                            val shareMessage = getShareMessage(context, voucher, userSession.shopName, url)
                            com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareMessage)
                            universalBottomSheet?.dismiss()
                        }
                    }
                }

                override fun onError(linkerError: LinkerError?) {}
            }
            )
        )
        sharingComponentTracker.sendSelectVoucherShareChannelClickEvent(
            shareModel.channel.orEmpty(),
            voucher.id.toString(),
            VoucherCreationAnalyticConstant.Values.TRACKER_ID_CLICK_CHANNEL_SHARE,
            UniversalShareBottomSheet.Companion.KEY_CONTEXTUAL_IMAGE
        )
    }

    private fun setupShareBottomSheet(status: Int = 0, promo: Int = 0): ShareVoucherBottomSheet {
        val shareVoucherBottomSheet = ShareVoucherBottomSheet.createInstance()
        shareVoucherBottomSheet.setBroadCastChatStatus(status)
        shareVoucherBottomSheet.setBroadCastChatPromo(promo)
        return shareVoucherBottomSheet
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.flush()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_mvc_voucher_active_list, menu)
        if (isActiveVoucher) {
            menu.removeItem(MENU_VOUCHER_ACTIVE_ID)
        } else {
            menu.removeItem(MENU_VOUCHER_HISTORY_ID)
        }

        // limit create voucher access
        menu.findItem(R.id.menuMvcAddVoucher)?.isVisible = mViewModel.isEligibleToCreateVoucher

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseVoucherListUiModel, VoucherListAdapterFactoryImpl> =
        VoucherListAdapter(adapterTypeFactory).apply {
            setOnAdapterInteractionListener(this@VoucherListFragment)
        }

    override fun getRecyclerViewResourceId(): Int = R.id.rvVoucherList

    override fun getSwipeRefreshLayoutResourceId(): Int = R.id.swipeMvcList

    override fun onSwipeRefresh() {
        clearAllData()
        super.onSwipeRefresh()
    }

    override fun getAdapterTypeFactory(): VoucherListAdapterFactoryImpl {
        return VoucherListAdapterFactoryImpl(this)
    }

    override fun getScreenName(): String = VoucherListFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onItemClicked(t: BaseVoucherListUiModel?) {

    }

    override fun loadData(page: Int) {
        mViewModel.currentPage = page
        if (!isToolbarAlreadyLoaded) {
            view?.run {
                binding?.searchBarMvc?.hide()
                binding?.sfVoucherList?.isVisible = false
            }
            renderList(listOf(LoadingStateUiModel(isActiveVoucher)))
        }
        mViewModel.getBroadCastMetaData()
        mViewModel.getCreateVoucherEligibility()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action = Click.BACK_BUTTON,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
                activity?.onBackPressed()
            }
            R.id.menuMvcShowVoucherActive -> {
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action = Click.HISTORY_BUTTON,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
                fragmentListener?.switchFragment(true)
            }
            R.id.menuMvcShowVoucherHistory -> {
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action = Click.HISTORY_BUTTON,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
                fragmentListener?.switchFragment(false)
            }
            R.id.menuMvcAddVoucher -> {
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action = Click.ADD_BUTTON,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
                val intent =
                    RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
                        .apply {
                            putExtra(CreateMerchantVoucherStepsActivity.FROM_VOUCHER_LIST, true)
                        }
                startActivityForResult(intent, CreateMerchantVoucherStepsActivity.REQUEST_CODE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMoreMenuClickListener(voucher: VoucherUiModel) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action =
            when (voucher.status) {
                VoucherStatusConst.ONGOING -> Click.BURGER_BUTTON_ONGOING
                VoucherStatusConst.NOT_STARTED -> Click.BURGER_BUTTON_UPCOMING
                VoucherStatusConst.ENDED -> Click.BURGER_BUTTON
                VoucherStatusConst.STOPPED -> Click.BURGER_BUTTON
                else -> ""
            },
            label =
            when (voucher.status) {
                VoucherStatusConst.ENDED -> EventLabel.ENDED
                VoucherStatusConst.STOPPED -> EventLabel.CANCELLED
                else -> ""
            },
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        activity?.run {
            KeyboardHandler.hideSoftKeyboard(this)
        }
        moreBottomSheet?.let {
            it.setOnModeClickListener(voucher) { menu ->
                onMoreMenuItemClickListener(menu, voucher)
            }
            it.show(childFragmentManager)
        }
    }

    override fun onVoucherClickListener(voucherId: Int) {
        context?.run {
            startActivity(
                VoucherDetailActivity.createDetailIntent(this)
                    .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId)
            )
        }
    }

    override fun onVoucherIconClickListener(status: Int) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.VOUCHER_ICON,
            label =
            when (status) {
                VoucherStatusConst.ONGOING -> EventLabel.ONGOING
                VoucherStatusConst.NOT_STARTED -> EventLabel.UPCOMING
                else -> ""
            },
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
    }

    override fun onShareClickListener(voucher: VoucherUiModel) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.VOUCHER_SHARE,
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        trackerClickEntrypointShare(voucher.id.toString())
        showShareBottomSheet(voucher)
    }

    override fun onEditQuotaClickListener(voucher: VoucherUiModel) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.CHANGE_QUOTA,
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        showEditQuotaBottomSheet(voucher)
    }

    private fun onMoreMenuItemClickListener(menu: MoreMenuUiModel, voucher: VoucherUiModel) {
        dismissBottomSheet<MoreMenuBottomSheet>(MoreMenuBottomSheet.TAG)
        var moreMenuClickEventAction: String? = null
        when (menu) {
            is EditQuota -> {
                moreMenuClickEventAction =
                    if (isActiveVoucher) {
                        when (voucher.status) {
                            VoucherStatusConst.ONGOING -> Click.EDIT_QUOTA_ONGOING
                            VoucherStatusConst.NOT_STARTED -> Click.EDIT_QUOTA_UPCOMING
                            else -> null
                        }
                    } else {
                        null
                    }
                showEditQuotaBottomSheet(voucher)
            }
            is ViewDetail -> {
                moreMenuClickEventAction =
                    if (isActiveVoucher) {
                        when (voucher.status) {
                            VoucherStatusConst.ONGOING -> Click.DETAIL_AND_EDIT_ONGOING
                            VoucherStatusConst.NOT_STARTED -> Click.DETAIL_AND_EDIT_UPCOMING
                            else -> null
                        }
                    } else {
                        Click.VOUCHER_DETAIL_BOTTOM_SHEET
                    }
                viewVoucherDetail(voucher.id)
            }
            is BroadCast -> {
                SharingUtil.shareToBroadCastChat(requireContext(), voucher.id)
            }
            is ShareVoucher -> {
                moreMenuClickEventAction = Click.SHARE_ONGOING
                trackerClickEntrypointShare(voucher.id.toString())
                showShareBottomSheet(voucher)
            }
            is EditPeriod -> {
                moreMenuClickEventAction = Click.CHANGE_PERIOD_UPCOMING
                showEditPeriodBottomSheet(voucher)
            }
            is DownloadVoucher -> {
                moreMenuClickEventAction = if (isActiveVoucher) {
                    when (voucher.status) {
                        VoucherStatusConst.ONGOING -> Click.DOWNLOAD_ONGOING
                        VoucherStatusConst.NOT_STARTED -> Click.DOWNLOAD_UPCOMING
                        else -> null
                    }
                } else {
                    null
                }
                showDownloadBottomSheet(voucher)
            }
            is CancelVoucher -> {
                moreMenuClickEventAction = Click.CANCEL_UPCOMING
                showCancelVoucherDialog(voucher)
            }
            is StopVoucher -> {
                moreMenuClickEventAction = Click.CANCEL_ONGOING
                showStopVoucherDialog(voucher)
            }
            is Duplicate -> {
                moreMenuClickEventAction = if (isActiveVoucher) {
                    when (voucher.status) {
                        VoucherStatusConst.ONGOING -> Click.DUPLICATE_ONGOING
                        VoucherStatusConst.NOT_STARTED -> Click.DUPLICATE_UPCOMING
                        else -> null
                    }
                } else {
                    Click.DUPLICATE_VOUCHER_BOTTOM_SHEET
                }
                duplicateVoucher(voucher)
            }
        }
        moreMenuClickEventAction?.let { eventAction ->
            VoucherCreationTracking.sendVoucherListClickTracking(
                action = eventAction,
                isActive = isActiveVoucher,
                userId = userSession.userId
            )
        }
    }

    override fun onDuplicateClickListener(voucher: VoucherUiModel) {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.DUPLICATE_VOUCHER,
            label =
            when (voucher.status) {
                VoucherStatusConst.ENDED -> EventLabel.ENDED
                VoucherStatusConst.STOPPED -> EventLabel.CANCELLED
                else -> ""
            },
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        duplicateVoucher(voucher)
    }

    override fun onErrorTryAgain() {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.TRY_AGAIN_ERROR_STATE,
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        clearAllData()
        loadInitialData()
    }

    override fun onDownloadComplete() {
        view?.showDownloadActionTicker(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(
                context,
                requestCode,
                permissions,
                grantResults
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CreateMerchantVoucherStepsActivity.REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    view?.run {
                        val errorString =
                            data?.getStringExtra(CreateMerchantVoucherStepsActivity.ERROR_INITIATE)
                        errorString?.let { message ->
                            val errorMessage =
                                if (message.isNotBlank()) {
                                    message
                                } else {
                                    context?.getString(R.string.mvc_general_error).toBlankOrString()
                                }
                            showErrorToaster(errorMessage)
                        }
                    }
                }
                Activity.RESULT_OK -> {
                    data?.getIntExtra(SUCCESS_VOUCHER_ID_KEY, 0)?.let { voucherId ->
                        data.getBooleanExtra(UPDATE_VOUCHER_KEY, false)
                            .let { isNeedToShowUpdateDialog ->
                                if (voucherId != INVALID_VOUCHER_ID) {
                                    showSuccessCreateBottomSheet(voucherId)
                                } else if (isNeedToShowUpdateDialog) {
                                    showSuccessUpdateToaster()
                                }
                            }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onImpressionListener(dataKey: String) {
        when (dataKey) {
            ErrorStateUiModel.DATA_KEY -> {
                VoucherCreationTracking.sendVoucherListImpressionTracking(
                    action = Impression.ERROR_STATE,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
            }
            NoResultStateUiModel.DATA_KEY -> {
                VoucherCreationTracking.sendVoucherListImpressionTracking(
                    action = Impression.NO_RESULT,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
            }
            EmptyStateUiModel.DATA_KEY -> {
                VoucherCreationTracking.sendVoucherListImpressionTracking(
                    action = Impression.NO_RESULT,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
            }
            else -> {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        childFragmentManager.dismissBottomSheetWithTags(
            DownloadVoucherBottomSheet.TAG,
            VoucherPeriodBottomSheet.TAG,
            EditQuotaBottomSheet.TAG,
            MoreMenuBottomSheet.TAG,
            SuccessCreateBottomSheet.TAG,
            FilterBottomSheet.TAG,
            ShareVoucherBottomSheet.TAG,
            SortBottomSheet.TAG
        )
    }

    private fun duplicateVoucher(voucher: VoucherUiModel) {
        activity?.let {
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
                    .apply {
                        putExtra(CreateMerchantVoucherStepsActivity.DUPLICATE_VOUCHER, voucher)
                        putExtra(CreateMerchantVoucherStepsActivity.IS_DUPLICATE, true)
                    }
            startActivityForResult(intent, CreateMerchantVoucherStepsActivity.REQUEST_CODE)
        }
    }

    private fun viewVoucherDetail(voucherId: Int) {
        activity?.let {
            startActivity(
                VoucherDetailActivity.createDetailIntent(it)
                    .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId)
            )
        }
    }

    private fun showSuccessCreateBottomSheet(voucherId: Int) {
        mViewModel.getSuccessCreatedVoucher(voucherId)
    }

    private fun showEditPeriodBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        VoucherPeriodBottomSheet.createInstance(voucher)
            .setOnSuccessClickListener {
                onSuccessUpdateVoucherPeriod()
            }
            .setOnFailClickListener { message ->
                val errorMessage =
                    if (message.isNotBlank()) {
                        message
                    } else {
                        context?.getString(R.string.mvc_general_error).toBlankOrString()
                    }
                view?.showErrorToaster(errorMessage)
            }
            .show(childFragmentManager)
    }

    private fun showShareBottomSheet(voucher: VoucherUiModel) {
         if (!isAdded) return
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_SHARE_SELLER, true)) {
            showUniversalBottomSheet(voucher)
        } else {
            shareVoucherBottomSheet?.setOnItemClickListener { socmedType ->
                context?.run {
                    shopBasicData?.shareVoucher(
                        context = this,
                        socmedType = socmedType,
                        voucher = voucher,
                        userId = userSession.userId,
                        shopId = userSession.shopId
                    )
                }
            }
            shareVoucherBottomSheet?.show(childFragmentManager)
        }
    }

    private fun trackerClickEntrypointShare(voucherId: String) {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_SHARE_SELLER, true)) {
            sharingComponentTracker.sendShareVoucherClickEvent(ShareComponentConstant.ENTRY_POINT_COUPON_LIST, voucherId, VoucherCreationAnalyticConstant.Values.TRACKER_ID_CLICK_ENTRYPOINT_SHARE)
        }
    }

    private fun showDownloadBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        DownloadVoucherBottomSheet.createInstance(
            voucher.image,
            voucher.imageSquare,
            userSession.userId
        )
            .setOnDownloadClickListener { voucherList ->
                context?.run {
                    permissionCheckerHelper.checkPermission(this@VoucherListFragment,
                        PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper.onPermissionDenied(this@run, permissionText)
                                view?.let {
                                    Toaster.make(
                                        it,
                                        getString(R.string.mvc_storage_permission_enabled_needed),
                                        Toast.LENGTH_LONG
                                    )
                                }
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper.onNeverAskAgain(this@run, permissionText)
                            }

                            override fun onPermissionGranted() {
                                if (ActivityCompat.checkSelfPermission(
                                        this@run,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    voucherList.forEach {
                                        downloadFiles(it.downloadVoucherType.imageUrl)
                                    }
                                }
                            }
                        })
                }
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action = Click.DOWNLOAD_VOUCHER,
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
            }
            .show(childFragmentManager)
    }

    private fun showStopVoucherDialog(voucher: VoucherUiModel) {
        StopVoucherDialog(context ?: return)
            .setOnPrimaryClickListener {
                mViewModel.cancelVoucher(voucher.id, false)
            }
            .show(voucher)
    }

    private fun showCancelVoucherDialog(voucher: VoucherUiModel) {
        CancelVoucherDialog(context ?: return)
            .setOnPrimaryClickListener {
                mViewModel.cancelVoucher(voucher.id, true)
            }
            .show(voucher)
    }

    private fun showEditQuotaBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        EditQuotaBottomSheet.createInstance(voucher)
            .setOnSuccessUpdateVoucher {
                loadInitialData()
                view?.run {
                    Toaster.make(
                        this,
                        context?.getString(R.string.mvc_quota_success).toBlankOrString(),
                        Toaster.LENGTH_LONG,
                        Toaster.TYPE_NORMAL,
                        context?.getString(R.string.mvc_oke).toBlankOrString()
                    )
                }
            }
            .setOnFailUpdateVoucher { message ->
                val errorMessage =
                    if (message.isNotBlank()) {
                        message
                    } else {
                        context?.getString(R.string.mvc_general_error).toBlankOrString()
                    }
                view?.showErrorToaster(errorMessage)
            }.show(childFragmentManager)
    }

    private fun setupView() = view?.run {
        setupActionBar()
        setupBroadCastChatTicker()
        setupRecyclerViewVoucherList()
        val filterData = ArrayList<SortFilterItem>()
        val sortFilter = SortFilterItem("Urutkan")
        sortFilter.listener = {
            sortFilter.toggle()
            sortFilter.updateSelectedRef = { chipType, _, _, _, _ ->
                sortFilter.refChipUnify.chipType = chipType
            }
            showSortBottomSheet()
        }
        filterData.add(sortFilter)
        binding?.sfVoucherList?.addItem(filterData)
        binding?.sfVoucherList?.sortFilterPrefix?.setOnClickListener {
            showFilterBottomSheet()
        }
        binding?.toolbarMvcList?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun SortFilterItem.toggle() {
        type = if(type == ChipsUnify.TYPE_NORMAL) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }

    private fun setupRecyclerViewVoucherList() {
        val rvDirection = -1
        binding?.rvVoucherList?.run {
            addItemDecoration(getMvcItemDecoration())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val status = recyclerView.canScrollVertically(rvDirection)
                    showAppBarElevation(status)
                }
            })
        }
    }

    private fun showAppBarElevation(isShown: Boolean) = view?.run {
        binding?.dividerMvcList?.visibility =
            if (isShown) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
    }

    private fun setupActionBar() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(binding?.toolbarMvcList)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val title =
                if (isActiveVoucher) context.getString(R.string.mvc_voucher_active) else context.getString(
                    R.string.mvc_voucher_history
                )
            activity.supportActionBar?.title = title
        }
        showAppBarElevation(false)
    }

    private fun setupSearchBar() {
        binding?.searchBarMvc?.run {
            searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    VoucherCreationTracking.sendVoucherListClickTracking(
                        action = Click.SEARCH_BAR,
                        isActive = isActiveVoucher,
                        userId = userSession.userId
                    )
                }
            }
            searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearAllData()
                    val keyword = searchBarTextField.text.toString()
                    if (keyword.isNotEmpty()) {

                        mViewModel.keyword = keyword

                        val sourceRequestParams = mViewModel.getVoucherSourceRequestParams(
                                isSellerCreated = mViewModel.isSellerCreated,
                                isVps = mViewModel.isVps,
                                isSubsidy = mViewModel.isSubsidy
                        )

                        mViewModel.searchVoucherByKeyword(
                                isActiveVoucher = isActiveVoucher,
                                keyword = mViewModel.keyword,
                                sourceRequestParams = sourceRequestParams,
                                targetBuyer = mViewModel.targetBuyer
                        )

                    } else {
                        loadInitialData()
                    }

                    return@setOnEditorActionListener true
                }
                false
            }
            clearListener = {
                // reset search keyword
                mViewModel.keyword = null
                clearAllData()
                loadInitialData()
            }
        }
    }

    private fun setupBroadCastChatTicker() {
        binding?.mvcTickerBc?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {}
            override fun onDismiss() {
                SharedPreferencesUtil.setIsBcTickerClosed(requireActivity(), isClosed = true)
            }
        })
    }

    private fun getMvcItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top = view.resources.getDimensionPixelSize(R.dimen.mvc_dimen_12dp)
            }
        }
    }

    private fun setOnChipListener(chip: BaseHeaderChipUiModel) {
        when (chip) {
            is HeaderChip -> {
                when (chip.type) {
                    ChipType.CHIP_SORT -> showSortBottomSheet()
                    ChipType.CHIP_FILTER -> showFilterBottomSheet()
                }
            }
            is ResetChip -> setOnResetClick()
        }
    }

    private fun setOnResetClick() {
        sortItems.clear()
        sortItems.addAll(SortBottomSheet.getMvcSortItems(requireContext()))
        filterItems.clear()
        filterItems.addAll(FilterBottomSheet.getMvcFilterItems(requireContext()))
        resetFetchValues()
    }

    private fun resetFetchValues() {
        mViewModel.voucherTarget = null
        mViewModel.voucherType = null
        mViewModel.isInverted = false
        mViewModel.isSortApplied = false

        clearAllData()
        loadInitialData()
    }

    private fun showSortBottomSheet() {
        activity?.run {
            KeyboardHandler.hideSoftKeyboard(this)
        }
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.SORT_BUTTON,
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        if (!isAdded) return
        sortBottomSheet
            ?.setOnSortClickedListener {
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action = Click.APPLY,
                    label =
                    when (it?.key) {
                        SortBy.NEWEST_DONE_DATE -> EventLabel.NEWEST
                        SortBy.OLDEST_DONE_DATE -> EventLabel.OLDEST
                        else -> ""
                    },
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
            }
            ?.setOnApplySortListener {
                applySort()
            }
            ?.setOnCancelApply { previousSortItems ->
                sortItems.clear()
                sortItems.addAll(previousSortItems)
            }
            ?.show(childFragmentManager, sortItems)
    }

    private fun showFilterBottomSheet() {
        activity?.run {
            KeyboardHandler.hideSoftKeyboard(this)
        }
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.FILTER_BY_TYPE,
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        if (!isAdded) return
        filterBottomSheet
            ?.setOnItemClickListener { key ->
                VoucherCreationTracking.sendVoucherListClickTracking(
                    action =
                    when (key) {
                        FilterBy.SPECIAL -> Click.FILTER_TYPE_PRIVATE
                        FilterBy.PUBLIC -> Click.FILTER_TYPE_PUBLIC
                        FilterBy.FREE_SHIPPING -> Click.FILTER_TYPE_SHIPPING
                        FilterBy.CASHBACK -> Click.FILTER_TYPE_CASHBACK
                        else -> ""
                    },
                    isActive = isActiveVoucher,
                    userId = userSession.userId
                )
            }
            ?.setOnApplyClickListener {
                applyFilter()
            }
            ?.setCancelApplyFilter { previousFilterItems ->
                filterItems.clear()
                filterItems.addAll(previousFilterItems)
            }
            ?.show(childFragmentManager, filterItems)
    }

    private fun applySort() {
        clearAllData()

        mViewModel.isSortApplied = true

        mViewModel.voucherSort = VoucherSort.FINISH_TIME

        val activeSort = sortItems.firstOrNull { it.isSelected }
        activeSort?.let { sort ->
            mViewModel.isInverted = sort.key == SortBy.OLDEST_DONE_DATE
        }

        loadInitialData()
    }

    private fun applyFilter() {
        clearAllData()
        mViewModel.voucherType = filterBottomSheet?.getSelectedVoucherType()
        mViewModel.voucherTarget = filterBottomSheet?.getSelectedVoucherTarget()
        mViewModel.isSellerCreated = filterBottomSheet?.isSellerCreated() ?: false
        mViewModel.isSubsidy = filterBottomSheet?.isSubsidy() ?: false
        mViewModel.isVps = filterBottomSheet?.isVps() ?: false
        if (mViewModel.isSubsidy == true || mViewModel.isVps == true) {
            mViewModel.targetBuyer = VoucherTargetBuyer.ALL_BUYER + "," + VoucherTargetBuyer.NEW_BUYER
        } else mViewModel.targetBuyer = null
        val counter = filterBottomSheet?.getFilterCounter() ?: 0
        if (counter.isMoreThanZero()) binding?.sfVoucherList?.indicatorCounter = counter
        else binding?.sfVoucherList?.resetAllFilters()
        loadInitialData()
    }

    private inline fun <reified T : BottomSheetUnify> dismissBottomSheet(tag: String) {
        val bottomSheet = childFragmentManager.findFragmentByTag(tag)
        if (bottomSheet is T) {
            bottomSheet.dismiss()
        }
    }

    private fun setOnSuccessGetVoucherList(vouchers: List<VoucherUiModel>) {
        if (isToolbarAlreadyLoaded && !isActiveVoucher) {
            renderList(vouchers, vouchers.isNotEmpty())
            if (adapter.data.isEmpty()) {
                renderList(listOf(NoResultStateUiModel()))
            }
        } else {
            clearAllData()
            if (vouchers.isEmpty()) {
                renderList(listOf(getEmptyStateUiModel()))
            } else {
                binding?.apply {
                    searchBarMvc.show()
                    sfVoucherList.show()
                    mvcTickerBc.isVisible = isActiveVoucher && mViewModel.getShowBroadCastChatTicker()
                    isToolbarAlreadyLoaded = true
                    setupSearchBar()
                }
                renderList(vouchers, vouchers.isNotEmpty() && !isActiveVoucher)
            }
        }
    }

    private fun setOnErrorGetVoucherList(throwable: Throwable) {
        clearAllData()
        renderList(listOf(ErrorStateUiModel()))
        // send crash report to firebase crashlytics
        MvcErrorHandler.logToCrashlytics(throwable, GET_VOUCHER_LIST_ERROR)
        // log error type to scalyr
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        ServerLogger.log(
            Priority.P2,
            "MVC_VALIDATE_VOUCHER_TARGET_ERROR",
            mapOf("type" to errorMessage)
        )
    }

    private fun onSuccessUpdateVoucherPeriod() {
        loadInitialData()
        view?.run {
            Toaster.make(
                this,
                context?.getString(R.string.mvc_success_update_period).toBlankOrString(),
                Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                context?.getString(R.string.mvc_oke).toBlankOrString()
            )
        }
    }

    private fun observeLiveData() {
        mViewModel.voucherList.observe(viewLifecycleOwner, Observer {
            (activity as? MvcPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
            when (it) {
                is Success -> {
                    val voucherList = it.data
                    voucherList.forEach { voucherUiModel ->
                        voucherUiModel.showNewBc = true
                    }
                    setOnSuccessGetVoucherList(voucherList)
                    binding?.rvVoucherList?.setOnLayoutListenerReady()
                }
                is Fail -> setOnErrorGetVoucherList(it.throwable)
            }
        })
        mViewModel.cancelVoucherResponseLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val voucherId = result.data
                    loadInitialData()
                    showCancellationSuccessToaster(true, voucherId)
                }
                is Fail -> {
                    if (result.throwable is VoucherCancellationException) {
                        showCancellationFailToaster(
                            true,
                            (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull()
                        )
                    }
                    // send crash report to firebase crashlytics
                    MvcErrorHandler.logToCrashlytics(result.throwable, CANCEL_VOUCHER_ERROR)
                    // log error type to scalyr
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    ServerLogger.log(
                        Priority.P2,
                        "MVC_CANCEL_VOUCHER_ERROR",
                        mapOf("type" to errorMessage)
                    )
                }
            }
        })
        mViewModel.stopVoucherResponseLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    val voucherId = result.data
                    loadInitialData()
                    showCancellationSuccessToaster(false, voucherId)
                }
                is Fail -> {
                    if (result.throwable is VoucherCancellationException) {
                        showCancellationFailToaster(
                            false,
                            (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull()
                        )
                    }
                    // send crash report to firebase crashlytics
                    MvcErrorHandler.logToCrashlytics(result.throwable, STOP_VOUCHER_ERROR)
                    // log error type to scalyr
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    ServerLogger.log(
                        Priority.P2,
                        "MVC_STOP_VOUCHER_ERROR",
                        mapOf("type" to errorMessage)
                    )
                }
            }
        })
        mViewModel.shopBasicLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> shopBasicData = result.data
                is Fail -> {
                    // send crash report to firebase crashlytics
                    MvcErrorHandler.logToCrashlytics(result.throwable, GET_SHOP_BASIC_DATA_ERROR)
                    // log error type to scalyr
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    ServerLogger.log(
                        Priority.P2,
                        "MVC_GET_SHOP_BASIC_DATA_ERROR",
                        mapOf("type" to errorMessage)
                    )
                }
            }
        })
        mViewModel.successVoucherLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    result.data.let { uiModel ->
                        uiModel.isFreeIconVisible = mViewModel.isFreeBroadCastIconVisible()
                        showBroadCastVoucherBottomSheet(uiModel)
                        mViewModel.setIsSuccessDialogDisplayed(true)
                    }
                }
                is Fail -> {
                    // send crash report to firebase crashlytics
                    MvcErrorHandler.logToCrashlytics(result.throwable, GET_VOUCHER_DETAIL_ERROR)
                    // log error type to scalyr
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    ServerLogger.log(
                        Priority.P2,
                        "MVC_GET_VOUCHER_DETAIL_ERROR",
                        mapOf("type" to errorMessage)
                    )
                }
            }
        })
        mViewModel.broadCastMetadata.observe(viewLifecycleOwner, Observer { result ->
            shareVoucherBottomSheet = when (result) {
                is Success -> {
                    val broadCastMetaData = result.data
                    // determine the free broadcast icon on success bottom sheet
                    mViewModel.setIsFreeBroadCastIconVisible(broadCastMetaData.promo)
                    setupShareBottomSheet(
                        status = broadCastMetaData.status,
                        promo = broadCastMetaData.promo
                    )
                }
                is Fail -> {
                    setupShareBottomSheet()
                }
            }

            // execute get voucher detail use case to show the bottomsheets
            if (successVoucherId != INVALID_VOUCHER_ID && isNeedToShowSuccessDialog && !mViewModel.isSuccessDialogDisplayed()) {
                showSuccessCreateBottomSheet(successVoucherId)
            } else if (isNeedToShowSuccessUpdateDialog) {
                showSuccessUpdateToaster()
            }
        })
        mViewModel.createVoucherEligibility.observe(viewLifecycleOwner, { result ->
            when(result) {
                is Success -> {
                    mViewModel.isEligibleToCreateVoucher = result.data.isCreateVoucherEligible
                    this.activity?.invalidateOptionsMenu()
                }
                is Fail -> {
                    val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                    view?.showErrorToaster(errorMessage)
                }
            }

            val sourceRequestParams = mViewModel.getVoucherSourceRequestParams(
                    isSellerCreated = mViewModel.isSellerCreated,
                    isVps = mViewModel.isVps,
                    isSubsidy = mViewModel.isSubsidy
            )

            // get voucher list data
            if (isActiveVoucher) {
                mViewModel.getActiveVoucherList(
                        isFirstTime = shopBasicData == null,
                        keyword = mViewModel.keyword,
                        type = mViewModel.voucherType,
                        target = mViewModel.voucherTarget,
                        sourceRequestParams = sourceRequestParams,
                        targetBuyer = mViewModel.targetBuyer
                )
            } else {
                mViewModel.getVoucherListHistory(
                        type = mViewModel.voucherType,
                        keyword = mViewModel.keyword,
                        targetList = mViewModel.voucherTarget,
                        sort = mViewModel.voucherSort,
                        page = mViewModel.currentPage,
                        isInverted = mViewModel.isInverted,
                        sourceRequestParams = sourceRequestParams,
                        targetBuyer = mViewModel.targetBuyer
                )
            }
        })
    }

    private fun showBroadCastVoucherBottomSheet(uiModel: VoucherUiModel) {
        BroadCastVoucherBottomSheet.createInstance(uiModel)
            .setOnShareClickListener {
                VoucherCreationTracking.sendCreateVoucherClickTracking(
                    step = VoucherCreationStep.REVIEW,
                    action = Click.VOUCHER_SUCCESS_SHARE_NOW,
                    userId = userSession.userId
                )
                trackerClickEntrypointShare(uiModel.id.toString())
                showShareBottomSheet(uiModel)
            }
            .setOnBroadCastClickListener {
                VoucherCreationTracking.sendBroadCastChatClickTracking(
                    category = VoucherCreationAnalyticConstant.EventCategory.VoucherCreation.PAGE,
                    shopId = userSession.shopId
                )
                SharingUtil.shareToBroadCastChat(requireContext(), uiModel.id)
            }
            .apply {
                clearContentPadding = true
                setCloseClickListener {
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                        step = VoucherCreationStep.REVIEW,
                        action = Click.VOUCHER_SUCCESS_CLICK_BACK_BUTTON,
                        userId = userSession.userId
                    )
                    dismiss()
                }
            }
            .show(childFragmentManager)
    }

    private fun onCreateVoucherClicked() {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action =
            if (isActiveVoucher) {
                Click.EMPTY_VOUCHER_CREATE_VOUCHER
            } else {
                Click.CREATE_VOUCHER
            },
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        RouteManager.route(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
    }

    private fun onSeeHistoryClicked() {
        VoucherCreationTracking.sendVoucherListClickTracking(
            action = Click.EMPTY_VOUCHER_HISTORY,
            isActive = isActiveVoucher,
            userId = userSession.userId
        )
        fragmentListener?.switchFragment(false)
    }

    private fun getEmptyStateUiModel() =
        EmptyStateUiModel(mViewModel.isEligibleToCreateVoucher, isActiveVoucher, ::onSeeHistoryClicked, ::onCreateVoucherClicked)

    private fun showCancellationSuccessToaster(
        isCancel: Boolean,
        voucherId: Int
    ) {
        val successMessageRes =
            if (isCancel) {
                R.string.mvc_cancel_success
            } else {
                R.string.mvc_stop_success
            }
        val successMessage = context?.getString(successMessageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_oke).toBlankOrString()

        view?.run {
            Toaster.make(this,
                successMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                actionText
            ) {
                /* nothing to do */
            }
        }
    }

    private fun showCancellationFailToaster(
        isCancel: Boolean,
        voucherId: Int
    ) {
        val errorMessageRes =
            if (isCancel) {
                R.string.mvc_cancel_fail
            } else {
                R.string.mvc_stop_fail
            }
        val errorMessage = context?.getString(errorMessageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_retry).toBlankOrString()

        view?.run {
            Toaster.make(this,
                errorMessage,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                actionText,
                View.OnClickListener {
                    mViewModel.cancelVoucher(voucherId, isCancel)
                })
        }
    }

    private fun showSuccessUpdateToaster() {
        view?.run {
            Toaster.make(this,
                context?.getString(R.string.mvc_success_update_toaster).toBlankOrString(),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                context?.getString(R.string.mvc_oke).toBlankOrString(),
                View.OnClickListener {})
        }
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun downloadFiles(uri: String) {
        activity?.let {
            try {
                val helper = DownloadHelper(
                    it,
                    uri,
                    System.currentTimeMillis().toString() + JPEG_EXT,
                    this@VoucherListFragment
                )
                helper.downloadFile { true }
            } catch (se: SecurityException) {
                MvcErrorHandler.logToCrashlytics(se, MvcError.ERROR_SECURITY)
                view?.showDownloadActionTicker(
                    isSuccess = false,
                    isInternetProblem = false
                )
            } catch (iae: IllegalArgumentException) {
                MvcErrorHandler.logToCrashlytics(iae, MvcError.ERROR_URI)
                view?.showDownloadActionTicker(
                    isSuccess = false,
                    isInternetProblem = false
                )
            } catch (ex: Exception) {
                MvcErrorHandler.logToCrashlytics(ex, MvcError.ERROR_DOWNLOAD)
                view?.showDownloadActionTicker(false)
            }
        }
    }

    private fun View.setOnLayoutListenerReady() {
        viewTreeObserver?.run {
            addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    (activity as? MvcPerformanceMonitoringListener)?.finishMonitoring()
                    removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    fun setFragmentListener(listener: Listener) {
        this.fragmentListener = listener
    }

    interface Listener {
        fun switchFragment(isActiveVoucher: Boolean)
    }
}
