package com.tokopedia.interestpick.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.interestpick.data.pojo.*
import com.tokopedia.interestpick.domain.usecase.GetInterestUseCase
import com.tokopedia.interestpick.domain.usecase.UpdateInterestUseCase
import com.tokopedia.interestpick.view.subscriber.InterestPickViewState
import com.tokopedia.interestpick.view.viewmodel.InterestPickViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.jvm.Throws

private const val DEFAULT_HEADER_TITLE = "Kamu Suka Apa?"

@ExperimentalCoroutinesApi
class InterestPickViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val getInterestUseCase: GetInterestUseCase = mockk(relaxed = true)

    private val updateInterestUseCase: UpdateInterestUseCase = mockk(relaxed = true)

    private lateinit var interestPickViewModel: InterestPickViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        interestPickViewModel = spyk(InterestPickViewModel(getInterestPickUseCase = getInterestUseCase, updateInterestUseCase = updateInterestUseCase))
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        unmockkAll()
    }

    private fun getDummyInterestList(): List<InterestsItem> {
        val interestsItem1 = InterestsItem(id = 1, name = "test1", imageUrl = "testUrl1", relationships = Relationships(isSelected = false))
        val interestsItem2 = InterestsItem(id = 2, name = "test2", imageUrl = "testUrl2", relationships = Relationships(isSelected = true))
        return listOf(interestsItem1, interestsItem2)
    }

    @Test
    fun `test convertToInterestList()`() {
        val dummyList = getDummyInterestList()
        val responseArray = interestPickViewModel.convertToInterestList(dummyList)
        for (i in 0 until responseArray.size) {
            assertEquals(responseArray[i].id, dummyList[i].id)
            assertEquals(responseArray[i].name, dummyList[i].name)
            assertEquals(responseArray[i].image, dummyList[i].imageUrl)
            assertEquals(responseArray[i].isSelected, dummyList[i].relationships.isSelected)
        }
    }

    @Test
    fun `test getTitle()`() {
        val dummyHeader1 = Header(title = "dummy header")
        val responseHeader = interestPickViewModel.getTitle(dummyHeader1)
        assertEquals(dummyHeader1.title, responseHeader)

        val dummyHeader2 = Header(title = "")
        val responseHeaderWhenEmpty = interestPickViewModel.getTitle(dummyHeader2)
        assertEquals(responseHeaderWhenEmpty, DEFAULT_HEADER_TITLE)
    }

    @Test
    fun `test onViewCreated call fetchData() only once`() {
        interestPickViewModel.onViewCreated()
        coVerify(exactly = 1) { interestPickViewModel.fetchData() }
    }

    @Test
    fun `test onRetry call fetchData() only once`() {
        interestPickViewModel.onRetry()
        coVerify(exactly = 1) { interestPickViewModel.fetchData() }
    }

    @Test
    fun `test fetchData() response for Success with error in data`() {
        val dummyResponse = FeedInterestUser(error = "dummy error")
        coEvery { getInterestUseCase.executeOnBackground() } returns GetInterestData(feedInterestUser = dummyResponse)
        interestPickViewModel.fetchData()
        coVerify(exactly = 1) { getInterestUseCase.executeOnBackground() }
        assertEquals(interestPickViewModel.getInterestPickLiveData().value, InterestPickViewState.Error(onUpdate = false, error = dummyResponse.error))
    }

    @Test
    fun `test fetchData() response for error`() {
        val dummyResponse = Exception("dummy Exception")
        coEvery { getInterestUseCase.executeOnBackground() } throws dummyResponse
        interestPickViewModel.fetchData()
        coVerify(exactly = 1) { getInterestUseCase.executeOnBackground() }
        assertEquals(interestPickViewModel.getInterestPickLiveData().value, InterestPickViewState.Error(onUpdate = false, error = dummyResponse.message.toString()))
    }

    @Test
    fun `test fetchData() response for Success with data`() {
        val header = Header(title = "dummy header title")
        val dummyResponse = FeedInterestUser(header = header, interests = getDummyInterestList())
        coEvery { getInterestUseCase.executeOnBackground() } returns GetInterestData(feedInterestUser = dummyResponse)
        interestPickViewModel.fetchData()
        coVerify(exactly = 1) { getInterestUseCase.executeOnBackground() }
        assertEquals(interestPickViewModel.getInterestPickLiveData().value, InterestPickViewState.GetInterestSuccess(headerModel = interestPickViewModel.getTitle(header),
                interestList = interestPickViewModel.convertToInterestList(getDummyInterestList())))
    }

    @Test
    fun `test updateInterest() response for Success with error in data`() {
        val dummyResponse = FeedInterestUserUpdate(error = "dummy error")
        coEvery { updateInterestUseCase.executeOnBackground() } returns UpdateInterestData(dummyResponse)
        interestPickViewModel.updateInterest(listOf())
        coVerify(exactly = 1) { updateInterestUseCase.setRequestParams(listOf()) }
        coVerify(exactly = 1) { updateInterestUseCase.executeOnBackground() }
        assertEquals(interestPickViewModel.getInterestPickLiveData().value, InterestPickViewState.Error(onUpdate = true, error = dummyResponse.error))
    }

    @Test
    fun `test updateInterest() response for error`() {
        val dummyResponse = Exception("dummy Exception")
        coEvery { updateInterestUseCase.executeOnBackground() } throws dummyResponse
        interestPickViewModel.updateInterest(listOf())
        coVerify(exactly = 1) { updateInterestUseCase.setRequestParams(listOf()) }
        coVerify(exactly = 1) { updateInterestUseCase.executeOnBackground() }
        assertEquals(interestPickViewModel.getInterestPickLiveData().value, InterestPickViewState.Error(onUpdate = true, error = dummyResponse.message.toString()))
    }

    @Test
    fun `test updateInterest() response for Success with data`() {
        val dummyResponse = FeedInterestUserUpdate(success = true, error = "")
        coEvery { updateInterestUseCase.executeOnBackground() } returns UpdateInterestData(dummyResponse)
        interestPickViewModel.updateInterest(listOf())
        coVerify(exactly = 1) { updateInterestUseCase.setRequestParams(listOf()) }
        coVerify(exactly = 1) { updateInterestUseCase.executeOnBackground() }
        assertEquals(interestPickViewModel.getInterestPickLiveData().value, InterestPickViewState.UpdateInterestSuccess)
        assertEquals(interestPickViewModel.isSaved, true)
    }

    @Test
    fun `test updateInterestWIthSkip() invocation only once`() {
        interestPickViewModel.updateInterestWIthSkip()
        coVerify(exactly = 1) { updateInterestUseCase.setRequestParamsSkip() }
        coVerify(exactly = 1) { updateInterestUseCase.executeOnBackground() }
    }

    @Test
    fun `test onBackPressed call updateInterestWIthSkip`() {
        //when isSaved is true
        interestPickViewModel.isSaved = true
        interestPickViewModel.onBackPressed()
        coVerify(exactly = 0) { interestPickViewModel.updateInterestWIthSkip() }
        //when isSaved is false
        interestPickViewModel.isSaved = false
        interestPickViewModel.onBackPressed()
        coVerify(exactly = 1) { interestPickViewModel.updateInterestWIthSkip() }
    }

}