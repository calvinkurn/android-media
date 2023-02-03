package com.tokopedia.updateinactivephone.features.successpage.withpin

import com.tokopedia.imageassets.TokopediaImageUrl

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.successpage.BaseInactivePhoneSuccessFragment

open class InactivePhoneWithPinSuccessFragment : BaseInactivePhoneSuccessFragment() {

    private val tracker = InactivePhoneWithPinTracker()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.tickerInactivePhoneNumber?.hide()
        setImageHeader(IMAGE_HEADER_URL_EXPEDITED)
    }

    override fun onClickButtonGotoHome() {
        tracker.clickOnButtonHomeSuccessPage()
        gotoHome()
    }

    override fun onFragmentBackPressed(): Boolean {
        gotoHome()
        return false
    }

    override fun title(): String {
        return getString(R.string.expedited_title_success_page)
    }

    override fun description(): String {
        return getString(R.string.expedited_description_success_page)
    }

    companion object {
        private const val IMAGE_HEADER_URL_EXPEDITED = TokopediaImageUrl.IMAGE_HEADER_URL_EXPEDITED
        fun instance(bundle: Bundle): InactivePhoneWithPinSuccessFragment {
            return InactivePhoneWithPinSuccessFragment().apply {
                arguments = bundle
            }
        }
    }
}