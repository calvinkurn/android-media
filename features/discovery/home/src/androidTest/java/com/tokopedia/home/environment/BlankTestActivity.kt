package com.tokopedia.home.environment

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.home.test.R

class BlankTestActivity : AppCompatActivity(){
    var containerBlank: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)
        containerBlank = findViewById(R.id.container_blank)
    }
}
