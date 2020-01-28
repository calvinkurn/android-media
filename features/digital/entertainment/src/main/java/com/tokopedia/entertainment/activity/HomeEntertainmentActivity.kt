package com.tokopedia.entertainment.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.entertainment.fragment.HomeEntertainmentFragment

/**
 * Author errysuprayogi on 27,January,2020
 */
class HomeEntertainmentActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = HomeEntertainmentFragment.getInstance()
}