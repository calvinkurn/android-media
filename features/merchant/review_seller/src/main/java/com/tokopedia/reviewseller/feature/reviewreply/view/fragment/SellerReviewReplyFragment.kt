package com.tokopedia.reviewseller.feature.reviewreply.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.reviewseller.R

class SellerReviewReplyFragment : Fragment() {

    companion object {
        fun newInstance() = SellerReviewReplyFragment()
    }

    private lateinit var viewModel: SellerReviewReplyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.seller_review_reply_fragment, container, false)
    }

}
