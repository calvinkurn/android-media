package com.tokopedia.product.addedit.mapper

import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.draft.data.model.description.VideoLinkListModel

fun mapProductInputModelDetailToDraft(productInputModel: ProductInputModel): ProductDraft {
    val productDraft = ProductDraft()
    productDraft.detailInputModel.productName = productInputModel.detailInputModel.productName
    productDraft.detailInputModel.categoryId = productInputModel.detailInputModel.categoryId
    productDraft.detailInputModel.price = productInputModel.detailInputModel.price.toFloat()
    productDraft.detailInputModel.stock = productInputModel.detailInputModel.stock
    productDraft.detailInputModel.minOrder = productInputModel.detailInputModel.minOrder
    productDraft.detailInputModel.condition = productInputModel.detailInputModel.sku
    productDraft.detailInputModel.sku = productInputModel.detailInputModel.condition
    productDraft.detailInputModel.imageUrlOrPathList = productInputModel.detailInputModel.imageUrlOrPathList
    productDraft.detailInputModel.preorder.apply {
        duration = productInputModel.detailInputModel.preorder.duration
        timeUnit = productInputModel.detailInputModel.preorder.timeUnit
        isActive = productInputModel.detailInputModel.preorder.isActive
    }
    productInputModel.detailInputModel.wholesaleList.map { wholeSale ->
        productDraft.detailInputModel.wholesaleList.map { wholeSaleDraft ->
            wholeSaleDraft.price = wholeSale.price
            wholeSaleDraft.quantity = wholeSale.price
        }
    }

    productDraft.descriptionInputModel.apply {
        productDescription = productInputModel.descriptionInputModel.productDescription
        val videoLinkList = MutableList(productInputModel.descriptionInputModel.videoLinkList.size) { VideoLinkListModel() }
        productInputModel.descriptionInputModel.videoLinkList.forEach { videoLink ->
            val id = videoLink.inputId
            val image  = videoLink.inputImage
            val url = videoLink.inputUrl
            videoLinkList.add(VideoLinkListModel(id,url,image))
        }
        this.videoLinkList = videoLinkList
    }

    productDraft.shipmentInputModel.apply {
        isMustInsurance = productInputModel.shipmentInputModel.isMustInsurance
        weight = productInputModel.shipmentInputModel.weight
        weightUnit = productInputModel.shipmentInputModel.weightUnit
    }

    return productDraft
}


fun mapDraftToProductInputModel(productDraft: ProductDraft): ProductInputModel {
    val productInputModel = ProductInputModel()
    productInputModel.detailInputModel.apply {
        productName = productDraft.detailInputModel.productName
        categoryId = productDraft.detailInputModel.categoryId
        price = productDraft.detailInputModel.price.toLong()
        stock = productDraft.detailInputModel.stock
        minOrder = productDraft.detailInputModel.minOrder
        condition = productDraft.detailInputModel.sku
        sku = productDraft.detailInputModel.condition
        imageUrlOrPathList = productDraft.detailInputModel.imageUrlOrPathList
    }

    productInputModel.detailInputModel.preorder.apply {
        duration = productDraft.detailInputModel.preorder.duration
        timeUnit = productDraft.detailInputModel.preorder.timeUnit
        isActive = productDraft.detailInputModel.preorder.isActive
    }

    productDraft.detailInputModel.wholesaleList.map { wholeSale ->
        productInputModel.detailInputModel.wholesaleList.map { wholeSaleDraft ->
            wholeSaleDraft.price = wholeSale.price
            wholeSaleDraft.quantity = wholeSale.price
        }
    }

    productInputModel.descriptionInputModel.apply {
        productDescription = productDraft.descriptionInputModel.productDescription
        val videoLinkList = MutableList(productDraft.descriptionInputModel.videoLinkList.size) { VideoLinkModel() }
        productDraft.descriptionInputModel.videoLinkList.forEach { videoLink ->
            val id = videoLink.inputId
            val image  = videoLink.inputImage
            val url = videoLink.inputUrl
            videoLinkList.add(VideoLinkModel(id,url,image))
        }
        this.videoLinkList = videoLinkList
    }

    productInputModel.shipmentInputModel.apply {
        isMustInsurance = productDraft.shipmentInputModel.isMustInsurance
        weight = productDraft.shipmentInputModel.weight
        weightUnit = productDraft.shipmentInputModel.weightUnit
    }

    productInputModel.draftId = productDraft.draftId

    return productInputModel
}

