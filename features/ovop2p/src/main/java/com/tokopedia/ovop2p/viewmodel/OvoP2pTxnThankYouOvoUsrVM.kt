package com.tokopedia.ovop2p.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.domain.model.OvoP2pTransferThankyouBase
import com.tokopedia.ovop2p.domain.usecase.OvoTrnxThankyouPageUseCase
import com.tokopedia.ovop2p.view.fragment.TxnSucsOvoUser.Companion.GENERAL_ERROR
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrPage
import com.tokopedia.ovop2p.view.viewStates.ThankYouErrSnkBar
import com.tokopedia.ovop2p.view.viewStates.ThankYouPageState
import com.tokopedia.ovop2p.view.viewStates.ThankYouSucs
import javax.inject.Inject

class OvoP2pTxnThankYouOvoUsrVM @Inject constructor(
    private val ovoTrnxThankyouPageUseCase: OvoTrnxThankyouPageUseCase
) : ViewModel() {

    var transferThankyouLiveData = MutableLiveData<ThankYouPageState>()
    // private var transferThankyouSubscriber: Subscriber<GraphqlResponse>? = null

    override fun onCleared() {
        super.onCleared()
        ovoTrnxThankyouPageUseCase.cancelJobs()
//        if (transferThankyouSubscriber != null) {
//            transferThankyouSubscriber!!.unsubscribe()
//        }
    }

    fun makeThankyouDataCall(dataMap: HashMap<String, Any>) {
        //   OvoP2pUtil.executeOvoP2pTransferThankyou(context, getThankYouDataSubscriber(context), dataMap)
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
                    transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferThankyouBase)
                }
            } ?: kotlin.run {
                transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferThankyouBase)
            }
        }
    }

    private fun onFailThankYou(throwable: Throwable?) {
        transferThankyouLiveData.value = ThankYouErrSnkBar(GENERAL_ERROR)
    }

//    fun getThankYouDataSubscriber(context: Context): Subscriber<GraphqlResponse> {
//        transferThankyouSubscriber = object : Subscriber<GraphqlResponse>() {
//            override fun onCompleted() {
//            }
//
//            override fun onError(e: Throwable?) {
//                transferThankyouLiveData.value = ThankYouErrSnkBar(context.resources.getString(R.string.general_error))
//            }
//
//            override fun onNext(graphqlResponse: GraphqlResponse?) {
//                val ovoP2pTransferConfirmBase = graphqlResponse?.getData<OvoP2pTransferThankyouBase>(OvoP2pTransferThankyouBase::class.java)
//                ovoP2pTransferConfirmBase?.ovoP2pTransferThankyou?.let {
//                    it.errors?.let { errList ->
//                        if (errList.isNotEmpty()) {
//                            errList[0].let { errMap ->
//                                errMap[Constants.Keys.MESSAGE]?.let { errMsg ->
//                                    transferThankyouLiveData.value = ThankYouErrPage(errMsg)
//                                }
//                            }
//                        } else {
//                            transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferConfirmBase)
//                        }
//                    } ?: run{
//                        transferThankyouLiveData.value = ThankYouSucs(ovoP2pTransferConfirmBase)
//                    }
//                } ?: run {
//                    transferThankyouLiveData.value = ThankYouErrSnkBar(context.resources.getString(R.string.general_error))
//                }
//            }
//        }
//        return transferThankyouSubscriber as Subscriber<GraphqlResponse>
//    }


}
