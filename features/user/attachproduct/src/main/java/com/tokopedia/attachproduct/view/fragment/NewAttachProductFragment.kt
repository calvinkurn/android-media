package com.tokopedia.attachproduct.view.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.ApplinkConst.AttachProduct
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.analytics.NewAttachProductAnalytics
import com.tokopedia.attachproduct.di.NewAttachProductModule
import com.tokopedia.attachproduct.di.DaggerNewAttachProductComponent
import com.tokopedia.attachproduct.view.activity.NewAttachProductActivity
import com.tokopedia.attachproduct.view.adapter.NewAttachProductListAdapter
import com.tokopedia.attachproduct.view.adapter.NewAttachProductListAdapterTypeFactory
import com.tokopedia.attachproduct.view.presenter.NewAttachProductContract
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel
import com.tokopedia.attachproduct.view.viewholder.NewCheckableInteractionListenerWithPreCheckedAction
import com.tokopedia.attachproduct.view.viewmodel.AttachProductViewModel
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject

/**
 * Created by Hendri on 13/02/18.
 */
class NewAttachProductFragment : BaseListFragment<NewAttachProductItemUiModel, NewAttachProductListAdapterTypeFactory>(), NewCheckableInteractionListenerWithPreCheckedAction, NewAttachProductContract.View {
    private lateinit var sendButton: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchBar: SearchBarUnify

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AttachProductViewModel::class.java)
    }

    private var activityContract: NewAttachProductContract.Activity? = null
    protected val adapter by lazy { NewAttachProductListAdapter(adapterTypeFactory) }
    private var isSeller = false
    private var source = ""
    private var shopId = ""
    private var warehouseId = "0"
    private var maxChecked = NewAttachProductActivity.MAX_CHECKED_DEFAULT
    private var hiddenProducts: ArrayList<String>? = ArrayList()

    fun setActivityContract(activityContract: NewAttachProductContract.Activity?) {
        this.activityContract = activityContract
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
            DaggerNewAttachProductComponent.builder().newAttachProductModule(NewAttachProductModule(requireContext())).baseAppComponent(
            (requireActivity().application as BaseMainApplication).baseAppComponent
        ).build().inject(this)
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initSearchBar()
        shopId = arguments?.getString(SHOP_ID, "") ?: ""
        setupWarehouseId()
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            isSeller = savedInstanceState.getBoolean(IS_SELLER, false)
            shopId = savedInstanceState.getString(SHOP_ID, "")
            source = savedInstanceState.getString(SOURCE, "")
            maxChecked = savedInstanceState.getInt(MAX_CHECKED, NewAttachProductActivity.MAX_CHECKED_DEFAULT)
            hiddenProducts = savedInstanceState.getStringArrayList(HIDDEN_PRODUCTS)
        } else if (arguments != null) {
            isSeller = requireArguments().getBoolean(IS_SELLER, false)
            source = requireArguments().getString(SOURCE, "")
            maxChecked = requireArguments().getInt(MAX_CHECKED, NewAttachProductActivity.MAX_CHECKED_DEFAULT)
            hiddenProducts = requireArguments().getStringArrayList(HIDDEN_PRODUCTS)
        }
        updateButtonBasedOnChecked(adapter.checkedCount)
        initObserver()
    }

    private fun setupWarehouseId() {
        if (arguments != null) {
            warehouseId = requireArguments().getString(
                AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID, "0"
            )
        }
    }

    private fun initSearchBar() {
        searchBar = requireActivity().findViewById(R.id.search_input_view)
        searchBar.searchBarTextField.addTextChangedListener(object : TextWatcher {
            var timer: Timer? = null
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        val mainHandler = Handler(searchBar.getContext().mainLooper)
                        val myRunnable = Runnable { onSearchTextChanged(s.toString()) }
                        mainHandler.post(myRunnable)
                    }
                }, 300)
            }
        })
        searchBar.searchBarTextField.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchBar.clearFocus()
                val `in` = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                `in`.hideSoftInputFromWindow(searchBar.getWindowToken(), 0)

                onSearchSubmitted()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SELLER, isSeller)
        outState.putString(SOURCE, source)
        outState.putInt(MAX_CHECKED, maxChecked)
        outState.putString(SHOP_ID, shopId)
        outState.putStringArrayList(HIDDEN_PRODUCTS, hiddenProducts)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_attach_product, container, false)
        sendButton = view.findViewById(R.id.send_button_attach_product)
        sendButton.setOnClickListener { sendButtonClicked() }
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener{
            if (searchBar.searchBarTextField.text.toString().isEmpty()) {
                viewModel.clearCache()
            }
            loadInitialData()
        }
        updateButtonBasedOnChecked(0)
        return view
    }

    override fun onStart() {
        super.onStart()
        if (activityContract == null) {
            if (activity is NewAttachProductContract.Activity) {
                activityContract = activity as NewAttachProductContract.Activity?
            }
        }
    }

    private fun onSearchSubmitted() {
        KeyboardHandler.DropKeyboard(activity, view)
        if (searchBar.searchBarTextField.text.toString().isNotEmpty()) {
            loadInitialData()
        }
    }

    private fun onSearchTextChanged(text: String) {
        if (text.isEmpty()) {
            adapter.clearAllElements()
            recyclerViewLayoutManager.scrollToPosition(0)
            addProductToList(viewModel.cacheList, viewModel.cacheHasNext)
        }
    }

    private fun initObserver() {
        viewModel.products.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    hideAllLoadingIndicator()
                    var hasNext = false
                    var listData = result.data.toMutableList()
                    if (listData.size >= AttachProductViewModel.DEFAULT_ROWS) {
                        hasNext = true
                        listData.removeAt(result.data.size - 1)
                    }
                    addProductToList(listData, hasNext)
                    if (result.data.isNotEmpty()) {
                        setShopName(listData.first().shopName)
                    }
                }
                is Fail -> {
                    showErrorMessage(result.throwable)
                }
            }
        })

        viewModel.checkedList.observe(viewLifecycleOwner, { result ->
            updateButtonBasedOnChecked(result.size)
        })
    }

    override fun onItemClicked(newAttachProductItemUiModel: NewAttachProductItemUiModel) {}
    override fun loadInitialData() {
        adapter.clearAllElements()
        recyclerViewLayoutManager.scrollToPosition(0)
        super.loadInitialData()
    }

    override fun loadData(page: Int) {
        if (activityContract != null) {
            if (searchBar.searchBarTextField.text.toString().isEmpty()
                    && viewModel.cacheList.isNotEmpty()) {
                val page = (viewModel.cacheList.size / 10) + 1
                viewModel.loadProductData(
                    "",
                    shopId,
                    page, warehouseId
                )
            }
            else {
                viewModel.loadProductData(
                        searchBar.searchBarTextField.text.toString(),
                shopId,
                page, warehouseId
                )
            }
        } else if (activity != null) {
            requireActivity().finish()
        }
    }

    override fun getDefaultInitialPage(): Int {
        return 1
    }

    override fun createAdapterInstance(): BaseListAdapter<NewAttachProductItemUiModel, NewAttachProductListAdapterTypeFactory> {
        return adapter
    }

    override fun getAdapter(): BaseListAdapter<NewAttachProductItemUiModel, NewAttachProductListAdapterTypeFactory> {
        return adapter
    }

    override fun getAdapterTypeFactory(): NewAttachProductListAdapterTypeFactory {
        return NewAttachProductListAdapterTypeFactory(this)
    }

    override fun shouldAllowCheckChange(position: Int, checked: Boolean): Boolean {
        val isCurrentlyChecked = isChecked(position)
        val willCheckedStatusChanged = isCurrentlyChecked xor checked
        return if (adapter.checkedCount >= maxChecked && willCheckedStatusChanged && !isCurrentlyChecked) {
            val message = getString(R.string.string_attach_product_warning_max_product_format, maxChecked.toString())
            NetworkErrorHelper.showSnackbar(activity, message)
            false
        } else {
            true
        }
    }

    override fun isChecked(position: Int): Boolean {
        return adapter.isChecked(position)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        adapter.itemChecked(isChecked, position)
        viewModel.updateCheckedList(adapter.checkedDataList)
        if (position != RecyclerView.NO_POSITION) {
            trackAction(source, adapter.data[position].productId)
        }
    }

    override fun addProductToList(productNews: List<NewAttachProductItemUiModel>, hasNextPage: Boolean) {
        if (productNews.isNotEmpty()) {
            sendButton.visibility = View.VISIBLE
        } else {
            sendButton.visibility = View.VISIBLE
        }
        removeHiddenProducts(productNews.toMutableList())
        renderList(productNews, hasNextPage)
    }

    override fun hideAllLoadingIndicator() {
        hideLoading()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showErrorMessage(throwable: Throwable) {
        throwable.printStackTrace()
        NetworkErrorHelper.showSnackbar(activity)
    }

    override fun updateButtonBasedOnChecked(checkedCount: Int) {
        sendButton.text = getString(R.string.string_attach_product_send_button_text, checkedCount.toString(), maxChecked.toString())
        sendButton.isEnabled = checkedCount in 1..maxChecked
    }

    private fun sendButtonClicked() {
        viewModel.completeSelection { resultProduct ->
            activityContract?.finishActivityWithResult(resultProduct)
        }
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyResultViewModel = EmptyResultViewModel()
        if (TextUtils.isEmpty(searchBar.searchBarTextField.text)) {
            if (isSeller) {
                emptyResultViewModel.content = getString(R.string.string_attach_product_my_empty_product)
            } else {
                emptyResultViewModel.content = getString(R.string.string_attach_product_empty_product)
            }
            emptyResultViewModel.iconRes = R.drawable.bg_attach_product_empty_result
        } else {
            emptyResultViewModel.content = getString(R.string.string_attach_product_search_not_found)
            emptyResultViewModel.iconRes = R.drawable.ic_attach_product_empty_search
        }
        if (isSeller) {
            emptyResultViewModel.buttonTitleRes = R.string.string_attach_product_add_product_now
            emptyResultViewModel.callback = object : BaseEmptyViewHolder.Callback {
                override fun onEmptyContentItemTextClicked() {}
                override fun onEmptyButtonClicked() {
                    addProductClicked()
                }
            }
        }
        return emptyResultViewModel
    }

    fun addProductClicked() {
        activityContract!!.goToAddProduct(shopId)
    }

    private fun removeHiddenProducts(productNews: MutableList<NewAttachProductItemUiModel>) {
        if (hiddenProducts == null) {
            return
        }
        val iterator = productNews.iterator()
        while (iterator.hasNext()) {
            val product = iterator.next()
            var shouldHide = false
            for (hiddenProduct in hiddenProducts!!) {
                if (TextUtils.equals(product.productId.toString(), hiddenProduct)) {
                    shouldHide = true
                    break
                }
            }
            if (shouldHide) {
                iterator.remove()
            }
        }
    }

    private fun trackAction(source: String, productId: String) {
        if (source == NewAttachProductActivity.SOURCE_TALK) {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                NewAttachProductAnalytics.getEventCheckProductTalk(productId).event
            )
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                NewAttachProductAnalytics.eventCheckProduct.event
            )
        }
    }

    override fun setShopName(shopName: String) {
        activityContract!!.setShopName(shopName)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val IS_SELLER = "isSeller"
        private const val SOURCE = "source"
        private const val MAX_CHECKED = "max_checked"
        private const val HIDDEN_PRODUCTS = "hidden_products"
        private const val SHOP_ID = "shop_id"

        @JvmStatic
        fun newInstance(
            checkedUIView: NewAttachProductContract.Activity?,
            isSeller: Boolean, source: String?, maxChecked: Int,
            hiddenProducts: ArrayList<String>?, warehouseId: String?, shopId: String
        ): NewAttachProductFragment {
            val args = Bundle()
            args.putString(SHOP_ID, shopId)
            args.putBoolean(IS_SELLER, isSeller)
            args.putString(SOURCE, source)
            args.putString(AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_WAREHOUSE_ID, warehouseId)
            args.putInt(MAX_CHECKED, maxChecked)
            args.putStringArrayList(HIDDEN_PRODUCTS, hiddenProducts)
            val fragment = NewAttachProductFragment()
            fragment.setActivityContract(checkedUIView)
            fragment.arguments = args
            return fragment
        }
    }
}