package com.tokopedia.deals.pdp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.deals.checkout.ui.activity.DealsCheckoutActivity
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.utils.DealsUtils
import com.tokopedia.deals.databinding.FragmentDealsDetailSelectQuantityBinding
import com.tokopedia.deals.pdp.data.ProductDetailData
import com.tokopedia.deals.pdp.di.DealsPDPComponent
import com.tokopedia.deals.pdp.ui.activity.DealsPDPActivity
import com.tokopedia.deals.pdp.ui.viewmodel.DealsPDPSelectQuantityViewModel
import com.tokopedia.deals.R.string as stringDeals
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DealsPDPSelectDealsQuantityFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var analytics: DealsAnalytics

    private val viewModel by viewModels<DealsPDPSelectQuantityViewModel> { viewModelFactory }
    private var productDetailData: ProductDetailData? = null
    private var binding by autoClearedNullable<FragmentDealsDetailSelectQuantityBinding>()
    private var toolbar: HeaderUnify? = null
    private var ivBrand: ImageView? = null
    private var tgTitle: Typography? = null
    private var tgBrandName: Typography? = null
    private var quantityEditor: QuantityEditorUnify? = null
    private var tgMrp: Typography? = null
    private var tgSalesPrice: Typography? = null
    private var tgTotalAmount: Typography? = null
    private var btnCheckout: UnifyButton? = null
    private var progressBar: LoaderUnify? = null
    private var progressBarLayout: FrameLayout? = null

    override fun initInjector() {
        getComponent(DealsPDPComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        productDetailData = arguments?.getParcelable<ProductDetailData>(EXTRA_PRODUCT_DATA)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDealsDetailSelectQuantityBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeFlowData()
        setupHeader()
        renderUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LOGIN -> context?.let {
                    goToCheckout()
                }
            }
        }
    }

    private fun setupUi() {
        view?.apply {
            toolbar = binding?.toolbar
            ivBrand = binding?.ivBrand
            tgTitle = binding?.tgDealDetails
            tgBrandName = binding?.tgBrandName
            tgMrp = binding?.tgMrp
            tgSalesPrice = binding?.tgSalesPrice
            tgTotalAmount = binding?.tgTotalAmount
            quantityEditor = binding?.qtyEditor
            btnCheckout = binding?.btnContinue
            progressBar = binding?.progressBar
            progressBarLayout = binding?.progressBarLayout
        }
    }

    private fun setupHeader() {
        toolbar?.headerTitle = context?.resources?.getString(stringDeals.deals_pdp_select_number_of_voucher).orEmpty()
        (activity as DealsPDPActivity).setSupportActionBar(toolbar)
        (activity as DealsPDPActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun renderUi() {
        productDetailData?.apply {
            ivBrand?.loadImage(imageApp)
            tgTitle?.text = displayName
            tgBrandName?.text = brand.title
            val mrp = mrp.toLong()
            val salesPrice = salesPrice.toLong()

            if (mrp.isMoreThanZero() && mrp != salesPrice) {
                tgMrp?.apply {
                    show()
                    text = DealsUtils.convertToCurrencyString(mrp)
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                tgMrp?.hide()
            }

            tgSalesPrice?.text = DealsUtils.convertToCurrencyString(salesPrice)
            setQuantity(minQty)
            updateTotalAmount(salesPrice)
            quantityEditor?.minValue = minQty
            quantityEditor?.maxValue = maxQty
            quantityEditor?.editText?.keyListener = null
            quantityEditor?.setValueChangedListener { newValue, _, _ ->
                setQuantity(newValue)
                updateTotalAmount(salesPrice)
            }
            btnCheckout?.setOnClickListener {
                if (userSession.isLoggedIn) {
                    goToCheckout()
                } else {
                    startActivityForResult(
                        RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN
                    )
                }
            }
        }
    }

    private fun getCurrentQuantity(): Int = viewModel.currentQuantity

    private fun setQuantity(qty: Int) {
        viewModel.currentQuantity = qty
    }

    private fun updateTotalAmount(salesPrice: Long) {
        tgTotalAmount?.text = DealsUtils.convertToCurrencyString(getCurrentQuantity() * salesPrice)
    }

    private fun goToCheckout() {
        showProgressBar()
        verifyCheckout()
    }

    private fun verifyCheckout() {
        productDetailData?.let {
            analytics.checkoutCartPageLoaded(getCurrentQuantity(), it.categoryId, it.id, it.displayName,
                it.brand.title, it.salesPrice)
            viewModel.setVerifyRequest(it)
        }
    }

    private fun showProgressBar() {
        progressBarLayout?.show()
        progressBar?.show()
    }

    private fun hideProgressBar() {
        progressBarLayout?.hide()
        progressBar?.hide()
    }

    private fun observeFlowData() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.flowVerify.collect {
                hideProgressBar()
                when (it) {
                    is Success -> {
                        context?.let { context ->
                            productDetailData?.let { productDetailData ->
                                    val intent = Intent(context, DealsCheckoutActivity::class.java)
                                    intent.putExtra(EXTRA_DEAL_DETAIL,productDetailData)
                                    intent.putExtra(EXTRA_DEAL_VERIFY, it.data.eventVerify)
                                    startActivity(intent)
                            }
                        }
                    }

                    is Fail -> {
                        val error = ErrorHandler.getErrorMessage(context, it.throwable)
                        view?.let {
                            Toaster.build(it, error, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_LOGIN = 101
        private const val EXTRA_PRODUCT_DATA = "EXTRA_PRODUCT_DATA"
        private const val EXTRA_DEAL_DETAIL = "EXTRA_DEAL_DETAIL"
        private const val EXTRA_DEAL_VERIFY = "EXTRA_DEAL_VERIFY"

        fun createInstance(data: ProductDetailData): DealsPDPSelectDealsQuantityFragment {
            val fragment = DealsPDPSelectDealsQuantityFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PRODUCT_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}
