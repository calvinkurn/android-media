package com.tokopedia.media.loader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.user.session.UserSession

class DebugMediaLoaderActivity : AppCompatActivity() {

    private val btnShow by lazy { findViewById<Button>(R.id.btn_show) }
    private val edtUrl by lazy { findViewById<EditText>(R.id.edt_url) }
    private val imgSample by lazy { findViewById<ImageView>(R.id.img_sample) }

    private val userSession by lazy {
        UserSession(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_medialoader)

        btnShow.setOnClickListener {
            val url = edtUrl.text.toString().trim()

            imgSample.loadSecureImage(url, userSession)
        }
    }

    companion object {
        private const val SAMPLE_SECURE_IMAGE_URL = "https://chat.tokopedia.com/tc/v1/download_secure/1844584535/2022-04-19/99f532b4-bfb0-11ec-9296-42010a2942a0"
    }

}