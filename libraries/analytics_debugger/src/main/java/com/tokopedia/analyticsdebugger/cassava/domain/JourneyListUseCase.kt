package com.tokopedia.analyticsdebugger.cassava.domain

import android.content.Context
import com.tokopedia.analyticsdebugger.cassava.data.CassavaRepository
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSource
import com.tokopedia.analyticsdebugger.cassava.di.CassavaQualifier
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.list.ValidatorListFragment
import javax.inject.Inject

/**
 * @author by furqan on 20/04/2021
 */
class JourneyListUseCase @Inject constructor(
        @CassavaQualifier private val context: Context,
        private val cassavaRepository: CassavaRepository) {

    fun execute(source: CassavaSource): List<Pair<String, String>> = when (source) {
        CassavaSource.NETWORK -> cassavaRepository.getNetworkJourneyList()
        else -> Utils.listAssetFiles(context, ValidatorListFragment.TRACKER_ROOT_PATH).map {
            Pair(it, it)
        }.toList()
    }

}