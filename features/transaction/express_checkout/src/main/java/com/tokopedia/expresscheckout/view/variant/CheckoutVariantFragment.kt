package com.tokopedia.expresscheckout.view.variant

import android.content.Context
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
import com.tokopedia.expresscheckout.view.variant.adapter.CheckoutVariantAdapterTypefactory
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantProductViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.ProductChild
import android.support.v7.widget.SimpleItemAnimator
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantOptionVariantViewModel
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantTypeVariantViewModel
import com.tokopedia.transactiondata.entity.response.variantdata.Option

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantFragment : BaseListFragment<Visitable<*>, CheckoutVariantAdapterTypefactory>(),
        CheckoutVariantContract.View, CheckoutVariantActionListener {

    val contextView: Context get() = activity!!
    lateinit var list: List<Visitable<*>>
    lateinit var presenter: CheckoutVariantPresenter
    lateinit var adapter: CheckoutVariantAdapter
    lateinit var recyclerView: RecyclerView
    var isDataLoaded = false

    companion object {
        fun createInstance(): CheckoutVariantFragment {
            return CheckoutVariantFragment()
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
        return view
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun getAdapterTypeFactory(): CheckoutVariantAdapterTypefactory {
        return CheckoutVariantAdapterTypefactory(this)
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

    override fun onChangeVariant(variantId: Int, checkoutVariantOptionVariantViewModel: CheckoutVariantOptionVariantViewModel) {
        var checkoutVariantProductViewModel = adapter.getProductDataViewModel()
        if (checkoutVariantProductViewModel != null && checkoutVariantProductViewModel.productChildrenList.isNotEmpty()) {
            var selectedKey = 0
            for ((key, value) in checkoutVariantProductViewModel.selectedVariantOptionsIdMap) {
                if (key == variantId && value != checkoutVariantOptionVariantViewModel.optionId) {
                    selectedKey = key
                }
            }
            if (selectedKey != 0) {
                checkoutVariantProductViewModel.selectedVariantOptionsIdMap.put(selectedKey, checkoutVariantOptionVariantViewModel.optionId)
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
                for (variantTypeViewModel: CheckoutVariantTypeVariantViewModel in variantTypeViewModels) {
                    if (variantTypeViewModel.variantId == variantId) {
                        variantTypeViewModel.variantSelectedValue = checkoutVariantOptionVariantViewModel.variantName
                        adapter.notifyItemChanged(adapter.getIndex(variantTypeViewModel))
                    } else {
                        for (variantOptionViewModel: CheckoutVariantOptionVariantViewModel in variantTypeViewModel.variantOptions) {
                            var hasAvailableChild = false
                            for (productChild: ProductChild in checkoutVariantProductViewModel.productChildrenList) {
                                if (productChild.isAvailable && variantOptionViewModel.optionId in productChild.optionsId &&
                                        checkoutVariantOptionVariantViewModel.optionId in productChild.optionsId) {
                                    hasAvailableChild = true
                                    break
                                }
                            }
                            if (!hasAvailableChild) {
                                variantOptionViewModel.hasAvailableChild = false
                                variantOptionViewModel.currentState == variantOptionViewModel.STATE_NOT_AVAILABLE
                            } else {
                                variantOptionViewModel.hasAvailableChild = true
                                variantOptionViewModel.currentState == variantOptionViewModel.STATE_NOT_SELECTED
                            }
                        }
                        adapter.notifyItemChanged(adapter.getIndex(variantTypeViewModel))
                    }
                }
            }

        }
    }

    override fun onBindProductUpdateQuantityViewModel(stockWording: String) {
        var quantityViewModel = adapter.getQuantityViewModel()
        if (quantityViewModel != null) {
            quantityViewModel.availableStock = stockWording
            if (recyclerView.isComputingLayout) {
                recyclerView.post {
                    adapter.notifyItemChanged(adapter.getIndex(quantityViewModel))
                }
            } else {
                adapter.notifyItemChanged(adapter.getIndex(quantityViewModel))
            }
        }
    }

    override fun onBindVariantGetProductViewModel(): CheckoutVariantProductViewModel? {
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

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {

    }

    override fun loadData(page: Int) {
        if (!isDataLoaded) {
            presenter.loadData()
        }
    }

    override fun showToasterError(message: String?) {
        ToasterError.make(view, message
                ?: contextView.getString(R.string.default_request_error_unknown), Snackbar.LENGTH_LONG).show()
    }

    override fun showData(arrayList: ArrayList<Visitable<*>>) {
        hideLoading()
        adapter.clearAllElements()
        adapter.addDataViewModel(arrayList)
        adapter.notifyDataSetChanged()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, CheckoutVariantAdapterTypefactory> {
        adapter = CheckoutVariantAdapter(adapterTypeFactory)
        return adapter
    }

    override fun getActivityContext(): Context? {
        return activity
    }
}