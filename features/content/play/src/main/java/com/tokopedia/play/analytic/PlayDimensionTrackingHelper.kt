package com.tokopedia.play.analytic

import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.play.view.storage.PlayQueryParamStorage
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2023
 */
@PlayScope
class PlayDimensionTrackingHelper @Inject constructor(
    private val queryParamStorage: PlayQueryParamStorage,
){

    fun getDimension90(): String {
        val pageSourceName = queryParamStorage.pageSourceName.ifEmpty { NO_VALUE }

        val categoryId = if (queryParamStorage.pageSourceName == ApplinkConstInternalContent.SOURCE_TYPE_HOME)
            queryParamStorage.widgetId
        else
            NO_VALUE

        /** {page_source_name}.{banner_component_name}.{banner_name}.{category_id} */
        return "$pageSourceName.$NO_VALUE.$NO_VALUE.$categoryId"
    }

    companion object {
        private const val NO_VALUE = "null"
    }
}
