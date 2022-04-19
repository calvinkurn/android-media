package com.tokopedia.updateinactivephone.features.onboarding.regular

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BulletSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_2
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.features.imagepicker.InactivePhoneImagePickerActivity
import com.tokopedia.updateinactivephone.features.onboarding.BaseInactivePhoneOnboardingFragment
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import kotlin.math.roundToInt

class InactivePhoneCaptureIdCardFragment : BaseInactivePhoneOnboardingFragment() {

    private val permissionCheckerHelper = PermissionCheckerHelper()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    override fun initView() {
        super.initView()

        updateImageHeader(IMAGE_ID_CARD_SAMPLE)
        updateTitle(getString(R.string.text_title_id_card))

        viewBinding?.textDescription?.hide()
        viewBinding?.layoutDescription?.apply {
            addView(addText(getString(R.string.text_onboarding_id_card_description_title)))
            addView(addTextWithBullet(getString(R.string.text_onboarding_id_card_description_1)))
            addView(addTextWithBullet(getString(R.string.text_onboarding_id_card_description_2)))
            addView(addTextWithBullet(getString(R.string.text_onboarding_id_card_description_3)))
        }?.show()
    }

    override fun onButtonNextClicked() {
        trackerRegular.clickOnNextButtonIdCardOnboarding()

        val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
        startActivityForResult(intent, REQUEST_CAPTURE_ID_CARD)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                permissionCheckerHelper.request(it, arrayOf(
                    PermissionCheckerHelper.Companion.PERMISSION_CAMERA,
                    PermissionCheckerHelper.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
                ), granted = {
                }, denied = {
                    it.finish()
                })
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper.onRequestPermissionsResult(it, requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CAPTURE_ID_CARD && resultCode == Activity.RESULT_OK) {
            context?.let {
                (it as InactivePhoneRegularActivity).gotoOnboardingSelfie()
            }
        }
    }

    private fun addText(text: String): Typography? {
        return context?.let {
            Typography(it).apply {
                this.setType(BODY_2)
                this.text = text
                this.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    private fun addTextWithBullet(text: String): Typography? {
        return context?.let {
            Typography(it).apply {
                val radius = DP_4.pxToDp(resources.displayMetrics)
                val gapWidth = DP_12.pxToDp(resources.displayMetrics)
                val margin = DP_8.pxToDp(resources.displayMetrics)
                val span = SpannableString(text)
                val color = MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N100)

                val bulletSpan: BulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    BulletSpan(gapWidth, color, radius)
                } else {
                    BulletSpan(gapWidth, color)
                }

                span.setSpan(bulletSpan, 0, text.length, 0)

                setMargins(this, 0, 0, 0, margin)
                this.setType(BODY_2)
                this.text = span
                this.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    companion object {
        private const val DP_4 = 4
        private const val DP_8 = 8
        private const val DP_12 = 12
        private const val IMAGE_ID_CARD_SAMPLE = "https://images.tokopedia.net/img/android/user/inactive-phone/inactivephone_ktp.png"
    }
}