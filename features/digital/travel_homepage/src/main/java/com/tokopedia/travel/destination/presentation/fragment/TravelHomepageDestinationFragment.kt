package com.tokopedia.travel.destination.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.travel.homepage.R

/**
 * @author by jessica on 2019-12-20
 */

class TravelHomepageDestinationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_travel_homepage_destination, container, false)
        return view
    }
}