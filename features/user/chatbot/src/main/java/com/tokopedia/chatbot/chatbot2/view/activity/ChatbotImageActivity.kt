package com.tokopedia.chatbot.chatbot2.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.databinding.ImagePreviewActivityBinding
import com.tokopedia.image_gallery.ImageGalleryItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class ChatbotImageActivity : BaseSimpleActivity() {

    private var viewBinding: ImagePreviewActivityBinding? by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_preview_activity)

        val imageUrl = intent.extras?.getString(IMAGE_URL) ?: ""

        viewBinding?.let {
            renderImageGallery(it, imageUrl)
        }
    }

    private fun renderImageGallery(viewBinding: ImagePreviewActivityBinding, imageUrl: String) {
        with(viewBinding) {
            val imageGalleryItems = arrayListOf<ImageGalleryItem>()
            imageGalleryItems.add(ImageGalleryItem(null, imageUrl))
            if (imageGalleryItems.isNotEmpty()) {
                imageGallery.apply {
                    isHiddenOverlay = true
                    overlayContainer.gone()
                    setImages(imageGalleryItems)
                }
                imageGallery.show()
            } else {
                imageGallery.gone()
            }

            buttonClose.setOnClickListener {
                finish()
            }
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.image_preview_activity
    }

    companion object {

        const val IMAGE_URL = "IMAGE_URL"

        @JvmStatic
        fun getCallingIntent(
            context: Context,
            imageUrl: String
        ): Intent {
            val intent = Intent(context, ChatbotImageActivity::class.java)
            val bundle = Bundle()
            bundle.putString(IMAGE_URL, imageUrl)
            intent.putExtras(bundle)
            return intent
        }
    }
}
