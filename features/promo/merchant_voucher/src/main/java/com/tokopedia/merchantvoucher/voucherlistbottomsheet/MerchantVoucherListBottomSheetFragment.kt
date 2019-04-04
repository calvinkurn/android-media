package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.merchantvoucher.FileUtils
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherModule
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherViewUsed
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.ResponseGetPromoStackFirst
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.transactionanalytics.CheckoutAnalyticsCart
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/03/19.
 */

open class MerchantVoucherListBottomSheetFragment : BottomSheets(), MerchantVoucherListBottomsheetContract.View, MerchantVoucherViewUsed.OnMerchantVoucherViewListener {

    @Suppress("DEPRECATION")
    private var progressDialog: ProgressDialog? = null
    private lateinit var merchantVoucherListBottomSheetAdapter: MerchantVoucherListBottomSheetAdapter

    private lateinit var rvVoucherList: RecyclerView
    private lateinit var buttonUse: TextView
    private lateinit var textInputCoupon: EditText
    private lateinit var layoutMerchantVoucher: CoordinatorLayout
    private lateinit var merchantVoucherContainer: LinearLayout
    private lateinit var errorContainer: LinearLayout
    private lateinit var pbLoading: ProgressBar
    private lateinit var textInputLayoutCoupon: TkpdHintTextInputLayout

    private var promo: Promo? = null
    private var shopId: Int = 0
    private var cartString: String = ""

    var bottomsheetView: View? = null

    @Inject
    lateinit var presenter: MerchantVoucherListBottomsheetPresenter
    @Inject
    lateinit var cartPageAnalytics: CheckoutAnalyticsCart

    lateinit var actionListener: ActionListener


    interface ActionListener {
        fun onClashCheckPromo(clashingInfoDetailUiModel: ClashingInfoDetailUiModel)
        fun onSuccessCheckPromoFirstStep(promoData: ResponseGetPromoStackUiModel)
    }

    companion object {
        val ARGUMENT_SHOP_ID = "ARGUMENT_SHOP_ID"
        val ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM = "ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM"
        val ARGUMENT_CART_STRING = "ARGUMENT_CART_STRING"

        @JvmStatic
        fun newInstance(shopId: Int, cartString: String, promo: Promo): MerchantVoucherListBottomSheetFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM, promo)
            bundle.putInt(ARGUMENT_SHOP_ID, shopId)
            bundle.putString(ARGUMENT_CART_STRING, cartString)

            val fragment = MerchantVoucherListBottomSheetFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun initView(view: View) {
        bottomsheetView = view

        getArgumentsValue()

        merchantVoucherListBottomSheetAdapter = MerchantVoucherListBottomSheetAdapter(this)
        if (activity != null) {
            initInjector()
        }

        @Suppress("DEPRECATION")
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage(getString(R.string.title_loading))

        layoutMerchantVoucher = view.findViewById(R.id.layout_merchant_voucher)
        rvVoucherList = view.findViewById(R.id.rvVoucherList)
        buttonUse = view.findViewById(R.id.buttonUse)
        textInputCoupon = view.findViewById(R.id.textInputCoupon)
        merchantVoucherContainer = view.findViewById(R.id.merchant_voucher_container)
        errorContainer = view.findViewById(R.id.error_container)
        pbLoading = view.findViewById(R.id.pb_loading)
        textInputLayoutCoupon = view.findViewById(R.id.textInputLayoutCoupon)

        textInputCoupon.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.isEmpty()) {
                    textInputLayoutCoupon.error = "Kode Voucher Harus Diisi"
                    updateHeight()
                } else {
                    textInputLayoutCoupon.setErrorEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        loadData()

        buttonUse.setOnClickListener {
            hideKeyboard()
            if (textInputCoupon.text.toString().isEmpty()) {
                textInputLayoutCoupon.error = "Kode Voucher Harus Diisi"
                cartPageAnalytics.eventClickPakaiMerchantVoucherManualInputFailed(textInputLayoutCoupon.error?.toString())
                updateHeight()
            } else {
                presenter.checkPromoFirstStep(textInputCoupon.text.toString(), cartString, promo, false)
            }
        }
    }

    private fun getArgumentsValue() {
        shopId = arguments?.getInt(ARGUMENT_SHOP_ID, 0) ?: 0
        promo = arguments?.getParcelable(ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM)
        cartString = arguments?.getString(ARGUMENT_CART_STRING) ?: ""
    }

    fun loadData() {
        showProgressLoading()
        presenter.clearCache()
        presenter.getVoucherList(shopId.toString(), 0)
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottomsheet_merchant_voucher
    }

    override fun title(): String {
        return getString(R.string.merchant_bottomsheet_title)
    }

    override fun showLoadingDialog() {
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

    override fun hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun showProgressLoading() {
        merchantVoucherContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideProgressLoading() {
        errorContainer.visibility = View.GONE
        pbLoading.visibility = View.GONE
        merchantVoucherContainer.visibility = View.VISIBLE
    }

    override fun isOwner(): Boolean {
        return false
    }

    override fun onMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        context?.let {
            merchantVoucherViewModel.run {
                this.status = MerchantVoucherStatusTypeDef.TYPE_RUN_OUT
                val intent = MerchantVoucherDetailActivity.createIntent(it, voucherId,
                        this, shopId.toString())
                startActivityForResult(intent, MerchantVoucherListFragment.REQUEST_CODE_MERCHANT_DETAIL)
                cartPageAnalytics.eventClickDetailMerchantVoucher(merchantVoucherViewModel.voucherCode)
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

        presenter.checkPromoFirstStep(merchantVoucherViewModel.voucherCode, cartString, promo, true)
    }

    fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .merchantVoucherModule(MerchantVoucherModule())
                    .build()
                    .inject(this@MerchantVoucherListBottomSheetFragment)
            presenter.attachView(this@MerchantVoucherListBottomSheetFragment)
        }
    }

    override fun getActivityContext(): Context? {
        return context
    }

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        merchantVoucherListBottomSheetAdapter.setViewModelList(merchantVoucherViewModelList)

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvVoucherList.layoutManager = linearLayoutManager
        rvVoucherList.adapter = merchantVoucherListBottomSheetAdapter
        updateHeight()
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        var message = ErrorHandler.getErrorMessage(context, e)
        if (TextUtils.isEmpty(message)) {
            message = "Terjadi kesalahan. Ulangi beberapa saat lagi"
        }
        NetworkErrorHelper.showEmptyState(activity, errorContainer, message) {
            errorContainer.visibility = View.GONE
            loadData()
        }

        /*dismiss()
        showClashingDummy()*/
    }

    override fun onErrorCheckPromoFirstStep(message: String, isFromList: Boolean) {
        hideKeyboard()
        var messageInfo = message
        if (TextUtils.isEmpty(messageInfo)) {
            messageInfo = "Terjadi kesalahan. Ulangi beberapa saat lagi."
        }
        if (isFromList) {
            ToasterError.make(layoutMerchantVoucher, messageInfo, ToasterError.LENGTH_SHORT).show()
            cartPageAnalytics.eventClickPakaiMerchantVoucherFailed(message)
        } else {
            textInputLayoutCoupon.error = message
            updateHeight()
            cartPageAnalytics.eventClickPakaiMerchantVoucherManualInputFailed(message)
        }
    }

    override fun onSuccessCheckPromoFirstStep(model: ResponseGetPromoStackUiModel, promoCode: String, isFromList: Boolean) {
        if (isFromList) {
            cartPageAnalytics.eventClickPakaiMerchantVoucherManualInputSuccess(promoCode)
        } else {
            cartPageAnalytics.eventClickPakaiMerchantVoucherSuccess(promoCode)
        }
        hideKeyboard()
        dismiss()
        actionListener.onSuccessCheckPromoFirstStep(model)
    }

    override fun onClashCheckPromoFirstStep(model: ClashingInfoDetailUiModel) {
        hideKeyboard()
        dismiss()
        actionListener.onClashCheckPromo(model)
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE);
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0);
    }

    // Todo : remove this
    private fun showClashingDummy() {
        val mapper = CheckPromoStackingCodeMapper()
        val gson = Gson()
        val responseGetPromoStackFirst = gson.fromJson(FileUtils().readRawTextFile(activity, R.raw.dummy_clashing_response), ResponseGetPromoStackFirst::class.java)
        val uiData = mapper.callDummy(responseGetPromoStackFirst)
        actionListener.onClashCheckPromo(uiData.data.clashings)
    }

}