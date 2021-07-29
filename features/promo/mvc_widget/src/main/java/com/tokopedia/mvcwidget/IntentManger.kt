package com.tokopedia.mvcwidget

import android.content.Intent
import android.os.Bundle

object IntentManger {

    object Action{
        val ACTION_REGISTER_MEMBER = "com.tokopedia.mvcwidget.REGISTER_MEMBER"
    }
    object Keys{
        val REGISTER_MEMBER_SUCCESS = "reg_member_suc"
        val ANIMATED_INFO = "anim_info"
        val IS_SHOWN = "is_shown"
    }


    fun getJadiMemberIntent(bundle: Bundle):Intent{
        return Intent().also {
            it.action = Action.ACTION_REGISTER_MEMBER
            it.putExtra(Keys.REGISTER_MEMBER_SUCCESS,bundle)
        }
    }

    fun prepareBundleForJadiMember(data:TokopointsCatalogMVCSummaryResponse):Bundle?{
        if(!data.data?.animatedInfoList.isNullOrEmpty()){
            val bundle = Bundle()
            val arrayList = java.util.ArrayList<AnimatedInfos>(data.data?.animatedInfoList!!)
            val isShown = data.data.isShown ?: false
            bundle.putParcelableArrayList(Keys.REGISTER_MEMBER_SUCCESS, arrayList)
            bundle.putBoolean(Keys.IS_SHOWN, isShown)
            return bundle
        }
        return null
    }
}
