package com.tokopedia.updateinactivephone.features.successpage.regular

import com.tokopedia.imageassets.ImageUrl

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.utils.getValidEmail
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.successpage.BaseInactivePhoneSuccessFragment

open class InactivePhoneRegularSuccessFragment : BaseInactivePhoneSuccessFragment() {

    private val tracker = InactivePhoneTracker()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.tickerInactivePhoneNumber?.apply {
            setHtmlDescription(getString(R.string.text_success_ticker))
        }?.show()

        setImageHeader(IMAGE_HEADER_URL_REGULAR)
    }

    override fun onFragmentBackPressed(): Boolean {
        gotoHome()
        return false
    }

    override fun onClickButtonGotoHome() {
        tracker.clickOnButtonGotoHome()
        gotoHome()
    }

    override fun title(): String {
        return getString(R.string.text_success_title)
    }

    override fun description(): String {
        return if (
            !inactivePhoneUserDataModel?.email?.getValidEmail().isNullOrEmpty()
            && inactivePhoneUserDataModel?.newPhoneNumber?.isNotEmpty() == true
        ) {
            String.format(
                getString(R.string.text_success_description_single_account),
                inactivePhoneUserDataModel?.email?.getValidEmail(),
                inactivePhoneUserDataModel?.newPhoneNumber
            )
        } else {
            String.format(getString(
                R.string.text_success_description_has_no_email),
                inactivePhoneUserDataModel?.newPhoneNumber
            )
        }
    }

    companion object {
        private const val IMAGE_HEADER_URL_REGULAR = ImageUrl.IMAGE_HEADER_URL_REGULAR

        fun instance(bundle: Bundle): InactivePhoneRegularSuccessFragment {
            return InactivePhoneRegularSuccessFragment().apply {
                arguments = bundle
            }
        }
    }
}