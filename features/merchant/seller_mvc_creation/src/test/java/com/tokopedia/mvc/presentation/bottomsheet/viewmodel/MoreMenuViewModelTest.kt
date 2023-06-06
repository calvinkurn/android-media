package com.tokopedia.mvc.presentation.bottomsheet.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.mvc.R
import com.tokopedia.mvc.data.mapper.GetInitiateVoucherPageMapper
import com.tokopedia.mvc.data.response.GetInitiateVoucherPageResponse
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.VoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.presentation.detail.VoucherDetailFragment
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel
import com.tokopedia.mvc.util.StringHandler
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoreMenuViewModelTest {

    private lateinit var viewModel: MoreMenuViewModel

    @RelaxedMockK
    lateinit var getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase

    @RelaxedMockK
    lateinit var voucherCreationMetadataObserver: Observer<in Result<VoucherCreationMetadata>>

    @RelaxedMockK
    lateinit var mapper: GetInitiateVoucherPageMapper

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MoreMenuViewModel(
            CoroutineTestDispatchersProvider,
            getInitiateVoucherPageUseCase
        )
        viewModel.voucherCreationMetadata.observeForever(voucherCreationMetadataObserver)
    }

    @After
    fun tearDown() {
        with(viewModel) {
            voucherCreationMetadata.removeObserver(voucherCreationMetadataObserver)
        }
    }

    @Test
    fun `when getVoucherCreationMetaData() is called, should set the data accordingly`() {
        runBlocking {
            // Given
            val result = mapper.map(GetInitiateVoucherPageResponse())
            coEvery { getInitiateVoucherPageUseCase.execute() } returns result

            val expected = Success(result)

            // When
            viewModel.getVoucherCreationMetadata()

            // Then
            val actual = viewModel.voucherCreationMetadata.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getVoucherCreationMetaData() is countering an error, should set the error data accordingly`() {
        runBlocking {
            // Given
            val error = MessageErrorException("Server Error")
            coEvery { getInitiateVoucherPageUseCase.execute() } throws error

            val expected = Fail(error)

            // When
            viewModel.getVoucherCreationMetadata()

            // Then
            val actual = viewModel.voucherCreationMetadata.getOrAwaitValue()
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when getMenuList() is called from VoucherDetailFragment, should return menuListItem data accordingly based on the voucher status`() {
        // Given
        val pageSource = VoucherDetailFragment::class.java.simpleName
        val ongoing = VoucherStatus.ONGOING
        val upcoming = VoucherStatus.NOT_STARTED
        val stopped = VoucherStatus.STOPPED

        val expectedOngoing = getVoucherDetailOngoingOptionsListMenu().map { listOf(it.icon) }
        val expectedUpcoming = getVoucherDetailUpcomingOptionsListMenu().map { listOf(it.icon) }
        val expectedStopped = getVoucherDetailEndedStoppedOptionsListMenu().map { listOf(it.icon) }

        // When
        val actualOngoing = viewModel.getMenuList(
            voucher = null,
            voucherStatus = ongoing,
            pageSource = pageSource,
            isDiscountPromoType = true,
            isDiscountPromoTypeEnabled = true
        ).map { listOf(it.icon) }
        val actualUpcoming = viewModel.getMenuList(
            voucher = null,
            voucherStatus = upcoming,
            pageSource = pageSource,
            isDiscountPromoType = true,
            isDiscountPromoTypeEnabled = true
        ).map { listOf(it.icon) }
        val actualStopped = viewModel.getMenuList(
            voucher = null,
            voucherStatus = stopped,
            pageSource = pageSource,
            isDiscountPromoType = true,
            isDiscountPromoTypeEnabled = true
        ).map { listOf(it.icon) }

        // Then
        assertEquals(expectedOngoing, actualOngoing)
        assertEquals(expectedUpcoming, actualUpcoming)
        assertEquals(expectedStopped, actualStopped)
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
