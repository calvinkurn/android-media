package com.tokopedia.play.broadcaster.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroSelectGameBinding
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 29, 2022
 */
class PlayBroSelectGameBottomSheet @Inject constructor(

) : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroSelectGameBinding? = null
    private val binding: BottomSheetPlayBroSelectGameBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = BottomSheetPlayBroSelectGameBinding.inflate(LayoutInflater.from(requireContext()))
        setChild(binding.root)

        showHeader = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.65f).toInt()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "SelectGameBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayBroSelectGameBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? PlayBroSelectGameBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                                        classLoader,
                                        PlayBroSelectGameBottomSheet::class.java.name
                                    ) as PlayBroSelectGameBottomSheet
        }
    }
}