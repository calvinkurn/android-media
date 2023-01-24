package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

interface ViewToViewListener {
    fun onProductImpressed(product: ViewToViewDataModel, position: Int)
    fun onProductClicked(product: ViewToViewDataModel, position: Int)
    fun onAddToCartClicked(product: ViewToViewDataModel, position: Int)
}
