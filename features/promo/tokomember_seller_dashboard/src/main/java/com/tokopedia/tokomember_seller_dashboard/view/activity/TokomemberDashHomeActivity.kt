package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashHomeMainFragment.Companion.TAG_HOME
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberDashProgramDetailFragment
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmProgramListViewModel
import com.tokopedia.unifycomponents.TabsUnify
import javax.inject.Inject

class TokomemberDashHomeActivity : AppCompatActivity(), TmProgramDetailCallback {

    private lateinit var homeHeader: HeaderUnify
    private lateinit var homeTabs: TabsUnify
    private lateinit var homeViewPager: ViewPager

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tmProgramListViewModel: TmProgramListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TmProgramListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokomember_dash_home)
        addFragment(TokomemberDashHomeMainFragment.newInstance(intent.extras), TAG_HOME)

        initDagger()
    }

    private fun initDagger() {
        DaggerTokomemberDashComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        }
        else if(supportFragmentManager.backStackEntryCount == 1){
            supportFragmentManager.popBackStack()
            return super.onBackPressed()
        }
        else {
            return super.onBackPressed()
        }
    }

    fun addFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_home, fragment, tag)
            .addToBackStack(tag).commit()
    }

    override fun openDetailFragment(shopId: Int, programId: Int) {
        val bundle = Bundle()
        bundle.putInt(BUNDLE_SHOP_ID, shopId)
        bundle.putInt(BUNDLE_PROGRAM_ID, programId)
        addFragment(TokomemberDashProgramDetailFragment.newInstance(bundle), TAG_HOME)
    }

    companion object{
        fun openActivity(shopId: Int, cardID:Int, context: Context? , isShowBs:Boolean = false){
            context?.let {
                val intent = Intent(it, TokomemberDashHomeActivity::class.java)
                intent.putExtra(BUNDLE_SHOP_ID, shopId)
                intent.putExtra(BUNDLE_CARD_ID, cardID)
                intent.putExtra(BUNDLE_IS_SHOW_BS, isShowBs)
                it.startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_REFRESH){
            if(resultCode == Activity.RESULT_OK){
                val state = data?.getIntExtra("REFRESH_STATE", REFRESH)
                if (state != null) {
                    tmProgramListViewModel.refreshList(state)
                }
            }
        }
    }
}