package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponQuotaUpdateUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUpdateStatusUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TmKuponInitialUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponInitialResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponListResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponUpdateResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmUpdateCouponQuotaDataExt
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class TmCouponViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TmCouponViewModel
    private val tmCouponUsecase = mockk<TmCouponUsecase>(relaxed = true)
    private val tmKuponInitialUsecase = mockk<TmKuponInitialUsecase>(relaxed = true)
    private val tmCouponUpdateStatusUsecase = mockk<TmCouponUpdateStatusUsecase>(relaxed = true)
    private val tmCouponQuotaUpdateUsecase = mockk<TmCouponQuotaUpdateUsecase>(relaxed = true)

    @Before
    fun setup(){
        viewModel = TmCouponViewModel(
            tmCouponUsecase,
            tmKuponInitialUsecase,
            tmCouponUpdateStatusUsecase,
            tmCouponQuotaUpdateUsecase,
            dispatcher
        )
    }

    @Test
    fun successGetCouponList(){
        val data = mockk<TmCouponListResponse>(relaxed = true)
        coEvery {
            tmCouponUsecase.getCouponList(any(), any(), "", 0, 0, 0)
        } coAnswers {
            firstArg<(TmCouponListResponse) -> Unit>().invoke(data)
        }
        viewModel.getCouponList("", 0 ,0, 0)
        assertEquals(data, (viewModel.couponListLiveData.value as TokoLiveDataResult).data)
    }

    @Test
    fun failureGetCouponList(){
        coEvery {
            tmCouponUsecase.getCouponList(any(), any(), "", 0, 0, 0)
        } coAnswers {
            arg<(Throwable) -> Unit>(1).invoke(mockThrowable)
        }
        viewModel.getCouponList("", 0 ,0, 0)
        assertEquals(mockThrowable, (viewModel.couponListLiveData.value as TokoLiveDataResult).error)
    }

    @Test
    fun successInitiateCoupon(){
        val data = mockk<TmCouponInitialResponse>(relaxed = true)
        coEvery {
            tmKuponInitialUsecase.getInitialCoupon(any(), any(), "", "")
        } coAnswers {
            firstArg<(TmCouponInitialResponse) -> Unit>().invoke(data)
        }
        viewModel.getInitialCouponData("", "")
        assertEquals(data, (viewModel.tmCouponInitialLiveData.value as TokoLiveDataResult).data)
    }

    @Test
    fun failureInitiateCoupon(){
        coEvery {
            tmKuponInitialUsecase.getInitialCoupon(any(), any(), "", "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getInitialCouponData("", "")
        assertEquals(mockThrowable, (viewModel.tmCouponInitialLiveData.value as TokoLiveDataResult).error)
    }

    @Test
    fun successUpdateCouponStatus(){
        val data = mockk<TmCouponUpdateResponse>(relaxed = true)
        coEvery {
            tmCouponUpdateStatusUsecase.updateCouponStatus(any(), any(), 0, "", "")
        } coAnswers {
            firstArg<(TmCouponUpdateResponse) -> Unit>().invoke(data)
        }
        viewModel.updateStatus(0, "", "")
        assertEquals(data, (viewModel.tmCouponUpdateLiveData.value as TokoLiveDataResult).data)
    }

    @Test
    fun failureUpdateCouponStatus(){
        coEvery {
            tmCouponUpdateStatusUsecase.updateCouponStatus(any(), any(), 0, "", "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.updateStatus(0, "", "")
        assertEquals(mockThrowable, (viewModel.tmCouponUpdateLiveData.value as TokoLiveDataResult).error)
    }

    @Test
    fun successUpdateCouponQuota(){
        val data = mockk<TmUpdateCouponQuotaDataExt>(relaxed = true)
        coEvery {
            tmCouponQuotaUpdateUsecase.updateCouponQuota(any(), any(), 0, 0, "")
        } coAnswers {
            firstArg<(TmUpdateCouponQuotaDataExt) -> Unit>().invoke(data)
        }
        viewModel.updateQuota(0, 0, "")
        assertEquals(data, (viewModel.tmCouponQuotaUpdateLiveData.value as TokoLiveDataResult).data)
    }

    @Test
    fun failureUpdateCouponQuota(){
        coEvery {
            tmCouponQuotaUpdateUsecase.updateCouponQuota(any(), any(), 0, 0, "")
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.updateQuota(0, 0, "")
        assertEquals(mockThrowable, (viewModel.tmCouponQuotaUpdateLiveData.value as TokoLiveDataResult).error)
    }

    @Test
    fun refreshListState(){
        viewModel.refreshListState(1)
        assertEquals(
            viewModel.tmCouponListStateLiveData.value,
            1
        )
    }

}
