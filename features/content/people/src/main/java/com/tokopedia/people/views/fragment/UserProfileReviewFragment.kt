package com.tokopedia.people.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.people.databinding.FragmentUserProfileReviewBinding
import com.tokopedia.people.databinding.UpFragmentVideoBinding
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.UserProfileActivity
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
class UserProfileReviewFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentUserProfileReviewBinding? = null
    private val binding: FragmentUserProfileReviewBinding
        get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            this,
            requireArguments().getString(UserProfileActivity.EXTRA_USERNAME) ?: "",
        )
    }
    override fun getScreenName(): String = TAG

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileReviewBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
