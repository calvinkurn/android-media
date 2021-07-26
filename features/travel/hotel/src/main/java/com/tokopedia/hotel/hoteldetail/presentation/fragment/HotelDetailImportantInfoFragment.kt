package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.simple_text_view_compat_item.*

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailImportantInfoFragment : Fragment() {

    lateinit var connector: Connector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.simple_text_view_compat_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::connector.isInitialized) {
            text_view.text = connector.getImportantInfo()
        }
        text_view.setPadding(text_view.paddingLeft, 16, text_view.paddingRight, text_view.paddingBottom)
    }

    interface Connector {
        fun getImportantInfo(): String
    }
}