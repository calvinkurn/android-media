package com.tokopedia.top_ads_headline.view.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.top_ads_headline.Constants.ACTION_EDIT
import com.tokopedia.top_ads_headline.Constants.AD_AND_KEYWORD_COST
import com.tokopedia.top_ads_headline.Constants.AD_CONTENT
import com.tokopedia.top_ads_headline.Constants.HEADLINE_EDIT_SOURCE
import com.tokopedia.top_ads_headline.Constants.OTHERS
import com.tokopedia.top_ads_headline.Constants.TAB_POSITION
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.fragment.AdContentFragment
import com.tokopedia.top_ads_headline.view.fragment.EditAdCostFragment
import com.tokopedia.top_ads_headline.view.fragment.EditAdOthersFragment
import com.tokopedia.top_ads_headline.view.viewmodel.EditFormHeadlineViewModel
import com.tokopedia.top_ads_headline.view.viewmodel.SharedEditHeadlineViewModel
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.view.adapter.viewpager.TopAdsEditPagerAdapter
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_edit_headline_activity.*
import javax.inject.Inject

private const val FRAGMENT_1 = 0
private const val FRAGMENT_2 = 1
private const val FRAGMENT_3 = 2

class EditFormHeadlineActivity : BaseActivity(), HasComponent<HeadlineAdsComponent>, SaveButtonState, OnMinBidChangeListener {

    private lateinit var adapter: TopAdsEditPagerAdapter

    private lateinit var submitButton: UnifyButton
    private lateinit var loaderUnify: LoaderUnify

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
        submitButton = findViewById(R.id.btn_submit)
        loaderUnify = findViewById(R.id.loader_unify)
        component.inject(this)
        setUpToolbar()
        showLoader()
        getDataFromIntent()
        setUpObservers()
        fetchAdDetails()
        renderTabAndViewPager()
        submitButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val fragments = (view_pager.adapter as TopAdsEditPagerAdapter).list
        var valid1 = true
        for (fragment in fragments) {
            if (valid1) {
                when (fragment) {
                    is AdContentFragment -> {
                        valid1 = fragment.onClickSubmit()
                    }
                    is EditAdCostFragment -> {
                        fragment.onClickSubmit()
                    }
                    is EditAdOthersFragment -> {
                        fragment.onClickSubmit()
                    }
                }
            } else {
                setButtonState(false)
                return
            }
        }
        sharedEditHeadlineViewModel.getEditHeadlineAdLiveData().value?.let {
            val input = getTopAdsManageHeadlineInput(it)
            editFormHeadlineViewModel.editHeadlineAd(input, ::onSuccess, ::onError)
        }
    }

    private fun onSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getTopAdsManageHeadlineInput(stepperModel: HeadlineAdStepperModel): TopAdsManageHeadlineInput {
        return TopAdsManageHeadlineInput().apply {
            source = HEADLINE_EDIT_SOURCE
            operation = TopAdsManageHeadlineInput.Operation(
                    action = ACTION_EDIT,
                    group = TopAdsManageHeadlineInput.Operation.Group(
                            id = groupId,
                            shopID = userSession.shopId,
                            name = stepperModel.groupName,
                            status = sharedEditHeadlineViewModel.getStatus(),
                            priceBid = stepperModel.minBid.toFloat(),
                            dailyBudget = stepperModel.dailyBudget,
                            scheduleStart = stepperModel.startDate,
                            scheduleEnd = stepperModel.endDate,
                            adOperations = stepperModel.adOperations,
                            keywordOperations = stepperModel.keywordOperations)
            )
        }
    }

    private fun setUpObservers() {
        editFormHeadlineViewModel = ViewModelProvider(this, viewModelFactory).get(EditFormHeadlineViewModel::class.java)
        sharedEditHeadlineViewModel = ViewModelProvider(this, viewModelFactory).get(SharedEditHeadlineViewModel::class.java)
        sharedEditHeadlineViewModel.getEditHeadlineAdLiveData().observe(this, Observer {
            hideLoader()
        })
    }

    private fun showLoader() {
        loaderUnify.show()
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun hideLoader() {
        loaderUnify.hide()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun onError(message: String) {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun renderTabAndViewPager() {
        val bundle = intent.extras
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        tab_layout?.addNewTab(AD_CONTENT)
        tab_layout?.addNewTab(AD_AND_KEYWORD_COST)
        tab_layout?.addNewTab(OTHERS)
        tab_layout?.getUnifyTabLayout()?.getTabAt(bundle?.getInt(TAB_POSITION, 0) ?: 0)?.select()
        view_pager.currentItem = bundle?.getInt(TAB_POSITION, 0) ?: 0
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    FRAGMENT_2 -> {
                        ((view_pager.adapter as? TopAdsEditPagerAdapter)?.getItem(FRAGMENT_1) as? AdContentFragment)?.setSelectedProductIds()
                    }
                }
            }
        })
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getViewPagerAdapter(): TopAdsEditPagerAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        list.add(AdContentFragment.newInstance())
        list.add(EditAdCostFragment.newInstance(groupId))
        list.add(EditAdOthersFragment.newInstance())
        adapter = TopAdsEditPagerAdapter(arrayOf(AD_CONTENT, AD_AND_KEYWORD_COST, OTHERS), supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    override fun getComponent(): HeadlineAdsComponent {
        return DaggerHeadlineAdsComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun setButtonState(isEnabled: Boolean) {
        submitButton.isEnabled = isEnabled
    }

    override fun onMinBidChange(minBid: Double) {
        ((view_pager.adapter as? TopAdsEditPagerAdapter)?.getItem(FRAGMENT_3) as? EditAdOthersFragment)?.setDailyBudget(minBid)
    }
}

interface SaveButtonState {
    fun setButtonState(isEnabled: Boolean)
}

interface OnMinBidChangeListener {
    fun onMinBidChange(minBid: Double)
}