package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.databinding.SimpleTextViewCompatItemBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailImportantInfoFragment : Fragment() {

    lateinit var connector: Connector
    private var binding by autoClearedNullable<SimpleTextViewCompatItemBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SimpleTextViewCompatItemBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (::connector.isInitialized) {
            binding?.textView?.text = connector.getImportantInfo()
        }
        binding?.let {
            it.textView.setPadding(it.textView.paddingLeft, 16, it.textView.paddingRight, it.textView.paddingBottom)
        }
    }

    interface Connector {
        fun getImportantInfo(): String
    }
}