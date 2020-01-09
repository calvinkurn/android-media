package com.tokopedia.age_restriction

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.age_restriction.data.UserDOBUpdateData
import com.tokopedia.age_restriction.data.UserDOBUpdateResponse
import com.tokopedia.age_restriction.viewmodel.VerifyDOBViewModel
import com.tokopedia.tradein_common.repository.BaseRepository
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class VerifyDOBViewModelTest {

    private var repository: BaseRepository = mockk()
    private var requestParams: HashMap<String, Any> = mockk()
    private var application: Application = mockk()

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
        coEvery { repository.getGQLData(anyString(), UserDOBUpdateResponse::class.java, requestParams) } returns response
        coEvery { viewModel.generateDOBRequestparam(anyString(), anyString(), anyString()) } returns requestParams
        coEvery { response.userDobUpdateData.error } returns "sadfasd"

        viewModel.updateUserDoB(anyString(), anyString(), anyString(), anyString())

        assertFalse(viewModel.progBarVisibility.value ?: true)
        assertEquals(viewModel.warningMessage.value, response.userDobUpdateData.error)
        assertEquals(viewModel.userIsAdult.value, null)
        assertEquals(viewModel.userNotAdult.value, null)
    }

    @Test
    fun `check for adult`() {
        // change response isDobVerified = true and age > 18
        coEvery { repository.getGQLData(anyString(), UserDOBUpdateResponse::class.java, requestParams) } returns response
        coEvery { viewModel.generateDOBRequestparam(anyString(), anyString(), anyString()) } returns requestParams
        coEvery { response.userDobUpdateData.isDobVerified } returns true
        coEvery { response.userDobUpdateData.age } returns 20


        viewModel.updateUserDoB(anyString(), anyString(), anyString(), anyString())


        //viewModel.checkIfAdult(response)
        assertEquals(viewModel.userIsAdult.value, true)
        assertEquals(viewModel.userNotAdult.value, null)
    }

    @Test
    fun `check for not adult`() {
        // change response isDobVerified = true and age < 18
        coEvery { repository.getGQLData(anyString(), UserDOBUpdateResponse::class.java, requestParams) } returns response
        coEvery { viewModel.generateDOBRequestparam(anyString(), anyString(), anyString()) } returns requestParams
        coEvery { response.userDobUpdateData.isDobVerified } returns true
        coEvery { response.userDobUpdateData.age } returns 12


        viewModel.updateUserDoB(anyString(), anyString(), anyString(), anyString())

        assertNull(viewModel.userIsAdult.value)
        assertEquals(viewModel.userNotAdult.value, true)
    }

    @Test
    fun generateDOBRequestparamTest() {
        val dobDD = "22"
        val dobMM = "22"
        val dobYY = "22"
        val response = viewModel.generateDOBRequestparam(dobDD, dobMM, dobYY)

        assertEquals(response.get("bdayDD"), dobDD)
        assertEquals(response.get("bdayMM"), dobMM)
        assertEquals(response.get("bdayYY"), dobYY)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }

}