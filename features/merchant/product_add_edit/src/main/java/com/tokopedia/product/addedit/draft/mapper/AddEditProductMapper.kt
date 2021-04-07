package com.tokopedia.product.addedit.draft.mapper

import com.tokopedia.product.addedit.common.util.JsonUtil.mapJsonToObject
import com.tokopedia.product.addedit.common.util.JsonUtil.mapObjectToJson
import com.tokopedia.product.addedit.description.presentation.model.VideoLinkModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.draft.presentation.model.ProductDraftUiModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.manage.common.draft.data.model.detail.ShowCaseInputModel
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.product.manage.common.feature.draft.data.model.description.VideoLinkListModel
import com.tokopedia.product.manage.common.feature.draft.mapper.AddEditProductDraftMapper
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.product.manage.common.feature.draft.data.model.detail.SpecificationInputModel as DraftSpecificationInputModel
import com.tokopedia.product.manage.common.feature.draft.data.model.detail.WholeSaleInputModel as DraftWholeSaleInputModel

object AddEditProductMapper {

    private fun mapProductInputModelPictureListToDraftPictureList(pictureList: List<PictureInputModel>) = pictureList.map {
        com.tokopedia.product.manage.common.feature.draft.data.model.detail.PictureInputModel(
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

    private fun mapDraftPictureListToProductInputModelPictureList(pictureList: List<com.tokopedia.product.manage.common.feature.draft.data.model.detail.PictureInputModel>) =
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
        productDraft.variantInputModel = mapObjectToJson(productInputModel.variantInputModel) ?: ""
        productDraft.productId = productInputModel.productId
        productDraft.detailInputModel.productName = productInputModel.detailInputModel.productName
        productDraft.detailInputModel.currentProductName = productInputModel.detailInputModel.currentProductName
        productDraft.detailInputModel.categoryId = productInputModel.detailInputModel.categoryId
        productDraft.detailInputModel.categoryName = productInputModel.detailInputModel.categoryName
        productDraft.detailInputModel.price = productInputModel.detailInputModel.price
        productDraft.detailInputModel.stock = productInputModel.detailInputModel.stock
        productDraft.detailInputModel.minOrder = productInputModel.detailInputModel.minOrder
        productDraft.detailInputModel.condition = productInputModel.detailInputModel.condition
        productDraft.detailInputModel.sku = productInputModel.detailInputModel.sku
        productDraft.detailInputModel.imageUrlOrPathList = productInputModel.detailInputModel.imageUrlOrPathList
        productDraft.detailInputModel.pictureList = mapProductInputModelPictureListToDraftPictureList(productInputModel.detailInputModel.pictureList)
        productDraft.detailInputModel.preorder.apply {
            duration = productInputModel.detailInputModel.preorder.duration
            timeUnit = productInputModel.detailInputModel.preorder.timeUnit
            isActive = productInputModel.detailInputModel.preorder.isActive
        }
        productDraft.detailInputModel.wholesaleList = productInputModel.detailInputModel.wholesaleList.map { wholeSale ->
            DraftWholeSaleInputModel(wholeSale.price, wholeSale.quantity)
        }
        productDraft.detailInputModel.productShowCases = productInputModel.detailInputModel.productShowCases.map {showCaseItem ->
            ShowCaseInputModel(showcaseId = showCaseItem.showcaseId, showcaseName = showCaseItem.showcaseName)
        }
        productDraft.detailInputModel.specification = productInputModel.detailInputModel.specifications?.map {
            DraftSpecificationInputModel(it.id, it.data)
        }
        productDraft.detailInputModel.status = productInputModel.detailInputModel.status
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
            productInputModel.variantInputModel = mapJsonToObject(productDraft.variantInputModel, VariantInputModel::class.java)
        } else {
            productInputModel.variantInputModel = VariantInputModel()
        }
        productInputModel.productId = productDraft.productId
        productInputModel.detailInputModel.apply {
            productName = productDraft.detailInputModel.productName
            currentProductName = productDraft.detailInputModel.currentProductName
            categoryId = productDraft.detailInputModel.categoryId
            categoryName = productDraft.detailInputModel.categoryName
            price = productDraft.detailInputModel.price
            stock = productDraft.detailInputModel.stock
            minOrder = productDraft.detailInputModel.minOrder
            condition = productDraft.detailInputModel.condition
            sku = productDraft.detailInputModel.sku
            imageUrlOrPathList = productDraft.detailInputModel.imageUrlOrPathList
            pictureList = mapDraftPictureListToProductInputModelPictureList(productDraft.detailInputModel.pictureList)
        }
        productInputModel.detailInputModel.preorder.apply {
            duration = productDraft.detailInputModel.preorder.duration
            timeUnit = productDraft.detailInputModel.preorder.timeUnit
            isActive = productDraft.detailInputModel.preorder.isActive
        }
        productInputModel.detailInputModel.wholesaleList = productDraft.detailInputModel.wholesaleList.map { wholeSale ->
            WholeSaleInputModel(wholeSale.price, wholeSale.quantity)
        }
        productInputModel.detailInputModel.productShowCases = productDraft.detailInputModel.productShowCases.map { showCase ->
            ShowcaseItemPicker(showcaseId = showCase.showcaseId, showcaseName = showCase.showcaseName)
        }
        productInputModel.detailInputModel.specifications = productDraft.detailInputModel.specification?.map { specification ->
            SpecificationInputModel(specification.id, specification.data)
        }
        productInputModel.detailInputModel.status = productDraft.detailInputModel.status
        productInputModel.descriptionInputModel.apply {
            productDescription = productDraft.descriptionInputModel.productDescription
            val videoLinkList = mutableListOf<VideoLinkModel>()
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

    fun mapProductDraftToProductDraftUiModel(draft: ProductDraft): ProductDraftUiModel {
        return ProductDraftUiModel(
                draft.draftId,
                draft.detailInputModel.imageUrlOrPathList.firstOrNull() ?: "",
                draft.detailInputModel.productName,
                AddEditProductDraftMapper.getCompletionPercent(draft),
        )
    }
}


