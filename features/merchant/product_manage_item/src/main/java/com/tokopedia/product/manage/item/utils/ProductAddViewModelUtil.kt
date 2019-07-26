package com.tokopedia.product.manage.item.utils

import android.text.TextUtils
import android.webkit.URLUtil
import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef
import com.tokopedia.product.manage.item.description.view.model.ProductDescription
import com.tokopedia.product.manage.item.logistic.view.model.ProductLogistic
import com.tokopedia.product.manage.item.main.base.data.model.*
import com.tokopedia.product.manage.item.main.base.view.listener.ListenerOnErrorAddProduct
import com.tokopedia.product.manage.item.main.base.view.model.ProductAddViewModel
import com.tokopedia.product.manage.item.name.view.model.ProductName
import com.tokopedia.product.manage.item.price.model.ProductPrice
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import com.tokopedia.product.manage.item.utils.constant.ProductConditionTypeDef

fun convertCategory(productCategory: ProductCategory?): ProductCategoryViewModel? {
    val productCategoryViewModel = ProductCategoryViewModel()
    productCategoryViewModel.categoryId = productCategory?.categoryId?.toLong() ?: -1
    productCategoryViewModel.categoryFullName = productCategory?.categoryName
    return productCategoryViewModel
}

private fun convertCatalog(productCatalog: ProductCatalog?): ProductCatalogViewModel? {
    if (productCatalog != null && productCatalog.catalogId > 1) {
        val productCatalogViewModel = ProductCatalogViewModel()
        productCatalogViewModel.catalogId = productCatalog.catalogId.toLong()
        productCatalogViewModel.catalogName = productCatalog.catalogName
        return productCatalogViewModel
    }
    return null
}

fun ProductAddViewModel.convertToProductViewModel(): ProductViewModel {
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
    productViewModel.productCondition = convertToProductCondition(this.productDescription?.isNew
            ?: false)
    productViewModel.productPreorder = convertToProductPreOrder(this.productLogistic?.preOrder
            ?: false,
            this.productLogistic?.processTime, this.productLogistic?.processTimeType)
    productViewModel.productVideo = convertToProductVideo(this.productDescription?.videoIDs)
    productViewModel.productPrice = this.productPrice?.price ?: 0.0
    productViewModel.productPriceCurrency = this.productPrice?.currencyType?.toLong() ?: CurrencyTypeDef.TYPE_IDR.toLong()
    productViewModel.productWholesale = this.productPrice?.wholesalePrice
    productViewModel.productId = if (TextUtils.isEmpty(this.productId)) null else this.productId
    productViewModel.productMinOrder = this.productPrice?.minOrder?.toLong() ?: 1
    productViewModel.productMaxOrder = this.productPrice?.maxOrder?.toLong() ?: 0
    productViewModel.productVariant = this.productVariantViewModel
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

fun convertToProductPreOrder(preOrder: Boolean, processTime: Int?, processTimeType: Int?) =
    ProductPreOrderViewModel().apply {
        preorderProcessTime = processTime?.toLong() ?: 0
        preorderStatus = if (preOrder) 1 else 0
        preorderTimeUnit = processTimeType?.toLong() ?: 1
    }

fun convertToProductCondition(isNew: Boolean) = if (isNew) ProductConditionTypeDef.TYPE_NEW.toLong() else ProductConditionTypeDef.TYPE_RECON.toLong()

fun convertToEtalaseViewModel(productAddViewModel: ProductAddViewModel) = ProductEtalaseViewModel()
        .apply {
            etalaseId = productAddViewModel.etalaseId?.toLong() ?: 0
            etalaseName = productAddViewModel.etalaseName
        }

fun ProductAddViewModel.isDataValid(listenerOnError: ListenerOnErrorAddProduct): Boolean {
    if (TextUtils.isEmpty(this.productName?.name)) {
        listenerOnError.onErrorName()
        return false
    }
    if (this.productCategory?.categoryId?:0 <= 0) {
        listenerOnError.onErrorCategoryEmpty()
        return false
    }
    if (this.productPrice?.price?:0.0 <= 0) {
        listenerOnError.onErrorPrice()
        return false
    }
    if (this.productLogistic?.weight?:0 <= 0) {
        listenerOnError.onErrorWeight()
        return false
    }
    if ( this.productCatalog?.catalogId?:0 <= 0 &&
            !this.productStock?.isActive!! &&
            this.productPictureList?.size?:0 <= 0) {
        listenerOnError.onErrorImage()
        return false
    }
    return true
}

fun ProductViewModel.convertToProductAddViewModel(isEditStatus : Boolean): ProductAddViewModel? {
    val productAddViewModel = ProductAddViewModel()
    productAddViewModel.productPictureList = productPictureViewModelList as ArrayList<ProductPictureViewModel>?
    productAddViewModel.productName = ProductName(this.productName)
    productAddViewModel.productCategory = ProductCategory(this.productCategory?.categoryId?.toInt()
            ?: -1, this.productCategory?.categoryFullName ?: "")
    productAddViewModel.productCatalog = ProductCatalog(this.productCatalog?.catalogId?.toInt()
            ?: -1, this.productCatalog?.catalogName ?: "", this.productCatalog?.catalogUrl ?: "")
    productAddViewModel.productDescription = ProductDescription(this.productDescription
            ?: "", "", TextApiUtils.isValueTrue(this.productCondition.toString()), ArrayList(this.productVideo.map { it.url }))
    productAddViewModel.productStock = ProductStock(this.isProductStatusActive, this.productStock.toInt(), this.productSku
            ?: "")
    productAddViewModel.productLogistic = ProductLogistic(this.productWeight.toInt(), this.productWeightUnit.toInt(),
            this.isProductMustInsurance, this.isProductFreeReturn, TextApiUtils.isValueTrue(this.productPreorder?.preorderStatus?.toString()
            ?: ""),
            this.productPreorder?.preorderProcessTime?.toInt()
                    ?: ProductEditPreOrderTimeType.DAY, this.productPreorder?.preorderTimeUnit?.toInt() ?: ProductEditPreOrderTimeType.DAY)
    if(isEditStatus) {
        productAddViewModel.hasOriginalVariantLevel1 = checkOriginalVariantLevel1(this)
        productAddViewModel.hasOriginalVariantLevel2 = checkOriginalVariantLevel2(this)
    }
    productAddViewModel.productSizeChart = this.productSizeChart
    productAddViewModel.productVariantViewModel = this.productVariant
    productAddViewModel.etalaseName = this.productEtalase?.etalaseName
    productAddViewModel.etalaseId = this.productEtalase?.etalaseId?.toInt()
    productAddViewModel.isProductNameEditable = this.isProductNameEditable
    productAddViewModel.productId = this.productId ?: ""
    val productWholesale = this.productWholesale ?: ArrayList<ProductWholesaleViewModel>()
    productAddViewModel.productPrice = ProductPrice(this.productPrice, this.productPriceCurrency.toInt(),
            productWholesale as java.util.ArrayList<ProductWholesaleViewModel>, this.productMinOrder.toInt(), this.productMaxOrder.toInt())
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
    for (pictureViewModel in this) {
        productPictureViewModels.add(convertToPictureViewModel(pictureViewModel))
    }
    return productPictureViewModels
}

fun convertToPictureViewModel(imagePath: String): ProductPictureViewModel {
    val productPictureViewModel = ProductPictureViewModel()
    productPictureViewModel.filePath = imagePath
    return productPictureViewModel
}

fun ProductAddViewModel.isFilledAny(): Boolean {
    return !TextUtils.isEmpty(this.productName?.name) ||
            this.productPictureList != null && this.productPictureList!!.size > 0 ||
            this.productCategory?.categoryId?:0 > 0 ||
            this.etalaseId?:0 > 0 ||
            this.productPrice?.price ?: 0.0 > 0 ||
            !TextUtils.isEmpty(this.productDescription?.description) ||
            this.productDescription?.videoIDs?.size?:0 > 0 ||
            this.productLogistic?.weight?:0 > 0 ||
            !TextUtils.isEmpty(this.productStock?.sku)
}

fun ProductAddViewModel?.resetCatalog() {
    this?.productCatalog = ProductCatalog(-1, "")
}
