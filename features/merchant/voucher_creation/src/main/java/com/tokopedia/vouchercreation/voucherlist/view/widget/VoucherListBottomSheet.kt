package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel
import com.tokopedia.vouchercreation.voucherlist.model.BottomSheetMenuUiModel
import com.tokopedia.vouchercreation.voucherlist.model.BottomSheetMenuUiModel.*
import com.tokopedia.vouchercreation.voucherlist.view.adapter.MenuAdapter
import com.tokopedia.vouchercreation.voucherlist.view.viewholder.MenuViewHolder
import kotlinx.android.synthetic.main.bottomsheet_mvc_voucher_list_menu.view.*

/**
 * Created By @ilhamsuaib on 18/04/20
 */

class VoucherListBottomSheet(
        private val parent: ViewGroup,
        private val listener: MenuViewHolder.Listener
) : BottomSheetUnify() {

    companion object {
        val TAG: String = VoucherListBottomSheet::class.java.simpleName
    }

    private val menuAdapter by lazy { MenuAdapter(listener) }

    init {
        val childView = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_voucher_list_menu, parent, false)

        childView.rvMvcBottomSheetMenu.layoutManager = LinearLayoutManager(context)
        childView.rvMvcBottomSheetMenu.adapter = menuAdapter

        setChild(childView)
    }

    fun show(voucher: VoucherUiModel, fm: FragmentManager) {
        val getMenuItem = if (voucher.isOngoingStatus) {
            getOngoingVoucherMenu()
        } else {
            getPendingVoucherMenu()
        }

        menuAdapter.clearAllElements()
        menuAdapter.addElement(getMenuItem)

        setTitle(voucher.name)
        show(fm, TAG)
    }

    private fun getPendingVoucherMenu(): List<BottomSheetMenuUiModel> {
        val menuItems = mutableListOf<BottomSheetMenuUiModel>()
        menuItems.add(EditQuota(parent.context.getString(R.string.mvc_edit_quota), R.drawable.ic_mvc_edit_quota))
        menuItems.add(EditPeriod(parent.context.getString(R.string.mvc_edit_shown_period), R.drawable.ic_mvc_calendar))
        menuItems.add(ViewDetail(parent.context.getString(R.string.mvc_view_detail_and_edit_voucher), R.drawable.ic_mvc_detail))
        menuItems.add(ItemDivider(ItemDivider.DIVIDER))
        menuItems.add(Download(parent.context.getString(R.string.mvc_download), R.drawable.ic_mvc_download))
        menuItems.add(Duplicate(parent.context.getString(R.string.mvc_duplicate), R.drawable.ic_mvc_duplicate))
        menuItems.add(ItemDivider(ItemDivider.DIVIDER))
        menuItems.add(CancelVoucher(parent.context.getString(R.string.mvc_cancel), R.drawable.ic_mvc_cancel))
        menuItems.add(ItemDivider(ItemDivider.EMPTY_SPACE))
        return menuItems
    }

    private fun getOngoingVoucherMenu(): List<BottomSheetMenuUiModel> {
        val menuItems = mutableListOf<BottomSheetMenuUiModel>()
        menuItems.add(EditQuota(parent.context.getString(R.string.mvc_edit_quota), R.drawable.ic_mvc_edit_quota))
        menuItems.add(ViewDetail(parent.context.getString(R.string.mvc_view_detail), R.drawable.ic_mvc_detail))
        menuItems.add(ItemDivider(ItemDivider.DIVIDER))
        menuItems.add(Share(parent.context.getString(R.string.mvc_share), R.drawable.ic_mvc_share))
        menuItems.add(Duplicate(parent.context.getString(R.string.mvc_duplicate), R.drawable.ic_mvc_duplicate))
        menuItems.add(Download(parent.context.getString(R.string.mvc_download), R.drawable.ic_mvc_download))
        menuItems.add(ItemDivider(ItemDivider.DIVIDER))
        menuItems.add(Stop(parent.context.getString(R.string.mvc_stop), R.drawable.ic_mvc_cancel))
        menuItems.add(ItemDivider(ItemDivider.EMPTY_SPACE))
        return menuItems
    }
}