package com.tokopedia.updateinactivephone.features.successpage.regular

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.successpage.BaseSuccessPageTest
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.checkSuccessPageIsDisplayed
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.checkTickerContent
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.clickOnGotoHomeButton
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForEmailAndPhoneNumber
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isDescriptionForEmailOnly
import com.tokopedia.updateinactivephone.features.successpage.SuccessPageViewAction.isTickerDisplayed
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class RegularSuccessPageGeneralTest : BaseSuccessPageTest() {

    @Test
    fun basic_test() {
        runTest(source = "") {
            Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    null
                )
            )

            checkSuccessPageIsDisplayed()
            isTickerDisplayed()
            checkTickerContent("Jangan pakai nomor HP ini di akun Tokopedia lainnya, biar nomor HP-mu berhasil diubah.")

            clickOnGotoHomeButton()
            intended(hasData(ApplinkConst.HOME))
        }
    }

    @Test
    fun should_render_description_with_out_email() {
        // Given
        fakeInactivePhoneUserDataModel = InactivePhoneUserDataModel(
            newPhoneNumber = "084444123123"
        )

        val text = String.format(
            "Datamu akan diproses maks. 1x12 jam ke depan. Informasi seputar pengajuan ini akan dikirim lewat SMS ke nomor %s.",
            fakeInactivePhoneUserDataModel.newPhoneNumber
        )

        // Then
        runTest(source = "") {
            isDescriptionForEmailOnly(text)
        }
    }

    @Test
    fun should_render_description_with_email_and_phone_number() {
        // Given
        fakeInactivePhoneUserDataModel = InactivePhoneUserDataModel(
            email = "rivaldy.firmansyah@tokopedia.com",
            newPhoneNumber = "084444123123"
        )

        val text = String.format(
            "Datamu akan diproses maks. 1x12 jam ke depan. Informasi seputar pengajuan ini akan dikirim lewat e-mail ke alamat %s dan SMS ke nomor %s.",
            fakeInactivePhoneUserDataModel.email,
            fakeInactivePhoneUserDataModel.newPhoneNumber
        )

        // Then
        runTest(source = "") {
            isDescriptionForEmailAndPhoneNumber(text)
        }
    }
}
