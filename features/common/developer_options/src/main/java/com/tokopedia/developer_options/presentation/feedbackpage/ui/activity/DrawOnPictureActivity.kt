package com.tokopedia.developer_options.presentation.feedbackpage.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.developer_options.presentation.feedbackpage.ui.fragment.DrawOnPictureFragment

class DrawOnPictureActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = DrawOnPictureFragment
            .getInstance(intent.getParcelableExtra<Uri>(EXTRA_IMAGE_URI))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        supportActionBar?.hide()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"

        fun getIntent(context: Context, imageUri: Uri?): Intent =
                Intent(context, DrawOnPictureActivity::class.java)
                        .putExtra(EXTRA_IMAGE_URI, imageUri)
    }

}
