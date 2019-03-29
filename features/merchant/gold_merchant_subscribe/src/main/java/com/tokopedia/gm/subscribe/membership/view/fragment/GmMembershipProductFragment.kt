package com.tokopedia.gm.subscribe.membership.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.view.adapter.GmMembershipProductAdapter
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.DEFAULT_SUBSCRIPTION_TYPE
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.EXTRA_SUBSCRIPTION_NAME
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.EXTRA_SUBSCRIPTION_PRICE
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.EXTRA_SUBSCRIPTION_TYPE
import com.tokopedia.gm.subscribe.view.fragment.GmProductFragment
import com.tokopedia.gm.subscribe.view.viewmodel.GmProductViewModel

class GmMembershipProductFragment : GmProductFragment(){

    private var adapter: GmMembershipProductAdapter = GmMembershipProductAdapter(this)

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_gm_subscribe_product_membership
    }

    override fun initView(view: View?) {
        super.initView(view)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_default)
        adapter = GmMembershipProductAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun renderProductList(gmProductDomainModels: MutableList<GmProductViewModel>?) {
        if (selectedProductId == UNDEFINED_DEFAULT_SELECTED) {
            val selected = findBestDeal(gmProductDomainModels)
            selectedProductId(selected)
        }
        adapter.addItem(gmProductDomainModels)
    }

    override fun errorGetProductList() {
        adapter.setOnRetryListenerRV { getPackage() }
        adapter.showRetryFull(true)
    }

    override fun showProgressDialog() {
        adapter.showLoadingFull(true)
    }

    override fun dismissProgressDialog() {
        adapter.showLoadingFull(false)
    }

    override fun clearPackage() {
        adapter.clearDatas()
    }

    override fun getTitle(): String {
        return getString(R.string.gmsubscribe_extend_product_selector)
    }

    override fun getPackage() {
        presenter.getExtendPackageSelection()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                activity.finish()
            }
        }
        return true
    }

    override fun confirmSelection() {
        if (currentSelectedProductId != DEFAULT_SUBSCRIPTION_TYPE) {
            UnifyTracking.eventClickSubscribeGoldMerchant(activity, adapter.productSelection)
            setReturn()
        } else {
            NetworkErrorHelper.showSnackbar(activity, getString(R.string.gm_subscribe_no_product_selected))
        }
    }

    private fun setReturn(){
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_SUBSCRIPTION_TYPE, currentSelectedProductId)
        resultIntent.putExtra(EXTRA_SUBSCRIPTION_NAME, currentSelectedProductName)
        resultIntent.putExtra(EXTRA_SUBSCRIPTION_PRICE, currentSelectedProductPrice)
        activity.setResult(Activity.RESULT_OK, resultIntent)
        activity.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance(defaultSelected : Int, buttonString : String) : GmMembershipProductFragment {
            val fragment = GmMembershipProductFragment()
            val args = Bundle()
            args.putInt(DEFAULT_SELECTED_PRODUCT, defaultSelected)
            args.putString(STRING_BUTTON_SELECT, buttonString)
            fragment.arguments = args
            return fragment

        }
    }
}