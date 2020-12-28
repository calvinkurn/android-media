package com.tokopedia.topads.edit.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class KeywordAdsViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val context: Context,
        private val userSession: UserSessionInterface,
        private val searchKeywordUseCase: GraphqlUseCase<KeywordSearch>,
        private val suggestionKeywordUseCase: SuggestionKeywordUseCase) : BaseViewModel(dispatcher) {

    fun getSuggestionKeyword(productIds: String?, groupId: Int?, onSuccess: ((List<KeywordData>) -> Unit)) {

        suggestionKeywordUseCase.setParams(groupId, productIds?.trim())
        suggestionKeywordUseCase.executeQuerySafeMode({
            onSuccess(it.topAdsGetKeywordSuggestionV3.data)
        }, { throwable ->
            throwable.printStackTrace()
        })
    }

    fun searchKeyword(keyword:String,product_ids : String,onSucceed:(List<SearchData>)->Unit ){
        GraphqlHelper.loadRawString(context.resources, R.raw.topads_gql_search_keywords)?.let { query ->
            val params = mapOf(ParamObject.PRODUCT_IDS to product_ids,
                    ParamObject.SEARCH_TERM to keyword, ParamObject.SHOP_id to userSession.shopId.toInt())

            searchKeywordUseCase.setTypeClass(KeywordSearch::class.java)
            searchKeywordUseCase.setRequestParams(params)
            searchKeywordUseCase.setGraphqlQuery(query)

            searchKeywordUseCase.execute(
                    onSuccessSearch(onSucceed),
                    onError()
            )

        }
    }

    private fun onSuccessSearch(onSucceed: (List<SearchData>) -> Unit):(KeywordSearch)->Unit{
        return{
            onSucceed(it.topAdsKeywordSearchTerm.data)
        }
    }

    private fun onError(): (Throwable) -> Unit{
        return{
            it.printStackTrace()
        }

    }



    public override fun onCleared() {
        super.onCleared()
        suggestionKeywordUseCase.cancelJobs()
    }

}