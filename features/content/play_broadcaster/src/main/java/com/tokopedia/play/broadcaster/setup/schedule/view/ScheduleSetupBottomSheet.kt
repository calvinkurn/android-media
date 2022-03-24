package com.tokopedia.play.broadcaster.setup.schedule.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroProductChooserBinding
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayBroScheduleSetupBinding
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 24/03/22
 */
class ScheduleSetupBottomSheet @Inject constructor(
) : BottomSheetUnify() {

    private var _binding: BottomSheetPlayBroScheduleSetupBinding? = null
    private val binding: BottomSheetPlayBroScheduleSetupBinding
        get() = _binding!!

    private val eventBus by viewLifecycleBound(
        creator = { EventBus<Any>() },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayBroScheduleSetupBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.root.layoutParams = binding.root.layoutParams.apply {
            height = (getScreenHeight() * 0.85f).toInt()
        }
    }

    companion object {
        private const val TAG = "PlayBroScheduleSetupBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): ScheduleSetupBottomSheet {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ScheduleSetupBottomSheet
            return if (oldInstance != null) oldInstance
            else {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    ScheduleSetupBottomSheet::class.java.name
                ) as ScheduleSetupBottomSheet
            }
        }
    }
}