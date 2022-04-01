package com.tokopedia.attachproduct.domain.model.mapper

import com.tokopedia.attachproduct.data.model.mapper.mapToAttachUiModel
import com.tokopedia.attachproduct.domain.model.AttachProductDomainModel
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel

fun AttachProductDomainModel.toDomainModelMapper(): List<AttachProductItemUiModel> {
   return this.products.map { product ->
       product.mapToAttachUiModel()
   }
}
