package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroFaceFilterSetupBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.play.broadcaster.R

/**
 * Created By : Jonathan Darwin on February 27, 2023
 */
class PlayBroFaceFilterSetupBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroFaceFilterSetupBinding? = null
    private val binding: BottomSheetPlayBroFaceFilterSetupBinding
        get() = _binding!!

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

        showCloseIcon = false
        showKnob = true
        isHideable = true
    }

    private fun setupView() {
        setTitle(getString(R.string.play_bro_face_filter_label))
        setAction(getString(R.string.play_broadcaster_reset_filter)) {
            /** TODO: handle this */
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
