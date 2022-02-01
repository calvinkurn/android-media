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
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ItemDivider
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditQuotaCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditPeriodCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ViewDetailCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.BroadCastChat
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DownloadCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.CancelCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.ShareCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.DuplicateCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.StopCoupon
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel.EditCoupon

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
                    EditQuotaCoupon(
                        title = context?.getString(R.string.mvc_edit_quota).orEmpty(),
                        icon = IconUnify.COUPON
                    ),
                    EditPeriodCoupon(
                        title = context?.getString(R.string.mvc_edit_period).orEmpty(),
                        icon = IconUnify.CALENDAR
                    ),
                    EditCoupon(
                        title = context?.getString(R.string.mvc_edit).orEmpty(),
                        icon = IconUnify.EDIT
                    ),
                    ViewDetailCoupon(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    ),
                    ItemDivider,
                    BroadCastChat(
                        title = context?.getString(R.string.mvc_broadcast_chat).orEmpty(),
                        icon = IconUnify.BROADCAST
                    ),
                    DownloadCoupon(
                        title = context?.getString(R.string.mvc_download).orEmpty(),
                        icon = IconUnify.DOWNLOAD
                    ),
                    ItemDivider,
                    CancelCoupon(
                        title = context?.getString(R.string.mvc_cancel).orEmpty(),
                        icon = IconUnify.CLEAR
                    )
            )

    private fun getOngoingStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    EditQuotaCoupon(
                        title = context?.getString(R.string.mvc_edit_quota).orEmpty(),
                        icon = IconUnify.COUPON
                    ),
                    ViewDetailCoupon(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    ),
                    ItemDivider,
                    BroadCastChat(
                        title = context?.getString(R.string.mvc_broadcast_chat).orEmpty(),
                        icon = IconUnify.BROADCAST
                    ),
                    ShareCoupon(
                        title = context?.getString(R.string.mvc_share).orEmpty(),
                        icon = IconUnify.SHARE_MOBILE
                    ),
                    DownloadCoupon(
                        title = context?.getString(R.string.mvc_download).orEmpty(),
                        icon = IconUnify.DOWNLOAD
                    ),
                    ItemDivider,
                    StopCoupon(
                        title = context?.getString(R.string.mvc_stop).orEmpty(),
                        icon = IconUnify.CLEAR
                    )
            )

    private fun getEndedStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetailCoupon(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    )
            )

    private fun getCanceledStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    DuplicateCoupon(
                        title = context?.getString(R.string.mvc_duplicate).orEmpty(),
                        icon = IconUnify.COPY
                    ),
                    ViewDetailCoupon(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    )
            )
}