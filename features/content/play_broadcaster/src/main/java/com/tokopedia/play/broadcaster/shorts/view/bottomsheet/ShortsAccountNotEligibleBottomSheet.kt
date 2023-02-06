package com.tokopedia.play.broadcaster.shorts.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.play.broadcaster.databinding.BottomSheetShortsAccountNotEligibleBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on November 16, 2022
 */
class ShortsAccountNotEligibleBottomSheet : BottomSheetUnify() {

    private var mListener: Listener? = null

    private var _binding: BottomSheetShortsAccountNotEligibleBinding? = null
    private val binding: BottomSheetShortsAccountNotEligibleBinding
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

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetShortsAccountNotEligibleBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)

        clearContentPadding = true
        showCloseIcon = false
        isDragable = true
        isSkipCollapseState = true
        isHideable = true
        isCancelable = false
        showKnob = true
        overlayClickDismiss = false

        setOnDismissListener {
            mListener?.onClose()
        }
    }

    private fun setupView() {
        binding.btnOk.setOnClickListener {
            dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        if(!isAdded) show(fragmentManager, TAG)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    companion object {
        private const val TAG = "ShortsAccountNotEligibleBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ShortsAccountNotEligibleBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ShortsAccountNotEligibleBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ShortsAccountNotEligibleBottomSheet::class.java.name
            ) as ShortsAccountNotEligibleBottomSheet
        }
    }

    interface Listener {
        fun onClose()
    }
}
