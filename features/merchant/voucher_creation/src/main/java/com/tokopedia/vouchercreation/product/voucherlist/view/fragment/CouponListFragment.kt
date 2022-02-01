package com.tokopedia.vouchercreation.product.voucherlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.base.BaseSimpleListFragment
import com.tokopedia.vouchercreation.common.bottmsheet.voucherperiodbottomsheet.VoucherPeriodBottomSheet
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.voucherlist.view.adapter.CouponListAdapter
import com.tokopedia.vouchercreation.product.voucherlist.view.viewmodel.CouponListViewModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.model.MoreMenuItemEventAction
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.bottomsheet.MoreMenuBottomSheet
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.EditQuotaBottomSheet
import java.util.*
import javax.inject.Inject

class CouponListFragment: BaseSimpleListFragment<CouponListAdapter, VoucherUiModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var couponMapper: CouponMapper

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(CouponListViewModel::class.java)
    }

    private val moreBottomSheet: MoreMenuBottomSheet? by lazy {
        return@lazy MoreMenuBottomSheet.createInstance()
    }

    companion object {
        fun newInstance(
            onCreateCouponMenuSelected: () -> Unit,
            onEditCouponMenuSelected: (Coupon) -> Unit,
            onDuplicateCouponMenuSelected: (Coupon) -> Unit,
            onViewCouponDetailMenuSelected : (Long) -> Unit = {}
        ): CouponListFragment {
            val args = Bundle()
            val fragment = CouponListFragment().apply {
                arguments = args
                this.onCreateCouponMenuSelected = onCreateCouponMenuSelected
                this.onEditCouponMenuSelected = onEditCouponMenuSelected
                this.onDuplicateCouponMenuSelected = onDuplicateCouponMenuSelected
                this.onViewCouponDetailMenuSelected = onViewCouponDetailMenuSelected
            }
            return fragment
        }
    }

    private var onCreateCouponMenuSelected : () -> Unit = {}
    private var onEditCouponMenuSelected : (Coupon) -> Unit = {}
    private var onDuplicateCouponMenuSelected : (Coupon) -> Unit = {}
    private var onViewCouponDetailMenuSelected : (Long) -> Unit = {}

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
        return inflater.inflate(R.layout.fragment_mvc_coupon_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chip = view.findViewById<SortFilter>(R.id.sf_voucher_list)
        chip.setOnClickListener {

        }
        chip.parentListener = {
            onCreateCouponMenuSelected()
        }
        viewModel.couponList.observe(viewLifecycleOwner) {
            if (it is Success) {
                renderList(it.data, false)
            }
        }
    }

    override fun createAdapter() = CouponListAdapter { selectedCoupon ->
        moreBottomSheet?.show(childFragmentManager)
        moreBottomSheet?.setOnItemClickListener(selectedCoupon.status) { menu ->
            moreBottomSheet?.dismiss()
            onMoreMenuItemClickListener(menu, selectedCoupon)
        }
    }

    override fun getRecyclerView(view: View): RecyclerView = view.findViewById(R.id.rvVoucherList)

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout = view.findViewById(R.id.swipeMvcList)

    override fun addElementToAdapter(list: List<VoucherUiModel>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        viewModel.getVoucherList()
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {

    }

    override fun onHideLoading() {

    }

    override fun onDataEmpty() {

    }

    override fun onGetListError(message: String) {

    }

    private fun onMoreMenuItemClickListener(menu: MoreMenuUiModel, selectedCoupon : VoucherUiModel) {
        when (menu) {
            is EditQuotaCoupon -> editQuotaVoucher(selectedCoupon)
            is ViewDetailCoupon -> viewDetailVoucher(selectedCoupon.id.toLong())
            is EditCoupon -> editVoucher(selectedCoupon)
            is BroadCastChat -> broadCastChat(selectedCoupon.id)
            is ShareCoupon -> shareVoucher()
            is EditPeriodCoupon -> editPeriod(selectedCoupon)
            is DownloadCoupon -> downloadVoucher()
            is CancelCoupon -> cancelVoucher()
            is StopCoupon -> stopVoucher()
            is DuplicateCoupon -> duplicateVoucher(selectedCoupon)
            else -> { /* do nothing */ }
        }
    }

    private fun editQuotaVoucher(coupon: VoucherUiModel) {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.EDIT_QUOTA_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.EDIT_QUOTA_UPCOMING
            ),
            isActiveVoucher = false
        )
        showEditQuotaBottomSheet(coupon)
    }

    private fun editVoucher(coupon: VoucherUiModel) {
        onEditCouponMenuSelected(couponMapper.map(coupon))
    }

    private fun viewDetailVoucher(couponId: Long) {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DETAIL_AND_EDIT_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DETAIL_AND_EDIT_UPCOMING,
                inActiveAction = VoucherCreationAnalyticConstant.EventAction.Click.VOUCHER_DETAIL_BOTTOM_SHEET
            ),
            isActiveVoucher = false
        )
        onViewCouponDetailMenuSelected(couponId)
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

    private fun editPeriod(coupon: VoucherUiModel) {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                action = VoucherCreationAnalyticConstant.EventAction.Click.CHANGE_PERIOD_UPCOMING
            ),
            isActiveVoucher = false
        )
        showEditPeriodBottomSheet(coupon)
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

    private fun duplicateVoucher(coupon: VoucherUiModel) {
        hitMoreMenuItemEventTracker(
            moreMenuItemEventAction = MoreMenuItemEventAction(
                ongoingAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_ONGOING,
                upcomingAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_UPCOMING,
                inActiveAction = VoucherCreationAnalyticConstant.EventAction.Click.DUPLICATE_VOUCHER_BOTTOM_SHEET
            ),
            isActiveVoucher = false
        )
        onDuplicateCouponMenuSelected(couponMapper.map(coupon))
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

    private fun loadInitialData() {
        this.clearAdapterData()
        this.onShowLoading()
        this.loadData(1)
    }
}