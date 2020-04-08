package com.tokopedia.gallery.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.gallery.viewmodel.ImageReviewItem

interface TypeFactory : AdapterTypeFactory {
    fun type(viewModel: ImageReviewItem): Int
}
