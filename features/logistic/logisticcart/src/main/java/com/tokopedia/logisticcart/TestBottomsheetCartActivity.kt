package com.tokopedia.logisticcart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.unifycomponents.UnifyButton

class TestBottomsheetCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_example)

        val newFragment: Fragment = TestBottomsheetCartFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.parent_view_example, newFragment, "")
            .commit()
    }
}