package com.tokopedia.topads.edit.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SharedViewModel
    private lateinit var productId: MutableLiveData<MutableList<String>>
    private lateinit var groupName: MutableLiveData<String>
    private lateinit var groupId: MutableLiveData<Int>
    private lateinit var negKeyword: MutableLiveData<List<GetKeywordResponse.KeywordsItem>>
    private lateinit var maxBudget: MutableLiveData<Int>
    private lateinit var bidSettings: MutableLiveData<List<TopAdsBidSettingsModel>>


    @Before
    fun setUp() {
        viewModel = SharedViewModel()
        productId = mockk(relaxed = true)
        groupName = mockk(relaxed = true)
        groupId = mockk(relaxed = true)
        negKeyword = mockk(relaxed = true)
        maxBudget = mockk(relaxed = true)
        bidSettings = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun setProductIds() {
        val productIdList: MutableList<String> = mutableListOf()
        every { productId.value } returns productIdList
        viewModel.setProductIds(productIdList)
        assertEquals(productId.value, productIdList)
    }

    @Test
    fun setGroupId() {
        val id = 12345
        every { groupId.value } returns id
        viewModel.setGroupId(id)
        assertEquals(groupId.value, id)
    }

    @Test
    fun setNegKeywords() {
        val data: List<GetKeywordResponse.KeywordsItem> = mockk()
        every { negKeyword.value } returns data
        viewModel.setNegKeywords(data)
        assertEquals(negKeyword.value, data)
    }

    @Test
    fun getProuductIds() {
        val productIdList: MutableList<String> = mutableListOf()
        every { productId.value } returns productIdList
        viewModel.setProductIds(productIdList)
        val actual = viewModel.getProuductIds()
        assertEquals(productId.value, actual.value)
    }

    @Test
    fun getGroupId() {
        val id = 12345
        every { groupId.value } returns id
        viewModel.setGroupId(id)
        val actual = viewModel.getGroupId()
        assertEquals(groupId.value, actual.value)
    }

    @Test
    fun getnegKeywords() {
        val data: List<GetKeywordResponse.KeywordsItem> = mockk()
        every { negKeyword.value } returns data
        viewModel.setNegKeywords(data)
        val actual = viewModel.getnegKeywords()
        assertEquals(negKeyword.value, actual.value)
    }

    @Test
    fun setFirstFetchMaxBudgetValue() {
        val dailyBudget = 12345
        every { maxBudget.value } returns dailyBudget
        viewModel.setFirstFetchMaxBudgetValue(dailyBudget)
        assertEquals(maxBudget.value, dailyBudget)
    }

    @Test
    fun getMaxBudget() {
        val data: MutableLiveData<Int> = mockk()
        every { data.value } returns 12345
        viewModel.getMaxBudget()
        viewModel.setFirstFetchMaxBudgetValue(12345)
        val actual = viewModel.getMaxBudget()
        assertEquals(12345, actual.value)
    }

    @Test
    fun getAutoBidStatus() {
        viewModel.getAutoBidStatus()
        assertEquals(null, viewModel.getAutoBidStatus().value)
    }

    @Test
    fun setBidSettings() {
        val data: List<TopAdsBidSettingsModel> = mockk()
        every { bidSettings.value } returns data
        viewModel.setBidSettings(data)
        assertEquals(bidSettings.value, data)
    }

}
