package com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.AppScreen
import com.tokopedia.shop_showcase.common.ImageAssets
import com.tokopedia.shop_showcase.common.ShopShowcaseEditParam
import com.tokopedia.shop_showcase.common.ShopShowcaseListParam
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.*
import com.tokopedia.shop_showcase.shop_showcase_add.di.component.DaggerShopShowcaseAddComponent
import com.tokopedia.shop_showcase.shop_showcase_add.di.component.ShopShowcaseAddComponent
import com.tokopedia.shop_showcase.shop_showcase_add.di.modules.ShopShowcaseAddModule
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter.ShopShowcaseAddAdapter
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewmodel.ShopShowcaseAddViewModel
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.activity.ShopShowcaseProductAddActivity
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddFragment
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.EmptyState
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopShowcaseAddFragment : BaseDaggerFragment(), HasComponent<ShopShowcaseAddComponent>, ShopShowcasePreviewListener {

    companion object {

        @JvmStatic
        fun createInstance(extraActionEdit: Boolean?, extraShowcaseId: String?, extraShowcaseName: String?): ShopShowcaseAddFragment {
            val fragment = ShopShowcaseAddFragment()
            val extraData = Bundle()
            extraActionEdit?.let { extraData.putBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT, it) }
            extraData.putString(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID, extraShowcaseId)
            extraData.putString(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME, extraShowcaseName)
            fragment.arguments = extraData
            return fragment
        }

        const val START_PRODUCT_SHOWCASE_ACTIVITY = 1
        const val SUCCESS_EDIT_SHOWCASE = 1
        const val SELECTED_SHOWCASE_PRODUCT = "selected_product_list"
        const val DEFAULT_SHOWCASE_ID = "0"
        const val ERROR_TOASTER = Toaster.TYPE_ERROR

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var showcaseAddAdapter: ShopShowcaseAddAdapter? = null
    private var addShopShowcaseParam = AddShopShowcaseParam()
    private var updateShopShowcaseParam = UpdateShopShowcaseParam()
    private var selectedProductListFilter = GetProductListFilter()
    private var viewVisible = View.VISIBLE
    private var viewGone = View.GONE
    private var selectedShowcaseProduct: ArrayList<ShowcaseProduct>? = arrayListOf()
    private var appendedShowcaseProduct: ArrayList<AppendedProduct> = arrayListOf()
    private var removedShowcaseProduct: ArrayList<RemovedProduct> = arrayListOf()
    private var isActionEdit: Boolean? = false
    private var showcaseId: String? = DEFAULT_SHOWCASE_ID
    private var showcaseName: String? = ""

    private val shopShowcaseAddViewModel: ShopShowcaseAddViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopShowcaseAddViewModel::class.java)
    }

    private val selectedProductText: Typography? by lazy {
        view?.findViewById<Typography>(R.id.tv_showcase_product)
    }

    private val chooseProductText: Typography? by lazy {
        view?.findViewById<Typography>(R.id.tv_choose_product)
    }

    private val selectedProductListRecyclerView: RecyclerView? by lazy {
        view?.findViewById<RecyclerView>(R.id.rv_showcase_add)
    }

    private val textFieldShowcaseName: TextFieldUnify? by lazy {
        view?.findViewById<TextFieldUnify>(R.id.textfield_showcase_name)?.apply {
            textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
        }
    }

    private val tvShowcaseTitle: Typography? by lazy {
        view?.findViewById<Typography>(R.id.tv_add_showcase_title)
    }

    private val emptyStateProduct: EmptyState? by lazy {
        view?.findViewById<EmptyState>(R.id.empty_state_product_showcase)
    }

    private val headerUnify: HeaderUnify? by lazy {
        view?.findViewById<HeaderUnify>(R.id.add_showcase_toolbar)?.apply {
            backButtonView?.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private val loaderUnify: LoaderUnify? by lazy {
        view?.findViewById<LoaderUnify>(R.id.loader_unify_add_showcase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
            if (isActionEdit == true) {
                showcaseId = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID)
                showcaseName = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME)
                selectedProductListFilter.fmenu = showcaseId
                getSelectedProductList(selectedProductListFilter)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == START_PRODUCT_SHOWCASE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val newSelectedProductList = data?.getParcelableArrayListExtra<ShowcaseProduct>(ShopShowcaseProductAddFragment.SHOWCASE_PRODUCT_LIST)
            newSelectedProductList?.map {
                val newAppendedProduct = AppendedProduct()
                newAppendedProduct.product_id = it.productId
                newAppendedProduct.menu_id = showcaseId
                appendedShowcaseProduct.add(newAppendedProduct)
            }
            updateSelectedProduct(showcaseAddAdapter, newSelectedProductList)
            showSelectedProductList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_showcase_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRecyclerView(view, this)
        initListener()
    }

    override fun getScreenName(): String  = AppScreen.ADD_SHOP_SHOWCASE_SCREEN

    override fun getComponent(): ShopShowcaseAddComponent? {
        return activity?.run {
            DaggerShopShowcaseAddComponent
                    .builder()
                    .shopShowcaseAddModule(ShopShowcaseAddModule())
                    .shopShowcaseComponent(ShopShowcaseInstance.getComponent(application))
                    .build()
        }
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun updateSelectedProduct(showcaseAddAdapter: ShopShowcaseAddAdapter?, selectedProductList: ArrayList<ShowcaseProduct>?) {
        showcaseAddAdapter?.updateSelectedDataSet(selectedProductList)
        selectedProductList?.let { selectedShowcaseProduct?.addAll(it) }
    }

    override fun deleteSelectedProduct(position: Int) {
        selectedShowcaseProduct?.get(position).let {
            val removedProduct = RemovedProduct()
            removedProduct.product_id = it?.productId
            removedProduct.menu_id = showcaseId
            removedShowcaseProduct.add(removedProduct)
        }
        showcaseAddAdapter?.deleteSelectedProduct(position)
        showChooseProduct()
    }

    override fun showChooseProduct() {
        if(showcaseAddAdapter?.getSelectedProductList()?.size == 0) {
            emptyStateProduct?.setImageUrl(ImageAssets.PRODUCT_EMPTY)
            emptyStateProduct?.visibility = View.VISIBLE
            headerUnify?.actionTextView?.visibility = View.GONE
            hideSelectedProductList()
        }
    }

    private fun initView() {
        observeCreateShopShowcase()
        observeLoaderState()
        observeGetSelectedProductList()
        observeUpdateShopShowcase()
    }

    private fun initRecyclerView(view: View?, previewListener: ShopShowcasePreviewListener) {
        view?.findViewById<RecyclerView>(R.id.rv_showcase_add)?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            showcaseAddAdapter = ShopShowcaseAddAdapter(context, previewListener)
            adapter = showcaseAddAdapter
        }
    }

    private fun initListener() {

        /**
         * Listener for action text "Selesai" on toolbar is clicked
         */
        headerUnify?.actionTextView?.setOnClickListener {
            val showcaseName = textFieldShowcaseName?.textFieldInput?.text.toString()
            validateShowcaseName(showcaseName, true)
        }

        /**
         * Listener for user click action done on their keyboard
         */
        textFieldShowcaseName?.textFieldInput?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, even: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    val showcaseName = textFieldShowcaseName?.textFieldInput?.text.toString()
                    validateShowcaseName(showcaseName, false)
                    return true
                }
                return false
            }
        })

        /**
         * Listener for user click primary "Pilih Produk" button on empty state
         * it will navigate to choose ShopShowcaseProductAddActivity.kt to choose
         * product
         */
        emptyStateProduct?.setPrimaryCTAClickListener {
            goToChooseProduct()
        }

        chooseProductText?.setOnClickListener {
            goToChooseProduct()
        }

    }

    /**
     * Function to validate showcase name input
     */
    private fun validateShowcaseName(showcaseName: String, isSubmitEdit: Boolean) {
        if(showcaseName.isEmpty()) {
            textFieldShowcaseName?.setError(true)
            context?.resources?.getString(R.string.empty_showcase_name_text)?.let {
                textFieldShowcaseName?.setMessage(it)
            }
        } else {
            hideSoftKeyboard()
            if(selectedShowcaseProduct?.size == 0) {
                textFieldShowcaseName?.setError(false)
                showChooseProduct()
            } else {
                addShopShowcaseParam.name = showcaseName
                if(isActionEdit == false) {
                    showcaseAddAdapter?.getSelectedProductList()?.map {
                        if(it is ShowcaseProduct) {
                            addShopShowcaseParam.productIDs.add(it.productId)
                        }
                    }
                    createShopShowcase(addShopShowcaseParam)
                } else {
                    if(isSubmitEdit) {
                        updateShopShowcaseParam.id = showcaseId
                        updateShopShowcaseParam.name = showcaseName
                        updateShopShowcase(updateShopShowcaseParam)
                    }
                }
            }
        }
    }

    private fun goToChooseProduct() {
        val intent = Intent(activity, ShopShowcaseProductAddActivity::class.java)
        intent.putParcelableArrayListExtra(SELECTED_SHOWCASE_PRODUCT, showcaseAddAdapter?.getSelectedProductList())
        startActivityForResult(intent, START_PRODUCT_SHOWCASE_ACTIVITY)
    }

    private fun showSelectedProductList() {
        showcaseAddAdapter?.itemCount?.let {
            if(it > 0) {
                selectedProductText?.visibility = View.VISIBLE
                chooseProductText?.visibility = View.VISIBLE
                selectedProductListRecyclerView?.visibility = View.VISIBLE
                emptyStateProduct?.visibility = View.GONE
                headerUnify?.actionTextView?.visibility = View.VISIBLE
            }
        }
    }

    private fun hideSelectedProductList() {
        selectedProductText?.visibility = View.GONE
        chooseProductText?.visibility = View.GONE
        selectedProductListRecyclerView?.visibility = View.GONE
    }

    private fun showLoader() {
        loaderUnify?.visibility = viewVisible
        tvShowcaseTitle?.visibility = viewGone
        textFieldShowcaseName?.visibility = viewGone
        selectedProductText?.visibility = viewGone
        chooseProductText?.visibility = viewGone
        selectedProductListRecyclerView?.visibility = viewGone
        headerUnify?.actionTextView?.visibility = viewGone
    }

    private fun hideLoader() {
        loaderUnify?.visibility = viewGone
        tvShowcaseTitle?.visibility = viewVisible
        chooseProductText?.visibility = viewVisible
        selectedProductText?.visibility = viewVisible
        selectedProductListRecyclerView?.visibility = viewVisible
        textFieldShowcaseName?.visibility = viewVisible
        headerUnify?.actionTextView?.visibility = viewVisible
    }

    private fun setCurrentlyShowcaseData(showcaseName: String?) {
        loaderUnify?.visibility = viewGone
        tvShowcaseTitle?.visibility = viewVisible
        textFieldShowcaseName?.visibility = viewVisible
        textFieldShowcaseName?.textFieldInput?.setText(showcaseName)
        textFieldShowcaseName?.setMessage(resources.getString(R.string.showcase_name_hint))
    }

    private fun showUnifyToaster(message: String, type: Int) {
        view?.run {
            Toaster.make(this, message, Snackbar.LENGTH_SHORT, type)
        }
    }

    private fun observeCreateShopShowcase() {
        observe(shopShowcaseAddViewModel.createShopShowcase) {
            when(it) {
                is Success -> {
                    val responseData = it.data
                    if(responseData.success) {
                        // navigate back to origin create showcase entry point
                        activity?.finish()
                    }
                    else {
                        showUnifyToaster(responseData.message, ERROR_TOASTER)
                    }
                }
            }
        }
    }

    private fun observeUpdateShopShowcase() {
        observe(shopShowcaseAddViewModel.listOfResponse) {
            val responseList = it
            if(responseList.size > 2) {

                val updateShowcaseNameResult = responseList[0] as Result<UpdateShopShowcaseResponse>
                val appendShowcaseProductResult = responseList[1] as Result<AppendShowcaseProductResponse>
                val removeShowcaseProductResult = responseList[2] as Result<RemoveShowcaseProductResponse>

                if(updateShowcaseNameResult is Success) {

                    // check if update showcase name is success
                    if(updateShowcaseNameResult.data.success) {

                        if(appendShowcaseProductResult is Success) {

                            // check if append new showcase product is success
                            if(appendShowcaseProductResult.data.status) {

                                if (removeShowcaseProductResult is Success) {

                                    // check if remove showcase product is success
                                    if(removeShowcaseProductResult.data.status) {

                                        // everything is fine, navigate back to showcase list
                                        val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                                        intent.putExtra(ShopShowcaseListParam.EXTRA_EDIT_SHOWCASE_RESULT, SUCCESS_EDIT_SHOWCASE)
                                        activity?.setResult(Activity.RESULT_OK, intent)
                                        activity?.finish()

                                    } else {
                                        // Show error remove showcase product
                                        showUnifyToaster(removeShowcaseProductResult.data.header.reason, ERROR_TOASTER)
                                    }

                                }

                            } else {
                                // Show error append new showcase product
                                showUnifyToaster(appendShowcaseProductResult.data.header.reason, ERROR_TOASTER)
                            }
                        }

                    } else {
                        // Show error update showcase name
                        showUnifyToaster(updateShowcaseNameResult.data.message, ERROR_TOASTER)
                    }
                }
            }
        }
    }

    private fun observeGetSelectedProductList() {
        observe(shopShowcaseAddViewModel.selectedProductList) {
            when(it) {
                is Success -> {
                    setCurrentlyShowcaseData(showcaseName)
                    updateSelectedProduct(showcaseAddAdapter, ArrayList(it.data))
                    showSelectedProductList()
                }
            }
        }
    }

    private fun observeLoaderState() {
        observe(shopShowcaseAddViewModel.loaderState) {
            if(it) showLoader()
            else {
                if(isActionEdit == false) hideLoader()
            }
        }
    }

    private fun createShopShowcase(addShopShowcase: AddShopShowcaseParam) {
        shopShowcaseAddViewModel.addShopShowcase(addShopShowcase)
    }

    private fun updateShopShowcase(updateShopShowcaseParam: UpdateShopShowcaseParam) {
        val appendShowcaseProductParam = AppendShowcaseProductParam()
        val removeShowcaseProductParam = RemoveShowcaseProductParam()
        appendShowcaseProductParam.listAppended = appendedShowcaseProduct
        removeShowcaseProductParam.listRemoved = removedShowcaseProduct
        shopShowcaseAddViewModel.updateShopShowcase(updateShopShowcaseParam, appendShowcaseProductParam, removeShowcaseProductParam)
    }

    private fun getSelectedProductList(filter: GetProductListFilter) {
        shopShowcaseAddViewModel.getSelectedProductList(filter)
    }

    private fun hideSoftKeyboard() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }
}