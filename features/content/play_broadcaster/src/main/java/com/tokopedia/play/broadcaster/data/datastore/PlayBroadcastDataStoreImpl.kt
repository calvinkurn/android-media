package com.tokopedia.play.broadcaster.data.datastore

import android.net.Uri
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.data.model.SerializableCoverData
import com.tokopedia.play.broadcaster.data.model.SerializableHydraSetupData
import com.tokopedia.play.broadcaster.data.model.SerializableProductData
import com.tokopedia.play.broadcaster.error.ClientException
import com.tokopedia.play.broadcaster.error.PlayErrorCode
import com.tokopedia.play.broadcaster.type.*
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import javax.inject.Inject

/**
 * Created by jegul on 22/06/20
 */
class PlayBroadcastDataStoreImpl @Inject constructor(
        private val mSetupDataStore: PlayBroadcastSetupDataStore
) : PlayBroadcastDataStore {

    override fun setFromSetupStore(setupDataStore: PlayBroadcastSetupDataStore) {
        mSetupDataStore.overwrite(setupDataStore)
    }

    override fun getSetupDataStore(): PlayBroadcastSetupDataStore {
        return mSetupDataStore
    }

    override fun getSerializableData(): SerializableHydraSetupData {
        val cover = mSetupDataStore.getSelectedCover()
        requireNotNull(cover)
        val (coverImage, coverSource) = when(val cropState = cover.croppedCover) {
            is CoverSetupState.Cropped -> cropState.coverImage to cropState.coverSource
            else -> throw ClientException(PlayErrorCode.Play001)
        }

        val title = when(val title = mSetupDataStore.getTitle()) {
            is PlayTitleUiModel.HasTitle -> title.title
            else -> throw ClientException(PlayErrorCode.Play002)
        }

        return SerializableHydraSetupData(
                selectedCoverData = SerializableCoverData(
                        coverImageUriString = coverImage.toString(),
                        coverTitle = title,
                        coverSource = coverSource.sourceString,
                        productId = if (coverSource is CoverSource.Product) coverSource.id else null
                )
        )
    }

    override fun setSerializableData(data: SerializableHydraSetupData) {
        mSetupDataStore.setFullCover(
                PlayCoverUiModel(
                        croppedCover = CoverSetupState.Cropped.Uploaded(
                                localImage = null,
                                coverImage = Uri.parse(data.selectedCoverData.coverImageUriString),
                                coverSource = CoverSource.getFromSourceString(data.selectedCoverData.coverSource, data.selectedCoverData.productId)
                        ),
                        state = SetupDataState.Uploaded
                )
        )
        mSetupDataStore.setTitle(title = data.selectedCoverData.coverTitle)
    }
}