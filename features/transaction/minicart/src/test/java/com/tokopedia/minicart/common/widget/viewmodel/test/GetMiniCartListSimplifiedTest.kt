package com.tokopedia.minicart.common.widget.viewmodel.test

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.minicart.cartlist.MiniCartListUiModelMapper
import com.tokopedia.minicart.common.domain.usecase.*
import com.tokopedia.minicart.common.widget.MiniCartViewModel
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Test

class GetMiniCartListSimplifiedTest {

    private lateinit var viewModel: MiniCartViewModel
    private lateinit var dispatcher: CoroutineDispatchers

    private var uiModelMapper: MiniCartListUiModelMapper = spyk()
    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private val getMiniCartListUseCase: GetMiniCartListUseCase = mockk()
    private val deleteCartUseCase: DeleteCartUseCase = mockk()
    private val undoDeleteCartUseCase: UndoDeleteCartUseCase = mockk()
    private val updateCartUseCase: UpdateCartUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = MiniCartViewModel(dispatcher, getMiniCartListSimplifiedUseCase, getMiniCartListUseCase, deleteCartUseCase, undoDeleteCartUseCase, updateCartUseCase, uiModelMapper)
    }

}