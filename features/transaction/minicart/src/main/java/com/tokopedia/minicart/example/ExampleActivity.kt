package com.tokopedia.minicart.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.minicart.R

class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        val newFragment: Fragment = ExampleFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view_example, newFragment, "")
                .commit()
    }

}