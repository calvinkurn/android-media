package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.di.MerchantVoucherModule
import com.tokopedia.merchantvoucher.common.gql.data.request.CartItemDataVoucher
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherViewUsed
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListFragment
import com.tokopedia.promocheckout.common.data.entity.request.Promo
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCart
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.shop.common.di.ShopCommonModule
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
    private var merchantVoucherViewModelList: List<MerchantVoucherViewModel> = emptyList()

    private var promo: Promo? = null
    private var shopId: Int = 0
    private var cartString: String = ""
    private var source: String = ""
    private val CART: String = "cart"
    private var cartItemDataVoucherList: ArrayList<CartItemDataVoucher> = arrayListOf()

    var bottomsheetView: View? = null

    @Inject
    lateinit var presenter: MerchantVoucherListBottomsheetPresenter
    @Inject
    lateinit var cartPageAnalytics: CheckoutAnalyticsCart
    @Inject
    lateinit var shipmentPageAnalytics: CheckoutAnalyticsCourierSelection

    lateinit var actionListener: ActionListener

    interface ActionListener {
        fun onClashCheckPromo(clashingInfoDetailUiModel: ClashingInfoDetailUiModel, type: String)
        fun onSuccessCheckPromoMerchantFirstStep(promoData: ResponseGetPromoStackUiModel, promoCode: String)
    }

    companion object {
        val ARGUMENT_SHOP_ID = "ARGUMENT_SHOP_ID"
        val ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM = "ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM"
        val ARGUMENT_CART_STRING = "ARGUMENT_CART_STRING"
        val ARGUMENT_SOURCE = "ARGUMENT_SOURCE"
        val ARGUMENT_CART_ITEM_DATA = "ARGUMENT_CART_ITEM_DATA"

        @JvmStatic
        fun newInstance(shopId: Int, cartString: String, promo: Promo, source: String, cartItemDataVoucherList: ArrayList<CartItemDataVoucher>): MerchantVoucherListBottomSheetFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM, promo)
            bundle.putInt(ARGUMENT_SHOP_ID, shopId)
            bundle.putString(ARGUMENT_CART_STRING, cartString)
            bundle.putString(ARGUMENT_SOURCE, source)
            bundle.putParcelableArrayList(ARGUMENT_CART_ITEM_DATA, cartItemDataVoucherList)

            val fragment = MerchantVoucherListBottomSheetFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun state(): BottomSheetsState {
        return BottomSheetsState.FLEXIBLE
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
        progressDialog?.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))

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
                    textInputLayoutCoupon.error = getString(R.string.code_voucher_blank_warning)
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
                textInputLayoutCoupon.error = getString(R.string.code_voucher_blank_warning)
                if (source.equals(CART, true)) {
                    cartPageAnalytics.eventClickUseMerchantVoucherFailed(textInputLayoutCoupon.error?.toString(), "", false)
                } else {
                    shipmentPageAnalytics.eventClickUseMerchantVoucherError(textInputLayoutCoupon.error?.toString(), "", false)
                }
                updateHeight()
            } else {
                presenter.checkPromoFirstStep("", textInputCoupon.text.toString(), cartString, promo, false)
            }
        }
    }

    private fun getArgumentsValue() {
        shopId = arguments?.getInt(ARGUMENT_SHOP_ID, 0) ?: 0
        promo = arguments?.getParcelable(ARGUMENT_CHECK_PROMO_FIRST_STEP_PARAM)
        cartString = arguments?.getString(ARGUMENT_CART_STRING) ?: ""
        source = arguments?.getString(ARGUMENT_SOURCE) ?: ""
        cartItemDataVoucherList = arguments?.getParcelableArrayList(ARGUMENT_CART_ITEM_DATA)
                ?: arrayListOf()
    }

    fun loadData() {
        showProgressLoading()
        presenter.clearCache()
        presenter.getVoucherList(shopId.toString(), 0, cartItemDataVoucherList)
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
            progressDialog?.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
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
                val intent = MerchantVoucherDetailActivity.createIntent(it, voucherId,
                        this, shopId.toString())
                startActivityForResult(intent, MerchantVoucherListFragment.REQUEST_CODE_MERCHANT_DETAIL)

                sendClickDetailMerchantVoucher(merchantVoucherViewModel)
            }
        }
    }

    private fun sendClickDetailMerchantVoucher(merchantVoucherViewModel: MerchantVoucherViewModel) {
        val position = merchantVoucherViewModelList.indexOf(merchantVoucherViewModel)
        val ecommerceMap = createEcommerceMap(listOf(merchantVoucherViewModel), ConstantTransactionAnalytics.EventName.PROMO_CLICK, position)

        if (source.equals(CART, true))
            cartPageAnalytics.eventClickDetailMerchantVoucher(ecommerceMap, merchantVoucherViewModel.voucherId.toString(), merchantVoucherViewModel.voucherCode)
        else
            shipmentPageAnalytics.eventClickDetailMerchantVoucher(ecommerceMap, merchantVoucherViewModel.voucherId.toString(), merchantVoucherViewModel.voucherCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
        if (context == null) {
            return
        }

        presenter.checkPromoFirstStep(merchantVoucherViewModel.voucherId.toString(), merchantVoucherViewModel.voucherCode, cartString, promo, true)
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

    //impression
    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        this.merchantVoucherViewModelList = merchantVoucherViewModelList
        merchantVoucherListBottomSheetAdapter.setViewModelList(merchantVoucherViewModelList)
        sendMvcImpressionEventTracking(merchantVoucherViewModelList)

        val linearLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
        rvVoucherList.layoutManager = linearLayoutManager
        rvVoucherList.adapter = merchantVoucherListBottomSheetAdapter
        updateHeight()
    }

    private fun sendMvcImpressionEventTracking(merchantVoucherViewModelList: List<MerchantVoucherViewModel>) {
        if (merchantVoucherViewModelList.isEmpty()) return
        val ecommerceMap = createEcommerceMap(merchantVoucherViewModelList, ConstantTransactionAnalytics.EventName.PROMO_VIEW, 0)

        if (source.equals(CART, true))
            cartPageAnalytics.eventImpressionUseMerchantVoucher(merchantVoucherViewModelList[0].voucherId.toString(), ecommerceMap)
        else
            shipmentPageAnalytics.eventImpressionUseMerchantVoucher(merchantVoucherViewModelList[0].voucherId.toString(), ecommerceMap)
    }

    private fun createEcommerceMap(mvcList: List<MerchantVoucherViewModel>, eventType: String, startPosition: Int): Map<String, Any> {
        if (mvcList.isEmpty()) return mapOf()

        val isFromCart = source.equals(CART, true)
        val page = if (isFromCart) "Cart" else "Checkout"
        return mapOf<String, Any>(
                eventType to mapOf(
                        ConstantTransactionAnalytics.Key.PROMOTIONS to mvcList.mapIndexed { i, mvc ->
                            return@mapIndexed mapOf(
                                    ConstantTransactionAnalytics.Key.ID to shopId.toString(),
                                    ConstantTransactionAnalytics.Key.NAME to "$page - ${startPosition.plus(i).plus(1)} - ${mvc.voucherName}",
                                    ConstantTransactionAnalytics.Key.CREATIVE to "",
                                    ConstantTransactionAnalytics.Key.POSITION to startPosition.plus(i).plus(1),
                                    ConstantTransactionAnalytics.Key.PROMO_ID_ to mvc.voucherId.toString(),
                                    ConstantTransactionAnalytics.Key.PROMO_CODE to mvc.voucherCode
                            )
                        }
                )
        )
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        var message = ErrorHandler.getErrorMessage(context, e)
        if (TextUtils.isEmpty(message)) {
            message = getString(R.string.general_warning)
        }
        NetworkErrorHelper.showEmptyState(activity, errorContainer, message) {
            errorContainer.visibility = View.GONE
            loadData()
        }
    }

    override fun onErrorCheckPromoFirstStep(message: String, promoId: String, isFromList: Boolean) {
        hideKeyboard()
        var messageInfo = message
        if (TextUtils.isEmpty(messageInfo)) {
            messageInfo = getString(R.string.general_warning)
        }
        textInputLayoutCoupon.error = messageInfo
        updateHeight()
        if (source.equals(CART, true)) {
            cartPageAnalytics.eventClickUseMerchantVoucherFailed(messageInfo, promoId, isFromList)
        } else {
            shipmentPageAnalytics.eventClickUseMerchantVoucherError(messageInfo, promoId, isFromList)
        }
    }

    //on success use merchant voucher
    override fun onSuccessCheckPromoFirstStep(model: ResponseGetPromoStackUiModel, promoCode: String, isFromList: Boolean, promoId: String) {

        if (source.equals(CART, true)) {
            cartPageAnalytics.eventClickUseMerchantVoucherSuccess(promoCode, promoId, isFromList)
        } else {
            shipmentPageAnalytics.eventClickUseMerchantVoucherSuccess(promoCode, promoId, isFromList)
        }

        hideKeyboard()
        dismiss()
        actionListener.onSuccessCheckPromoMerchantFirstStep(model, promoCode)
    }

    override fun onClashCheckPromoFirstStep(model: ClashingInfoDetailUiModel, type: String) {
        hideKeyboard()
        dismiss()
        actionListener.onClashCheckPromo(model, type)
    }

    override fun configView(parentView: View?) {
        super.configView(parentView)
        parentView?.findViewById<View>(com.tokopedia.design.R.id.layout_title)?.setOnClickListener(null)
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE);
        (inputMethodManager as InputMethodManager?)?.hideSoftInputFromWindow(view?.windowToken, 0);
    }

}
