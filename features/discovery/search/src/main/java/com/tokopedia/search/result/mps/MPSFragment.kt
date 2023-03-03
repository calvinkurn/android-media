package com.tokopedia.search.result.mps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.discovery.common.State
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.search.databinding.SearchMpsFragmentLayoutBinding
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressListener
import com.tokopedia.search.utils.BackToTop
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.mvvm.SearchView
import com.tokopedia.search.utils.mvvm.fragmentViewModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MPSFragment:
    TkpdBaseV4Fragment(),
    SearchView,
    ChooseAddressListener,
    FragmentProvider,
    BackToTop {

    var viewModelFactory: ViewModelProvider.Factory? = null
        @Inject set

    private val viewModel: MPSViewModel? by lazy(NONE) {
        viewModelFactory?.let {
            ViewModelProvider(this, it).get()
        }
    }

    private var binding by autoClearedNullable<SearchMpsFragmentLayoutBinding>()

    private val mpsTypeFactory = MPSTypeFactoryImpl(
        fragmentProvider = this,
        chooseAddressListener = this,
    )
    private val mpsListAdapter = MPSListAdapter(mpsTypeFactory)

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
        fun newInstance() = MPSFragment()
    }
}
