package com.tokopedia.deals.checkout.ui.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.deals.R
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_DETAIL
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_VERIFY
import com.tokopedia.deals.checkout.ui.viewmodel.DealsCheckoutViewModel
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsCheckoutBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DealsCheckoutFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentDealsCheckoutBinding>()
    private val viewModel by viewModels<DealsCheckoutViewModel> { viewModelFactory }
    private var dealsDetail: ProductDetailData? = null
    private var dealsVerify: EventVerifyResponse? = null
    private var dealsItemMap: ItemMapResponse? = null

    private var imgBrand: ImageUnify? = null
    private var tgBrandName: Typography? = null
    private var tgTitle: Typography? = null
    private var tgExpiredDate: Typography? = null
    private var tgNumbersLocation: Typography? = null
    private var tgAllLocation: Typography? = null
    private var etEmail: TextFieldUnify? = null
    private var tgMrpPerQuantity: Typography? = null
    private var tgSalesPricePerQty: Typography? = null
    private var tgSalesPriceAllQty: Typography? = null
    private var tgServiceFee: Typography? = null
    private var tgServiceFeeAmount: Typography? = null
    private var tgTotalAmount: Typography? = null
    private var tgNumberVoucher: Typography? = null
    private var btnPayment: UnifyButton? = null
    private var tickerPromoCode: TickerPromoStackingCheckoutView? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DealsCheckoutComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dealsDetail = it.getParcelable(EXTRA_DEAL_DETAIL)
            dealsVerify = it.getParcelable(EXTRA_DEAL_VERIFY)
            dealsItemMap = dealsVerify?.metadata?.itemMap?.first()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsCheckoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        showUI()
    }

    private fun setupUI() {
        view?.apply {
            imgBrand = binding?.imageViewBrand
            tgBrandName = binding?.tgBrandName
            tgTitle = binding?.tgDealDetails
            tgExpiredDate = binding?.tgExpiryDate
            tgNumbersLocation = binding?.tgNoLocations
            tgAllLocation = binding?.tgAvailableLocations
            etEmail = binding?.etEmail
            tgMrpPerQuantity = binding?.tgMrpPerQuantity
            tgSalesPricePerQty = binding?.tgSalesPricePerQuantity
            tgSalesPriceAllQty = binding?.tgSalesPriceAllQuantity
            tgServiceFee = binding?.tgServiceFee
            tgServiceFeeAmount = binding?.tgServiceFeeAmount
            tgTotalAmount = binding?.tgTotalAmount
            tgNumberVoucher = binding?.tgNumberVouchers
        }
    }

    private fun showUI() {
        dealsDetail?.let {
            imgBrand?.loadImage(it.imageApp)
            tgBrandName?.text = it.brand.title
            tgTitle?.text = it.displayName
            tgExpiredDate?.text = String.format(
                getString(
                    com.tokopedia.deals.R.string.deals_pdp_valid_through,
                    DealsUtils.convertEpochToString(it.saleEndDate.toIntSafely()
                    )
                )
            )

            if (it.outlets.isNullOrEmpty()) {
                tgAllLocation?.text = context?.resources?.getString(
                    com.tokopedia.deals.R.string.deals_checkout_all_indonesia
                )
            }

            if (!it.outlets.isNullOrEmpty()) {
                tgNumbersLocation?.text = String.format(
                    getString(
                        com.tokopedia.deals.R.string.deals_checkout_number_of_locations,
                            it.outlets.size
                    )
                )
            }

            etEmail?.textFieldInput?.setText(userSession.email)
            etEmail?.textFieldInput?.setKeyListener(null)

            if (it.mrp.toIntSafely() != 0 && it.mrp != it.salesPrice) {
                tgMrpPerQuantity?.apply {
                    show()
                    text = DealsUtils.convertToCurrencyString(it.mrp.toIntSafely().toLong())
                    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                tgMrpPerQuantity?.hide()
            }

            dealsItemMap?.let { itemMap ->
                tgSalesPricePerQty?.text = DealsUtils.convertToCurrencyString(itemMap.price)
                tgSalesPriceAllQty?.text = DealsUtils.convertToCurrencyString(itemMap.price * itemMap.quantity)

                if (itemMap.commission <= 0) {
                    tgServiceFee?.gone()
                    tgServiceFeeAmount?.gone()
                } else {
                    tgServiceFeeAmount?.text = DealsUtils.convertToCurrencyString(itemMap.commission.toLong())
                }

                tgTotalAmount?.text = DealsUtils.convertToCurrencyString(itemMap.price.toLong() * itemMap.quantity.toLong() + itemMap.commission.toLong())
                tgNumberVoucher?.text = context?.resources?.getString(com.tokopedia.deals.R.string.deals_checkout_number_of_vouchers, itemMap.quantity)
            }
        }
    }

    companion object {
        fun createInstance(productDetailData: ProductDetailData?, verifyData: EventVerifyResponse?): DealsCheckoutFragment {
            val fragment = DealsCheckoutFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_DEAL_DETAIL, productDetailData)
                putParcelable(EXTRA_DEAL_VERIFY, verifyData)
            }
            return fragment
        }
    }
}
