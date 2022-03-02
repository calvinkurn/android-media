package com.tokopedia.play.broadcaster.helper

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
open class BottomSheetContainer(
    private val creator: () -> BottomSheetDialogFragment,
) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return creator()
            }
        }

        val bottomSheet = childFragmentManager.fragmentFactory
            .instantiate(requireActivity().classLoader, "") as BottomSheetDialogFragment

        bottomSheet.show(childFragmentManager, "")
    }
}