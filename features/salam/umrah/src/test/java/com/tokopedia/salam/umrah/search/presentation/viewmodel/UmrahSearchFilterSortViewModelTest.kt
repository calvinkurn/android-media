package com.tokopedia.salam.umrah.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.salam.umrah.common.data.UmrahSearchParameterEntity
import com.tokopedia.salam.umrah.common.usecase.UmrahSearchParameterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)

class UmrahSearchFilterSortViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var umrahSearchParammeterUseCase : UmrahSearchParameterUseCase

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var umrahSearchFilterViewModel : UmrahSearchFilterSortViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        umrahSearchFilterViewModel = UmrahSearchFilterSortViewModel(umrahSearchParammeterUseCase, dispatcher)
    }

    @Test
    fun `responseSearchFilter_SuccessGetFilter_ShowActualResult`(){
        //given
        coEvery {
            umrahSearchParammeterUseCase.executeUseCase(any())
        }returns Success(UmrahSearchParameterEntity())

        //when
        umrahSearchFilterViewModel.getUmrahSearchParameter("")
        val actual = umrahSearchFilterViewModel.umrahSearchParameter.value
        assert(actual is Success)
    }

    @Test
    fun `responseSearchFilter_FailureGetFilter_ShowFailureResult`(){
        //given
        coEvery {
            umrahSearchParammeterUseCase.executeUseCase(any())
        }returns Fail(Throwable())

        //when
        umrahSearchFilterViewModel.getUmrahSearchParameter("")
        val actual = umrahSearchFilterViewModel.umrahSearchParameter.value
        assert(actual is Fail)
    }



}