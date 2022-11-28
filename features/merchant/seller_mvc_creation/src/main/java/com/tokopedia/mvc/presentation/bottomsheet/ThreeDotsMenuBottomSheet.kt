package com.tokopedia.mvc.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetThreeDotsMenuBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.presentation.bottomsheet.adapter.MoreMenuAdapter
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VoucherThreeDotsBottomSheet : BottomSheetUnify() {

    private var context: FragmentActivity? = null
    private var entryPoint: MVCBottomSheetType = MVCBottomSheetType.UpcomingEntryPoint
    private var voucher: Voucher? = null
    private var menuAdapter: MoreMenuAdapter? = null
    private var menuItem: List<MoreMenuUiModel> = emptyList()
    private var binding by autoClearedNullable<SmvcBottomsheetThreeDotsMenuBinding>()

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
        binding?.recyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        val adapter = ThreeBotsBottomSheetAdapter(context, entryPoint)
//        when(entryPoint) {
//            is MVCBottomSheetType.UpcomingEntryPoint -> adapter.setUpcomingOptionsList()
//            is MVCBottomSheetType.OngoingEntryPoint -> adapter.setOngoingOptionsList()
//            is MVCBottomSheetType.CancelledEntryPoint -> adapter.setCancelledOptionsList()
//        }
        val adapter = MoreMenuAdapter()
        adapter?.clearAllElements()
        adapter?.addElement(menuItem)
        binding?.recyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            context: FragmentActivity,
            voucher: Voucher?
        ): VoucherThreeDotsBottomSheet {
            return VoucherThreeDotsBottomSheet().apply {
                this.context = context
                this.voucher = voucher
                this.menuItem = getUpcomingOptionsListMenu()
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
                }



            }
        }

    private fun getUpcomingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                context?.getString(R.string.voucher_bs_ubah_kuota).orEmpty(),
                IconUnify.COUPON
            ),
            MoreMenuUiModel.Calendar(
                context?.getString(R.string.voucher_bs_ubah_periode).orEmpty(),
                IconUnify.CALENDAR
            ),
            MoreMenuUiModel.Edit(
                context?.getString(R.string.voucher_bs_ubah).orEmpty(),
                IconUnify.EDIT
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty(),
                IconUnify.CLIPBOARD
            ),
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty(),
                IconUnify.BROADCAST
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty(), IconUnify.DOWNLOAD
            ),
            MoreMenuUiModel.Clear(
                context?.getString(R.string.voucher_bs_ubah_batalkan).orEmpty(),
                IconUnify.CLEAR
            )
        )
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_kuota) ?: "", IconUnify.COUPON))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_periode) ?: "", IconUnify.CALENDAR))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah) ?: "", IconUnify.EDIT))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",
//            IconUnify.CLIPBOARD))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_broadcast_chat) ?: "",
//            IconUnify.BROADCAST))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_download) ?: "", IconUnify.DOWNLOAD))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_batalkan) ?: "", IconUnify.CLEAR))
    }


//    fun setUpcomingOptionsList() {
//        this.optionsList.clear()
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_kuota) ?: "",IconUnify.COUPON))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_periode) ?: "",IconUnify.CALENDAR))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah) ?: "",IconUnify.EDIT))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",IconUnify.CLIPBOARD))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_broadcast_chat) ?: "",IconUnify.BROADCAST))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_download) ?: "",IconUnify.DOWNLOAD))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_batalkan) ?: "",IconUnify.CLEAR))
//    }
//
//    fun setOngoingOptionsList() {
//        this.optionsList.clear()
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_kuota) ?: "",IconUnify.COUPON))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",IconUnify.CLIPBOARD))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_broadcast_chat) ?: "",IconUnify.BROADCAST))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_bagikan) ?: "",IconUnify.SHARE_MOBILE))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_download) ?: "",IconUnify.DOWNLOAD))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_hentikan) ?: "",IconUnify.CLEAR))
//    }
//
//    fun setCancelledOptionsList() {
//        this.optionsList.clear()
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_duplikat) ?: "",IconUnify.COPY))
//        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",IconUnify.CLIPBOARD))
//    }


    }


sealed class MVCBottomSheetType {
    object UpcomingEntryPoint : MVCBottomSheetType()
    object OngoingEntryPoint: MVCBottomSheetType()
    object CancelledEntryPoint: MVCBottomSheetType()
}
