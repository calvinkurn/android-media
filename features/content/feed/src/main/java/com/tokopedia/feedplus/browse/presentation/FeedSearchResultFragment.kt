package com.tokopedia.feedplus.browse.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.feedplus.browse.presentation.adapter.FeedSearchResultAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.CategoryInspirationItemDecoration
import com.tokopedia.feedplus.browse.presentation.factory.FeedSearchResultViewModelFactory
import com.tokopedia.feedplus.browse.presentation.model.FeedSearchResultUiState
import com.tokopedia.feedplus.databinding.FragmentFeedSearchResultBinding
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import javax.inject.Inject
import com.tokopedia.feedplus.R as feedplusR

class FeedSearchResultFragment @Inject constructor(
    private val viewModelFactoryCreator: FeedSearchResultViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedSearchResultBinding? = null
    private val binding: FragmentFeedSearchResultBinding get() = _binding!!

    private val viewModel: FeedSearchResultViewModel by viewModels {
        viewModelFactoryCreator.create(
            this,
            arguments?.getString(FeedSearchResultActivity.KEYWORD_PARAM).orEmpty()
        )
    }

    private var rvAdapter: FeedSearchResultAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedSearchResultBinding.inflate(inflater)

        setupHeader()
        initObserver()
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getScreenName(): String = "Search Result Fragment"

    private fun setupHeader() {
        binding.srpHeader.onBackClicked {
            activity?.finish()
        }
    }

    private fun initObserver() {
        viewModel.resultState.observe(viewLifecycleOwner) {
            when(it) {
                is FeedSearchResultUiState.Success -> showResult()
                else -> showError(it)
            }
        }

        viewModel.keyword.observe(viewLifecycleOwner) {
            binding.srpHeader.setSearchbarText(it)
        }

        viewModel.resultData.observe(viewLifecycleOwner) {
            updateResultRv(it)
        }
    }

    private fun initRecyclerView() {
        binding.resultRv.let {
            showResult()
            FeedSearchResultAdapter(this.viewLifecycleOwner.lifecycleScope).also { adapter ->
                rvAdapter = adapter

                val layoutManager = GridLayoutManager(context, adapter.spanCount).apply {
                    spanSizeLookup = adapter.getSpanSizeLookup()
                }
                it.layoutManager = layoutManager
                it.itemAnimator = null
                it.addItemDecoration(
                    CategoryInspirationItemDecoration(
                        it.resources,
                        layoutManager.spanCount
                    )
                )
                it.adapter = adapter

                adapter.setLoadingState()
            }
        }
    }

    private fun updateResultRv(data: SearchTempDataModel) {
        if (data.uiState == FeedSearchResultUiState.Success) {
            showResult()
            rvAdapter?.setList(data)
        }
    }

    private fun showResult() {
        binding.resultRv.show()
        binding.errorView.hide()
    }

    private fun showError(uiState: FeedSearchResultUiState) {
        binding.resultRv.hide()

        var title: String? = null
        var desc: String? = null
        var ctaText: String? = null
        var customIllustration: Int? = null

        when(uiState) {
            is FeedSearchResultUiState.Restricted -> {
                title = getString(feedplusR.string.feed_local_search_restricted_title)
                desc = getString(feedplusR.string.feed_local_search_restricted_desc)
                ctaText = getString(feedplusR.string.feed_local_search_restricted_cta)
                customIllustration = feedplusR.drawable.feed_search_restricted_illustration
            }
            is FeedSearchResultUiState.NotFound -> {
                title = getString(feedplusR.string.feed_local_search_not_found_title)
                desc = getString(feedplusR.string.feed_local_search_not_found_desc)
                ctaText = getString(feedplusR.string.feed_local_search_not_found_cta)
                customIllustration = feedplusR.drawable.feed_search_not_found_illustration
            }
            is FeedSearchResultUiState.InternalError -> {
                binding.errorView.setType(GlobalError.SERVER_ERROR)
            }
            else -> {
                binding.errorView.setType(GlobalError.NO_CONNECTION)
            }
        }

        binding.errorView.let { globalError ->
            title?.let { globalError.errorTitle.text = it }
            desc?.let { globalError.errorDescription.text = it }
            ctaText?.let {
                if (uiState is FeedSearchResultUiState.Restricted) {
                    globalError.errorSecondaryAction.text = it

                    globalError.errorAction.hide()
                    globalError.errorSecondaryAction.show()
                } else {
                    globalError.errorAction.text = it

                    globalError.errorAction.show()
                    globalError.errorSecondaryAction.hide()
                }
            }

            customIllustration?.let {
                globalError.errorIllustration.setImageResource(it)
            }


            // Todo: Will be adjust later
            globalError.setActionClickListener {
                showResult()
                viewModel.getDataResult()
            }

            globalError.setSecondaryActionClickListener {
                showResult()
                viewModel.getDataResult()
            }

            globalError.show()
        }
    }

    companion object {
        fun create(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle?
        ): FeedSearchResultFragment {
            return fragmentManager.fragmentFactory.instantiate(
                classLoader,
                FeedSearchResultFragment::class.java.name
            ).apply {
                arguments = bundle
            } as FeedSearchResultFragment
        }
    }
}
