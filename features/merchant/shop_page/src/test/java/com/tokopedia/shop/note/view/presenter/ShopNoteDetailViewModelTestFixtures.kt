package com.tokopedia.shop.note.view.presenter

import com.tokopedia.shop.common.domain.GetShopNoteUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before

internal open class ShopNoteDetailViewModelTestFixtures {

    @RelaxedMockK
    protected lateinit var getShopNoteUseCase: GetShopNoteUseCase
    protected lateinit var shopNoteDetailViewModel: ShopNoteDetailViewModel
    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        shopNoteDetailViewModel = ShopNoteDetailViewModel(getShopNoteUseCase, testCoroutineDispatcherProvider)
    }
}