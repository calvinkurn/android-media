package com.tokopedia.brandlist.brandlist_search.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.brandlist.BrandlistInstance
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.di.BrandlistSearchModule
import com.tokopedia.brandlist.brandlist_search.di.DaggerBrandlistSearchComponent
import com.tokopedia.brandlist.brandlist_search.presentation.viewmodel.BrandlistSearchViewModel
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class BrandlistSearchFragment: BaseDaggerFragment(),
        HasComponent<BrandlistSearchComponent> {

    companion object {
        @JvmStatic
        fun createInstance(): Fragment {
            return BrandlistSearchFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    @Inject
    lateinit var viewModel: BrandlistSearchViewModel

    private var searchView: SearchInputView? = null
    private var statusBar: View? = null
    private var recyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_brandlist_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = view.findViewById(R.id.search_input_view)
        recyclerView = view.findViewById(R.id.rv_brandlist_search)
        initView(view)
        observeSearchResultData()
    }

    override fun getComponent(): BrandlistSearchComponent? {
        return activity?.run {
            DaggerBrandlistSearchComponent
                    .builder()
                    .brandlistSearchModule(BrandlistSearchModule())
                    .brandlistComponent(BrandlistInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onDestroy() {
        viewModel.brandlistSearchResponse.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    private fun initView(view: View) {
        statusBar = view.findViewById(R.id.statusbar)
        toolbar = view.findViewById(R.id.toolbar)
        configToolbar(view)
        initSearchView()
    }

    private fun configToolbar(view: View){
        toolbar?.setNavigationIcon(com.tokopedia.brandlist.R.drawable.brandlist_icon_arrow_black)
        activity?.let {
            (it as AppCompatActivity).let {
                it.setSupportActionBar(toolbar)
                it.supportActionBar?.setDisplayShowTitleEnabled(false)
                it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initSearchView() {
        searchView?.setDelayTextChanged(250)
        searchView?.setListener(object : SearchInputView.Listener {
            override fun onSearchSubmitted(text: String?) {
                searchView?.hideKeyboard()
            }

            override fun onSearchTextChanged(text: String?) {
                println(text)
                text?.let {
                    if (it.isNotEmpty()) {
                        val categoryId = 0
                        val offset = 0
                        val sortType = 1
                        val firstLetter = ""
                        val brandSize = 10
                        viewModel.searchBrand(categoryId, offset, it,
                                brandSize, sortType, firstLetter)
                    }
                }
            }

        })
    }

    private fun observeSearchResultData() {
        viewModel.brandlistSearchResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    println(it.data.officialStoreAllBrands)
                    val response = it.data.officialStoreAllBrands
                    println(response)
                }
                is Fail -> {
                    println("Fail")
                }
            }
        })
    }

}