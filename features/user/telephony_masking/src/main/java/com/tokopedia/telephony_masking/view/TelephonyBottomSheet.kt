package com.tokopedia.telephony_masking.view

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.media.loader.loadImage
import com.tokopedia.telephony_masking.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

object TelephonyBottomSheet {

    private const val MAIN_IMAGE_URL =
            "https://images.tokopedia.net/img/android/user/telephony_allow_contact_access.png"
    private const val TAG = "TelephonyBottomSheet"

    fun show(
            activity: FragmentActivity,
            onGiveAccessButtonClick: (() -> Unit),
            onSkipButtonClick: (() -> Unit),
            onCloseButtonClick: (() -> Unit)
    ) {
        val bottomSheetUnify = BottomSheetUnify()
        val view = View.inflate(
                activity, R.layout.layout_telephony_bottomsheet, null
        )

        setupImage(view)
        setupPrivacyText(view)
        setupButton(view, bottomSheetUnify, onGiveAccessButtonClick, onSkipButtonClick, onCloseButtonClick)

        bottomSheetUnify.apply {
            showCloseIcon = true
            showHeader = true
            setChild(view)
        }

        bottomSheetUnify.show(activity.supportFragmentManager, TAG)
    }

    private fun setupImage(view: View) {
        val mainImage = view.findViewById<ImageUnify>(R.id.main_image_telephony)
        mainImage.loadImage(MAIN_IMAGE_URL)
    }

    private fun setupPrivacyText(view: View) {
        val privacy = view.findViewById<Typography>(R.id.privacy_telephony)
        privacy.setOnClickListener {
            //TODO: RouteManager.route()
        }
    }

    private fun setupButton(
            view: View,
            bottomSheetUnify: BottomSheetUnify,
            onGiveAccessButtonClick: (() -> Unit),
            onSkipButtonClick: (() -> Unit),
            onCloseButtonClick: (() -> Unit)
    ) {
        val saveButton = view.findViewById<UnifyButton>(R.id.telephony_save_button)
        val skipButton = view.findViewById<UnifyButton>(R.id.telephony_skip_button)
        saveButton?.setOnClickListener {
            onGiveAccessButtonClick()
        }
        skipButton?.setOnClickListener {
            onSkipButtonClick()
            bottomSheetUnify.dismiss()
        }

        bottomSheetUnify.setCloseClickListener {
            onCloseButtonClick()
        }
    }
}