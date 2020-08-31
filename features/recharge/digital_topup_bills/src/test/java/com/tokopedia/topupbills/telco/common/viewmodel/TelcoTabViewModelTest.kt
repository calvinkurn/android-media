package com.tokopedia.topupbills.telco.common.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topupbills.telco.common.model.TelcoTabItem
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoTabViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var telcoTabViewModel: TelcoTabViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        telcoTabViewModel = TelcoTabViewModel(Dispatchers.Unconfined)
    }

    private fun createListTab() : List<TelcoTabItem> {
        var idLong = 1L
        val listTabs = mutableListOf<TelcoTabItem>()
        listTabs.add(TelcoTabItem(Bundle(), "Pulsa", idLong++))
        listTabs.add(TelcoTabItem(Bundle(), "Paket Data", idLong++))
        listTabs.add(TelcoTabItem(Bundle(), "Roaming", idLong++))
        return listTabs
    }

    @Test
    fun getTabs_ValidData() {
        //given
        val listTabs = createListTab()

        //when
        telcoTabViewModel.addAll(listTabs)

        //then
        val actualData = telcoTabViewModel.getAll()
        Assert.assertEquals(listTabs[0].id, actualData[0].id)
    }

    @Test
    fun createIdSnapshot_ValidData() {
        //given
        val listTabs = createListTab()

        //when
        telcoTabViewModel.addAll(listTabs)

        //then
        val actualData = telcoTabViewModel.createIdSnapshot()
        Assert.assertEquals(listTabs[0].id, actualData[0])
    }
}