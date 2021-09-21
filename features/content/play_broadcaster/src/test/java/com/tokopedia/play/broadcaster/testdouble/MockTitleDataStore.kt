package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.datastore.TitleDataStore
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.util.TestDoubleModelBuilder
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Created by jegul on 11/05/21
 */
open class MockTitleDataStore(
        dispatchers: CoroutineDispatchers = CoroutineTestDispatchers
) : TitleDataStore {

    var isUploaded: Boolean = false

    private val testModelBuilder = TestDoubleModelBuilder()
    private val titleDataStore = testModelBuilder.buildRealTitleDataStore(dispatchers)

    override fun getObservableTitle(): Flow<PlayTitleUiModel> {
        return titleDataStore.getObservableTitle()
    }

    override fun getTitle(): PlayTitleUiModel {
        return titleDataStore.getTitle()
    }

    override fun setTitle(title: String) {
        return titleDataStore.setTitle(title)
    }

    override suspend fun uploadTitle(channelId: String): NetworkResult<Unit> {
        isUploaded = true
        return titleDataStore.uploadTitle(channelId)
    }
}