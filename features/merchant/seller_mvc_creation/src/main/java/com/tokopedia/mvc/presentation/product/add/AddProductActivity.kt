package com.tokopedia.mvc.presentation.product.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.mvc.R

class AddProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_add_product)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AddProductFragment.newInstance(0L))
            .commit()
    }

}
