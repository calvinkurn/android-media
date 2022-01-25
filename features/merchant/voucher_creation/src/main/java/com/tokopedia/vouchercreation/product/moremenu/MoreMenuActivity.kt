package com.tokopedia.vouchercreation.product.moremenu

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.bottmsheet.StopVoucherDialog
import com.tokopedia.vouchercreation.common.bottmsheet.downloadvoucher.DownloadVoucherBottomSheet
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.common.utils.shareVoucher
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.product.moremenu.data.entity.MoreMenuItemEventAction
import com.tokopedia.vouchercreation.product.moremenu.presentation.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.shop.create.view.activity.CreateMerchantVoucherStepsActivity
import com.tokopedia.vouchercreation.shop.detail.view.activity.VoucherDetailActivity
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.CancelVoucherDialog
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.EditQuotaBottomSheet

class MoreMenuActivity : AppCompatActivity() {

    private var isActiveVoucher = false

    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance(isActiveVoucher)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc_voucher_product_detail)

        moreBottomSheet?.show(supportFragmentManager)
        moreBottomSheet?.setOnItemClickListener(voucher = "") { moreMenuUiModel ->
//            onMoreMenuItemClickListener(moreMenuUiModel, voucher = null)
        }
    }

    private fun hitMoreMenuItemEventTracker(moreMenuItemEventAction: MoreMenuItemEventAction, @VoucherStatusConst status: Int? = null) {
        /*
            This function is created to distinguish the event action taken from MoreMenuItem.
            put event action into -action property if you don't need to distinguish with isActiveVoucher.
            put event action into -ongoingAction, -upcomingAction and -inActiveAction if you need to distinguish with isActiveVoucher.
        */
        val eventAction = when {
            moreMenuItemEventAction.action != null -> {
                moreMenuItemEventAction.action
            }
            isActiveVoucher -> {
                when (status) {
                    VoucherStatusConst.ONGOING -> moreMenuItemEventAction.ongoingAction
                    VoucherStatusConst.NOT_STARTED -> moreMenuItemEventAction.upcomingAction
                    else -> null
                }
            }
            else -> {
                moreMenuItemEventAction.inActiveAction
            }
        }

        eventAction?.let {
            VoucherCreationTracking.sendVoucherListClickTracking(
                action = eventAction,
                isActive = isActiveVoucher,
                userId = "userId"
            )
        }
    }

    private fun onMoreMenuItemClickListener(menu: MoreMenuUiModel, voucher: VoucherUiModel) {
        when (menu) {
            is MoreMenuUiModel.EditQuota -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.EDIT_QUOTA_ONGOING,
                        upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.EDIT_QUOTA_UPCOMING
                    )
                )
                showEditQuotaBottomSheet(voucher)
            }
            is MoreMenuUiModel.ViewDetail -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DETAIL_AND_EDIT_ONGOING,
                        upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DETAIL_AND_EDIT_UPCOMING,
                        inActiveAction = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_DETAIL_BOTTOM_SHEET
                    )
                )
                viewVoucherDetail(voucher.id)
            }
            is MoreMenuUiModel.BroadCast -> {
                SharingUtil.shareToBroadCastChat(this, voucher.id)
            }
            is MoreMenuUiModel.ShareVoucher -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        action = VoucherCreationAnalyticConstant.EventAction.Click.SHARE_ONGOING)
                )
                showShareBottomSheet(voucher)
            }
            is MoreMenuUiModel.EditPeriod -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        action = VoucherCreationAnalyticConstant.EventAction.Click.CHANGE_PERIOD_UPCOMING)
                )
                showEditPeriodBottomSheet(voucher)
            }
            is MoreMenuUiModel.DownloadVoucher -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_ONGOING,
                        upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_UPCOMING
                    )
                )
                showDownloadBottomSheet(voucher)
            }
            is MoreMenuUiModel.CancelVoucher -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        action = VoucherCreationAnalyticConstant.EventAction.Click.CANCEL_UPCOMING)
                )
                showCancelVoucherDialog(voucher)
            }
            is MoreMenuUiModel.StopVoucher -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        action = VoucherCreationAnalyticConstant.EventAction.Click.CANCEL_ONGOING)
                )
                showStopVoucherDialog(voucher)
            }
            is MoreMenuUiModel.Duplicate -> {
                hitMoreMenuItemEventTracker(
                    moreMenuItemEventAction = MoreMenuItemEventAction(
                        ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_ONGOING,
                        upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_UPCOMING,
                        inActiveAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_VOUCHER_BOTTOM_SHEET
                    )
                )
                duplicateVoucher(voucher)
            }
        }
    }

    private fun showEditQuotaBottomSheet(voucher: VoucherUiModel) {
//        if (!isAdded) return
        EditQuotaBottomSheet.createInstance(voucher)
            .setOnSuccessUpdateVoucher {
//                loadInitialData()
                Toaster.build(
                    View(this),
                    getString(R.string.mvc_quota_success).toBlankOrString(),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_NORMAL,
                    getString(R.string.mvc_oke).toBlankOrString()
                ).show()
            }
            .setOnFailUpdateVoucher { message ->
                val errorMessage =
                    if (message.isNotBlank()) {
                        message
                    } else {
                        getString(R.string.mvc_general_error).toBlankOrString()
                    }
                View(this).showErrorToaster(errorMessage)
            }.show(supportFragmentManager)
    }

    private fun viewVoucherDetail(voucherId: Int) {
        startActivity(VoucherDetailActivity.createDetailIntent(this).putExtra(VoucherDetailActivity.VOUCHER_ID, voucherId))
    }

    private fun showShareBottomSheet(voucher: VoucherUiModel) {
//        if (!isAdded) return
//        shareVoucherBottomSheet?.setOnItemClickListener { socmedType ->
//            context?.run {
//                shopBasicData?.shareVoucher(
//                    context = this,
//                    socmedType = socmedType,
//                    voucher = voucher,
//                    userId = userSession.userId,
//                    shopId = userSession.shopId
//                )
//            }
//        }
//        shareVoucherBottomSheet?.show(childFragmentManager)
    }

    private fun showEditPeriodBottomSheet(voucher: VoucherUiModel) {
//        if (!isAdded) return
//        VoucherPeriodBottomSheet.createInstance(voucher)
//            .setOnSuccessClickListener {
//                onSuccessUpdateVoucherPeriod()
//            }
//            .setOnFailClickListener { message ->
//                val errorMessage =
//                    if (message.isNotBlank()) {
//                        message
//                    } else {
//                        context?.getString(R.string.mvc_general_error).toBlankOrString()
//                    }
//                view?.showErrorToaster(errorMessage)
//            }
//            .show(childFragmentManager)
    }

    private fun showDownloadBottomSheet(voucher: VoucherUiModel) {
//        if (!isAdded) return
//        DownloadVoucherBottomSheet.createInstance(
//            voucher.image,
//            voucher.imageSquare,
//            userSession.userId
//        )
//            .setOnDownloadClickListener { voucherList ->
//                context?.run {
//                    permissionCheckerHelper.checkPermission(this@VoucherListFragment,
//                        PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE,
//                        object : PermissionCheckerHelper.PermissionCheckListener {
//                            override fun onPermissionDenied(permissionText: String) {
//                                permissionCheckerHelper.onPermissionDenied(this@run, permissionText)
//                                view?.let {
//                                    Toaster.make(
//                                        it,
//                                        getString(R.string.mvc_storage_permission_enabled_needed),
//                                        Toast.LENGTH_LONG
//                                    )
//                                }
//                            }
//
//                            override fun onNeverAskAgain(permissionText: String) {
//                                permissionCheckerHelper.onNeverAskAgain(this@run, permissionText)
//                            }
//
//                            override fun onPermissionGranted() {
//                                if (ActivityCompat.checkSelfPermission(
//                                        this@run,
//                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                                    ) == PackageManager.PERMISSION_GRANTED
//                                ) {
//                                    voucherList.forEach {
//                                        downloadFiles(it.downloadVoucherType.imageUrl)
//                                    }
//                                }
//                            }
//                        })
//                }
//                VoucherCreationTracking.sendVoucherListClickTracking(
//                    action = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_VOUCHER,
//                    isActive = isActiveVoucher,
//                    userId = userSession.userId
//                )
//            }
//            .show(childFragmentManager)
    }

    private fun showStopVoucherDialog(voucher: VoucherUiModel) {
//        StopVoucherDialog(context ?: return)
//            .setOnPrimaryClickListener {
//                mViewModel.cancelVoucher(voucher.id, false)
//            }
//            .show(voucher)
    }

    private fun showCancelVoucherDialog(voucher: VoucherUiModel) {
//        CancelVoucherDialog(context ?: return)
//            .setOnPrimaryClickListener {
//                mViewModel.cancelVoucher(voucher.id, true)
//            }
//            .show(voucher)
    }

    private fun duplicateVoucher(voucher: VoucherUiModel) {
//        activity?.let {
//            val intent =
//                RouteManager.getIntent(context, ApplinkConstInternalSellerapp.CREATE_VOUCHER)
//                    .apply {
//                        putExtra(CreateMerchantVoucherStepsActivity.DUPLICATE_VOUCHER, voucher)
//                        putExtra(CreateMerchantVoucherStepsActivity.IS_DUPLICATE, true)
//                    }
//            startActivityForResult(intent, CreateMerchantVoucherStepsActivity.REQUEST_CODE)
//        }
    }
}