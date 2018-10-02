package com.tokopedia.merchantvoucher.voucherList

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.ShopCommonModule
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class MerchantVoucherListFragment : BaseListFragment<MerchantVoucherViewModel, MerchantVoucherAdapterTypeFactory>(),
        MerchantVoucherListView, MerchantVoucherView.OnMerchantVoucherViewListener {

    lateinit var shopId: String
    var shopInfo: ShopInfo? = null
        get

    @Inject
    lateinit var presenter: MerchantVoucherListPresenter

    lateinit var onMerchantVoucherListFragmentListener: OnMerchantVoucherListFragmentListener

    interface OnMerchantVoucherListFragmentListener {
        fun enableShare(shopInfo: ShopInfo)
    }

    override fun getAdapterTypeFactory(): MerchantVoucherAdapterTypeFactory {
        return MerchantVoucherAdapterTypeFactory(this)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merchant_voucher_list, container, false)
    }

    override fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .build()
                    .inject(this@MerchantVoucherListFragment)
            presenter.attachView(this@MerchantVoucherListFragment)
        }
    }

    override fun loadData(page: Int) {
        presenter.getVoucherList(shopId)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        val recyclerView: RecyclerView = super.getRecyclerView(view)
        if (recyclerView is VerticalRecyclerView) {
            recyclerView.clearItemDecoration()
        }
        return recyclerView
    }

    override fun onSwipeRefresh() {
        presenter.clearCache()
        super.onSwipeRefresh()
    }

    companion object {
        const val EXTRA_SHOP_ID = "shop_id"

        @JvmStatic
        fun createInstance(shopId: String): Fragment {
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
        }
        loadInitialData()
    }

    override fun callInitialLoadAutomatically() = false

    override fun hasInitialSwipeRefresh() = true

    private fun getShopInfo() {
        presenter.getShopInfo(shopId)
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this@MerchantVoucherListFragment.shopInfo = shopInfo
        this.onMerchantVoucherListFragmentListener.enableShare(shopInfo)
    }

    override fun onErrorGetShopInfo(e: Throwable) {
        //no op, share will not shown.
    }

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        super.renderList(merchantVoucherViewModelList, false)
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        // TODO need custom error network?
        // adapter.errorNetworkModel = ErrorNetworkModel().apply {  }
        super.showGetListError(e)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_state
        //TODO error message when voucher empty
//        emptyModel.title = getString(R.string.shop_has_no_etalase_search, searchText)
//        emptyModel.content = getString(R.string.change_your_keyword)
        return emptyModel
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        // TODO if not login, open login activity
        // TODO if login, make call to use the voucher
        Log.i("Test", "Test")
    }

    override fun onItemClicked(t: MerchantVoucherViewModel?) {
        //TODO go to detail activity
        Log.i("Test", "Test")
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onMerchantVoucherListFragmentListener = context as OnMerchantVoucherListFragmentListener
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
