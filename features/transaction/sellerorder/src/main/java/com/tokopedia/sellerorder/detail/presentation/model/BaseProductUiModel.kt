package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactory
import com.tokopedia.sellerorder.detail.presentation.adapter.factory.SomDetailAdapterFactoryImpl

/**
 * Created By @ilhamsuaib on 05/07/21
 */

interface BaseProductUiModel : Visitable<SomDetailAdapterFactoryImpl>
