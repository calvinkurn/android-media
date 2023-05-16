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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
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
import com.tokopedia.people.utils.getBoldSpan
import com.tokopedia.people.utils.getClickableSpan
import com.tokopedia.people.utils.getGreenColorSpan
import com.tokopedia.people.views.adapter.UserReviewAdapter
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.viewholder.UserReviewViewHolder

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

    private val linearLayoutManager by lazyThreadSafetyNone {
        LinearLayoutManager(activity)
    }

    private val adapter: UserReviewAdapter by lazyThreadSafetyNone {
        UserReviewAdapter(
            listener = object : UserReviewViewHolder.Review.Listener {
                override fun onClickLike(review: UserReviewUiModel.Review) {
                    viewModel.submitAction(UserProfileAction.ClickLikeReview(review))
                }
            },
            onLoading = {
                viewModel.submitAction(UserProfileAction.LoadPlayVideo(isRefresh = false))
            }
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

        setupView()
        setupObserver()

        viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = true))
    }

    private fun setupView() {
        binding.rvReview.layoutManager = linearLayoutManager
        binding.rvReview.adapter = adapter
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
        binding.rvReview.show()

        if (reviewContent.isLoading && reviewContent.reviewList.isEmpty()) {
            /** TODO: show shimmering */

            return
        }

        val mappedList = reviewContent.reviewList.map {
            UserReviewAdapter.Model.Review(it)
        }

        val finalList = if (reviewContent.hasNext) {
            mappedList + listOf(UserReviewAdapter.Model.Loading)
        } else {
            mappedList
        }

        adapter.setItemsAndAnimateChanges(finalList)
    }

    private fun showNoReviewLayout() {
        if (viewModel.isSelfProfile) {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_self_review_empty_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = setupClickableText(
                fullText = getString(R.string.up_profile_self_review_empty_desc),
                highlightedText = getString(R.string.up_profile_create_review),
                clickablePolicy = getClickableSpan {
                    RouteManager.route(requireContext(), ApplinkConst.REPUTATION)
                }
            )
            binding.layoutNoUserReview.tvReviewHiddenDesc.movementMethod = LinkMovementMethod.getInstance()
        } else {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_other_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = getString(R.string.up_profile_other_review_hidden_desc, viewModel.firstName)
        }
        binding.layoutNoUserReview.root.show()
        binding.rvReview.hide()
    }

    private fun showHiddenReviewLayout() {
        if (viewModel.isSelfProfile) {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_self_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = setupClickableText(
                fullText = getString(R.string.up_profile_self_review_hidden_desc),
                highlightedText = getString(R.string.up_profile_settings_title),
                clickablePolicy = getClickableSpan {
                    userProfileUiBridge.eventBus.emit(UserProfileUiBridge.Event.OpenProfileSetingsPage)
                }
            )
            binding.layoutNoUserReview.tvReviewHiddenDesc.movementMethod = LinkMovementMethod.getInstance()
        } else {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_other_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = getString(R.string.up_profile_other_review_hidden_desc, viewModel.firstName)
        }
        binding.layoutNoUserReview.root.show()
        binding.rvReview.hide()
    }

    private fun setupClickableText(
        fullText: String,
        highlightedText: String,
        clickablePolicy: ClickableSpan,
    ): CharSequence {
        val text = SpannableStringBuilder()

        text.append(fullText)
        text.setSpanOnText(highlightedText, clickablePolicy, getBoldSpan(), getGreenColorSpan(requireContext()))

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
