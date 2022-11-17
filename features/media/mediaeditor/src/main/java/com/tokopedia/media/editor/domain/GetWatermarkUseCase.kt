package com.tokopedia.media.editor.domain

import android.graphics.Bitmap
import com.tokopedia.media.editor.data.repository.WatermarkFilterRepository
import com.tokopedia.media.editor.domain.base.UseCase
import com.tokopedia.media.editor.domain.mapper.EditorDetailEntityMapper.map
import com.tokopedia.media.editor.domain.param.WatermarkUseCaseParam
import javax.inject.Inject

class GetWatermarkUseCase @Inject constructor(
    private val watermarkRepository: WatermarkFilterRepository
) : UseCase<WatermarkUseCaseParam, Bitmap>() {
    override fun execute(params: WatermarkUseCaseParam): Bitmap {
        val element = params.element?.map()
        val bitmapResult = watermarkRepository.watermark(
            source = params.source,
            type = params.type,
            shopNameParam = params.shopNameParam,
            isThumbnail = params.isThumbnail,
            element = element,
            useStorageColor = params.useStorageColor
        )

        params.element?.watermarkMode = element?.watermarkModeEntityData
        return bitmapResult
    }
}