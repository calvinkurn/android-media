package com.tokopedia.normalcheckout.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantItemDecorator
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog
import com.tokopedia.normalcheckout.adapter.NormalCheckoutAdapterTypeFactory
import com.tokopedia.normalcheckout.constant.ATC_AND_BUY
import com.tokopedia.normalcheckout.constant.ATC_AND_SELECT
import com.tokopedia.normalcheckout.constant.ATC_ONLY
import com.tokopedia.normalcheckout.constant.ProductAction
import com.tokopedia.normalcheckout.di.DaggerNormalCheckoutComponent
import com.tokopedia.normalcheckout.model.ProductInfoAndVariant
import com.tokopedia.normalcheckout.presenter.NormalCheckoutViewModel
import com.tokopedia.normalcheckout.router.NormalCheckoutRouter
import com.tokopedia.payment.activity.TopPayActivity
import com.tokopedia.payment.model.PaymentPassData
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_normal_checkout.*
import rx.Observable
import javax.inject.Inject

class NormalCheckoutFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypeFactory>(),
        NormalCheckoutContract.View, CheckoutVariantActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: NormalCheckoutViewModel

    val tkpdProgressDialog: TkpdProgressDialog by lazy {
        TkpdProgressDialog(context, TkpdProgressDialog.NORMAL_PROGRESS)
    }
    val fragmentViewModel: FragmentViewModel by lazy {
        FragmentViewModel()
    }
    private lateinit var router: NormalCheckoutRouter
    private lateinit var adapter: CheckoutVariantAdapter

    var shopId: String? = null
    var productId: String? = null
    var notes: String? = null
    var quantity: Int = 0
    var selectedVariantId: String? = null
    var placeholderProductImage: String? = null
    @ProductAction
    var action: Int = ATC_AND_BUY

    var selectedProductInfo: ProductInfo? = null
    var originalProduct: ProductInfoAndVariant? = null

    companion object {
        const val EXTRA_SHOP_ID = "shop_id"
        const val EXTRA_PRODUCT_ID = "product_id"
        const val EXTRA_NOTES = "notes"
        const val EXTRA_QUANTITY = "quantity"
        const val EXTRA_SELECTED_VARIANT_ID = "selected_variant_id"
        const val EXTRA_ACTION = "action"
        const val EXTRA_PRODUCT_IMAGE = "product_image"

        fun createInstance(shopId: String?, productId: String?,
                           notes: String? = "", quantity: Int? = 0,
                           selectedVariantId: String? = null,
                           @ProductAction action: Int = ATC_AND_BUY,
                           placeholderProductImage: String?): NormalCheckoutFragment {
            val fragment = NormalCheckoutFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SHOP_ID, shopId)
                    putString(EXTRA_PRODUCT_ID, productId)
                    putString(EXTRA_NOTES, notes)
                    putInt(EXTRA_QUANTITY, quantity ?: 0)
                    putInt(EXTRA_ACTION, action)
                    putString(EXTRA_PRODUCT_IMAGE, placeholderProductImage)
                    putString(EXTRA_SELECTED_VARIANT_ID, selectedVariantId ?: "")
                }
            }

            return fragment
        }
    }

    override fun initInjector() {
        activity?.run {
            DaggerNormalCheckoutComponent.builder()
                    .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()
                    .inject(this@NormalCheckoutFragment)
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(NormalCheckoutViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.productInfoResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetProductInfo(it.data)
                is Fail -> onErrorGetProductInfo(it.throwable)
            }
        })
    }

    private fun onSuccessGetProductInfo(productInfoAndVariant: ProductInfoAndVariant) {
        originalProduct = productInfoAndVariant
        if (selectedVariantId.isNullOrEmpty()) {
            selectedVariantId = productInfoAndVariant.productVariant.defaultChildString
        }
        originalProduct?.run {
            onProductChange(this, selectedVariantId)
        }
    }

    override fun onVariantGuidelineClick(variantGuideline: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * selectedVariantId comes from the parameter of the fragment or might come from the user input
     * This function will get the product corresponding to the id.
     * If the id is not given, then the default product is return
     * If the id exists, it will search for the variant in the product and then return the mapping
     */
    fun getSelectedProductInfo(productInfoAndVariant: ProductInfoAndVariant, selectedVariantId: String?): ProductInfo {
        if (selectedVariantId.isNullOrEmpty() ||
                selectedVariantId.equals(productInfoAndVariant.productInfo.basic.id.toString(), false)) {
            return productInfoAndVariant.productInfo
        } else {
            val selectedVariant = productInfoAndVariant.productVariant.getVariant(selectedVariantId)
            if (selectedVariant != null) {
                if (selectedVariant.isBuyable) {
                    return ModelMapper.convertToModels(originalProduct?.productInfo!!, selectedVariant)
                } else {
                    val child = getOtherSiblingProduct(originalProduct!!, selectedVariant.optionIds)
                    if (child == null) {
                        return productInfoAndVariant.productInfo
                    } else {
                        return ModelMapper.convertToModels(productInfoAndVariant.productInfo, child)
                    }
                }
            } else {
                return productInfoAndVariant.productInfo
            }
        }
    }

    /**
     * When the optionId given is actually not n the children of the variant, we want to switch to another product
     * For example, option ID for [101,201,301] is not found as a children for variant,
     * So, another first product is searched: [101,201,**] that is buyable. The first item found is returned.
     */
    private fun getOtherSiblingProduct(productInfoAndVariant: ProductInfoAndVariant?, optionId: List<Int>): Child? {
        var selectedChild: Child? = null
        // we need to reselect other variant
        productInfoAndVariant?.run {
            val optionsIdList = optionId
            val optionPartialSize = optionsIdList.size - 1
            val partialOptionIdList = optionsIdList.subList(0, optionPartialSize)
            for (childLoop: Child in productVariant.children) {
                if (childLoop.isBuyable && childLoop.optionIds.subList(0, optionPartialSize).equals(partialOptionIdList)) {
                    selectedChild = childLoop
                    break
                }
            }
        }
        return selectedChild
    }

    private fun renderActionButton(productInfo: ProductInfo) {
        if (GlobalConfig.isCustomerApp() && !viewModel.isShopOwner(productInfo.basic.shopID) &&
                productInfo.basic.isActive()) {
            if (productInfo.basic.isWarehouse()) { //out of stock
                showFullButton(false, false, false)
            } else {
                button_buy_full.gone()
                button_buy_partial.visible()
                rl_bottom_action_container.visible()
                if (action == ATC_AND_SELECT || action == ATC_AND_BUY) {
                    button_cart.visible()
                } else {
                    button_cart.gone()
                }
                button_buy_partial.text = if (action == ATC_ONLY) {
                    getString(R.string.add_to_cart)
                } else if (productInfo.isPreorderActive) {
                    getString(R.string.label_button_preorder)
                } else {
                    getString(R.string.label_button_buy)
                }
                if (hasError()) {
                    button_buy_partial.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
                } else {
                    button_buy_partial.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_orange_enabled)
                }
            }
        } else { // sellerapp
            showFullButton(productInfo.basic.isWarehouse(), productInfo.isPreorderActive, false)
        }
    }

    private fun renderTotalPrice(productInfo: ProductInfo) {
        // if it has campaign, use campaign price
        var totalString = ""
        if (productInfo.campaign.activeAndHasId) {
            totalString = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    productInfo.campaign.discountedPrice.toDouble() * quantity, true)
        } else {
            // if it has wholesale, use the price in the wholesale range
            if (productInfo.hasWholesale) {
                productInfo.wholesale!!.forEachIndexed { index, item ->
                    val hasNextItem = (index + 1) < (productInfo.wholesale!!.size)
                    val isLessThanNextMinQty = if (hasNextItem) {
                        quantity < productInfo.wholesale!![index + 1].minQty
                    } else true
                    if (quantity >= item.minQty && isLessThanNextMinQty) {
                        totalString = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                item.price.toDouble() * quantity, true)
                    }
                }
            }
        }
        tv_total.text = if (totalString.isEmpty()) {
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    productInfo.basic.price.toDouble() * quantity, true)
        } else {
            totalString
        }
    }

    // show disabled secondary-buy button, and hide main buttons
    private fun showFullButton(hasStock: Boolean = false,
                               isPreorder: Boolean = false, enabled: Boolean = false) {
        rl_bottom_action_container.gone()
        button_buy_full.visible()
        button_buy_full.text = if (!hasStock) {
            getString(R.string.no_stock)
        } else if (isPreorder) {
            getString(R.string.label_button_preorder)
        } else {
            getString(R.string.label_button_buy)
        }
        button_buy_full.isClickable = enabled
        button_buy_full.isEnabled = enabled
    }

    private fun onErrorGetProductInfo(throwable: Throwable) {
        ToasterError.make(activity!!.findViewById(android.R.id.content),
                ErrorHandler.getErrorMessage(context, throwable)).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val argument = arguments
        if (argument != null) {
            shopId = argument.getString(EXTRA_SHOP_ID)
            productId = argument.getString(EXTRA_PRODUCT_ID)
            notes = argument.getString(EXTRA_NOTES)
            quantity = argument.getInt(EXTRA_QUANTITY)
            placeholderProductImage = argument.getString(EXTRA_PRODUCT_IMAGE)
            action
        }
        if (savedInstanceState == null) {
            if (argument != null) {
                selectedVariantId = argument.getString(EXTRA_SELECTED_VARIANT_ID)
            }
        } else {
            selectedVariantId = savedInstanceState.getString(EXTRA_SELECTED_VARIANT_ID)
            notes = savedInstanceState.getString(EXTRA_NOTES)
            quantity = savedInstanceState.getInt(EXTRA_QUANTITY)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_normal_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(CheckoutVariantItemDecorator())
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        super.onViewCreated(view, savedInstanceState)
        button_buy_partial.setOnClickListener {
            if (hasError()) {
                return@setOnClickListener
            }
            //TODO buy
            if (action == ATC_ONLY) {
                addToCart()
            } else {
                // TODO buy or preorder
            }
        }
        button_cart.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        //TODO add to cart
    }

    override fun showData(viewModels: ArrayList<Visitable<*>>) { /* no op we use onSuccess */}

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.run {
            router = applicationContext as NormalCheckoutRouter
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypeFactory {
        return NormalCheckoutAdapterTypeFactory(this)
    }

    override fun loadData(page: Int) {
        viewModel.getProductInfo(ProductParams(productId, null, null), resources)
    }

    override fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel) {
        val optionList = mutableListOf<Int>()
        var variantSize = 0
        for (viewModel: Visitable<*> in fragmentViewModel.viewModels) {
            if (viewModel is TypeVariantViewModel) {
                viewModel.getSelectedOption()?.let {
                    optionList.add(it)
                }
                variantSize++
            }
        }
        //selection option might partial selected, we only care for full selection
        if (optionList.isNotEmpty() && optionList.size == variantSize) {
            originalProduct?.run {
                if (productVariant.hasChildren) {
                    var selectedChild: Child? = null
                    for (childModel: Child in productVariant.children) {
                        if (childModel.optionIds.equals(optionList)) {
                            selectedChild = childModel
                            break
                        }
                    }
                    if (selectedChild == null) {
                        val child = getOtherSiblingProduct(this, optionList)
                        if (child == null) {
                            onProductChange(this, productInfo.basic.id.toString())
                        } else {
                            onProductChange(this, child.productId.toString())
                        }
                    } else {
                        onProductChange(this, selectedChild.productId.toString())
                    }
                }
            }

            fragmentViewModel.isStateChanged = true
        }
    }

    private fun onProductChange(originalProduct: ProductInfoAndVariant, inputSelectedVariantId: String?) {
        selectedVariantId = inputSelectedVariantId
        selectedProductInfo = getSelectedProductInfo(originalProduct, selectedVariantId)
        selectedProductInfo?.let { it ->
            val viewModels = ModelMapper.convertToModels(it, originalProduct.productVariant,
                    notes, quantity)
            fragmentViewModel.viewModels = viewModels
            quantity = fragmentViewModel.getQuantityViewModel()?.orderQuantity
                    ?: 0
            adapter.clearAllElements()
            adapter.addDataViewModel(viewModels)
            adapter.notifyDataSetChanged()
            renderActionButton(it)
            renderTotalPrice(it)
        }
    }

    override fun onChangeQuantity(quantityViewModel: QuantityViewModel) {
        quantity = quantityViewModel.orderQuantity
        //TODO check with the previous code
        selectedProductInfo?.let {
            renderActionButton(it)
            renderTotalPrice(it)
        }
        fragmentViewModel.isStateChanged = true
    }

    override fun onChangeNote(noteViewModel: NoteViewModel) {
        if (fragmentViewModel.isStateChanged == false && noteViewModel.note.isNotEmpty()) {
            fragmentViewModel.isStateChanged = true
        }
    }

    private fun hasError(): Boolean {
        var hasError = false
        when {
            fragmentViewModel.getQuantityViewModel()?.isStateError == true -> hasError = true
        }
        return hasError
    }

    override fun onGetCompositeSubscriber() = null

    override fun onBindProductUpdateQuantityViewModel(productViewModel: ProductViewModel, stockWording: String) {}

    override fun onBindVariantGetProductViewModel(): ProductViewModel? {
        return fragmentViewModel.getProductViewModel()
    }

    override fun getScreenName(): String? = null

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: activity?.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun navigateCheckoutToPayment(paymentPassData: PaymentPassData) {
        if (activity != null) startActivityForResult(
                TopPayActivity.createInstance(activity, paymentPassData),
                TopPayActivity.REQUEST_CODE)
        activity?.finish()
    }

    override fun navigateCheckoutToThankYouPage(appLink: String) {
        if (activity != null) startActivity(RouteManager.getIntent(activity, appLink))
        activity?.finish()
    }

    override fun getAddToCartObservable(addToCartRequest: AddToCartRequest): Observable<AddToCartResult> {
        return router.addToCartProduct(addToCartRequest, true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SELECTED_VARIANT_ID, selectedVariantId)
        outState.putInt(EXTRA_QUANTITY, quantity)
        outState.putString(EXTRA_NOTES, notes)
    }

    override fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean) {}
    override fun showErrorNotAvailable(message: String) {}
    override fun updateFragmentViewModel(atcResponseModel: AtcResponseModel) {}
    override fun onNeedToNotifySingleItem(position: Int) {}
    override fun onItemClicked(t: Visitable<*>?) {}
    override fun onClickEditProfile() {}
    override fun onClickEditDuration() {}
    override fun onClickEditCourier() {}
    override fun onNeedToRemoveSingleItem(position: Int) {}
    override fun onNeedToNotifyAllItem() {}
    override fun onClickInsuranceInfo(insuranceInfo: String) {}
    override fun onSummaryChanged(summaryViewModel: SummaryViewModel?) {}
    override fun onInsuranceCheckChanged(insuranceViewModel: InsuranceViewModel) {}
    override fun onNeedToValidateButtonBuyVisibility() {}
    override fun onNeedToRecalculateRatesAfterChangeTemplate() {}
    override fun onNeedToUpdateOnboardingStatus() {}
    override fun onBindVariantUpdateProductViewModel() {}

}