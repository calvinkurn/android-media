package com.tokopedia.updateinactivephone.features.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker

abstract class BaseInactivePhoneOnboardingFragment : BaseDaggerFragment() {

    val tracker = InactivePhoneTracker()

    private var textTitle: Typography? = null
    private var textDescription: Typography? = null
    private var buttonNext: UnifyButton? = null

    protected abstract fun onButtonNextClicked()

    override fun getScreenName(): String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_inactive_phone_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        textTitle = view?.findViewById(R.id.txtTitle)
        textDescription = view?.findViewById(R.id.textDescription)
        buttonNext = view?.findViewById(R.id.btnNext)
        buttonNext?.setOnClickListener {
            onButtonNextClicked()
        }
    }

    fun updateTitle(title: String) {
        textTitle?.text = title
    }

    fun updateDescription(description: String) {
        textDescription?.text = description
    }

}