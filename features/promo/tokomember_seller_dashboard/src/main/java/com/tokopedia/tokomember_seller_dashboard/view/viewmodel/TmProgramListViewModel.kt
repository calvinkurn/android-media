package com.tokopedia.tokomember_seller_dashboard.view.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramListUsecase
import com.tokopedia.tokomember_seller_dashboard.model.LayoutType
import com.tokopedia.tokomember_seller_dashboard.model.ProgramItem
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmProgramListViewModel @Inject constructor(
    private val tokomemberDashGetProgramListUsecase: TokomemberDashGetProgramListUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    ): BaseViewModel(dispatcher) {


    private val _tmRefreshProgramListLiveData = MutableLiveData<Int>()
    val tmRefreshProgramListLiveData: LiveData<Int> = _tmRefreshProgramListLiveData

    private val _tmProgramListLoadingStateLiveData = MutableLiveData<Int>()
    val tmProgramListLoadingStateLiveData: LiveData<Int> = _tmProgramListLoadingStateLiveData

    private val _tokomemberProgramListResultLiveData = MutableLiveData<TokoLiveDataResult<ProgramList>>()
    val tokomemberProgramListResultLiveData: LiveData<TokoLiveDataResult<ProgramList>> = _tokomemberProgramListResultLiveData

    val programList:MutableList<ProgramItem> = mutableListOf()

    fun refreshProgramList(state: Int){
        _tmRefreshProgramListLiveData.postValue(state)
    }

    fun programListLoadingState(state: Int){
        _tmProgramListLoadingStateLiveData.postValue(state)
    }

    fun getProgramList(shopId: Int, cardID: Int, status: Int = -1, page: Int = 1, pageSize: Int = 10){
        tokomemberDashGetProgramListUsecase.cancelJobs()
        if(page>1){
            addLoader()
            _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.infiniteLoading())
        }
        else {
            _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.loading())
        }
        tokomemberDashGetProgramListUsecase.getProgramList({
            if(page>1) removeLoader()
            if(page==1){
                programList.clear()
            }
            it.membershipGetProgramList?.programSellerList?.let { it1 ->
                it1.forEach { it2 ->
                    it2?.let{it3 -> programList.add(ProgramItem(it3)) }
                }
            }
            _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.error(it))
        }, shopId, cardID, status, page, pageSize)
    }

    private fun addLoader(){
        programList.add(ProgramItem(ProgramSellerListItem(), LayoutType.LOADER))
    }

    private fun removeLoader() {
        if(programList.isNotEmpty() && programList.last().layoutType == LayoutType.LOADER) {
            programList.removeLast()
        }
    }

}
