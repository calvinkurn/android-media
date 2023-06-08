package com.tokopedia.csat_rating.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating.Companion.NO_EMOJI
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
        val targetCaption = captionListDummy[Companion.FIRST_EMOJI - 1]
        val targetQuestion = questionListDummy[Companion.FIRST_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(BaseProvideRatingFragmentViewModel.FIRST_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, BaseProvideRatingFragmentViewModel.FIRST_EMOJI)
        assertTrue(resultInReal is FirstScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is SECOND_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[Companion.SECOND_EMOJI - 1]
        val targetQuestion = questionListDummy[Companion.SECOND_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(BaseProvideRatingFragmentViewModel.SECOND_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, BaseProvideRatingFragmentViewModel.SECOND_EMOJI)
        assertTrue(resultInReal is SecondScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is THIRD_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[Companion.THIRD_EMOJI - 1]
        val targetQuestion = questionListDummy[Companion.THIRD_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(BaseProvideRatingFragmentViewModel.THIRD_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, BaseProvideRatingFragmentViewModel.THIRD_EMOJI)
        assertTrue(resultInReal is ThirdScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is FOURTH_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[Companion.FOURTH_EMOJI - 1]
        val targetQuestion = questionListDummy[Companion.FOURTH_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(BaseProvideRatingFragmentViewModel.FOURTH_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, BaseProvideRatingFragmentViewModel.FOURTH_EMOJI)
        assertTrue(resultInReal is FourthScreenState)
        assertEquals(resultInReal?.getMessage(), targetCaption)
        assertEquals(resultInReal?.getQuestion(), targetQuestion)
        assertEquals(viewModel.csatTitle, "ABC")
    }

    @Test
    fun `check setSelectedEmoji when is FIFTH_EMOJI`() {
        val captionListDummy = setCaptionDummy()
        val questionListDummy = setQuestionDummy()
        val targetCaption = captionListDummy[Companion.FIFTH_EMOJI - 1]
        val targetQuestion = questionListDummy[Companion.FIFTH_EMOJI - 1]
        viewModel.setCaption(captionListDummy)
        viewModel.setQuestion(questionListDummy)
        viewModel.setSelectedEmoji(BaseProvideRatingFragmentViewModel.FIFTH_EMOJI)
        viewModel.setCsatTitle("ABC")
        val resultInReal = viewModel.screenState.value

        assertEquals(viewModel.emojiState, BaseProvideRatingFragmentViewModel.FIFTH_EMOJI)
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

    companion object {
        const val FIRST_EMOJI = 1
        const val SECOND_EMOJI = 2
        const val THIRD_EMOJI = 3
        const val FOURTH_EMOJI = 4
        const val FIFTH_EMOJI = 5
    }
}
