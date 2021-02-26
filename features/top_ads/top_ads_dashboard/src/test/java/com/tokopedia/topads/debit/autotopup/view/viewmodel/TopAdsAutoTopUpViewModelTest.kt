package com.tokopedia.topads.debit.autotopup.view.viewmodel

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.dashboard.data.model.TkpdProducts
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsAutoTopUpUSeCase
import com.tokopedia.topads.dashboard.domain.interactor.TopAdsSaveSelectionUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TopAdsAutoTopUpViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()
    private lateinit var viewModel: TopAdsAutoTopUpViewModel
    private var autoTopUpUSeCase: TopAdsAutoTopUpUSeCase = mockk(relaxed = true)
    private var saveSelectionUseCase: TopAdsSaveSelectionUseCase = mockk(relaxed = true)
    private var useCase: GraphqlUseCase<TkpdProducts> = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = TopAdsAutoTopUpViewModel(useCase, autoTopUpUSeCase, saveSelectionUseCase, rule.dispatchers)
    }

    @Test
    fun `auto topup status pass`() {
        viewModel.getAutoTopUpStatusFull()
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
    }

    @Test
    fun `auto top up status fail`() {
        val exception = Exception("lalalla")
        every { autoTopUpUSeCase.execute(any(), any()) } answers {
            exception
        }
        viewModel.getAutoTopUpStatusFull()
        verify {
            autoTopUpUSeCase.execute(any(), any())
        }
        //      Assert.assertEquals(Fail(exception), viewModel.getAutoTopUpStatus.value)
    }

    @Test
    fun `save selection pass`() {
//        val observer1 = mockk<Observer<SavingAutoTopUpState>> {
//            every { onChanged(any()) } just runs
//        }
//      //  viewModel.statusSaveSelection.value = Loading
//
//        val selectedItem: AutoTopUpItem = mockk(relaxed = true)
//        viewModel.statusSaveSelection.observeForever(observer1)
//        viewModel.saveSelection(true,selectedItem)
//        Assert.assertEquals(Loading, viewModel.statusSaveSelection.value)
//        Assert.assertTrue(viewModel.statusSaveSelection.value == null)
//        verify {
//            saveSelectionUseCase.execute(any(),any())
//        }
    }

    @Test
    fun `populate credit list success`() {
        viewModel.populateCreditList("123") {}
        verify {
            useCase.execute(any(), any())
        }
    }
}