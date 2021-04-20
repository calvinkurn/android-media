package com.tokopedia.analyticsdebugger.cassava.domain

import android.content.Context
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.CassavaQuery
import com.tokopedia.analyticsdebugger.cassava.validator.core.QueryMode
import com.tokopedia.analyticsdebugger.cassava.validator.core.toCassavaQuery
import javax.inject.Inject

/**
 * @author by furqan on 07/04/2021
 */
class QueryListUseCase @Inject constructor(private val repository: CassavaRepository) {

    suspend fun execute(context: Context,
                        source: CassavaSource,
                        filePath: String)
            : CassavaQuery? {
        return if (source == CassavaSource.LOCAL) {
            Utils.getJsonDataFromAsset(context, filePath)?.toCassavaQuery()
        } else {
            repository.getNetworkQueryList(filePath).let {
                val cassavaRegexList: MutableList<Pair<Int, Map<String, Any>>> = arrayListOf()

                it.regexData.map { regex ->
                    cassavaRegexList.add(Pair(regex.dataLayerId, regex.dataLayer))
                }

                CassavaQuery(
                        mode = QueryMode.EXACT,
                        readme = it.journeyName,
                        query = cassavaRegexList
                )
            }
        }
    }

}