package com.tokopedia.people.views.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.tokopedia.applink.RouteManager
import com.tokopedia.header.compose.NestHeader
import com.tokopedia.header.compose.NestHeaderType
import com.tokopedia.nest.components.tabs.NestTabs
import com.tokopedia.nest.components.tabs.NestTabsConfig
import com.tokopedia.nest.components.tabs.TabConfig
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.viewmodels.FollowListViewModel
import com.tokopedia.people.views.screen.FollowListErrorScreen
import com.tokopedia.people.views.screen.FollowListScreen
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.PeopleUiModel
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.people.views.uimodel.appLink
import com.tokopedia.people.views.uimodel.id
import com.tokopedia.people.views.uimodel.isFollowed
import com.tokopedia.people.views.uimodel.isMySelf
import com.tokopedia.people.views.uimodel.state.FollowListState
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun FollowingFollowerListScreen(
    profileName: String,
    totalFollowersFmt: String,
    totalFollowingsFmt: String,
    onPageChanged: (FollowListType) -> Unit,
    onBackClicked: () -> Unit,
    onListRefresh: () -> Unit,
    followListViewModel: (FollowListType) -> FollowListViewModel,
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
        modifier.background(NestTheme.colors.NN._0)
    ) {
        NestHeader(
            type = NestHeaderType.SingleLine(title = profileName, onBackClicked = onBackClicked),
            modifier = Modifier.fillMaxWidth()
        )
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
        HorizontalPager(
            count = FollowListType.values.size,
            state = pagerState,
            key = { page ->
                when (page) {
                    0 -> FollowListType.Follower
                    1 -> FollowListType.Following
                    else -> Unit
                }
            }
        ) { page ->
            when (page) {
                0 -> {
                    val viewModel = remember { followListViewModel(FollowListType.Follower) }
                    FollowListScreen(viewModel, tracker, onListRefresh)
                }
                1 -> {
                    val viewModel = remember { followListViewModel(FollowListType.Following) }
                    FollowListScreen(viewModel, tracker, onListRefresh)
                }
            }
        }
    }
}

@Composable
internal fun FollowListScreen(
    viewModel: FollowListViewModel,
    tracker: UserProfileTracker,
    onListRefresh: () -> Unit,
) {
    val context = LocalContext.current

    fun onPeopleClicked(people: PeopleUiModel) {
        tracker.clickUserFollowers(people.id, people.isMySelf)

        RouteManager.route(context, people.appLink)
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

    val state: FollowListState by viewModel.state.collectAsStateWithLifecycle()

    FollowListScreen(
        state = state,
        onPeopleClicked = ::onPeopleClicked,
        onFollowClicked = ::onFollowClicked,
        onLoadMore = ::onLoadMore,
        onRefresh = ::onRefresh
    )
}

@Composable
internal fun FollowListScreen(
    state: FollowListState,
    onPeopleClicked: (PeopleUiModel) -> Unit,
    onFollowClicked: (PeopleUiModel) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit
) {
    if (state.result?.isFailure == true && state.followList.isEmpty()) {
        Box(Modifier.fillMaxSize()) {
            FollowListErrorScreen(
                isLoading = state.isLoading,
                onRefreshButtonClicked = onRefresh
            )
        }
    } else {
        FollowListScreen(
            people = state.followList.toImmutableList(),
            hasNextPage = state.hasNextPage,
            onLoadMore = onLoadMore,
            onPeopleClicked = onPeopleClicked,
            onFollowClicked = onFollowClicked,
            Modifier.fillMaxSize()
        )
    }
}
