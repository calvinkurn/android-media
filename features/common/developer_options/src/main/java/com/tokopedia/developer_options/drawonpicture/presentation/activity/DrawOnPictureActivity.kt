package com.tokopedia.developer_options.drawonpicture.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.drawonpicture.di.DaggerDrawOnPictureComponent
import com.tokopedia.developer_options.drawonpicture.di.DrawOnPictureComponent
import com.tokopedia.developer_options.drawonpicture.presentation.fragment.DrawOnPictureFragment

class DrawOnPictureActivity : BaseSimpleActivity(), HasComponent<DrawOnPictureComponent> {

    override fun getNewFragment(): Fragment = DrawOnPictureFragment
            .getInstance(intent.getParcelableExtra(EXTRA_IMAGE_URI))

    override fun getComponent(): DrawOnPictureComponent =
            DaggerDrawOnPictureComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getLayoutRes(): Int = R.layout.activity_draw_on_picture

    override fun getToolbarResourceID(): Int = R.id.dopHeaderUnify

    override fun getParentViewResourceID(): Int = R.id.dopFrameLayout

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"

        fun getIntent(context: Context, imageUri: Uri?): Intent =
                Intent(context, DrawOnPictureActivity::class.java)
                        .putExtra(EXTRA_IMAGE_URI, imageUri)
    }

}
