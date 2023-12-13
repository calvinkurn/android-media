package com.tokopedia.tokopedianow.annotation.presentation.adapter

import com.tokopedia.tokopedianow.annotation.presentation.uimodel.AnnotationUiModel

interface AllAnnotationTypeFactory {
    fun type(uiModel: AnnotationUiModel): Int
}
