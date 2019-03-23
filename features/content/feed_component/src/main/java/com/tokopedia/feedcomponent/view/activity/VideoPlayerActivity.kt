package com.tokopedia.feedcomponent.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.view.Window
import android.view.WindowManager
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Footer
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Header
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateFooter
import com.tokopedia.feedcomponent.view.fragment.SingleVideoPlayerFragment
import com.tokopedia.feedcomponent.view.viewmodel.data.FooterViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.HeaderViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.template.TemplateFooterViewModel
import com.tokopedia.kotlin.extensions.view.hide

/**
 * @author by yfsx on 23/03/19.
 */
class VideoPlayerActivity: BaseSimpleActivity() {

    companion object {
        const val PARAM_SINGLE_URL = "PARAM_SINGLE_URL"
        const val PARAM_HEADER = "PARAM_HEADER"
        const val PARAM_FOOTER = "PARAM_FOOTER"
        const val PARAM_TEMPLATE_FOOTER = "PARAM_TEMPLATE_FOOTER"
        fun getInstance(context: Context,
                        url: String,
                        header: HeaderViewModel,
                        footer: FooterViewModel,
                        templateFooter: TemplateFooterViewModel): Intent {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            val bundle = Bundle()
            bundle.putString(PARAM_SINGLE_URL, url)
            bundle.putParcelable(PARAM_HEADER, header)
            bundle.putParcelable(PARAM_FOOTER, footer)
            bundle.putParcelable(PARAM_TEMPLATE_FOOTER, templateFooter)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun getNewFragment(): Fragment {
        toolbar.hide()
        return SingleVideoPlayerFragment.getInstance(intent.extras)
    }
}