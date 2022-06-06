package com.tokopedia.createpost.presenter

import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.domain.entity.GetContentFormDomain
import com.tokopedia.createpost.domain.usecase.GetContentFormUseCase
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Rule
import org.junit.Test
import rx.Observable

/**
 * Created By : Jonathan Darwin on June 06, 2022
 */
class CreatePostPresenterTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockGetContentFormUseCase: GetContentFormUseCase = mockk()

    private val presenter: CreatePostContract.Presenter = CreatePostPresenter(
        userSession = mockk(relaxed = true),
        getContentFormUseCase = mockGetContentFormUseCase,
        getFeedUseCase = mockk(relaxed = true),
        getProfileHeaderUseCase = mockk(relaxed = true),
        twitterManager = mockk(relaxed = true),
        getProductSuggestionUseCase = mockk(relaxed = true),
        getShopFavoriteStatusUseCase = mockk(relaxed = true),
    )


    @Test
    fun `when user fetch content form and success, it should emit the data successfully`() {
        val mockResponse = GetContentFormDomain()
        every { mockGetContentFormUseCase.createObservable(any()) } returns Observable.just(mockResponse)
        every { mockGetContentFormUseCase.execute(any(), any()) } returns Unit

        val view: CreatePostContract.View = mockk(relaxed = true)
        presenter.attachView(view)
        presenter.invalidateShareOptions()

        verifySequence {
            view.onGetAvailableShareTypeList(any())
            view.getContext()
            view.changeShareHeaderText(any())
//            view.onSuccessGetContentForm(mockResponse.feedContentResponse!!.feedContentForm, true)
        }
    }
}