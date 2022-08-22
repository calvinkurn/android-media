package com.tokopedia.autocompletecomponent.universal.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.UniversalSearchFragmentLayoutBinding
import com.tokopedia.autocompletecomponent.universal.UniversalConstant.UNIVERSAL_SEARCH_SCREEN_NAME
import com.tokopedia.autocompletecomponent.universal.di.DaggerUniversalSearchComponent
import com.tokopedia.autocompletecomponent.universal.presentation.adapter.UniversalSearchAdapter
import com.tokopedia.autocompletecomponent.universal.presentation.typefactory.UniversalSearchTypeFactoryImpl
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModel
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselListenerDelegate
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.model.SearchParameter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class UniversalSearchFragment: BaseDaggerFragment() {

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

    @Inject
    internal lateinit var universalSearchViewModel: UniversalSearchViewModel

    private var binding by autoClearedNullable<UniversalSearchFragmentLayoutBinding>()

    override fun getScreenName(): String {
        return UNIVERSAL_SEARCH_SCREEN_NAME
    }

    override fun initInjector() {
        DaggerUniversalSearchComponent
            .builder()
            .baseAppComponent(getComponent(BaseAppComponent::class.java))
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

        initViews()
        observeViewModelData()

        universalSearchViewModel.onViewCreated()
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

    private fun createDividerItemDecoration(): DividerItemDecoration {
        val dividerDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.universal_search_divider
        )
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        dividerDrawable?.let {
            dividerItemDecoration.setDrawable(it)
        }

        return dividerItemDecoration
    }

    private fun initUniversalSearchAdapter() {
        val carouselListenerDelegate = CarouselListenerDelegate()

        val typeFactory = UniversalSearchTypeFactoryImpl(carouselListenerDelegate)
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
                binding?.universalSearchLoader?.hide()
            }
            is State.Error -> {

            }
        }
    }

    private fun updateList(universalSearchState: State<List<Visitable<*>>>) {
        universalSearchAdapter?.updateList(universalSearchState.data ?: listOf())
    }
}