package com.tokopedia.imagepicker_insta.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.imagepicker_insta.R

class NoPermissionFragment: Fragment() {

    fun getLayout() = R.layout.imagepicker_insta_no_perm_cam_fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(context).inflate(getLayout(), container, false)
        return v
    }
}