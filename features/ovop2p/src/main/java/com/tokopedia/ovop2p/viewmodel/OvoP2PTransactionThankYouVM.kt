package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.domain.usecase.OvoTrnxThankyouPageUseCase
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrPage
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrSnkBar
import com.tokopedia.ovop2p.view.viewStates.ThankYouPageState
import com.tokopedia.ovop2p.view.viewStates.ThankYouSucs
import javax.inject.Inject

class OvoP2PTransactionThankYouVM @Inject constructor(
    private val ovoTrnxThankyouPageUseCase: OvoTrnxThankyouPageUseCase
) : ViewModel() {

    var transferThankyouLiveData = MutableLiveData<ThankYouPageState>()

    override fun onCleared() {
        super.onCleared()
        ovoTrnxThankyouPageUseCase.cancelJobs()
    }

    fun makeThankyouDataCall(dataMap: HashMap<String, Any>) {
        ovoTrnxThankyouPageUseCase.getThankyouPageData(
            ::onSuccessThankYouData,
            ::onFailThankYou,
            dataMap
        )
    }

    private fun onSuccessThankYouData(ovoP2pTransferThankyouBase: OvoP2pTransferThankyouBase) {
        ovoP2pTransferThankyouBase.ovoP2pTransferThankyou?.let {
            it.errors?.let { errList ->
                if (errList.isNotEmpty()) {
                    errList[0].let { errMap ->
                        errMap[Constants.Keys.MESSAGE]?.let { errMsg ->
                            transferThankyouLiveData.value = ThankYouErrPage(errMsg)
                        }
                    }
                } else {
                    saveSuccessValue(ovoP2pTransferThankyouBase)
                }
            } ?: run {
                saveSuccessValue(ovoP2pTransferThankyouBase)
            }
        }?: run {
            transferThankyouLiveData.value = ThankYouErrSnkBar(GENERAL_ERROR)
        }
    }

    private fun saveSuccessValue(ovoP2pTransferThankyouBase: OvoP2pTransferThankyouBase) {
        transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferThankyouBase)
    }

    private fun onFailThankYou(throwable: Throwable?) {
        transferThankyouLiveData.value = ThankYouErrSnkBar(GENERAL_ERROR)
    }

    companion object {
        const val GENERAL_ERROR = "Ada yang salah. Silakan coba lagi"
    }



}
