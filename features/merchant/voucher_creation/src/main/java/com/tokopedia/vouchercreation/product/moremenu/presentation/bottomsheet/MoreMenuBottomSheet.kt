package com.tokopedia.vouchercreation.product.moremenu.presentation.bottomsheet

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
import com.tokopedia.vouchercreation.product.moremenu.adapter.MoreMenuAdapter
import com.tokopedia.vouchercreation.product.moremenu.adapter.MoreMenuDiffer
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.EditQuota
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.EditPeriod
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.EditVoucher
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.ViewDetail
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.ItemDivider
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.BroadCast
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.DownloadVoucher
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.CancelVoucher
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.ShareVoucher
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel.DuplicateVoucher

class MoreMenuBottomSheet : BottomSheetUnify() {

    companion object {
        private const val IS_ACTIVE = "is_active"
        const val TAG: String = "MoreMenuBottomSheet"

        @JvmStatic
        fun createInstance(isActiveVoucher: Boolean): MoreMenuBottomSheet = MoreMenuBottomSheet().apply {
            arguments = Bundle().apply {
                putBoolean(IS_ACTIVE, isActiveVoucher)
            }
        }
    }

    private var binding by autoClearedNullable<BottomsheetMvcMoreMenuBinding>()

    private val isActiveVoucher by lazy {
        arguments?.getBoolean(IS_ACTIVE) ?: false
    }

    private var menuAdapter: MoreMenuAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun setOnItemClickListener(voucher: String, callback: (MoreMenuUiModel) -> Unit) {
        this.menuAdapter = MoreMenuAdapter(callback, MoreMenuDiffer())
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
        val menuItem = getMenuListByStatusVoucher(VoucherStatusConst.NOT_STARTED)
        menuAdapter?.submitList(menuItem)

        binding?.rvMvcBottomSheetMenu?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
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
                    EditQuota(
                        title = context?.getString(R.string.mvc_edit_quota).orEmpty(),
                        icon = IconUnify.COUPON
                    ),
                    EditPeriod(
                        title = context?.getString(R.string.mvc_edit_period).orEmpty(),
                        icon = IconUnify.CALENDAR
                    ),
                    EditVoucher(
                        title = context?.getString(R.string.mvc_edit).orEmpty(),
                        icon = IconUnify.EDIT
                    ),
                    ViewDetail(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    ),
                    ItemDivider,
                    BroadCast(
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
                    EditQuota(
                        title = context?.getString(R.string.mvc_edit_quota).orEmpty(),
                        icon = IconUnify.COUPON
                    ),
                    ViewDetail(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    ),
                    ItemDivider,
                    BroadCast(
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
                    CancelVoucher(
                        title = context?.getString(R.string.mvc_stop).orEmpty(),
                        icon = IconUnify.CLEAR
                    )
            )

    private fun getEndedStatusVoucherMenu(): List<MoreMenuUiModel> =
            listOf(
                    ViewDetail(
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
                    ViewDetail(
                        title = context?.getString(R.string.mvc_view_detail).orEmpty(),
                        icon = IconUnify.CLIPBOARD
                    )
            )
}