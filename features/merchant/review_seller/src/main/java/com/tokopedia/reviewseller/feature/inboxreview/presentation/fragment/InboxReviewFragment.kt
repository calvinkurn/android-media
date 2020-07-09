package com.tokopedia.reviewseller.feature.inboxreview.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.reviewseller.R

class InboxReviewFragment: BaseDaggerFragment() {

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun initInjector() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_review, container, false)
    }
}