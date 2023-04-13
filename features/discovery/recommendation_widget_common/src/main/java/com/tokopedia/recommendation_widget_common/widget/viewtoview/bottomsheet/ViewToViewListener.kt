package com.tokopedia.recommendation_widget_common.widget.viewtoview.bottomsheet

interface ViewToViewListener {
    fun onProductImpressed(
        product: ViewToViewDataModel,
        position: Int,
        className: String,
    )

    fun onProductClicked(
        product: ViewToViewDataModel,
        position: Int,
        className: String,
    )

    fun onAddToCartClicked(
        product: ViewToViewDataModel,
        position: Int,
    )
}
