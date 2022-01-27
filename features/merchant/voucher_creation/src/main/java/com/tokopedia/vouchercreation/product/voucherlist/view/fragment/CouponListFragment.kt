package com.tokopedia.vouchercreation.product.voucherlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.model.MoreMenuItemEventAction
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditQuotaVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditPeriodVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ViewDetailVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.BroadCastChat
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DownloadVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.CancelVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ShareVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DuplicateVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.StopVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.bottomsheet.MoreMenuBottomSheet
import java.util.*

class CouponListFragment: BaseDaggerFragment() {

    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance()
    }

    private var onEditCouponMenuSelected : (Coupon) -> Unit = {}

    override fun getScreenName(): String = CouponListFragment::class.java.simpleName

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mvc_voucher_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moreBottomSheet?.show(childFragmentManager)
        moreBottomSheet?.setOnItemClickListener(VoucherStatusConst.NOT_STARTED) { menu ->
            moreBottomSheet?.dismiss()
            onMoreMenuItemClickListener(menu, "voucherDummy")
        }
    }

    private fun onMoreMenuItemClickListener(menu: MoreMenuUiModel, voucher: String) {
        when (menu) {
            is EditQuotaVoucher -> editQuotaVoucher()
            is ViewDetailVoucher -> viewDetailVoucher()
            is EditVoucher -> editVoucher()
            is BroadCastChat -> broadCastChat(123)
            is ShareVoucher -> shareVoucher()
            is EditPeriodVoucher -> editPeriod()
            is DownloadVoucher -> downloadVoucher()
            is CancelVoucher -> cancelVoucher()
            is StopVoucher -> stopVoucher()
            is DuplicateVoucher -> duplicateVoucher()
            else -> { /* do nothing */ }
        }
    }

    private fun editQuotaVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.EDIT_QUOTA_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.EDIT_QUOTA_UPCOMING
            ),
            isActiveVoucher = false
        )
        // showEditQuotaBottomSheet(voucher)
    }

    private fun editVoucher() {
        onEditCouponMenuSelected(populateDummyCoupon())
        // editVoucher(voucher)
    }

    private fun viewDetailVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DETAIL_AND_EDIT_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DETAIL_AND_EDIT_UPCOMING,
                inActiveAction = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_DETAIL_BOTTOM_SHEET
            ),
            isActiveVoucher = false
        )
        // viewVoucherDetail(voucher.id)
    }

    private fun broadCastChat(voucherId: Int) {
        context?.apply {
            SharingUtil.shareToBroadCastChat(
                context = this,
                voucherId = voucherId
            )
        }
    }

    private fun shareVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                action = VoucherCreationAnalyticConstant.EventAction.Click.SHARE_ONGOING
            ),
            isActiveVoucher = false
        )
        // showShareBottomSheet(voucher)
    }

    private fun editPeriod() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                action = VoucherCreationAnalyticConstant.EventAction.Click.CHANGE_PERIOD_UPCOMING
            ),
            isActiveVoucher = false
        )
        // showEditPeriodBottomSheet(voucher)
    }

    private fun downloadVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DOWNLOAD_UPCOMING
            ),
            isActiveVoucher = false
        )
        // showDownloadBottomSheet(voucher)
    }

    private fun cancelVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                action = VoucherCreationAnalyticConstant.EventAction.Click.CANCEL_UPCOMING
            ),
            isActiveVoucher = false
        )
        // showCancelVoucherDialog(voucher)
    }

    private fun stopVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                action = VoucherCreationAnalyticConstant.EventAction.Click.CANCEL_ONGOING
            ),
            isActiveVoucher = false
        )
        // showStopVoucherDialog(voucher)
    }

    private fun duplicateVoucher() {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_UPCOMING,
                inActiveAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_VOUCHER_BOTTOM_SHEET
            ),
            isActiveVoucher = false
        )
        // duplicateVoucher(voucher)
    }

    private fun hitMoreMenuItemEventTracker(moreMenuItemEventAction: MoreMenuItemEventAction, @VoucherStatusConst status: Int? = null, isActiveVoucher: Boolean) {
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

    fun setOnEditCouponMenuSelected(onEditCouponMenuSelected : (Coupon) -> Unit) {
        this.onEditCouponMenuSelected = onEditCouponMenuSelected
    }

    private fun populateDummyCoupon(): Coupon {
        //Stub the coupon preview data for testing purpose
        val startDate = Calendar.getInstance().apply { set(2022, 0, 28, 22, 30, 0) }
        val endDate = Calendar.getInstance().apply { set(2022, 0, 30, 22, 0, 0) }
        val period = CouponInformation.Period(startDate.time, endDate.time)

        val information = CouponInformation(
            CouponInformation.Target.PUBLIC,
            "Kupon Kopi Kenangan",
            "KOPKEN",
            period

        )

        val setting = CouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            10000,
            100,
            10000,
            10,
            5000,
            1000000
        )

        val products =
            listOf(
                CouponProduct(
                    "2147956088",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    19
                ),
                CouponProduct(
                    "15455652",
                    18000,
                    4.7F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    1000
                ),
                CouponProduct(
                    "15429644",
                    18000,
                    5.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    2100
                ),
                CouponProduct(
                    "15409031",
                    25000,
                    4.0F,
                    "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e.jpg",
                    31000
                )
            )

        return Coupon(200, "https://images.tokopedia.net/img/VqbcmM/2021/4/15/16087191-6556-40b5-9150-36944b73f85e_3.jpg", information, setting, products)
    }
}