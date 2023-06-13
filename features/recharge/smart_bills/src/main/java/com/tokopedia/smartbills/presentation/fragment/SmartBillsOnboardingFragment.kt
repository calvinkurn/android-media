package com.tokopedia.smartbills.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.databinding.FragmentSmartBillsOnboardingBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SmartBillsOnboardingFragment : BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentSmartBillsOnboardingBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSmartBillsOnboardingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            val bulletSpan: BulletSpan
            val gapWidth = dpToPx(activity, BULLET_GAP_WIDTH_PX)
            val radius = dpToPx(activity, BULLET_RADIUS_PX)
            val bulletColor = ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_N700)
            bulletSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                BulletSpan(gapWidth, bulletColor, radius)
            } else {
                BulletSpan(gapWidth, bulletColor)
            }

            val firstDesc = SpannableString(getString(R.string.smart_bills_onboarding_desc_1))
            firstDesc.setSpan(bulletSpan, 0, firstDesc.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.smartBillsOnboardingDesc1?.text = firstDesc

            val secondDesc = SpannableString(getString(R.string.smart_bills_onboarding_desc_2))
            secondDesc.setSpan(bulletSpan, 0, firstDesc.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.smartBillsOnboardingDesc2?.text = secondDesc

            // Request login from user
            binding?.smartBillsOnboardingButton?.setOnClickListener {
                val intent = RouteManager.getIntent(activity, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_CODE_LOGIN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // If user has logged in, redirect to smart bills page
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LOGIN) {
            context?.let {
                RouteManager.route(it, ApplinkConsInternalDigital.SMART_BILLS)
            }
        }
    }

    private fun dpToPx(context: Context, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 1010

        const val BULLET_GAP_WIDTH_PX = 12f
        const val BULLET_RADIUS_PX = 4f
    }
}
