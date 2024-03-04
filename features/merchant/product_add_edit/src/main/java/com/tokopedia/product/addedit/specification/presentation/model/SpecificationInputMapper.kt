package com.tokopedia.product.addedit.specification.presentation.model

import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.domain.model.Values

object SpecificationInputMapper {
    fun getItemSelected(annotationCategoryList: List<AnnotationCategoryData>, productInputModel: ProductInputModel?) =
        annotationCategoryList.map { annotationCategoryData ->
            var valueResult: Values? = null
            val required = annotationCategoryData.isMandatory

            if (productInputModel != null) {
                val specificationList = productInputModel.detailInputModel.specifications.orEmpty()
                valueResult = annotationCategoryData.data.firstOrNull { value ->
                    specificationList.any { it.id == value.id }
                }
            }

            return@map if (valueResult != null) {
                SpecificationInputModel(
                    id = valueResult.id,
                    data = valueResult.name,
                    specificationVariant = annotationCategoryData.variant,
                    required = required)
            } else if (annotationCategoryData.isCustomAnnotType) {
                val specificationList = productInputModel?.detailInputModel?.specifications.orEmpty()
                val selectedSpecification = specificationList.firstOrNull {
                    it.specificationVariant == annotationCategoryData.variant
                } ?: SpecificationInputModel(
                    required = required,
                    specificationVariant = annotationCategoryData.variant,
                )
                selectedSpecification.apply {
                    isTextInput = true
                    variantId = annotationCategoryData.variantId.toString()
                }
            } else {
                SpecificationInputModel(
                    required = required,
                    specificationVariant = annotationCategoryData.variant,
                )
            }
        }

    fun hasInputtedSpecification(specificationList: List<SpecificationInputModel>) = specificationList.any {
        it.id.isNotBlank() || it.data.isNotEmpty()
    }
}
