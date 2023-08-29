package com.tokopedia.merchantvoucher.voucherDetail

import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.Dialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.analytic.MerchantVoucherTracking
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.gql.data.UseMerchantVoucherQueryResult
import com.tokopedia.merchantvoucher.common.model.*
import com.tokopedia.merchantvoucher.voucherDetail.presenter.MerchantVoucherDetailPresenter
import com.tokopedia.merchantvoucher.voucherDetail.presenter.MerchantVoucherDetailView
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class MerchantVoucherDetailFragment : BaseDaggerFragment(),
        MerchantVoucherDetailView {

    var voucherId: Int = 0
    var merchantVoucherViewModel: MerchantVoucherViewModel? = null
    var voucherShopId: String? = null

    var loadingUseMerchantVoucher: ProgressDialog? = null

    private var btnUseVoucher: Button? = null
    private var tvSeeCart: TextView? = null
    private var btnContainer: LinearLayout? = null
    private var vgVoucherStatus: LinearLayout? = null
    private var vgContent: LinearLayout? = null
    private var loadingView: LinearLayout? = null
    private var ivVoucherBanner: ImageView? = null
    private var tvVoucherTitle: TextView? = null
    private var tvMinTransaction: TextView? = null
    private var tvMinTransactionLabel: TextView? = null
    private var tvValidThru: TextView? = null
    private var tvVoucherStatus: TextView? = null
    private var tvTncLabel: TextView? = null
    private var webViewTnc: WebView? = null

    @Inject
    lateinit var presenter: MerchantVoucherDetailPresenter

    var merchantVoucherTracking: MerchantVoucherTracking? = null

    companion object {
        const val EXTRA_VOUCHER_ID = "voucher_id"
        const val EXTRA_VOUCHER = "voucher"
        const val EXTRA_SHOP_ID = "shop_id"

        @JvmStatic
        fun createInstance(voucherId: Int, merchantVoucherViewModel: MerchantVoucherViewModel?,
                           shopId: String? = null): Fragment {
            return MerchantVoucherDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_VOUCHER_ID, voucherId)
                    putParcelable(EXTRA_VOUCHER, merchantVoucherViewModel)
                    putString(EXTRA_SHOP_ID, shopId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherId = arguments!!.getInt(EXTRA_VOUCHER_ID)
        merchantVoucherViewModel = arguments!!.getParcelable(EXTRA_VOUCHER)
        voucherShopId = arguments!!.getString(EXTRA_SHOP_ID)
        super.onCreate(savedInstanceState)
        activity?.run {
            merchantVoucherTracking = MerchantVoucherTracking()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_merchant_voucher_detail, container, false)
        btnUseVoucher = view.findViewById(R.id.btnUseVoucher)
        tvSeeCart = view.findViewById(R.id.tvSeeCart)
        btnContainer = view.findViewById(R.id.btnContainer)
        vgVoucherStatus = view.findViewById(R.id.vgVoucherStatus)
        vgContent = view.findViewById(R.id.vgContent)
        ivVoucherBanner = view.findViewById(R.id.ivVoucherBanner)
        tvVoucherTitle = view.findViewById(R.id.tvVoucherTitle)
        tvMinTransactionLabel = view.findViewById(R.id.tvMinTransactionLabel)
        tvMinTransaction = view.findViewById(R.id.tvMinTransaction)
        tvValidThru = view.findViewById(R.id.tvValidThru)
        tvVoucherStatus = view.findViewById(R.id.tvVoucherStatus)
        tvTncLabel = view.findViewById(R.id.tvTncLabel)
        webViewTnc = view.findViewById(R.id.webViewTnc)
        loadingView = view.findViewById(R.id.loadingView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVoucherDetail()
        tvSeeCart?.setOnClickListener {
            if (RouteManager.isSupportApplink(context!!, ApplinkConst.CART)) {
                val intent = RouteManager.getIntent(context!!, ApplinkConst.CART)
                intent?.run {
                    startActivity(intent)
                }
            }
        }
        btnUseVoucher?.setOnClickListener {
            merchantVoucherTracking?.clickUseVoucherFromDetail(voucherId.toString())

            //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
            /*if (!presenter.isLogin()) {
                if (RouteManager.isSupportApplink(context!!, ApplinkConst.LOGIN)) {
                    val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
                    startActivityForResult(intent, MerchantVoucherListFragment.REQUEST_CODE_LOGIN)
                }
            } else if (!presenter.isMyShop(voucherShopId)) {
                showUseMerchantVoucherLoading()
                merchantVoucherViewModel?.let {
                    presenter.useMerchantVoucher(it.voucherCode, voucherId)
                }
            }*/
            //TOGGLE_MVC_OFF
            activity?.run {
                copyVoucherCodeToClipboard()
                val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.title_voucher_code_copied),
                        Snackbar.LENGTH_LONG)
                snackbar.setAction(activity!!.getString(com.tokopedia.design.R.string.close)) { snackbar.dismiss() }
                snackbar.setActionTextColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
                snackbar.show()
            }
        }
        if (merchantVoucherViewModel?.isPublic == false) {
            btnContainer?.show()
            btnUseVoucher?.show()
        } else {
            btnUseVoucher?.hide()
        }
    }

    private fun copyVoucherCodeToClipboard() {
        val voucherCode = merchantVoucherViewModel?.voucherCode
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(voucherCode, voucherCode)
        clipboard.setPrimaryClip(clip)
    }

    override fun onSuccessUseVoucher(useMerchantVoucherQueryResult: UseMerchantVoucherQueryResult) {
        hideUseMerchantVoucherLoading()
        merchantVoucherViewModel?.run {
            status = MerchantVoucherStatusTypeDef.TYPE_IN_USE
            onSuccessGetMerchantVoucherDetail(this)
        }
        activity?.let { it ->
            Dialog(it, Dialog.Type.PROMINANCE).apply {
                setTitle(useMerchantVoucherQueryResult.errorMessageTitle)
                setDesc(useMerchantVoucherQueryResult.errorMessage)
                setBtnOk(getString(com.tokopedia.design.R.string.label_close))
                setOnOkClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    override fun onErrorUseVoucher(e: Throwable) {
        hideUseMerchantVoucherLoading()
        activity?.run {
            Toaster.make(findViewById(android.R.id.content), ErrorHandler.getErrorMessage(this, e),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.retry), View.OnClickListener {
                merchantVoucherViewModel?.let {
                    showUseMerchantVoucherLoading()
                    presenter.useMerchantVoucher(it.voucherCode, voucherId)
                }
            })
        }
    }

    override fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .build()
                    .inject(this@MerchantVoucherDetailFragment)
            presenter.attachView(this@MerchantVoucherDetailFragment)
        }
    }

    override fun onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel: MerchantVoucherViewModel) {
        hideLoading()
        if (merchantVoucherViewModel.bannerUrl.isNullOrEmpty()) {
            ivVoucherBanner?.visibility = View.GONE
        } else {
            ImageHandler.loadImageAndCache(ivVoucherBanner, merchantVoucherViewModel.bannerUrl)
            ivVoucherBanner?.visibility = View.VISIBLE
        }
        val voucherString = merchantVoucherViewModel.getTypeString(context!!) + " " +
                merchantVoucherViewModel.getAmountString()
        tvVoucherTitle?.text = voucherString
        if (merchantVoucherViewModel.minimumSpend <= 0) {
            tvMinTransaction?.visibility = View.GONE
            tvMinTransactionLabel?.text = getString(R.string.no_min_spend)
        } else {
            tvMinTransaction?.visibility = View.VISIBLE
            tvMinTransactionLabel?.text = getString(R.string.min_transaction_colon)
            tvMinTransaction?.text = merchantVoucherViewModel.getMinSpendAmountString()
        }
        tvValidThru?.text = merchantVoucherViewModel.getValidThruString()

        if (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE &&
                !presenter.isMyShop(voucherShopId)) {
            vgVoucherStatus?.visibility = View.GONE
            btnContainer?.visibility = View.VISIBLE
        } else if (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_IN_USE) {
            //TOGGLE_MVC_ON use voucher is not ready, so we use copy instead. Keep below code for future release
            /*vgVoucherStatus?.visibility = View.VISIBLE
            tvSeeCart.visibility = View.VISIBLE
            tvVoucherStatus.text = merchantVoucherViewModel.getStatusString(context!!)
            btnContainer.visibility = View.GONE*/

            //TOGGLE_MVC_OFF
            vgVoucherStatus?.visibility = View.GONE
            btnContainer?.visibility = View.VISIBLE
        } else {
            vgVoucherStatus?.visibility = View.VISIBLE
            tvSeeCart?.visibility = View.GONE
            tvVoucherStatus?.text = merchantVoucherViewModel.getStatusString(context!!)
            btnContainer?.visibility = View.GONE
        }
        if (merchantVoucherViewModel.tnc.isNullOrEmpty()) {
            tvTncLabel?.visibility = View.GONE
            webViewTnc?.visibility = View.GONE
        } else {
            tvTncLabel?.visibility = View.VISIBLE
            webViewTnc?.visibility = View.VISIBLE
            webViewTnc?.loadData(processWebViewHtmlStyle(merchantVoucherViewModel.tnc!!), "text/html; charset=utf-8", "UTF-8")
        }
    }

    fun processWebViewHtmlStyle(html_string: String): String {
        return getString(R.string.html_process_web_view, html_string)
    }

    private fun showUseMerchantVoucherLoading() {
        if (loadingUseMerchantVoucher == null) {
            loadingUseMerchantVoucher = ProgressDialog(activity)
            loadingUseMerchantVoucher!!.setCancelable(false)
            loadingUseMerchantVoucher!!.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
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

    override fun onErrorGetMerchantVoucherDetail(e: Throwable) {
        hideLoading()
        NetworkErrorHelper.showEmptyState(context, view,
                ErrorHandler.getErrorMessage(context, e)) { loadVoucherDetail() }
    }

    override fun getScreenName(): String {
        return ""
    }

    private fun loadVoucherDetail() {
        if (merchantVoucherViewModel == null) {
            showLoading()
            presenter.getVoucherDetail(voucherId)
        } else {
            onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            MerchantVoucherListFragment.REQUEST_CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                // no op
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showLoading() {
        loadingView?.visibility = View.VISIBLE
        vgContent?.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingView?.visibility = View.GONE
        vgContent?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
