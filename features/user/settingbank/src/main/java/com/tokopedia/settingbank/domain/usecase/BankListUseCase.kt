package com.tokopedia.settingbank.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.settingbank.domain.GQL_GET_BANK_LIST
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.domain.model.GetBankListResponse
import com.tokopedia.settingbank.view.viewState.BankListState
import com.tokopedia.settingbank.view.viewState.OnBankListLoaded
import com.tokopedia.settingbank.view.viewState.OnBankListLoadingError
import javax.inject.Inject


@GqlQuery("GQLGetBankList", GQL_GET_BANK_LIST)
class BankListUseCase  @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GetBankListResponse>(graphqlRepository) {


    private val PARAM_CURRENT_PAGE = "page"
    private val PARAM_ITEM_PER_PAGE = "perPage"
    private var currentPage: Int = 1
    private var maxItemPerPage = 999

    fun getBankList(bankListResult: (BankListState) -> Unit) {
        setTypeClass(GetBankListResponse::class.java)
        setGraphqlQuery(GQLGetBankList.GQL_QUERY)
        setRequestParams(getBankListParams())
        execute({
            bankListResult(processLoadedBankList(it))
        }, {
            bankListResult(OnBankListLoadingError(it))
        })
    }

    private fun processLoadedBankList(it: GetBankListResponse): OnBankListLoaded {
        val bankList: ArrayList<Bank>? = it.bankListResponse?.bankData?.bankList
        return if (bankList.isNullOrEmpty()) {
            OnBankListLoaded(arrayListOf())
        } else {
            OnBankListLoaded(bankList = bankList)
        }
    }

    private fun getBankListParams() = mapOf(PARAM_CURRENT_PAGE to currentPage,
            PARAM_ITEM_PER_PAGE to maxItemPerPage)
}