package com.tokopedia.search.result.presentation.view.listener


import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel

interface InspirationCarouselViewHintListener {

    fun createViewHintListener(product: InspirationCarouselViewModel.Option.Product): ViewHintListener

}