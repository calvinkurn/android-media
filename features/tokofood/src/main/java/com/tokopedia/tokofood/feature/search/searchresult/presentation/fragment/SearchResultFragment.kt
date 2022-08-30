package com.tokopedia.tokofood.feature.search.searchresult.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokofood.databinding.FragmentSearchResultBinding
import com.tokopedia.tokofood.feature.search.container.presentation.listener.SearchResultViewUpdateListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SearchResultFragment: BaseDaggerFragment() {

    private var binding by autoClearedNullable<FragmentSearchResultBinding>()
    private var searchResultViewUpdateListener: SearchResultViewUpdateListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {

    }

    fun setSearchResultViewUpdateListener(searchResultViewUpdateListener: SearchResultViewUpdateListener) {
        this.searchResultViewUpdateListener = searchResultViewUpdateListener
    }

    fun showSearchResultState(keyword: String) {
        this.searchResultViewUpdateListener?.showSearchResultView()
    }
}