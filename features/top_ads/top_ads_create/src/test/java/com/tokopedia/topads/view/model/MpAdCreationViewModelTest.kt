package com.tokopedia.topads.view.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.topads.common.domain.model.TopadsShopInfoV2Model
import com.tokopedia.topads.common.domain.usecase.TopadsGetShopInfoUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.instanceOf


@ExperimentalCoroutinesApi
class MpAdCreationViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private var viewModel:MpAdCreationViewModel? = null
    private val getShopInfoUseCase = mockk<TopadsGetShopInfoUseCase>()

    @Before
    fun setup(){
        viewModel = MpAdCreationViewModel(rule.dispatchers,getShopInfoUseCase)
    }

    @Test
    fun `get shop info success`(){
        val shopId = ""
        val response = TopadsShopInfoV2Model()
        every { getShopInfoUseCase.getShopInfo(any(),any(),"") }.answers{
            firstArg<(TopadsShopInfoV2Model) -> Unit>().invoke(response)
        }
        viewModel?.getShopInfo(shopId)
        Assert.assertThat(viewModel?.shopInfoResult?.value, instanceOf(Success::class.java))
        Assert.assertEquals((viewModel?.shopInfoResult?.value as Success).data,response)
    }

    @Test
    fun `get shop info failure`(){
        val shopId = ""
        val error = Throwable("Error")
        every { getShopInfoUseCase.getShopInfo(any(),any(),"") }.answers{
            secondArg<(Throwable) -> Unit>().invoke(error)
        }
        viewModel?.getShopInfo(shopId)
        Assert.assertThat(viewModel?.shopInfoResult?.value, instanceOf(Fail::class.java))
        Assert.assertEquals((viewModel?.shopInfoResult?.value as Fail).throwable,error)
    }
}
