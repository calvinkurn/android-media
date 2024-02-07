package com.tokopedia.privacycenter.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.privacycenter.R
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not

class PrivacyCenterRobot {
    fun scrollToBottom() {
        onView(withText("Butuh info lebih lanjut?"))
            .perform(nestedScrollTo())
    }

    fun scrollToFooterImage() {
        onView(withId(R.id.img_footer_privacy_center))
            .perform(nestedScrollTo())
    }

    fun clickRiwayatKebijakan() {
        onView(withText("Riwayat Pemberitahuan Privasi")).perform(click())
    }
}

class PrivacyResultRobot {
    fun shouldShowCorrectHeader(name: String) {
        onView(withId(R.id.text_name)).check(matches(withText(name)))
        onView(withId(R.id.text_desc)).check(matches(withText("Penggunaan datamu bisa diatur di sini")))
    }
    fun shouldShowRecommendationSectionSuccess() {
        onView(withText("Rekomendasi & promo")).check(matches(isDisplayed()))
        onView(withText("Pakai perangkatmu buat penawaran spesial")).check(matches(isDisplayed()))
        onView(withText("Shake Shake")).check(matches(isDisplayed()))
        onView(withText("Goyang HP untuk dapat promo menarik")).check(matches(isDisplayed()))
        onView(withText("Geolokasi")).check(matches(isDisplayed()))
        onView(withText("Atur rekomendasi berdasarkan lokasi")).check(matches(isDisplayed()))
        onView(withText("Rekomendasi Teman di Feed")).check(matches(isDisplayed()))
        onView(withText("Tampilkan teman yang bisa di-follow")).check(matches(isDisplayed()))
    }

    fun shouldShowRecommendationSectionFailed() {
        onView(withText("Rekomendasi & promo")).check(matches(isDisplayed()))
        onView(withText("Pakai perangkatmu buat penawaran spesial")).check(matches(isDisplayed()))
        onView(withText("Shake Shake")).check(matches(not(isDisplayed())))
        onView(withText("Goyang HP untuk dapat promo menarik")).check(matches(not(isDisplayed())))
        onView(withText("Geolokasi")).check(matches(not(isDisplayed())))
        onView(withText("Atur rekomendasi berdasarkan lokasi")).check(matches(not(isDisplayed())))
        onView(withText("Rekomendasi Teman di Feed")).check(matches(not(isDisplayed())))
        onView(withText("Tampilkan teman yang bisa di-follow")).check(matches(not(isDisplayed())))
    }

    fun shouldShowConsentWithdrawalSuccess() {
        onView(withText("Persetujuan kamu")).check(matches(isDisplayed()))
        onView(withText("Atur fitur apa aja yang boleh akses datamu")).check(matches(isDisplayed()))
        onView(withText("Testing DPPO Portal")).check(matches(isDisplayed()))
        onView(withText("Nanti akan ada fitur lain yang bisa kamu atur persetujuannya di sini.")).check(matches(isDisplayed()))
    }

    fun shouldShowConsentWithdrawalFailed() {
        onView(withText("Persetujuan kamu")).check(matches(isDisplayed()))
        onView(withText("Atur fitur apa aja yang boleh akses datamu")).check(matches(isDisplayed()))
    }

    fun shouldShowPrivacyPolicy() {
        onView(withText("Pemberitahuan Privasi Tokopedia")).check(matches(isDisplayed()))
        onView(withText("Pemberitahuan Privasi menjelaskan data pribadimu yang disimpan, bagaimana Tokopedia menggunakannya, dan kenapa Tokopedia memprosesnya.")).check(matches(isDisplayed()))
        onView(withText("Cek Pemberitahuan Privasi Tokopedia")).check(matches(isDisplayed()))
        onView(withText("Riwayat Pemberitahuan Privasi")).check(matches(isDisplayed()))
    }

    fun shouldShowFaq() {
        onView(withText("Punya pertanyaan seputar privasi?")).check(matches(isDisplayed()))
        onView(withText("Temukan semua jawabannya disini")).check(matches(isDisplayed()))
    }

    fun shouldShowTokopediaCare() {
        onView(withText("Butuh info lebih lanjut?")).check(matches(isDisplayed()))
        onView(withText("Hubungi Tokopedia Care")).check(matches(isDisplayed()))
    }

    fun shouldShowFooterImage() {
        onView(withId(R.id.img_footer_privacy_center)).check(matches(isDisplayed()))
    }

    fun shouldShowActivitySection() {
        onView(withText("Aktivitas")).check(matches(isDisplayed()))
        onView(withText("Atur data dari aktivitasmu di Tokopedia")).check(matches(isDisplayed()))
        onView(withText("Bagikan Wishlist")).check(matches(isDisplayed()))
        onView(withText("Riwayat Pencarian")).check(matches(isDisplayed()))
    }

    fun shouldDisplayPrivacyTestData() {
        onView(withText("Privacy test - 06 Apr 2021"))
            .check(matches(isDisplayed()))
    }

    fun shouldShowRecommendationShareFriend(isActivated: Boolean) {
        onView(allOf(withId(R.id.itemSwitch), isDescendantOfA(withId(R.id.item_recommendation_friend)))).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.itemSwitch), isDescendantOfA(withId(R.id.item_recommendation_friend)))).check(
            matches(
                if (isActivated) {
                    isChecked()
                } else {
                    isNotChecked()
                }
            )
        )
    }
}

fun privacyCenterRobot(func: PrivacyCenterRobot.() -> Unit): PrivacyCenterRobot {
    return PrivacyCenterRobot().apply(func)
}

infix fun PrivacyCenterRobot.assert(func: PrivacyResultRobot.() -> Unit): PrivacyResultRobot {
    return PrivacyResultRobot().apply(func)
}
