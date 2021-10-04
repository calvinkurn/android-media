package com.tokopedia.updateinactivephone.features.submitnewphone.regular

import com.tokopedia.updateinactivephone.common.viewaction.simulateOnBackPressed
import com.tokopedia.updateinactivephone.features.submitnewphone.BaseSubmitDataTest
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkSubmitDataPageDisplayed
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkThumbnailImageIdCard
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.checkThumbnailImageSelfie
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.clickOnButtonSubmit
import com.tokopedia.updateinactivephone.features.submitnewphone.SubmitDataViewAction.setPhoneNumberText
import org.junit.Test

class RegularSubmitDataGeneralTest : BaseSubmitDataTest() {

    var phone = "084444123456"

    override fun before() {
        super.before()
        inactivePhoneDependency.apply {
            submitDataUseCaseStub.response = inactivePhoneSubmitDataModel
        }
    }

    @Test
    fun show_submit_data_regular_page() {
        runTest {
            checkSubmitDataPageDisplayed()
        }
    }

    @Test
    fun input_valid_phone_number() {
        runTest {
            setPhoneNumberText(phone)
        }
    }

    @Test
    fun submit_new_phone() {
        runTest {
            setPhoneNumberText(phone)
            clickOnButtonSubmit()
            checkTracker()
        }
    }

    @Test
    fun open_popup() {
        runTest {
            simulateOnBackPressed()
            SubmitDataViewAction.checkPopupIsDisplayed()
            checkTracker()
        }
    }

    @Test
    fun on_click_exit_popup() {
        runTest {
            simulateOnBackPressed()
            SubmitDataViewAction.clickOnButtonExitPopup()
            checkTracker()
        }
    }

    @Test
    fun on_click_lanjut_verifikasi() {
        runTest {
            simulateOnBackPressed()
            SubmitDataViewAction.clickOnButtonLanjutVerifikasi()
            checkTracker()
        }
    }

    @Test
    fun check_thumbnail_id_card() {
        runTest {
            checkThumbnailImageIdCard()
        }
    }

    @Test
    fun check_thumbnail_selfie() {
        runTest {
            checkThumbnailImageSelfie()
        }
    }
}