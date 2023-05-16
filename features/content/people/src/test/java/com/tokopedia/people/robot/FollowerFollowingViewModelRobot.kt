package com.tokopedia.people.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.viewmodels.FollowerFollowingViewModel
import io.mockk.mockk
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.runTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FollowerFollowingViewModelRobot(
    repo: UserFollowRepository = mockk(relaxed = true),
) : Closeable {

    val viewModel = FollowerFollowingViewModel(
        repo = repo,
    )

    fun getFollowers(
        cursor: String = "",
        limit: Int = 10,
    ) {
        viewModel.getFollowers(cursor, limit)
    }

    fun getFollowings(
        cursor: String = "",
        limit: Int = 10,
    ) {
        viewModel.getFollowings(cursor, limit)
    }

    fun start(fn: suspend FollowerFollowingViewModelRobot.() -> Unit) {
        use {
            runTest { fn() }
        }
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}
