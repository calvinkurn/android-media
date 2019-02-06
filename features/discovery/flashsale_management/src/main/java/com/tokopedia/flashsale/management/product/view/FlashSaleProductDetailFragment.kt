package com.tokopedia.flashsale.management.product.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.KMNumbers
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.product.data.*
import com.tokopedia.flashsale.management.product.view.presenter.FlashSaleProductDetailPresenter
import com.tokopedia.graphql.data.GraphqlClient
import kotlinx.android.synthetic.main.fragment_flash_sale_product_detail.*
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class FlashSaleProductDetailFragment : BaseDaggerFragment() {

    var progressDialog: ProgressDialog? = null
    var canSubmit: Boolean = false
    var canEdit: Boolean = false
    var campaignId: Int = 0
    var currencyTextWatcher: CurrencyTextWatcher? = null
    val stockTextWatcher: AfterTextWatcher by lazy {
        object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                //modify 0123 to 123, remove first 0
                etStock.removeTextChangedListener(stockTextWatcher)
                var stockStr = etStock.text.toString()
                var isEdit = false
                if (stockStr.isNotEmpty() && stockStr.startsWith(ZERO)) {
                    stockStr = stockStr.replaceFirst(ZERO, "")
                    isEdit = true
                }
                if (stockStr.isEmpty()) {
                    stockStr = ZERO
                    isEdit = true
                }
                if (isEdit) {
                    etStock.setText(stockStr)
                    etStock.setSelection(etStock.text.length)
                }
                etStock.addTextChangedListener(stockTextWatcher)
                isStockValid()
            }
        }
    }

    @Inject
    lateinit var presenter: FlashSaleProductDetailPresenter

    lateinit var onFlashSaleProductDetailFragmentListener: OnFlashSaleProductDetailFragmentListener

    interface OnFlashSaleProductDetailFragmentListener {
        fun getProduct(): FlashSaleProductItem
    }

    companion object {
        private const val EXTRA_PARAM_CAMPAIGN_ID = "campaign_id"
        private const val EXTRA_CAN_SUBMIT = "can_edit"
        private const val ZERO = "0"

        const val RESULT_IS_CATEGORY_FULL = "is_category_full"

        @JvmStatic
        fun createInstance(campaignId: Int, canSubmit: Boolean): Fragment {
            return FlashSaleProductDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_PARAM_CAMPAIGN_ID, campaignId)
                    putBoolean(EXTRA_CAN_SUBMIT, canSubmit)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { GraphqlClient.init(it) }
        canSubmit = arguments!!.getBoolean(EXTRA_CAN_SUBMIT)
        campaignId = arguments!!.getInt(EXTRA_PARAM_CAMPAIGN_ID)
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flash_sale_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        flashSaleProductWidget.setData(flashSaleProductItem)

        tvCategoryText.text = flashSaleProductItem.getDepartmentNameString()

        canEdit = canSubmit && flashSaleProductItem is FlashSaleSubmissionProductItem &&
                flashSaleProductItem.campaign.getProductStatusAction() == FlashSaleProductActionTypeDef.RESERVE

        renderPrice(savedInstanceState)
        renderDiscount(savedInstanceState)
        renderStock(savedInstanceState)

        if (flashSaleProductItem is FlashSaleSubmissionProductItem &&
                canSubmit &&
                flashSaleProductItem.campaign.getProductStatusAction() != FlashSaleProductActionTypeDef.NO_ACTION) {
            context?.run {
                btnRequestProduct.text = flashSaleProductItem.campaign.getProductStatusActionString(this)
                btnRequestProduct.setOnClickListener {
                    onBtnRequestProductClicked()
                }
            }
            btnRequestProduct.isEnabled = true
        } else {
            btnRequestProduct.isEnabled = false
        }

        if (flashSaleProductItem is FlashSaleSubmissionProductItem) {
            btnContainer.visibility = View.VISIBLE
        } else {
            btnContainer.visibility = View.GONE
        }
    }

    private fun renderPrice(savedInstanceState: Bundle?) {
        currencyTextWatcher = CurrencyTextWatcher(etPrice, CurrencyEnum.RPwithSpace)
        etPrice.addTextChangedListener(currencyTextWatcher)

        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        if (savedInstanceState == null) {
            if (flashSaleProductItem.getDiscountedPrice() > 0) {
                etPrice.setText(flashSaleProductItem.getDiscountedPrice().toString())
            } else {
                etPrice.setText("0")
            }
        }

        if (flashSaleProductItem is FlashSaleSubmissionProductItem) {
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
        } else {
            tilPrice.setHelper(null)
        }
        etPrice.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                isPriceValid()
                calculateDiscount()
                isDiscountValid()
            }
        })

        if (canEdit) {
            etPrice.isEnabled = true
        } else {
            etPrice.background = null
            etPrice.isEnabled = false
        }
    }

    private fun renderDiscount(savedInstanceState: Bundle?) {
        etDiscount.background = null
        if (savedInstanceState == null) {
            calculateDiscount()
        }
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        if (flashSaleProductItem is FlashSaleSubmissionProductItem) {
            if (flashSaleProductItem.campaign.criteria.discountPercentageMin > 0) {
                if (flashSaleProductItem.campaign.criteria.discountPercentageMax <= 0) {
                    tilDiscount.setHelper(context!!.getString(R.string.price_discount_above_x,
                            Math.round(flashSaleProductItem.campaign.criteria.discountPercentageMin.toDouble()).toString()))
                } else {
                    tilDiscount.setHelper(context!!.getString(R.string.price_discount_between_x_and_x,
                            Math.round(flashSaleProductItem.campaign.criteria.discountPercentageMin.toDouble()).toString(),
                            Math.round(flashSaleProductItem.campaign.criteria.discountPercentageMax.toDouble()).toString()))
                }
            } else {
                if (flashSaleProductItem.campaign.criteria.priceMax <= 0) {
                    tilDiscount.setHelper(null)
                } else {
                    tilDiscount.setHelper(context!!.getString(R.string.price_discount_below_x,
                            Math.round(flashSaleProductItem.campaign.criteria.discountPercentageMax.toDouble()).toString()))
                }
            }
        } else {
            tilDiscount.setHelper(null)
        }
    }

    private fun calculateDiscount() {
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        val discount: Double = if (canSubmit) {
            var productPrice = flashSaleProductItem.getCampOriginalPrice()
            if (productPrice == 0) {
                productPrice = flashSaleProductItem.getProductPrice()
            }
            (productPrice - getFinalPrice()) * 100 / productPrice
        } else {
            flashSaleProductItem.getDiscountPercentage().toDouble()
        }
        if (discount > 0 && discount < 100) {
            etDiscount.setText(getString(R.string.flash_sale_x_percent,
                    Math.round(discount).toString()))
        } else {
            etDiscount.setText(getString(R.string.flash_sale_no_discount))
        }
    }

    private fun getFinalPrice() =
            StringUtils.convertToNumeric(etPrice.text.toString(), false);


    private fun renderStock(savedInstanceState: Bundle?) {
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        if (savedInstanceState == null) {
            val stock = flashSaleProductItem.getCustomStock()
            if (stock > 0) {
                etStock.setText(stock.toString())
            } else {
                etStock.setText("0")
            }
        }

        if (flashSaleProductItem is FlashSaleSubmissionProductItem) {
            if (flashSaleProductItem.campaign.criteria.stockMin > 0) {
                tilStock.setHelper(context!!.getString(R.string.flash_sale_min_stock_x,
                        flashSaleProductItem.campaign.criteria.stockMin))
            } else {
                tilStock.setHelper(null)
            }
        } else {
            tilStock.setHelper(null)
        }
        etStock.addTextChangedListener(stockTextWatcher)
        if (canEdit) {
            etStock.isEnabled = true
        } else {
            etStock.background = null
            etStock.isEnabled = false
        }
    }

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    private fun onBtnRequestProductClicked() {
        if (!canSubmit) {
            return
        }
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        if (flashSaleProductItem is FlashSaleSubmissionProductItem) {
            when (flashSaleProductItem.campaign.getProductStatusAction()) {
                FlashSaleProductActionTypeDef.NO_ACTION -> { /*no-op*/
                }
                FlashSaleProductActionTypeDef.RESERVE -> {
                    if (!isInputValid()) {
                        return
                    }
                    showProgressDialog()
                    presenter.reserveProduct(campaignId,
                            flashSaleProductItem.campaign.criteria.criteriaId,
                            flashSaleProductItem.id, getFinalPrice().toInt(), 0, etStock.text.toString().toInt(),
                            onSuccess = {
                                onSuccessDoAction(it)
                            },
                            onError = {
                                onErrorDoAction(it)
                            })
                }
                FlashSaleProductActionTypeDef.CANCEL -> {
                    showProgressDialog()
                    // cancel reserve
                    presenter.dereserveProduct(campaignId, flashSaleProductItem.id,
                            onSuccess = {
                                onSuccessDoAction(it)
                            },
                            onError = {
                                onErrorDoAction(it)
                            })

                }
                FlashSaleProductActionTypeDef.UNDO_CANCEL -> {
                    showProgressDialog()
                    // submit api
                    presenter.submitProduct(campaignId, flashSaleProductItem.id,
                            onSuccess = {
                                onSuccessDoAction(it)
                            },
                            onError = {
                                onErrorDoAction(it)
                            })
                }
                else -> { /*no-op*/
                }
            }
        }
    }

    private fun onSuccessDoAction(it: FlashSaleDataContainer) {
        hideProgressDialog()
        val resultIntent = Intent()
        if (it.flashSaleCriteriaResponseData.isNotEmpty()) {
            val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct() as FlashSaleSubmissionProductItem
            it.flashSaleCriteriaResponseData
                    .firstOrNull { it.criteriaId == flashSaleProductItem.campaign.criteria.criteriaId }
                    ?.run {
                        resultIntent.putExtra(RESULT_IS_CATEGORY_FULL, !this.isAvailable)
                    }
        }
        if (!resultIntent.hasExtra(RESULT_IS_CATEGORY_FULL)) {
            resultIntent.putExtra(RESULT_IS_CATEGORY_FULL, false)
        }
        activity?.setResult(Activity.RESULT_OK, resultIntent)
        activity?.finish()
    }

    private fun onErrorDoAction(it: Throwable) {
        hideProgressDialog()
        ToasterError.make(view, ErrorHandler.getErrorMessage(context, it), BaseToaster.LENGTH_INDEFINITE)
                .setAction(R.string.retry_label) {
                    onBtnRequestProductClicked()
                }.show()
    }

    private fun isInputValid(): Boolean {
        var isInputValid = true
        if (!isPriceValid()) isInputValid = false
        if (!isDiscountValid()) isInputValid = false
        if (!isStockValid()) isInputValid = false
        return isInputValid
    }

    private fun isPriceValid(): Boolean {
        val item = onFlashSaleProductDetailFragmentListener.getProduct()
        if (item is FlashSaleSubmissionProductItem) {
            val criteria = item.campaign.criteria
            val finalPriceInput = getFinalPrice()
            if ((criteria.priceMin > 0 && finalPriceInput < criteria.priceMin) ||
                    (criteria.priceMax > 0 && finalPriceInput > criteria.priceMax)) {
                tilPrice.setHelperTextAppearance(R.style.TextAppearance_Design_Error)
                return false
            } else {
                tilPrice.setHelperTextAppearance(R.style.helperTextAppearance)
                return true
            }
        } else {
            return true
        }
    }

    private fun isDiscountValid(): Boolean {
        val item = onFlashSaleProductDetailFragmentListener.getProduct()
        if (item is FlashSaleSubmissionProductItem) {
            val criteria = item.campaign.criteria
            val discountInput = StringUtils.convertToNumeric(etDiscount.text.toString(), true)
            if ((criteria.discountPercentageMin > 0 && discountInput < criteria.discountPercentageMin) ||
                    (criteria.discountPercentageMax > 0 && discountInput > criteria.discountPercentageMax)) {
                tilDiscount.setHelperTextAppearance(R.style.TextAppearance_Design_Error)
                context?.let {
                    etDiscount.setTextColor(ContextCompat.getColor(it, R.color.tkpd_dark_red))
                }
                return false
            } else {
                tilDiscount.setHelperTextAppearance(R.style.helperTextAppearance)
                context?.let {
                    etDiscount.setTextColor(ContextCompat.getColor(it, R.color.tkpd_main_green))
                }
                return true
            }
        } else {
            return true
        }
    }

    private fun isStockValid(): Boolean {
        val item = onFlashSaleProductDetailFragmentListener.getProduct()
        if (item is FlashSaleSubmissionProductItem) {
            val criteria = item.campaign.criteria
            val stockInput = StringUtils.convertToNumeric(etStock.text.toString(), true)
            if (stockInput < 0 || stockInput < criteria.stockMin) {
                tilStock.setHelperTextAppearance(R.style.TextAppearance_Design_Error)
                return false
            } else {
                tilStock.setHelperTextAppearance(R.style.helperTextAppearance)
                return true
            }
        } else {
            return true
        }
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setCancelable(true)
            progressDialog!!.setOnCancelListener { presenter.cancelJob() }
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancelJob()
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onFlashSaleProductDetailFragmentListener = context as OnFlashSaleProductDetailFragmentListener
    }

}
