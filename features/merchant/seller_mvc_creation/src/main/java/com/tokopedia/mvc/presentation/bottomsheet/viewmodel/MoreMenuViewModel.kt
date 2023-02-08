package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.util.StringHandler
import javax.inject.Inject

class MoreMenuViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private var menuItem: List<MoreMenuUiModel> = emptyList()

    fun getMenuList(
        voucher: Voucher?,
        isFromVoucherDetail: Boolean,
        voucherStatus: VoucherStatus?,
        isFromRecurringBottomSheet: Boolean
    ): List<MoreMenuUiModel> {
        if (isFromVoucherDetail) {
            menuItem =
                when (voucherStatus) {
                    VoucherStatus.ONGOING -> {
                        getVoucherDetailOngoingOptionsListMenu()
                    }
                    VoucherStatus.NOT_STARTED -> {
                        getVoucherDetailUpcomingOptionsListMenu()
                    }
                    else -> {
                        getVoucherDetailEndedStoppedOptionsListMenu()
                    }
                }
        } else if (isFromRecurringBottomSheet) {
            menuItem = getOtherScheduleListMenu()
        } else {
            if (voucher == null) {
                return menuItem
            } else {
                voucher.type.let {
                    menuItem =
                        // ONGOING
                        if (voucher.isOngoingPromo()) {
                            getOptionsListForOngoingPromo(voucher)
                        }
                        // UPCOMING
                        else if (voucher.isUpComingPromo()) {
                            getOptionsListForUpcomingPromo(voucher)
                        }
                        // STOPPED and ENDED
                        else {
                            when (voucher.status) {
                                VoucherStatus.ENDED -> {
                                    getOptionsListForEndedPromo(voucher)
                                }
                                VoucherStatus.STOPPED -> {
                                    getOptionsListForStoppedPromo(voucher)
                                }
                                else ->
                                    getEndedOrCancelledOptionsListMenu()
                            }
                        }
                }
            }
        }
        return menuItem
    }

    private fun getOptionsListForOngoingPromo(voucher: Voucher): List<MoreMenuUiModel> {
        // return vps voucher menu
        return if (voucher.isVps) {
            getOngoingVpsSubsidyMenu()
        }
        // return subsidy voucher menu, isVps is always false here
        else if (voucher.isSubsidy) {
            getOngoingVpsSubsidyMenu()
        } else {
            // return seller create voucher menu
            getOngoingOptionsListMenu()
        }
    }

    private fun getOptionsListForUpcomingPromo(voucher: Voucher): List<MoreMenuUiModel> {
        // return vps voucher menu
        return if (voucher.isVps) {
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

    private fun getOptionsListForStoppedPromo(voucher: Voucher): List<MoreMenuUiModel> {
        return if (voucher.isVps) {
            getCancelledVpsSubsidyListMenu()
        } else if (voucher.isSubsidy) {
            getCancelledVpsSubsidyListMenu()
        } else {
            getEndedOrCancelledOptionsListMenu()
        }
    }

    private fun getOptionsListForEndedPromo(voucher: Voucher): List<MoreMenuUiModel> {
        return if (voucher.isVps) {
            getEndedVpsSubsidyListMenu()
        } else if (voucher.isSubsidy) {
            getEndedVpsSubsidyListMenu()
        } else {
            getEndedOrCancelledOptionsListMenu()
        }
    }

    private fun getUpcomingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_kuota)
            ),
            MoreMenuUiModel.EditPeriod(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_periode)
            ),
            MoreMenuUiModel.Edit(
                StringHandler.ResourceString(R.string.voucher_bs_ubah)
            ),
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_broadcast_chat)
            ),
            MoreMenuUiModel.Download(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_download)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Clear(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_batalkan)
            )
        )
    }

    private fun getOngoingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_kuota)
            ),
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_broadcast_chat)
            ),
            MoreMenuUiModel.Share(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_bagikan)
            ),
            MoreMenuUiModel.Download(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_download)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Stop(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_hentikan)
            )
        )
    }

    private fun getEndedOrCancelledOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Copy(
                StringHandler.ResourceString(R.string.voucher_bs_duplikat)
            ),
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            )
        )
    }

    private fun getVoucherDetailUpcomingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.TermsAndConditions(
                StringHandler.ResourceString(R.string.voucher_bs_terms_conditions)
            ),
            MoreMenuUiModel.Clear(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_batalkan)
            )
        )
    }

    private fun getVoucherDetailOngoingOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.TermsAndConditions(
                StringHandler.ResourceString(R.string.voucher_bs_terms_conditions)
            ),
            MoreMenuUiModel.Stop(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_hentikan)
            )
        )
    }

    private fun getVoucherDetailEndedStoppedOptionsListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.TermsAndConditions(
                StringHandler.ResourceString(R.string.voucher_bs_terms_conditions)
            )
        )
    }

    private fun getOngoingVpsSubsidyMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_broadcast_chat)
            ),
            MoreMenuUiModel.Share(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_bagikan)
            ),
            MoreMenuUiModel.Download(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_download)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Stop(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_hentikan)
            )
        )
    }

    private fun getUpcomingVpsSubsidyMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            ),
            MoreMenuUiModel.Broadcast(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_broadcast_chat)
            ),
            MoreMenuUiModel.Download(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_download)
            ),
            MoreMenuUiModel.Clear(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_batalkan)
            )
        )
    }

    private fun getEndedVpsSubsidyListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            )
        )
    }

    private fun getCancelledVpsSubsidyListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Copy(
                StringHandler.ResourceString(R.string.voucher_bs_duplikat)
            ),
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            )
        )
    }

    // Can be used from bottomsheet recurring period
    private fun getOtherScheduleListMenu(): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.Coupon(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_kuota)
            ),
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Broadcast(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_broadcast_chat)
            ),
            MoreMenuUiModel.Download(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_download)
            ),
            MoreMenuUiModel.ItemDivider,
            MoreMenuUiModel.Clear(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_batalkan)
            )
        )
    }
}
