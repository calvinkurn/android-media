package com.tokopedia.attachproduct.domain.model.mapper

import com.tokopedia.attachproduct.data.model.mapper.mapToAttachUiModel
import com.tokopedia.attachproduct.domain.model.NewAttachProductDomainModel
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel

fun NewAttachProductDomainModel.toDomainModelMapper(): List<NewAttachProductItemUiModel> {
   return this.productNews.map { product ->
       product.mapToAttachUiModel()
   }
}
