package com.tokopedia.content.product.preview.data.mapper

import com.tokopedia.content.product.preview.data.GetMiniProductInfoResponse
import com.tokopedia.content.product.preview.data.MediaReviewResponse
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel

/**
 * @author by astidhiyaa on 06/12/23
 */
interface ProductPreviewMapper {
    fun map(response: MediaReviewResponse): List<ReviewUiModel>

    fun map(response: GetMiniProductInfoResponse): BottomNavUiModel
}
