package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.MoreMenuAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_more_menu.*

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MoreMenuBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(isActiveVoucher: Boolean): MoreMenuBottomSheet = MoreMenuBottomSheet().apply {
            arguments = Bundle().apply {
                putBoolean(IS_ACTIVE, isActiveVoucher)
            }
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        const val TAG: String = "MoreMenuBottomSheet"

        private const val IS_ACTIVE = "is_active"
    }

    private val isActiveVoucher by lazy {
        arguments?.getBoolean(IS_ACTIVE) ?: false
    }

    private var voucher: VoucherUiModel? = null
    private var menuAdapter: MoreMenuAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun initBottomSheet() {
        val childView = View.inflate(context, R.layout.bottomsheet_mvc_more_menu, null)
        setTitle(String.format(context?.getString(R.string.mvc_voucher_name).orEmpty(), voucher?.name))
        setChild(childView)
    }

    private fun setupView() {
        voucher?.type?.let { type ->
            val getMenuItem =
                    when(type) {
                        VoucherTypeConst.FREE_ONGKIR -> {
                            if (isActiveVoucher) {
                                if (voucher?.isOngoingPromo() == true) {
                                    getFreeShippingOngoingMenu()
                                } else {
                                    getFreeShippingUpcomingMenu()
                                }
                            } else {
                                getFreeShippingHistoryMenu()
                            }
                        }
                        else -> {
                            if (isActiveVoucher) {
                                // ONGOING
                                if (voucher?.isOngoingPromo() == true) {
                                    // return vps voucher menu
                                    if (voucher?.isVps == true) {
                                        getOngoingVpsVoucherMenu()
                                    }
                                    // return subsidy voucher menu
                                    else if (voucher?.isVps == false && voucher?.isSubsidy == true) {
                                        getOngoingSubsidyVoucherMenu()
                                    }
                                    // return seller created voucher menu
                                    else getOngoingVoucherMenu()
                                }
                                // UPCOMING
                                else {
                                    // return vps voucher menu
                                    if (voucher?.isVps == true) {
                                        getUpcomingVpsVoucherMenu()
                                    }
                                    // return subsidy voucher menu
                                    else if (voucher?.isVps == false && voucher?.isSubsidy == true) {
                                        getUpcomingSubsidyVoucherMenu()
                                    }
                                    // return seller created voucher menu
                                    else getUpcomingVoucherMenu()
                                }
                            }
                            // STOPPED AND ENDED
                            else {
                                when (voucher?.status) {
                                    // ended
                                    VoucherStatusConst.ENDED -> {
                                        if (voucher?.isVps == true) {
                                            getVpsVoucherHistoryMenu()
                                        }
                                        else if (voucher?.isVps == false && voucher?.isSubsidy == true) {
                                            getSubsidyVoucherHistoryMenu()
                                        }
                                        else getVoucherHistoryMoreMenu()
                                    }
                                    // stoppped
                                    VoucherStatusConst.STOPPED -> {
                                        if (voucher?.isVps == true) {
                                            getVpsVoucherHistoryMenu()
                                        }
                                        else if (voucher?.isVps == false && voucher?.isSubsidy == true) {
                                            getSubsidyVoucherHistoryMenu()
                                        }
                                        else listOf()
                                    }
                                    else -> getVoucherHistoryMoreMenu()
                                }
                            }
                        }
                    }
            menuAdapter?.clearAllElements()
            menuAdapter?.addElement(getMenuItem)
        }

        rvMvcBottomSheetMenu?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
            addItemDecoration(getItemDecoration())
        }
    }

    fun setOnModeClickListener(voucher: VoucherUiModel, callback: (MoreMenuUiModel) -> Unit) {
        this.voucher = voucher
        this.menuAdapter = MoreMenuAdapter(callback)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun getItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == menuAdapter?.itemCount?.minus(1)) {
                outRect.bottom = view.resources.getDimensionPixelSize(R.dimen.mvc_dimen_12dp)
            }
        }
    }

    private fun getVoucherHistoryMoreMenu(): List<MoreMenuUiModel> {
        return listOf(
                ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                Duplicate(context?.getString(R.string.mvc_duplicate).orEmpty(), R.drawable.ic_mvc_duplicate)
        )
    }

    private fun getVpsVoucherHistoryMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail)
            )

    private fun getSubsidyVoucherHistoryMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail)
            )

    private fun getPendingVoucherMenu(): List<MoreMenuUiModel> {
        val menuItems = mutableListOf<MoreMenuUiModel>()
        menuItems.add(EditQuota(context?.getString(R.string.mvc_edit_quota).orEmpty(), R.drawable.ic_mvc_edit_quota))
        menuItems.add(EditPeriod(context?.getString(R.string.mvc_edit_shown_period).orEmpty(), R.drawable.ic_mvc_calendar))
        menuItems.add(ViewDetail(context?.getString(R.string.mvc_view_detail_and_edit_voucher).orEmpty(), R.drawable.ic_mvc_detail))
        menuItems.add(ItemDivider)
        menuItems.add(DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download))
        menuItems.add(Duplicate(context?.getString(R.string.mvc_duplicate).orEmpty(), R.drawable.ic_mvc_duplicate))
        menuItems.add(ItemDivider)
        menuItems.add(CancelVoucher(context?.getString(R.string.mvc_cancel).orEmpty(), R.drawable.ic_mvc_cancel))
        return menuItems
    }

    private fun getUpcomingVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    EditQuota(context?.getString(R.string.mvc_edit_quota).orEmpty(), R.drawable.ic_mvc_edit_quota),
                    EditPeriod(context?.getString(R.string.mvc_edit_shown_period).orEmpty(), R.drawable.ic_mvc_calendar),
                    ViewDetail(context?.getString(R.string.mvc_view_detail_and_edit_voucher).orEmpty(), R.drawable.ic_mvc_detail),
                    ItemDivider,
                    BroadCast(context?.getString(R.string.mvc_broadcast_chat).orEmpty(), R.drawable.ic_mvc_broadcast),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download),
                    Duplicate(context?.getString(R.string.mvc_duplicate).orEmpty(), R.drawable.ic_mvc_duplicate),
                    ItemDivider,
                    CancelVoucher(context?.getString(R.string.mvc_cancel).orEmpty(), R.drawable.ic_mvc_cancel)
            )

    private fun getUpcomingVpsVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    ItemDivider,
                    BroadCast(context?.getString(R.string.mvc_broadcast_chat).orEmpty(), R.drawable.ic_mvc_broadcast),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download)
            )

    private fun getUpcomingSubsidyVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    ItemDivider,
                    BroadCast(context?.getString(R.string.mvc_broadcast_chat).orEmpty(), R.drawable.ic_mvc_broadcast),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download)
            )

    private fun getOngoingVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    EditQuota(context?.getString(R.string.mvc_edit_quota).orEmpty(), R.drawable.ic_mvc_edit_quota),
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    ItemDivider,
                    BroadCast(context?.getString(R.string.mvc_broadcast_chat).orEmpty(), R.drawable.ic_mvc_broadcast),
                    ShareVoucher(context?.getString(R.string.mvc_share).orEmpty(), R.drawable.ic_mvc_share),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download),
                    StopVoucher(context?.getString(R.string.mvc_stop).orEmpty(), R.drawable.ic_mvc_cancel)
            )

    private fun getOngoingVpsVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    ItemDivider,
                    BroadCast(context?.getString(R.string.mvc_broadcast_chat).orEmpty(), R.drawable.ic_mvc_broadcast),
                    ShareVoucher(context?.getString(R.string.mvc_share).orEmpty(), R.drawable.ic_mvc_share),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download)
            )

    private fun getOngoingSubsidyVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    ItemDivider,
                    BroadCast(context?.getString(R.string.mvc_broadcast_chat).orEmpty(), R.drawable.ic_mvc_broadcast),
                    ShareVoucher(context?.getString(R.string.mvc_share).orEmpty(), R.drawable.ic_mvc_share),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download)
            )

    private fun getFreeShippingHistoryMenu(): List<MoreMenuUiModel> =
            listOf(
                    InformationTicker,
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail)
            )

    private fun getFreeShippingUpcomingMenu(): List<MoreMenuUiModel> =
            listOf(
                    InformationTicker,
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download),
                    CancelVoucher(context?.getString(R.string.mvc_cancel).orEmpty(), R.drawable.ic_mvc_cancel)
            )

    private fun getFreeShippingOngoingMenu(): List<MoreMenuUiModel> =
            listOf(
                    InformationTicker,
                    ViewDetail(context?.getString(R.string.mvc_view_detail).orEmpty(), R.drawable.ic_mvc_detail),
                    ShareVoucher(context?.getString(R.string.mvc_share).orEmpty(), R.drawable.ic_mvc_share),
                    DownloadVoucher(context?.getString(R.string.mvc_download).orEmpty(), R.drawable.ic_mvc_download),
                    StopVoucher(context?.getString(R.string.mvc_stop).orEmpty(), R.drawable.ic_mvc_cancel)
            )
}