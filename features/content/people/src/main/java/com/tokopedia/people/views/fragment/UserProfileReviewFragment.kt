package com.tokopedia.people.views.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
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
import com.tokopedia.people.analytic.UserReviewImpressCoordinator
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.utils.UserProfileUiBridge
import com.tokopedia.people.utils.getBoldSpan
import com.tokopedia.people.utils.getClickableSpan
import com.tokopedia.people.utils.getGreenColorSpan
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.views.adapter.UserReviewAdapter
import com.tokopedia.people.views.itemdecoration.UserReviewItemDecoration
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.viewholder.UserReviewViewHolder
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on May 12, 2023
 */
class UserProfileReviewFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private val userProfileUiBridge: UserProfileUiBridge,
    private val userProfileTracker: UserProfileTracker,
    private val userReviewImpressCoordinator: UserReviewImpressCoordinator,
    private val router: Router,
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentUserProfileReviewBinding? = null
    private val binding: FragmentUserProfileReviewBinding
        get() = _binding!!

    private val viewModel: UserProfileViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            this,
            requireArguments().getString(UserProfileActivity.EXTRA_USERNAME).orEmpty(),
        )
    }

    private val linearLayoutManager by lazyThreadSafetyNone {
        LinearLayoutManager(activity)
    }

    private val adapter: UserReviewAdapter by lazyThreadSafetyNone {
        UserReviewAdapter(
            lifecycleOwner = viewLifecycleOwner,
            listener = object : UserReviewViewHolder.Review.Listener {
                override fun onImpressCard(review: UserReviewUiModel.Review, position: Int) {
                    userReviewImpressCoordinator.impress(review) {
                        userProfileTracker.impressReviewCard(
                            userId = viewModel.profileUserID,
                            feedbackId = review.feedbackID,
                            isSelf = viewModel.isSelfProfile,
                            productId = review.product.productID,
                            position = position,
                        )
                    }
                }

                override fun onClickLike(review: UserReviewUiModel.Review) {
                    userProfileTracker.clickLikeReview(
                        userId = viewModel.profileUserID,
                        feedbackId = review.feedbackID,
                        isSelf = viewModel.isSelfProfile,
                        productId = review.product.productID,
                    )
                    viewModel.submitAction(UserProfileAction.ClickLikeReview(review))
                }

                override fun onClickSeeMore(review: UserReviewUiModel.Review) {
                    userProfileTracker.clickReviewSeeMoreDescription(
                        userId = viewModel.profileUserID,
                        feedbackId = review.feedbackID,
                        isSelf = viewModel.isSelfProfile,
                        productId = review.product.productID,
                    )
                    viewModel.submitAction(UserProfileAction.ClickReviewTextSeeMore(review))
                }

                override fun onClickProductInfo(review: UserReviewUiModel.Review) {
                    userProfileTracker.clickReviewProductInfo(
                        userId = viewModel.profileUserID,
                        feedbackId = review.feedbackID,
                        isSelf = viewModel.isSelfProfile,
                        productReview = review.product,
                    )
                    viewModel.submitAction(UserProfileAction.ClickProductInfo(review))
                }

                override fun onMediaClick(
                    feedbackId: String,
                    productId: String,
                    attachment: UserReviewUiModel.Attachment
                ) {
                    userProfileTracker.clickReviewMedia(
                        userId = viewModel.profileUserID,
                        feedbackId = feedbackId,
                        isSelf = viewModel.isSelfProfile,
                        productId = productId,
                    )
                    viewModel.submitAction(UserProfileAction.ClickReviewMedia(feedbackId, attachment))
                }
            },
            onLoading = {
                viewModel.submitAction(UserProfileAction.LoadUserReview(isRefresh = false))
            }
        )
    }

    private val reviewMediaGalleryForActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                val feedbackId = ReviewMediaGalleryRouter.getFeedbackIdResult(data)
                val likeStatus = ReviewMediaGalleryRouter.getLikeStatusResult(data)
                viewModel.submitAction(UserProfileAction.UpdateLikeStatus(feedbackId, likeStatus))
            }
        }
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

    override fun onPause() {
        super.onPause()
        sendPendingTracker()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvReview.addItemDecoration(UserReviewItemDecoration(requireContext()))
        binding.rvReview.layoutManager = linearLayoutManager
        binding.rvReview.adapter = adapter
    }

    private fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderMainLayout(it.prevValue, it.value)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UserProfileUiEvent.SendPendingTracker -> {
                        sendPendingTracker()
                    }
                    is UserProfileUiEvent.ErrorLoadReview -> {
                        showError(event.throwable)
                    }
                    is UserProfileUiEvent.ErrorLikeDislike -> {
                        showError(event.error)
                    }
                    is UserProfileUiEvent.OpenProductDetailPage -> {
                        router.route(requireContext(), ApplinkConst.PRODUCT_INFO, event.productId)
                    }
                    is UserProfileUiEvent.OpenReviewMediaGalleryPage -> {
                        val intent = ReviewMediaGalleryRouter.routeToReviewMediaGallery(
                            context = requireContext(),
                            pageSource = ReviewMediaGalleryRouter.PageSource.USER_PROFILE,
                            productID = event.review.product.productID,
                            shopID = "",
                            isProductReview = true,
                            isFromGallery = false,
                            mediaPosition = event.mediaPosition,
                            showSeeMore = false,
                            isReviewOwner = viewModel.isSelfProfile,
                            preloadedDetailedReviewMediaResult = event.review.mapToProductReviewMediaGalleryModel(viewModel.profileUserID)
                        )

                        router.route(reviewMediaGalleryForActivityResult, intent)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderMainLayout(
        prev: UserProfileUiState?,
        value: UserProfileUiState,
    ) {
        if (prev?.reviewSettings == value.reviewSettings &&
            prev.reviewContent == value.reviewContent) return

        if (value.reviewSettings == ProfileSettingsUiModel.Empty) return

        if (value.reviewSettings.isEnabled) {
            when (value.reviewContent.status) {
                UserReviewUiModel.Status.Loading -> {

                    showMainLayout()

                    if (value.reviewContent.isLoading && value.reviewContent.reviewList.isEmpty()) {
                        adapter.setItemsAndAnimateChanges(
                            List(USER_REVIEW_SHIMMER_COUNT) { UserReviewAdapter.Model.Shimmer }
                        )
                    }
                }
                UserReviewUiModel.Status.Success -> {
                    if (value.reviewContent.reviewList.isEmpty()) {
                        showNoReviewLayout()
                        return
                    }

                    showMainLayout()

                    val mappedList = value.reviewContent.reviewList.map {
                        UserReviewAdapter.Model.Review(it)
                    }

                    val finalList = if (value.reviewContent.hasNext) {
                        mappedList + listOf(UserReviewAdapter.Model.Loading)
                    } else {
                        mappedList
                    }

                    adapter.setItemsAndAnimateChanges(finalList)
                }
                else -> {}
            }
        } else {
            showHiddenReviewLayout()
        }
    }

    private fun showMainLayout() {
        binding.layoutNoUserReview.root.hide()
        binding.rvReview.show()
    }

    private fun showNoReviewLayout() {
        userProfileTracker.openScreenEmptyOrHiddenReviewTab()
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
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_other_review_empty_title)
        }

        binding.layoutNoUserReview.tvReviewHiddenDesc.showWithCondition(viewModel.isSelfProfile)
        binding.layoutNoUserReview.root.show()
        binding.rvReview.hide()
    }

    private fun showHiddenReviewLayout() {
        userProfileTracker.openScreenEmptyOrHiddenReviewTab()
        if (viewModel.isSelfProfile) {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_self_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = setupClickableText(
                fullText = getString(R.string.up_profile_self_review_hidden_desc),
                highlightedText = getString(R.string.up_profile_settings_title),
                clickablePolicy = getClickableSpan {
                    userProfileUiBridge.eventBus.emit(UserProfileUiBridge.Event.OpenProfileSettingsPage)
                }
            )
            binding.layoutNoUserReview.tvReviewHiddenDesc.movementMethod = LinkMovementMethod.getInstance()
        } else {
            binding.layoutNoUserReview.tvReviewHiddenTitle.text = getString(R.string.up_profile_other_review_hidden_title)
            binding.layoutNoUserReview.tvReviewHiddenDesc.text = getString(R.string.up_profile_other_review_hidden_desc, viewModel.firstName)
        }

        binding.layoutNoUserReview.tvReviewHiddenDesc.show()
        binding.layoutNoUserReview.root.show()
        binding.rvReview.hide()
    }

    private fun sendPendingTracker() {
        userReviewImpressCoordinator.sendTracker()
    }

    private fun setupClickableText(
        fullText: String,
        highlightedText: String,
        clickablePolicy: ClickableSpan,
    ): CharSequence {
        val text = SpannableStringBuilder()

        text.append(fullText)
        text.setSpanOnText(
            highlightedText,
            clickablePolicy,
            getBoldSpan(),
            getGreenColorSpan(requireContext())
        )

        return text
    }

    private fun showError(throwable: Throwable) {
        val message = when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                requireContext().getString(R.string.up_error_local_error)
            }
            else -> throwable.message ?: getString(R.string.up_error_unknown)
        }

        view?.showErrorToast(message)
    }

    companion object {
        private const val TAG = "UserProfileReviewFragment"

        private const val USER_REVIEW_SHIMMER_COUNT = 2

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
