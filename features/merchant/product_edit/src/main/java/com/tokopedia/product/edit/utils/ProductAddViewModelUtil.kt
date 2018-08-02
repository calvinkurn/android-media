package com.tokopedia.product.edit.utils

import android.webkit.URLUtil
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.product.edit.common.model.edit.*
import com.tokopedia.product.edit.constant.ProductConditionTypeDef
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.view.model.ProductAddViewModel

fun convertCategory(productCategory: ProductCategory?): ProductCategoryViewModel? {
    val productCategoryViewModel = ProductCategoryViewModel()
    productCategoryViewModel.categoryId = productCategory?.categoryId?.toLong()?:-1
    productCategoryViewModel.categoryFullName = productCategory?.categoryName
    return productCategoryViewModel
}

private fun convertCatalog(productCatalog: ProductCatalog?): ProductCatalogViewModel? {
    val productCatalogViewModel = ProductCatalogViewModel()
    productCatalogViewModel.catalogId = productCatalog?.catalogId?.toLong()?:-1
    productCatalogViewModel.catalogName = productCatalog?.catalogName
    return productCatalogViewModel
}

fun ProductAddViewModel.convertToProductViewModel(): ProductViewModel? {
    val productViewModel = ProductViewModel()
    productViewModel.productCatalog = convertCatalog(this.productCatalog)
    productViewModel.productCategory = convertCategory(this.productCategory)
    productViewModel.productWeight = this.productLogistic?.weight?.toLong()!!
    productViewModel.productWeightUnit = this.productLogistic?.weightType?.toLong()!!
    val isActive = this.productStock?.isActive ?: false
    productViewModel.setProductStatus(isActive)
    productViewModel.productDescription = this.productDescription?.description
    productViewModel.productName = this.productName?.name
    productViewModel.isProductNameEditable = this.isProductNameEditable
    productViewModel.productSizeChart = this.productSizeChart
    productViewModel.productSku = this.productStock?.sku
    val stockCount = this.productStock?.stockCount ?: 0
    productViewModel.productStock = stockCount.toLong()
    productViewModel.isProductFreeReturn = this.productLogistic?.freeReturn ?: false
    productViewModel.isProductMustInsurance = this.productLogistic?.insurance ?: false
    productViewModel.productPictureViewModelList = this.productPictureList
    productViewModel.productEtalase = convertToEtalaseViewModel(this)
    productViewModel.productCondition = convertToProductCondition(this.productDescription?.isNew?:false)
    productViewModel.productPreorder = convertToProductPreOrder(this.productLogistic?.preOrder
            ?: false,
            this.productLogistic?.processTime, this.productLogistic?.processTimeType)
    productViewModel.productVideo = convertToProductVideo(this.productDescription?.videoIDs)
    /* todo productViewModel.productPrice =
            productViewModel.productPriceCurrency =
            productViewModel.productWholesale = */
    productViewModel.productId = this.productId
    return productViewModel
}

fun convertToProductVideo(videoIDs: ArrayList<String>?): MutableList<ProductVideoViewModel>? {
    val productVideos = ArrayList<ProductVideoViewModel>()
    if (videoIDs != null) {
        for (videoID in videoIDs) {
            productVideos.add(ProductVideoViewModel(videoID))
        }
    }
    return productVideos
}

fun convertToProductPreOrder(preOrder: Boolean, processTime: Int?, processTimeType: Int?): ProductPreOrderViewModel? {
    val productPreOrderViewModel = ProductPreOrderViewModel()
    productPreOrderViewModel.preorderProcessTime = processTime?.toLong() ?: 0
    productPreOrderViewModel.preorderStatus = if (preOrder) 1 else 0
    productPreOrderViewModel.preorderTimeUnit = processTimeType?.toLong() ?: 1
    return productPreOrderViewModel
}

fun convertToProductCondition(isNew: Boolean): Long {
    if (isNew) {
        return ProductConditionTypeDef.TYPE_NEW.toLong()
    } else {
        return ProductConditionTypeDef.TYPE_RECON.toLong()
    }
}

fun convertToEtalaseViewModel(productAddViewModel: ProductAddViewModel): ProductEtalaseViewModel? {
    val productEtalaseViewModel = ProductEtalaseViewModel()
    productEtalaseViewModel.etalaseId = productAddViewModel.etalaseId?.toLong() ?: 0
    productEtalaseViewModel.etalaseName = productAddViewModel.etalaseName
    return productEtalaseViewModel
}

fun ProductAddViewModel.isDataValid(): Boolean {
    return true
}

fun ProductViewModel.convertToProductAddViewModel(): ProductAddViewModel? {
    val productAddViewModel = ProductAddViewModel()
    productAddViewModel.productPictureList = productPictureViewModelList as ArrayList<ProductPictureViewModel>?
    productAddViewModel.productName = ProductName(this.productName)
    productAddViewModel.productCategory = ProductCategory(this.productCategory?.categoryId?.toInt() ?: -1, this.productCategory.categoryFullName ?: "")
    productAddViewModel.productCatalog = ProductCatalog(this.productCatalog?.catalogId?.toInt() ?: -1, this.productCatalog?.catalogName?:"", this.productCatalog?.catalogUrl?:"")
    productAddViewModel.productDescription = ProductDescription(this.productDescription?:"", "", TextApiUtils.isValueTrue(this.productCondition.toString()))
    productAddViewModel.productStock = ProductStock(this.isProductStatusActive, this.productStock.toInt() , this.productSku?:"")
    productAddViewModel.productLogistic = ProductLogistic(this.productWeight.toInt(), this.productWeightUnit.toInt() ,
            this.isProductMustInsurance, this.isProductFreeReturn, TextApiUtils.isValueTrue(this.productPreorder?.preorderStatus?.toString()?:""),
            this.productPreorder?.preorderProcessTime?.toInt()?:-1, this.productPreorder?.preorderTimeUnit?.toInt()?:-1)
    productAddViewModel.hasOriginalVariantLevel1 = checkOriginalVariantLevel1(this)
    productAddViewModel.hasOriginalVariantLevel2 = checkOriginalVariantLevel2(this)
    productAddViewModel.productSizeChart = this.productSizeChart
    productAddViewModel.productVariantViewModel = this.productVariant
    productAddViewModel.etalaseName = this.productEtalase.etalaseName
    productAddViewModel.etalaseId = this.productEtalase.etalaseId.toInt()
    productAddViewModel.isProductNameEditable = this.isProductNameEditable
    return productAddViewModel
}

private fun checkOriginalVariantLevel1(model: ProductViewModel): Boolean {
    if (model.hasVariant()) {
        val productVariantViewModel = model.productVariant
        return productVariantViewModel.getVariantOptionParent(0) != null
    }
    return false
}

private fun checkOriginalVariantLevel2(model: ProductViewModel): Boolean {
    if (model.hasVariant()) {
        val productVariantViewModel = model.productVariant
        return productVariantViewModel.getVariantOptionParent(0) != null
    }
    return false
}

fun ArrayList<ProductPictureViewModel>.convertToListImageString(): ArrayList<String>? {
    val imageList = ArrayList<String>()
    for (productPictureViewModel in this) {
        imageList.add(productPictureViewModel.uriOrPath)
    }
    return imageList
}

fun ArrayList<String>.convertImageListResult(productAddViewModel: ProductAddViewModel,
                                             originalUrlList: ArrayList<String>?,
                                             isEditedList: ArrayList<Boolean>?): ArrayList<ProductPictureViewModel> {
    val productPictureViewModels = ArrayList<ProductPictureViewModel>()
    if (this.isEmpty()) {
        return productPictureViewModels
    } else {

        val prevImageSelectModelList = productAddViewModel.productPictureList

        // LOGIC to retain HTTP url:
        // check with the previous imageSelectView
        // if the new Image Path's original Image is HTTP and exist in the prevImageSelectModel.getUri and editted = false
        // then the data in previous model will be retained, and the uri will be the previous model
        // Otherwise, set the new ImagePath to ImageSelectView
        // example: prev data: "http://a.jpg", "http://b.jpg"
        // example: result data: { "http://a.jpg" edit=false }, "data://b_edit.jpg"
        // example: final data: "http://a.jpg", "data://b_edit.jpg"
        var i = 0
        val sizei = this.size
        while (i < sizei) {
            val imagePath = this.get(i)
            if (originalUrlList != null && prevImageSelectModelList != null && prevImageSelectModelList.size > 0) {
                val editOriginalPath = originalUrlList.get(i)
                val hasAnyEdit = isEditedList?.get(i) ?: false

                if (URLUtil.isNetworkUrl(editOriginalPath) && !hasAnyEdit) {
                    var existInPrevModel = false
                    for (prevImageSelectModel in prevImageSelectModelList) {
                        if (prevImageSelectModel.getUriOrPath().equals(editOriginalPath)) {
                            // HTTP, no edit, exists in prev model, add with prev model
                            productPictureViewModels.add(prevImageSelectModel)
                            existInPrevModel = true
                            break
                        }
                    }
                    if (!existInPrevModel) { // HTTP AND no edit, but not exists in prev model
                        productPictureViewModels.add(convertToPictureViewModel(imagePath))
                    }
                } else { // not HTTP OR has any edit
                    productPictureViewModels.add(convertToPictureViewModel(imagePath))
                }
            } else {
                productPictureViewModels.add(convertToPictureViewModel(imagePath))
            }
            i++
        }
    }
    return productPictureViewModels
}

fun ArrayList<String>.convertImageListResult(): ArrayList<ProductPictureViewModel> {
    val productPictureViewModels = ArrayList<ProductPictureViewModel>()
    for(pictureViewModel in this){
        productPictureViewModels.add(convertToPictureViewModel(pictureViewModel))
    }
    return productPictureViewModels
}

fun convertToPictureViewModel(imagePath: String): ProductPictureViewModel {
    val productPictureViewModel = ProductPictureViewModel()
    productPictureViewModel.filePath = imagePath
    return productPictureViewModel
}
