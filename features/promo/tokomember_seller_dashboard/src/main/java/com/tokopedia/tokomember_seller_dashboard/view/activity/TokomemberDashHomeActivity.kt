package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponDetailCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmCouponListRefreshCallback
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.util.ADD_QUOTA
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_CARD_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_IS_SHOW_BS
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ACTION
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_PROGRAM_ID
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_SHOP_ID
import com.tokopedia.tokomember_seller_dashboard.util.PATH_TOKOMEMBER_COUPON_DETAIL
import com.tokopedia.tokomember_seller_dashboard.util.REFRESH
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH_HOME
import com.tokopedia.tokomember_seller_dashboard.util.REQUEST_CODE_REFRESH_PROGRAM_LIST
import com.tokopedia.tokomember_seller_dashboard.util.TOKOMEMBER_SCREEN
import com.tokopedia.tokomember_seller_dashboard.util.TmPrefManager
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmDashCouponDetailFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TmDashCouponDetailFragment.Companion.TAG_COUPON_DETAIL
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment.Companion.TAG_HOME
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramDetailFragment
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.tm_activity_tokomember_dash_home.*
import javax.inject.Inject

class TokomemberDashHomeActivity : AppCompatActivity(), TmProgramDetailCallback,TmCouponDetailCallback{

    private lateinit var homeHeader: HeaderUnify
    private lateinit var homeTabs: TabsUnify
    private lateinit var homeViewPager: ViewPager
    private var bundleNewIntent: Bundle? = null
    private var tmCouponListRefreshCallback: TmCouponListRefreshCallback? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmProgramListViewModel: TmProgramListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmProgramListViewModel::class.java)
    }
    private val tokomemberDashHomeViewmodel: TokomemberDashHomeViewmodel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashHomeViewmodel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tm_activity_tokomember_dash_home)
        if(!checkApplink()){
            if (bundleNewIntent == null) {
                openHomeFragment(intent.extras)
            } else {
                openHomeFragment(bundleNewIntent)
            }
        }
        initDagger()
    }

    private fun initDagger() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 1 -> {
                if(supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name == TAG_COUPON_DETAIL)
                    tmCouponListRefreshCallback?.refreshCouponList(ADD_QUOTA)
                supportFragmentManager.popBackStack()
            }
            supportFragmentManager.backStackEntryCount == 1 -> {
                supportFragmentManager.popBackStack()
                return super.onBackPressed()
            }
            else -> {
                return super.onBackPressed()
            }
        }
    }

    fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_home, fragment, tag)
            .addToBackStack(tag).commit()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        bundleNewIntent = intent?.extras
        openHomeFragment(bundleNewIntent)
    }

    private fun openHomeFragment(bundle: Bundle?) {
        addFragment(TokomemberDashHomeMainFragment.newInstance(bundle), TAG_HOME)
    }

    override fun openDetailFragment(shopId: Int, programId: Int) {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_SHOP_ID, shopId)
        bundle.putInt(BUNDLE_PROGRAM_ID, programId)
        addFragment(TokomemberDashProgramDetailFragment.newInstance(bundle), TAG_HOME)
    }

    override fun openCouponDetailFragment(voucherId:Int, tmCouponListRefreshCallback: TmCouponListRefreshCallback?) {
        this.tmCouponListRefreshCallback = tmCouponListRefreshCallback
        addFragment(TmDashCouponDetailFragment.newInstance(voucherId), TAG_COUPON_DETAIL)
    }

    private fun checkApplink() : Boolean{
        intent.extras?.let{
            it.get(TOKOMEMBER_SCREEN).also { it1 ->
                if(it1 is Uri){
                    val segments = it1.pathSegments
                    if(segments.size>=2){
                        when(segments[1]){
                            PATH_TOKOMEMBER_COUPON_DETAIL -> {
                              val couponId=segments[2]
                                Log.i("from dash home","voicher id - $couponId")
                                openCouponDetailFragment(couponId.toIntOrZero(), tmCouponListRefreshCallback)
                                return true
                            }
                            else -> return false
                        }
                    }
                }
            }
        }
        return false
    }

    companion object{
        fun openActivity(shopId: Int, cardID:Int, context: Context? , isShowBs:Boolean = false, programAction: Int = -1){
            context?.let {
                val intent = Intent(it, TokomemberDashHomeActivity::class.java)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                intent.putExtra(BUNDLE_CARD_ID, cardID)
                intent.putExtra(BUNDLE_IS_SHOW_BS, isShowBs)
                intent.putExtra(BUNDLE_PROGRAM_ACTION, programAction)
                if(context is TokomemberMainActivity)
                   intent.putExtra(TOKOMEMBER_SCREEN,context.intent?.data)
                it.startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_REFRESH_PROGRAM_LIST){
            if(resultCode == Activity.RESULT_OK){
                val state = data?.getIntExtra("REFRESH_STATE", REFRESH)
                if (state != null) {
                    Toaster.build(container_home, "Program TokoMember kamu berhasil diubah.", Toaster.TYPE_NORMAL).show()
                    tmProgramListViewModel.refreshProgramList(state)
                }
            }
        }
        if(requestCode == REQUEST_CODE_REFRESH_HOME){
            if(resultCode == Activity.RESULT_OK){
                val state = data?.getIntExtra("REFRESH_STATE", REFRESH)
                if (state != null) {
                    Toaster.build(container_home, "Yay! Perubahan Kartu TokoMembe disimpan.").show()
                    tokomemberDashHomeViewmodel.refreshHomeData(state)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        TmPrefManager(this).clearPref()
    }


}
