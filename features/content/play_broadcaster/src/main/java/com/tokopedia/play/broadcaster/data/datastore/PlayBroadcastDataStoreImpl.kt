package com.tokopedia.play.broadcaster.data.datastore

import android.net.Uri
import com.tokopedia.play.broadcaster.data.model.HydraSetupData
import com.tokopedia.play.broadcaster.data.model.SerializableCoverData
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
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

    override fun getAllData(): HydraSetupData {
        val cover = mSetupDataStore.getSelectedCover()
        requireNotNull(cover)
        val (coverImage, coverSource) = when(val cropState = cover.croppedCover) {
            is CoverSetupState.Cropped -> cropState.coverImage to cropState.coverSource
            else -> throw IllegalStateException("Cover in this state should have been cropped")
        }

        return HydraSetupData(
                selectedProduct = mSetupDataStore.getSelectedProducts(),
                selectedCoverData = SerializableCoverData(
                        coverImageUriString = coverImage.toString(),
                        coverTitle = cover.title,
                        coverSource = coverSource.sourceString,
                        productId = if (coverSource is CoverSource.Product) coverSource.id else null
                )
        )
    }

    override fun setAllData(data: HydraSetupData) {
        mSetupDataStore.setSelectedProducts(data.selectedProduct)
        mSetupDataStore.setFullCover(
                PlayCoverUiModel(
                        croppedCover = CoverSetupState.Cropped.Uploaded(
                                localImage = null,
                                coverImage = Uri.parse(data.selectedCoverData.coverImageUriString),
                                coverSource = CoverSource.getFromSourceString(data.selectedCoverData.coverSource, data.selectedCoverData.productId)
                        ),
                        title = data.selectedCoverData.coverTitle,
                        state = SetupDataState.Uploaded
                )
        )
    }
}