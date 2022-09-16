package com.tokopedia.autocompletecomponent.universal.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchFragmentLayoutBinding
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_SCREEN_NAME
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchContextModule
import com.tokopedia.autocompletecomponent.universal.di.UniversalSearchSearchParameterModule
import com.tokopedia.autocompletecomponent.universal.presentation.adapter.UniversalSearchAdapter
import com.tokopedia.autocompletecomponent.universal.presentation.itemdecoration.UniversalSearchItemDecoration
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactoryImpl
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModel
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselListenerDelegate
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineListenerDelegate
import com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate.ErrorStateListenerDelegate
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridListenerDelegate
import com.tokopedia.autocompletecomponent.universal.presentation.widget.related.RelatedItemListenerDelegate
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

internal class UniversalSearchFragment: BaseDaggerFragment(), HasComponent<BaseAppComponent> {

    companion object {
        const val UNIVERSAL_SEARCH_FRAGMENT_TAG = "UNIVERSAL_SEARCH_FRAGMENT"
        private const val EXTRA_SEARCH_PARAMETER = "EXTRA_SEARCH_PARAMETER"

        fun newInstance(searchParameter: SearchParameter?): UniversalSearchFragment {
            val args = Bundle().apply {
                putParcelable(EXTRA_SEARCH_PARAMETER, searchParameter)
            }

            return UniversalSearchFragment().apply {
                arguments = args
            }
        }
    }

    private var universalSearchAdapter: UniversalSearchAdapter? = null

    private lateinit var universalSearchViewModel: UniversalSearchViewModel

    @Inject
    internal lateinit var carouselListenerDelegate: CarouselListenerDelegate

    @Inject
    internal lateinit var doubleLineListenerDelegate: DoubleLineListenerDelegate

    @Inject
    internal lateinit var listGridListenerDelegate: ListGridListenerDelegate

    @Inject
    internal lateinit var relatedItemListenerDelegate: RelatedItemListenerDelegate

    @Inject
    internal lateinit var errorStateListenerDelegate: ErrorStateListenerDelegate

    private var binding by autoClearedNullable<UniversalSearchFragmentLayoutBinding>()

    private val searchParameter: SearchParameter by lazy {
        arguments?.getParcelable(EXTRA_SEARCH_PARAMETER) ?: SearchParameter()
    }

    override fun getScreenName(): String {
        return UNIVERSAL_SEARCH_SCREEN_NAME
    }

    override fun initInjector() {
        DaggerUniversalSearchFragmentComponent
            .builder()
            .baseAppComponent(component)
            .universalSearchContextModule(UniversalSearchContextModule(activity as Context))
            .universalSearchSearchParameterModule(UniversalSearchSearchParameterModule(searchParameter))
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UniversalSearchFragmentLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews()
        observeViewModelData()

        universalSearchViewModel.loadData()
    }

    private fun initViewModel() {
        activity?.let {
            universalSearchViewModel =
                ViewModelProvider(it).get(UniversalSearchViewModel::class.java)
        }
    }

    private fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding?.universalSearchRecyclerView?.let {
            initUniversalSearchAdapter()
            it.layoutManager = LinearLayoutManager(context)
            it.addItemDecoration(createDividerItemDecoration())
            it.adapter = universalSearchAdapter
        }
    }

    private fun createDividerItemDecoration(): RecyclerView.ItemDecoration {
        val dividerDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.universal_search_divider
        )
        val dividerItemDecoration = UniversalSearchItemDecoration()
        dividerDrawable?.let {
            dividerItemDecoration.setDrawable(it)
        }

        return dividerItemDecoration
    }

    private fun initUniversalSearchAdapter() {
        val typeFactory = UniversalSearchTypeFactoryImpl(
            carouselListener = carouselListenerDelegate,
            doubleLineListener = doubleLineListenerDelegate,
            listGridListener = listGridListenerDelegate,
            relatedItemListener = relatedItemListenerDelegate,
            errorStateListener = errorStateListenerDelegate,
        )
        universalSearchAdapter = UniversalSearchAdapter(typeFactory)
    }

    private fun observeViewModelData() {
        observeUniversalSearchState()
    }

    private fun observeUniversalSearchState() {
        universalSearchViewModel.getUniversalSearchState().observe(viewLifecycleOwner) {
            updateView(it)
        }
    }

    private fun updateView(universalSearchState: State<List<Visitable<*>>>?) {
        when(universalSearchState) {
            is State.Loading -> {
                updateList(universalSearchState)
                binding?.universalSearchLoader?.visible()
            }
            is State.Success -> {
                updateList(universalSearchState)
                binding?.universalSearchLoader?.gone()
            }
            is State.Error -> {
                updateList(universalSearchState)
                binding?.universalSearchLoader?.gone()
            }
        }
    }

    private fun updateList(universalSearchState: State<List<Visitable<*>>>) {
        universalSearchAdapter?.updateList(universalSearchState.data ?: listOf())
    }

    override fun getComponent(): BaseAppComponent {
        return (activity?.application as BaseMainApplication).baseAppComponent
    }
}