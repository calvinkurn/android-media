package com.tokopedia.ovop2p.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.constants.GQL_GET_WALLET_DETAIL
import com.tokopedia.ovop2p.domain.model.Wallet
import com.tokopedia.ovop2p.domain.model.WalletDataBase
import com.tokopedia.ovop2p.view.fragment.OvoP2PForm
import com.tokopedia.ovop2p.view.viewStates.WalletData
import javax.inject.Inject


@GqlQuery("WalletDetailQuery", GQL_GET_WALLET_DETAIL)
class GetWalletBalanceUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<WalletDataBase>(graphqlRepository) {

    fun getWalletDetail(
        onSuccess: (ResposeType) -> Unit,
        onError: (ResposeType) -> Unit
    ) {
        try {
            this.setTypeClass(WalletDataBase::class.java)
            this.setGraphqlQuery(WalletDetailQuery.GQL_QUERY)
            this.execute(
                { result ->
                    onSuccess(processSuccess(result))
                }, { error ->
                    onError(processErrorCase(error.message))
                }
            )
        } catch (throwable: Throwable) {
            processErrorCase(throwable.message)
        }
    }

    private fun processErrorCase(error: String?): ResposeType {
        return ResposeType.FailResponse(errorMessage = OvoP2PForm.GENERAL_ERROR)
    }

    private fun processSuccess(walletDataBase: WalletDataBase): ResposeType {
        walletDataBase.wallet?.let { walletObj ->
            walletObj.errors?.let { errList ->
                return if (errList.isNotEmpty()) {
                    ResposeType.FailResponse(errorMessage = (errList[0].message))
                } else {
                    onSuccessGetWalletDetail(walletObj)
                }
            } ?: kotlin.run {
                return onSuccessGetWalletDetail(walletObj)
            }
        } ?: kotlin.run {
            return ResposeType.FailResponse(errorMessage = OvoP2PForm.GENERAL_ERROR)
        }
    }

    private fun onSuccessGetWalletDetail(walletObj: Wallet): ResposeType {
        var cashBal = walletObj.cashBalance
        cashBal = Constants.Prefixes.SALDO + cashBal
        val sndrAmt = walletObj.rawCashBalance.toLong()
        return ResposeType.SuccessResponse(WalletData(cashBal, sndrAmt))
    }


    sealed class ResposeType {

        data class SuccessResponse(val walletData: WalletData) : ResposeType()
        data class FailResponse(val errorMessage: String?) : ResposeType()

    }

}