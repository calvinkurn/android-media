package com.tokopedia.travelhomepage.destination.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.travelhomepage.R

/**
 * @author by jessica on 2019-12-20
 */

class TravelDestinationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_travel_homepage_destination, container, false)
    }

    companion object {
        fun getInstance(): TravelDestinationFragment = TravelDestinationFragment()
    }
}