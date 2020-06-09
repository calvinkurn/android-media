package com.tokopedia.seller.search.feature.initialsearch.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.feature.initialsearch.view.viewmodel.InitialSearchViewModel

class InitialSearchFragment : BaseDaggerFragment() {

    private var viewModel: InitialSearchViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.initial_search_fragment, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InitialSearchViewModel::class.java)
    }

}
