package com.tokopedia.topads.view.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.topads.data.response.Error
import com.tokopedia.topads.data.response.ResponseGroupValidateName
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CreateGroupAdsViewModelTest {
    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private lateinit var viewModel: CreateGroupAdsViewModel

    private lateinit var repository: GraphqlRepository
    private lateinit var context: Context
    private lateinit var userSession: UserSession

    @Before
    fun setUp() {
        repository = mockk()
        context = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        viewModel = spyk(CreateGroupAdsViewModel(context, rule.dispatchers, userSession, repository))
        mockkObject(RequestHelper)
        every { RequestHelper.getGraphQlRequest(any(), any(), any()) } returns mockk(relaxed = true)
        every { RequestHelper.getCacheStrategy() } returns mockk(relaxed = true)
    }

    @Test
    fun `test exception in validateGroup`() {
        var t: Throwable? = null
        val myThrowable: Throwable = Exception("my excep")

        coEvery { repository.getReseponse(any(), any()) } throws myThrowable

        viewModel.validateGroup(
                groupName = "",
                onSuccess = {
                },
                onError = {
                    t = it
                }
        )

        Assert.assertEquals(myThrowable.message, t?.message)
    }

    @Test
    fun `check invocation of onError validateGroup`() {
        val expected = "error"
        var actual = ""
        val data = ResponseGroupValidateName(ResponseGroupValidateName.TopAdsGroupValidateName(errors = listOf(Error(detail = expected))))
        val response: GraphqlResponse = mockk(relaxed = true)

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseGroupValidateName::class.java) } returns listOf()
        every { response.getData<ResponseGroupValidateName>(ResponseGroupValidateName::class.java) } returns data

        viewModel.validateGroup(
                groupName = "",
                onSuccess = {
                },
                onError = {
                    actual = it.message ?: ""
                }
        )

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `check invocation of onSuccess validateGroup`() {
        val expected = "groupName"
        var actual = ""
        val data = ResponseGroupValidateName(ResponseGroupValidateName.TopAdsGroupValidateName(ResponseGroupValidateName.TopAdsGroupValidateName.Data(groupName = expected)))
        val response: GraphqlResponse = mockk(relaxed = true)

        mockkStatic(GraphqlHelper::class)
        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
        coEvery { repository.getReseponse(any(), any()) } returns response
        every { response.getError(ResponseGroupValidateName::class.java) } returns listOf()
        every { response.getData<ResponseGroupValidateName>(ResponseGroupValidateName::class.java) } returns data

        viewModel.validateGroup(
                groupName = "",
                onSuccess = {
                    actual = it.groupName
                },
                onError = {
                }
        )

        Assert.assertEquals(expected, actual)
    }
}