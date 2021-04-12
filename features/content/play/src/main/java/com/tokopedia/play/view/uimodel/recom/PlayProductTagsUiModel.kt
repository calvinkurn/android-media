package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel

/**
 * Created by jegul on 28/01/21
 */
sealed class PlayProductTagsUiModel {

    abstract val basicInfo: PlayProductTagsBasicInfoUiModel

    data class Incomplete(override val basicInfo: PlayProductTagsBasicInfoUiModel) : PlayProductTagsUiModel()
    data class Complete(
            override val basicInfo: PlayProductTagsBasicInfoUiModel,
            val productList: List<PlayProductUiModel>,
            val voucherList: List<PlayVoucherUiModel>
    ) : PlayProductTagsUiModel()
}

data class PlayProductTagsBasicInfoUiModel(
        val bottomSheetTitle: String,
        val partnerId: Long,
        val maxFeaturedProducts: Int
)

fun PlayProductTagsUiModel.setContent(
        basicInfo: PlayProductTagsBasicInfoUiModel = this.basicInfo,
        productList: List<PlayProductUiModel> = emptyList(),
        voucherList: List<PlayVoucherUiModel> = emptyList()
) = PlayProductTagsUiModel.Complete(basicInfo, productList, voucherList)