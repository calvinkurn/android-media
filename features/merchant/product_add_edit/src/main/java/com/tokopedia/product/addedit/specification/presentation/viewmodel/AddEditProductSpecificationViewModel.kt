package com.tokopedia.product.addedit.specification.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.addedit.specification.domain.usecase.AnnotationCategoryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddEditProductSpecificationViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val annotationCategoryUseCase: AnnotationCategoryUseCase
) : BaseViewModel(coroutineDispatcher) {

}