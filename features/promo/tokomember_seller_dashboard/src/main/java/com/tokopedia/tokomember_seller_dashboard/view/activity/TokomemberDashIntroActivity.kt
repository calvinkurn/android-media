package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPEN_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_AVATAR
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_NAME
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmIntroFragment

class TokomemberDashIntroActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {

        return TmIntroFragment.newInstance(intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    companion object{
        fun openActivity(shopId: Int, shopAvatar:String,shopName:String, openBS: Boolean = false, context: Context?){
            context?.let {
                val intent = Intent(it, TokomemberDashIntroActivity::class.java)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                intent.putExtra(BUNDLE_SHOP_NAME,shopName)
                intent.putExtra(BUNDLE_SHOP_AVATAR,shopAvatar)
                intent.putExtra(BUNDLE_OPEN_BS, openBS)
                it.startActivity(intent)
            }
        }
    }

}