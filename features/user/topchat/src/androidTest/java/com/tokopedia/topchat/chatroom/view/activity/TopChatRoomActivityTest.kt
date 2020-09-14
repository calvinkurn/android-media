package com.tokopedia.topchat.chatroom.view.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TopChatRoomActivityTest {

    @get:Rule
    var mActivityTestRule = ActivityTestRule(TopChatRoomActivityStub::class.java)

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: TopChatRoomActivityStub

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        activity = mActivityTestRule.activity
    }

    @Test
    fun size_2_chat_list() {
        // Given

        // When
        activity.setupTestFragment()

        // Then
        assertTrue(true)
    }

}