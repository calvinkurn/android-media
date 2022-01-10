package com.tokopedia.updateinactivephone.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.databinding.FragmentInactivePhoneOnboardingBinding
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class BaseInactivePhoneOnboardingFragment : BaseDaggerFragment() {

    var viewBinding by autoClearedNullable<FragmentInactivePhoneOnboardingBinding>()
    var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    val trackerRegular = InactivePhoneTracker()
    val trackerWithPin = InactivePhoneWithPinTracker()

    protected abstract fun onButtonNextClicked()

    override fun getScreenName(): String = ""
    override fun initInjector() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            inactivePhoneUserDataModel = it.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentInactivePhoneOnboardingBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    open fun initView() {
        updateImageHeader(IMAGE_HEADER)

        viewBinding?.buttonNext?.setOnClickListener {
            onButtonNextClicked()
        }
    }

    fun updateImageHeader(url: String) {
        viewBinding?.imgHeader?.setImageUrl(url, isSkipCache = true)
    }

    fun updateTitle(title: String) {
        viewBinding?.textTitle?.text = title
    }

    fun updateDescription(description: String) {
        viewBinding?.textDescription?.text = description
    }

    companion object {
        const val IMAGE_HEADER = "https://images.tokopedia.net/img/android/user/inactive-phone/inactivephone_onboarding.png"
    }
}