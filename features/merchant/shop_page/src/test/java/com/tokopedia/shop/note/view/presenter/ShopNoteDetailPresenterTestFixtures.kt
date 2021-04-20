package com.tokopedia.shop.note.view.presenter

import com.tokopedia.shop.note.domain.interactor.GetShopNoteDetailUseCase
import com.tokopedia.shop.note.view.listener.ShopNoteDetailView
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import org.junit.Before

internal open class ShopNoteDetailPresenterTestFixtures {

    @RelaxedMockK
    protected lateinit var shopNoteDetailView: ShopNoteDetailView
    @RelaxedMockK
    protected lateinit var getShopNoteDetailUseCase: GetShopNoteDetailUseCase
    protected lateinit var shopNoteDetailPresenter: ShopNoteDetailPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        shopNoteDetailPresenter = ShopNoteDetailPresenter(getShopNoteDetailUseCase)
        shopNoteDetailPresenter.attachView(shopNoteDetailView)
    }
}