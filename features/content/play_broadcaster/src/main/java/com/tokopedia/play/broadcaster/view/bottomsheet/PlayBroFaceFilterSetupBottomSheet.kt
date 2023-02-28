package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroFaceFilterSetupBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.adapter.FaceFilterPagerAdapter
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class PlayBroFaceFilterSetupBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroFaceFilterSetupBinding? = null
    private val binding: BottomSheetPlayBroFaceFilterSetupBinding
        get() = _binding!!

    private val pagerAdapter by lazyThreadSafetyNone {
        FaceFilterPagerAdapter(
            childFragmentManager,
            requireContext().classLoader,
            lifecycle
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroFaceFilterSetupBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        clearContentPadding = true
        showCloseIcon = false
        showKnob = true
        isHideable = true
    }

    private fun setupView() {
        setTitle(getString(R.string.play_bro_face_filter_label))
        setAction(getString(R.string.play_broadcaster_reset_filter)) {
            /** TODO: handle this */
        }

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter

        TabsUnifyMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.setCustomText(getString(R.string.play_broadcaster_face_tab))
                1 -> tab.setCustomText(getString(R.string.play_broadcaster_makeup_tab))
                else -> {}
            }

        }
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) showNow(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "PlayBroFaceFilterSetupBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayBroFaceFilterSetupBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroFaceFilterSetupBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroFaceFilterSetupBottomSheet::class.java.name
            ) as PlayBroFaceFilterSetupBottomSheet
        }
    }
}
