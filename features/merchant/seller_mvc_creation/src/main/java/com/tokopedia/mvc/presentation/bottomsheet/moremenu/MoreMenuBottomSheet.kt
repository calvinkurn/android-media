package com.tokopedia.mvc.presentation.bottomsheet.moremenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetThreeDotsMenuBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.presentation.bottomsheet.adapter.MoreMenuAdapter
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.util.constant.VoucherTypeConst
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class MoreMenuBottomSheet : BottomSheetUnify() {

    private var context: FragmentActivity? = null
    private var voucher: Voucher? = null
    private var menuItem: List<MoreMenuUiModel> = emptyList()
    private var sheetTitle: String = ""
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
        setTitle(sheetTitle)
        binding?.recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter?.clearAllElements()
        adapter?.addElement(menuItem)

        binding?.recyclerView?.adapter = adapter
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setOnMenuClickListener(callback: (MoreMenuUiModel) -> Unit) {
        this.adapter = MoreMenuAdapter(callback)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            context: FragmentActivity,
            voucher: Voucher?,
            title: String = context.resources?.getString(R.string.voucher_three_bots_title).toBlankOrString()
        ): MoreMenuBottomSheet {
            return MoreMenuBottomSheet().apply {
                this.context = context
                this.voucher = voucher
                this.sheetTitle = title

                voucher?.type?.let { type ->
                    this.menuItem = when (type) {
                        // TODO do we handle for free shipping
                        VoucherTypeConst.FREE_ONGKIR -> {
                            // TODO adjust this
                            getOngoingOptionsListMenu()
                        }
                        else -> {
                            // ONGOING
                            if (voucher.isOngoingPromo()) {
                                // return vps voucher menu
                                if (voucher.isVps) {
                                    getOngoingVpsSubsidyMenu()
                                }
                                // return subsidy voucher menu, isVps is always false here
                                else if (voucher.isSubsidy) {
                                    getOngoingVpsSubsidyMenu()
                                }
                                // return seller create voucher menu
                                getOngoingOptionsListMenu()
                            }
                            // UPCOMING
                            else if (voucher.isUpComingPromo()) {
                                // return vps voucher menu
                                if (voucher.isVps) {
                                    getUpcomingVpsSubsidyMenu()
                                }
                                // return subsidy voucher menu
                                else if (voucher.isSubsidy) {
                                    getUpcomingVpsSubsidyMenu()
                                }
                                // return seller created voucher menu
                                else {
                                    getUpcomingOptionsListMenu()
                                }
                            }
                            // STOPPED and ENDED
                            else {
                                when (voucher.status) {
                                    VoucherStatus.ENDED -> {
                                        if (voucher.isVps) {
                                            getEndedVpsSubsidyListMenu()
                                        } else if (voucher.isSubsidy) {
                                            getEndedVpsSubsidyListMenu()
                                        } else {
                                            getEndedOrCancelledOptionsListMenu()
                                        }
                                    }
                                    VoucherStatus.STOPPED -> {
                                        if (voucher.isVps) {
                                            getCancelledVpsSubsidyListMenu()
                                        } else if (voucher.isSubsidy) {
                                            getCancelledVpsSubsidyListMenu()
                                        } else {
                                            getEndedOrCancelledOptionsListMenu()
                                        }
                                    }
                                    else ->
                                        getEndedOrCancelledOptionsListMenu()
                                }
                            }
                        }
                    }
                }
            }
        }

        const val TAG: String = "MoreMenuBottomSheet"
    }

    private fun getUpcomingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                context?.getString(R.string.voucher_bs_ubah_kuota).orEmpty()
            ),
            MoreMenuUiModel.EditPeriod(
                context?.getString(R.string.voucher_bs_ubah_periode).orEmpty()
            ),
            MoreMenuUiModel.Edit(
                context?.getString(R.string.voucher_bs_ubah).orEmpty()
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            ),
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty()
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
                context?.getString(R.string.voucher_bs_ubah_kuota).orEmpty()
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty()
            ),
            MoreMenuUiModel.Share(
                context?.getString(R.string.voucher_bs_ubah_bagikan).orEmpty()
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Stop(
                context?.getString(R.string.voucher_bs_ubah_hentikan).orEmpty()
            )
        )
    }

    private fun getEndedOrCancelledOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Copy(
                context?.getString(R.string.voucher_bs_duplikat).orEmpty()
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            )
        )
    }

    private fun getVoucherDetailUpcomingOngoingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.TermsAndConditions(
                context?.getString(R.string.voucher_bs_terms_conditions).orEmpty()
            ),
            MoreMenuUiModel.Clear(
                context?.getString(R.string.voucher_bs_ubah_batalkan).orEmpty()
            )
        )
    }

    private fun getVoucherDetailEndedStoppedOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.TermsAndConditions(
                context?.getString(R.string.voucher_bs_terms_conditions).orEmpty()
            )
        )
    }

    private fun getOngoingVpsSubsidyMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty()
            ),
            MoreMenuUiModel.Share(
                context?.getString(R.string.voucher_bs_ubah_bagikan).orEmpty()
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Stop(
                context?.getString(R.string.voucher_bs_ubah_hentikan).orEmpty()
            )
        )
    }

    private fun getUpcomingVpsSubsidyMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            ),
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty()
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty()
            ),
            MoreMenuUiModel.Clear(
                context?.getString(R.string.voucher_bs_ubah_batalkan).orEmpty()
            )
        )
    }

    private fun getEndedVpsSubsidyListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            )
        )
    }

    private fun getCancelledVpsSubsidyListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Copy(
                context?.getString(R.string.voucher_bs_duplikat).orEmpty()
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            )
        )
    }

    private fun getOtherScheduleListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                context?.getString(R.string.voucher_bs_ubah_kuota).orEmpty()
            ),
            MoreMenuUiModel.Clipboard(
                context?.getString(R.string.voucher_bs_ubah_lihat_detail).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                context?.getString(R.string.voucher_bs_ubah_broadcast_chat).orEmpty()
            ),
            MoreMenuUiModel.Download(
                context?.getString(R.string.voucher_bs_ubah_download).orEmpty()
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Clear(
                context?.getString(R.string.voucher_bs_ubah_batalkan).orEmpty()
            )
        )
    }
}
