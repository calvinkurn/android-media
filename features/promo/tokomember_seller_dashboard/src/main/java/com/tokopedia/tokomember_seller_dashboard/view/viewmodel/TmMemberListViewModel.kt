package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmMemberListUsecase
import com.tokopedia.tokomember_seller_dashboard.model.DataModel
import com.tokopedia.tokomember_seller_dashboard.model.MemberListInfiniteLoader
import com.tokopedia.tokomember_seller_dashboard.model.TmMemberListResponse
import com.tokopedia.tokomember_seller_dashboard.util.InfiniteListResult
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.MemberListMapper
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmMemberListViewModel @Inject constructor(
    private val memberListUseCase:TmMemberListUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _tmMemberListInitialResult = MutableLiveData<TokoLiveDataResult<TmMemberListResponse>>()
    val tmMemberListInitialResult:LiveData<TokoLiveDataResult<TmMemberListResponse>> = _tmMemberListInitialResult

    private val _tmMemberList  = MutableLiveData<InfiniteListResult<DataModel>>()
    val tmMemberList:LiveData<InfiniteListResult<DataModel>>  = _tmMemberList

    private var page = 1
    var hasMorePages:Boolean? = true

    fun getInitialMemberList(shopId:Int){
        _tmMemberListInitialResult.postValue(TokoLiveDataResult.loading())
        memberListUseCase.getShopMemberList(
            shopId,
            1,
            success = {
                _tmMemberListInitialResult.postValue(TokoLiveDataResult.success(it))
            },
            failure = {
                _tmMemberListInitialResult.postValue(TokoLiveDataResult.error(it))
            }
        )
    }

    fun mapInitialMemberList(){
        tmMemberListInitialResult.value?.data?.let {
            val mappedList = MemberListMapper.getMemberListAdapterModel(it)
            hasMorePages = it.membershipGetUserCardMemberList?.paging?.hasNext
            _tmMemberList.postValue(InfiniteListResult.notLoading(mappedList))
        }
    }

    fun getMoreMembers(shopId: Int){
            page++
            addInfiniteLoader()
            memberListUseCase.getShopMemberList(
                shopId,
                page,
                success = {
                    removeInfiniteLoader()
                    val mappedList  = MemberListMapper.getMemberListAdapterModel(it)
                    hasMorePages = it.membershipGetUserCardMemberList?.paging?.hasNext
                    tmMemberList.value?.list?.let { it1 ->
                        val mL = it1.toMutableList()
                        mappedList.forEach { it2 -> mL.add(it2) }
                        _tmMemberList.postValue(InfiniteListResult.notLoading(mL))
                    }
                },
                failure = {
                    removeInfiniteLoader()
                }
            )

    }

    private fun addInfiniteLoader(){
        tmMemberList.value?.let{
            it.list?.let{ it1 ->
                if(it1.isNotEmpty()){
                    val mL = it1.toMutableList()
                    mL.add(MemberListInfiniteLoader())
                    _tmMemberList.value = InfiniteListResult.infiniteLoading(mL)
                }
            }
        }
    }

    private fun removeInfiniteLoader(){
        tmMemberList.value?.let{
            it.list?.let{ it1 ->
                if(it1.isNotEmpty() && it1.last() is MemberListInfiniteLoader){
                    val mL = it1.toMutableList()
                    mL.removeLast()
                    _tmMemberList.value = InfiniteListResult.loadingStopped(mL)
                }
            }
        }
    }

}
