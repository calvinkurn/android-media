package com.tokopedia.feedback_form.drawonpicture.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.feedback_form.drawonpicture.di.DrawOnPictureComponent
import com.tokopedia.feedback_form.R
import com.tokopedia.feedback_form.drawonpicture.di.DaggerDrawOnPictureComponent
import com.tokopedia.feedback_form.drawonpicture.presentation.fragment.DrawOnPictureFragment
import kotlinx.android.synthetic.main.activity_draw_on_picture.*

class DrawOnPictureActivity : BaseSimpleActivity(), HasComponent<DrawOnPictureComponent> {

    private lateinit var mFragment: DrawOnPictureFragment

    override fun getNewFragment(): Fragment {
        mFragment = DrawOnPictureFragment
                .getInstance(intent.getParcelableExtra(EXTRA_IMAGE_URI))
        return mFragment
    }

    override fun getComponent(): DrawOnPictureComponent =
            DaggerDrawOnPictureComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getLayoutRes(): Int = R.layout.activity_draw_on_picture

    override fun getToolbarResourceID(): Int = R.id.dopHeaderUnify

    override fun getParentViewResourceID(): Int = R.id.dopFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dopHeaderUnify.actionText = getString(R.string.developer_options_feedback_next_label)
        dopHeaderUnify.actionTextView?.setOnClickListener {
            if (::mFragment.isInitialized) mFragment.saveNewImage()
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"

        fun getIntent(context: Context, imageUri: Uri?): Intent =
                Intent(context, DrawOnPictureActivity::class.java)
                        .putExtra(EXTRA_IMAGE_URI, imageUri)
    }

}
