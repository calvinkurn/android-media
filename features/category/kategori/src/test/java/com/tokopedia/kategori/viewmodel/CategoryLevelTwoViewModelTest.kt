package com.tokopedia.kategori.viewmodel

import com.tokopedia.kategori.usecase.AllCategoryQueryUseCase
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test


class CategoryLevelTwoViewModelTest {

    var allCategoryQueryUseCase: AllCategoryQueryUseCase = mockk(relaxed = true)


    @RelaxedMockK
    lateinit var performanceMonitoringListener: PerformanceMonitoringListener

   // val mockSubscriber: CategoryLevelTwoSubscriber = mockk(relaxed = true)


    @RelaxedMockK
    lateinit var requestParams: RequestParams

    private lateinit var mViewModel: CategoryLevelTwoViewModel

    @Before
    fun setup() {
        mViewModel = spyk(CategoryLevelTwoViewModel())
        MockKAnnotations.init(this)
    }

    @Test
    fun refresh() {
     //   val slotSubscriber = slot<CategoryLevelTwoSubscriber>()

        // given
      //  every { mViewModel.getSubscriber("0") } returns mockSubscriber
        every { allCategoryQueryUseCase.createRequestParams(2, true) } returns requestParams
     //   every { allCategoryQueryUseCase.execute(requestParams, capture(slotSubscriber)) } just runs

        //when
        mViewModel.refresh("0")

        // then
        verify {
            allCategoryQueryUseCase.execute(requestParams, any())
        }

      //  Assert.assertEquals(slotSubscriber.captured, mockSubscriber)
    }


}