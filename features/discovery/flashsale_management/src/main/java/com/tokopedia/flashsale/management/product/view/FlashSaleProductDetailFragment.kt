package com.tokopedia.flashsale.management.product.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.product.view.presenter.FlashSaleProductDetailPresenter
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_flash_sale_product_detail.*
import kotlinx.android.synthetic.main.partial_flash_sale_product_detail_loading.*
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class FlashSaleProductDetailFragment : BaseDaggerFragment() {

    var progressDialog: ProgressDialog? = null

    @Inject
    lateinit var presenter: FlashSaleProductDetailPresenter

    companion object {

        @JvmStatic
        fun createInstance(): Fragment {
            return FlashSaleProductDetailFragment().apply {
                arguments = Bundle()
//                        .apply {
//                    putInt(EXTRA_VOUCHER_ID, voucherId)
//                    putParcelable(EXTRA_VOUCHER, merchantVoucherViewModel)
//                    putString(EXTRA_SHOP_ID, shopId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { GraphqlClient.init(it) }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flash_sale_product_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFlashSaleProductDetail()
        btnRequestProduct.setOnClickListener {
            onBtnRequestProductClicked()
        }
    }

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    fun onBtnRequestProductClicked(){
        //TODO
    }

    fun onSuccessGetFlashSaleProductDetail(string: String) {
        hideLoading()
        btnContainer.visibility = View.VISIBLE
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage(getString(R.string.title_loading))
        }
        if (progressDialog!!.isShowing()) {
            progressDialog!!.dismiss()
        }
        progressDialog!!.show()
    }

    private fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    private fun loadFlashSaleProductDetail() {
//        if (merchantVoucherViewModel == null) {
        showLoading()
        presenter.getFlashSaleDetail(
                onSuccess = {
                    onSuccessGetFlashSaleProductDetail(it)
                },
                onError = {
                    hideLoading()
                    NetworkErrorHelper.showEmptyState(context, view,
                            ErrorHandler.getErrorMessage(context, it)) { loadFlashSaleProductDetail() }
                }
        )
//        } else {
//            onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel!!)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when (requestCode) {
//            MerchantVoucherListFragment.REQUEST_CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
//                // no op
//            }
//            else ->
        super.onActivityResult(requestCode, resultCode, data)
//        }
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
        vgContent.visibility = View.GONE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
        vgContent.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
