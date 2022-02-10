package com.tokopedia.attachvoucher.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.attachvoucher.common.matchers.withRecyclerView
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.test.base.AttachVoucherTest
import org.junit.Test

class AttachVoucherListTest: AttachVoucherTest() {

    @Test
    fun should_show_voucher_list_when_success_get_vouchers() {
        //Given
        getVoucherUseCase.response = successGetMerchantPromotionGetMVListResponse
        launchAttachVoucherActivity()

        //When
        scrollListToPosition(0)

        //Then
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(0, R.id.title))
            .check(matches(withText("Voucher Test Position 0")))
    }

    @Test
    fun should_show_empty_list_when_get_empty_vouchers() {
        //Given
        getVoucherUseCase.response = GetMerchantPromotionGetMVListResponse() //Empty list
        launchAttachVoucherActivity()

        //Then
        onView(withId(R.id.btnCreateVoucher))
            .check(matches(withText(R.string.action_attachvoucher_create_voucher)))
    }

    @Test
    fun should_show_error_view_when_fail_to_vouchers() {
        //Given
        getVoucherUseCase.errorMessage = errorMessageResponse
        launchAttachVoucherActivity()

        //Then
        checkViewNotDisplayed(R.id.flAttach)
        checkViewWithMessageDisplayed(errorMessageResponse)
    }

    @Test
    fun should_show_label_public_when_success_get_public_voucher() {
        //Given
        getVoucherUseCase.response = successGetMerchantPromotionGetMVListResponse
        launchAttachVoucherActivity()

        //When
        scrollListToPosition(0)

        //Then
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(0, R.id.voucher_publicity_status))
            .check(matches(withText("Publik")))
    }

    @Test
    fun should_show_label_private_when_success_get_private_voucher() {
        //Given
        getVoucherUseCase.response = successGetMerchantPromotionGetMVListResponse
        launchAttachVoucherActivity()

        //When
        scrollListToPosition(1)

        //Then
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(1, R.id.voucher_publicity_status))
            .check(matches(withText("Khusus")))
    }

    @Test
    fun should_show_remaining_quota_when_success_get_vouchers() {
        //Given
        getVoucherUseCase.response = successGetMerchantPromotionGetMVListResponse
        launchAttachVoucherActivity()

        //When
        scrollListToPosition(0)

        //Then
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(0, R.id.validStatus))
            .check(matches(withSubstring("Tersisa : 5")))
    }

    @Test
    fun should_show_additional_text_for_private_voucher() {
        //Given
        getVoucherUseCase.response = successGetMerchantPromotionGetMVListResponse
        launchAttachVoucherActivity()

        //When
        scrollListToPosition(1)

        //Then
        onView(withRecyclerView(R.id.recycler_view)
            .atPositionOnView(1, com.tokopedia.merchantvoucher.R.id.tvVoucherDesc))
            .check(matches(withSubstring("untuk produk tertentu")))
    }
}