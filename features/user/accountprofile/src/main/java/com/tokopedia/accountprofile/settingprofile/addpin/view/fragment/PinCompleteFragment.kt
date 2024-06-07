package com.tokopedia.accountprofile.settingprofile.addpin.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.addpin.view.activity.PinCompleteActivity
import com.tokopedia.accountprofile.common.ColorUtils
import com.tokopedia.accountprofile.common.analytics.TrackingPinConstant.Screen.SCREEN_POPUP_PIN_SUCCESS
import com.tokopedia.accountprofile.common.analytics.TrackingPinUtil
import com.tokopedia.accountprofile.databinding.FragmentCompletePinBinding
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-11.
 * ade.hadian@tokopedia.com
 */

class PinCompleteFragment : BaseDaggerFragment() {

    private var _binding: FragmentCompletePinBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompletePinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.completeImage?.loadImageFitCenter(COMPLETE_PICT_URL)
        binding?.btnComplete?.setOnClickListener {
            trackingPinUtil.trackClickFinishButton()
            activity?.let {
                if (arguments?.getInt(ApplinkConstInternalGlobal.PARAM_SOURCE) == SOURCE_FORGOT_PIN_2FA) {
                    val intent = Intent().putExtras(requireArguments())
                    intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
                    it.setResult(Activity.RESULT_OK, intent)
                } else {
                    it.setResult(Activity.RESULT_OK)
                }
                it.finish()
            }
        }
        initViews()
    }

    private fun initViews() {
        context?.run {
            when (arguments?.getInt(ApplinkConstInternalGlobal.PARAM_SOURCE)) {
                SOURCE_CHANGE_PIN -> {
                    binding?.titleComplete?.text = getString(R.string.change_pin_success)
                    setToolbarTitle(getString(R.string.title_change_pin))
                }
                SOURCE_FORGOT_PIN, SOURCE_FORGOT_PIN_2FA -> {
                    binding?.titleComplete?.text = getString(R.string.change_pin_success)
                    setToolbarTitle(getString(R.string.change_pin_title_setting))
                }
            }
        }
    }

    private fun setToolbarTitle(title: String) {
        if (activity is PinCompleteActivity) {
            (activity as PinCompleteActivity).supportActionBar?.title = title
        }
    }

    override fun onStart() {
        super.onStart()
        trackingPinUtil.trackScreen(screenName)
    }

    override fun getScreenName(): String = SCREEN_POPUP_PIN_SUCCESS

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    fun onBackPressed() {
        trackingPinUtil.trackClickBackButtonSuccess()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val COMPLETE_PICT_URL =
            "https://images.tokopedia.net/android/user/success_update_pin.png"

        const val SOURCE_DEFAULT = 0
        const val SOURCE_ADD_PIN = 1
        const val SOURCE_CHANGE_PIN = 2
        const val SOURCE_FORGOT_PIN = 3
        const val SOURCE_FORGOT_PIN_2FA = 4

        fun createInstance(bundle: Bundle): PinCompleteFragment {
            val fragment = PinCompleteFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
