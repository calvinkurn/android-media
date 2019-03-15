package com.tokopedia.merchantvoucher.voucherList

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.merchantvoucher.MerchantVoucherModuleRouter
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.analytic.MerchantVoucherTracking
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.adapter.MerchantVoucherAdapterTypeFactory
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.ShopCommonModule
import javax.inject.Inject


open class MerchantVoucherListFragment : BaseListFragment<MerchantVoucherViewModel,
        MerchantVoucherAdapterTypeFactory>(),
        MerchantVoucherListView, MerchantVoucherView.OnMerchantVoucherViewListener {

    lateinit var voucherShopId: String
    var needRefreshData: Boolean = false
    var loadingUseMerchantVoucher: ProgressDialog? = null
    var merchantVoucherTracking: MerchantVoucherTracking? = null

    var shopInfo: ShopInfo? = null
        get

    @Inject
    lateinit var presenter: MerchantVoucherListPresenter

    var onMerchantVoucherListFragmentListener: OnMerchantVoucherListFragmentListener? = null

    interface OnMerchantVoucherListFragmentListener {
        fun enableShare(shopInfo: ShopInfo)
    }

    override fun getAdapterTypeFactory(): MerchantVoucherAdapterTypeFactory {
        return MerchantVoucherAdapterTypeFactory(this, false)
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
        presenter.getVoucherList(voucherShopId)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        val recyclerView: RecyclerView = super.getRecyclerView(view)
        if (recyclerView is VerticalRecyclerView) {
            recyclerView.clearItemDecoration()
        }
        return recyclerView
    }

    override fun isOwner(): Boolean = presenter.isMyShop(voucherShopId)

    override fun onSwipeRefresh() {
        presenter.clearCache()
        super.onSwipeRefresh()
    }

    companion object {
        const val EXTRA_SHOP_ID = "shop_id"

        const val REQUEST_CODE_LOGIN = 3001
        const val REQUEST_CODE_MERCHANT_DETAIL = 3004

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
        voucherShopId = arguments!!.getString(MerchantVoucherListActivity.SHOP_ID)
        activity?.run {
            merchantVoucherTracking = MerchantVoucherTracking(application as AbstractionRouter)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAllData()
    }

    open fun loadAllData() {
        getShopInfo()
        loadInitialData()
    }

    private fun showUseMerchantVoucherLoading() {
        if (loadingUseMerchantVoucher == null) {
            loadingUseMerchantVoucher = ProgressDialog(activity)
            loadingUseMerchantVoucher!!.setCancelable(false)
            loadingUseMerchantVoucher!!.setMessage(getString(R.string.title_loading))
        }
        if (loadingUseMerchantVoucher!!.isShowing()) {
            loadingUseMerchantVoucher!!.dismiss()
        }
        loadingUseMerchantVoucher!!.show()
    }

    private fun hideUseMerchantVoucherLoading() {
        if (loadingUseMerchantVoucher != null) {
            loadingUseMerchantVoucher!!.dismiss()
        }
    }

    override fun callInitialLoadAutomatically() = false

    override fun hasInitialSwipeRefresh() = true

    protected open fun getShopInfo() {
        presenter.getShopInfo(voucherShopId)
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this@MerchantVoucherListFragment.shopInfo = shopInfo
        this.onMerchantVoucherListFragmentListener?.enableShare(shopInfo)
    }

    override fun onErrorGetShopInfo(e: Throwable) {
        //no op, share will not shown.
    }

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        adapter.clearAllElements()
        super.renderList(merchantVoucherViewModelList, false)
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        adapter.errorNetworkModel = ErrorNetworkModel().apply {
            errorMessage = ErrorHandler.getErrorMessage(context, e)
            onRetryListener = ErrorNetworkModel.OnRetryListener {
                presenter.clearCache()
                shopInfo?.run {
                    presenter.getVoucherList(info.shopId)
                }
            }
        }
        super.showGetListError(e)
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_empty_state
        emptyModel.title = getString(R.string.no_voucher)
        return emptyModel
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        if (context == null) {
            return
        }
        merchantVoucherTracking?.clickUseVoucherFromList()
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
        /*if (presenter.isLogin() == false) {
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        } else if (!presenter.isMyShop(shopId)) {
            showUseMerchantVoucherLoading();
            presenter.useMerchantVoucher(merchantVoucherViewModel.voucherCode, merchantVoucherViewModel.voucherId)
        }*/
        //TOGGLE_MVC_OFF
        activity?.run{
            val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.title_voucher_code_copied),
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(activity!!.getString(R.string.close), View.OnClickListener { snackbar.dismiss() })
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        }
    }

    override fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading()
        activity?.let { it ->
            Dialog(it, Dialog.Type.PROMINANCE).apply {
                setTitle(useMerchantVoucherQueryResult.errorMessageTitle)
                setDesc(useMerchantVoucherQueryResult.errorMessage)
                setBtnOk(getString(R.string.label_close))
                setOnOkClickListener {
                    dismiss()
                }
                show()
            }

            presenter.clearCache()
            loadInitialData()

            it.setResult(Activity.RESULT_OK)
        }
    }

    override fun onErrorUseVoucher(e: Throwable) {
        hideUseMerchantVoucherLoading()
        if (e is MessageTitleErrorException) {
            activity?.let { it ->
                Dialog(it, Dialog.Type.PROMINANCE).apply {
                    setTitle(e.errorMessageTitle)
                    setDesc(e.message)
                    setBtnOk(getString(R.string.label_close))
                    setOnOkClickListener {
                        dismiss()
                    }
                    show()
                }
            }
        } else {
            activity?.let {
                ToasterError.showClose(it, ErrorHandler.getErrorMessage(it, e))
            }
        }
    }

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        merchantVoucherTracking?.clickMvcDetailFromList()
        context?.let {
            merchantVoucherViewModel?.run {
                val intent = MerchantVoucherDetailActivity.createIntent(it, voucherId,
                        this, voucherShopId)
                startActivityForResult(intent, REQUEST_CODE_MERCHANT_DETAIL)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_MERCHANT_DETAIL -> if (resultCode == Activity.RESULT_OK) {
                activity?.setResult(Activity.RESULT_OK)
                needRefreshData = true
            }
            REQUEST_CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                needRefreshData = true
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        if (needRefreshData) {
            presenter.clearCache()
            presenter.getVoucherList(voucherShopId, 0)
            needRefreshData = false
        }
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        if (context is OnMerchantVoucherListFragmentListener) {
            onMerchantVoucherListFragmentListener = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
