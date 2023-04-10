package com.tokopedia.minicart.common.simplified.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.promo.domain.usecase.ValidateUseMvcUseCase
import com.tokopedia.minicart.common.simplified.MiniCartSimplifiedViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FieldTest {

    private lateinit var viewModel: MiniCartSimplifiedViewModel

    private var getMiniCartListSimplifiedUseCase: GetMiniCartListSimplifiedUseCase = mockk()
    private var validateUseMvcUseCase: ValidateUseMvcUseCase = mockk()

    @get: Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = MiniCartSimplifiedViewModel(getMiniCartListSimplifiedUseCase, validateUseMvcUseCase)
    }

    @Test
    fun `Field Setter Getter Test`() {
        // Given
        val currentPageSource = MiniCartAnalytics.Page.MVC_PAGE
        val currentSite = "currentSite"
        val currentBusinessUnit = "currentBusinessUnit"
        val currentStateFirstValidate = true

        // When
        viewModel.currentPageSource = currentPageSource
        viewModel.currentSite = currentSite
        viewModel.currentBusinessUnit = currentBusinessUnit
        viewModel.isFirstValidate = currentStateFirstValidate

        // Then
        assert(viewModel.currentPageSource == currentPageSource)
        assert(viewModel.currentSite == currentSite)
        assert(viewModel.currentBusinessUnit == currentBusinessUnit)
        assert(viewModel.isFirstValidate == currentStateFirstValidate)
    }
}
