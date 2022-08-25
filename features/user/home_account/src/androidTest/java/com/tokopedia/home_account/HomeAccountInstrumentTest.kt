package com.tokopedia.home_account

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.home_account.base.HomeAccountTest
import com.tokopedia.home_account.utils.QueryUtils
import com.tokopedia.home_account.utils.ViewUtils
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Test

@CassavaTest
class HomeAccountInstrumentTest : HomeAccountTest() {

    //1.Cassava Test ID - 759
    @Test
    fun click_profile() {
        runTest {
            fragment.onProfileClicked()
        }.validate(
                QueryUtils.queryProfile()
        )
    }

    //2.Cassava Test ID - 796
    @Test
    fun click_ovo_wallet() {
        runTest {
            ViewUtils.clickWalletViewHolder("OVO")
        }.validate(QueryUtils.queryOvo())
    }

    //3.Cassava Test ID - 797
    @Test
    fun click_saldo() {
        runTest {
            ViewUtils.clickWalletViewHolder("Saldo Tokopedia")
        }.validate(QueryUtils.querySaldo())
    }


    //4.Cassava Test ID - 798
    // This test is disabled for a while, because it's failed on the cassava nightly build, will release the patch ASAP.
    /*
    @Test
    fun click_more_account_settings() {
        runTest {
            Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
            onView(withId(R.id.home_account_member_layout_member_forward)).check(matches(isDisplayed())).perform(click())
        }.validate(QueryUtils.queryMoreSettings("Member"))
    }
    */

    //5.Cassava Test ID - 802
    @Test
    fun click_list_address() {
        runTest {
            ViewUtils.clickSettingView("Pengaturan Akun", AccountConstants.Analytics.Label.LABEL_LIST_ADDRESS)
        }.validate(QueryUtils.queryAccountSettings(AccountConstants.Analytics.Label.LABEL_LIST_ADDRESS))
    }

    //6.Cassava Test ID - 803
    @Test
    fun click_bank_account() {
        runTest {
            onView(withId(R.id.home_account_user_fragment_rv)).perform(ViewActions.swipeUp())
            ViewUtils.clickSettingView("Pengaturan Akun", AccountConstants.Analytics.Label.LABEL_BANK_ACCOUNT)
        }.validate(QueryUtils.queryAccountSettings(AccountConstants.Analytics.Label.LABEL_BANK_ACCOUNT))
    }

    //7. Cassava Test ID - 804
    @Test
    fun click_instant_payment() {
        runTest {
            onView(withId(R.id.home_account_user_fragment_rv)).perform(ViewActions.swipeUp())
            ViewUtils.clickSettingView("Pengaturan Akun", AccountConstants.Analytics.Label.LABEL_INSTANT_PAYMENT)
        }.validate(QueryUtils.queryAccountSettings(AccountConstants.Analytics.Label.LABEL_INSTANT_PAYMENT))
    }

    //9. Cassava Test ID - 806
    @Test
    fun click_account_security() {
        runTest {
            onView(withId(R.id.home_account_user_fragment_rv)).perform(ViewActions.swipeUp())
            ViewUtils.clickSettingView("Pengaturan Akun", AccountConstants.Analytics.Label.LABEL_ACCOUNT_SECURITY)
        }.validate(QueryUtils.queryAccountSettings(AccountConstants.Analytics.Label.LABEL_ACCOUNT_SECURITY))
    }

    //10. Cassava Test ID - 807
    @Test
    fun click_notification() {
        runTest {
            onView(withId(R.id.home_account_user_fragment_rv)).perform(ViewActions.swipeUp())
            ViewUtils.clickSettingView("Pengaturan Akun", AccountConstants.Analytics.Label.LABEL_NOTIFICATION)
        }.validate(QueryUtils.queryAccountSettings(AccountConstants.Analytics.Label.LABEL_NOTIFICATION))
    }

    //11. Cassava Test ID - 808
    @Test
    fun click_more_application_setting() {
        runTest {
            ViewUtils.clickSettingMoreView("Pengaturan Aplikasi")
        }.validate(QueryUtils.queryMoreSettings(AccountConstants.Analytics.Label.LABEL_APP_SETTING))
    }

    //12. Cassava Test ID - 809
    @Test
    fun toogle_shake_switch() {
        runTest {
            ViewUtils.clickSettingMoreView("Pengaturan Aplikasi")

            ViewUtils.clickSwitchOnApplicationSetting("Shake Shake")
        }.validate(listOf(QueryUtils.queryShakeCampaign(false), QueryUtils.queryShakeCampaign(true)))
    }
}