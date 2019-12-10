package com.tokopedia.opportunity.activity

import android.net.Uri
import android.os.Bundle
import com.tokopedia.core.app.BasePresenterActivity
import com.tokopedia.opportunity.R
import com.tokopedia.opportunity.fragment.OpportunityListFragment
import com.tokopedia.opportunity.presenter.OpportunityListPresenter

/**
 * Created by fwidjaja on 2019-09-09.
 */
class OpportunityListActivity: BasePresenterActivity<OpportunityListPresenter>() {
    override fun setupURIPass(data: Uri?) {}

    override fun setupBundlePass(extras: Bundle?) {}

    override fun initialPresenter() {}

    override fun getLayoutId(): Int {
        return R.layout.activity_simple_fragment
    }

    override fun initView() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if (fragmentManager.findFragmentById(R.id.container) == null) {
            val fragment = OpportunityListFragment.createInstance(intent.extras)
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
            fragmentTransaction.commit()
        }
    }

    override fun setViewListener() {}

    override fun initVar() {}

    override fun setActionVar() {}
}