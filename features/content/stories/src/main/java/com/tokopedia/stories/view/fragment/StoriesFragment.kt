package com.tokopedia.stories.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.stories.databinding.FragmentStoriesBinding
import com.tokopedia.stories.view.viewmodel.StoriesViewModel
import com.tokopedia.stories.view.viewmodel.factory.StoriesViewModelFactory
import timber.log.Timber
import javax.inject.Inject

class StoriesFragment @Inject constructor(
    private val viewModelFactory: StoriesViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesBinding? = null
    private val binding: FragmentStoriesBinding
        get() = _binding!!

    private val viewModel by activityViewModels<StoriesViewModel> {
        viewModelFactory.create(requireActivity())
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
        Timber.d("data ${viewModel.getData()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoriesBaseFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): StoriesFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesFragment::class.java.name
            ) as StoriesFragment
        }
    }

}
