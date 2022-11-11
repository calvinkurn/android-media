package com.tokopedia.play.broadcaster.shorts.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsPreparationBinding
import com.tokopedia.play.broadcaster.databinding.FragmentPlayShortsSummaryBinding
import com.tokopedia.play.broadcaster.shorts.view.fragment.base.PlayShortsBaseFragment
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsSummaryFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
) : PlayShortsBaseFragment() {

    override fun getScreenName(): String = TAG

    private var _binding: FragmentPlayShortsSummaryBinding? = null
    private val binding: FragmentPlayShortsSummaryBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayShortsSummaryBinding.inflate(
            inflater,
            container,
            false
        )
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "PlayShortsSummaryFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsSummaryFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayShortsSummaryFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsSummaryFragment::class.java.name
            ) as PlayShortsSummaryFragment
        }
    }
}
