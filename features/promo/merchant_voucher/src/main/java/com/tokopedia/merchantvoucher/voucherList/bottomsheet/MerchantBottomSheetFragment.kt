package com.tokopedia.merchantvoucher.voucherList.bottomsheet

import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.gql.data.MessageTitleErrorException
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherViewUsed
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListPresenter
import com.tokopedia.merchantvoucher.voucherList.presenter.MerchantVoucherListView
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo
import com.tokopedia.shop.common.di.ShopCommonModule
import kotlinx.android.synthetic.main.bottomsheet_merchant_voucher.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/03/19.
 */

open class MerchantBottomSheetFragment : BottomSheetDialogFragment(), MerchantVoucherListView, MerchantVoucherViewUsed.OnMerchantVoucherViewListener {
    override fun isOwner(): Boolean {
        return true
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        if (context == null) {
            return
        }
        // merchantVoucherTracking?.clickUseVoucherFromList()
        //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
        /*if (presenter.isLogin() == false) {
            val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN)
        } else if (!presenter.isMyShop(voucherShopId)) {
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

    lateinit var voucherShopId: String
    private var mTitle: String? = null
    @Suppress("DEPRECATION")
    private var loadingUseMerchantVoucher: ProgressDialog? = null
    private var progressDialog: ProgressDialog? = null
    private lateinit var merchantBottomSheetAdapter: MerchantBottomSheetAdapter

    @Inject
    lateinit var presenter: MerchantVoucherListPresenter

    companion object {
        @JvmStatic
        fun newInstance(shopId: String): MerchantBottomSheetFragment {
            return MerchantBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MerchantVoucherListFragment.EXTRA_SHOP_ID, shopId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        voucherShopId = arguments!!.getString(MerchantVoucherListActivity.SHOP_ID)
        merchantBottomSheetAdapter = MerchantBottomSheetAdapter(this)
        if (activity != null) {
            initInjector()
            mTitle = activity!!.getString(R.string.merchant_bottomsheet_title)
        }
    }

    fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .build()
                    .inject(this@MerchantBottomSheetFragment)
            presenter.attachView(this@MerchantBottomSheetFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.clearCache()
        presenter.getVoucherList(voucherShopId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.widget_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val container = view.findViewById<FrameLayout>(R.id.bottomsheet_container)
        View.inflate(context, R.layout.bottomsheet_merchant_voucher, container)

        val textViewTitle = view.findViewById<TextView>(com.tokopedia.design.R.id.tv_title)
        textViewTitle.text = mTitle

        @Suppress("DEPRECATION")
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(getString(R.string.title_loading))

        // merchantVoucherListWidget = view.findViewById(R.id.merchantVoucherListWidget)

        val layoutTitle = view.findViewById<View>(com.tokopedia.design.R.id.layout_title)
        layoutTitle.setOnClickListener {
            dismiss()
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

        merchantBottomSheetAdapter.setViewModelList(merchantVoucherViewModelList)

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvVoucherList.layoutManager = linearLayoutManager
        rvVoucherList.adapter = merchantBottomSheetAdapter
        promoContainer.visibility = View.VISIBLE
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        /*adapter.errorNetworkModel = ErrorNetworkModel().apply {
            errorMessage = ErrorHandler.getErrorMessage(context, e)
            onRetryListener = ErrorNetworkModel.OnRetryListener {
                presenter.clearCache()
                presenter.getVoucherList(voucherShopId)
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
                merchantVoucherViewModel, voucherShopId)
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