package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.MoreMenuUiModel.*
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.MoreMenuAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_more_menu.view.*

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class MoreMenuBottomSheet(
        private val parent: ViewGroup
) : BottomSheetUnify() {

    companion object {
        val TAG: String = MoreMenuBottomSheet::class.java.simpleName
    }

    private var voucher: VoucherUiModel? = null
    private var menuAdapter: MoreMenuAdapter? = null

    fun setOnModeClickListener(voucher: VoucherUiModel, callback: (MoreMenuUiModel) -> Unit) {
        this.voucher = voucher
        this.menuAdapter = MoreMenuAdapter(callback)
        setupView()
    }

    private fun setupView() {
        val childView = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_more_menu, parent, false)

        childView.rvMvcBottomSheetMenu.layoutManager = LinearLayoutManager(context)
        childView.rvMvcBottomSheetMenu.adapter = menuAdapter
        childView.rvMvcBottomSheetMenu.addItemDecoration(getItemDecoration())

        setChild(childView)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    fun show(isActiveVoucher: Boolean, fm: FragmentManager) {
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
                                if (voucher?.isOngoingPromo() == true) {
                                    getOngoingVoucherMenu()
                                } else {
                                    getPendingVoucherMenu()
                                }
                            } else {
                                getVoucherHistoryMoreMenu()
                            }
                        }
                    }

            menuAdapter?.clearAllElements()
            menuAdapter?.addElement(getMenuItem)

            setTitle(String.format(parent.context?.getString(R.string.mvc_voucher_name).orEmpty(), voucher?.name.orEmpty()))
            show(fm, TAG)
        }
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
                Duplicate(parent.context.getString(R.string.mvc_duplicate), R.drawable.ic_mvc_duplicate),
                ViewDetail(parent.context.getString(R.string.mvc_view_detail), R.drawable.ic_mvc_detail)
        )
    }

    private fun getPendingVoucherMenu(): List<MoreMenuUiModel> {
        val menuItems = mutableListOf<MoreMenuUiModel>()
        menuItems.add(EditQuota(parent.context.getString(R.string.mvc_edit_quota), R.drawable.ic_mvc_edit_quota))
        menuItems.add(EditPeriod(parent.context.getString(R.string.mvc_edit_shown_period), R.drawable.ic_mvc_calendar))
        menuItems.add(ViewDetail(parent.context.getString(R.string.mvc_view_detail_and_edit_voucher), R.drawable.ic_mvc_detail))
        menuItems.add(ItemDivider)
        menuItems.add(DownloadVoucher(parent.context.getString(R.string.mvc_download), R.drawable.ic_mvc_download))
        menuItems.add(Duplicate(parent.context.getString(R.string.mvc_duplicate), R.drawable.ic_mvc_duplicate))
        menuItems.add(ItemDivider)
        menuItems.add(CancelVoucher(parent.context.getString(R.string.mvc_cancel), R.drawable.ic_mvc_cancel))
        return menuItems
    }

    private fun getOngoingVoucherMenu(): List<MoreMenuUiModel> {
        val menuItems = mutableListOf<MoreMenuUiModel>()
        menuItems.add(EditQuota(parent.context.getString(R.string.mvc_edit_quota), R.drawable.ic_mvc_edit_quota))
        menuItems.add(ViewDetail(parent.context.getString(R.string.mvc_view_detail), R.drawable.ic_mvc_detail))
        menuItems.add(ItemDivider)
        menuItems.add(ShareVoucher(parent.context.getString(R.string.mvc_share), R.drawable.ic_mvc_share))
        menuItems.add(Duplicate(parent.context.getString(R.string.mvc_duplicate), R.drawable.ic_mvc_duplicate))
        menuItems.add(DownloadVoucher(parent.context.getString(R.string.mvc_download), R.drawable.ic_mvc_download))
        menuItems.add(ItemDivider)
        menuItems.add(StopVoucher(parent.context.getString(R.string.mvc_stop), R.drawable.ic_mvc_cancel))
        return menuItems
    }

    private fun getFreeShippingHistoryMenu(): List<MoreMenuUiModel> =
            listOf(
                    InformationTicker,
                    ViewDetail(parent.context.getString(R.string.mvc_view_detail), R.drawable.ic_mvc_detail)
            )

    private fun getFreeShippingUpcomingMenu(): List<MoreMenuUiModel> =
            listOf(
                    InformationTicker,
                    ViewDetail(parent.context.getString(R.string.mvc_view_detail), R.drawable.ic_mvc_detail),
                    DownloadVoucher(parent.context.getString(R.string.mvc_download), R.drawable.ic_mvc_download),
                    CancelVoucher(parent.context.getString(R.string.mvc_cancel), R.drawable.ic_mvc_cancel)
            )

    private fun getFreeShippingOngoingMenu(): List<MoreMenuUiModel> =
            listOf(
                    InformationTicker,
                    ViewDetail(parent.context.getString(R.string.mvc_view_detail), R.drawable.ic_mvc_detail),
                    ShareVoucher(parent.context.getString(R.string.mvc_share), R.drawable.ic_mvc_share),
                    DownloadVoucher(parent.context.getString(R.string.mvc_download), R.drawable.ic_mvc_download),
                    StopVoucher(parent.context.getString(R.string.mvc_stop), R.drawable.ic_mvc_cancel)
            )
}