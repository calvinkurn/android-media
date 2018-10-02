package com.tokopedia.merchantvoucher.voucherList

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.ShopCommonModule
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class MerchantVoucherListFragment: BaseListFragment<MerchantVoucherViewModel, MerchantVoucherAdapterTypeFactory>() ,
        MerchantVoucherListView {

    lateinit var shopId: String
    var shopInfo: ShopInfo? = null
    get

    @Inject
    lateinit var presenter: MerchantVoucherListPresenter

    override fun getAdapterTypeFactory(): MerchantVoucherAdapterTypeFactory {
        return MerchantVoucherAdapterTypeFactory()
    }

    override fun onItemClicked(t: MerchantVoucherViewModel?) {
        //TODO
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        activity?.run {
            if (this.application is BaseMainApplication) {
                DaggerMerchantVoucherComponent.builder()
                        .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                        .shopCommonModule(ShopCommonModule())
                        .build()
                        .inject(this@MerchantVoucherListFragment)
                presenter.attachView(this@MerchantVoucherListFragment)
            }
        }

    }

    override fun loadData(page: Int) {
        //TODO
    }

    companion object {
        const val EXTRA_SHOP_ID = "shop_id"

        @JvmStatic
        fun createInstance(shopId:String) :Fragment {
            return MerchantVoucherListFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SHOP_ID, shopId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopId = arguments!!.getString(MerchantVoucherListActivity.SHOP_ID)
        super.onCreate(savedInstanceState)

        if (shopInfo == null) {
            getShopInfo()
        } else {
            getVoucherList()
        }
    }

    override fun callInitialLoadAutomatically() = false

    override fun hasInitialSwipeRefresh() = true

    private fun getShopInfo(){
        presenter.getShopInfo(shopId)
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        shopInfo.run {
            this@MerchantVoucherListFragment.shopInfo = this
            getVoucherList()
        }
    }

    override fun onErrorGetShopInfo(e: Throwable) {
        //TODO show snackbar or full page
    }

    private fun getVoucherList(){

    }

}
