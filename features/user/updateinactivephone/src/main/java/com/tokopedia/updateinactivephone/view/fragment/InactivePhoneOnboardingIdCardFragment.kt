package com.tokopedia.updateinactivephone.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BulletSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BODY_2
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant.REQUEST_CAPTURE_ID_CARD
import com.tokopedia.updateinactivephone.common.cameraview.CameraViewMode
import com.tokopedia.updateinactivephone.view.InactivePhoneTracker
import com.tokopedia.updateinactivephone.view.activity.InactivePhoneImagePickerActivity
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.utils.permission.request
import kotlinx.android.synthetic.main.fragment_inactive_phone_onboarding_id_card.*
import kotlin.math.roundToInt

class InactivePhoneOnboardingIdCardFragment : BaseDaggerFragment() {

    lateinit var tracker: InactivePhoneTracker

    private lateinit var fragmentTransactionInterface: FragmentTransactionInterface
    private val permissionCheckerHelper = PermissionCheckerHelper()

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding_id_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentTransactionInterface = activity as FragmentTransactionInterface
        tracker = InactivePhoneTracker()

        checkPermission()

        btnNext?.setOnClickListener {
            tracker.clickOnNextButtonIdCardOnboarding()

            val intent = InactivePhoneImagePickerActivity.createIntentCamera(context, CameraViewMode.ID_CARD)
            startActivityForResult(intent, REQUEST_CAPTURE_ID_CARD)
        }

        ImageUtils.loadImage(imgHeader, IMAGE_ID_CARD_SAMPLE)

        addText(getString(R.string.text_onboarding_id_card_description_title))
        addTextWithBullet(getString(R.string.text_onboarding_id_card_description_1))
        addTextWithBullet(getString(R.string.text_onboarding_id_card_description_2))
        addTextWithBullet(getString(R.string.text_onboarding_id_card_description_3))
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
            gotoOnboardingSelfie()
        }
    }

    private fun gotoOnboardingSelfie() {
        fragmentTransactionInterface.replace(InactivePhoneOnboardingSelfieFragment())
    }

    private fun addText(text: String) {
        context?.let {
            val tv = Typography(it)
            tv?.let { view ->
                view.setType(BODY_2)
                view.text = text
                view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            layoutDescription?.addView(tv)
        }
    }

    private fun addTextWithBullet(text: String) {
        context?.let {
            val radius = dpToPx(4)
            val gapWidth = dpToPx(12)
            val color = MethodChecker.getColor(it, com.tokopedia.unifyprinciples.R.color.dark_N600)
            val margin = dpToPx(8)
            val bulletSpan: BulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                BulletSpan(gapWidth, color, radius)
            } else {
                BulletSpan(gapWidth, color)
            }

            val span = SpannableString(text)
            span.setSpan(bulletSpan, 0, text.length, 0)

            val tv = Typography(it)
            tv?.let { view ->
                setMargins(view, 0, 0, 0, margin)
                view.setType(BODY_2)
                view.text = span
                view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            layoutDescription?.addView(tv)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    companion object {
        const val IMAGE_ID_CARD_SAMPLE = "https://ecs7.tokopedia.net/android/others/account_verification_ktp_onboarding.png"
    }
}