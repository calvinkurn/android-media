package com.tokopedia.attachproduct.domain.model.mapper

import com.tokopedia.attachproduct.data.model.mapper.mapToAttachUiModel
import com.tokopedia.attachproduct.domain.model.NewAttachProductDomainModel
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel

fun NewAttachProductDomainModel.toDomainModelMapper(): List<AttachProductItemUiModel> {
   return this.products.map { product ->
       product.mapToAttachUiModel()
   }
}
