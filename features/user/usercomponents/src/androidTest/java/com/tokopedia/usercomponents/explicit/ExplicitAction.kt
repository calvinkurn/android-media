package com.tokopedia.usercomponents.explicit

import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.common.clickOnButton
import com.tokopedia.usercomponents.common.isDisplayed
import com.tokopedia.usercomponents.common.isNotDisplayed
import com.tokopedia.usercomponents.common.isTextDisplayed

fun initQuestionDisplayed() {
    isDisplayed(
        R.id.img_background,
        R.id.img_icon,
        R.id.img_dismiss,
        R.id.txt_title,
        R.id.txt_description,
        R.id.btn_positive_action,
        R.id.btn_negative_action
    )
    isTextDisplayed(
        "Kamu hanya konsumsi kuliner halal?",
        "Kami bisa kasih rekomendasi buat kamu!",
        "Iya, Benar",
        "Nggak"
    )
}

fun isFailedDisplayed() {
    isDisplayed(R.id.container_local_load)
    isTextDisplayed(
        "Konten gagal ditampilkan",
        "Klik ikon di samping buat coba lagi."
    )
}

fun isSuccessDisplayed() {
    isDisplayed(
        R.id.img_success_background,
        R.id.img_success_icon,
        R.id.img_success_dismiss,
        R.id.txt_success_title
    )
}

fun isHideWidget() {
    isNotDisplayed(
        R.id.img_background,
        R.id.img_icon,
        R.id.img_dismiss,
        R.id.txt_title,
        R.id.txt_description,
        R.id.btn_positive_action,
        R.id.btn_negative_action,
        R.id.img_success_background,
        R.id.img_success_icon,
        R.id.img_success_dismiss,
        R.id.txt_success_title
    )
}

fun clickButtonAnswer(isPositive: Boolean) {
    clickOnButton(
        if (isPositive) {
            R.id.btn_positive_action
        } else {
            R.id.btn_negative_action
        }
    )
}

//if onQuestionPage = false then click dismiss on success page
fun clickOnDismiss(onQuestionPage: Boolean) {
    clickOnButton(
        if (onQuestionPage) {
            R.id.img_dismiss
        } else {
            R.id.img_success_dismiss
        }
    )
}