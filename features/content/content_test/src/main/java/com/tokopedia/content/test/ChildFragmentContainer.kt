package com.tokopedia.content.test

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

/**
 * Created by kenny.hadisaputra on 17/03/22
 */
open class ChildFragmentContainer(
    private val creator: (className: String) -> Fragment,
) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        childFragmentManager.fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return creator(className)
            }
        }

        val childFragment = childFragmentManager.fragmentFactory
            .instantiate(requireActivity().classLoader, "") as Fragment

        when (childFragment) {
            is DialogFragment -> childFragment.show(childFragmentManager, "")
            else -> {
                childFragmentManager.beginTransaction()
                    .add(childFragment,  "")
                    .commit()
            }
        }
    }
}