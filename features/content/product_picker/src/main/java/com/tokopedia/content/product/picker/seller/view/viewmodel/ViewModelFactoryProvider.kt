package com.tokopedia.content.product.picker.seller.view.viewmodel

import androidx.lifecycle.ViewModelProvider

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
interface ViewModelFactoryProvider {

    fun getFactory(): ViewModelProvider.Factory
}
