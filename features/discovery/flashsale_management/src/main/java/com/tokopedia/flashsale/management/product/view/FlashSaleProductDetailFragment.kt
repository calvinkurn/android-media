package com.tokopedia.flashsale.management.product.view

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.KMNumbers
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleProductStatusTypeDef
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.product.data.FlashSaleProductItem
import com.tokopedia.flashsale.management.product.view.presenter.FlashSaleProductDetailPresenter
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_flash_sale_product_detail.*
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class FlashSaleProductDetailFragment : BaseDaggerFragment() {

    var progressDialog: ProgressDialog? = null
    var canEdit: Boolean = false
    var currencyTextWatcher: CurrencyTextWatcher? = null

    @Inject
    lateinit var presenter: FlashSaleProductDetailPresenter

    lateinit var onFlashSaleProductDetailFragmentListener: OnFlashSaleProductDetailFragmentListener

    interface OnFlashSaleProductDetailFragmentListener {
        fun getProduct(): FlashSaleProductItem
    }

    companion object {
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_CAN_EDIT = "can_edit"
        @JvmStatic
        fun createInstance(campaignId: Int, canEdit: Boolean): Fragment {
            return FlashSaleProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                    putBoolean(EXTRA_CAN_EDIT, canEdit)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { GraphqlClient.init(it) }
        //TODO just for test
//        canEdit = arguments!!.getBoolean(EXTRA_CAN_EDIT)
        canEdit = true
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flash_sale_product_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        flashSaleProductWidget.setData(flashSaleProductItem)

        tvCategoryText.text = flashSaleProductItem.getDepartmentNameString()

        renderPrice(flashSaleProductItem)
        renderDiscount(flashSaleProductItem)
        renderStock(flashSaleProductItem)

        if (canEdit) {
            context?.run {
                btnRequestProduct.text = flashSaleProductItem.campaign.getProductStatusActionString(this)
                btnRequestProduct.setOnClickListener {
                    onBtnRequestProductClicked()
                }
            }

            btnContainer.visibility = View.VISIBLE
        } else {
            btnContainer.visibility = View.GONE
        }
    }

    private fun renderPrice(flashSaleProductItem: FlashSaleProductItem){
        currencyTextWatcher = CurrencyTextWatcher(etPrice, CurrencyEnum.RPwithSpace)
        etPrice.addTextChangedListener(currencyTextWatcher)
        if (flashSaleProductItem.campaign.discountedPrice > 0) {
            etPrice.setText(flashSaleProductItem.campaign.discountedPrice.toString())
        } else {
            etPrice.setText("0")
        }
        if (flashSaleProductItem.campaign.criteria.priceMin > 0) {
            if (flashSaleProductItem.campaign.criteria.priceMax <= 0) {
                tilPrice.setHelper(context!!.getString(R.string.price_criteria_above_x,
                        KMNumbers.formatRupiahString(flashSaleProductItem.campaign.criteria.priceMin.toLong())))
            } else {
                tilPrice.setHelper(context!!.getString(R.string.price_criteria_between_x_and_x,
                        KMNumbers.formatRupiahString(flashSaleProductItem.campaign.criteria.priceMin.toLong()),
                        KMNumbers.formatRupiahString(flashSaleProductItem.campaign.criteria.priceMax.toLong())))
            }
        } else {
            if (flashSaleProductItem.campaign.criteria.priceMax <= 0) {
                tilPrice.setHelper(null)
            } else {
                tilPrice.setHelper(context!!.getString(R.string.price_criteria_below_x,
                        KMNumbers.formatRupiahString(flashSaleProductItem.campaign.criteria.priceMax.toLong())))
            }
        }

        if (canEdit) {
            etPrice.isEnabled = true
        } else {
            etPrice.background = null
            etPrice.isEnabled = false
        }
    }

    private fun renderDiscount(flashSaleProductItem: FlashSaleProductItem){
        etDiscount.background = null
    }

    private fun renderStock(flashSaleProductItem: FlashSaleProductItem){

    }

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    fun onBtnRequestProductClicked() {
        //TODO
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        when (flashSaleProductItem.campaign.productStatus) {
            FlashSaleProductStatusTypeDef.NOTHING -> getString(R.string.flash_sale_reserve_product)
            FlashSaleProductStatusTypeDef.SUBMITTED -> getString(R.string.flash_sale_cancel_reserve)
            FlashSaleProductStatusTypeDef.REJECTED -> getString(R.string.flash_sale_resubmit_product)
            FlashSaleProductStatusTypeDef.RESERVE -> getString(R.string.flash_sale_cancel_reserve)
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL -> getString(R.string.flash_sale_reserve_product)
            FlashSaleProductStatusTypeDef.SUBMIT_CANCEL_SUBMIT -> getString(R.string.flash_sale_reserve_product)
            else -> getString(R.string.flash_sale_resubmit_product)
        }
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
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

//    private fun loadFlashSaleProductDetail() {
////        if (merchantVoucherViewModel == null) {
//        showLoading()
//        presenter.getFlashSaleDetail(
//                onSuccess = {
//                    onSuccessGetFlashSaleProductDetail()
//                },
//                onError = {
//                    hideLoading()
//                    NetworkErrorHelper.showEmptyState(context, view,
//                            ErrorHandler.getErrorMessage(context, it)) { loadFlashSaleProductDetail() }
//                }
//        )
////        } else {
////            onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel!!)
////        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when (requestCode) {
//            MerchantVoucherListFragment.REQUEST_CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
//                // no op
//            }
//            else ->
        super.onActivityResult(requestCode, resultCode, data)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onFlashSaleProductDetailFragmentListener = context as OnFlashSaleProductDetailFragmentListener
    }

}
