package com.tokopedia.tokopedianow.educationalinfo.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.educationalinfo.presentation.fragment.TokoNowEducationalInfoFragment

class TokoNowEducationalInfoActivity: BaseTokoNowActivity() {
    override fun getFragment(): Fragment {
        return TokoNowEducationalInfoFragment.newInstance()
    }
}