package com.tokopedia.sellerpersona.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerpersona.view.fragment.ResultFragment

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

class ResultActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = ResultFragment.newInstance()
}