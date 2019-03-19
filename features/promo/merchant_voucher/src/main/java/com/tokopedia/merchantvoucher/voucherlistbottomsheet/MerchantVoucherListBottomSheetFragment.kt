package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment.Companion.REQUEST_CODE_LOGIN
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.ShopCommonModule
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/03/19.
 */

open class MerchantVoucherListBottomSheetFragment : BottomSheets(), MerchantVoucherListView, MerchantVoucherView.OnMerchantVoucherViewListener {

    @Suppress("DEPRECATION")
    private var loadingUseMerchantVoucher: ProgressDialog? = null
    private var progressDialog: ProgressDialog? = null
    private lateinit var merchantVoucherListBottomSheetAdapter: MerchantVoucherListBottomSheetAdapter
    private lateinit var rvVoucherList: RecyclerView

    lateinit var shopId: String
    lateinit var checkoutType: String

    @Inject
    lateinit var presenter: MerchantVoucherListPresenter

    companion object {
        @JvmStatic
        fun newInstance(merchantVoucherListBottomsheetParamData: MerchantVoucherListBottomsheetParamData): MerchantVoucherListBottomSheetFragment {
            return MerchantVoucherListBottomSheetFragment().apply {
                arguments = merchantVoucherListBottomsheetParamData.getBundle()
            }
        }
    }

    override fun initView(view: View) {
        getArgumentsValue()
        merchantVoucherListBottomSheetAdapter = MerchantVoucherListBottomSheetAdapter(this)
        if (activity != null) {
            initInjector()
        }

        @Suppress("DEPRECATION")
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(getString(R.string.title_loading))

        // merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget)
        rvVoucherList = view.findViewById(R.id.rvVoucherList)
        presenter.clearCache()
        presenter.getVoucherList(shopId)
    }

    fun getArgumentsValue() {
        shopId = arguments?.getString(MerchantVoucherListBottomsheetParamData.EXTRA_SHOP_ID) ?: MerchantVoucherListBottomsheetParamData.EXTRA_SHOP_ID_DEFAULT_VALUE
        checkoutType = arguments?.getString(MerchantVoucherListBottomsheetParamData.EXTRA_CHECKOUT_TYPE) ?: MerchantVoucherListBottomsheetParamData.EXTRA_CHECKOUT_TYPE_DEFAULT_VALUE
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_merchant_voucher
    }

    override fun title(): String {
        return getString(R.string.merchant_bottomsheet_title)
    }

    override fun isOwner(): Boolean {
        return false
    }

    fun onMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        context?.let {
            merchantVoucherViewModel.run {
                this.status = MerchantVoucherStatusTypeDef.TYPE_RUN_OUT
                val intent = MerchantVoucherDetailActivity.createIntent(it, voucherId,
                        this, shopId)
                startActivityForResult(intent, MerchantVoucherListFragment.REQUEST_CODE_MERCHANT_DETAIL)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        if (context == null) {
            return
        }
        // merchantVoucherTracking?.clickUseVoucherFromList()
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
        if (!presenter.isLogin()) {
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        } else if (!presenter.isMyShop(shopId)) {
            showUseMerchantVoucherLoading()
            presenter.useMerchantVoucher(merchantVoucherViewModel.voucherCode, merchantVoucherViewModel.voucherId)
        }

        //TOGGLE_MVC_OFF
        /*activity?.run {
            val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.title_voucher_code_copied),
                    Snackbar.LENGTH_LONG)
            snackbar.setAction(activity!!.getString(R.string.close), View.OnClickListener { snackbar.dismiss() })
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.show()
        }*/
    }

    fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .build()
                    .inject(this@MerchantVoucherListBottomSheetFragment)
            presenter.attachView(this@MerchantVoucherListBottomSheetFragment)
        }
    }

    override fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        // no op
    }

    override fun onErrorGetShopInfo(e: Throwable) {
        // no op
    }

    override fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading()
        activity?.let {
            Dialog(it, Dialog.Type.PROMINANCE).apply {
                setTitle(useMerchantVoucherQueryResult.errorMessageTitle)
                setDesc(useMerchantVoucherQueryResult.errorMessage)
                setBtnOk(getString(com.tokopedia.merchantvoucher.R.string.label_close))
                setOnOkClickListener {
                    dismiss()
                }
                show()
            }

            presenter.clearCache()
            // loadPromo()

            it.setResult(Activity.RESULT_OK)
        }
    }

    override fun onErrorUseVoucher(e: Throwable) {
        hideUseMerchantVoucherLoading()
        if (e is MessageTitleErrorException) {
            activity?.let {
                Dialog(it, Dialog.Type.PROMINANCE).apply {
                    setTitle(e.errorMessageTitle)
                    setDesc(e.message)
                    setBtnOk(getString(com.tokopedia.merchantvoucher.R.string.label_close))
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

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        /*if (merchantVoucherViewModelList.size == 0) {
            merchantVoucherListWidget?.setData(null)
            return
        }
        merchantVoucherListWidget?.setData(merchantVoucherViewModelList)
        promoContainer.visibility = View.VISIBLE*/

        /*adapter.clearAllElements()
        super.renderList(merchantVoucherViewModelList, false)*/

        merchantVoucherListBottomSheetAdapter.setViewModelList(merchantVoucherViewModelList)

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvVoucherList.layoutManager = linearLayoutManager
        rvVoucherList.adapter = merchantVoucherListBottomSheetAdapter
//        promoContainer.visibility = View.VISIBLE
        updateHeight()
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        /*adapter.errorNetworkModel = ErrorNetworkModel().apply {
            errorMessage = ErrorHandler.getErrorMessage(context, e)
            onRetryListener = ErrorNetworkModel.OnRetryListener {
                presenter.clearCache()
                presenter.getVoucherList(shopId)
            }
        }
        super.showGetListError(e)*/
    }

    private fun hideUseMerchantVoucherLoading() {
        if (loadingUseMerchantVoucher != null) {
            loadingUseMerchantVoucher!!.dismiss()
        }
    }

    /*override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        val intent = MerchantVoucherDetailActivity.createIntent(context!!, merchantVoucherViewModel.voucherId,
                merchantVoucherViewModel, shopId)
        startActivity(intent)
    }*/

    private fun showUseMerchantVoucherLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog?.setCancelable(false)
            progressDialog?.setMessage(getString(R.string.title_loading))
        }
        if (progressDialog!!.isShowing()) {
            progressDialog?.dismiss()
        }
        progressDialog?.show()
    }
}