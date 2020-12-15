package com.tokopedia.product.info.model.productdetail.uidata

import com.tokopedia.product.info.model.productdetail.response.DataShopNotes
import com.tokopedia.product.info.model.productdetail.response.Discussion

/**
 * Created by Yehezkiel on 13/10/20
 */
data class BottomSheetProductDetailInfoUiData(
        val listOfUi: List<ProductDetailInfoVisitable> = listOf(),

        //External datas
        val dataShopNotes: DataShopNotes = DataShopNotes(),
        val discussion: Discussion = Discussion()
)