package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.mycoupon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.HideSectionResponse
import com.tokopedia.discovery2.usecase.HideSectionUseCase
import com.tokopedia.discovery2.usecase.MyCouponUseCase
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyCouponModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: MyCouponViewModel =
        spyk(MyCouponViewModel(application, componentsItem, 99))

    private val myCouponUseCase: MyCouponUseCase by lazy {
        mockk()
    }

    private val hideSectionUseCase: HideSectionUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }


    /**************************** test for onAttachToViewHolder *******************************************/
    @Test
    fun `test for onAttachToViewHolder when catalogSlug is not empty`(){
        viewModel.myCouponUseCase = myCouponUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(catalogSlug = arrayListOf("COUTEST2ACLAIM,COUTEST2BCLAIMA,COUTEST2CCLAIMA"))
        list.add(item)
        every { componentsItem.data } returns list
        val componentItemList = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItemList
        coEvery {
            myCouponUseCase.getMyCouponData(any(),any(),any())
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getComponentList().value , componentItemList)
    }

    @Test
    fun `test for onAttachToViewHolder when getCouponsList is empty`(){
        viewModel.myCouponUseCase = myCouponUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(catalogSlug = arrayListOf("COUTEST2ACLAIM,COUTEST2BCLAIMA,COUTEST2CCLAIMA"))
        list.add(item)
        every { componentsItem.data } returns list
        val componentItemList = arrayListOf<ComponentsItem>()
        every { componentsItem.getComponentsItem() } returns componentItemList
        coEvery {
            myCouponUseCase.getMyCouponData(any(),any(),any())
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getComponentList().value , null)
    }

    @Test
    fun `test for onAttachToViewHolder when getCouponsList returns null`(){
        viewModel.myCouponUseCase = myCouponUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(catalogSlug = arrayListOf("COUTEST2ACLAIM,COUTEST2BCLAIMA,COUTEST2CCLAIMA"))
        list.add(item)
        every { componentsItem.data } returns list
        every { componentsItem.getComponentsItem() } returns null
        coEvery {
            myCouponUseCase.getMyCouponData(any(),any(),any())
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getComponentList().value , null)
    }

    @Test
    fun `test for onAttachToViewHolder when getMyCouponData throws exception`(){
        viewModel.myCouponUseCase = myCouponUseCase
        viewModel.hideSectionUseCase = hideSectionUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(catalogSlug = arrayListOf("COUTEST2ACLAIM,COUTEST2BCLAIMA,COUTEST2CCLAIMA"))
        list.add(item)
        every { componentsItem.data } returns list
        val hideSectionResponse = HideSectionResponse(true,"21")
        coEvery { hideSectionUseCase.checkForHideSectionHandling(any())} returns hideSectionResponse
        coEvery {
            myCouponUseCase.getMyCouponData(any(),any(),any())
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideSectionLD.value , "21")
    }

    @Test
    fun `test for onAttachToViewHolder when catalogSlug is empty and section is not empty`(){
        viewModel.hideSectionUseCase = hideSectionUseCase
        val list = ArrayList<DataItem>()
        val item = DataItem(catalogSlug = arrayListOf())
        list.add(item)
        every { componentsItem.data } returns list
        val hideSectionResponse = HideSectionResponse(true,"21")
        coEvery { hideSectionUseCase.checkForHideSectionHandling(any())} returns hideSectionResponse

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideSectionLD.value , "21")
    }

}