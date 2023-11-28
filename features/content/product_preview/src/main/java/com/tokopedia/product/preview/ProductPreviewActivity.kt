package com.tokopedia.product.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.product.preview.R as productpreviewR

class ProductPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(productpreviewR.layout.activity_product_preview)
    }
}
