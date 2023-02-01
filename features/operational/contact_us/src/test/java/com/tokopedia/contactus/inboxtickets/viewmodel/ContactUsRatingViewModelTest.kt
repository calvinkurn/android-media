package com.tokopedia.contactus.inboxtickets.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.contactus.inboxtickets.view.viewModel.ContactUsRatingViewModel
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.presenter.screenState.*
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import org.junit.*
import org.junit.Assert.assertEquals

class ContactUsRatingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ContactUsRatingViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        viewModel = ContactUsRatingViewModel(
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `set Caption and Question, and Reason list, title and then selected emoji on position 1`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 1
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is FirstScreenState
        assertEquals(true, isInCorrectState)
    }

    @Test
    fun `set Caption and Question, and Reason list, title and then selected emoji on position 2`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 2
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is SecondScreenState
        assertEquals(true, isInCorrectState)
    }

    @Test
    fun `set Caption and Question, and Reason list,, title and then selected emoji on position 3`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 3
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is ThirdScreenState
        assertEquals(true, isInCorrectState)
    }

    @Test
    fun `set Caption and Question, and Reason list, title and then selected emoji on position 4`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 4
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is FourthScreenState
        assertEquals(true, isInCorrectState)
    }

    @Test
    fun `set Caption and Question, and Reason list, title and then selected emoji on position 5`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 5
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is FifthScreenState
        assertEquals(true, isInCorrectState)
    }

    @Test
    fun `set Caption and Question, and Reason list, title and then selected emoji on position 0`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 0
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is ZeroScreenState
        assertEquals(true, isInCorrectState)
    }

    @Test
    fun `set Caption and Question, and Reason list, title and then selected emoji on position 6`() {
        val inputTitle = "Hai Csat"
        val inputCaptions = listOf("as", "ad", "ac", "av", "ad")
        val inputQuestions = listOf("asa", "ada", "aca", "ava", "ada")
        val inputCsatOptions = createBadCsatReasonListItem()
        val selectedPosition = 6
        viewModel.setCaption(inputCaptions)
        viewModel.setQuestion(inputQuestions)
        viewModel.setReasonList(inputCsatOptions)
        viewModel.setCsatTitle(inputTitle)
        viewModel.setSelectedEmoji(selectedPosition)
        assertEquals(inputTitle, viewModel.csatTitle)
        assertEquals(selectedPosition, viewModel.emojiState)
        assertEquals(inputCsatOptions[1], viewModel.reasonList[1])
        val actualCondition = viewModel.screenState.value
        val isInCorrectState = actualCondition is ZeroScreenState
        assertEquals(true, isInCorrectState)
    }

    private fun createBadCsatReasonListItem(): ArrayList<BadCsatReasonListItem> {
        val badCsatReasonListItem: ArrayList<BadCsatReasonListItem> = arrayListOf()
        for (i in 1..6) {
            badCsatReasonListItem.add(
                BadCsatReasonListItem().apply {
                    id = i
                    messageEn = "message-$i"
                    message = "message-$i"
                }
            )
        }
        return badCsatReasonListItem
    }
}
