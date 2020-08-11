package com.tokopedia.product.addedit.draft.mapper

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.addedit.description.presentation.model.*
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.draft.data.model.description.VideoLinkListModel

object AddEditProductMapper {

    private fun mapProductInputModelPictureListToDraftPictureList(pictureList: List<PictureInputModel>) = pictureList.map {
        com.tokopedia.product.manage.common.draft.data.model.detail.PictureInputModel(
                picID = it.picID,
                description = it.description,
                filePath = it.filePath,
                fileName = it.fileName,
                width = it.width,
                height = it.height,
                isFromIG = it.isFromIG,
                urlOriginal = it.urlOriginal,
                urlThumbnail = it.urlThumbnail,
                url300 = it.url300,
                status = it.status
        )
    }

    private fun mapDraftPictureListToProductInputModelPictureList(pictureList: List<com.tokopedia.product.manage.common.draft.data.model.detail.PictureInputModel>) =
            pictureList.map {
                PictureInputModel(
                        picID = it.picID,
                        description = it.description,
                        filePath = it.filePath,
                        fileName = it.fileName,
                        width = it.width,
                        height = it.height,
                        isFromIG = it.isFromIG,
                        urlOriginal = it.urlOriginal,
                        urlThumbnail = it.urlThumbnail,
                        url300 = it.url300,
                        status = it.status
                )
            }

    fun mapProductInputModelDetailToDraft(productInputModel: ProductInputModel): ProductDraft {
        val productDraft = ProductDraft()
        productDraft.variantInputModel = mapProductInputModelToJsonString(productInputModel.variantInputModel)
        productDraft.productId = productInputModel.productId
        productDraft.detailInputModel.productName = productInputModel.detailInputModel.productName
        productDraft.detailInputModel.categoryId = productInputModel.detailInputModel.categoryId
        productDraft.detailInputModel.categoryName = productInputModel.detailInputModel.categoryName
        productDraft.detailInputModel.price = productInputModel.detailInputModel.price
        productDraft.detailInputModel.stock = productInputModel.detailInputModel.stock
        productDraft.detailInputModel.minOrder = productInputModel.detailInputModel.minOrder
        productDraft.detailInputModel.condition = productInputModel.detailInputModel.sku
        productDraft.detailInputModel.sku = productInputModel.detailInputModel.condition
        productDraft.detailInputModel.imageUrlOrPathList = productInputModel.detailInputModel.imageUrlOrPathList
        productDraft.detailInputModel.pictureList = mapProductInputModelPictureListToDraftPictureList(productInputModel.detailInputModel.pictureList)
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
            val videoLinkList = mutableListOf<VideoLinkListModel>()
            productInputModel.descriptionInputModel.videoLinkList.forEach { videoLink ->
                val title = videoLink.inputTitle
                val description = videoLink.inputDescription
                val image  = videoLink.inputImage
                val url = videoLink.inputUrl
                videoLinkList.add(VideoLinkListModel(url, title, description, image))
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
        if(productDraft.variantInputModel.isNotEmpty()) {
            productInputModel.variantInputModel = mapJsonToProductInputModel(productDraft.variantInputModel)
        } else {
            productInputModel.variantInputModel = VariantInputModel()
        }
        productInputModel.productId = productDraft.productId
        productInputModel.detailInputModel.apply {
            productName = productDraft.detailInputModel.productName
            categoryId = productDraft.detailInputModel.categoryId
            categoryName = productDraft.detailInputModel.categoryName
            price = productDraft.detailInputModel.price
            stock = productDraft.detailInputModel.stock
            minOrder = productDraft.detailInputModel.minOrder
            condition = productDraft.detailInputModel.sku
            sku = productDraft.detailInputModel.condition
            imageUrlOrPathList = productDraft.detailInputModel.imageUrlOrPathList
            pictureList = mapDraftPictureListToProductInputModelPictureList(productDraft.detailInputModel.pictureList)
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
                val title = videoLink.inputTitle
                val description = videoLink.inputDescription
                val image  = videoLink.inputImage
                val url = videoLink.inputUrl
                videoLinkList.add(VideoLinkModel(url, title, description, image))
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

    private fun mapProductInputModelToJsonString(productVariantInputModel: VariantInputModel): String {
        return CacheUtil.convertModelToString(productVariantInputModel, object : TypeToken<VariantInputModel>() {}.type)
    }

    private fun mapJsonToProductInputModel(jsonData : String): VariantInputModel {
        return CacheUtil.convertStringToModel(jsonData, VariantInputModel::class.java)
    }
}


