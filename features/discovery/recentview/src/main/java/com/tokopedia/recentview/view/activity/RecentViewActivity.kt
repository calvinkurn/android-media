package com.tokopedia.recentview.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.recentview.view.fragment.RecentViewFragment

class RecentViewActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? = RecentViewFragment.createInstance(Bundle())

}