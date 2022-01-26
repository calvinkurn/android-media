package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcMoreMenuBinding
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter.MoreMenuAdapter
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter.MoreMenuDiffer
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditQuotaVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditPeriodVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ViewDetailVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ItemDivider
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.BroadCastChat
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DownloadVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.CancelVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ShareVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DuplicateVoucher
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.StopVoucher

class MoreMenuBottomSheet : BottomSheetUnify() {

    companion object {
        const val TAG: String = "MoreMenuBottomSheet"

        @JvmStatic
        fun createInstance(): MoreMenuBottomSheet = MoreMenuBottomSheet()
    }

    private var binding by autoClearedNullable<BottomsheetMvcMoreMenuBinding>()

    private var moreMenuAdapter: MoreMenuAdapter? = null
    private var status: Int = VoucherStatusConst.NOT_STARTED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun setOnItemClickListener(@VoucherStatusConst voucherStatus: Int, callback: (MoreMenuUiModel) -> Unit) {
        status = voucherStatus
        moreMenuAdapter = MoreMenuAdapter(callback, MoreMenuDiffer())
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    private fun initBottomSheet() {
        binding = BottomsheetMvcMoreMenuBinding.inflate(LayoutInflater.from(context))
        setTitle("Nama Kupon Maks. 30 character")
        setChild(binding?.root)
    }

    private fun setupView() {
        val menuItem = getMenuListByStatusVoucher(status)
        moreMenuAdapter?.submitList(menuItem)

        binding?.rvMvcBottomSheetMenu?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = moreMenuAdapter
        }
    }

    private fun getMenuListByStatusVoucher(@VoucherStatusConst status: Int): List<MoreMenuUiModel> {
        return when (status) {
            VoucherStatusConst.NOT_STARTED -> getUpcomingStatusVoucherMenu()
            VoucherStatusConst.ONGOING -> getOngoingStatusVoucherMenu()
            VoucherStatusConst.ENDED -> getEndedStatusVoucherMenu()
            VoucherStatusConst.STOPPED -> getCanceledStatusVoucherMenu()
            else -> listOf()
        }
    }

    private fun getUpcomingStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    EditQuotaVoucher(
                        title = context?.getString(R.string.mvc_edit_quota).orEmpty(),
                        icon = IconUnify.COUPON
                    ),
                    EditPeriodVoucher(
                        title = context?.getString(R.string.mvc_edit_period).orEmpty(),
                        icon = IconUnify.CALENDAR
                    ),
                    EditVoucher(
                        title = context?.getString(R.string.mvc_edit).orEmpty(),
                        icon = IconUnify.EDIT
                    ),
                    ViewDetailVoucher(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    ),
                    ItemDivider,
                    BroadCastChat(
                        title = context?.getString(R.string.mvc_broadcast_chat).orEmpty(),
                        icon = IconUnify.BROADCAST
                    ),
                    DownloadVoucher(
                        title = context?.getString(R.string.mvc_download).orEmpty(),
                        icon = IconUnify.DOWNLOAD
                    ),
                    ItemDivider,
                    CancelVoucher(
                        title = context?.getString(R.string.mvc_cancel).orEmpty(),
                        icon = IconUnify.CLEAR
                    )
            )

    private fun getOngoingStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    EditQuotaVoucher(
                        title = context?.getString(R.string.mvc_edit_quota).orEmpty(),
                        icon = IconUnify.COUPON
                    ),
                    ViewDetailVoucher(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    ),
                    ItemDivider,
                    BroadCastChat(
                        title = context?.getString(R.string.mvc_broadcast_chat).orEmpty(),
                        icon = IconUnify.BROADCAST
                    ),
                    ShareVoucher(
                        title = context?.getString(R.string.mvc_share).orEmpty(),
                        icon = IconUnify.SHARE_MOBILE
                    ),
                    DownloadVoucher(
                        title = context?.getString(R.string.mvc_download).orEmpty(),
                        icon = IconUnify.DOWNLOAD
                    ),
                    ItemDivider,
                    StopVoucher(
                        title = context?.getString(R.string.mvc_stop).orEmpty(),
                        icon = IconUnify.CLEAR
                    )
            )

    private fun getEndedStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetailVoucher(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    )
            )

    private fun getCanceledStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    DuplicateVoucher(
                        title = context?.getString(R.string.mvc_duplicate).orEmpty(),
                        icon = IconUnify.COPY
                    ),
                    ViewDetailVoucher(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    )
            )
}