package com.tokopedia.people.views.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.tokopedia.content.common.navigation.people.UserProfileActivityResult
import com.tokopedia.content.common.util.activitycontract.ContentActivityResultContracts.OpenAppLink
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.components.tabs.NestTabs
import com.tokopedia.nest.components.tabs.NestTabsConfig
import com.tokopedia.nest.components.tabs.TabConfig
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.people.R
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.utils.LoginListener
import com.tokopedia.people.utils.rememberLoginToFollowHelper
import com.tokopedia.people.utils.resId
import com.tokopedia.people.viewmodels.FollowListViewModel
import com.tokopedia.people.views.compose.FollowListErrorLayout
import com.tokopedia.people.views.compose.FollowListLayout
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.PeopleUiModel
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.people.views.uimodel.id
import com.tokopedia.people.views.uimodel.isFollowed
import com.tokopedia.people.views.uimodel.isMySelf
import com.tokopedia.people.views.uimodel.state.FollowListEvent
import com.tokopedia.people.views.uimodel.state.FollowListState
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.shop.common.util.ShopPageActivityResult
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
internal fun FollowingFollowerListScreen(
    profileName: String,
    totalFollowersFmt: String,
    totalFollowingsFmt: String,
    onPageChanged: (FollowListType) -> Unit,
    onBackClicked: () -> Unit,
    onListRefresh: () -> Unit,
    followListViewModel: (FollowListType) -> FollowListViewModel,
    loginListener: LoginListener,
    tracker: UserProfileTracker,
    initialSelectedTabType: FollowListType,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(initialSelectedTabType.ordinal)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect {
                onPageChanged(
                    if (it == FollowListType.Follower.ordinal) {
                        FollowListType.Follower
                    } else {
                        FollowListType.Following
                    }
                )
            }
    }

    Column(
        modifier.semantics {
            testTagsAsResourceId = true
        }.background(NestTheme.colors.NN._0)
    ) {
        NestHeader(
            type = NestHeaderType.SingleLine(title = profileName, onBackClicked = onBackClicked),
            modifier = Modifier.fillMaxWidth().resId("header_follower")
        )
        Box(
            Modifier
                .zIndex(2f)
                .background(NestTheme.colors.NN._0)
                .resId("tp_follow")
        ) {
            NestTabs(
                config = NestTabsConfig(
                    selectedIndex = pagerState.currentPage,
                    mode = NestTabsConfig.Mode.FIXED,
                    tabConfigs = listOf(
                        TabConfig(
                            iconConfig = TabConfig.IconConfig.NoIcon,
                            text = "$totalFollowersFmt Followers",
                            counter = -1,
                            dotted = false,
                            showNewLabel = false,
                            enabled = true
                        ),
                        TabConfig(
                            iconConfig = TabConfig.IconConfig.NoIcon,
                            text = "$totalFollowingsFmt Following",
                            counter = -1,
                            dotted = false,
                            showNewLabel = false,
                            enabled = true
                        )
                    ).toImmutableList(),
                    onTabClicked = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                )
            )
        }

        HorizontalPager(
            count = FollowListType.values.size,
            state = pagerState,
            key = { page ->
                when (page) {
                    0 -> FollowListType.Follower
                    1 -> FollowListType.Following
                    else -> Unit
                }
            },
            modifier = Modifier.resId("view_pager")
        ) { page ->
            when (page) {
                0 -> {
                    val viewModel = remember { followListViewModel(FollowListType.Follower) }
                    FollowListScreen(viewModel, loginListener, tracker, onListRefresh)
                }
                1 -> {
                    val viewModel = remember { followListViewModel(FollowListType.Following) }
                    FollowListScreen(viewModel, loginListener, tracker, onListRefresh)
                }
            }
        }
    }
}

@Composable
internal fun FollowListScreen(
    viewModel: FollowListViewModel,
    loginListener: LoginListener,
    tracker: UserProfileTracker,
    onListRefresh: () -> Unit
) {
    LaunchedEffect(loginListener) {
        loginListener.observe {
            viewModel.onAction(FollowListAction.Refresh)
        }
    }

    val userAppLinkLauncher = rememberLauncherForActivityResult(OpenAppLink()) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val intent = result.intent ?: return@rememberLauncherForActivityResult
        val isFollow = UserProfileActivityResult.isFollow(intent)
        val userId = UserProfileActivityResult.getUserId(intent)

        viewModel.onAction(
            FollowListAction.UpdateUserFollowFromResult(userId, isFollow)
        )
    }

    val shopAppLinkLauncher = rememberLauncherForActivityResult(OpenAppLink()) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val intent = result.intent ?: return@rememberLauncherForActivityResult
        val isFollow = ShopPageActivityResult.isFollow(intent)
        val shopId = ShopPageActivityResult.getShopId(intent)

        viewModel.onAction(
            FollowListAction.UpdateShopFollowFromResult(shopId, isFollow)
        )
    }

    val loginToFollowHelper = rememberLoginToFollowHelper {
        viewModel.onAction(FollowListAction.Follow(it))
    }

    fun onPeopleClicked(people: PeopleUiModel) {
        tracker.clickUserFollowers(people.id, people.isMySelf)

        when (people) {
            is PeopleUiModel.UserUiModel -> userAppLinkLauncher.launch(people.appLink)
            is PeopleUiModel.ShopUiModel -> shopAppLinkLauncher.launch(people.appLink)
        }
    }

    fun onFollowClicked(people: PeopleUiModel) {
        if (people.isFollowed) {
            when (viewModel.type) {
                FollowListType.Follower -> {
                    tracker.clickUnfollowFromFollowers(people.id, people.isMySelf)
                }
                FollowListType.Following -> {
                    tracker.clickUnfollowFromFollowing(people.id, people.isMySelf)
                }
            }
        } else {
            when (viewModel.type) {
                FollowListType.Follower -> {
                    tracker.clickFollowFromFollowers(people.id, people.isMySelf)
                }
                FollowListType.Following -> {
                    tracker.clickFollowFromFollowing(people.id, people.isMySelf)
                }
            }
        }

        viewModel.onAction(FollowListAction.Follow(people))
    }

    fun onLoadMore() {
        viewModel.onAction(FollowListAction.LoadMore)
    }

    fun onRefresh() {
        viewModel.onAction(FollowListAction.Refresh)
        onListRefresh()
    }

    val state: FollowListState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val toaster = rememberPlayToaster()

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            if (event == null) return@collect
            when (event) {
                is FollowListEvent.SuccessFollow -> {
                    toaster.showToaster(event.message)
                }
                is FollowListEvent.FailedFollow -> {
                    toaster.showError(
                        IllegalStateException(),
                        customErrMessage = context.getString(
                            if (event.isGoingToFollow) {
                                R.string.up_error_follow
                            } else {
                                R.string.up_error_unfollow
                            }
                        )
                    )
                }
                is FollowListEvent.LoginToFollow -> {
                    loginToFollowHelper.launch(event.people)
                }
            }
            viewModel.onAction(FollowListAction.ConsumeEvent(event))
        }
    }

    FollowListScreen(
        state = state,
        onPeopleClicked = ::onPeopleClicked,
        onFollowClicked = ::onFollowClicked,
        onLoadMore = ::onLoadMore,
        onRefresh = ::onRefresh
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FollowListScreen(
    state: FollowListState,
    onPeopleClicked: (PeopleUiModel) -> Unit,
    onFollowClicked: (PeopleUiModel) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = onRefresh
    )

    if (state.result?.isFailure == true && state.followList.isEmpty()) {
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState())
        ) {
            FollowListErrorLayout(
                isLoading = state.isLoading,
                onRefreshButtonClicked = onRefresh,
                modifier = Modifier.resId("ge_followers")
            )

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter).resId("swipe_refresh_layout")
            )
        }
    } else {
        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            FollowListLayout(
                people = state.followList.toImmutableList(),
                hasNextPage = state.hasNextPage,
                onLoadMore = onLoadMore,
                onPeopleClicked = onPeopleClicked,
                onFollowClicked = onFollowClicked,
                Modifier.fillMaxSize().resId("rv_followers")
            )

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter).resId("swipe_refresh_layout")
            )
        }
    }
}

@Composable
private fun rememberPlayToaster(): PlayToaster {
    val lifecycleOwner = LocalLifecycleOwner.current
    val view = LocalView.current

    return remember { PlayToaster(view, lifecycleOwner) }
}
