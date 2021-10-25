package com.tokopedia.mediauploader

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MediaUploaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_uploader)

        findViewById<TextView>(R.id.txt_hello_world).setText("Halo!")
    }

}