package com.tokopedia.expresscheckout.view.variant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.Tooltip
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapter
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypeFactory
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProductViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProductChild
import android.support.v7.widget.SimpleItemAnimator
import com.tokopedia.expresscheckout.view.errorview.ErrorBottomsheets
import com.tokopedia.expresscheckout.view.errorview.ErrorBottomsheetsActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.OptionVariantViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.TypeVariantViewModel
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequest

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypeFactory>(),
        CheckoutVariantContract.View, CheckoutVariantActionListener, ErrorBottomsheetsActionListener {

    val contextView: Context get() = activity!!
    lateinit var list: List<Visitable<*>>
    lateinit var presenter: CheckoutVariantPresenter
    lateinit var adapter: CheckoutVariantAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var errorBottomSheets: ErrorBottomsheets
    lateinit var fragmentListener: CheckoutVariantFragmentListener
    var isDataLoaded = false

    companion object {
        val ARGUMENT_ATC_REQUEST = "ARGUMENT_ATC_REQUEST"

        fun createInstance(atcRequest: AtcRequest): CheckoutVariantFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARGUMENT_ATC_REQUEST, atcRequest)
            val fragment = CheckoutVariantFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = CheckoutVariantPresenter()
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product_page, container, false)
        recyclerView = getRecyclerView(view)
        recyclerView.addItemDecoration(CheckoutVariantItemDecorator())
        (recyclerView.getItemAnimator() as SimpleItemAnimator).supportsChangeAnimations = false

        errorBottomSheets = ErrorBottomsheets()
        errorBottomSheets.actionListener = this

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentListener = context as CheckoutVariantFragmentListener
    }

    override fun showLoading() {
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypeFactory {
        return CheckoutVariantAdapterTypeFactory(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {

    }

    override fun onNeedToNotifySingleItem(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun onNeedToNotifyAllItem() {
        adapter.notifyDataSetChanged()
    }

    override fun onClickEditProfile() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickEditDuration() {

    }

    override fun onClickEditCourier() {

    }

    override fun onClickInsuranceInfo(insuranceInfo: String) {
        if (activity != null) {
            val tooltip = Tooltip(contextView)
            tooltip.setTitle(contextView.getString(R.string.title_bottomsheet_insurance))
            tooltip.setDesc(insuranceInfo)
            tooltip.setTextButton(contextView.getString(R.string.label_button_bottomsheet_close))
            tooltip.setIcon(R.drawable.ic_insurance)
            tooltip.btnAction.setOnClickListener {
                tooltip.dismiss()
            }
            tooltip.show()
        }
    }

    override fun onChangeVariant(selectedOptionViewModel: OptionVariantViewModel) {
        var checkoutVariantProductViewModel = adapter.getProductDataViewModel()
        if (checkoutVariantProductViewModel != null && checkoutVariantProductViewModel.productChildrenList.isNotEmpty()) {
            var selectedKey = 0
            for ((key, value) in checkoutVariantProductViewModel.selectedVariantOptionsIdMap) {
                if (key == selectedOptionViewModel.variantId && value != selectedOptionViewModel.optionId) {
                    selectedKey = key
                }
            }
            if (selectedKey != 0) {
                checkoutVariantProductViewModel.selectedVariantOptionsIdMap.put(selectedKey, selectedOptionViewModel.optionId)
            }

            // Check is product child for selected variant is available
            var newSelectedProductChild: ProductChild? = null
            for (productChild: ProductChild in checkoutVariantProductViewModel.productChildrenList) {
                var matchOptionId = 0
                for ((key, value) in checkoutVariantProductViewModel.selectedVariantOptionsIdMap) {
                    if (value in productChild.optionsId) {
                        matchOptionId++
                    }
                }
                if (matchOptionId == checkoutVariantProductViewModel.selectedVariantOptionsIdMap.size) {
                    newSelectedProductChild = productChild
                    break
                }
            }

            if (newSelectedProductChild != null) {
                for (productChild: ProductChild in checkoutVariantProductViewModel.productChildrenList) {
                    productChild.isSelected = productChild.productId == newSelectedProductChild.productId
                }
                adapter.notifyItemChanged(adapter.getIndex(checkoutVariantProductViewModel))

                var variantTypeViewModels = adapter.getVariantTypeViewModel()
                for (variantTypeViewModel: TypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId == selectedOptionViewModel.variantId) {
                        variantTypeViewModel.variantSelectedValue = selectedOptionViewModel.variantName
                        adapter.notifyItemChanged(adapter.getIndex(variantTypeViewModel))
                        break
                    }
                }

                for (variantTypeViewModel: TypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId != selectedOptionViewModel.variantId) {
                        for (optionViewModel: OptionVariantViewModel in variantTypeViewModel.variantOptions) {

                            // Get other variant type selected option id
                            var otherVariantSelectedOptionIds = ArrayList<Int>()
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
                            for (productChild: ProductChild in checkoutVariantProductViewModel.productChildrenList) {
                                hasAvailableChild = checkChildAvailable(productChild, optionViewModel.optionId, selectedOptionViewModel.optionId, otherVariantSelectedOptionIds)
                                if (hasAvailableChild) break
                            }

                            // Set option id state with checking result
                            if (!hasAvailableChild) {
                                optionViewModel.hasAvailableChild = false
                                optionViewModel.currentState == optionViewModel.STATE_NOT_AVAILABLE
                            } else if (optionViewModel.currentState != optionViewModel.STATE_SELECTED) {
                                optionViewModel.hasAvailableChild = true
                                optionViewModel.currentState == optionViewModel.STATE_NOT_SELECTED
                            }
                        }
                        adapter.notifyItemChanged(adapter.getIndex(variantTypeViewModel))
                    }
                }
            }
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

    override fun onBindProductUpdateQuantityViewModel(stockWording: String) {
        var quantityViewModel = adapter.getQuantityViewModel()
        if (quantityViewModel != null) {
            quantityViewModel.stockWording = stockWording
            if (recyclerView.isComputingLayout) {
                recyclerView.post {
                    adapter.notifyItemChanged(adapter.getIndex(quantityViewModel))
                }
            } else {
                adapter.notifyItemChanged(adapter.getIndex(quantityViewModel))
            }
        }
    }

    override fun onBindVariantGetProductViewModel(): ProductViewModel? {
        return adapter.getProductDataViewModel()
    }

    override fun onBindVariantUpdateProductViewModel() {
        val productViewModel = adapter.getProductDataViewModel()
        if (productViewModel != null) {
            if (recyclerView.isComputingLayout) {
                recyclerView.post {
                    adapter.notifyItemChanged(adapter.getIndex(productViewModel))
                }
            } else {
                adapter.notifyItemChanged(adapter.getIndex(productViewModel))
            }
        }
    }

    override fun onActionButtonClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {

    }

    override fun loadData(page: Int) {
        if (!isDataLoaded) {
            presenter.loadExpressCheckoutData(arguments?.get(ARGUMENT_ATC_REQUEST) as AtcRequest)
        }
    }

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: contextView.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun finishWithError(messages: String) {
        fragmentListener.finishWithResult(messages)
    }

    override fun showBottomsheetError(title: String, message: String, action: String) {
        errorBottomSheets.setError(title, message, action)
        if (errorBottomSheets.isVisible) {
            errorBottomSheets.dismiss()
        }
        errorBottomSheets.show(fragmentManager, title)
    }

    override fun showData(arrayList: ArrayList<Visitable<*>>) {
        hideLoading()
        adapter.clearAllElements()
        adapter.addDataViewModel(arrayList)
        adapter.notifyDataSetChanged()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypeFactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getActivityContext(): Context? {
        return activity
    }
}