package com.tokopedia.tokopedianow.annotation.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.annotation.presentation.adapter.AllAnnotationTypeFactory

data class AnnotationUiModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val appLink: String
): Visitable<AllAnnotationTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: AllAnnotationTypeFactory): Int = typeFactory.type(this)
}
