package com.tokopedia.merchantvoucher.voucherList

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.toBlankOrString
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
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR


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

    override fun getRecyclerView(view: View?): RecyclerView? {
        val recyclerView: RecyclerView? = super.getRecyclerView(view)
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
        voucherShopId = arguments?.getString(MerchantVoucherListActivity.SHOP_ID).orEmpty()
        activity?.run {
            merchantVoucherTracking = MerchantVoucherTracking()
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
            loadingUseMerchantVoucher!!.setMessage(getString(abstractionR.string.title_loading))
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
                    presenter.getVoucherList(this.shopCore.shopID)
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
        merchantVoucherTracking?.clickUseVoucherFromList(merchantVoucherViewModel.voucherId.toString())
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
        /*if (presenter.isLogin() == false) {
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        } else if (!presenter.isMyShop(voucherShopId)) {
            showUseMerchantVoucherLoading();
            presenter.useMerchantVoucher(merchantVoucherViewModel.voucherCode, merchantVoucherViewModel.voucherId)
        }*/
        //TOGGLE_MVC_OFF
        activity?.run {
            val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.title_voucher_code_copied),
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(requireActivity().getString(R.string.mvc_label_close)) { snackbar.dismiss() }
            snackbar.setActionTextColor(androidx.core.content.ContextCompat.getColor(this, unifyprinciplesR.color.Unify_NN0))
            snackbar.show()
        }
    }

    override fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading()
        activity?.let { it ->
            DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(useMerchantVoucherQueryResult.errorMessageTitle)
                setDescription(useMerchantVoucherQueryResult.errorMessage.toBlankOrString())
                setPrimaryCTAText(getString(R.string.mvc_label_close))
                setPrimaryCTAClickListener { dismiss() }
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
                DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(e.errorMessageTitle)
                    setDescription(e.message.toBlankOrString())
                    setPrimaryCTAText(getString(R.string.mvc_label_close))
                    setPrimaryCTAClickListener { dismiss() }
                    show()
                }
            }
        } else {
            activity?.let {
                Toaster.make(requireView(), ErrorHandler.getErrorMessage(it, e), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
            }
        }
    }

    override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel?) {
        context?.let {
            merchantVoucherViewModel?.run {
                merchantVoucherTracking?.clickMvcDetailFromList(voucherId.toString())
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
