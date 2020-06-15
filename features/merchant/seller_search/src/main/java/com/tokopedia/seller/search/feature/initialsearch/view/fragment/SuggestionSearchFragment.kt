package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.SuggestionSearchViewModel

class SuggestionSearchFragment : Fragment() {

    companion object {
        fun newInstance() = SuggestionSearchFragment()
    }

    private lateinit var viewModel: SuggestionSearchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.suggestion_search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SuggestionSearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
