package com.tokopedia.entertainment.pdp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.entertainment.pdp.EventJsonMapper.getJson
import com.tokopedia.entertainment.pdp.data.EventContentByIdEntity
import com.tokopedia.entertainment.pdp.data.EventPDPContentCombined
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EventPDPFormViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var eventPDPFormViewModel: EventPDPFormViewModel

    @MockK
    lateinit var graphqlRepository: MultiRequestGraphqlUseCase

    @MockK
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var eventProductDetailUseCase: EventProductDetailUseCase

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        eventPDPFormViewModel = EventPDPFormViewModel(Dispatchers.Unconfined, eventProductDetailUseCase)
    }

    @Test
    fun `DataForm_NotNullData_Shownotnulldata`(){
        Assert.assertNotNull(eventProductDetailUseCase)
        Assert.assertNotNull(graphqlRepository)
        Assert.assertNotNull(userSessionInterface)
    }

    @Test
    fun `ProductdetailFormData_SuccessShowProduct_ShowActualResult`(){
        //given
        val contentMock = Gson().fromJson(getJson("content_mock.json"), EventContentByIdEntity::class.java)
        val pdpMock = Gson().fromJson(getJson("pdp_mock.json"), EventProductDetailEntity::class.java)

        coEvery {
            eventProductDetailUseCase.executeUseCase("", "", false, "")
        } returns Success(EventPDPContentCombined(contentMock, pdpMock))

        //when
        eventPDPFormViewModel.getData("","","")

        //then
        assertEquals(eventPDPFormViewModel.mFormData.value, pdpMock.eventProductDetail.productDetailData.forms)
    }

    @Test
    fun `ProductdetailFormData_FailShowProduct_FailActualResult`(){
        //given
        val error = Throwable("Error Form Data")
        coEvery { eventProductDetailUseCase.executeUseCase("", "", false, "") } returns Fail(error)

        //when
        eventPDPFormViewModel.getData("","","")

        //then
        assertNull(eventPDPFormViewModel.mFormData.value)
        assertNotNull(eventPDPFormViewModel.error)

        assert(eventPDPFormViewModel.error.value.equals(error.message))
    }

}