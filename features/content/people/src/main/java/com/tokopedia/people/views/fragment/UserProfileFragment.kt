package com.tokopedia.people.views.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.updateMarginsRelative
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.navigation.shorts.PlayShorts
import com.tokopedia.content.common.navigation.shorts.PlayShortsParam
import com.tokopedia.content.common.onboarding.view.fragment.UGCOnboardingParentFragment
import com.tokopedia.content.common.types.BundleData.KEY_IS_OPEN_FROM
import com.tokopedia.content.common.types.ContentCommonUserType.KEY_AUTHOR_TYPE
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkConfig
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.shoprecom.cordinator.ShopRecomImpressCoordinator
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem
import com.tokopedia.feedcomponent.util.manager.FeedFloatingButtonManager
import com.tokopedia.feedcomponent.view.base.FeedPlusContainerListener
import com.tokopedia.globalerror.GlobalError.Companion.NO_CONNECTION
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_FULL
import com.tokopedia.globalerror.GlobalError.Companion.PAGE_NOT_FOUND
import com.tokopedia.globalerror.GlobalError.Companion.SERVER_ERROR
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.linker.share.DataMapper
import com.tokopedia.people.R
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.databinding.UpFragmentUserProfileBinding
import com.tokopedia.people.databinding.UpLayoutUserProfileHeaderBinding
import com.tokopedia.people.utils.showErrorToast
import com.tokopedia.people.utils.showToast
import com.tokopedia.people.utils.withCache
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.viewmodels.UserProfileViewModel.Companion.UGC_ONBOARDING_OPEN_FROM_LIVE
import com.tokopedia.people.viewmodels.UserProfileViewModel.Companion.UGC_ONBOARDING_OPEN_FROM_POST
import com.tokopedia.people.viewmodels.UserProfileViewModel.Companion.UGC_ONBOARDING_OPEN_FROM_SHORTS
import com.tokopedia.people.viewmodels.factory.UserProfileViewModelFactory
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity
import com.tokopedia.people.views.activity.UserProfileActivity.Companion.EXTRA_USERNAME
import com.tokopedia.people.views.adapter.UserProfilePagerAdapter
import com.tokopedia.people.views.adapter.UserProfilePagerAdapter.Companion.FRAGMENT_KEY_FEEDS
import com.tokopedia.people.views.adapter.UserProfilePagerAdapter.Companion.FRAGMENT_KEY_VIDEO
import com.tokopedia.people.views.fragment.bottomsheet.UserProfileOptionBottomSheet
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonItem
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.PermissionListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.net.SocketTimeoutException
import java.net.URLEncoder
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.feedcomponent.R as feedComponentR

class UserProfileFragment @Inject constructor(
    private val viewModelFactoryCreator: UserProfileViewModelFactory.Creator,
    private var userProfileTracker: UserProfileTracker,
    private val userSession: UserSessionInterface,
    private val feedFloatingButtonManager: FeedFloatingButtonManager,
    private val impressionCoordinator: ShopRecomImpressCoordinator,
    private val coachMarkManager: ContentCoachMarkManager,
    private val playShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig,
) : TkpdBaseV4Fragment(),
    ShareBottomsheetListener,
    ScreenShotListener,
    PermissionListener,
    ShopRecomWidgetCallback,
    FeedPlusContainerListener {

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var screenShotDetector: ScreenshotDetector? = null

    private var shouldRefreshRecyclerView: Boolean = false
    private var isViewMoreClickedBio: Boolean = false
    private var fabCreated: Boolean = false

    private var _binding: UpFragmentUserProfileBinding? = null

    private val binding: UpFragmentUserProfileBinding
        get() = _binding!!

    private val mainBinding: UpLayoutUserProfileHeaderBinding
        get() = _binding!!.mainLayout

    private var mBlockDialog: DialogUnify? = null

    private val dp8 by lazy(LazyThreadSafetyMode.NONE) {
        8.dpToPx(mainBinding.root.resources.displayMetrics)
    }

    private val viewModel: UserProfileViewModel by activityViewModels {
        viewModelFactoryCreator.create(
            this,
            requireArguments().getString(EXTRA_USERNAME).orEmpty()
        )
    }

    private val pagerAdapter: UserProfilePagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        UserProfilePagerAdapter(
            childFragmentManager,
            requireActivity(),
            lifecycle,
            mainBinding.profileTabs.tabLayout,
            mainBinding.profileTabs.viewPager
        ) {
            if (it == FRAGMENT_KEY_FEEDS) {
                userProfileTracker.clickFeedTab(viewModel.profileUserID, viewModel.isSelfProfile)
            } else if (it == FRAGMENT_KEY_VIDEO) userProfileTracker.clickVideoTab(viewModel.profileUserID, viewModel.isSelfProfile)
        }
    }

    private var ugcOnboardingOpenFrom: Int = 0
    private var viewPagerSelectedPage: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpFragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedFloatingButtonManager.setInitialData(this)

        initObserver()
        initListener()
        setHeader()
        setupCoachMark()

        if (arguments == null || requireArguments().getString(EXTRA_USERNAME).isNullOrBlank()) {
            // TODO show error page
            activity?.finish()
        }

        mainBinding.appBarUserProfile.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                binding.swipeRefreshLayout.isEnabled = verticalOffset == 0
            }
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshLandingPageData(true)
        }

        refreshLandingPageData(true)
        mainBinding.userPostContainer.displayedChild = PAGE_LOADING

        mainBinding.appBarUserProfile.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                shouldRefreshRecyclerView = verticalOffset == 0
            }
        )
        mainBinding.cardUserReminder.clContainer.setBackgroundResource(
            R.drawable.bg_card_profile_reminder
        )

        mainBinding.appBarUserProfile.addOnOffsetChangedListener(feedFloatingButtonManager.offsetListener)

        context?.let {
            screenShotDetector = UniversalShareBottomSheet.createAndStartScreenShotDetector(
                it,
                this,
                this,
                permissionListener = this
            )
        }

        mainBinding.btnKebabOption.setDrawable(
            getIconUnifyDrawable(
                requireContext(),
                IconUnify.MENU_KEBAB_HORIZONTAL
            )
        )

        mainBinding.btnKebabOption.setOnClickListener {
            UserProfileOptionBottomSheet.getOrCreate(
                childFragmentManager,
                requireContext().classLoader
            ).show(childFragmentManager)
        }

        initFabUserProfile()
        initTab()
    }

    override fun onResume() {
        super.onResume()
        mainBinding.fabUserProfile.checkFabMenuStatusWithTimer {
            mainBinding.fabUp.menuOpen
        }
    }

    override fun onPause() {
        super.onPause()
        mainBinding.fabUserProfile.stopTimer()
        impressionCoordinator.sendTracker { userProfileTracker.sendAll() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainBinding.appBarUserProfile.removeOnOffsetChangedListener(feedFloatingButtonManager.offsetListener)
        feedFloatingButtonManager.cancel()
        coachMarkManager.dismissAllCoachMark()

        dismissBlockUserDialog()
        mBlockDialog = null

        _binding = null
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when (childFragment) {
            is UGCOnboardingParentFragment -> {
                childFragment.setListener(
                    object : UGCOnboardingParentFragment.Listener {
                        override fun onSuccess() {
                            viewModel.submitAction(UserProfileAction.LoadProfile())
                            when (ugcOnboardingOpenFrom) {
                                UGC_ONBOARDING_OPEN_FROM_POST -> goToCreatePostPage()
                                UGC_ONBOARDING_OPEN_FROM_LIVE -> goToCreateLiveStream()
                                UGC_ONBOARDING_OPEN_FROM_SHORTS -> goToCreateShortsPage()
                                else -> {}
                            }
                        }

                        override fun impressTncOnboarding() {
                            userProfileTracker.impressionOnBoardingBottomSheetWithUsername(
                                viewModel.profileUserID
                            )
                        }

                        override fun impressCompleteOnboarding() {
                            userProfileTracker.impressionOnBoardingBottomSheetWithoutUsername(
                                viewModel.profileUserID
                            )
                        }

                        override fun clickNextOnTncOnboarding() {
                            userProfileTracker.clickLanjutOnBoardingBottomSheetWithUsername(
                                viewModel.profileUserID
                            )
                        }

                        override fun clickNextOnCompleteOnboarding() {
                            userProfileTracker.clickLanjutOnBoardingBottomSheetWithoutUsername(
                                viewModel.profileUserID
                            )
                        }
                    }
                )
            }
            is UserProfileOptionBottomSheet -> {
                childFragment.setListener(object : UserProfileOptionBottomSheet.Listener {
                    override fun onBlockingUser(
                        bottomSheet: UserProfileOptionBottomSheet,
                        shouldBlock: Boolean
                    ) {
                        bottomSheet.dismiss()

                        if (!userSession.isLoggedIn) {
                            startActivityForResult(
                                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                                REQUEST_CODE_LOGIN
                            )
                            return
                        }

                        if (shouldBlock) {
                            getBlockUserDialog().show()
                        } else {
                            viewModel.submitAction(UserProfileAction.UnblockUser)
                        }
                    }

                    override fun onReportUser(bottomSheet: UserProfileOptionBottomSheet) {
                        bottomSheet.dismiss()

                        if (!userSession.isLoggedIn) {
                            startActivityForResult(
                                RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                                REQUEST_CODE_LOGIN
                            )
                            return
                        }
                        goToTopChatReport()
                    }
                })
                childFragment.setDataSource(object : UserProfileOptionBottomSheet.DataSource {
                    override fun isBlocking(): Boolean {
                        return viewModel.isBlocking
                    }
                })
            }
        }
    }

    private fun initListener() {
        mainBinding.apply {
            textFollowingLabel.setOnClickListener { goToFollowingFollowerPage(false) }
            textFollowingCount.setOnClickListener { goToFollowingFollowerPage(false) }
            textFollowerLabel.setOnClickListener { goToFollowingFollowerPage(true) }
            textFollowerCount.setOnClickListener { goToFollowingFollowerPage(true) }
            shopRecommendation.setListener(this@UserProfileFragment, this@UserProfileFragment)

            textSeeMore.setOnClickListener {
                userProfileTracker.clickSelengkapnya(userSession.userId, self = viewModel.isSelfProfile)
                textBio.maxLines = MAX_LINE
                textSeeMore.hide()
                isViewMoreClickedBio = true
            }

            btnAction.setOnClickListener {
                if (viewModel.isSelfProfile) {
                    userProfileTracker.clickEditProfileButtonInOwnProfile(viewModel.profileUserID)
                    navigateToEditProfile()
                    return@setOnClickListener
                }

                if (!userSession.isLoggedIn) {
                    startActivityForResult(
                        RouteManager.getIntent(activity, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN_TO_FOLLOW
                    )
                } else if (viewModel.isBlocking) {
                    viewModel.submitAction(UserProfileAction.UnblockUser)
                } else {
                    doFollowUnfollow()
                }
            }

            mainBinding.cardUserReminder.btnCompleteProfile.setOnClickListener {
                userProfileTracker.clickProfileCompletionPrompt(viewModel.profileUserID)
                navigateToEditProfile()
            }
        }
    }

    private fun initFabUserProfile() = with(mainBinding) {
        fabUp.type = FloatingButtonUnify.BASIC
        fabUp.color = FloatingButtonUnify.COLOR_GREEN
        fabUp.circleMainMenu.invisible()

        fabUserProfile.setOnClickListener {
            coachMarkManager.hasBeenShown(fabUserProfile)

            fabUp.menuOpen = !fabUp.menuOpen

            if(fabUp.menuOpen && viewModel.isWhitelist) {
                userProfileTracker.viewCreateShorts(viewModel.profileUserID)
            }
        }
    }

    private fun initTab() = with(mainBinding.profileTabs) {
        viewPager.adapter = pagerAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerSelectedPage = position
            }
        })
    }

    private fun initObserver() {
        observeUiState()
        observeUiEvent()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderContent(it.prevValue, it.value)

                renderProfileInfo(it.prevValue?.profileInfo, it.value.profileInfo)
                renderButtonAction(it.prevValue, it.value)
                renderButtonOption(it.prevValue, it.value)
                renderCreateContentButton(it.prevValue, it.value)
                renderProfileReminder(it.prevValue, it.value)
                renderShopRecom(it.prevValue, it.value)
                renderProfileTab(it.prevValue?.profileTab, it.value.profileTab)

                renderGlobalError(it.prevValue, it.value)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UserProfileUiEvent.SuccessLoadTabs -> {
                        if (event.isEmptyContent) {
                            if (viewModel.isSelfProfile) emptyPostSelf() else emptyPostVisitor()
                            mainBinding.userPostContainer.displayedChild = PAGE_EMPTY
                        } else {
                            mainBinding.profileTabs.viewPager.currentItem = viewPagerSelectedPage
                            mainBinding.userPostContainer.displayedChild = PAGE_CONTENT
                        }
                    }
                    is UserProfileUiEvent.ErrorGetProfileTab -> {
                        if (binding.swipeRefreshLayout.isRefreshing) {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                        showErrorContent {
                            refreshLandingPageData(true)
                        }
                    }
                    is UserProfileUiEvent.ErrorFollowUnfollow -> {
                        val message = if (event.throwable.message.isNullOrEmpty()) getDefaultErrorMessage() else event.throwable.message
                        message?.let { view?.showErrorToast(it) }
                    }
                    is UserProfileUiEvent.ErrorUpdateReminder -> {
                        val message = when (event.throwable) {
                            is UnknownHostException, is SocketTimeoutException -> {
                                requireContext().getString(R.string.up_error_local_error)
                            }
                            else -> {
                                event.throwable.message ?: getDefaultErrorMessage()
                            }
                        }

                        view?.showErrorToast(message)
                    }
                    is UserProfileUiEvent.ErrorLoadProfile -> {
                        if (binding.swipeRefreshLayout.isRefreshing) {
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                    }
                    is UserProfileUiEvent.SuccessBlockUser -> {
                        dismissBlockUserDialog()
                        mainBinding.profileTabs.tabLayout.showWithCondition(
                            !event.isBlocking && pagerAdapter.getTabs().size > 1
                        )
                        val message = getString(
                            if (event.isBlocking) {
                                R.string.up_block_user_success_toaster
                            } else {
                                R.string.up_unblock_user_success_toaster
                            }
                        )
                        view?.showToast(message)
                        refreshLandingPageData(true)
                    }
                    is UserProfileUiEvent.ErrorBlockUser -> {
                        dismissBlockUserDialog()
                        val message = getString(
                            if (event.isBlocking) {
                                R.string.up_block_user_error_toaster
                            } else {
                                R.string.up_unblock_user_error_toaster
                            }
                        )
                        view?.showErrorToast(message)
                    }
                    is UserProfileUiEvent.BlockingUserState -> {
                        emptyPostVisitor()
                        mainBinding.userPostContainer.displayedChild = PAGE_EMPTY
                    }
                }
            }
        }
    }

    fun refreshLandingPageData(isRefreshPost: Boolean = false) {
        viewModel.submitAction(UserProfileAction.LoadProfile(isRefreshPost))
        if (!isRefreshPost) return

        if (pagerAdapter.getTabs().isEmpty()) return
        if (pagerAdapter.getFeedsTabs().isNotEmpty())
            viewModel.submitAction(UserProfileAction.LoadFeedPosts())
        if (pagerAdapter.getVideoTabs().isNotEmpty())
            viewModel.submitAction(UserProfileAction.LoadPlayVideo())
    }

    private fun addLiveClickListener(appLink: String) {
        RouteManager.route(context, appLink)
    }

    private fun showErrorContent(action: () -> Unit) = with(mainBinding.globalErrorContent) {
        mainBinding.userPostContainer.displayedChild = PAGE_ERROR
        apply {
            progressState = false
            refreshBtn?.setOnClickListener {
                progressState = true
                action.invoke()
            }
        }
    }

    /** Render UI */
    private fun renderContent(
        prev: UserProfileUiState?,
        curr: UserProfileUiState
    ) {
        if (prev == curr) return

        binding.viewFlipper.displayedChild = when {
            curr.profileInfo.isBlockedBy || curr.error != null -> PAGE_ERROR
            curr.isLoading -> PAGE_LOADING
            else -> PAGE_CONTENT
        }
    }

    private fun renderProfileInfo(
        prev: ProfileUiModel?,
        curr: ProfileUiModel
    ) {
        if (prev == curr || curr == ProfileUiModel.Empty) return

        binding.viewFlipper.displayedChild = PAGE_CONTENT

        userProfileTracker.openUserProfile(
            viewModel.profileUserID,
            live = curr.liveInfo.isLive
        )

        if (binding.swipeRefreshLayout.isRefreshing) {
            binding.swipeRefreshLayout.isRefreshing = false
        }

        /** Setup Profile Info */
        setProfileImg(curr)

        with(mainBinding) {
            textUserName.shouldShowWithAction(curr.username.isNotBlank()) {
                textUserName.text = getString(R.string.up_username_template, curr.username)
            }
            textDisplayName.text = curr.name
            textContentCount.text = curr.stats.totalPostFmt
            textFollowerCount.text = curr.stats.totalFollowerFmt
            textFollowingCount.text = curr.stats.totalFollowingFmt

            /** Setup Bio */
            val displayBioText = HtmlLinkHelper(requireContext(), curr.biography).spannedString
            textBio.showWithCondition(displayBioText?.isNotEmpty() == true)
            textBio.text = displayBioText

            if (displayBioText?.lines()?.count().orZero() > SEE_ALL_LINE) {
                if (isViewMoreClickedBio) {
                    textBio.maxLines = MAX_LINE
                    textSeeMore.hide()
                } else {
                    textBio.maxLines = SEE_ALL_LINE
                    textSeeMore.show()
                }
            } else {
                textSeeMore.hide()
            }
        }
        binding.headerProfile.title = curr.name
        binding.headerProfile.alpha = 1F
    }

    private fun renderButtonAction(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if (prev?.followInfo == value.followInfo &&
            prev.profileType == value.profileType &&
            prev.profileInfo.isBlocking == value.profileInfo.isBlocking
        ) {
            return
        }

        when (value.profileType) {
            ProfileType.NotLoggedIn -> {
                buttonActionUIUnFollow()
                mainBinding.btnAction.show()
            }
            ProfileType.OtherUser -> {
                if (value.profileInfo.isBlocking) {
                    buttonActionUIUnblock()
                } else if (value.followInfo.status) {
                    buttonActionUIFollow()
                } else {
                    buttonActionUIUnFollow()
                }
                mainBinding.btnAction.show()
            }
            ProfileType.Self -> {
                buttonActionUIEditProfile()
                mainBinding.btnAction.show()
            }
            ProfileType.Unknown -> {
                mainBinding.btnAction.hide()
            }
        }

        (mainBinding.btnAction.layoutParams as MarginLayoutParams)
            .updateMarginsRelative(
                end = if (value.profileType == ProfileType.OtherUser ||
                    value.profileType == ProfileType.NotLoggedIn
                ) {
                    dp8
                } else {
                    0
                }
            )
    }

    private fun renderButtonOption(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if (prev?.profileType == value.profileType) return

        mainBinding.btnKebabOption.showWithCondition(
            shouldShow = value.profileType == ProfileType.OtherUser ||
                value.profileType == ProfileType.NotLoggedIn
        )
    }

    private fun renderCreateContentButton(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if (prev?.followInfo == value.followInfo &&
            prev.profileType == value.profileType &&
            prev.profileWhitelist == value.profileWhitelist
        ) {
            return
        }

        try {
            if (value.profileType == ProfileType.Self && value.profileWhitelist.isWhitelist) {
                if (!fabCreated) {
                    val items = arrayListOf<FloatingButtonItem>()
                    items.add(createLiveFab())
                    items.add(createPostFab())
                    if(playShortsEntryPointRemoteConfig.isShowEntryPoint()) {
                        items.add(createShortsFab())
                    }

                    mainBinding.fabUp.addItem(items)
                    mainBinding.fabUserProfile.show()
                    fabCreated = true
                }
            } else {
                mainBinding.fabUserProfile.hide()
            }
        } catch (e: Exception) { }
    }

    private fun renderProfileReminder(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if (prev?.followInfo == value.followInfo &&
            prev.profileType == value.profileType
        ) {
            return
        }

        val usernameEmpty = value.profileInfo.username.isBlank()
        val biographyEmpty = value.profileInfo.biography.isBlank()

        val isShowProfileReminder = viewModel.isSelfProfile && usernameEmpty && biographyEmpty

        mainBinding.cardUserReminder.root.shouldShowWithAction(isShowProfileReminder) {
            userProfileTracker.impressionProfileCompletionPrompt(viewModel.profileUserID)
            mainBinding.btnAction.hide()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun renderShopRecom(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if (prev?.shopRecom == value.shopRecom) return

        val shopRecom = value.shopRecom

        mainBinding.shopRecommendation.setData(shopRecom)

        if (value.shopRecom.items.isEmpty()) mainBinding.shopRecommendation.hide()
        else {
            mainBinding.shopRecommendation.show()
            mainBinding.shopRecommendation.showContentShopRecom()
        }
    }

    private fun renderProfileTab(prev: ProfileTabUiModel?, value: ProfileTabUiModel) {
        if ((prev == null || prev == value) && value == ProfileTabUiModel()) return

        if (value.tabs == pagerAdapter.getTabs()) return

        pagerAdapter.insertFragment(value.tabs)
        mainBinding.profileTabs.tabLayout.showWithCondition(value.showTabs)
    }

    private fun createLiveFab(): FloatingButtonItem {
        return FloatingButtonItem(
            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.VIDEO),
            title = getString(feedComponentR.string.feed_fab_create_live),
            listener = {
                mainBinding.fabUp.menuOpen = false

                // TODO onboarding for `Buat Live` will be in the next phase
//                if (viewModel.needOnboarding) {
//                    ugcOnboardingOpenFrom = UGC_ONBOARDING_OPEN_FROM_LIVE
//                    openUGCOnboardingBottomSheet()
//                } else goToCreateLiveStream()

                goToCreateLiveStream()
            }
        )
    }

    private fun createPostFab(): FloatingButtonItem {
        return FloatingButtonItem(
            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.IMAGE),
            title = getString(feedComponentR.string.feed_fab_create_post),
            listener = {
                mainBinding.fabUp.menuOpen = false
                userProfileTracker.clickCreatePost(viewModel.profileUserID)
                if (viewModel.needOnboarding) {
                    ugcOnboardingOpenFrom = UGC_ONBOARDING_OPEN_FROM_POST
                    openUGCOnboardingBottomSheet()
                } else {
                    goToCreatePostPage()
                }
            }
        )
    }

    private fun renderGlobalError(
        prev: UserProfileUiState?,
        value: UserProfileUiState
    ) {
        if (prev == value) return

        if (value.profileInfo.isBlockedBy) {
            binding.globalError.setType(PAGE_NOT_FOUND)
            binding.globalError.setActionClickListener {
                activity?.onBackPressed()
            }
        } else if (value.error != null) {
            val type = when (value.error) {
                is UnknownHostException, is SocketTimeoutException -> NO_CONNECTION
                is IllegalStateException -> PAGE_FULL
                is RuntimeException -> {
                    when (value.error.localizedMessage?.toIntOrNull()) {
                        ReponseStatus.NOT_FOUND -> PAGE_NOT_FOUND
                        else -> SERVER_ERROR
                    }
                }
                else -> SERVER_ERROR
            }
            binding.globalError.setType(type)
            binding.globalError.setActionClickListener {
                binding.viewFlipper.displayedChild = PAGE_LOADING
                refreshLandingPageData(true)
            }
        }
    }

    private fun createShortsFab(): FloatingButtonItem {
        return FloatingButtonItem(
            iconDrawable = getIconUnifyDrawable(requireContext(), IconUnify.SHORT_VIDEO),
            title = getString(feedComponentR.string.feed_fab_create_shorts),
            listener = {
                mainBinding.fabUp.menuOpen = false

                userProfileTracker.clickCreateShorts(viewModel.profileUserID)

                /** TODO: uncomment this if you need onboarding */
//                if (viewModel.needOnboarding) {
//                    viewModel.ugcOnboardingOpenFrom = UGC_ONBOARDING_OPEN_FROM_SHORTS
//                    openUGCOnboardingBottomSheet()
//                }
//                else goToCreateShortsPage()

                goToCreateShortsPage()
            }
        )
    }

    private fun buttonActionUIFollow() = with(mainBinding.btnAction) {
        text = getString(R.string.up_btn_text_following)
        buttonVariant = UnifyButton.Variant.GHOST
        buttonType = UnifyButton.Type.ALTERNATE

        activity?.intent?.putExtra(EXTRA_FOLLOW_UNFOLLOW_STATUS, EXTRA_VALUE_IS_FOLLOWED)
    }

    private fun buttonActionUIUnFollow() = with(mainBinding.btnAction) {
        text = getString(R.string.up_btn_text_follow)
        buttonVariant = UnifyButton.Variant.FILLED
        buttonType = UnifyButton.Type.MAIN

        activity?.intent?.putExtra(EXTRA_FOLLOW_UNFOLLOW_STATUS, EXTRA_VALUE_IS_NOT_FOLLOWED)
    }

    private fun buttonActionUIEditProfile() = with(mainBinding.btnAction) {
        text = getString(R.string.up_btn_profile)
        buttonVariant = UnifyButton.Variant.GHOST
        buttonType = UnifyButton.Type.ALTERNATE
    }

    private fun buttonActionUIUnblock() = with(mainBinding.btnAction) {
        text = getString(R.string.up_unblock_action)
        buttonVariant = UnifyButton.Variant.FILLED
        buttonType = UnifyButton.Type.MAIN
    }

    private fun setProfileImg(profile: ProfileUiModel) {
        with(mainBinding) {
            imgProfileImage.urlSrc = profile.imageCover

            if (profile.liveInfo.isLive) {
                viewProfileOuterRing.show()
                textLive.show()

                val appLink = profile.liveInfo.channelLink.appLink
                textLive.setOnClickListener { addLiveClickListener(appLink) }
                imgProfileImage.setOnClickListener { addLiveClickListener(appLink) }
            } else {
                viewProfileOuterRing.visibility = View.INVISIBLE
                textLive.hide()

                textLive.setOnClickListener(null)
                imgProfileImage.setOnClickListener {
                    userProfileTracker.clickProfilePicture(userSession.userId, self = viewModel.isSelfProfile, profile.liveInfo.channelId)
                }
            }
        }
    }

    private fun setHeader() {
        binding.headerProfile.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
                userProfileTracker.clickBack(userSession.userId, self = viewModel.isSelfProfile)
            }

            val imgShare = addRightIcon(0)

            imgShare.clearImage()
            imgShare.setImageDrawable(getIconUnifyDrawable(context, IconUnify.SHARE_MOBILE))
            imgShare.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN1000
                ),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            imgShare.setOnClickListener {
                showUniversalShareBottomSheet()
                userProfileTracker.clickShare(userSession.userId, self = viewModel.isSelfProfile)
                userProfileTracker.clickShareButton(userSession.userId, self = viewModel.isSelfProfile)
                userProfileTracker.viewShareChannel(userSession.userId, self = viewModel.isSelfProfile)
            }

            if (!GlobalConfig.isSellerApp()) addNavigationMainMenu(this)
        }
    }

    private fun addNavigationMainMenu(parent: HeaderUnify) {
        parent.addRightIcon(0).apply {
            clearImage()
            setImageDrawable(getIconUnifyDrawable(context, IconUnify.MENU_HAMBURGER))

            setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_NN1000
                ),
                android.graphics.PorterDuff.Mode.MULTIPLY
            )

            setOnClickListener {
                userProfileTracker.clickBurgerMenu(userSession.userId, self = viewModel.isSelfProfile)
                RouteManager.route(activity, APPLINK_MENU)
            }
        }
    }

    private fun setupCoachMark() {
        coachMarkManager.setupCoachMark(
            ContentCoachMarkConfig(mainBinding.fabUserProfile).apply {
                title = getString(feedComponentR.string.feed_play_shorts_entry_point_coachmark_title)
                subtitle = getString(feedComponentR.string.feed_play_shorts_entry_point_coachmark_description)
                setCoachmarkPrefKey(ContentCoachMarkSharedPref.Key.PlayShortsEntryPoint, userSession.userId)
            }
        )
    }

    private fun navigateToEditProfile() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConstInternalUserPlatform.SETTING_PROFILE)
        startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
    }

    private fun doFollowUnfollow(isFromLogin: Boolean = false) {
        if (viewModel.isFollowed.not() || isFromLogin) {
            userProfileTracker.clickFollow(userSession.userId, viewModel.isSelfProfile)
        } else {
            userProfileTracker.clickUnfollow(userSession.userId, viewModel.isSelfProfile)
        }

        viewModel.submitAction(UserProfileAction.ClickFollowButton(isFromLogin))
    }

    private fun getDefaultErrorMessage() = getString(R.string.up_error_unknown)

    private fun getUsernameWithAdd() = getString(R.string.up_username_template, viewModel.profileUsername)

    override fun getScreenName(): String = TAG

    private fun goToFollowingFollowerPage(isFollowers: Boolean) {
        if (isFollowers) {
            userProfileTracker.clickFollowers(userSession.userId, self = viewModel.isSelfProfile)
        } else {
            userProfileTracker.clickFollowing(userSession.userId, self = viewModel.isSelfProfile)
        }

        startActivity(
            activity?.let {
                FollowerFollowingListingActivity.getCallingIntent(
                    it,
                    getFollowersBundle(isFollowers)
                )
            }
        )
    }

    private fun goToCreateLiveStream() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConst.PLAY_BROADCASTER)
        intent.putExtra(KEY_AUTHOR_TYPE, TYPE_USER)
        startActivity(intent)
    }

    private fun goToCreatePostPage() {
        val intent = RouteManager.getIntent(requireContext(), ApplinkConst.IMAGE_PICKER_V2)
        intent.putExtra(KEY_APPLINK_AFTER_CAMERA_CAPTURE, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
        intent.putExtra(KEY_MAX_MULTI_SELECT_ALLOWED, KEY_MAX_MULTI_SELECT_ALLOWED_VALUE)
        intent.putExtra(KEY_TITLE, getString(feedComponentR.string.feed_post_sebagai))
        intent.putExtra(KEY_APPLINK_FOR_GALLERY_PROCEED, ApplinkConst.AFFILIATE_DEFAULT_CREATE_POST_V2)
        intent.putExtra(KEY_IS_CREATE_POST_AS_BUYER, true)
        intent.putExtra(KEY_IS_OPEN_FROM, VALUE_IS_OPEN_FROM_USER_PROFILE)
        startActivity(intent)
    }

    private fun goToCreateShortsPage() {
        val appLink = PlayShorts.generateApplink {
            setAuthorType(PlayShortsParam.AuthorType.User)
        }

        RouteManager.route(requireContext(), appLink)
    }

    private fun openUGCOnboardingBottomSheet() {
        childFragmentManager.executePendingTransactions()
        val existingFragment = childFragmentManager.findFragmentByTag(UGCOnboardingParentFragment.TAG)
        if (existingFragment is UGCOnboardingParentFragment && existingFragment.isVisible) return

        val bundle = UGCOnboardingParentFragment.createBundle(
            if (viewModel.profileUsername.isEmpty()) UGCOnboardingParentFragment.OnboardingType.Complete
            else UGCOnboardingParentFragment.OnboardingType.Tnc
        )
        childFragmentManager.beginTransaction()
            .add(UGCOnboardingParentFragment::class.java, bundle, UGCOnboardingParentFragment.TAG)
            .commit()
    }

    private fun getFollowersBundle(isFollowers: Boolean): Bundle {
        val bundle = Bundle()
        bundle.putString(EXTRA_DISPLAY_NAME, viewModel.displayName)
        bundle.putString(EXTRA_USER_NAME, viewModel.profileUsername)
        bundle.putString(EXTRA_USER_ID, viewModel.profileUserID)
        bundle.putString(EXTRA_PROFILE_USER_ID, viewModel.profileUserID)
        bundle.putString(EXTRA_TOTAL_FOLLOWINGS, viewModel.totalFollowing)
        bundle.putString(EXTRA_TOTAL_FOLLOWERS, viewModel.totalFollower)
        bundle.putBoolean(EXTRA_IS_FOLLOWERS, isFollowers)
        return bundle
    }

    private fun emptyPostSelf() {
        mainBinding.emptyPost.textErrorEmptyTitle.text = getString(R.string.up_empty_post_title_on_self)
        mainBinding.emptyPost.textErrorEmptyDesc.apply {
            text = getString(R.string.up_empty_post_desc_on_self)
            show()
        }
    }

    private fun emptyPostVisitor() {
        mainBinding.emptyPost.textErrorEmptyTitle.text = getString(R.string.up_empty_post_on_visitor)
        mainBinding.emptyPost.textErrorEmptyDesc.hide()
    }

    @SuppressLint("PII Data Exposure")
    private fun getBlockUserDialog(): DialogUnify {
        if (mBlockDialog == null) {
            mBlockDialog = DialogUnify(
                context = requireContext(),
                actionType = DialogUnify.HORIZONTAL_ACTION,
                imageType = DialogUnify.NO_IMAGE
            ).apply {
                setOverlayClose(false)

                setTitle(
                    getString(R.string.up_block_user_dialog_title, viewModel.displayName)
                )
                setDescription(
                    getString(R.string.up_block_user_dialog_desc)
                )

                setPrimaryCTAText(getString(R.string.up_block_user_dialog_confirm))
                setPrimaryCTAClickListener {
                    dialogPrimaryCTA.isLoading = true
                    dialogPrimaryCTA.isEnabled = false

                    viewModel.submitAction(UserProfileAction.BlockUser)
                }
                setSecondaryCTAText(getString(R.string.up_block_user_dialog_cancel))
                setSecondaryCTAClickListener { dismiss() }

                setOnDismissListener {
                    dialogPrimaryCTA.isLoading = false
                    dialogPrimaryCTA.isEnabled = true
                }
            }
        }
        return mBlockDialog!!
    }

    private fun dismissBlockUserDialog() {
        if (mBlockDialog == null) return
        val dialog = getBlockUserDialog()
        if (dialog.isShowing) dialog.dismiss()
    }

    override fun onShopRecomCloseClicked(item: ShopRecomUiModelItem) {
        viewModel.submitAction(UserProfileAction.RemoveShopRecomItem(item.id))
    }

    override fun onShopRecomFollowClicked(item: ShopRecomUiModelItem) {
        userProfileTracker.clickFollowProfileRecommendation(
            viewModel.profileUserID,
            item
        )
        viewModel.submitAction(UserProfileAction.ClickFollowButtonShopRecom(item.id))
    }

    override fun onShopRecomItemClicked(item: ShopRecomUiModelItem, postPosition: Int) {
        userProfileTracker.clickProfileRecommendation(
            viewModel.profileUserID,
            item,
            item.logoImageURL,
            postPosition
        )
        RouteManager.route(requireContext(), item.applink)
    }

    override fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int) {
        impressionCoordinator.initiateShopImpress(item) { shopImpress ->
            userProfileTracker.impressionProfileRecommendation(
                viewModel.profileUserID,
                shopImpress,
                postPosition
            )
        }
    }

    override fun onShopRecomLoadingNextPage(nextCursor: String) {
        viewModel.submitAction(UserProfileAction.LoadNextPageShopRecom(nextCursor))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /** No need to check resultCode since edit profile page doesn't give specific result code */
        if (requestCode == REQUEST_CODE_EDIT_PROFILE) refreshLandingPageData(isRefreshPost = false)

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            REQUEST_CODE_LOGIN_TO_FOLLOW -> doFollowUnfollow(isFromLogin = true)
            REQUEST_CODE_LOGIN_TO_SET_REMINDER -> viewModel.submitAction(UserProfileAction.ClickUpdateReminder(isFromLogin = true))
            REQUEST_CODE_LOGIN -> refreshLandingPageData(isRefreshPost = true)
        }
    }

    private fun showUniversalShareBottomSheet() {
        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@UserProfileFragment)
            userSession.userId.ifEmpty { "0" }.let {
                setUtmCampaignData(
                    PAGE_NAME_PROFILE,
                    it,
                    viewModel.profileUserID,
                    FEATURE_SHARE
                )
            }
            setMetaData(
                tnTitle = viewModel.displayName,
                tnImage = viewModel.profileCover
            )
            setOgImageUrl(viewModel.profileCover)
        }
        universalShareBottomSheet?.show(childFragmentManager, this, screenShotDetector)
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val desc = buildString {
            append("Lihat foto & video menarik dari ${viewModel.displayName}")
            if (viewModel.profileUsername.isNotBlank()) append(" (${getUsernameWithAdd()})")
            append(", yuk! \uD83D\uDE0D")
        }

        val linkerShareData = DataMapper.getLinkerShareData(
            LinkerData().apply {
                type = LinkerData.USER_PROFILE_SOCIAL
                uri = viewModel.profileWebLink
                id = viewModel.profileUsername
                // set and share in the Linker Data
                feature = shareModel.feature
                channel = shareModel.channel
                campaign = shareModel.campaign
                ogTitle = if (viewModel.profileUsername.isBlank()) {
                    viewModel.displayName
                } else {
                    "${viewModel.displayName} (${getUsernameWithAdd()})"
                }
                ogDescription = "${viewModel.totalFollower} Follower ${viewModel.totalFollowing} Following ${viewModel.totalPost} Post"
                if (shareModel.ogImgUrl != null && shareModel.ogImgUrl?.isNotEmpty() == true) {
                    ogImageUrl = shareModel.ogImgUrl
                }
            }
        )
        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(
                0,
                linkerShareData,
                object : ShareCallback {
                    override fun urlCreated(linkerShareData: LinkerShareResult?) {
                        context?.let {
                            val shareString = desc + "\n" + linkerShareData?.shareUri
                            SharingUtil.executeShareIntent(
                                shareModel,
                                linkerShareData,
                                activity,
                                view,
                                shareString
                            )
                            // send gtm trackers if you want to

                            when (UniversalShareBottomSheet.getShareBottomSheetType()) {
                                UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET -> {
                                    userProfileTracker.clickChannelScreenshotShareBottomsheet(userSession.userId, self = viewModel.isSelfProfile)
                                }
                                UniversalShareBottomSheet.CUSTOM_SHARE_SHEET -> {
                                    shareModel.channel?.let { it1 ->
                                        userProfileTracker.clickShareChannel(userSession.userId, self = viewModel.isSelfProfile, it1)
                                    }
                                }
                            }
                            universalShareBottomSheet?.dismiss()
                        }
                    }

                    override fun onError(linkerError: LinkerError?) {
                        // Most of the error cases are already handled for you. Let me know if you want to add your own error handling.
                    }
                }
            )
        )
    }

    override fun screenShotTaken() {
        showUniversalShareBottomSheet()
        userProfileTracker.viewScreenshotShareBottomsheet(userSession.userId, self = viewModel.isSelfProfile)
        // add tracking for the screenshot bottom sheet
    }

    override fun permissionAction(action: String, label: String) {
        // add tracking for the permission dialog for screenshot sharing
        userProfileTracker.clickAccessMedia(userSession.userId, self = viewModel.isSelfProfile, label)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenShotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    override fun onCloseOptionClicked() {
//        TODO gtm tracking
        // This method will be mostly used for GTM Tracking stuff. So add the tracking accordingly
        // this will give you the bottomsheet type : if it's screenshot or general
        when (UniversalShareBottomSheet.getShareBottomSheetType()) {
            UniversalShareBottomSheet.SCREENSHOT_SHARE_SHEET -> {
                userSession.userId.let { userProfileTracker.clickCloseScreenshotShareBottomsheet(it, self = viewModel.isSelfProfile) }
            }
            UniversalShareBottomSheet.CUSTOM_SHARE_SHEET -> {
                userSession.userId.let { userProfileTracker.clickCloseShareButton(it, self = viewModel.isSelfProfile) }
            }
        }
    }

    override fun expandFab() {
        if (!mainBinding.fabUp.menuOpen) mainBinding.fabUserProfile.expand()
    }

    override fun shrinkFab() {
        mainBinding.fabUserProfile.shrink()
    }

    override fun onChildRefresh() {
        /** Not yet implemented */
    }

    override fun updateVideoTabSelectedChipValue(chipValue: String) {
        /** Not yet implemented */
    }

    /**
     * Temporary report feature
     */
    private fun goToTopChatReport() {
        val reportUrl = URLEncoder.encode("${TokopediaUrl.getInstance().WEB}chat/report/47012163?isSeller=1", "UTF-8")
        context?.let {
            val reportIntent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.TOPCHAT_REPORT).apply {
                putExtra(KEY_URL, reportUrl)
                putExtra(KEY_TITLEBAR, true)
                putExtra(KEY_ALLOW_OVERRIDE, true)
                putExtra(KEY_NEED_LOGIN, false)
                putExtra(KEY_TITLE, "")
            }
            startActivity(reportIntent)
        }
    }

    companion object {
        const val PAGE_NAME_PROFILE = "UserProfile"
        const val FEATURE_SHARE = "share"
        const val EXTRA_DISPLAY_NAME = "display_name"
        const val EXTRA_TOTAL_FOLLOWERS = "total_followers"
        const val EXTRA_TOTAL_FOLLOWINGS = "total_following"
        const val EXTRA_USER_NAME = "user_name"
        const val EXTRA_USER_ID = "userid"
        const val EXTRA_PROFILE_USER_ID = "profileUserid"
        const val EXTRA_IS_FOLLOWERS = "is_followers"
        const val APPLINK_MENU = "tokopedia://navigation/main"
        const val REQUEST_CODE_PLAY_ROOM = 123
        const val REQUEST_CODE_LOGIN_TO_FOLLOW = 1
        const val REQUEST_CODE_LOGIN_TO_SET_REMINDER = 2
        const val REQUEST_CODE_EDIT_PROFILE = 2423
        const val REQUEST_CODE_USER_PROFILE = 99
        const val EXTRA_POSITION_OF_PROFILE = "profile_position"
        const val EXTRA_FOLLOW_UNFOLLOW_STATUS = "follow_unfollow_status"
        const val EXTRA_VALUE_IS_FOLLOWED = "is_followed"
        const val EXTRA_VALUE_IS_NOT_FOLLOWED = "is_not_followed"

        const val LOADING = -94567
        const val PAGE_CONTENT = 0
        const val PAGE_ERROR = 2
        const val PAGE_LOADING = 1
        const val PAGE_EMPTY = 3
        const val SEE_ALL_LINE = 3
        const val MAX_LINE = 20

        private const val KEY_APPLINK_AFTER_CAMERA_CAPTURE = "link_cam"
        private const val KEY_MAX_MULTI_SELECT_ALLOWED = "max_multi_select"
        private const val KEY_MAX_MULTI_SELECT_ALLOWED_VALUE = 5
        private const val KEY_TITLE = "title"
        private const val KEY_APPLINK_FOR_GALLERY_PROCEED = "link_gall"
        private const val KEY_IS_CREATE_POST_AS_BUYER = "is_create_post_as_buyer"
        private const val VALUE_IS_OPEN_FROM_USER_PROFILE = "is_open_from_user_profile"

        private const val TAG = "UserProfileFragment"

        private const val KEY_URL = "url"
        private const val KEY_TITLEBAR = "titlebar"
        private const val KEY_ALLOW_OVERRIDE = "allow_override"
        private const val KEY_NEED_LOGIN = "need_login"

        private const val REQUEST_CODE_LOGIN = 551

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): UserProfileFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? UserProfileFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                UserProfileFragment::class.java.name
            ).apply {
                arguments = bundle
            } as UserProfileFragment
        }
    }
}
