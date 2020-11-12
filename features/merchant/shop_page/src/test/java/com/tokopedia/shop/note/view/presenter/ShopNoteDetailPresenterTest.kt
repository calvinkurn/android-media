package com.tokopedia.shop.note.view.presenter

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class ShopNoteDetailPresenterTest: ShopNoteDetailPresenterTestFixtures() {

    @Test
    fun showShopNoteWhenSuccess() {
        //given
        val shopNoteModel = ShopNoteModel()
        every { getShopNoteDetailUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<ShopNoteModel>>().onStart()
            secondArg<Subscriber<ShopNoteModel>>().onCompleted()
            secondArg<Subscriber<ShopNoteModel>>().onNext(shopNoteModel)
        }

        //when
        shopNoteDetailPresenter.getShopNoteList("1177", "7711")

        //then
        verify { shopNoteDetailView.onSuccessGetShopNoteList(shopNoteModel) }
        confirmVerified(shopNoteDetailView)
    }

    @Test
    fun showErrorShopNoteWhenFailed() {
        //given
        val message = "something error"
        val throwable = Throwable(MessageErrorException(message))
        every { getShopNoteDetailUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<ShopNoteModel>>().onStart()
            secondArg<Subscriber<ShopNoteModel>>().onError(throwable)
        }

        //when
        shopNoteDetailPresenter.getShopNoteList("1177", "7711")

        //then
        verify { shopNoteDetailView.onErrorGetShopNoteList(throwable) }
        confirmVerified(shopNoteDetailView)
    }

    @Test
    fun `when detach view should unsubscribe use case`() {
        shopNoteDetailPresenter.detachView()
        coVerify { getShopNoteDetailUseCase.unsubscribe() }
    }

}