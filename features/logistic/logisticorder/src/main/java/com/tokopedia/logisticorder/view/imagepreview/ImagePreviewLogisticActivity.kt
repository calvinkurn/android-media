package com.tokopedia.logisticorder.view.imagepreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tokopedia.imagepreview.ImagePreviewActivity
import java.util.ArrayList

class ImagePreviewLogisticActivity  : ImagePreviewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(com.tokopedia.imagepreview.R.id.ivDownload)?.visibility = View.GONE
    }

    companion object {

        @JvmStatic
        @JvmOverloads
        fun createIntent(
                context: Context,
                imageUris: ArrayList<String>,
                imageDesc: ArrayList<String>? = null,
                position: Int = 0,
                title: String? = null,
                description: String? = null): Intent {
            val intent = Intent(context, ImagePreviewLogisticActivity::class.java)
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(DESCRIPTION, description)
            bundle.putStringArrayList(IMAGE_URIS, imageUris)
            bundle.putStringArrayList(IMAGE_DESC, imageDesc)
            bundle.putInt(IMG_POSITION, position)
            intent.putExtras(bundle)
            return intent
        }
    }
}