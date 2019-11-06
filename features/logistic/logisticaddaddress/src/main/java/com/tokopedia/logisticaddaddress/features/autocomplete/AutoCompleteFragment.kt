package com.tokopedia.logisticaddaddress.features.autocomplete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R

class AutoCompleteFragment : Fragment() {

    lateinit var searchInput: SearchInputView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_autocomplete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchInput = view.findViewById(R.id.search_input_autocomplete)

        val rvResults = view.findViewById<RecyclerView>(R.id.rv_autocomplete)
    }

    companion object {
        fun newInstance(): Fragment = AutoCompleteFragment().apply {
            arguments = Bundle()
        }
    }
}