package com.tokopedia.affiliate.feature.createpost.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.feature.createpost.view.fragment.MediaPreviewFragment

/**
 * @author by milhamj on 25/02/19.
 */
class MediaPreviewActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        return MediaPreviewFragment.createInstance()
    }
}