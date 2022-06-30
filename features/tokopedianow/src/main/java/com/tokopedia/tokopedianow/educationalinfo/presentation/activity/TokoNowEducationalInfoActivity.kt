package com.tokopedia.tokopedianow.educationalinfo.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.educationalinfo.presentation.fragment.TokoNowEducationalInfoFragment

/**
 * AppLink                   : ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO
 * AppLink with Play Params  : ApplinkConstInternalTokopediaNow.EDUCATIONAL_INFO + "?source=play&channel_id={channel_id}&state={live/vod}"
 */

class TokoNowEducationalInfoActivity: BaseTokoNowActivity() {
    override fun getFragment(): Fragment {
        return TokoNowEducationalInfoFragment.newInstance()
    }
}