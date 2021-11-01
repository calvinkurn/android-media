package com.tokopedia.updateinactivephone.features.successpage.withpin

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.features.InactivePhoneWithPinTracker
import com.tokopedia.updateinactivephone.features.successpage.BaseInactivePhoneSuccessFragment
import com.tokopedia.utils.image.ImageUtils

open class InactivePhoneWithPinSuccessFragment : BaseInactivePhoneSuccessFragment() {

    private val tracker = InactivePhoneWithPinTracker()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.tickerInactivePhoneNumber?.hide()
        setImageHeader()
    }

    override fun onClickButtonGotoHome() {
        tracker.clickOnButtonHomeSuccessPage()
        gotoHome()
    }

    override fun onFragmentBackPressed(): Boolean {
        gotoHome()
        return false
    }

    private fun setImageHeader() {
        ImageUtils.clearImage(viewBinding?.imgHeader)
        viewBinding?.imgHeader?.setImageUrl(IMAGE_HEADER_URL)
    }

    override fun title(): String {
        return getString(R.string.expedited_title_success_page)
    }

    override fun description(): String {
        return getString(R.string.expedited_description_success_page)
    }

    companion object {
        private const val IMAGE_HEADER_URL = ""
        fun instance(bundle: Bundle): InactivePhoneWithPinSuccessFragment {
            return InactivePhoneWithPinSuccessFragment().apply {
                arguments = bundle
            }
        }
    }
}