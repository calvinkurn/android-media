package com.tokopedia.top_ads_headline.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.top_ads_headline.view.fragment.TopAdsProductListFragment

const val SELECTED_PRODUCT_LIST = "selectedProductList"
const val IS_EDITED = "isEdited"
const val GROUP_NAME = "group_name"

class TopAdsProductListActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        val fragment = TopAdsProductListFragment.newInstance()
        val bundle = Bundle()
        bundle.putString(GROUP_NAME, intent.getStringExtra(GROUP_NAME))
        bundle.putSerializable(SELECTED_PRODUCT_LIST, intent.getSerializableExtra(SELECTED_PRODUCT_LIST))
        fragment.arguments = bundle
        return fragment
    }

}