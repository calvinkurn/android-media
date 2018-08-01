package com.tokopedia.product.edit.utils

import com.tokopedia.abstraction.common.utils.network.TextApiUtils
import com.tokopedia.product.edit.common.model.edit.ProductCatalogViewModel
import com.tokopedia.product.edit.common.model.edit.ProductCategoryViewModel
import com.tokopedia.product.edit.common.model.edit.ProductPictureViewModel
import com.tokopedia.product.edit.common.model.edit.ProductViewModel
import com.tokopedia.product.edit.price.model.*
import com.tokopedia.product.edit.view.model.ProductAddViewModel
import java.util.ArrayList


fun convertCategory(productCategory: ProductCategory): ProductCategoryViewModel? {
    val productCategoryViewModel = ProductCategoryViewModel()
    productCategoryViewModel.categoryId = productCategory.categoryId.toLong()
    productCategoryViewModel.categoryFullName = productCategory.categoryName
    return productCategoryViewModel
}

private fun convertCatalog(productCatalog: ProductCatalog): ProductCatalogViewModel? {
    val productCatalogViewModel = ProductCatalogViewModel()
    productCatalogViewModel.catalogId = productCatalog.catalogId.toLong()
    productCatalogViewModel.catalogName = productCatalog.catalogName
    return productCatalogViewModel
}

fun ProductAddViewModel.convertToProductViewModel(): ProductViewModel? {
    val productViewModel = ProductViewModel()
    productViewModel.productCatalog = convertCatalog(this.productCatalog)
    productViewModel.productCategory = convertCategory(this.productCategory)
    return productViewModel
}

fun ProductAddViewModel.isDataValid() : Boolean{
    return true
}

fun ProductViewModel.convertToProductAddViewModel() : ProductAddViewModel?{
    val productAddViewModel = ProductAddViewModel()
    productAddViewModel.productPictureList = convertToListImage(this.productPictureViewModelList)
    productAddViewModel.productName = ProductName(this.productName)
    productAddViewModel.productCategory = ProductCategory(this.productCategory.categoryId.toInt(), this.productCategory.categoryFullName)
    productAddViewModel.productCatalog = ProductCatalog(this.productCatalog.catalogId.toInt(), this.productCatalog.catalogName, this.productCatalog.catalogUrl)
    productAddViewModel.productDescription = ProductDescription(this.productDescription, "", TextApiUtils.isValueTrue(this.productCondition.toString()))
    productAddViewModel.productStock = ProductStock(this.isProductStatusActive, this.productStock.toInt(), this.productSku)
    productAddViewModel.productLogistic = ProductLogistic(this.productWeight.toInt(), this.productWeightUnit.toInt(),
            this.isProductMustInsurance, this.isProductFreeReturn, TextApiUtils.isValueTrue(this.productPreorder.toString()),
            this.productPreorder.preorderProcessTime.toInt(), this.productPreorder.preorderTimeUnit.toInt())
    productAddViewModel.hasOriginalVariantLevel1 = checkOriginalVariantLevel1(this)
    productAddViewModel.hasOriginalVariantLevel2 = checkOriginalVariantLevel2(this)
    productAddViewModel.productSizeChart = this.productSizeChart
    productAddViewModel.productVariantViewModel = this.productVariant
    productAddViewModel.etalaseName = this.productEtalase.etalaseName
    productAddViewModel.etalaseId = this.productEtalase.etalaseId.toInt()
    return productAddViewModel
}

private fun checkOriginalVariantLevel1(model: ProductViewModel) : Boolean {
    if (model.hasVariant()) {
        val productVariantViewModel = model.productVariant
        return productVariantViewModel.getVariantOptionParent(0) != null
    }
    return false
}

private fun checkOriginalVariantLevel2(model: ProductViewModel) : Boolean {
    if (model.hasVariant()) {
        val productVariantViewModel = model.productVariant
        return productVariantViewModel.getVariantOptionParent(0) != null
    }
    return false
}

fun convertToListImage(productPictureViewModelList: List<ProductPictureViewModel>): ArrayList<String>? {
    val imageList = ArrayList<String>()
    for(productPictureViewModel in productPictureViewModelList){
        imageList.add(productPictureViewModel.urlOriginal)
    }
    return imageList
}
