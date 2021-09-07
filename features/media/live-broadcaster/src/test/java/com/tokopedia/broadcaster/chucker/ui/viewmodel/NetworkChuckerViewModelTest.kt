package com.tokopedia.broadcaster.chucker.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.broadcaster.chucker.data.entity.ChuckerLog
import com.tokopedia.broadcaster.chucker.data.repository.ChuckerLogRepository
import com.tokopedia.broadcaster.chucker.ui.uimodel.ChuckerLogUIModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NetworkChuckerViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val repository = mockk<ChuckerLogRepository>(relaxed = true)
    private val chuckerLogObserver: Observer<MutableList<ChuckerLogUIModel>> = mockk(relaxed = true)

    private lateinit var viewModel: NetworkChuckerViewModel

    private fun createViewModel() {
        viewModel = NetworkChuckerViewModel(repository, Dispatchers.Main)
        viewModel.chuckers.observeForever(chuckerLogObserver)
    }
//
//    @Test fun `should be show the chucker data`() = runBlocking {
//        // given
//        createViewModel()
//
//        val expectedValue = listOf<ChuckerLog>()
//        every { repository.chuckers() } returns expectedValue.map {
//            it.apply {
//                connectionId = 123
//            }
//        }
//
//        // when
//        viewModel.getChuckers()
//
//        // then
//        assertTrue(viewModel.chuckers.value?.first()?.connectionId == 123)
//    }

}