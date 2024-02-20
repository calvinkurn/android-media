package com.tokopedia.media.loader

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.FailureType
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

        edtUrl.setText("https://images.tokopedia.net/img/cache/200/haryot/2023/10/10/6cf964e4-f3a9-4645-abd3-de8fc3725c83.jpg")
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
            var isArchived: FailureType? = null


            imgSample.loadImage(url) {
                setRoundedRadius(roundedRadius) // sample rounded
                shouldTrackNetworkResponse(true)
                networkResponse { _, type ->
                    isArchived = type
                }
                listener(
                    onSuccess = { _, _ ->
                        checkMediaFailure(isArchived)
                    },
                    onError = {
                        checkMediaFailure(isArchived)
                    }
                )
            }
        }
    }

    private fun checkMediaFailure(failureType: FailureType?) {
        if (failureType != null) {
            Handler().postDelayed({
                val archivalUrl = "https://images.tokopedia.net/img/android/order_management/img_product_archived_small.png"
                imgSample.loadImage(archivalUrl)
                Toast.makeText(this, "Failure = ${failureType.value}", Toast.LENGTH_LONG).show()
            },1000)
        }
    }
}
