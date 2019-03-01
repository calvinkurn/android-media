package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.os.Bundle

/**
 * @author by milhamj on 01/03/19.
 */
class AffiliateCreatePostFragment : BaseCreatePostFragment() {
    companion object {
        fun createInstance(bundle: Bundle): AffiliateCreatePostFragment {
            val fragment = AffiliateCreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun fetchContentForm() {
        presenter.fetchContentForm(viewModel.adIdList)
    }
}