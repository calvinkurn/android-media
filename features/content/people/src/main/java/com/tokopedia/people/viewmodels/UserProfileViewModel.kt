package com.tokopedia.people.viewmodels

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.combine
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.FOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.LOADING_FOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.LOADING_UNFOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.UNFOLLOW
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.utils.UserProfileSharedPref
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.FollowInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileCreationInfoUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel
import com.tokopedia.people.views.uimodel.profile.ProfileType
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import com.tokopedia.people.views.uimodel.getReviewSettings
import com.tokopedia.people.views.uimodel.mapper.UserProfileLikeStatusMapper
import com.tokopedia.people.views.uimodel.profile.ProfileTabState
import com.tokopedia.people.views.uimodel.saved.SavedReminderData
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel @AssistedInject constructor(
    @Assisted private val username: String,
    private val repo: UserProfileRepository,
    private val followRepo: UserFollowRepository,
    private val userSession: UserSessionInterface,
    private val userProfileSharedPref: UserProfileSharedPref,
    dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    @AssistedFactory
    interface Factory {
        fun create(username: String): UserProfileViewModel
    }

    /** Public Getter */
    val displayName: String
        get() = _profileInfo.value.name

    val firstName: String
        get() = _profileInfo.value.name.trim().split(SPACE).firstOrNull().orEmpty()

    val profileUserID: String
        get() = _profileInfo.value.userID

    val profileUserEncryptedID: String
        get() = _profileInfo.value.encryptedUserID

    val profileUsername: String
        get() = _profileInfo.value.username

    val profileCover: String
        get() = _profileInfo.value.imageCover

    val isBlocking: Boolean
        get() = _profileInfo.value.isBlocking

    val totalFollower: String
        get() = _profileInfo.value.stats.totalFollowerFmt

    val totalFollowing: String
        get() = _profileInfo.value.stats.totalFollowingFmt

    val totalPost: String
        get() = _profileInfo.value.stats.totalPostFmt

    val isFollowed: Boolean
        get() = _followInfo.value.status

    val isSelfProfile: Boolean
        get() = _profileType.value == ProfileType.Self

    val profileWebLink: String
        get() = _profileInfo.value.shareLink.webLink

    val isShopRecomShow: Boolean get() = _shopRecom.value.isShown

    val isShortVideoEntryPointShow: Boolean get() = _creationInfo.value.showShortVideo

    val profileTab: ProfileTabUiModel
        get() = when (val state = _profileTab.value) {
            is ProfileTabState.Success -> state.profileTab
            else -> ProfileTabUiModel()
        }

    private val isFirstTimeSeeReviewTab: Boolean
        get() = isSelfProfile &&
            _reviewSettings.value.isEnabled &&
            !userProfileSharedPref.hasBeenShown(UserProfileSharedPref.Key.ReviewOnboarding)

    private val _savedReminderData = MutableStateFlow<SavedReminderData>(SavedReminderData.NoData)
    private val _profileInfo = MutableStateFlow(ProfileUiModel.Empty)
    private val _followInfo = MutableStateFlow(FollowInfoUiModel.Empty)
    private val _creationInfo = MutableStateFlow(ProfileCreationInfoUiModel())
    private val _profileType = MutableStateFlow(ProfileType.Unknown)
    private val _shopRecom = MutableStateFlow(ShopRecomUiModel())
    private val _profileTab = MutableStateFlow<ProfileTabState>(ProfileTabState.Unknown)
    private val _feedPostsContent = MutableStateFlow(UserFeedPostsUiModel())
    private val _videoPostContent = MutableStateFlow(UserPlayVideoUiModel.Empty)
    private val _reviewContent = MutableStateFlow(UserReviewUiModel.Empty)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<Throwable?>(null)
    private val _reviewSettings = MutableStateFlow(ProfileSettingsUiModel.Empty)
    private val _likePool = MutableStateFlow(hashMapOf<String, Boolean>())

    private val _uiEvent = MutableSharedFlow<UserProfileUiEvent>()

    val uiEvent: Flow<UserProfileUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _profileInfo,
        _followInfo,
        _profileType,
        _creationInfo,
        _shopRecom,
        _profileTab,
        _feedPostsContent,
        _videoPostContent,
        _reviewContent,
        _isLoading,
        _error,
        _reviewSettings,
    ) { profileInfo,
        followInfo,
        profileType,
        creationInfo,
        shopRecom,
        profileTab,
        feedPostsContent,
        videoPostContent,
        reviewContent,
        isLoading,
        error,
        reviewSettings ->
        UserProfileUiState(
            profileInfo = profileInfo,
            followInfo = followInfo,
            profileType = profileType,
            creationInfo = creationInfo,
            shopRecom = shopRecom,
            profileTab = profileTab,
            feedPostsContent = feedPostsContent,
            videoPostsContent = videoPostContent,
            reviewContent = reviewContent,
            isLoading = isLoading,
            error = error,
            reviewSettings = reviewSettings
        )
    }

    init {
        launch {
            _likePool
                .debounce(LIKE_REVIEW_DEBOUNCE)
                .collect { likePool ->

                    if (likePool.entries.isEmpty()) return@collect

                    likePool.entries.forEach {  map ->
                        submitAction(UserProfileAction.ProcessLikeRequest(map.key, map.value))
                    }

                    _likePool.update { hashMapOf() }
                }
        }
    }

    fun submitAction(action: UserProfileAction) {
        when (action) {
            is UserProfileAction.ClickFollowButton -> handleClickFollowButton(action.isFromLogin)
            is UserProfileAction.ClickFollowButtonShopRecom -> handleClickFollowButtonShopRecom(
                action.itemID
            )
            is UserProfileAction.ClickUpdateReminder -> handleClickUpdateReminder(action.isFromLogin)
            is UserProfileAction.LoadFeedPosts -> handleLoadFeedPosts(action.cursor, action.isRefresh)
            is UserProfileAction.LoadPlayVideo -> handleLoadPlayVideo(action.isRefresh)
            is UserProfileAction.LoadUserReview -> handleLoadUserReview(action.isRefresh)
            is UserProfileAction.LoadProfile -> handleLoadProfile(action.isRefresh)
            is UserProfileAction.LoadNextPageShopRecom -> handleLoadNextPageShopRecom(action.nextCurSor)
            is UserProfileAction.RemoveShopRecomItem -> handleRemoveShopRecomItem(action.itemID)
            is UserProfileAction.SaveReminderActivityResult -> handleSaveReminderActivityResult(action.channel)
            UserProfileAction.BlockUser -> handleBlockUser()
            UserProfileAction.UnblockUser -> handleUnblockUser()
            is UserProfileAction.DeletePlayChannel -> handleDeletePlayChannel(action.channelId)
            is UserProfileAction.UpdatePlayChannelInfo -> handleUpdatePlayChannelInfo(action.channelId, action.totalView, action.isReminderSet)
            is UserProfileAction.ClickPlayVideoMenuAction -> handleClickPlayVideoMenuAction(action.channel)
            is UserProfileAction.ClickCopyLinkPlayChannel -> handleClickCopyLinkPlayChannel(action.channel)
            is UserProfileAction.ClickSeePerformancePlayChannel -> handleClickSeePerformancePlayChannel(action.channel)
            is UserProfileAction.ClickDeletePlayChannel -> handleClickDeletePlayChannel(action.channel)
            is UserProfileAction.ClickLikeReview -> handleClickLikeReview(action.review)
            is UserProfileAction.ProcessLikeRequest -> handleProcessLikeRequest(action.feedbackID, action.isLike)
            is UserProfileAction.ClickReviewTextSeeMore -> handleClickReviewTextSeeMore(action.review)
            is UserProfileAction.ClickProductInfo -> handleClickProductInfo(action.review)
            is UserProfileAction.ClickReviewMedia -> handleClickReviewMedia(action.feedbackID, action.attachment)
            is UserProfileAction.UpdateLikeStatus -> handleUpdateLikeStatus(action.feedbackId, action.likeStatus)
            else -> {}
        }
    }

    /** Handle Action */
    private fun handleLoadProfile(isRefresh: Boolean) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launchCatchError(block = {
            loadProfileInfo(isRefresh)
            _isLoading.value = false
            _error.value = null
        }) {
            _isLoading.value = false
            _error.value = it
            _uiEvent.emit(UserProfileUiEvent.ErrorLoadProfile(it))
        }
    }

    private fun handleLoadFeedPosts(cursor: String, isRefresh: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                val currentSize = _feedPostsContent.value.posts.size
                val data = if (isRefresh) {
                    repo.getFeedPosts(profileUserID, "", if (currentSize == 0) DEFAULT_LIMIT else currentSize)
                } else {
                    repo.getFeedPosts(profileUserID, cursor, DEFAULT_LIMIT)
                }

                val finalPosts = (
                    if (cursor.isEmpty() || isRefresh) {
                        data.posts
                    } else {
                        _feedPostsContent.value.posts + data.posts
                    }
                    ).distinctBy { it.id }

                _feedPostsContent.update {
                    it.copy(
                        pagination = data.pagination,
                        posts = finalPosts
                    )
                }
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorFeedPosts(it))
            }
        )
    }

    private fun handleLoadPlayVideo(isRefresh: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                val currVideoPostModel = _videoPostContent.value

                if (!isRefresh && currVideoPostModel.nextCursor.isEmpty()) return@launchCatchError
                if (currVideoPostModel.isLoading) return@launchCatchError

                _videoPostContent.update {
                    it.copy(
                        status = UserPlayVideoUiModel.Status.Loading,
                        items = if (isRefresh) emptyList() else it.items
                    )
                }

                val cursor = if (isRefresh) "" else currVideoPostModel.nextCursor
                val result = repo.getPlayVideo(profileUserID, cursor, isSelfProfile)

                _videoPostContent.update {
                    it.copy(
                        items = it.items + result.items,
                        nextCursor = result.nextCursor,
                        status = UserPlayVideoUiModel.Status.Success
                    )
                }
            },
            onError = {
                _videoPostContent.update { videoPostContent ->
                    videoPostContent.copy(status = UserPlayVideoUiModel.Status.Error)
                }
                _uiEvent.emit(UserProfileUiEvent.ErrorVideoPosts(it))
            }
        )
    }

    private fun handleLoadUserReview(isRefresh: Boolean) {
        viewModelScope.launchCatchError(block = {
            val currReviewContent = _reviewContent.value

            if (currReviewContent.isLoading) return@launchCatchError
            if (!isRefresh && !currReviewContent.hasNext) return@launchCatchError

            if (isRefresh) {
                _reviewSettings.update {
                    repo.getProfileSettings(_profileInfo.value.userID).getReviewSettings()
                }

                _uiEvent.emit(UserProfileUiEvent.SendPendingTracker)
            }
            if (!_reviewSettings.value.isEnabled) return@launchCatchError

            _reviewContent.update {
                it.copy(
                    status = UserReviewUiModel.Status.Loading,
                    reviewList = if (isRefresh) emptyList() else it.reviewList,
                    page = if (isRefresh) 1 else it.page,
                    hasNext = if (isRefresh) true else it.hasNext
                )
            }

            val response = repo.getUserReviewList(
                userID = profileUserID,
                limit = DEFAULT_LIMIT,
                page = _reviewContent.value.page
            )

            _reviewContent.update {
                it.copy(
                    reviewList = it.reviewList + response.reviewList,
                    page = response.page,
                    hasNext = response.hasNext,
                    status = response.status,
                )
            }
        }) {
            _reviewContent.update { userReview ->
                userReview.copy(status = UserReviewUiModel.Status.Error)
            }

            _uiEvent.emit(UserProfileUiEvent.ErrorLoadReview(it))
        }
    }

    private fun handleBlockUser() {
        viewModelScope.launchCatchError(block = {
            repo.blockUser(profileUserID)
            _profileInfo.update { it.copy(isBlocking = true) }
            _uiEvent.emit(UserProfileUiEvent.SuccessBlockUser(isBlocking = true))
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorBlockUser(isBlocking = true))
        }
    }

    private fun handleUnblockUser() {
        viewModelScope.launchCatchError(block = {
            repo.unblockUser(profileUserID)
            _profileInfo.update { it.copy(isBlocking = false) }
            _uiEvent.emit(UserProfileUiEvent.SuccessBlockUser(isBlocking = false))
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorBlockUser(isBlocking = false))
        }
    }

    private fun handleClickFollowButton(isFromLogin: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                if (isFromLogin) loadProfileInfo(false)

                val followInfo = _followInfo.value

                if (userSession.isLoggedIn.not() || (isFollowed && isFromLogin) || isSelfProfile) {
                    return@launchCatchError
                }

                when (
                    val result = followRepo.followUser(
                        encryptedUserId = followInfo.encryptedUserID,
                        follow = !followInfo.status
                    )
                ) {
                    is MutationUiModel.Success -> {
                        _followInfo.update { it.copy(status = !followInfo.status) }
                        _profileInfo.update { repo.getProfile(followInfo.userID) }
                    }
                    is MutationUiModel.Error -> {
                        _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(result.message)))
                    }
                }
            }
        ) {
            _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable()))
        }
    }

    private fun handleClickUpdateReminder(isFromLogin: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                if (isFromLogin) loadProfileInfo(false)

                val data = _savedReminderData.value

                if (data is SavedReminderData.Saved) {
                    val reminderTypeAfterAction = data.channel.reminderType.switch()
                    val result = repo.updateReminder(data.channel.channelId, reminderTypeAfterAction.reminded)

                    when (result) {
                        is MutationUiModel.Success -> {
                            handleRemoveReminderActivityResult()
                            updatePartialChannelInfo(data.channel.channelId) { channel ->
                                channel.copy(
                                    reminderType = reminderTypeAfterAction
                                )
                            }

                            _uiEvent.emit(
                                UserProfileUiEvent.SuccessUpdateReminder(result.message)
                            )
                        }
                        is MutationUiModel.Error -> {
                            _uiEvent.emit(
                                UserProfileUiEvent.ErrorUpdateReminder(Exception(result.message))
                            )
                        }
                    }
                }
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorUpdateReminder(it))
            }
        )
    }

    private fun handleSaveReminderActivityResult(channel: PlayWidgetChannelUiModel) {
        _savedReminderData.update {
            SavedReminderData.Saved(channel)
        }
    }

    private fun handleRemoveReminderActivityResult() {
        _savedReminderData.update { SavedReminderData.NoData }
    }

    private fun handleClickFollowButtonShopRecom(itemId: Long) {
        val currentItem = _shopRecom.value.items.find { it.id == itemId } ?: return
        val currentState =
            if (currentItem.state == LOADING_FOLLOW || currentItem.state == LOADING_UNFOLLOW) {
                return
            } else {
                currentItem.state
            }
        val isCurrentStateFollow = currentState == FOLLOW
        val loadingState = if (isCurrentStateFollow) LOADING_FOLLOW else LOADING_UNFOLLOW
        val followInfo = _followInfo.value

        viewModelScope.launchCatchError(
            block = {
                updateLoadingStateFollowShopRecom(itemId, loadingState)

                val result = when (currentItem.type) {
                    FOLLOW_TYPE_SHOP -> {
                        followRepo.followShop(
                            currentItem.id.toString(),
                            ShopFollowAction.getActionByState(currentState == FOLLOW)
                        )
                    }
                    FOLLOW_TYPE_BUYER -> {
                        followRepo.followUser(
                            encryptedUserId = currentItem.encryptedID,
                            follow = currentState == UNFOLLOW
                        )
                    }
                    else -> return@launchCatchError
                }

                when (result) {
                    is MutationUiModel.Success -> {
                        _profileInfo.update { repo.getProfile(followInfo.userID) }
                        _shopRecom.update { data ->
                            data.copy(
                                items = data.items.map {
                                    if (currentItem.id == it.id) {
                                        it.copy(state = if (currentState == FOLLOW) UNFOLLOW else FOLLOW)
                                    } else {
                                        it
                                    }
                                }
                            )
                        }
                    }
                    is MutationUiModel.Error -> {
                        updateLoadingStateFollowShopRecom(itemId, currentState)
                        _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(result.message)))
                    }
                }
            },
            onError = {
                updateLoadingStateFollowShopRecom(itemId, currentState)
                _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable()))
            }
        )
    }

    private fun updateLoadingStateFollowShopRecom(itemID: Long, state: ShopRecomFollowState) {
        _shopRecom.update { data ->
            data.copy(
                items = data.items.map {
                    if (itemID == it.id) {
                        it.copy(state = state)
                    } else {
                        it
                    }
                }
            )
        }
    }

    private fun handleRemoveShopRecomItem(itemID: Long) {
        _shopRecom.update { data ->
            data.copy(items = data.items.filterNot { it.id == itemID })
        }
    }

    private fun handleDeletePlayChannel(channelId: String) {
        launchCatchError(block = {
            val channelId = repo.deletePlayChannel(channelId, _profileInfo.value.userID)

            _videoPostContent.update {
                it.copy(
                    items = it.items.filter { item -> item.channelId != channelId }
                )
            }
            _uiEvent.emit(UserProfileUiEvent.SuccessDeleteChannel)
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorDeleteChannel(it))
        }
    }

    private fun handleUpdatePlayChannelInfo(channelId: String, totalView: String, isReminderSet: Boolean) {
        launchCatchError(block = {
            val channels = _videoPostContent.value.items
            val currChannel = channels.firstOrNull { it.channelId == channelId } ?: return@launchCatchError

            val currTotalView = currChannel.totalView.totalViewFmt
            val currIsReminderSet = currChannel.reminderType.reminded

            if (totalView.isNotEmpty() && totalView != currTotalView) {
                updatePartialChannelInfo(channelId) { channel ->
                    channel.copy(
                        totalView = channel.totalView.copy(
                            totalViewFmt = totalView
                        )
                    )
                }
            } else if (isReminderSet != currIsReminderSet) {
                updatePartialChannelInfo(channelId) { channel ->
                    channel.copy(
                        reminderType = if (isReminderSet) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded
                    )
                }
            }
        }) { }
    }

    private fun handleClickPlayVideoMenuAction(channel: PlayWidgetChannelUiModel) {
        launchCatchError(block = {
            _uiEvent.emit(UserProfileUiEvent.OpenPlayVideoActionMenu(channel))
        }) {}
    }

    private fun handleClickCopyLinkPlayChannel(channel: PlayWidgetChannelUiModel) {
        launchCatchError(block = {
            _uiEvent.emit(UserProfileUiEvent.CopyLinkPlayVideo(channel.share.fullShareContent))
        }) {}
    }

    private fun handleClickSeePerformancePlayChannel(channel: PlayWidgetChannelUiModel) {
        launchCatchError(block = {
            _uiEvent.emit(UserProfileUiEvent.OpenPerformancePlayChannel(channel.performanceSummaryLink))
        }) {}
    }

    private fun handleClickDeletePlayChannel(channel: PlayWidgetChannelUiModel) {
        launchCatchError(block = {
            _uiEvent.emit(UserProfileUiEvent.ShowDeletePlayVideoConfirmationDialog(channel))
        }) {}
    }

    private fun handleClickLikeReview(review: UserReviewUiModel.Review) {
        launch {
            /** Immediate follow / unfollow feedback to user */
            val likeDislikeAfterToggle = toggleLikeDislikeStatus(review.feedbackID) ?: return@launch

            _likePool.update { likePool ->
                HashMap(likePool).apply {
                    if (this.contains(review.feedbackID))
                        remove(review.feedbackID)
                    else
                        put(review.feedbackID, likeDislikeAfterToggle.isLike)
                }
            }
        }
    }

    private fun handleProcessLikeRequest(feedbackId: String, isLike: Boolean) {
        viewModelScope.launchCatchError(block = {

            _reviewContent.value.reviewList.firstOrNull { it.feedbackID == feedbackId } ?: return@launchCatchError

            val response = repo.setLikeStatus(
                feedbackID = feedbackId,
                isLike = isLike,
            )

            /** Sync follow / unfollow status from BE */
            _reviewContent.update {
                it.copy(
                    reviewList = it.reviewList.map { item ->
                        if (feedbackId == item.feedbackID) {
                            item.copy(likeDislike = response)
                        } else {
                            item
                        }
                    }
                )
            }
        }) { throwable ->
            toggleLikeDislikeStatus(feedbackId)

            _uiEvent.emit(UserProfileUiEvent.ErrorLikeDislike(throwable))
        }
    }

    private fun handleClickReviewTextSeeMore(review: UserReviewUiModel.Review) {
        launch {
            _reviewContent.update {
                it.copy(
                    reviewList = it.reviewList.map { item ->
                        if (item.feedbackID == review.feedbackID) {
                            item.copy(isReviewTextExpanded = true)
                        } else {
                            item
                        }
                    }
                )
            }
        }
    }

    private fun handleClickProductInfo(review: UserReviewUiModel.Review) {
        launch {
            _uiEvent.emit(UserProfileUiEvent.OpenProductDetailPage(review.product.productID))
        }
    }

    private fun handleClickReviewMedia(feedbackID: String, attachment: UserReviewUiModel.Attachment) {
        launch {
            val review = _reviewContent.value.reviewList.find { it.feedbackID == feedbackID } ?: return@launch
            val mediaPosition = review.attachments.indexOf(attachment) + 1

            _uiEvent.emit(
                UserProfileUiEvent.OpenReviewMediaGalleryPage(
                    review = review,
                    mediaPosition = mediaPosition,
                )
            )
        }
    }

    private fun handleUpdateLikeStatus(feedbackId: String, likeStatus: Int) {
        val review = _reviewContent.value.reviewList.firstOrNull { it.feedbackID == feedbackId } ?: return

        if (review.likeDislike.isLike != UserProfileLikeStatusMapper.isLike(likeStatus)) {
            toggleLikeDislikeStatus(feedbackId)
        }
    }

    /** Helper */
    private suspend fun loadProfileInfo(isRefresh: Boolean) {
        val profileInfo = repo.getProfile(username)

        val profileType = if (userSession.isLoggedIn) {
            if (userSession.userId == profileInfo.userID) {
                ProfileType.Self
            } else {
                ProfileType.OtherUser
            }
        } else {
            ProfileType.NotLoggedIn
        }

        val followInfo = if (userSession.isLoggedIn) {
            repo.getFollowInfo(listOf(profileInfo.userID))
        } else {
            FollowInfoUiModel.Empty
        }

        _profileInfo.update { profileInfo }
        _followInfo.update { followInfo }
        _profileType.update { profileType }

        if (isBlocking) {
            _feedPostsContent.value = UserFeedPostsUiModel()
            viewModelScope.launch {
                _uiEvent.emit(
                    UserProfileUiEvent.BlockingUserState(
                        MessageErrorException("User ini diblokir")
                    )
                )
            }
            return
        }

        if (profileType == ProfileType.Self) _creationInfo.update { repo.getCreationInfo() }

        if (isRefresh) loadProfileTab()
    }

    private suspend fun loadProfileTab() {
        viewModelScope.launchCatchError(
            block = {
                _reviewSettings.update { repo.getProfileSettings(_profileInfo.value.userID).getReviewSettings() }

                val result = repo.getUserProfileTab(_profileInfo.value.userID)
                val isEmpty = result == ProfileTabUiModel()

                _profileTab.update {
                    ProfileTabState.Success(
                        result.copy(
                            tabs = if (isFirstTimeSeeReviewTab) {
                                result.tabs.map { tab ->
                                    tab.copy(isNew = tab.key == ProfileTabUiModel.Key.Review)
                                }
                            } else {
                                result.tabs
                            },
                            showTabs = result.showTabs
                        )
                    )
                }

                if (isFirstTimeSeeReviewTab) {
                    viewModelScope.launch {
                        delay(DELAY_SHOW_REVIEW_ONBOARDING)

                        userProfileSharedPref.setHasBeenShown(UserProfileSharedPref.Key.ReviewOnboarding)
                        _uiEvent.emit(UserProfileUiEvent.ShowReviewOnboarding)
                    }
                }

                if (isEmpty && isSelfProfile) loadShopRecom()
            },
            onError = { err ->
                _profileTab.update {
                    ProfileTabState.Error(err)
                }
            }
        )
    }

    private fun loadShopRecom(cursor: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val result = repo.getShopRecom(cursor)
                if (result.isShown) {
                    val items = if (cursor.isEmpty()) {
                        result.items
                    } else {
                        _shopRecom.value.items + result.items
                    }

                    _shopRecom.update {
                        it.copy(
                            isShown = true,
                            nextCursor = result.nextCursor,
                            title = result.title,
                            loadNextPage = result.loadNextPage,
                            items = items,
                            isRefresh = cursor.isEmpty()
                        )
                    }
                } else {
                    _shopRecom.update { ShopRecomUiModel() }
                }
            },
            onError = {
                _shopRecom.update { ShopRecomUiModel() }
            }
        )
    }

    private fun handleLoadNextPageShopRecom(nextCursor: String) {
        if (nextCursor.isEmpty()) return
        loadShopRecom(nextCursor)
    }

    private fun updatePartialChannelInfo(channelId: String, fn: (PlayWidgetChannelUiModel) -> PlayWidgetChannelUiModel) {
        _videoPostContent.update {
            it.copy(
                items = it.items.map { channel ->
                    if (channel.channelId == channelId) {
                        fn(channel)
                    } else {
                        channel
                    }
                }
            )
        }
    }

    private fun toggleLikeDislikeStatus(feedbackID: String): UserReviewUiModel.LikeDislike? {
        var selectedLikeDislike: UserReviewUiModel.LikeDislike? = null

        _reviewContent.update {
            it.copy(
                reviewList = it.reviewList.map { item ->
                    if (item.feedbackID == feedbackID) {
                        item.copy(
                            likeDislike = item.likeDislike.copy(
                                totalLike = if (item.likeDislike.isLike) {
                                    item.likeDislike.totalLike - 1
                                } else {
                                    item.likeDislike.totalLike + 1
                                },
                                isLike = !item.likeDislike.isLike
                            ).also { likeDislike ->
                                selectedLikeDislike = likeDislike
                            }
                        )
                    } else {
                        item
                    }
                }
            )
        }

        return selectedLikeDislike
    }

    companion object {
        const val UGC_ONBOARDING_OPEN_FROM_LIVE = 1
        const val UGC_ONBOARDING_OPEN_FROM_POST = 2
        const val UGC_ONBOARDING_OPEN_FROM_SHORTS = 3
        private const val FOLLOW_TYPE_SHOP = 2
        private const val FOLLOW_TYPE_BUYER = 3
        private const val DEFAULT_LIMIT = 10
        private const val DELAY_SHOW_REVIEW_ONBOARDING = 1000L
        private const val SPACE = " "
        private const val LIKE_REVIEW_DEBOUNCE = 500L
    }
}
