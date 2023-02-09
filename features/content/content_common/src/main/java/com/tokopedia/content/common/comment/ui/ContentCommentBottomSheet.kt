package com.tokopedia.content.common.comment.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.content.common.databinding.FragmentContentCommentBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by astidhiyaa on 09/02/23
 */
class ContentCommentBottomSheet : BottomSheetUnify() {

    private var _binding: FragmentContentCommentBottomSheetBinding? = null
    private val binding: FragmentContentCommentBottomSheetBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBottomSheet()
    }

    private fun setupBottomSheet() {
        _binding = FragmentContentCommentBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        clearContentPadding = true
        isDragable = false
        showKnob = false
        isFullpage = true
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) return
        show(fragmentManager, TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ContentCommentBottomSheet"

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): ContentCommentBottomSheet {
            return fragmentManager.findFragmentByTag(TAG) as? ContentCommentBottomSheet
                ?: fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    ContentCommentBottomSheet::class.java.name
                ) as ContentCommentBottomSheet
        }
    }
}
