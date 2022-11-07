package com.tokopedia.autocompletecomponent.universal.presenter.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.autocompletecomponent.initialstate.TestException
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.autocompletecomponent.stubExecute
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.BaseUniversalDataView
import com.tokopedia.autocompletecomponent.universal.presentation.mapper.UniversalSearchModelMapperModule
import com.tokopedia.autocompletecomponent.universal.presentation.viewmodel.UniversalSearchViewModel
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presenter.viewmodel.testinstance.universalSearchModel
import com.tokopedia.autocompletecomponent.util.ChooseAddressWrapper
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule

internal open class UniversalSearchDataViewTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val universalSearchUseCase = mockk<UseCase<UniversalSearchModel>>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)
    protected val dimension90 = ""
    protected val keyword = "susu"
    private val universalSearchModelMapperModule = UniversalSearchModelMapperModule(dimension90, keyword)
    protected val universalSearchModelMapper = universalSearchModelMapperModule.provideUniversalSearchModelMapper()

    protected lateinit var universalSearchViewModel: UniversalSearchViewModel

    protected val universalSearchParameterCommon = mapOf(
        SearchApiConst.Q to keyword,
        SearchApiConst.DEVICE to "android",
        SearchApiConst.USER_ID to "123123",
        SearchApiConst.SOURCE to "universe",
    )

    protected val testException = TestException("Error")

    @Before
    open fun setUp() {
        universalSearchViewModel = createUniversalSearchViewModel()
    }

    private fun createUniversalSearchViewModel(): UniversalSearchViewModel {
        return UniversalSearchViewModel(
            CoroutineTestDispatchersProvider,
            universalSearchUseCase,
            universalSearchModelMapper,
            universalSearchParameterCommon,
            chooseAddressWrapper,
        )
    }

    protected fun `Given universal search API will be successful`() {
        universalSearchUseCase.stubExecute().returns(universalSearchModel)
    }

    protected fun `Given universal search API will be fail`() {
        universalSearchUseCase.stubExecute().throws(testException)
    }
}