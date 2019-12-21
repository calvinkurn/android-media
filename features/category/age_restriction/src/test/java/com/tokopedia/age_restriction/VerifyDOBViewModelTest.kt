package com.tokopedia.age_restriction

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.JsonObject
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.tokopedia.age_restriction.data.UserDOBResponse
import com.tokopedia.age_restriction.data.UserDOBUpdateData
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import com.tokopedia.graphql.CommonUtils
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tradein_common.repository.BaseRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.io.File
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class VerifyDOBViewModelTest {

    var repository: BaseRepository = mockk()
    var requestParams: HashMap<String, Any> = mockk()
    var application: Application = mockk()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel: VerifyDOBViewModel by lazy {
        spyk(VerifyDOBViewModel(application))
    }

    @RelaxedMockK
    lateinit var response: UserDOBUpdateResponse

    val data = mockk<UserDOBUpdateData>()
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { viewModel.getRepo() } returns repository
    }

    @Test
    fun testDataMethod() {
        coEvery { viewModel.generateDOBRequestparam(anyString(), anyString(), anyString()) } returns requestParams
        coEvery { repository.getGQLData(anyString(), UserDOBUpdateResponse::class.java, requestParams) } returns response
        viewModel.updateUserDoB("", "", "", "")
        coVerify {
            repository.getGQLData(anyString(), UserDOBUpdateResponse::class.java, requestParams)
        }
    }

    @Test
    fun testDataMethodFails() {
        //given
        val throwable = Throwable("adfad")
        coEvery { viewModel.generateDOBRequestparam(anyString(), anyString(), anyString()) } returns requestParams
        coEvery { repository.getGQLData(anyString(), UserDOBUpdateResponse::class.java, requestParams) } throws throwable
        //when
        viewModel.updateUserDoB(anyString(), anyString(), anyString(), anyString())
        //then
        assertEquals(viewModel.warningMessage.value, throwable.localizedMessage)
    }

    @Test
    fun `check whether response error is not null`() {
        coEvery { response.userDobUpdateData.error } returns "sadfasd"
        viewModel.checkIfAdult(response)
        assertFalse(viewModel.progBarVisibility.value ?: true)
        assertEquals(viewModel.warningMessage.value, response.userDobUpdateData.error)
        assertEquals(viewModel.userIsAdult.value, null)
        assertEquals(viewModel.userNotAdult.value, null)
    }

    @Test
    fun `check for adult`() {
        // change response isDobVerified = true and age > 18
        coEvery { response.userDobUpdateData.isDobVerified } returns true
        coEvery { response.userDobUpdateData.age } returns 20
        viewModel.checkIfAdult(response)
        assertEquals(viewModel.userIsAdult.value, true)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }

    private fun getJsonFromFile(path: String): String {
        val uri = ClassLoader.getSystemClassLoader().getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }
}