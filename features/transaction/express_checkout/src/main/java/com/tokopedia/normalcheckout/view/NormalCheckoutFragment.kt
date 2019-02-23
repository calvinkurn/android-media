package com.tokopedia.normalcheckout.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.Tooltip
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheetsActionListener
import com.tokopedia.expresscheckout.data.constant.MAX_QUANTITY
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.domain.model.atc.WholesalePriceModel
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantItemDecorator
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.util.isOnboardingStateHasNotShown
import com.tokopedia.expresscheckout.view.variant.util.setOnboardingStateHasNotShown
import com.tokopedia.expresscheckout.view.variant.viewmodel.*
import com.tokopedia.logisticcommon.utils.TkpdProgressDialog
import com.tokopedia.normalcheckout.adapter.NormalCheckoutAdapterTypeFactory
import com.tokopedia.normalcheckout.constant.ATC_AND_BUY
import com.tokopedia.normalcheckout.constant.ProductAction
import com.tokopedia.normalcheckout.di.DaggerNormalCheckoutComponent
import com.tokopedia.normalcheckout.model.ProductInfoAndVariant
import com.tokopedia.normalcheckout.presenter.NormalCheckoutViewModel
import com.tokopedia.normalcheckout.router.NormalCheckoutRouter
import com.tokopedia.payment.activity.TopPayActivity
import com.tokopedia.payment.model.PaymentPassData
import com.tokopedia.product.detail.common.data.model.ProductParams
import com.tokopedia.transaction.common.sharedata.AddToCartRequest
import com.tokopedia.transaction.common.sharedata.AddToCartResult
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_normal_checkout.*
import rx.Observable
import rx.subscriptions.CompositeSubscription
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
    val errorBottomsheets: ErrorBottomsheets by lazy {
        ErrorBottomsheets()
    }

    private lateinit var router: NormalCheckoutRouter
    private lateinit var adapter: CheckoutVariantAdapter
    private lateinit var fragmentListener: NormalCheckoutListener
    private val compositeSubscription = CompositeSubscription()

    var shopId: String? = null
    var productId: String? = null
    var notes: String? = null
    var quantity: Int? = null
    var selectedVariantId: ArrayList<String>? = null
    var placeholderProductImage: String? = null
    lateinit var recyclerView: RecyclerView
    @ProductAction
    var action: Int = ATC_AND_BUY

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
                           selectedVariantId: ArrayList<String>? = null,
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
                    putStringArrayList(EXTRA_SELECTED_VARIANT_ID, selectedVariantId
                            ?: arrayListOf<String>())
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
        val viewModels = ModelMapper.convertToModels(productInfoAndVariant,
                notes, quantity ?: 0, selectedVariantId)
        fragmentViewModel.viewModels = viewModels
        adapter.clearAllElements()
        adapter.addDataViewModel(viewModels)
        adapter.notifyDataSetChanged()

    }

    private fun onErrorGetProductInfo(throwable: Throwable) {
        ToasterError.make(activity!!.findViewById(android.R.id.content),
                ErrorHandler.getErrorMessage(context, throwable)).show()
    }


    override fun onClickEditProfile() {
        //no op
    }

    override fun onClickEditDuration() {
        //no op
    }

    override fun onClickEditCourier() {
        //no op
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
                selectedVariantId = argument.getStringArrayList(EXTRA_SELECTED_VARIANT_ID)
            }
        } else {
            selectedVariantId = savedInstanceState.getStringArrayList(EXTRA_SELECTED_VARIANT_ID)
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

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentListener = context as NormalCheckoutListener
        router = context.applicationContext as NormalCheckoutRouter
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypeFactory {
        return NormalCheckoutAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun loadData(page: Int) {
        viewModel.getProductInfo(ProductParams(productId, null, null), resources)
    }

    override fun onNeedToNotifySingleItem(position: Int) {
        if (recyclerView.isComputingLayout) {
            recyclerView.post {
                adapter.notifyItemChanged(position)
            }
        } else {
            adapter.notifyItemChanged(position)
        }
    }

    override fun onNeedToRemoveSingleItem(position: Int) {
        if (recyclerView.isComputingLayout) {
            recyclerView.post {
                adapter.notifyItemRemoved(position)
            }
        } else {
            adapter.notifyItemRemoved(position)
        }
    }

    override fun onNeedToNotifyAllItem() {
        if (recyclerView.isComputingLayout) {
            recyclerView.post {
                adapter.notifyDataSetChanged()
            }
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onClickInsuranceInfo(insuranceInfo: String) {
        if (activity != null) {
            val tooltip = Tooltip(activity as Context)
            tooltip.setTitle(activity?.getString(R.string.title_bottomsheet_insurance))
            tooltip.setDesc(insuranceInfo)
            tooltip.setTextButton(activity?.getString(R.string.label_button_bottomsheet_close))
            tooltip.setIcon(R.drawable.ic_insurance)
            tooltip.btnAction.setOnClickListener {
                tooltip.dismiss()
            }
            tooltip.show()
        }
    }

    override fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel) {
        val productViewModel = fragmentViewModel.getProductViewModel()
        val quantityViewModel = fragmentViewModel.getQuantityViewModel()

        if (productViewModel != null && productViewModel.productChildrenList.isNotEmpty()) {
            var selectedKey = 0
            for ((key, value) in productViewModel.selectedVariantOptionsIdMap) {
                if (key == selectedOptionViewModel.variantId && value != selectedOptionViewModel.optionId) {
                    selectedKey = key
                }
            }
            if (selectedKey != 0) {
                productViewModel.selectedVariantOptionsIdMap[selectedKey] = selectedOptionViewModel.optionId
            }

            // Check is product child for selected variant is available
            var newSelectedProductChild: ProductChild? = null
            for (productChild: ProductChild in productViewModel.productChildrenList) {
                var matchOptionId = 0
                for ((_, value) in productViewModel.selectedVariantOptionsIdMap) {
                    if (value in productChild.optionsId) {
                        matchOptionId++
                    }
                }
                if (matchOptionId == productViewModel.selectedVariantOptionsIdMap.size) {
                    newSelectedProductChild = productChild
                    break
                }
            }

            if (newSelectedProductChild != null) {
                for (productChild: ProductChild in productViewModel.productChildrenList) {
                    productChild.isSelected = productChild.productId == newSelectedProductChild.productId
                }
                onNeedToNotifySingleItem(fragmentViewModel.getIndex(productViewModel))

                val variantTypeViewModels = fragmentViewModel.getVariantTypeViewModel()
                for (variantTypeViewModel: TypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId == selectedOptionViewModel.variantId) {
                        variantTypeViewModel.variantSelectedValue = selectedOptionViewModel.variantName
                        onNeedToNotifySingleItem(fragmentViewModel.getIndex(variantTypeViewModel))
                        break
                    }
                }

                for (variantTypeViewModel: TypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId != selectedOptionViewModel.variantId) {
                        for (optionViewModel: OptionVariantViewModel in variantTypeViewModel.variantOptions) {

                            // Get other variant type selected option id
                            val otherVariantSelectedOptionIds = ArrayList<Int>()
                            for (otherVariantViewModel: TypeVariantViewModel in variantTypeViewModels) {
                                if (otherVariantViewModel.variantId != variantTypeViewModel.variantId &&
                                        otherVariantViewModel.variantId != selectedOptionViewModel.variantId) {
                                    for (otherVariantTypeOption: OptionVariantViewModel in otherVariantViewModel.variantOptions) {
                                        if (otherVariantTypeOption.currentState == otherVariantTypeOption.STATE_SELECTED) {
                                            otherVariantSelectedOptionIds.add(otherVariantTypeOption.optionId)
                                            break
                                        }
                                    }
                                }
                            }

                            // Look for available child
                            var hasAvailableChild = false
                            for (productChild: ProductChild in productViewModel.productChildrenList) {
                                hasAvailableChild = checkChildAvailable(productChild, optionViewModel.optionId, selectedOptionViewModel.optionId, otherVariantSelectedOptionIds)
                                if (hasAvailableChild) break
                            }

                            // Set option id state with checking result
                            if (!hasAvailableChild) {
                                optionViewModel.hasAvailableChild = false
                                optionViewModel.currentState = optionViewModel.STATE_NOT_AVAILABLE
                            } else if (optionViewModel.currentState != optionViewModel.STATE_SELECTED) {
                                optionViewModel.hasAvailableChild = true
                                optionViewModel.currentState = optionViewModel.STATE_NOT_SELECTED
                            }
                        }
                        onNeedToNotifySingleItem(fragmentViewModel.getIndex(variantTypeViewModel))
                    }
                }

                if (quantityViewModel != null) {
                    if (newSelectedProductChild.isAvailable && newSelectedProductChild.stock == 0) {
                        quantityViewModel.maxOrderQuantity = MAX_QUANTITY
                    } else {
                        quantityViewModel.maxOrderQuantity = newSelectedProductChild.stock
                    }
                    onNeedToNotifySingleItem(fragmentViewModel.getIndex(quantityViewModel))
                }
            }
            fragmentViewModel.isStateChanged = true
        }
    }

    private fun checkChildAvailable(productChild: ProductChild,
                                    optionViewModelId: Int,
                                    currentChangedOptionId: Int,
                                    otherVariantSelectedOptionIds: ArrayList<Int>): Boolean {

        // Check is child with newly selected option id, other variant selected option ids,
        // and current looping variant option id is available
        var otherSelectedOptionIdCount = 0
        for (optionId: Int in otherVariantSelectedOptionIds) {
            if (optionId in productChild.optionsId) {
                otherSelectedOptionIdCount++
            }
        }

        val otherSelectedOptionIdCountEqual = otherSelectedOptionIdCount == otherVariantSelectedOptionIds.size
        val currentChangedOptionIdAvailable = currentChangedOptionId in productChild.optionsId
        val optionViewModelIdAvailable = optionViewModelId in productChild.optionsId

        return productChild.isAvailable && currentChangedOptionIdAvailable && optionViewModelIdAvailable && otherSelectedOptionIdCountEqual
    }

    override fun onChangeQuantity(quantityViewModel: QuantityViewModel) {
        val productViewModel = fragmentViewModel.getProductViewModel()
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()

        if (fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.wholesalePriceModel?.isNotEmpty() == true) {
            val wholesalePriceModels = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.wholesalePriceModel?.asReversed()
            if (wholesalePriceModels != null) {
                var eligibleForWholesalePrice = false
                for (wholesalePriceModel: WholesalePriceModel in wholesalePriceModels) {
                    if (quantityViewModel.orderQuantity >= wholesalePriceModel.qtyMax ||
                            (quantityViewModel.orderQuantity < wholesalePriceModel.qtyMax &&
                                    quantityViewModel.orderQuantity >= wholesalePriceModel.qtyMin)) {
                        productViewModel?.productPrice = wholesalePriceModel.prdPrc
                        eligibleForWholesalePrice = true
                        break
                    }
                }
                if (!eligibleForWholesalePrice) {
                    productViewModel?.productPrice = fragmentViewModel.atcResponseModel?.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)?.productPrice
                            ?: 0
                }
            }
        }

        if (productViewModel?.productChildrenList != null && productViewModel.productChildrenList.size > 0) {
            for (productChild: ProductChild in productViewModel.productChildrenList) {
                if (productChild.isSelected) {
                    summaryViewModel?.itemPrice = productChild.productPrice.toLong() * quantityViewModel.orderQuantity
                    break
                }
            }
        } else {
            summaryViewModel?.itemPrice = productViewModel?.productPrice?.toLong()?.times(quantityViewModel.orderQuantity)
                    ?: 0
        }

        if (summaryViewModel != null) {
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
            onSummaryChanged(summaryViewModel)
        }

        onNeedToNotifySingleItem(fragmentViewModel.getIndex(quantityViewModel))
        fragmentViewModel.isStateChanged = true
    }

    override fun onChangeNote(noteViewModel: NoteViewModel) {
        if (fragmentViewModel.isStateChanged == false && noteViewModel.note.isNotEmpty()) {
            fragmentViewModel.isStateChanged = true
        }
    }

    override fun onSummaryChanged(summaryViewModel: SummaryViewModel?) {
        val totalPayment = summaryViewModel?.itemPrice?.plus(summaryViewModel.shippingPrice)?.plus(summaryViewModel.servicePrice)?.plus(summaryViewModel.insurancePrice)
        fragmentViewModel.totalPayment = totalPayment

        tv_total.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(fragmentViewModel.totalPayment
                ?: 0, false)
    }

    override fun onInsuranceCheckChanged(insuranceViewModel: InsuranceViewModel) {
        val summaryViewModel = fragmentViewModel.getSummaryViewModel()
        if (summaryViewModel != null) {
            if (insuranceViewModel.isChecked) {
                summaryViewModel.insurancePrice = insuranceViewModel.insurancePrice
                summaryViewModel.isUseInsurance = true
            } else {
                summaryViewModel.insurancePrice = 0
                summaryViewModel.isUseInsurance = false
            }

            onNeedToNotifySingleItem(fragmentViewModel.getIndex(summaryViewModel))
        }
        onNeedToNotifySingleItem(fragmentViewModel.getIndex(insuranceViewModel))
        fragmentViewModel.isStateChanged = true
    }

    override fun onNeedToValidateButtonBuyVisibility() {
        var hasError = false
        when {
            fragmentViewModel.getQuantityViewModel()?.isStateError == true -> hasError = true
        }

        renderButton(hasError)
    }

    fun renderButton(hasError: Boolean = false) {
        if (activity != null) {
            if (hasError) {
                button_buy_partial.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_disabled)
                button_buy_partial.setOnClickListener { }
            } else {
                button_buy_partial.background = ContextCompat.getDrawable(activity as Context, R.drawable.bg_button_orange_enabled)
                button_buy_partial.setOnClickListener {
                    //TODO
                }
            }
        }
    }

    override fun onNeedToRecalculateRatesAfterChangeTemplate() {
        fragmentViewModel.getProfileViewModel()?.isStateHasChangedProfile = false
    }

    override fun onNeedToUpdateOnboardingStatus() {
        setOnboardingStateHasNotShown(activity, false)
    }

    override fun onGetCompositeSubscriber() = compositeSubscription

    override fun onBindProductUpdateQuantityViewModel(productViewModel: ProductViewModel, stockWording: String) {
        // TODO need update quantity?
    }

    override fun onBindVariantGetProductViewModel(): ProductViewModel? {
        return fragmentViewModel.getProductViewModel()
    }

    override fun onBindVariantUpdateProductViewModel() {
        val productViewModel = fragmentViewModel.getProductViewModel()
        if (productViewModel != null) {
            onNeedToNotifySingleItem(fragmentViewModel.getIndex(productViewModel))
        }
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: activity?.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun finishWithError(messages: String) {
        fragmentListener.finishWithResult(messages)
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

    override fun showBottomSheetError(title: String, message: String, action: String, enableRetry: Boolean) {
        errorBottomsheets.setData(title, message, action, enableRetry)
        if (errorBottomsheets.isVisible) {
            errorBottomsheets.dismiss()
        }
        errorBottomsheets.show(fragmentManager, title)
        fragmentViewModel.isStateChanged = true
    }

    override fun showErrorNotAvailable(message: String) {
        showBottomSheetError(getString(R.string.bottomsheet_title_product_not_available), message, getString(R.string.bottomsheet_action_close), false)
        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListener {
            override fun onActionButtonClicked() {
                errorBottomsheets.dismiss()
            }
        }
        fragmentViewModel.isStateChanged = true
    }

    override fun updateFragmentViewModel(atcResponseModel: AtcResponseModel) {
        fragmentViewModel.atcResponseModel = atcResponseModel
    }

    override fun showData(viewModels: ArrayList<Visitable<*>>) {
        for (viewModel: Visitable<*> in viewModels) {
            if (viewModel is com.tokopedia.expresscheckout.view.variant.viewmodel.ProfileViewModel) {
                viewModel.isFirstTimeShowProfile = isOnboardingStateHasNotShown(activity)
                break
            }
        }
        fragmentViewModel.viewModels = viewModels
        adapter.clearAllElements()
        adapter.addDataViewModel(viewModels)
        adapter.notifyDataSetChanged()

        onSummaryChanged(fragmentViewModel.getSummaryViewModel())

        rl_bottom_action_container.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //TODO getselected variantID to be saved
        outState.putStringArrayList(EXTRA_SELECTED_VARIANT_ID, arrayListOf())
    }

}