package com.tokopedia.play.broadcaster.view.fragment.facefilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.play.broadcaster.databinding.FragmentPlayBroMakeupTabBinding

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class PlayBroMakeupTabFragment : TkpdBaseV4Fragment() {

    private var _binding: FragmentPlayBroMakeupTabBinding? = null
    private val binding: FragmentPlayBroMakeupTabBinding
        get() = _binding!!

    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayBroMakeupTabBinding.inflate(inflater, container, false)
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
        private const val TAG = "PlayBroMakeupTabFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayBroMakeupTabFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroMakeupTabFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroMakeupTabFragment::class.java.name
            ) as PlayBroMakeupTabFragment
        }
    }
}
