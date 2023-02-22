package com.tokopedia.media.loader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.Properties

open class DebugMediaLoaderActivity : AppCompatActivity() {

    private val btnShow by lazy { findViewById<Button>(R.id.btn_show) }
    private val btnEdit by lazy { findViewById<ImageView>(R.id.img_edit) }
    private val edtUrl by lazy { findViewById<EditText>(R.id.edt_url) }
    private val edtProperties by lazy { findViewById<EditText>(R.id.edt_properties) }
    private val imgSample by lazy { findViewById<ImageView>(R.id.img_sample) }

    private var isCustomPropertiesVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_medialoader)

        btnShow.setOnClickListener {
            val url = edtUrl.text.toString().trim()

            imgSample.loadSecureImage(url, userSession)
        }
    }

    private fun ImageView.debugLoadImage(
        url: String? = "",
        properties: Properties = Properties()
    ) = call(url, properties)
}