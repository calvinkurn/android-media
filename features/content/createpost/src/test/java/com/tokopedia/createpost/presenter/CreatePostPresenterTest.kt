package com.tokopedia.createpost.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.createpost.common.domain.entity.FeedDetail
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.createpost.fake.FakeUseCase
import com.tokopedia.createpost.model.CommonModelBuilder
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
class CreatePostPresenterTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val view: CreatePostContract.View = mockk(relaxed = true)

    private val mockGetContentFormUseCase = FakeUseCase<GetContentFormDomain>()
    private val mockGetFeedUseCase = FakeUseCase<FeedDetail?>()

    private val modelBuilder = CommonModelBuilder()
    private val mockException = modelBuilder.buildException()

    private val presenter: CreatePostContract.Presenter = CreatePostPresenter(
        userSession = mockk(relaxed = true),
        getContentFormUseCase = mockGetContentFormUseCase,
        getFeedUseCase = mockGetFeedUseCase,
        getProfileHeaderUseCase = mockk(relaxed = true),
        twitterManager = mockk(relaxed = true),
        getProductSuggestionUseCase = mockk(relaxed = true),
        getShopFavoriteStatusUseCase = mockk(relaxed = true),
    )

    @Before
    fun setUp() {
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } answers {
            val text = invocation.args[0] as? String
            text?.isEmpty() ?: true
        }

        presenter.attachView(view)
    }

    @Test
    fun `when user fetch content form and success, it should emit the data successfully`() {
        val mockResponse = GetContentFormDomain()

        mockGetContentFormUseCase.setSuccessResponse(mockResponse)
        presenter.fetchContentForm(mutableListOf(), "", "")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.onSuccessGetContentForm(
                feedContentForm = mockResponse.feedContentResponse!!.feedContentForm,
                isFromTemplateToken = false,
            )
        }
    }

    @Test
    fun `when user fetch content form with token and success, it should emit the data successfully`() {
        val mockResponse = GetContentFormDomain()

        mockGetContentFormUseCase.setSuccessResponse(mockResponse)
        presenter.fetchContentFormByToken("token", "")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.onSuccessGetContentForm(
                feedContentForm = mockResponse.feedContentResponse!!.feedContentForm,
                isFromTemplateToken = true,
            )
        }
    }

    @Test
    fun `when user fetch content form and fail, it should emit the failed data`() {
        mockGetContentFormUseCase.setErrorResponse(mockException)
        presenter.fetchContentForm(mutableListOf(), "", "")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.onErrorGetContentForm(
                ErrorHandler.getErrorMessage(view.getContext(), mockException), mockException
            )
        }
    }

    @Test
    fun `when user fetch content form with token and fail, it should emit the failed data`() {
        mockGetContentFormUseCase.setErrorResponse(mockException)
        presenter.fetchContentFormByToken("token", "")

        verifySequence {
            view.showLoading()
            view.hideLoading()
            view.onErrorGetContentForm(
                ErrorHandler.getErrorMessage(view.getContext(), mockException), mockException
            )
        }
    }

    @Test
    fun `when user fetch feed detail and success, it should emit the data successfully`() {
        val mockResponse = FeedDetail()
        mockGetFeedUseCase.setSuccessResponse(mockResponse)

        presenter.getFeedDetail("", true)

        verifySequence {
            view.showLoading()
            view.onSuccessGetPostEdit(mockResponse)
        }
    }

    @Test
    fun `when user fetch feed detail and failed, it should emit the failed data`() {
        mockGetFeedUseCase.setErrorResponse(mockException)

        presenter.getFeedDetail("", true)

        verifySequence {
            view.showLoading()
            view.onErrorGetPostEdit(mockException)
        }
    }

    @Test
    fun `when user fetch feed detail and the data is empty, it should emit the failed data`() {
        val mockResponse = null
        mockGetFeedUseCase.setSuccessResponse(mockResponse)

        presenter.getFeedDetail("", true)

        verifySequence {
            view.showLoading()
            view.onErrorGetPostEdit(null)
        }
    }

    @Test
    fun `when user wants to invalidate share option, it should change share header text`() {
        presenter.invalidateShareOptions()

        verifySequence {
            view.onGetAvailableShareTypeList(any())
            view.getContext()
            view.changeShareHeaderText(any())
        }
    }
}