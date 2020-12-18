package com.tokopedia.top_ads_headline.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.fragment.EditAdContentFragment
import com.tokopedia.top_ads_headline.view.fragment.EditAdCostFragment
import com.tokopedia.top_ads_headline.view.fragment.EditAdOthersFragment
import com.tokopedia.top_ads_headline.view.viewmodel.EditFormHeadlineViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.constant.Constants
import com.tokopedia.topads.common.constant.Constants.GROUP_ID
import com.tokopedia.topads.common.constant.Constants.TAB_POSITION
import com.tokopedia.topads.common.view.adapter.viewpager.TopAdsEditPagerAdapter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_edit_headline_activity.*
import javax.inject.Inject

class EditFormHeadlineActivity : BaseActivity(), HasComponent<HeadlineAdsComponent> {

    private lateinit var adapter: TopAdsEditPagerAdapter

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var groupId: String = ""

    private lateinit var editFormHeadlineViewModel: EditFormHeadlineViewModel
    private lateinit var sharedEditHeadlineViewModel: SharedEditHeadlineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_edit_headline_activity)
        component.inject(this)
        setUpToolbar()
        getDataFromIntent()
        setUpObservers()
        fetchAdDetails()
        renderTabAndViewPager()
    }

    private fun setUpObservers() {
        editFormHeadlineViewModel = ViewModelProvider(this, viewModelFactory).get(EditFormHeadlineViewModel::class.java)
        sharedEditHeadlineViewModel = ViewModelProvider(this, viewModelFactory).get(SharedEditHeadlineViewModel::class.java)
    }

    private fun onError(message:String){
        Toaster.build(root, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun fetchAdDetails() {
        sharedEditHeadlineViewModel.getHeadlineAdId(groupId.toIntOrZero(), userSession.shopId.toIntOrZero(), this::onError)
    }

    private fun getDataFromIntent() {
        groupId = intent.getStringExtra(GROUP_ID) ?: ""
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun renderTabAndViewPager() {
        val bundle = intent.extras
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        tab_layout?.addNewTab(Constants.AD_CONTENT)
        tab_layout?.addNewTab(Constants.AD_AND_KEYWORD_COST)
        tab_layout?.addNewTab(Constants.OTHERS)
        tab_layout?.getUnifyTabLayout()?.getTabAt(bundle?.getInt(TAB_POSITION, 2) ?: 2)?.select()
        view_pager.currentItem = bundle?.getInt(TAB_POSITION, 2) ?: 2
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getViewPagerAdapter(): TopAdsEditPagerAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        list.add(EditAdContentFragment.newInstance())
        list.add(EditAdCostFragment.newInstance())
        list.add(EditAdOthersFragment.newInstance())
        adapter = TopAdsEditPagerAdapter(arrayOf(Constants.AD_CONTENT, Constants.AD_AND_KEYWORD_COST, Constants.OTHERS), supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    override fun getComponent(): HeadlineAdsComponent {
        return DaggerHeadlineAdsComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

}