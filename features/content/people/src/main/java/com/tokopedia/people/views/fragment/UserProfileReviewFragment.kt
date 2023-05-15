package com.tokopedia.people.views.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.people.databinding.FragmentUserProfileReviewBinding
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.UserProfileActivity
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.people.R
import com.tokopedia.people.utils.UserProfileUiBridge
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
class UserProfileReviewFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userProfileUiBridge: UserProfileUiBridge,
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

    private val boldSpan: StyleSpan
        get() = StyleSpan(Typeface.BOLD)

    private val colorSpan: ForegroundColorSpan
        get() = ForegroundColorSpan(MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_GN500))

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

        setupObserver()

        viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderMainLayout(it.prevValue, it.value)
            }
        }
    }

    private fun renderMainLayout(
        prev: UserProfileUiState?,
        value: UserProfileUiState,
    ) {
        if (prev?.reviewSettings == value.reviewSettings &&
            prev.reviewContent == value.reviewContent) return

        /** TODO: handle loading state here */

        if (value.reviewSettings.isEnabled) {
            if (value.reviewContent.reviewList.isNotEmpty()) {
                showMainLayout(value.reviewContent)
            } else {
                showNoReviewLayout()
            }
        } else {
            showHiddenReviewLayout()
        }
    }

    private fun showMainLayout(reviewContent: UserReviewUiModel) {
        binding.layoutNoUserReview.root.gone()

        if (reviewContent.isLoading && reviewContent.reviewList.isEmpty()) {
            /** TODO: show shimmering */

            return
        }


    }

    private fun showNoReviewLayout() {
        if (viewModel.isSelfProfile) {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_self_review_empty_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = setupClickableText(
                fullText = getString(R.string.up_profile_self_review_empty_desc),
                highlightedText = getString(R.string.up_profile_create_review),
                clickablePolicy = object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        /** TODO: handle this */
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }
            )
            binding.layoutNoUserReview.tvReviewHiddenDesc.movementMethod = LinkMovementMethod.getInstance()
        } else {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_other_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = getString(R.string.up_profile_other_review_hidden_desc, viewModel.firstName)
        }
        binding.layoutNoUserReview.root.show()
    }

    private fun showHiddenReviewLayout() {
        if (viewModel.isSelfProfile) {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_self_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = setupClickableText(
                fullText = getString(R.string.up_profile_self_review_hidden_desc),
                highlightedText = getString(R.string.up_profile_settings_title),
                clickablePolicy = object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        userProfileUiBridge.eventBus.emit(UserProfileUiBridge.Event.OpenProfileSetingsPage)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }
            )
            binding.layoutNoUserReview.tvReviewHiddenDesc.movementMethod = LinkMovementMethod.getInstance()
        } else {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_other_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = getString(R.string.up_profile_other_review_hidden_desc, viewModel.firstName)
        }
        binding.layoutNoUserReview.root.show()
    }

    private fun setupClickableText(
        fullText: String,
        highlightedText: String,
        clickablePolicy: ClickableSpan,
    ): CharSequence {
        val text = SpannableStringBuilder()

        text.append(fullText)
        text.setSpanOnText(highlightedText, clickablePolicy, boldSpan, colorSpan)

        return text
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
