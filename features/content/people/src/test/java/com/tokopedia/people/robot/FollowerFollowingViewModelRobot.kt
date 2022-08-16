package com.tokopedia.people.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.people.domains.FollowerFollowingListingUseCase
import com.tokopedia.people.domains.ProfileFollowUseCase
import com.tokopedia.people.domains.ProfileUnfollowedUseCase
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import io.mockk.mockk
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowerFollowingViewModelRobot(
    private val useCaseFollowersFollowingsList: FollowerFollowingListingUseCase = mockk(relaxed = true),
    private val useCaseDoFollow: ProfileFollowUseCase = mockk(relaxed = true),
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase = mockk(relaxed = true),
) : Closeable {

    val viewModel = FollowerFollowingViewModel(
        useCaseFollowersList = useCaseFollowersFollowingsList,
        useCaseFollowingList = useCaseFollowersFollowingsList,
        useCaseDoFollow = useCaseDoFollow,
        useCaseDoUnFollow = useCaseDoUnFollow,
    )

    fun getFollowers(
        username: String = "",
        cursor: String = "",
        limit: Int = 10,
    ) {
        viewModel.getFollowers(username, cursor, limit)
    }

    fun getFollowings(
        username: String = "",
        cursor: String = "",
        limit: Int = 10,
    ) {
        viewModel.getFollowings(username, cursor, limit)
    }

    fun start(fn: suspend FollowerFollowingViewModelRobot.() -> Unit) {
        use {
            runBlockingTest { fn() }
        }
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}