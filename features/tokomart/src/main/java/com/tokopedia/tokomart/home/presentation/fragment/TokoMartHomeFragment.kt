package com.tokopedia.tokomart.home.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.home.di.component.DaggerTokoMartHomeComponent
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapter
import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeAdapterTypeFactory
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokomart.home.presentation.viewmodel.TokoMartHomeViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_tokomart_home.*
import javax.inject.Inject

class TokoMartHomeFragment: Fragment() {

    @Inject
    lateinit var viewModel: TokoMartHomeViewModel

    private val adapter by lazy { TokoMartHomeAdapter(TokoMartHomeAdapterTypeFactory(), TokoMartHomeListDiffer()) }

    companion object {
        fun newInstance() = TokoMartHomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tokomart_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeLiveData()
        getHomeLayout()
    }

    override fun onAttach(context: Context) {
        initInjector()
        super.onAttach(context)
    }

    private fun initInjector() {
        DaggerTokoMartHomeComponent.builder()
            .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun setupRecyclerView() {
        with(rvHome) {
            adapter = this@TokoMartHomeFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLiveData() {
        observe(viewModel.homeLayoutList) {
            if (it is Success) {
                loadHomeLayout(it.data)
            }
        }
    }

    private fun loadHomeLayout(data: HomeLayoutListUiModel) {
        data.run {
            if (isInitialLoad) {
                adapter.submitList(result)
                // TO-DO: Lazy Load Data
                viewModel.getLayoutData()
            } else {
                adapter.submitList(result)
            }
        }
    }

    private fun getHomeLayout() {
        viewModel.getHomeLayout()
    }
}