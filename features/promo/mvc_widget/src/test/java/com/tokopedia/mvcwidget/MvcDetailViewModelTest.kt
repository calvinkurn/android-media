package com.tokopedia.mvcwidget

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.mvcwidget.usecases.CatalogMVCListUseCase
import com.tokopedia.mvcwidget.usecases.FollowShopUseCase
import com.tokopedia.mvcwidget.usecases.MembershipRegisterUseCase
import io.mockk.*
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.json.JSONObject
import org.spekframework.spek2.Spek
import java.io.File
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MvcDetailViewModelTest : Spek({

    InstantTaskExecutorRuleSpek(this)
    lateinit var viewModel :MvcDetailViewModel
//    lateinit var context: Context
    val gson = Gson()
    val dispatcher = TestCoroutineDispatcher()
    val catalogMVCListUseCase: CatalogMVCListUseCase = mockk()
    val membershipRegisterUseCase: MembershipRegisterUseCase = mockk()
    val followUseCase: FollowShopUseCase = mockk()

    fun getViewModel():MvcDetailViewModel {
        val vm = MvcDetailViewModel(dispatcher,catalogMVCListUseCase,membershipRegisterUseCase,followUseCase)
        return (vm)
    }

    beforeEachTest {
        viewModel = getViewModel()
//        context = mockk()
//        mockkStatic(GraphqlHelper::class)
//        every { GraphqlHelper.loadRawString(any(), any()) } returns ""
//        every { GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_gratif_notification) } returns ""
//        every { GraphqlHelper.loadRawString(context.resources, R.raw.t_promo_hachiko_coupon) } returns ""
    }

    fun <T> getMockResponse(fileName: String, clazz: Class<T>): T {
        val jsonFile = File(javaClass.getResource("/assets/$fileName.json")!!.path)
        return gson.fromJson(JSONObject(jsonFile.readText()).getJSONObject("data").toString(), clazz)
    }



    group("get list data"){

        test("response is not null"){
            val shopId = "TEST"
            val tokopointsCatalogMVCListResponse:TokopointsCatalogMVCListResponse = getMockResponse("mvc_tokopoints_catalog_mvc_list_response", TokopointsCatalogMVCListResponse::class.java)
            viewModel.listLiveData.observeForever {  }
            every { catalogMVCListUseCase.getQueryParams(shopId) } returns HashMap()
            coEvery { catalogMVCListUseCase.getResponse(any()) } returns tokopointsCatalogMVCListResponse

            viewModel.getListData(shopId)

            coVerify {
                viewModel.shopId == shopId
                viewModel.listLiveData.postValue(LiveDataResult.loading())
                catalogMVCListUseCase.getResponse(catalogMVCListUseCase.getQueryParams(shopId))
                viewModel.membershipRegistrationSuccessMessage == tokopointsCatalogMVCListResponse.data!!.toasterSuccessMessage!!
                viewModel.membershipCardID == tokopointsCatalogMVCListResponse.data?.followWidget?.membershipCardID
                viewModel.listLiveData.postValue(LiveDataResult.success(tokopointsCatalogMVCListResponse))
            }

            assertEquals(viewModel.listLiveData.value?.status , LiveDataResult.STATUS.SUCCESS)

        }
    }
})