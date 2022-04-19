package com.tokopedia.common.topupbills

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SavedNumberViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = CoroutineTestRule()

    lateinit var topupBillsSavedNumberViewModel: TopupBillsSavedNumberViewModel

    @Before
    fun setUp() {
        topupBillsSavedNumberViewModel = TopupBillsSavedNumberViewModel(testCoroutineRule.dispatchers)
    }

    @Test
    fun setSearchKeyword_returnSameData() {
        //given
        topupBillsSavedNumberViewModel.setSearchKeyword("test")

        //then
        assert(topupBillsSavedNumberViewModel.searchKeyword.value == "test")
    }

    @Test
    fun setClueVisibility_returnSameData() {
        topupBillsSavedNumberViewModel.setClueVisibility(true)
        assert(topupBillsSavedNumberViewModel.clueVisibility.value == true)

        topupBillsSavedNumberViewModel.setClueVisibility(false)
        assert(topupBillsSavedNumberViewModel.clueVisibility.value == false)
    }

    @Test
    fun refreshSearchBar_returnSameData() {
        topupBillsSavedNumberViewModel.refreshSearchBar(1)
        assert(topupBillsSavedNumberViewModel.refreshSearchBar.value == 1)
    }

    @Test
    fun enableSearchBar_returnSameData() {
        topupBillsSavedNumberViewModel.enableSearchBar(true)
        assert(topupBillsSavedNumberViewModel.enableSearchBar.value == true)

        topupBillsSavedNumberViewModel.enableSearchBar(false)
        assert(topupBillsSavedNumberViewModel.enableSearchBar.value == false)
    }
}