package com.tokopedia.search.result.mps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.Error
import com.tokopedia.discovery.common.State.Success
import com.tokopedia.iris.Iris
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.search.databinding.SearchMpsFragmentLayoutBinding
import com.tokopedia.search.result.SearchViewModel
import com.tokopedia.search.result.mps.addtocart.AddToCartView
import com.tokopedia.search.result.mps.chooseaddress.ChooseAddressListener
import com.tokopedia.search.result.mps.emptystate.EmptyStateListener
import com.tokopedia.search.result.mps.filter.bottomsheet.BottomSheetFilterView
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterView
import com.tokopedia.search.result.mps.shopwidget.MPSShopWidgetListenerDelegate
import com.tokopedia.search.utils.BackToTopView
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.mvvm.RefreshableView
import com.tokopedia.search.utils.mvvm.SearchView
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import kotlin.reflect.KProperty1
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener as EndlessScrollListener

class MPSFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val trackingQueue: TrackingQueue,
    private val iris: Iris,
):
    TkpdBaseV4Fragment(),
    SearchView,
    ChooseAddressListener,
    FragmentProvider,
    BackToTopView,
    ListListener,
    EmptyStateListener {

    private val viewModel: MPSViewModel? by viewModels { viewModelFactory }
    private val searchViewModel: SearchViewModel? by viewModels { viewModelFactory }
    private var binding: SearchMpsFragmentLayoutBinding? by autoClearedNullable()
    private val recycledViewPool = RecycledViewPool()
    private var mpsListAdapter: MPSListAdapter? = null
    private var endlessScrollListener: EndlessScrollListener? = null

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
        initRecyclerView()
        initQuickFilterView()
        initBottomSheetFilter()
        initAddToCartView()
        initSwipeRefreshLayout()
    }

    private fun initRecyclerView() {
        val context = context ?: return

        initAdapter()

        binding?.mpsRecyclerView?.apply {
            adapter = mpsListAdapter
            layoutManager = LinearLayoutManager(context)
            endlessScrollListener = endlessScrollListener(layoutManager).also(::addOnScrollListener)
        }
    }

    private fun initAdapter() {
        val mpsTypeFactory = MPSTypeFactoryImpl(
            recycledViewPool = recycledViewPool,
            fragmentProvider = this,
            chooseAddressListener = this,
            shopWidgetListener = MPSShopWidgetListenerDelegate(
                context,
                viewModel,
                trackingQueue,
                iris,
            ),
            emptyStateListener = this,
        )

        mpsListAdapter = MPSListAdapter(mpsTypeFactory, this)
    }

    private fun initSwipeRefreshLayout() {
        binding?.mpsSwipeRefreshLayout?.setOnRefreshListener {
            viewModel?.onViewReloadData()
        }
    }

    private fun endlessScrollListener(layoutManager: LayoutManager?): EndlessScrollListener =
        object: EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel?.onViewLoadMore()
            }
        }

    private fun initQuickFilterView() {
        QuickFilterView(viewModel, binding?.mpsSortFilter)
            .onStateRefresh(MPSState::quickFilterState)
    }

    private fun initBottomSheetFilter() {
        BottomSheetFilterView(viewModel, context, parentFragmentManager)
            .onStateRefresh(MPSState::bottomSheetFilterState)
    }

    private fun initAddToCartView() {
        AddToCartView(viewModel, context, view).onStateRefresh(MPSState::addToCartState)
    }

    private fun <P> RefreshableView<P>.onStateRefresh(prop: KProperty1<MPSState, P>) {
        viewModel?.onEach(prop, ::refresh)
    }

    override fun refresh() = withState(viewModel) { state ->
        binding?.mpsLoadingView?.showWithCondition(state.result is State.Loading)
        binding?.mpsSwipeRefreshLayout?.apply {
            isRefreshing = state.result is State.Loading
            showWithCondition(state.result is Success)
        }
        if (state.result is Error) showNetworkError(state.result)

        if (state.loadMoreThrowable != null) showNetworkErrorLoadMore(state.loadMoreThrowable)

        mpsListAdapter?.submitList(state.result.data)
    }

    private fun showNetworkError(result: Error<List<Visitable<*>>>) {
        val context = context ?: return
        val view = view ?: return
        val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)

        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            viewModel?.onViewReloadData()
        }
    }

    private fun showNetworkErrorLoadMore(throwable: Throwable) {
        val activity = activity ?: return
        val errorMessage = ErrorHandler.getErrorMessage(activity, throwable)

        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            viewModel?.onViewLoadMore()
        }.showRetrySnackbar()
    }

    override fun onPause() {
        super.onPause()

        trackingQueue.sendAll()
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

    override fun onCurrentListChanged(
        previousList: List<Visitable<*>>,
        currentList: List<Visitable<*>>,
    ) {
        endlessScrollListener?.updateStateAfterGetData()
    }

    override fun onEmptyButtonKeywordClicked() {
        searchViewModel?.showAutoCompleteView()
    }

    override fun onEmptyButtonFilterClicked() {
        viewModel?.resetFilter()
    }

    companion object {

        @JvmStatic
        internal fun newInstance(
            classLoader: ClassLoader,
            fragmentFactory: FragmentFactory,
        ): MPSFragment {
            return fragmentFactory.instantiate(
                classLoader,
                MPSFragment::class.java.name,
            ) as MPSFragment
        }
    }
}
