package com.tokopedia.updateinactivephone.features.successpage.withpin

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.successpage.BaseSuccessPageTest
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.checkSuccessPageIsDisplayed
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.checkTickerContent
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.clickOnGotoHomeButton
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForEmailAndPhoneNumber
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForEmailOnly
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForExpedited
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isTickerDisplayed
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isTitleForExpedited
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class WithPinSuccessPageGeneralTest : BaseSuccessPageTest() {

    @Test
    fun show_success_page() {
        runTest(source = InactivePhoneConstant.EXPEDITED) {
            checkSuccessPageIsDisplayed()
        }
    }

    @Test
    fun should_render_for_expedited() {
        runTest(source = InactivePhoneConstant.EXPEDITED) {
            isTitleForExpedited("Nomor HP berhasil diubah")
            isDescriptionForExpedited("Selamat, akunmu sudah menggunakan nomor HP baru.")
        }
    }

    @Test
    fun click_on_button_goto_home() {
        runTest(source = InactivePhoneConstant.EXPEDITED) {
            clickOnGotoHomeButton()
            checkTracker()
        }
    }
}