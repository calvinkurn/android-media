package com.tokopedia.kategori.viewmodel

//import com.tokopedia.kategori.subscriber.CategoryLevelOneSubscriber
import com.tokopedia.kategori.usecase.AllCategoryQueryUseCase
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class CategoryLevelOneViewModelTest {

    var allCategoryQueryUseCase: AllCategoryQueryUseCase = mockk(relaxed = true)


    @RelaxedMockK
    lateinit var performanceMonitoringListener: PerformanceMonitoringListener

   // val mockSubscriber: CategoryLevelOneSubscriber = mockk(relaxed = true)


    @RelaxedMockK
    lateinit var requestParams: RequestParams

    private lateinit var mViewModel: CategoryLevelOneViewModel

    @Before
    fun setup() {
        mViewModel = spyk(CategoryLevelOneViewModel())
        MockKAnnotations.init(this)
    }


    @Test
    fun bound() {
       // val slotSubscriber = slot<CategoryLevelOneSubscriber>()

        // given
      //  every { mViewModel.getSubscriber(performanceMonitoringListener) } returns mockSubscriber
        every { allCategoryQueryUseCase.createRequestParams(2, true) } returns requestParams
      //  every { allCategoryQueryUseCase.execute(requestParams, capture(slotSubscriber)) } just runs

        //when
        mViewModel.bound(performanceMonitoringListener)

        // then
        verify {
            allCategoryQueryUseCase.execute(requestParams, any())
        }

      //  Assert.assertEquals(slotSubscriber.captured, mockSubscriber)
    }
}

