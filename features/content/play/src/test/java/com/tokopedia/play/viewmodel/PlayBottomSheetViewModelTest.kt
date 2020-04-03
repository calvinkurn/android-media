package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.GsonBuilder
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.viewmodel.PlayBottomSheetViewModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by mzennis on 02/04/20.
 */
class PlayBottomSheetViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockGetProductVariantUseCase: GetProductVariantUseCase = mockk(relaxed = true)
    private val mockPostAddToCartUseCase: PostAddToCartUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val modelBuilder = ModelBuilder()

    private lateinit var playBottomSheetViewModel: PlayBottomSheetViewModel

    @Before
    fun setUp() {
        playBottomSheetViewModel = PlayBottomSheetViewModel(
                mockGetProductVariantUseCase,
                mockPostAddToCartUseCase,
                userSession,
                dispatchers
        )

        coEvery { mockGetProductVariantUseCase.executeOnBackground() } returns modelBuilder.buildProductVariant()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `test observe product variant`() {
        val expectedModel = modelBuilder.buildVariantSheetUiModel()
        val expectedResult = PlayResult.Success(expectedModel)

        println("expected: ${GsonBuilder().setPrettyPrinting().create().toJson(expectedModel)}")

        playBottomSheetViewModel.getProductVariant(
                PlayUiMapper.mapItemProduct(modelBuilder.buildProduct()),
                ProductAction.AddToCart
        )

        Assertions
                .assertThat(playBottomSheetViewModel.observableProductVariant.getOrAwaitValue())
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test add to cart`() {


    }
}