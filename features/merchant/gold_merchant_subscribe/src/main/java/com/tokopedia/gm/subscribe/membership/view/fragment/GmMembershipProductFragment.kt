package com.tokopedia.gm.subscribe.membership.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.membership.view.adapter.GmMembershipProductAdapter
import com.tokopedia.gm.subscribe.membership.view.fragment.GmMembershipFragment.Companion.EXTRA_SUBSCRIPTION_NAME
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

    override fun setVisibilitySelectButton(isView: Boolean) {
        buttonSelectProduct.visibility = View.GONE
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
                setReturn()
            }
        }
        return true
    }

    fun setReturn(){
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_SUBSCRIPTION_TYPE, currentSelectedProductId)
        resultIntent.putExtra(EXTRA_SUBSCRIPTION_NAME, currentSelectedProductName)
        activity.setResult(Activity.RESULT_OK, resultIntent)
        activity.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance(defaultSelected : Int) : GmMembershipProductFragment {
            val fragment = GmMembershipProductFragment()
            val args = Bundle()
            args.putInt(DEFAULT_SELECTED_PRODUCT, defaultSelected)
            fragment.arguments = args
            return fragment

        }
    }
}