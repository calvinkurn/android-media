package com.tokopedia.people.views.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
class UserProfileReviewFragment @Inject constructor(

) : TkpdBaseV4Fragment() {
    override fun getScreenName(): String = TAG

    companion object {
        private const val TAG = "UserProfileReviewFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle,
        ): UserProfileReviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserProfileReviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserProfileReviewFragment::class.java.name,
            ).apply {
                arguments = bundle
            } as UserProfileReviewFragment
        }
    }
}
