package com.tokopedia.deals.checkout.ui.fragment

import android.app.Activity
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDeals
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common_entertainment.data.EventVerifyResponse
import com.tokopedia.common_entertainment.data.ItemMapResponse
import com.tokopedia.deals.checkout.di.DealsCheckoutComponent
import com.tokopedia.deals.checkout.ui.DealsCheckoutCallbacks
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_DETAIL
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity.Companion.EXTRA_DEAL_VERIFY
import com.tokopedia.deals.checkout.ui.mapper.DealsCheckoutMapper
import com.tokopedia.deals.checkout.ui.viewmodel.DealsCheckoutViewModel
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsCheckoutBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class DealsCheckoutFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var binding by autoClearedNullable<FragmentDealsCheckoutBinding>()
    private val viewModel by viewModels<DealsCheckoutViewModel> { viewModelFactory }
    private var dealsCheckoutCallbacks: DealsCheckoutCallbacks? = null
    private var dealsDetail: ProductDetailData? = null
    private var dealsVerify: EventVerifyResponse? = null
    private var dealsItemMap: ItemMapResponse? = null
    private var promoCode = ""
    private var voucherCode = ""
    private var couponCode = ""
    private var promoApplied = false

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
    private var clPromoDiscount: ConstraintLayout? = null
    private var tgPromoDiscount: Typography? = null
    private var progressBar: FrameLayout? = null

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

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        dealsCheckoutCallbacks = activity as DealsCheckoutActivity
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LOYALTY_ACTIVITY_REQUEST_CODE) {
            hideProgressBar()
            when (resultCode){
                VOUCHER_RESULT_CODE -> {
                    val code = data?.extras?.getString(VOUCHER_CODE) ?: ""
                    val message = data?.extras?.getString(VOUCHER_MESSAGE) ?: ""
                    val amount = data?.extras?.getInt(VOUCHER_DISCOUNT_AMOUNT) ?: 0
                    val isCancel = data?.extras?.getBoolean(IS_CANCEL) ?: false
                    voucherCode = code
                    promoCode = code
                    showPromoSuccess(code, message, amount.toLong(), isCancel)
                }

                COUPON_RESULT_CODE -> {
                    val code = data?.extras?.getString(COUPON_CODE) ?: ""
                    val message = data?.extras?.getString(COUPON_MESSAGE) ?: ""
                    val amount = data?.extras?.getInt(COUPON_DISCOUNT_AMOUNT) ?: 0
                    val isCancel = data?.extras?.getBoolean(IS_CANCEL) ?: false
                    couponCode = code
                    promoCode = code
                    showPromoSuccess(code, message, amount.toLong(), isCancel)
                }
            }
        }
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
            tickerPromoCode = binding?.tickerPromocode
            clPromoDiscount = binding?.clPromo
            tgPromoDiscount = binding?.tgPromoDiscount
            progressBar = binding?.progressBarLayout
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
                    DealsUtils.convertEpochToString(
                        it.saleEndDate.toIntSafely()
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
                tgNumbersLocation?.setOnClickListener { _ ->
                    dealsCheckoutCallbacks?.onShowAllLocation(it.outlets)
                }
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
                tgSalesPriceAllQty?.text =
                    DealsUtils.convertToCurrencyString(itemMap.price * itemMap.quantity)

                if (itemMap.commission <= 0) {
                    tgServiceFee?.gone()
                    tgServiceFeeAmount?.gone()
                } else {
                    tgServiceFeeAmount?.text =
                        DealsUtils.convertToCurrencyString(itemMap.commission.toLong())
                }

                tgTotalAmount?.text =
                    DealsUtils.convertToCurrencyString(itemMap.price.toLong() * itemMap.quantity.toLong() + itemMap.commission.toLong())
                tgNumberVoucher?.text = context?.resources?.getString(
                    com.tokopedia.deals.R.string.deals_checkout_number_of_vouchers,
                    itemMap.quantity
                )
            }

            tickerPromoCode?.apply {
                enableView()
                actionListener = object : TickerPromoStackingCheckoutView.ActionListener {

                    override fun onClickUsePromo() {
                        goToPromoListDealsActivity()
                    }

                    override fun onResetPromoDiscount() {
                        setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                        showPromoSuccess("", "", 0, true)
                        promoApplied = false
                        promoCode = ""
                    }

                    override fun onClickDetailPromo() {
                        if (!couponCode.isNullOrEmpty()) {
                            goToPromoDetailDeals()
                        } else if (!voucherCode.isNullOrEmpty()) {
                            goToPromoListDealsWithVoucher()
                        }
                    }

                    override fun onDisablePromoDiscount() {
                        setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                        showPromoSuccess("", "", 0, true)
                        promoApplied = false
                        promoCode = ""
                    }
                }
            }
        }
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State, title: String, desc: String) {
        if (state == TickerCheckoutView.State.EMPTY) {
            tickerPromoCode?.title = title
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.EMPTY
        } else if (state == TickerCheckoutView.State.ACTIVE) {
            tickerPromoCode?.title = title
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.ACTIVE
            tickerPromoCode?.desc = desc
        }
    }

    private fun showPromoSuccess(
        title: String,
        message: String,
        discountAmount: Long,
        isCancel: Boolean
    ) {
        if (isCancel) {
            promoCode = ""
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.EMPTY
            promoApplied = false
            tickerPromoCode?.title = ""
            tickerPromoCode?.desc = ""
        } else {
            tickerPromoCode?.state = TickerPromoStackingCheckoutView.State.ACTIVE
            tickerPromoCode?.title = title
            tickerPromoCode?.desc = message
            promoApplied = true
        }

        if (discountAmount != 0L) {
            clPromoDiscount?.show()
            tgPromoDiscount?.text = DealsUtils.convertToCurrencyString(discountAmount)
        } else {
            clPromoDiscount?.gone()
        }

        updateAmount(discountAmount)
    }

    private fun updateAmount(discountAmount: Long) {
        dealsItemMap?.let {
            tgTotalAmount?.text =
                DealsUtils.convertToCurrencyString(it.price.toLong() * it.quantity.toLong() + it.commission.toLong() - discountAmount)
        }
    }

    private fun goToPromoListDealsActivity() {
        dealsVerify?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS)
            intent.putExtra(EXTRA_META_DATA, DealsCheckoutMapper.getMetaDataString(it))
            intent.putExtra(EXTRA_CATEGORY_NAME, it.metadata.categoryName)
            intent.putExtra(EXTRA_GRAND_TOTAL, it.metadata.totalPrice)
            intent.putExtra(
                EXTRA_CATEGORYID,
                dealsDetail?.catalog?.digitalCategoryId?.toIntSafely()
            )
            intent.putExtra(EXTRA_PRODUCTID, dealsItemMap?.productId)
            startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun goToPromoDetailDeals() {
        dealsVerify?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_DETAIL_DEALS)
            intent.putExtra(EXTRA_META_DATA, DealsCheckoutMapper.getMetaDataString(it))
            intent.putExtra(EXTRA_CATEGORY_NAME, it.metadata.categoryName)
            intent.putExtra(EXTRA_GRAND_TOTAL, it.metadata.totalPrice)
            intent.putExtra(COUPON_EXTRA_IS_USE, true)
            intent.putExtra(EXTRA_KUPON_CODE, couponCode)
            startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun goToPromoListDealsWithVoucher(){
        dealsVerify?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalPromo.PROMO_LIST_DEALS)
            intent.putExtra(EXTRA_META_DATA, DealsCheckoutMapper.getMetaDataString(it))
            intent.putExtra(EXTRA_CATEGORY_NAME, it.metadata.categoryName)
            intent.putExtra(EXTRA_GRAND_TOTAL, it.metadata.totalPrice)
            intent.putExtra(EXTRA_CATEGORYID, dealsDetail?.catalog?.digitalCategoryId?.toIntSafely())
            intent.putExtra(EXTRA_PRODUCTID, dealsItemMap?.productId)
            intent.putExtra(EXTRA_PROMO_CODE, voucherCode)
            startActivityForResult(intent, LOYALTY_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun showProgressBar() {
        progressBar?.show()
    }

    private fun hideProgressBar() {
        progressBar?.hide()
    }

    companion object {
        private const val EXTRA_META_DATA = "EXTRA_META_DATA"
        private const val EXTRA_PRODUCTID = "EXTRA_PRODUCTID"
        private const val EXTRA_CATEGORYID = "EXTRA_CATEGORYID"
        private const val EXTRA_GRAND_TOTAL = "EXTRA_GRAND_TOTAL"
        private const val EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME"
        private const val COUPON_EXTRA_IS_USE = "EXTRA_IS_USE"
        private const val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        private const val EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        private const val VOUCHER_CODE = "voucher_code"
        private const val COUPON_CODE = "coupon_code"
        private const val IS_CANCEL = "IS_CANCEL"
        private const val VOUCHER_DISCOUNT_AMOUNT = "VOUCHER_DISCOUNT_AMOUNT"
        private const val COUPON_DISCOUNT_AMOUNT = "COUPON_DISCOUNT_AMOUNT"
        private const val VOUCHER_MESSAGE = "voucher_message"
        private const val COUPON_MESSAGE = "coupon_message"

        private const val LOYALTY_ACTIVITY_REQUEST_CODE = 12345
        private const val VOUCHER_RESULT_CODE = 12
        private const val COUPON_RESULT_CODE = 15

        fun createInstance(
            productDetailData: ProductDetailData?,
            verifyData: EventVerifyResponse?
        ): DealsCheckoutFragment {
            val fragment = DealsCheckoutFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_DEAL_DETAIL, productDetailData)
                putParcelable(EXTRA_DEAL_VERIFY, verifyData)
            }
            return fragment
        }
    }
}
