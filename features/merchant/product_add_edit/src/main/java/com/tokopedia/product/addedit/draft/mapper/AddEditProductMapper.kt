package com.tokopedia.product.addedit.draft.mapper

import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
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

    private fun mapProductInputModelToDraftVariant(productDraft: ProductDraft, productInputModel: ProductInputModel): ProductDraft {
        productDraft.variantInputModel.productSizeChart?.apply {
            id = productInputModel.variantInputModel.productSizeChart?.id ?: 0
            x = productInputModel.variantInputModel.productSizeChart?.x ?: 0L
            y = productInputModel.variantInputModel.productSizeChart?.y ?: 0L
            fileName = productInputModel.variantInputModel.productSizeChart?.fileName ?: ""
            filePath = productInputModel.variantInputModel.productSizeChart?.filePath ?: ""
            fromIg = productInputModel.variantInputModel.productSizeChart?.fromIg ?: 0
            status = productInputModel.variantInputModel.productSizeChart?.status ?: 0
            urlOriginal = productInputModel.variantInputModel.productSizeChart?.urlOriginal ?: ""
            urlThumbnail = productInputModel.variantInputModel.productSizeChart?.urlThumbnail ?: ""
        }

        productDraft.variantInputModel.productVariant.map { productVariantDraft ->
            productInputModel.variantInputModel.productVariant.map { productVariantInputModel ->
                productVariantDraft.apply {
                    level1String = productVariantInputModel.level1String
                    level2String = productVariantInputModel.level2String
                    opt = productVariantInputModel.opt
                    priceVar = productVariantInputModel.priceVar
                    sku = productVariantInputModel.sku
                    st = productVariantInputModel.st
                    stock = productVariantInputModel.stock
                }
            }
        }

        productDraft.variantInputModel.variantOptionParent.map { variantOptionDraft ->
            productInputModel.variantInputModel.variantOptionParent.map { variantOptionInputModel ->
                variantOptionDraft.apply {
                    name = variantOptionInputModel.name
                    position = variantOptionInputModel.position
                    identifier = variantOptionInputModel.identifier
                    productVariantOptionChild?.map { optionChildDraft ->
                        variantOptionInputModel.productVariantOptionChild?.map { optionChildInputModel ->
                            optionChildDraft.apply {
                                hex = optionChildInputModel.hex
                                pvo = optionChildInputModel.pvo
                                tId = optionChildInputModel.tId
                                value = optionChildInputModel.value
                                vuv = optionChildInputModel.vuv
                                productPictureViewModelList?.map { pictureDraft ->
                                    optionChildInputModel.productPictureViewModelList?.map { pictureInputModel ->
                                        pictureDraft.apply {
                                            id = pictureInputModel.id
                                            x = pictureInputModel.x
                                            y = pictureInputModel.y
                                            urlThumbnail = pictureInputModel.urlThumbnail
                                            urlOriginal = pictureInputModel.urlOriginal
                                            status = pictureInputModel.status
                                            fromIg = pictureInputModel.fromIg
                                            filePath = pictureInputModel.filePath
                                            fileName = pictureInputModel.fileName
                                        }
                                    }
                                }
                            }
                        }
                    }
                    unitName = variantOptionInputModel.unitName
                    v = variantOptionInputModel.v
                    vu = variantOptionInputModel.vu
                }
            }
        }
        return productDraft
    }

    private fun mapDraftToProductInputModelVariant(productDraft: ProductDraft, productInputModel: ProductInputModel): ProductInputModel {
        productInputModel.variantInputModel.productSizeChart?.apply {
            id = productDraft.variantInputModel.productSizeChart?.id ?: 0
            x = productDraft.variantInputModel.productSizeChart?.x ?: 0L
            y = productDraft.variantInputModel.productSizeChart?.y ?: 0L
            fileName = productDraft.variantInputModel.productSizeChart?.fileName ?: ""
            filePath = productDraft.variantInputModel.productSizeChart?.filePath ?: ""
            fromIg = productDraft.variantInputModel.productSizeChart?.fromIg ?: 0
            status = productDraft.variantInputModel.productSizeChart?.status ?: 0
            urlOriginal = productDraft.variantInputModel.productSizeChart?.urlOriginal ?: ""
            urlThumbnail = productDraft.variantInputModel.productSizeChart?.urlThumbnail ?: ""
        }

        productDraft.variantInputModel.productVariant.map { productVariantDraft ->
            productInputModel.variantInputModel.productVariant.map { productVariantInputModel ->
                productVariantInputModel.apply {
                    level1String = productVariantDraft.level1String
                    level2String = productVariantDraft.level2String
                    opt = productVariantDraft.opt
                    priceVar = productVariantDraft.priceVar
                    sku = productVariantDraft.sku
                    st = productVariantDraft.st
                    stock = productVariantDraft.stock
                }
            }
        }

        productInputModel.variantInputModel.variantOptionParent.map { variantOptionInputModel ->
            productDraft.variantInputModel.variantOptionParent.map { variantOptionDraft ->
                variantOptionInputModel.apply {
                    name = variantOptionDraft.name
                    position = variantOptionDraft.position
                    identifier = variantOptionDraft.identifier
                    productVariantOptionChild?.map { optionChildInputModel ->
                        variantOptionDraft.productVariantOptionChild?.map { optionChildDraft ->
                            optionChildInputModel.apply {
                                hex = optionChildDraft.hex
                                pvo = optionChildDraft.pvo
                                tId = optionChildDraft.tId
                                value = optionChildDraft.value
                                vuv = optionChildDraft.vuv
                                productPictureViewModelList?.map { pictureInputModel ->
                                    optionChildInputModel.productPictureViewModelList?.map { pictureDraft ->
                                        pictureInputModel.apply {
                                            id = pictureDraft.id
                                            x = pictureDraft.x
                                            y = pictureDraft.y
                                            urlThumbnail = pictureDraft.urlThumbnail
                                            urlOriginal = pictureDraft.urlOriginal
                                            status = pictureDraft.status
                                            fromIg = pictureDraft.fromIg
                                            filePath = pictureDraft.filePath
                                            fileName = pictureDraft.fileName
                                        }
                                    }
                                }
                            }
                        }
                    }
                    unitName = variantOptionDraft.unitName
                    v = variantOptionDraft.v
                    vu = variantOptionDraft.vu
                }
            }
        }
        return productInputModel
    }

    fun mapProductInputModelDetailToDraft(productInputModel: ProductInputModel): ProductDraft {
        var productDraft = ProductDraft()
        productDraft = mapProductInputModelToDraftVariant(productDraft, productInputModel)
        productDraft.productId = productInputModel.productId
        productDraft.detailInputModel.productName = productInputModel.detailInputModel.productName
        productDraft.detailInputModel.categoryId = productInputModel.detailInputModel.categoryId
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
        var productInputModel = ProductInputModel().apply {
            productId = productDraft.productId
        }
        productInputModel = mapDraftToProductInputModelVariant(productDraft, productInputModel)
        productInputModel.detailInputModel.apply {
            productName = productDraft.detailInputModel.productName
            categoryId = productDraft.detailInputModel.categoryId
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
}


