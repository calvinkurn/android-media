package com.tokopedia.media.loader.uitest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.media.loader.*
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.transform.CenterCrop
import com.tokopedia.media.loader.transform.FitCenter
import com.tokopedia.media.loader.utils.MediaTarget
import kotlinx.android.synthetic.main.activity_test_media.*

class MediaActivity : AppCompatActivity() {

    private val url = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2021/2/1/e0e5d52e-2d9c-498f-8b13-2cba0e2f1e11.jpg?b=A4ADcRuO_2y?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_media)

        btnNormal?.setOnClickListener { loadImage() }
        btnRoundedProperties?.setOnClickListener { loadImageRounded() }
        btnFitCenterProperties?.setOnClickListener { loadImageFitCenter() }
        btnCenterCropProperties?.setOnClickListener { loadImageCenterCrop() }
        btnCircle?.setOnClickListener { loadImageCircle() }

        btnError?.setOnClickListener { loadImageError() }
        btnCustomError?.setOnClickListener { loadImageCustomError() }
        btnWithoutPlaceHolder?.setOnClickListener { loadImageWithoutPlaceHolder() }
        btnCustomTarget?.setOnClickListener { loadImageCustomTarget() }
        btnBlurHash?.setOnClickListener { loadImageBlurHash() }
    }

    private fun loadImage() {
        imgTest?.loadImage(url) {
            status("loadImage", this)
        }
    }

    private fun loadImageRounded() {
        imgTest?.loadImageRounded(url) {
            status("loadImageRounded()", this)
        }
    }

    private fun loadImageFitCenter() {
        imgTest?.loadImage(url) {
            transform(FitCenter())
            status("loadImageFitCenter()", this)
        }
    }

    private fun loadImageCenterCrop() {
        imgTest?.loadImage(url) {
            transform(CenterCrop())
            status("loadImageCenterCrop()", this)
        }
    }

    private fun loadImageCircle() {
        imgTest?.loadImage(url) {
            isCircular(true)
            status("loadImageCircle()", this)
        }
    }

    private fun loadImageError() {
        imgTest?.loadImage("https://test.error/loremipsum.png") {
            status("loadImageError()", this)
        }
    }

    private fun loadImageCustomError() {
        Toast.makeText(applicationContext, "initialize...", Toast.LENGTH_SHORT).show()
        imgTest?.loadImage(url) {
            setErrorDrawable(R.drawable.media_sample_error)
            setDelay(3000)
            listener({ _, _ ->
                Toast.makeText(applicationContext, "loaded!", Toast.LENGTH_SHORT).show()
                status("loadImageCustomError()", this)
            })
        }
    }

    private fun loadImageWithoutPlaceHolder() {
        imgTest?.loadImageWithoutPlaceholder(url) {
            status("loadImageWithoutPlaceHolder()", this)
        }
    }

    private fun loadImageCustomTarget() {
        var properties: Properties? = null

        Toast.makeText(applicationContext, "initialize...", Toast.LENGTH_SHORT).show()
        loadImageWithTarget(this, url, {
            setDelay(3000)
            useBlurHash(true)
            properties = this
        }, MediaTarget(imgTest, onReady = {
            imgTest.setImageBitmap(it)
            status("loadImageCustomTarget()", properties!!)
            Toast.makeText(applicationContext, "loaded!", Toast.LENGTH_SHORT).show()
        }))
    }

    private fun loadImageBlurHash() {
        imgTest?.loadImage(url) {
            setDelay(3000)
            status("loadImageBlurHash()", this)
        }
    }

    private fun status(methodNme: String, properties: Properties) {
        txtStatus?.text = "method = $methodNme \n" +
                "url = $url \n" +
                "$properties"
    }

}