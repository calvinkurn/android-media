package com.tokopedia.createpost.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
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

    private val modelBuilder = CommonModelBuilder()
    private val mockException = modelBuilder.buildException()

    private val presenter: CreatePostContract.Presenter = CreatePostPresenter(
        getContentFormUseCase = mockGetContentFormUseCase,
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
}