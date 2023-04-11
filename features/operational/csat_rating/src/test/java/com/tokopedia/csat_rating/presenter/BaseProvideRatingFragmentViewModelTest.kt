package com.tokopedia.csat_rating.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating.Companion.NO_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIFTH_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FIRST_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.FOURTH_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.SECOND_EMOJI
import com.tokopedia.csat_rating.presenter.BaseProvideRatingFragmentViewModel.Companion.THIRD_EMOJI
import com.tokopedia.csat_rating.presenter.screenState.FifthScreenState
import com.tokopedia.csat_rating.presenter.screenState.FirstScreenState
import com.tokopedia.csat_rating.presenter.screenState.FourthScreenState
import com.tokopedia.csat_rating.presenter.screenState.SecondScreenState
import com.tokopedia.csat_rating.presenter.screenState.ThirdScreenState
import com.tokopedia.csat_rating.presenter.screenState.ZeroScreenState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseProvideRatingFragmentViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BaseProvideRatingFragmentViewModel

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = BaseProvideRatingFragmentViewModel(
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `check setSelectedEmoji when is Zero`() {
        viewModel.setCaption(setCaptionDummy())
        viewModel.setQuestion(setQuestionDummy())
        viewModel.setSelectedEmoji(NO_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, NO_EMOJI)
        assertTrue(resultInReal is ZeroScreenState)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is FIRST_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[FIRST_EMOJI - 1]
        val targetQuestion = questionListDummy[FIRST_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(FIRST_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, FIRST_EMOJI)
        assertTrue(resultInReal is FirstScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is SECOND_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[SECOND_EMOJI - 1]
        val targetQuestion = questionListDummy[SECOND_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(SECOND_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, SECOND_EMOJI)
        assertTrue(resultInReal is SecondScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is THIRD_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[THIRD_EMOJI - 1]
        val targetQuestion = questionListDummy[THIRD_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(THIRD_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, THIRD_EMOJI)
        assertTrue(resultInReal is ThirdScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is FOURTH_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[FOURTH_EMOJI - 1]
        val targetQuestion = questionListDummy[FOURTH_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(FOURTH_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, FOURTH_EMOJI)
        assertTrue(resultInReal is FourthScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is FIFTH_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[FIFTH_EMOJI - 1]
        val targetQuestion = questionListDummy[FIFTH_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(FIFTH_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, FIFTH_EMOJI)
        assertTrue(resultInReal is FifthScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is unknowen`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(6)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertTrue(resultInReal is ZeroScreenState)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check is reason list not empty when assigned it`() {
        val targetAndDummy = arrayListOf<BadCsatReasonListItem>(mockk(relaxed = true), mockk(relaxed = true))
        viewModel.setReasonList(targetAndDummy)
        assertEquals(viewModel.reasonList, targetAndDummy)
        assertEquals(viewModel.reasonList.size, targetAndDummy.size)
    }

    @Test
    fun `check is reason list is empty when assigned it`() {
        viewModel.setReasonList(arrayListOf())
        assertEquals(viewModel.reasonList.size, 0)
    }

    private fun setCaptionDummy(): ArrayList<String> {
        return arrayListOf("Caption 1", "Caption 2", "Caption 3", "Caption 4", "Caption 5")
    }

    private fun setQuestionDummy(): ArrayList<String> {
        return arrayListOf(
            "Apa ada yang kurang?",
            "Apa yang bisa kami pahami tentang anda?",
            "apa yang harus kami lakukan untuk anda?",
            "apa yang bisa kami tambahkan?",
            "apa yang bisa kami pertahankan?"
        )
    }
}
