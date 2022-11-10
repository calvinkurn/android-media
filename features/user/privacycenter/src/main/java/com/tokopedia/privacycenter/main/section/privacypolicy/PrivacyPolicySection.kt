package com.tokopedia.privacycenter.main.section.privacypolicy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.SectionPrivacyPolicyBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

class PrivacyPolicySection constructor(
    private val context: Context?
) : BasePrivacyCenterSection(context) {
    override val sectionViewBinding: ViewBinding = SectionPrivacyPolicyBinding.inflate(
        LayoutInflater.from(context)
    )
    override val sectionTextTitle: String? = context?.resources?.getString(R.string.section_title_privacypolicy)
    override val sectionTextDescription: String? = context?.resources?.getString(R.string.section_description_privacypolicy)
    override val isShowDirectionButton: Boolean = false

    override fun initObservers() {

    }

    override fun onViewRendered() {
        showShimmering(false)
    }

    override fun onButtonDirectionClick(view: View) {

    }

    companion object {
        const val TAG = "PrivacyPolicy"
    }

}
