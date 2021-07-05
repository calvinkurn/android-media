package com.tokopedia.ordermanagement.snapshot

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.ordermanagement.snapshot.data.model.GetOrderSnapshot
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotParam
import com.tokopedia.ordermanagement.snapshot.domain.SnapshotUseCase
import com.tokopedia.ordermanagement.snapshot.view.viewmodel.SnapshotViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by fwidjaja on 2/5/21.
 */

@RunWith(JUnit4::class)
class SnapshotViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var snapshotViewModel: SnapshotViewModel
    private var snapshotResult = GetOrderSnapshot()
    private var productUrlTesting = "www.testing.com"

    @RelaxedMockK
    lateinit var snapshotUseCase: SnapshotUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        snapshotViewModel = SnapshotViewModel(CoroutineTestDispatchersProvider, snapshotUseCase)

        snapshotResult = GetOrderSnapshot(productUrl = productUrlTesting)
    }

    // load content
    // finish order result
    @Test
    fun finishOrderResult_shouldReturnSuccess() {
        //given
        coEvery {
            snapshotUseCase.executeSuspend(any())
        } returns Success(GetOrderSnapshot(productUrl = productUrlTesting))

        //when
        snapshotViewModel.loadSnapshot(SnapshotParam())

        //then
        assert(snapshotViewModel.snapshotResponse.value is Success)
        assert((snapshotViewModel.snapshotResponse.value as Success<GetOrderSnapshot>).data.productUrl == productUrlTesting)
    }

    @Test
    fun snapshotProductUrl_shouldReturnFail() {
        //given
        coEvery {
            snapshotUseCase.executeSuspend(any())
        } returns Fail(Throwable())

        //when
        snapshotViewModel.loadSnapshot(SnapshotParam())

        //then
        assert(snapshotViewModel.snapshotResponse.value is Fail)
    }

    @Test
    fun snapshotProductUrl_shouldNotReturnEmptyMessage() {
        //given
        coEvery {
            snapshotUseCase.executeSuspend(any())
        } returns Success(GetOrderSnapshot(productUrl = productUrlTesting))

        //when
        snapshotViewModel.loadSnapshot(SnapshotParam())

        //then
        assert(snapshotViewModel.snapshotResponse.value is Success)
        assert((snapshotViewModel.snapshotResponse.value as Success<GetOrderSnapshot>).data.productUrl.isNotEmpty())
    }
}