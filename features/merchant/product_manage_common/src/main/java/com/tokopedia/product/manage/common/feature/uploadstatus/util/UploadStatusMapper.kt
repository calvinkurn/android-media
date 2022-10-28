package com.tokopedia.product.manage.common.feature.uploadstatus.util

import com.tokopedia.product.manage.common.feature.uploadstatus.data.db.entity.UploadStatusEntity
import com.tokopedia.product.manage.common.feature.uploadstatus.data.model.UploadStatusModel

object UploadStatusMapper {

    fun UploadStatusModel.convertToEntity() = UploadStatusEntity(
        productId = this.productId,
        status = this.status
    )

    fun UploadStatusEntity.convertToModel() = UploadStatusModel(
        productId = this.productId,
        status = this.status
    )

}