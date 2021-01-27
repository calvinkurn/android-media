package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT_IDS
import com.tokopedia.topads.common.data.internal.ParamObject.SEARCH_TERM
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.common.data.response.KeywordData
import com.tokopedia.topads.common.data.response.KeywordSearch
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.domain.usecase.SuggestionKeywordUseCase
import com.tokopedia.topads.create.R
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,November,2019
 */
class KeywordAdsViewModel @Inject constructor(
        private val context: Context,
        dispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val searchKeywordUseCase: GraphqlUseCase<KeywordSearch>,
        private val suggestionKeywordUseCase: SuggestionKeywordUseCase) : BaseViewModel(dispatcher.main) {


    fun getSuggestionKeyword(productIds: String, groupId: Int, onSuccess: ((List<KeywordData>) -> Unit), onEmpty: (() -> Unit)) {
        launch {
            suggestionKeywordUseCase.setParams(groupId, productIds)
            suggestionKeywordUseCase.executeQuerySafeMode(
                    {
                        if (it.topAdsGetKeywordSuggestionV3.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topAdsGetKeywordSuggestionV3.data)
                        }
                    },
                    {
                        it.printStackTrace()
                    }
            )
        }
    }

    fun searchKeyword(keyword: String, product_ids: String, onSucceed: (List<SearchData>) -> Unit) {
        GraphqlHelper.loadRawString(context.resources, R.raw.topads_gql_search_keywords)?.let { query ->
            val params = mapOf(PRODUCT_IDS to product_ids,
                    SEARCH_TERM to keyword, SHOP_id to userSession.shopId.toInt())

            searchKeywordUseCase.setTypeClass(KeywordSearch::class.java)
            searchKeywordUseCase.setRequestParams(params)
            searchKeywordUseCase.setGraphqlQuery(query)

            searchKeywordUseCase.execute(
                    onSuccessSearch(onSucceed),
                    onError()
            )

        }
    }


    private fun onSuccessSearch(onSucceed: (List<SearchData>) -> Unit): (KeywordSearch) -> Unit {
        return {
            onSucceed(it.topAdsKeywordSearchTerm.data)
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
        }

    }

}