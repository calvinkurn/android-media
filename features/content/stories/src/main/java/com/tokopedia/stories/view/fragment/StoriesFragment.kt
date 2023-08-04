package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.stories.databinding.FragmentStoriesBinding
import com.tokopedia.stories.view.adapter.StoriesPagerAdapter
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.action.StoriesAction
import javax.inject.Inject

class StoriesFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesBinding? = null
    private val binding: FragmentStoriesBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> { viewModelFactory }

    private val pagerAdapter: StoriesPagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        StoriesPagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
            binding.storiesViewPager,
        ) { tabPosition ->
            viewModel.mCounter = tabPosition
        }
    }

    override fun getScreenName(): String {
        return TAG
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAction(StoriesAction.SetInitialData(arguments))

        setupViewPager()
        setupObserver()
    }

    private fun viewModelAction(event: StoriesAction) {
        viewModel.submitAction(event)
    }

    private fun setupViewPager() = with(binding.storiesViewPager) {
        adapter = pagerAdapter
    }

    private fun setupObserver() {
        pagerAdapter.setCategorySize(10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "StoriesFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): StoriesFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesFragment
        }
    }

}
