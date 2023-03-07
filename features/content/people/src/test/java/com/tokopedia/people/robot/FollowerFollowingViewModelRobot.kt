package com.tokopedia.people.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import io.mockk.mockk
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowerFollowingViewModelRobot(
    repo: UserProfileRepository = mockk(relaxed = true),
) : Closeable {

    val viewModel = FollowerFollowingViewModel(
        repo = repo,
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
