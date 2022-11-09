package com.tokopedia.privacycenter.main.section.dummy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.tokopedia.privacycenter.databinding.SectionConsentwithdrawalBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

/**
 * Will remove later, just for testing purpose
 */
class DummySection(context: Context?) : BasePrivacyCenterSection(context) {

    override val sectionViewBinding: ViewBinding = SectionConsentwithdrawalBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String = "Dummy Section"
    override val sectionTextDescription: String = "Lorem ipsum dolor sit amet"
    override val isShowDirectionButton: Boolean = true

    override fun initObservers() {

    }

    override fun onViewRendered() {
        showShimmering(false)
    }

    override fun onButtonDirectionClick(view: View) {

    }

    companion object {
        const val TAG = "DummySection"
    }
}
