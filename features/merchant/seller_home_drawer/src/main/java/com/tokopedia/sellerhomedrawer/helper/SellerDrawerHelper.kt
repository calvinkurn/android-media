package com.tokopedia.sellerhomedrawer.helper

import android.app.Activity
import android.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomedrawer.R
//import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerItem

abstract class SellerDrawerHelper {

    protected val context: Activity
    protected val drawerLayout: DrawerLayout
    protected val recyclerView: RecyclerView
    protected val toolbar: Toolbar

    constructor(activity: Activity) {
        context = activity
        drawerLayout = activity.findViewById(R.id.drawer_layout_nav)
        recyclerView = activity.findViewById(R.id.left_drawer)
        toolbar = activity.findViewById(R.id.app_bar)
    }

    companion object {
        @JvmStatic
        val DRAWER_CACHE = "DRAWER_CACHE"
        @JvmStatic
        val REQUEST_LOGIN = 345
    }

//    abstract fun createDrawerData(): ArrayList<SellerDrawerItem>

    abstract fun initDrawer(activity: Activity)

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }
}