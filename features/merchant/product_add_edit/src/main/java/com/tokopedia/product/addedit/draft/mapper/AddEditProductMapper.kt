package com.tokopedia.product.addedit.draft.mapper

import com.tokopedia.product.addedit.description.presentation.model.*
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.draft.data.model.description.VideoLinkListModel
import java.util.ArrayList

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

    private fun mapDraftToProductInputModelVariant(productDraft: ProductDraft, productInputModel: ProductInputModel) {

        productDraft.variantInputModel.productSizeChart?.apply {
            productInputModel.variantInputModel.productSizeChart?.id = id
            productInputModel.variantInputModel.productSizeChart?.x = x
            productInputModel.variantInputModel.productSizeChart?.y = y
            productInputModel.variantInputModel.productSizeChart?.fileName = fileName
            productInputModel.variantInputModel.productSizeChart?.filePath = filePath
            productInputModel.variantInputModel.productSizeChart?.fromIg = fromIg
            productInputModel.variantInputModel.productSizeChart?.status = status
            productInputModel.variantInputModel.productSizeChart?.urlOriginal = urlOriginal
            productInputModel.variantInputModel.productSizeChart?.urlThumbnail = urlThumbnail
        }

        val listProductVariantCombinationViewModelInputModel = ArrayList<ProductVariantCombinationViewModel>()
        val productVariantCombinationViewModelInputModel = ProductVariantCombinationViewModel()
        productDraft.variantInputModel.productVariant.forEach { productVariantDraft ->
            productVariantCombinationViewModelInputModel.apply {
                level1String = productVariantDraft.level1String
                level2String = productVariantDraft.level2String
                opt = productVariantDraft.opt
                priceVar = productVariantDraft.priceVar
                sku = productVariantDraft.sku
                st = productVariantDraft.st
                stock = productVariantDraft.stock
            }
            listProductVariantCombinationViewModelInputModel.add(productVariantCombinationViewModelInputModel)
        }
        productInputModel.variantInputModel.productVariant = listProductVariantCombinationViewModelInputModel

        val listProductVariantOptionParentInputModel = ArrayList<ProductVariantOptionParent>()
        val productVariantOptionParentInputModel = ProductVariantOptionParent()
        val listProductVariantOptionChildInputModel = ArrayList<ProductVariantOptionChild>()
        val productVariantOptionChildInputModel = ProductVariantOptionChild()
        val listPictureViewModelInputModel = ArrayList<PictureViewModel>()
        val pictureViewModelInputModel = PictureViewModel()

        productDraft.variantInputModel.variantOptionParent.forEach { variantOptionDraft ->
            productVariantOptionParentInputModel.name = variantOptionDraft.name
            productVariantOptionParentInputModel.position = variantOptionDraft.position
            productVariantOptionParentInputModel.identifier = variantOptionDraft.identifier
            variantOptionDraft.productVariantOptionChild?.forEach { optionChildDraft ->
                optionChildDraft.apply {
                    productVariantOptionChildInputModel.hex = hex
                    productVariantOptionChildInputModel.pvo = pvo
                    productVariantOptionChildInputModel.tId = tId
                    productVariantOptionChildInputModel.value = value
                    productVariantOptionChildInputModel.vuv = vuv
                    optionChildDraft.productPictureViewModelList?.forEach { pictureDraft ->
                        pictureDraft.apply {
                            pictureViewModelInputModel.id = id
                            pictureViewModelInputModel.x = x
                            pictureViewModelInputModel.y = y
                            pictureViewModelInputModel.urlThumbnail = urlThumbnail
                            pictureViewModelInputModel.urlOriginal = urlOriginal
                            pictureViewModelInputModel.status = status
                            pictureViewModelInputModel.fromIg = fromIg
                            pictureViewModelInputModel.filePath = filePath
                            pictureViewModelInputModel.fileName = fileName
                        }
                        listPictureViewModelInputModel.add(pictureViewModelInputModel)
                    }
                    productVariantOptionChildInputModel.productPictureViewModelList = listPictureViewModelInputModel
                }
                listProductVariantOptionChildInputModel.add(productVariantOptionChildInputModel)
            }
            productVariantOptionParentInputModel.productVariantOptionChild = listProductVariantOptionChildInputModel
            productVariantOptionParentInputModel.unitName = variantOptionDraft.unitName
            productVariantOptionParentInputModel.v = variantOptionDraft.v
            productVariantOptionParentInputModel.vu = variantOptionDraft.vu
            listProductVariantOptionParentInputModel.add(productVariantOptionParentInputModel)
        }
        productInputModel.variantInputModel.variantOptionParent = listProductVariantOptionParentInputModel
    }

    private fun mapProductInputModelToDraftVariant(productDraft: ProductDraft, productInputModel: ProductInputModel) {
        productInputModel.variantInputModel.productSizeChart?.apply {
            productDraft.variantInputModel.productSizeChart?.id = id
            productDraft.variantInputModel.productSizeChart?.x = x
            productDraft.variantInputModel.productSizeChart?.y = y
            productDraft.variantInputModel.productSizeChart?.fileName = fileName
            productDraft.variantInputModel.productSizeChart?.filePath = filePath
            productDraft.variantInputModel.productSizeChart?.fromIg = fromIg
            productDraft.variantInputModel.productSizeChart?.status = status
            productDraft.variantInputModel.productSizeChart?.urlOriginal = urlOriginal
            productDraft.variantInputModel.productSizeChart?.urlThumbnail = urlThumbnail
        }

        val listProductVariantCombinationViewModelDraft = ArrayList<com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantCombinationViewModel>()
        val productVariantCombinationViewModelDraft = com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantCombinationViewModel()
        productInputModel.variantInputModel.productVariant.forEach { productVariantInputModel ->
            productVariantCombinationViewModelDraft.apply {
                level1String = productVariantInputModel.level1String
                level2String = productVariantInputModel.level2String
                opt = productVariantInputModel.opt
                priceVar = productVariantInputModel.priceVar
                sku = productVariantInputModel.sku
                st = productVariantInputModel.st
                stock = productVariantInputModel.stock
            }
            listProductVariantCombinationViewModelDraft.add(productVariantCombinationViewModelDraft)
        }
        productDraft.variantInputModel.productVariant = listProductVariantCombinationViewModelDraft

        val listProductVariantOptionParentDraft = ArrayList<com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantOptionParent>()
        val productVariantOptionParentDraft = com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantOptionParent()
        val listProductVariantOptionChildDraft = ArrayList<com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantOptionChild>()
        val productVariantOptionChildDraft = com.tokopedia.product.manage.common.draft.data.model.description.ProductVariantOptionChild()
        val listPictureViewModelDraft = ArrayList<com.tokopedia.product.manage.common.draft.data.model.description.PictureViewModel>()
        val pictureViewModelDraft = com.tokopedia.product.manage.common.draft.data.model.description.PictureViewModel()

        productDraft.variantInputModel.variantOptionParent.forEach { variantOptionDraft ->
            productVariantOptionParentDraft.name = variantOptionDraft.name
            productVariantOptionParentDraft.position = variantOptionDraft.position
            productVariantOptionParentDraft.identifier = variantOptionDraft.identifier
            variantOptionDraft.productVariantOptionChild?.forEach { optionChildDraft ->
                optionChildDraft.apply {
                    productVariantOptionChildDraft.hex = hex
                    productVariantOptionChildDraft.pvo = pvo
                    productVariantOptionChildDraft.tId = tId
                    productVariantOptionChildDraft.value = value
                    productVariantOptionChildDraft.vuv = vuv
                    optionChildDraft.productPictureViewModelList?.forEach { pictureDraft ->
                        pictureDraft.apply {
                            pictureViewModelDraft.id = id
                            pictureViewModelDraft.x = x
                            pictureViewModelDraft.y = y
                            pictureViewModelDraft.urlThumbnail = urlThumbnail
                            pictureViewModelDraft.urlOriginal = urlOriginal
                            pictureViewModelDraft.status = status
                            pictureViewModelDraft.fromIg = fromIg
                            pictureViewModelDraft.filePath = filePath
                            pictureViewModelDraft.fileName = fileName
                        }
                        listPictureViewModelDraft.add(pictureViewModelDraft)
                    }
                    productVariantOptionChildDraft.productPictureViewModelList = listPictureViewModelDraft
                }
                listProductVariantOptionChildDraft.add(productVariantOptionChildDraft)
            }
            productVariantOptionParentDraft.productVariantOptionChild = listProductVariantOptionChildDraft
            productVariantOptionParentDraft.unitName = variantOptionDraft.unitName
            productVariantOptionParentDraft.v = variantOptionDraft.v
            productVariantOptionParentDraft.vu = variantOptionDraft.vu
            listProductVariantOptionParentDraft.add(productVariantOptionParentDraft)
        }
        productDraft.variantInputModel.variantOptionParent = listProductVariantOptionParentDraft
    }


    fun mapProductInputModelDetailToDraft(productInputModel: ProductInputModel): ProductDraft {
        val productDraft = ProductDraft()
        mapProductInputModelToDraftVariant(productDraft, productInputModel)
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
        val productInputModel = ProductInputModel()
        mapDraftToProductInputModelVariant(productDraft, productInputModel)
        productInputModel.productId = productDraft.productId
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


