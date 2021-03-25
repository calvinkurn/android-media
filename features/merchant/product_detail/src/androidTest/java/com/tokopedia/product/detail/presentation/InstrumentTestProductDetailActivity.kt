package com.tokopedia.product.detail.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.product.detail.test.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet

class InstrumentTestProductDetailActivity : AppCompatActivity(), InstrumentTestTopAdsCounter {

    private var shouldShowCartAnimation = false
    private var topAdsCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail_test)
        showAddToCartDoneBottomSheet()
    }

    override fun onTopAdsUrlHit() {
        topAdsCount++
    }

    private fun showAddToCartDoneBottomSheet() {
        val addToCartDoneBottomSheet = InstrumentTestAddToCartBottomSheet(this)
        val productName = "Paket HDMI Dongle + HDMI To AV Alat Konverter Dari Hp ke TV Tabung"
        val productImageUrl = "https://ecs7-p.tokopedia.net/img/cache/200-square/product-1/2019/10/26/13988587/13988587_26632e2e-cb31-4729-8471-f56de2375c4b_806_806"
        val addedProductDataModel = AddToCartDoneAddedProductDataModel(
                "596843822",
                productName,
                productImageUrl,
                false,
                333311,
                ""
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


    fun getTopAdsCount(): Int {
        return topAdsCount
    }
}