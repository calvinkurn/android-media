package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TmMemberListUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmMemberListResponse
import com.tokopedia.tokomember_seller_dashboard.model.UserCardMember
import com.tokopedia.tokomember_seller_dashboard.util.InfiniteListResult
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmMemberListViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TmMemberListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel : TmMemberListViewModel
    lateinit var memberListUseCase: TmMemberListUsecase
    lateinit var dispatcher : TestCoroutineDispatcher
    lateinit var throwable: Throwable

    @Before
    fun setup(){
        memberListUseCase = mockk()
        dispatcher = TestCoroutineDispatcher()
        viewModel = spyk(TmMemberListViewModel(memberListUseCase, dispatcher))
        throwable = Throwable("Exception")
    }

    @Test
    fun `initial member list success`(){
        val memberListResponse:TmMemberListResponse = mockk()
        every {
            memberListUseCase.getShopMemberList(any(),any(),any(),any(),any())
        } answers {
           arg<(TmMemberListResponse) -> Unit>(3).invoke(memberListResponse)
        }
        viewModel.getInitialMemberList(0)
        assertEquals(viewModel.tmMemberListInitialResult.value?.status,TokoLiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun `initial member list failure`(){
        every {
            memberListUseCase.getShopMemberList(any(),any(),any(),any(),any())
        } answers {
            arg<(Throwable) -> Unit>(4).invoke(throwable)
        }
        viewModel.getInitialMemberList(0)
        assertEquals(viewModel.tmMemberListInitialResult.value?.status,TokoLiveDataResult.STATUS.ERROR)
    }

    @Test
    fun `map initial member list`(){
        val memberListResponse = TmMemberListResponse()
        every {
            memberListUseCase.getShopMemberList(any(),any(),any(),any(),any())
        } answers {
            arg<(TmMemberListResponse) -> Unit>(3).invoke(memberListResponse)
        }
        viewModel.getInitialMemberList(0)
        viewModel.mapInitialMemberList()
        assertEquals(viewModel.tmMemberList.value?.status,InfiniteListResult.InfiniteState.NOT_LOADING)
    }

    @Test
    fun `get more members success`(){
        val memberListResponse = TmMemberListResponse()
        every {
            memberListUseCase.getShopMemberList(any(),any(),any(),any(),any())
        } answers {
            arg<(TmMemberListResponse) -> Unit>(3).invoke(memberListResponse)
        }
        viewModel.getInitialMemberList(0)
        viewModel.mapInitialMemberList()
        viewModel.getMoreMembers(0)
        assertEquals(viewModel.tmMemberList.value?.status,InfiniteListResult.InfiniteState.NOT_LOADING)
    }


    @Test
    fun `add infinite loader`(){
        val memberListResponse = mockk<TmMemberListResponse>()
        every {
            memberListResponse.membershipGetUserCardMemberList?.userCardMemberList?.userCardMember
        } returns mutableListOf<UserCardMember>().apply { add(UserCardMember()) }
        every { memberListResponse.membershipGetUserCardMemberList?.paging?.hasNext } returns true
        every {
            memberListUseCase.getShopMemberList(any(),any(),any(),any(),any())
        } answers {
            val size = memberListResponse.membershipGetUserCardMemberList?.userCardMemberList?.userCardMember?.size
            if(size == null || size == 1){
                arg<(TmMemberListResponse) -> Unit>(3).invoke(memberListResponse)
            }
        }
        viewModel.getInitialMemberList(0)
        viewModel.mapInitialMemberList()
        viewModel.getMoreMembers(0)
        assertEquals(viewModel.tmMemberList.value?.list?.size,2)
    }

}
