package com.tokopedia.createpost.view.plist
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

 class ShopProductListActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment? {
        return ShopProductListFragment.newInstance()
//        return if (mUserSession?.isLoggedIn == true) {
//            tokoPointsHomeFragmentNew
//        } else {
//            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
//            null
//        }
    }

    companion object {
        private const val REQUEST_CODE_LOGIN = 1
    }
}
