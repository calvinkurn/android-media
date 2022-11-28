package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetThreeDotsMenuBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.adapter.MoreMenuAdapter
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MoreMenuBottomSheet : BottomSheetUnify() {

    private var context: FragmentActivity? = null
    private var voucher: Voucher? = null
    private var menuItem: List<MoreMenuUiModel> = emptyList()
    private var binding by autoClearedNullable<SmvcBottomsheetThreeDotsMenuBinding>()
    private var adapter: MoreMenuAdapter? = null
    init {
        isFullpage = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcBottomsheetThreeDotsMenuBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
        setTitle(context?.getString(R.string.voucher_three_bots_title) ?: "")
        binding?.recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding?.recyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            context: FragmentActivity,
            voucher: Voucher?,
            callback: (MoreMenuUiModel) -> Unit
        ): MoreMenuBottomSheet {
            return MoreMenuBottomSheet().apply {
                this.context = context
                this.voucher = voucher
                this.menuItem = getOngoingOptionsListMenu()
//                voucher?.type?.let { type ->
//                    val getMenuItem =
//                        when(type) {
//                            VoucherTypeConst.FREE_ONGKIR -> {
//                                //Hardcoded to true
//                                if (true) {
//                                    if (voucher.isOngoingPromo()) {
//                                        getFreeShippingOngoingMenu()
//                                    } else {
//                                        getFreeShippingUpcomingMenu()
//                                    }
//                                } else {
//                                    getFreeShippingHistoryMenu()
//                                }
//                            }
//                            else -> {
//                                if (isActiveVoucher) {
//                                    // ONGOING
//                                    if (voucher.isOngoingPromo()) {
//                                        // return vps voucher menu
//                                        if (voucher?.isVps) {
//                                            getOngoingVpsVoucherMenu()
//                                        }
//                                        // return subsidy voucher menu
//                                        else if (!voucher?.isVps && voucher?.isSubsidy) {
//                                            getOngoingSubsidyVoucherMenu()
//                                        }
//                                        // return seller created voucher menu
//                                        else getOngoingVoucherMenu()
//                                    }
//                                    // UPCOMING
//                                    else {
//                                        // return vps voucher menu
//                                        if (voucher.isVps) {
//                                            getUpcomingVpsVoucherMenu()
//                                        }
//                                        // return subsidy voucher menu
//                                        else if (voucher.isVps == false && voucher.isSubsidy == true) {
//                                            getUpcomingSubsidyVoucherMenu()
//                                        }
//                                        // return seller created voucher menu
//                                        else getUpcomingVoucherMenu()
//                                    }
//                                }
//                                // STOPPED AND ENDED
//                                else {
//                                    when (voucher.status) {
//                                        // ended
//                                        VoucherStatus.ENDED -> {
//                                            if (voucher.isVps) {
//                                                getVpsVoucherHistoryMenu()
//                                            }
//                                            else if (!voucher.isVps && voucher?.isSubsidy) {
//                                                getSubsidyVoucherHistoryMenu()
//                                            }
//                                            else getVoucherHistoryMoreMenu()
//                                        }
//                                        // stoppped
//                                        VoucherStatus.STOPPED -> {
//                                            if (voucher.isVps) {
//                                                getVpsVoucherHistoryMenu()
//                                            }
//                                            else if (voucher.isVps == false && voucher.isSubsidy == true) {
//                                                getSubsidyVoucherHistoryMenu()
//                                            }
//                                            else listOf()
//                                        }
//                                        else -> getVoucherHistoryMoreMenu()
//                                    }
//                                }
//                            }
//                        }
//                    menuAdapter?.clearAllElements()
//                    menuAdapter?.addElement(getMenuItem)

                adapter = MoreMenuAdapter(callback)
                adapter?.clearAllElements()
                adapter?.addElement(menuItem)
            }
        }

        const val TAG: String = "MoreMenuBottomSheet"
    }

    private fun getUpcomingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                context?.getString(R.string.voucher_bs_ubah_kuota).orEmpty(),
            ),
            MoreMenuUiModel.Calendar(
                context?.getString(R.string.voucher_bs_ubah_periode).orEmpty(),
            ),
            MoreMenuUiModel.Edit(
                context?.getString(R.string.voucher_bs_ubah).orEmpty(),
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty(),
            ),
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty(),
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty()
            ),
            MoreMenuUiModel.Clear(
                context?.getString(R.string.voucher_bs_ubah_batalkan).orEmpty()
            )
        )
    }

    private fun getOngoingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                context?.getString(R.string.voucher_bs_ubah_kuota).orEmpty(),
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty(),
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty(),
            ),
            MoreMenuUiModel.Share(
                context?.getString(R.string.voucher_bs_ubah_bagikan).orEmpty()
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Hentikan(
                context?.getString(R.string.voucher_bs_ubah_hentikan).orEmpty()
            )
        )
    }

    private fun getEndedOrCancelledOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Copy(
                context?.getString(R.string.voucher_bs_duplikat).orEmpty(),
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty(),
            ),
        )
    }

}
