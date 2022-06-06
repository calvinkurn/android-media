package com.tokopedia.createpost.presenter

import android.text.TextUtils
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.createpost.fake.FakeUseCase
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import rx.Observable

/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
class CreatePostPresenterTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val view: CreatePostContract.View = mockk(relaxed = true)

    private val mockGetContentFormUseCase = FakeUseCase<GetContentFormDomain>()

    private val presenter: CreatePostContract.Presenter = CreatePostPresenter(
        userSession = mockk(relaxed = true),
        getContentFormUseCase = mockGetContentFormUseCase,
        getFeedUseCase = mockk(relaxed = true),
        getProfileHeaderUseCase = mockk(relaxed = true),
        twitterManager = mockk(relaxed = true),
        getProductSuggestionUseCase = mockk(relaxed = true),
        getShopFavoriteStatusUseCase = mockk(relaxed = true),
    )

    @Before
    fun setUp() {
        presenter.attachView(view)
        mockkStatic(TextUtils::class)
        every { TextUtils.isEmpty(any()) } answers {
            val text = invocation.args[0] as? String
            text?.isEmpty() ?: true
        }
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
    fun `when user wants to invalidate share option, it should change share header text`() {
        presenter.invalidateShareOptions()

        verifySequence {
            view.onGetAvailableShareTypeList(any())
            view.getContext()
            view.changeShareHeaderText(any())
        }
    }
}