package com.tokopedia.media.loader.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.media.loader.*
import com.tokopedia.media.loader.common.Properties
import com.tokopedia.media.loader.data.ERROR_RES_UNIFY
import com.tokopedia.media.loader.transform.BlurHashDecoder
import com.tokopedia.media.loader.transform.TopRightCrop
import com.tokopedia.media.loader.utils.AspectRatio
import com.tokopedia.media.loader.utils.MediaTarget
import kotlinx.android.synthetic.main.activity_test_media.*

class MediaActivity : AppCompatActivity() {

    private lateinit var url: String
    private val hash = "A4ADcRuO_2y?"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_media)

        default()

        // from medialoader: normal
        btnBlurHashFromLoader1?.setOnClickListener {
            imgTest?.loadImage(generateBlurHash(hash))
        }

        // from medialoader: aspect ratio
        btnBlurHashFromLoader2?.setOnClickListener {
            imgTest?.loadImage(generateBlurHash(hash, imgTest.measuredWidth, imgTest.measuredHeight))
        }

        // from native: normal
        btnBlurHashFromNative1?.setOnClickListener {
            imgTest?.setImageBitmap(generateBlurHash(hash))
        }

        // from native: aspect ratio
        btnBlurHashFromNative2?.setOnClickListener {
            imgTest?.setImageBitmap(generateBlurHash(hash, imgTest.measuredWidth, imgTest.measuredHeight))
        }

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

        btnLoadIcon?.setOnClickListener { loadIcon() }

        btnTopRightCrop?.setOnClickListener { loadImageTopRightCrop() }
    }

    private fun generateBlurHash(hash: String?, width: Int? = 2, height: Int? = 2): Bitmap? {
        imgTest?.scaleType = ImageView.ScaleType.FIT_CENTER

        val ratio = AspectRatio.calculate(
                (width?: 2) + 10,
                (height?: 2) + 10
        )

        return BlurHashDecoder.decode(
                blurHash = hash,
                width = ratio.first,
                height = ratio.second,
                parallelTasks = 2
        )
    }

    private fun default() {
        url = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2021/2/1/e0e5d52e-2d9c-498f-8b13-2cba0e2f1e11.jpg?b=A4ADcRuO_2y?"
        edtUrl?.setText(url)
        imgTest?.scaleType = ImageView.ScaleType.FIT_XY
    }

    private fun loadImage() {
        default()

        imgTest?.loadImage(url) {
            status("loadImage", this)
        }
    }

    private fun loadImageRounded() {
        imgTest?.loadImageRounded(url, 100f) {
            status("loadImageRounded()", this)
        }
    }

    private fun loadImageFitCenter() {
        imgTest?.loadImage(url) {
            fitCenter()
            status("loadImageFitCenter()", this)
        }
    }

    private fun loadImageCenterCrop() {
        imgTest?.loadImage(url) {
            centerCrop()
            status("loadImageCenterCrop()", this)
        }
    }

    private fun loadImageCircle() {
        imgTest?.loadImageCircle(url)

        Handler().postDelayed({
            imgTest?.loadImage(url) {
                isCircular(true)
                status("loadImageCircle()", this)
            }
        }, 3000)
    }

    private fun loadImageError() {
        imgTest?.loadImage("https://test.error/loremipsum.png") {
            status("loadImageError()", this)
        }
    }

    private fun loadImageCustomError() {
        Toast.makeText(applicationContext, "initialize...", Toast.LENGTH_SHORT).show()
        imgTest?.loadImage(url) {
            setErrorDrawable(ERROR_RES_UNIFY)
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
        }, MediaTarget(imgTest, onReady = { _, resource ->
            imgTest.setImageBitmap(resource)
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

    private fun loadIcon() {
        url = "https://ecs7.tokopedia.net/pdp/info/icon/pdp-paylatercicilan@3x.png"
        edtUrl?.setText(url)

        imgTest?.loadIcon(url)
        imgTest?.scaleType = ImageView.ScaleType.FIT_CENTER
    }
    
    private fun loadImageTopRightCrop() {
        imgTest?.loadImage(url)

        Handler().postDelayed({
            imgTest?.loadImage(url) {
                transform(TopRightCrop())
            }
        }, 3000)
    }

    private fun status(methodNme: String, properties: Properties) {
        println("method = $methodNme \n" +
                "url = $url \n" +
                "$properties")
    }

}