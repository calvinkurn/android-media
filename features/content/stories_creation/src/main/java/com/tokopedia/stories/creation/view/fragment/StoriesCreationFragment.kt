package com.tokopedia.stories.creation.view.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationFragment @Inject constructor(): TkpdBaseV4Fragment() {

    override fun getScreenName(): String = TAG

    companion object {
        private const val TAG = "StoriesCreationFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): StoriesCreationFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? StoriesCreationFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesCreationFragment::class.java.name
            ).apply {
                arguments = bundle
            } as StoriesCreationFragment
        }
    }
}
