package com.tokopedia.similarsearch

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment

internal class SimilarSearchFragment: TkpdBaseV4Fragment() {

    companion object {

        fun getInstance(): SimilarSearchFragment {
            return SimilarSearchFragment()
        }
    }

    override fun getScreenName(): String {
        return "/searchproduct - product"
    }
}