package com.tokopedia.sellerhomedrawer.drawer

import android.content.BroadcastReceiver
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager
import com.tokopedia.core.drawer2.view.DrawerHelper
import com.tokopedia.user.session.UserSession

open class SellerDrawerPresenterActivity : BaseSimpleActivity()
{
    private val MAX_NOTIF = 999

    private var isLogin: Boolean? = null
    protected var drawerHelper: DrawerHelper? = null
    protected var sessionHandler: UserSession? = null
    protected var drawerDataManager: DrawerDataManager? = null
    protected var drawerCache: LocalCacheHandler? = null
    private var drawerActivityBroadcastReceiver: BroadcastReceiver? = null
    private var broadcastReceiverTokoPoint: BroadcastReceiver? = null
    private var broadcastReceiverPendingTokocash: BroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionHandler = UserSession(applicationContext)

        //TODO : Change DrawerHelper
        drawerCache = LocalCacheHandler(this, DrawerHelper.DRAWER_CACHE)
    }

    override fun getNewFragment(): Fragment? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}