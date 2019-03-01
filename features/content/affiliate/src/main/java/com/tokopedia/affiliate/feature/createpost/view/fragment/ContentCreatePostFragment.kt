package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.os.Bundle

/**
 * @author by milhamj on 01/03/19.
 */
class ContentCreatePostFragment: BaseCreatePostFragment() {
    companion object {
        fun createInstance(bundle: Bundle): ContentCreatePostFragment {
            val fragment = ContentCreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun fetchContentForm() {
        presenter.fetchContentForm(viewModel.productIdList)
    }
}