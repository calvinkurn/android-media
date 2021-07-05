package com.tokopedia.travelhomepage.homepage.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.travelhomepage.homepage.data.*
import com.tokopedia.travelhomepage.homepage.usecase.GetEmptyModelsUseCase
import com.tokopedia.travelhomepage.homepage.usecase.GetSubhomepageUnifiedDataUseCase
import com.tokopedia.travelhomepage.shouldBe
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.reflect.Type


/**
 * @author by furqan on 04/02/2020
 */

class TravelHomepageViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val travelDispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var viewModel: TravelHomepageViewModel

    @RelaxedMockK
    lateinit var emptyUseCase: GetEmptyModelsUseCase

    @RelaxedMockK
    lateinit var getSubhomepageUnifiedDataUseCase: GetSubhomepageUnifiedDataUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TravelHomepageViewModel(emptyUseCase, travelDispatcherProvider, getSubhomepageUnifiedDataUseCase)
    }

    @Test
    fun getListFromCloud_ReturnListOfModel_valueShouldBeListWithTwoItem() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        // when
        viewModel.getListFromCloud("", true)

        // then
        viewModel.travelItemList.size shouldBe 2
        viewModel.travelItemList.forEach {
            it.isLoaded shouldBe false
            it.isLoadFromCloud shouldBe true
        }
        viewModel.isAllError.value shouldBe null
    }

    @Test
    fun getListFromCloud_FailedToFetchLayout_LayoutShouldBeNull() {
        // given
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Fail(Throwable())

        // when
        viewModel.getListFromCloud("", true)

        // then
        viewModel.travelItemList.size shouldBe 0
        viewModel.isAllError.value shouldBe true
    }

    @Test
    fun onGetTravelUnifiedData_AllSuccess_ItemModelShouldRepresentFailedToFetch() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        coEvery {
            getSubhomepageUnifiedDataUseCase.execute(any(), any(), any(), any())
        } returns listOf()

        // when
        runBlocking {
            viewModel.getListFromCloud("", true)
            viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 0),true, TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
            viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 1), true, TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)
        }

        // then
        Thread.sleep(1000)
        viewModel.isAllError.value shouldBe null
        viewModel.travelItemList.let {
            it[0].isLoaded shouldBe true
            it[1].isLoaded shouldBe true

            it[0].isSuccess shouldBe true
            it[1].isSuccess shouldBe true
        }
    }

    @Test
    fun onGetTravelUnifiedData_FailedToFetchData_ItemModelShouldRepresentFailedToFetch() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        coEvery {
            getSubhomepageUnifiedDataUseCase.execute(any(), any(), any(), any())
        } throws Throwable()

        // when
        viewModel.getListFromCloud("", true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 0), true, TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 1), true, TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)

        // then
        Thread.sleep(1000)
        viewModel.isAllError.value shouldBe true
        viewModel.travelItemList.let {
            it[0].isLoaded shouldBe true
            it[1].isLoaded shouldBe true

            it[0].isSuccess shouldBe false
            it[1].isSuccess shouldBe false
        }
    }

    @Test
    fun onGetTravelUnifiedData_IfPartialSuccess_ItemModelShouldRepresentPartialSuccess() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())
        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        coEvery {
            getSubhomepageUnifiedDataUseCase.execute(any(), any(), any(), TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
        } returns listOf()

        coEvery {
            getSubhomepageUnifiedDataUseCase.execute(any(), any(), any(), TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)
        } throws Exception()

        // when
        viewModel.getListFromCloud("", true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 0), true, TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 1), true, TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)

        // then
        Thread.sleep(1000)
        viewModel.isAllError.value shouldBe null
        viewModel.travelItemList.let {
            it[0].isLoaded shouldBe true
            it[1].isLoaded shouldBe true

            it[0].isSuccess shouldBe true
            it[1].isSuccess shouldBe false
        }
    }

    @Test
    fun checkIfAllError_ifAllLoadedAndSuccess_ValueShouldReturnNull() {
        // given
        val list = mutableListOf<TravelHomepageItemModel>()
        list.add(TravelHomepageProductCardModel())
        list.add(TravelHomepageLegoBannerModel())

        coEvery {
            emptyUseCase.getTravelLayoutSubhomepage(any(), any())
        } returns Success(list)

        coEvery {
            getSubhomepageUnifiedDataUseCase.execute(any(), any(), any(), TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
        } returns listOf()

        coEvery {
            getSubhomepageUnifiedDataUseCase.execute(any(), any(), any(), TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)
        } returns listOf()

        //when
        viewModel.getListFromCloud("", true)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 0), true, TypeUnifiedSubhomepageResponse.ProductCardResponse::class.java)
        viewModel.getTravelUnifiedData("", TravelLayoutSubhomepage.Data(position = 1), true, TypeUnifiedSubhomepageResponse.LegoBannerResponse::class.java)

        // then
        // then
        Thread.sleep(1000)
        viewModel.isAllError.value shouldBe null
        viewModel.travelItemList.let {
            it[0].isLoaded shouldBe true
            it[1].isLoaded shouldBe true

            it[0].isSuccess shouldBe true
            it[1].isSuccess shouldBe true
        }
    }
}