package com.tokopedia.profile.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.profile.view.listener.ProfileContract

/**
 * @author by milhamj on 9/17/18.
 */

class ProfileFragment : BaseDaggerFragment(), ProfileContract.View {

    companion object {
        fun createInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }
}