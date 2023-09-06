package com.tokopedia.stories.creation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.stories.creation.databinding.FragmentStoriesCreationBinding
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationFragment @Inject constructor(): TkpdBaseV4Fragment() {

    private var _binding: FragmentStoriesCreationBinding? = null

    private val binding: FragmentStoriesCreationBinding = _binding!!

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoriesCreationBinding.inflate(
            inflater,
            container,
            false
        )
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "StoriesCreationFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): StoriesCreationFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesCreationFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesCreationFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesCreationFragment
        }
    }
}
