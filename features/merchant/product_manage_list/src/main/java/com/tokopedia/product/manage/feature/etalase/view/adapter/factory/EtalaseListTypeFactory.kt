package com.tokopedia.product.manage.feature.etalase.view.adapter.factory

import com.tokopedia.product.manage.feature.etalase.data.model.EtalaseViewModel

interface EtalaseListTypeFactory {

    fun type(viewModel: EtalaseViewModel): Int
}