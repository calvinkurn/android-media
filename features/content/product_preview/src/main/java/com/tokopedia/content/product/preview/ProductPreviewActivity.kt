package com.tokopedia.content.product.preview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.content.product.preview.R as contentproductpreviewR

class ProductPreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentproductpreviewR.layout.activity_product_preview)
    }
}
