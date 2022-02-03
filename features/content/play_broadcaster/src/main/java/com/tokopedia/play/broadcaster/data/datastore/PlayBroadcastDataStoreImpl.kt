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
                selectedProduct = mSetupDataStore.getSelectedProducts().map {
                    SerializableProductData(
                            id = it.id,
                            name = it.name,
                            imageUrl = it.imageUrl,
                            originalImageUrl = it.originalImageUrl,
                            hasStock = it.stock is StockAvailable,
                            totalStock = if (it.stock is StockAvailable) it.stock.stock else 0,
                            price = when(val price = it.price) {
                                is OriginalPrice -> price.price
                                is DiscountedPrice -> price.originalPrice
                                else -> ""
                            },
                            priceNumber = when(val price = it.price) {
                                is OriginalPrice -> price.priceNumber
                                is DiscountedPrice -> price.originalPriceNumber
                                else -> 0.0
                            },
                            discountedPrice = when(val price = it.price) {
                                is DiscountedPrice -> price.discountedPrice
                                else -> ""
                            },
                            discountedPriceNumber = when(val price = it.price) {
                                is DiscountedPrice -> price.discountedPriceNumber
                                else -> 0.0
                            },
                            discountedPercent = when(val price = it.price) {
                                is DiscountedPrice -> price.discountPercent
                                else -> 0
                            },
                        )
                },
                selectedCoverData = SerializableCoverData(
                        coverImageUriString = coverImage.toString(),
                        coverTitle = title,
                        coverSource = coverSource.sourceString,
                        productId = if (coverSource is CoverSource.Product) coverSource.id else null
                )
        )
    }

    override fun setSerializableData(data: SerializableHydraSetupData) {
        mSetupDataStore.setSelectedProducts(data.selectedProduct.map {
            ProductData(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    originalImageUrl = it.originalImageUrl,
                    stock = if (it.hasStock) StockAvailable(it.totalStock) else OutOfStock,
                    price = when {
                        it.discountedPercent != 0 && it.discountedPrice.isNotEmpty() -> DiscountedPrice(
                            originalPrice = it.price,
                            originalPriceNumber = it.priceNumber,
                            discountPercent = it.discountedPercent,
                            discountedPrice = it.discountedPrice,
                            discountedPriceNumber = it.discountedPriceNumber
                        )
                        it.discountedPercent == 0 && it.price.isNotEmpty() -> OriginalPrice(
                            price = it.price,
                            priceNumber = it.priceNumber
                        )
                        else -> PriceUnknown
                    }
            )
        })
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
    }
}