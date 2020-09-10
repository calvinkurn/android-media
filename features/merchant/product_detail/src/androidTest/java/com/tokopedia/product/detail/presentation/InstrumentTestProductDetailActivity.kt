package com.tokopedia.product.detail.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.detail.test.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet

class InstrumentTestProductDetailActivity : AppCompatActivity() {

    private var shouldShowCartAnimation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail_test)
        showAddToCartDoneBottomSheet()
    }

    private fun showAddToCartDoneBottomSheet() {
        val addToCartDoneBottomSheet = InstrumentTestAddToCartBottomSheet()
        val productName = "Bunga 3"
        val productImageUrl = "https://ecs7-p.tokopedia.net/img/cache/200-square/product-1/2019/11/7/77816542/77816542_a7945d31-95c8-4795-ac94-2d4a63d4ebff_1024_1024"
        val addedProductDataModel = AddToCartDoneAddedProductDataModel(
                "604161938",
                productName,
                productImageUrl,
                false,
                6996572
        )
        val bundleData = Bundle()
        bundleData.putParcelable(AddToCartDoneBottomSheet.KEY_ADDED_PRODUCT_DATA_MODEL, addedProductDataModel)
        addToCartDoneBottomSheet.arguments = bundleData
        addToCartDoneBottomSheet.setDismissListener(BottomSheets.BottomSheetDismissListener {
            shouldShowCartAnimation = true
        })
        supportFragmentManager.let {
            addToCartDoneBottomSheet.show(
                    it, "ADD_TO_CART"
            )
        }
    }
}