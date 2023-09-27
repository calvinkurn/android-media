package com.tokopedia.universal_sharing.test

import com.tokopedia.universal_sharing.test.base.BaseUniversalSharingPostPurchaseBottomSheetTest
import org.junit.Test

class UniversalSharingPostPurchaseSharingTest : BaseUniversalSharingPostPurchaseBottomSheetTest() {
    @Test
    fun click_share_open_share_bottom_sheet() {
        launchActivity()
        // Click bagikan button

        // Check intent bottom sheet
    }

    @Test
    fun click_share_show_network_error_toaster() {
        launchActivity()
        // Click bagikan button

        // Check toaster error
    }

    @Test
    fun click_share_show_product_unavailable_toaster() {
        launchActivity()
        // Click bagikan button

        // Check toaster product
    }
}
