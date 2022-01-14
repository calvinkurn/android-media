package com.tokopedia.attachvoucher.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
}