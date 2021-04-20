package com.tokopedia.profilecompletion.addpin.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.view.activity.PinCompleteActivity
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant.Screen.SCREEN_POPUP_PIN_SUCCESS
import com.tokopedia.profilecompletion.common.analytics.TrackingPinUtil
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import kotlinx.android.synthetic.main.fragment_complete_pin.*
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-11.
 * ade.hadian@tokopedia.com
 */

class PinCompleteFragment : BaseDaggerFragment() {

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_complete_pin, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ImageHandler.LoadImage(completeImage, COMPLETE_PICT_URL)
        btnComplete.setOnClickListener {
            trackingPinUtil.trackClickFinishButton()
            activity?.let {
                if (arguments?.getInt(ApplinkConstInternalGlobal.PARAM_SOURCE) == SOURCE_FORGOT_PIN_2FA) {
                    val intent = Intent().putExtras(arguments!!)
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
        when (arguments?.getInt(ApplinkConstInternalGlobal.PARAM_SOURCE)) {
            SOURCE_CHANGE_PIN -> {
                titleComplete.text = getString(R.string.change_pin_success)
                setToolbarTitle(resources.getString(R.string.title_change_pin))
            }
            SOURCE_FORGOT_PIN or SOURCE_FORGOT_PIN_2FA -> {
                titleComplete.text = getString(R.string.change_pin_success)
                setToolbarTitle(resources.getString(R.string.change_pin_title_setting))
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

    companion object {
        const val COMPLETE_PICT_URL = "https://ecs7.tokopedia.net/android/user/success_update_pin.png"

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