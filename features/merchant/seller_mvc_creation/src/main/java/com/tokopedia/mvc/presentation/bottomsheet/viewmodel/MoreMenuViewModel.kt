package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.presentation.detail.VoucherDetailFragment
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.util.StringHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class MoreMenuViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase
) : BaseViewModel(dispatchers.main) {

    private var menuItem: List<MoreMenuUiModel> = emptyList()

    private val _voucherCreationMetadata = MutableLiveData<Result<VoucherCreationMetadata>>()
    val voucherCreationMetadata: LiveData<Result<VoucherCreationMetadata>> get() = _voucherCreationMetadata

    fun getVoucherCreationMetadata() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherCreationMetadata = getInitiateVoucherPageUseCase.execute()
                _voucherCreationMetadata.postValue(Success(voucherCreationMetadata))
            },
            onError = { error ->
                _voucherCreationMetadata.postValue(Fail(error))
            }
        )
    }

    fun getMenuList(
        voucher: Voucher?,
        voucherStatus: VoucherStatus?,
        pageSource: String,
        isDiscountPromoTypeEnabled: Boolean,
        isDiscountPromoType: Boolean
    ): List<MoreMenuUiModel> {
        if (pageSource == VoucherDetailFragment::class.java.simpleName) {
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
                                    getOptionsListForEndedPromo(voucher, isDiscountPromoTypeEnabled, isDiscountPromoType)
                                }
                                VoucherStatus.STOPPED -> {
                                    getOptionsListForStoppedPromo(voucher, isDiscountPromoTypeEnabled, isDiscountPromoType)
                                }
                                else -> {
                                    getEndedOrCancelledOptionsListMenu(isDiscountPromoTypeEnabled, isDiscountPromoType)
                                }
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
        } else if (!voucher.isEditable) {
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
        } else if (!voucher.isEditable) {
            getUpcomingVpsSubsidyMenu()
        }
        // return seller created voucher menu
        else {
            getUpcomingOptionsListMenu()
        }
    }

    private fun getOptionsListForStoppedPromo(
        voucher: Voucher,
        isDiscountPromoTypeEnabled: Boolean,
        isDiscountPromoType: Boolean
    ): List<MoreMenuUiModel> {
        return if (voucher.isVps) {
            getCancelledVpsSubsidyListMenu(isDiscountPromoTypeEnabled)
        } else if (voucher.isSubsidy) {
            getCancelledVpsSubsidyListMenu(isDiscountPromoTypeEnabled)
        } else {
            getEndedOrCancelledOptionsListMenu(isDiscountPromoTypeEnabled, isDiscountPromoType)
        }
    }

    private fun getOptionsListForEndedPromo(
        voucher: Voucher,
        isDiscountPromoTypeEnabled: Boolean,
        isDiscountPromoType: Boolean
    ): List<MoreMenuUiModel> {
        return if (voucher.isVps) {
            getEndedVpsSubsidyListMenu()
        } else if (voucher.isSubsidy) {
            getEndedVpsSubsidyListMenu()
        } else {
            getEndedOrCancelledOptionsListMenu(isDiscountPromoTypeEnabled, isDiscountPromoType)
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

    private fun getEndedOrCancelledOptionsListMenu(
        isDiscountPromoTypeEnabled: Boolean,
        isDiscountPromoType: Boolean
    ): List<MoreMenuUiModel> {
        val enableDuplicateVoucherMenu = when {
            !isDiscountPromoType -> true
            isDiscountPromoType && !isDiscountPromoTypeEnabled -> false
            else -> true
        }

        return listOf(
            MoreMenuUiModel.DuplicateVoucher(
                title = StringHandler.ResourceString(R.string.voucher_bs_duplikat),
                enabled = enableDuplicateVoucherMenu
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

    private fun getCancelledVpsSubsidyListMenu(isDiscountPromoTypeEnabled: Boolean): List<MoreMenuUiModel> {
        return listOf(
            MoreMenuUiModel.DuplicateVoucher(
                title = StringHandler.ResourceString(R.string.voucher_bs_duplikat),
                enabled = isDiscountPromoTypeEnabled
            ),
            MoreMenuUiModel.Clipboard(
                StringHandler.ResourceString(R.string.voucher_bs_ubah_lihat_detail)
            )
        )
    }
}
