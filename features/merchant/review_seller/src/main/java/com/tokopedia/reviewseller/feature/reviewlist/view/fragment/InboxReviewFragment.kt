package com.tokopedia.reviewseller.feature.reviewlist.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.reviewseller.R

/**
 * A simple [Fragment] subclass.
 */
class InboxReviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox_review, container, false)
    }


}
