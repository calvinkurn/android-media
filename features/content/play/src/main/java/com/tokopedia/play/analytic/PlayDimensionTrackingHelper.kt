package com.tokopedia.play.analytic

import com.tokopedia.play.data.storage.PlayPageSourceStorage
import com.tokopedia.play.di.PlayScope
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on August 22, 2023
 */
@PlayScope
class PlayDimensionTrackingHelper @Inject constructor(
    private val pageSourceStorage: PlayPageSourceStorage,
){

    fun getDimension90(
        categoryId: String = NO_VALUE,
    ): String {
        /** {page_source_name}.{banner_component_name}.{banner_name}.{category_id} */
        return "${pageSourceStorage.pageSource}.$NO_VALUE.$NO_VALUE.$categoryId"
    }

    companion object {
        private const val NO_VALUE = "null"
    }
}
