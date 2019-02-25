package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.affiliate.R

/**
 * @author by milhamj on 25/02/19.
 */
class MediaPreviewFragment: BaseDaggerFragment() {

    companion object {
        fun createInstance(): Fragment {
            return MediaPreviewFragment()
        }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_af_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}