package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

interface ViewToViewListener {
    fun onProductImpressed(product: ViewToViewDataModel.Product, position: Int)
    fun onProductClicked(product: ViewToViewDataModel.Product, position: Int)
    fun onAddToCartClicked(product: ViewToViewDataModel.Product, position: Int)
}
