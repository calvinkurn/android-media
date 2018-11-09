package com.tokopedia.flashsale.management.product.view

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.KMNumbers
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.data.FlashSaleProductActionTypeDef
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
    var campaignId: Int = 0
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
        campaignId = arguments!!.getInt(EXTRA_PARAM_CAMPAIGN_ID)
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

        renderPrice(savedInstanceState, flashSaleProductItem)
        renderDiscount(savedInstanceState, flashSaleProductItem)
        renderStock(savedInstanceState, flashSaleProductItem)

        if (canEdit && flashSaleProductItem.campaign.getProductStatusAction() == FlashSaleProductStatusTypeDef.NOTHING) {
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

    private fun renderPrice(savedInstanceState: Bundle?, flashSaleProductItem: FlashSaleProductItem) {
        currencyTextWatcher = CurrencyTextWatcher(etPrice, CurrencyEnum.RPwithSpace)
        etPrice.addTextChangedListener(currencyTextWatcher)
        if (savedInstanceState == null) {
            if (flashSaleProductItem.campaign.discountedPrice > 0) {
                etPrice.setText(flashSaleProductItem.campaign.discountedPrice.toString())
            } else {
                etPrice.setText("0")
            }
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

    private fun renderDiscount(savedInstanceState: Bundle?, flashSaleProductItem: FlashSaleProductItem) {
        etDiscount.background = null
        if (savedInstanceState == null) {
            val discount: Double = if (canEdit) {
                (flashSaleProductItem.price - getFinalPrice()) * 100 / flashSaleProductItem.price
            } else {
                flashSaleProductItem.campaign.discountedPercentage.toDouble()
            }
            if (discount > 0 && discount < 100) {
                etDiscount.setText(getString(R.string.flash_sale_x_percent,
                        KMNumbers.formatDouble2PCheckRound(discount, true)))
            } else {
                etDiscount.setText(getString(R.string.flash_sale_no_discount))
            }
        }
        if (flashSaleProductItem.campaign.criteria.discountPercentageMin > 0) {
            if (flashSaleProductItem.campaign.criteria.discountPercentageMax <= 0) {
                tilDiscount.setHelper(context!!.getString(R.string.price_discount_above_x,
                        KMNumbers.formatDouble2PCheckRound(flashSaleProductItem.campaign.criteria.discountPercentageMin.toDouble(),
                                true)))
            } else {
                tilDiscount.setHelper(context!!.getString(R.string.price_discount_between_x_and_x,
                        KMNumbers.formatDouble2PCheckRound(flashSaleProductItem.campaign.criteria.discountPercentageMin.toDouble(),
                                true),
                        KMNumbers.formatDouble2PCheckRound(flashSaleProductItem.campaign.criteria.discountPercentageMax.toDouble(),
                                true)))
            }
        } else {
            if (flashSaleProductItem.campaign.criteria.priceMax <= 0) {
                tilDiscount.setHelper(null)
            } else {
                tilDiscount.setHelper(context!!.getString(R.string.price_discount_below_x,
                        KMNumbers.formatDouble2PCheckRound(flashSaleProductItem.campaign.criteria.discountPercentageMax.toDouble(),
                                true)))
            }
        }
    }

    private fun getFinalPrice() =
            StringUtils.convertToNumeric(etPrice.text.toString(), false);


    private fun renderStock(savedInstanceState: Bundle?, flashSaleProductItem: FlashSaleProductItem) {
        if (savedInstanceState == null) {
            val stock = flashSaleProductItem.campaign.stock
            if (stock > 0) {
                etStock.setText(stock.toString())
            } else {
                etStock.text = null
            }
        }

        if (flashSaleProductItem.campaign.criteria.stockMin > 0) {
            tilStock.setHelper(context!!.getString(R.string.flash_sale_min_stock_x,
                    flashSaleProductItem.campaign.criteria.stockMin))
        } else {
            tilStock.setHelper(null)
        }
    }

    override fun initInjector() {
        getComponent(CampaignComponent::class.java).inject(this)
    }

    fun onBtnRequestProductClicked() {
        //TODO
        if (!canEdit) {
            return
        }
        val flashSaleProductItem = onFlashSaleProductDetailFragmentListener.getProduct()
        when (flashSaleProductItem.campaign.getProductStatusAction()) {
            FlashSaleProductActionTypeDef.NO_ACTION -> { /*no-op*/ }
            FlashSaleProductActionTypeDef.RESERVE -> {
                showProgressDialog()
                presenter.reserveProduct(campaignId, flashSaleProductItem.campaign.criteria.criteriaId,
                        flashSaleProductItem.id, getFinalPrice().toInt(),  0 , etStock.text.toString().toInt(),
                        onSuccess = {
                            hideProgressDialog()
                            //TODO show message, activity Result
                            /*activity?.run {
                                ToasterNormal.showClose(this, it)
                            }
                            loadInitialData()*/
                        },
                        onError = {
                            hideProgressDialog()
                            //TODO show error
                            /*ToasterError.make(view, ErrorHandler.getErrorMessage(context, it), BaseToaster.LENGTH_INDEFINITE)
                                    .setAction(R.string.retry_label) {
                                        onClickToUpdateSubmission()
                                    }.show()*/
                        })
            }
            FlashSaleProductActionTypeDef.CANCEL -> {
                showProgressDialog()
                // cancel reserve

            }
            FlashSaleProductActionTypeDef.UNDO_CANCEL -> {
                showProgressDialog()
                // submit api
            }
            FlashSaleProductActionTypeDef.RE_RESERVE -> {
                showProgressDialog()
                // re-reserve
            }
            else -> { /*no-op*/ }
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancelJob()
    }

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        onFlashSaleProductDetailFragmentListener = context as OnFlashSaleProductDetailFragmentListener
    }

}
