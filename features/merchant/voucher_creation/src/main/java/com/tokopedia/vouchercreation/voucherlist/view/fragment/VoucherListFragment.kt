package com.tokopedia.vouchercreation.voucherlist.view.fragment

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
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.util.DownloadHelper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
import com.tokopedia.vouchercreation.common.consts.VoucherCreationConst.JPEG_EXT
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcError
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.common.plt.MvcPerformanceMonitoringListener
import com.tokopedia.vouchercreation.common.utils.*
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherTargetType
import com.tokopedia.vouchercreation.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.detail.view.activity.VoucherDetailActivity
import com.tokopedia.vouchercreation.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherSort
import com.tokopedia.vouchercreation.voucherlist.model.ui.*
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity.Companion.SUCCESS_VOUCHER_ID_KEY
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity.Companion.UPDATE_VOUCHER_KEY
import com.tokopedia.vouchercreation.voucherlist.view.adapter.VoucherListAdapter
import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactoryImpl
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.VoucherViewHolder
import com.tokopedia.vouchercreation.voucherlist.view.viewmodel.VoucherListViewModel
import com.tokopedia.vouchercreation.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.voucherlist.view.widget.EditQuotaBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.SuccessCreateBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet.FilterBy
import com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips.ChipType
import com.tokopedia.vouchercreation.voucherlist.view.widget.sharebottomsheet.ShareVoucherBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBottomSheet
import com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet.SortBy
import kotlinx.android.synthetic.main.fragment_mvc_voucher_list.*
import kotlinx.android.synthetic.main.fragment_mvc_voucher_list.view.*
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/04/20
 */

class VoucherListFragment : BaseListFragment<BaseVoucherListUiModel, VoucherListAdapterFactoryImpl>(),
        VoucherViewHolder.Listener, DownloadHelper.DownloadHelperListener {

    companion object {
        const val KEY_IS_ACTIVE_VOUCHER = "is_active_voucher"
        private val MENU_VOUCHER_ACTIVE_ID = R.id.menuMvcShowVoucherActive
        private val MENU_VOUCHER_HISTORY_ID = R.id.menuMvcShowVoucherHistory

        const val IS_SUCCESS_VOUCHER = "is_success"
        const val IS_UPDATE_VOUCHER = "is_update"
        const val VOUCHER_ID_KEY = "voucher_id"

        private const val ERROR_GET_VOUCHER = "Error get voucher list"
        private const val ERROR_STOP_VOUCHER = "Error stop voucher list"

        fun newInstance(): VoucherListFragment = VoucherListFragment()
    }

    private var fragmentListener: Listener? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

    @VoucherTypeConst
    private var voucherType: Int? = null
    private var voucherTarget: List<Int>? = null
    @VoucherSort
    private var voucherSort: String = VoucherSort.FINISH_TIME
    private var isInverted: Boolean = false
    private var isSortApplied: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MvcPerformanceMonitoringListener)?.startNetworkPerformanceMonitoring()
        setHasOptionsMenu(true)

        // will return current time by default
        val voucherListFirstTimeVisit = SharedPreferencesUtil.getVoucherListFirstTimeVisit(requireActivity())

        // to determine if the ticker was ever closed by the user
        val isBcTickerClosed = SharedPreferencesUtil.isBcTickerClosed(requireActivity())
        if (!isBcTickerClosed) {
            // will return true by default
            val isVoucherListFirstTimeVisit = SharedPreferencesUtil.isVoucherListFirstTimeVisit(requireActivity())
            if (isVoucherListFirstTimeVisit) {
                // update the first time visit state
                SharedPreferencesUtil.setIsVoucherListFirstTimeVisit(requireActivity(), false)
                // record the time stamp
                SharedPreferencesUtil.setVoucherListFirstTimeVisit(requireActivity(), Date())
            } else {
                // check if the broadcast chat ticker duration is not over
                val isBroadCastChatTickerExpired = mViewModel.isBroadCastChatTickerExpired(voucherListFirstTimeVisit)
                // show the broadcast chat ticker if the period is not expired
                mViewModel.setShowBroadCastChatTicker(!isBroadCastChatTickerExpired)
            }
        } else mViewModel.setShowBroadCastChatTicker(false)

        if (successVoucherId != 0 && isNeedToShowSuccessDialog) {
            showSuccessCreateBottomSheet(successVoucherId)
        } else if (isNeedToShowSuccessUpdateDialog) {
            showSuccessUpdateToaster()
        }

        VoucherCreationTracking.sendOpenScreenTracking(
                if (isActiveVoucher) {
                    VoucherCreationAnalyticConstant.ScreenName.VoucherList.ACTIVE
                } else {
                    VoucherCreationAnalyticConstant.ScreenName.VoucherList.HISTORY
                },
                userSession.isLoggedIn,
                userSession.userId)

        setupView()
        observeLiveData()
    }

    private fun setupShareBottomSheet(status: Int = 0, quota: Int = 0): ShareVoucherBottomSheet {
        val shareVoucherBottomSheet = ShareVoucherBottomSheet.createInstance()
        shareVoucherBottomSheet.setBroadCastChatStatus(status)
        shareVoucherBottomSheet.setBroadCastChatQuota(quota)
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
        if (!isToolbarAlreadyLoaded) {
            view?.run {
                searchBarMvc.isVisible = false
                headerChipMvc.isVisible = false
            }
            renderList(listOf(LoadingStateUiModel(isActiveVoucher)))
        }
        if (isActiveVoucher) {
            mViewModel.getActiveVoucherList(shopBasicData == null)
        } else {
            mViewModel.getVoucherListHistory(voucherType, voucherTarget, voucherSort, page, isInverted)
        }
        mViewModel.getBroadCastMetaData()
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
                val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER).apply {
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
                            .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId))
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
            is ShareVoucher -> {
                moreMenuClickEventAction = Click.SHARE_ONGOING
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CreateMerchantVoucherStepsActivity.REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_CANCELED -> {
                    view?.run {
                        val errorString = data?.getStringExtra(CreateMerchantVoucherStepsActivity.ERROR_INITIATE)
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
                        data.getBooleanExtra(UPDATE_VOUCHER_KEY, false).let { isNeedToShowUpdateDialog ->
                            if (voucherId != 0) {
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
            val intent = RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER).apply {
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
                            .putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId))
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
        shareVoucherBottomSheet?.setOnItemClickListener { socmedType ->
            context?.run {
                shopBasicData?.shareVoucher(
                        context = this,
                        socmedType = socmedType,
                        voucher = voucher,
                        userId = userSession.userId,
                        shopId = userSession.shopId)
            }
        }
        shareVoucherBottomSheet?.show(childFragmentManager)
    }

    private fun showDownloadBottomSheet(voucher: VoucherUiModel) {
        if (!isAdded) return
        DownloadVoucherBottomSheet.createInstance(voucher.image, voucher.imageSquare, userSession.userId)
                .setOnDownloadClickListener { voucherList ->
                    context?.run {
                        permissionCheckerHelper.checkPermission(this@VoucherListFragment,
                                PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
                                object : PermissionCheckerHelper.PermissionCheckListener {
                                    override fun onPermissionDenied(permissionText: String) {
                                        permissionCheckerHelper.onPermissionDenied(this@run, permissionText)
                                        view?.let { Toaster.make(it, getString(R.string.mvc_storage_permission_enabled_needed), Toast.LENGTH_LONG) }
                                    }

                                    override fun onNeverAskAgain(permissionText: String) {
                                        permissionCheckerHelper.onNeverAskAgain(this@run, permissionText)
                                    }

                                    override fun onPermissionGranted() {
                                        if (ActivityCompat.checkSelfPermission(this@run, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                        Toaster.make(this,
                                context?.getString(R.string.mvc_quota_success).toBlankOrString(),
                                Toaster.LENGTH_LONG,
                                Toaster.TYPE_NORMAL,
                                context?.getString(R.string.mvc_oke).toBlankOrString())
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

        headerChipMvc.init {
            setOnChipListener(it)
        }
        toolbarMvcList?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setupRecyclerViewVoucherList() {
        val rvDirection = -1
        view?.rvVoucherList?.run {
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
        dividerMvcList?.visibility =
                if (isShown) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
    }

    private fun setupActionBar() = view?.run {
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbarMvcList)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val title = if (isActiveVoucher) context.getString(R.string.mvc_voucher_active) else context.getString(R.string.mvc_voucher_history)
            activity.supportActionBar?.title = title
        }
        showAppBarElevation(false)
    }

    private fun setupSearchBar() {
        searchBarMvc?.run {
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
                        mViewModel.setSearchKeyword(keyword)
                    } else {
                        loadInitialData()
                    }

                    return@setOnEditorActionListener true
                }
                false
            }
            clearListener = {
                clearAllData()
                loadInitialData()
            }
        }
    }

    private fun setupBroadCastChatTicker() {
        mvcTickerBc.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {}
            override fun onDismiss() {
                SharedPreferencesUtil.setIsBcTickerClosed(requireActivity(), isClosed = true)
            }
        })
    }

    private fun getMvcItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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

        view?.headerChipMvc?.resetFilter()

        resetFetchValues()
    }

    private fun resetFetchValues() {
        voucherTarget = null
        voucherType = null
        isInverted = false
        isSortApplied = false

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

        isSortApplied = true

        voucherSort = VoucherSort.FINISH_TIME

        val activeSort = sortItems.firstOrNull { it.isSelected }
        activeSort?.let { sort ->
            view?.headerChipMvc?.run {
                setActiveSort(sort)
                showResetButton()
            }

            isInverted = sort.key == SortBy.OLDEST_DONE_DATE
        }

        loadInitialData()
    }

    private fun applyFilter() {
        clearAllData()

        val activeFilterList = filterItems.filterIsInstance<BaseFilterUiModel.FilterItem>().filter { it.isSelected }
        val canResetFilter = activeFilterList.isNotEmpty()
        if (canResetFilter) {
            view?.headerChipMvc?.showResetButton()
        } else if (!isSortApplied) {
            view?.headerChipMvc?.resetFilter()
        }
        headerChipMvc?.setActiveFilter(activeFilterList)

        voucherType = null
        activeFilterList.firstOrNull { it.key == FilterBy.CASHBACK || it.key == FilterBy.FREE_SHIPPING }?.key?.let { type ->
            voucherType =
                    when (type) {
                        FilterBy.FREE_SHIPPING -> VoucherTypeConst.FREE_ONGKIR
                        FilterBy.CASHBACK -> VoucherTypeConst.CASHBACK
                        else -> VoucherTypeConst.DISCOUNT
                    }
        }

        val voucherTargetFilter = activeFilterList.filter { it.key == FilterBy.PUBLIC || it.key == FilterBy.SPECIAL }
        voucherTarget =
                if (voucherTargetFilter.size in 1..2) {
                    voucherTargetFilter.map {
                        when (it.key) {
                            FilterBy.PUBLIC -> VoucherTargetType.PUBLIC
                            FilterBy.SPECIAL -> VoucherTargetType.PRIVATE
                            else -> VoucherTargetType.PUBLIC
                        }
                    }
                } else {
                    null
                }

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
                view?.run {
                    searchBarMvc.isVisible = !isActiveVoucher
                    headerChipMvc.isVisible = !isActiveVoucher
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
        MvcErrorHandler.logToCrashlytics(throwable, ERROR_GET_VOUCHER)
    }

    private fun onSuccessUpdateVoucherPeriod() {
        loadInitialData()
        view?.run {
            Toaster.make(this,
                    context?.getString(R.string.mvc_success_update_period).toBlankOrString(),
                    Snackbar.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    context?.getString(R.string.mvc_oke).toBlankOrString())
        }
    }

    private fun observeLiveData() {
        mViewModel.voucherList.observe(viewLifecycleOwner, Observer {
            (activity as? MvcPerformanceMonitoringListener)?.startRenderPerformanceMonitoring()
            when (it) {
                is Success -> {
                    setOnSuccessGetVoucherList(it.data)
                    rvVoucherList?.setOnLayoutListenerReady()
                }
                is Fail -> setOnErrorGetVoucherList(it.throwable)
            }
        })
        mViewModel.localVoucherListLiveData.observe(viewLifecycleOwner, Observer { result ->
            setOnSuccessGetVoucherList(result)
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
                        showCancellationFailToaster(true, (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull())
                    }
                    MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_STOP_VOUCHER)
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
                        showCancellationFailToaster(false, (result.throwable as? VoucherCancellationException)?.voucherId.toZeroIfNull())
                    }
                    MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_STOP_VOUCHER)
                }
            }
        })
        mViewModel.shopBasicLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> shopBasicData = result.data
                is Fail -> MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_GET_VOUCHER)
            }
        })
        mViewModel.successVoucherLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Success -> {
                    result.data.let { uiModel ->
                        if (uiModel.isPublic) {
                            view?.run {
                                Toaster.make(this,
                                        context?.getString(R.string.mvc_success_toaster).toBlankOrString(),
                                        Toaster.LENGTH_LONG,
                                        Toaster.TYPE_NORMAL,
                                        context?.getString(R.string.mvc_oke).toBlankOrString(),
                                        View.OnClickListener {})
                            }
                        } else {
                            SuccessCreateBottomSheet.createInstance(uiModel)
                                    .setOnShareClickListener {
                                        VoucherCreationTracking.sendCreateVoucherClickTracking(
                                                step = VoucherCreationStep.REVIEW,
                                                action = Click.VOUCHER_SUCCESS_SHARE_NOW,
                                                userId = userSession.userId
                                        )
                                        showShareBottomSheet(uiModel)
                                    }
                                    .setOnDownloadClickListener {
                                        VoucherCreationTracking.sendCreateVoucherClickTracking(
                                                step = VoucherCreationStep.REVIEW,
                                                action = Click.VOUCHER_SUCCESS_DOWNLOAD,
                                                userId = userSession.userId
                                        )
                                        showDownloadBottomSheet(uiModel)
                                    }
                                    .apply {
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
                    }
                }
                is Fail -> MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_GET_VOUCHER)
            }
        })
        mViewModel.broadCastMetadata.observe(viewLifecycleOwner, Observer { result ->
            shareVoucherBottomSheet = when (result) {
                is Success -> {
                    val broadCastMetaData = result.data
                    setupShareBottomSheet(
                            status = broadCastMetaData.status,
                            quota = broadCastMetaData.quota
                    )
                }
                is Fail -> {
                    setupShareBottomSheet()
                }
            }
        })
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

    private fun getEmptyStateUiModel() = EmptyStateUiModel(isActiveVoucher, ::onSeeHistoryClicked, ::onCreateVoucherClicked)

    private fun showCancellationSuccessToaster(isCancel: Boolean,
                                               voucherId: Int) {
        val successMessageRes =
                if (isCancel) {
                    R.string.mvc_cancel_success
                } else {
                    R.string.mvc_stop_success
                }
        val successMessage = context?.getString(successMessageRes).toBlankOrString()
        val actionText = context?.getString(R.string.mvc_lihat).toBlankOrString()

        view?.run {
            Toaster.make(this,
                    successMessage,
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    actionText,
                    View.OnClickListener {
                        viewVoucherDetail(voucherId)
                    })
        }
    }

    private fun showCancellationFailToaster(isCancel: Boolean,
                                            voucherId: Int) {
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
                val helper = DownloadHelper(it, uri, System.currentTimeMillis().toString() + JPEG_EXT, this@VoucherListFragment)
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