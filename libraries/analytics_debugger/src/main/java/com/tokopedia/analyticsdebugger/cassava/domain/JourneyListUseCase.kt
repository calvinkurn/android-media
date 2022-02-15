package com.tokopedia.analyticsdebugger.cassava.domain

import android.content.Context
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.utils.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.list.ValidatorListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 20/04/2021
 */
class JourneyListUseCase @Inject constructor(
    private val context: Context,
    private val cassavaRepository: CassavaRepository
) {

    suspend fun execute(
        source: CassavaSource,
        tribeName: String = "",
        journeyName: String = ""
    ): List<Pair<String, String>> = withContext(Dispatchers.IO) {
        when (source) {
            CassavaSource.NETWORK -> cassavaRepository.getNetworkJourneyList(
                context.getString(com.tokopedia.keys.R.string.thanos_token_key),
                tribeName,
                journeyName
            )
            else -> Utils.listAssetFiles(context, ValidatorListFragment.TRACKER_ROOT_PATH).map {
                Pair(it, it)
            }.toList()
        }
    }

}