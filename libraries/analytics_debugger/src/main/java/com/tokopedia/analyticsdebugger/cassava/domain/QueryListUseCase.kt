package com.tokopedia.analyticsdebugger.cassava.domain

import android.content.Context
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.CassavaQuery
import com.tokopedia.analyticsdebugger.cassava.validator.core.QueryMode
import com.tokopedia.analyticsdebugger.cassava.validator.core.toCassavaQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 07/04/2021
 */
class QueryListUseCase @Inject constructor(
        private val context: Context,
        private val repository: CassavaRepository) {

    suspend fun execute(source: CassavaSource,
                        filePath: String)
            : CassavaQuery? {
        return withContext(Dispatchers.IO) {
            if (source == CassavaSource.LOCAL) {
                Utils.getJsonDataFromAsset(context, filePath)?.toCassavaQuery()
            } else {
                repository.getNetworkQueryList(
                        filePath,
                        context.getString(com.tokopedia.keys.R.string.thanos_token_key)
                ).let {
                    val cassavaRegexList: MutableList<Pair<Int, Map<String, Any>>> = arrayListOf()

                    it.regexData.map { regex ->
                        cassavaRegexList.add(Pair(regex.dataLayerId, regex.dataLayer))
                    }

                    CassavaQuery(
                            mode = QueryMode.SUBSET,
                            readme = it.journeyName,
                            query = cassavaRegexList
                    )
                }
            }
        }
    }

}