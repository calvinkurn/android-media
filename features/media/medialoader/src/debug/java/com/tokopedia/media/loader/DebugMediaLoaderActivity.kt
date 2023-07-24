package com.tokopedia.media.loader

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.Label

/** @suppress */
open class DebugMediaLoaderActivity : AppCompatActivity() {

    private val btnShow: Button by lazy { findViewById(R.id.btn_show) }
    private val btnEdit: ImageView by lazy { findViewById(R.id.img_edit) }
    private val edtUrl: EditText by lazy { findViewById(R.id.edt_url) }
    private val edtProperties: EditText by lazy { findViewById(R.id.edt_properties) }
    private val label: Label by lazy { findViewById(R.id.label) }

    @VisibleForTesting
    val imgSample: ImageView by lazy { findViewById(R.id.img_sample) }

    private var isCustomPropertiesVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug_medialoader)

        edtUrl.setText("")
        edtProperties.setText("")

        btnEdit.setOnClickListener {
            isCustomPropertiesVisible = if (!isCustomPropertiesVisible) {
                edtProperties.show()
                true
            } else {
                edtProperties.hide()
                false
            }
        }

        btnShow.setOnClickListener {
            val url = edtUrl.text.toString().trim()

            imgSample.loadImage(url) {
                shouldTrackNetworkResponse(true)

                networkResponse { _, failureType ->
                    label.showWithCondition(failureType != null)

                    if (failureType != null) {
                        label.setLabel(failureType.toString())
                    }
                }
            }
        }
    }
}
