package com.tokopedia.search.result.mps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.discovery.common.State
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.search.databinding.SearchMpsFragmentLayoutBinding
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterView
import com.tokopedia.search.result.presentation.view.activity.SearchComponent
import com.tokopedia.search.utils.BackToTopView
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.mvvm.SearchView
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MPSFragment:
    TkpdBaseV4Fragment(),
    SearchView,
    ChooseAddressListener,
    FragmentProvider,
    BackToTopView {

    @Inject
    @Suppress("LateinitUsage")
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MPSViewModel? by viewModels { viewModelFactory }
    private var binding: SearchMpsFragmentLayoutBinding? by autoClearedNullable()
    private val recycledViewPool = RecycledViewPool()
    private val mpsTypeFactory = MPSTypeFactoryImpl(
        recycledViewPool = recycledViewPool,
        fragmentProvider = this,
        chooseAddressListener = this,
    )
    private val mpsListAdapter = MPSListAdapter(mpsTypeFactory)
    private val quickFilterView by lazy(NONE) { QuickFilterView(viewModel) }

    override fun getScreenName(): String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchMpsFragmentLayoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel?.observeState()
        viewModel?.onViewCreated()
    }

    private fun initViews() {
        val context = context ?: return
        binding?.mpsRecyclerView?.adapter = mpsListAdapter
        binding?.mpsRecyclerView?.layoutManager = LinearLayoutManager(context)
    }

    override fun refresh() = withState(viewModel) {
        binding?.mpsLoadingView?.showWithCondition(it.result is State.Loading)
        binding?.mpsSwipeRefreshLayout?.showWithCondition(it.result is State.Success)

        mpsListAdapter.submitList(it.result.data)
        quickFilterView.refreshQuickFilter(binding?.mpsSortFilter, it)
    }

    override fun onResume() {
        super.onResume()

        viewModel?.onViewResumed()
    }

    override fun backToTop() {
        binding?.mpsRecyclerView?.smoothScrollToPosition(0)
    }

    override fun getFragment(): Fragment = this

    override fun onLocalizingAddressSelected() {
        viewModel?.onLocalizingAddressSelected()
    }

    companion object {

        internal fun newInstance(searchComponent: SearchComponent?) = MPSFragment().apply {
            searchComponent?.inject(this)
        }
    }
}
