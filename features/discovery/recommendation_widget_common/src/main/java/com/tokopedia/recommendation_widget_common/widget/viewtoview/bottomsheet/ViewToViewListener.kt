package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

interface ViewToViewListener {
    fun onProductClicked(product: ViewToViewDataModel.Product, position: Int)
    fun onAddToCartClicked(product: ViewToViewDataModel.Product, position: Int)
}
