package com.tokopedia.updateinactivephone.features.successpage

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.checkSuccessPageIsDisplayed
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.clickOnHomeButton
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForEmailOnly
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isTickerDisplayed
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class SuccessPageGeneralTest : BaseSuccessPageTest() {

    @Test
    fun show_success_page() {
        // Given
        startSuccessPageActivity()

        // Then
        checkSuccessPageIsDisplayed()
    }

    @Test
    fun should_render_ticker() {
        // Given
        startSuccessPageActivity()

        // Then
        isTickerDisplayed()
    }

    @Test
    fun should_render_description_with_email_only() {
        // Given
        activity?.inactivePhoneUserDataModel = InactivePhoneUserDataModel(
            email = "rivaldy.firmansyah+100@tokopedia.com"
        )

        startSuccessPageActivity()

        // Then
        val text = String.format(
            "Datamu akan diproses maks. 1x24 jam ke depan. Informasi seputar pengajuan ini akan dikirim lewat SMS ke nomor %s",
            activity?.inactivePhoneUserDataModel?.email
        )

        isDescriptionForEmailOnly(text)
    }

    @Test
    fun should_render_description_with_email_and_phone_number() {
        // Given
        activity?.inactivePhoneUserDataModel = InactivePhoneUserDataModel(
            email = "rivaldy.firmansyah+100@tokopedia.com",
            newPhoneNumber = "084444123123"
        )

        startSuccessPageActivity()

        // Then
        val text = String.format(
            "Datamu akan diproses maks. 1x24 jam ke depan. Informasi seputar pengajuan ini akan dikirim lewat e-mail ke alamat %s dan SMS ke nomor %s",
            activity?.inactivePhoneUserDataModel?.email,
            activity?.inactivePhoneUserDataModel?.newPhoneNumber
        )

        isDescriptionForEmailOnly(text)
    }

    @Test
    fun should_click_on_button_goto_home() {
        // Given
        startSuccessPageActivity()

        // Then
        clickOnHomeButton()
    }
}