package com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.*
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.*
import com.tokopedia.shop_showcase.shop_showcase_add.di.component.DaggerShopShowcaseAddComponent
import com.tokopedia.shop_showcase.shop_showcase_add.di.component.ShopShowcaseAddComponent
import com.tokopedia.shop_showcase.shop_showcase_add.di.modules.ShopShowcaseAddModule
import com.tokopedia.shop_showcase.shop_showcase_add.domain.mapper.AppendedProductMapper
import com.tokopedia.shop_showcase.shop_showcase_add.domain.mapper.DeletedProductMapper
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.adapter.ShopShowcaseAddAdapter
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener.ShopShowcasePreviewListener
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewmodel.ShopShowcaseAddViewModel
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.activity.ShopShowcaseProductAddActivity
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.fragment.ShopShowcaseProductAddFragment
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
        const val MAX_SHOWCASE_NAME_LENGTH = 128
        const val SELECTED_SHOWCASE_PRODUCT = "selected_product_list"
        const val EXCLUDED_SHOWCASE_PRODUCT = "excluded_product_list"
        const val NEW_APPENDED_SHOWCASE_PRODUCT = "appended_product_list"
        const val DEFAULT_SHOWCASE_ID = "0"
        const val ERROR_TOASTER = Toaster.TYPE_ERROR

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var tracking: ShopShowcaseTracking

    @Inject
    lateinit var userSession: UserSessionInterface

    private var showcaseAddAdapter: ShopShowcaseAddAdapter? = null
    private var addShopShowcaseParam = AddShopShowcaseParam()
    private var updateShopShowcaseParam = UpdateShopShowcaseParam()
    private var selectedProductListFilter = GetProductListFilter()
    private var excludedProduct: List<ShowcaseProduct> = listOf()
    private var isActionEdit: Boolean = false
    private var showcaseId: String? = DEFAULT_SHOWCASE_ID
    private var showcaseName: String? = ""
    private var appendedProductMapper = AppendedProductMapper()
    private var deletedProductMapper = DeletedProductMapper()

    private val shopShowcaseAddViewModel: ShopShowcaseAddViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopShowcaseAddViewModel::class.java)
    }

    private val shopType: String by lazy {
        tracking.getShopType(userSession)
    }

    private val shopId: String by lazy {
        userSession.shopId
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
            textFieldInput.filters = arrayOf(InputFilter.LengthFilter(MAX_SHOWCASE_NAME_LENGTH))
        }
    }

    private val tvShowcaseTitle: Typography? by lazy {
        view?.findViewById<Typography>(R.id.tv_add_showcase_title)
    }

    private val emptyStateProduct: EmptyStateUnify? by lazy {
        view?.findViewById<EmptyStateUnify>(R.id.empty_state_product_showcase)
    }

    private val headerUnify: HeaderUnify? by lazy {
        view?.findViewById<HeaderUnify>(R.id.add_showcase_toolbar)?.apply {
            // disable actiontextview "Selesai"
            actionTextView?.isEnabled = false

            setNavigationOnClickListener {
                tracking.addShowcaseClickBackButton(shopId, shopType, isActionEdit)
                activity?.onBackPressed()
            }
        }
    }

    private val loaderUnify: LoaderUnify? by lazy {
        view?.findViewById<LoaderUnify>(R.id.loader_unify_add_showcase)
    }

    private val productCounter: CardView? by lazy {
        view?.findViewById<CardView>(R.id.product_choosen_counter)
    }

    private val productChoosenImage: ImageUnify? by lazy {
        view?.findViewById<ImageUnify>(R.id.product_choosen_image)
    }

    private val productCounterText: Typography? by lazy {
        view?.findViewById<Typography>(R.id.total_selected_product_counter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
            if (isActionEdit) {
                showcaseId = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID)
                showcaseName = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME)
                selectedProductListFilter.fmenu = showcaseId
                selectedProductListFilter.perPage = 0
                getSelectedProductList(selectedProductListFilter)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == START_PRODUCT_SHOWCASE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val newSelectedProductList = data?.getParcelableArrayListExtra<ShowcaseProduct>(ShopShowcaseProductAddFragment.SHOWCASE_PRODUCT_LIST)
            val newDeletedProductList = data?.getParcelableArrayListExtra<ShowcaseProduct>(ShopShowcaseProductAddFragment.SHOWCASE_DELETED_LIST)
            updateSelectedProduct(showcaseAddAdapter, newSelectedProductList)
            updateAppendedSelectedProduct(showcaseAddAdapter, newSelectedProductList)
            updateDeletedProduct(showcaseAddAdapter, newDeletedProductList)
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
        tracking.sendScreenName()
    }

    override fun getScreenName(): String = AppScreen.ADD_SHOP_SHOWCASE_SCREEN

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

    override fun setupDeleteCounter(firstDeletedItem: ShowcaseProduct) {
        // try catch to avoid ImageUnify crash on set image to delete product counter image
        try {
            if(productChoosenImage?.context?.isValidGlideContext() == true)
                ImageHandler.LoadImage(productChoosenImage, firstDeletedItem.productImageUrl)
        } catch (e : Throwable) {}
        productCounterText?.text = context?.getString(
                R.string.deleted_product_counter_text,
                getDeletedProductSize().toString()
        )
    }

    override fun showDeleteCounter() {
        if (getDeletedProductSize() > 0) {
            productCounter?.visible()
        }
    }

    override fun hideDeleteCounter() {
        productCounter?.gone()
    }

    override fun deleteSelectedProduct(position: Int) {
        showcaseAddAdapter?.deleteSelectedProduct(position)
        tracking.addShowcaseClickDeleteButtonProductCard(shopId, shopType, isActionEdit)
        showSelectedProductList()
    }

    override fun showChooseProduct() {
        emptyStateProduct?.setImageUrl(ImageAssets.PRODUCT_EMPTY)
        emptyStateProduct?.visible()
        headerUnify?.actionTextView?.isEnabled = false
        hideSelectedProductList()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(shopShowcaseAddViewModel.createShopShowcase)
        removeObservers(shopShowcaseAddViewModel.selectedProductList)
        removeObservers(shopShowcaseAddViewModel.loaderState)
        removeObservers(shopShowcaseAddViewModel.listOfResponse)
    }

    fun onBackPressedConfirm() {
        activity?.let {
            if (isShowcaseDataChanged() && isActionEdit) {
                DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(getString(R.string.text_exit_confirm_dialog_title))
                    setDescription(getString(R.string.text_exit_confirm_dialog_description))
                    setPrimaryCTAText(getString(R.string.text_cancel_button))
                    setSecondaryCTAText(getString(R.string.text_exit_button))
                    setPrimaryCTAClickListener {
                        this.dismiss()
                    }
                    setSecondaryCTAClickListener {
                        this.dismiss()
                        it.finish()
                    }
                    show()
                }
            } else {
                it.finish()
            }
        }
    }

    private fun initView() {
        showSoftKeyboard()
        if(!isActionEdit) {
            showChooseProduct()
        }
        observeCreateShopShowcase()
        observeLoaderState()
        observeGetSelectedProductList()
        observeUpdateShopShowcase()
    }

    private fun initRecyclerView(view: View?, previewListener: ShopShowcasePreviewListener) {
        view?.findViewById<RecyclerView>(R.id.rv_showcase_add)?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            showcaseAddAdapter = ShopShowcaseAddAdapter(context, previewListener)
            adapter = showcaseAddAdapter
        }
    }

    private fun initListener() {

        /**
         * Listener for action text "Selesai" on toolbar is clicked
         */
        headerUnify?.actionTextView?.setOnClickListener {
            val showcaseName = getCurrentShowcaseName()
            validateShowcaseName(showcaseName, true)
        }

        /**
         * Listener for showcase name textfield
         */
        textFieldShowcaseName?.textFieldInput?.run {
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(view: TextView?, actionId: Int, even: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val showcaseName = getCurrentShowcaseName()
                        validateShowcaseName(showcaseName, false)
                        return true
                    }
                    return false
                }
            })

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    // no op
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // no op
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.isNotEmpty()) {
                        textFieldShowcaseName?.setError(false)
                    }
                }
            })
        }

        /**
         * Send tracker if textfield get focus after clicked,
         * not use onClickListener since this unify component have bug.
         */
        textFieldShowcaseName?.textFieldInput?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) tracking.addShowcaseClickNameField(shopId, shopType, isActionEdit)
        }

        /**
         * Listener for user click primary "Pilih Produk" button on empty state
         * it will navigate to choose ShopShowcaseProductAddActivity.kt to choose
         * product
         */
        emptyStateProduct?.setPrimaryCTAClickListener {
            tracking.addShowcaseClickAddProduct(shopId, shopType)
            showChooseProductConfirmDialog()
        }

        chooseProductText?.setOnClickListener {
            tracking.addShowcaseClickChooseProductText(shopId, shopType, isActionEdit)
            showChooseProductConfirmDialog()
        }

    }

    /**
     * Function to validate showcase name input
     */
    private fun validateShowcaseName(showcaseName: String, isSubmitEdit: Boolean) {
        if (showcaseName.isEmpty()) {
            textFieldShowcaseName?.setError(true)
            context?.resources?.getString(R.string.empty_showcase_name_text)?.let {
                textFieldShowcaseName?.setMessage(it)
            }
            // since last ux improvement choose product is shown on initial view
            // it will produce error when click "selesai" and texfield showcase name still empty
            tracking.addShowcaseIsCreatedSuccessfully(shopId, shopType, isSuccess = false)
        } else {
            hideSoftKeyboard()
            if (showcaseAddAdapter?.getSelectedProductList()?.size == 0) {
                textFieldShowcaseName?.setError(false)
                showSelectedProductList()
            } else {
                addShopShowcaseParam.name = showcaseName
                if (!isActionEdit) {
                    showcaseAddAdapter?.getSelectedProductList()?.map {
                        if (it is ShowcaseProduct) {
                            addShopShowcaseParam.productIDs.add(it.productId)
                        }
                    }
                    createShopShowcase(addShopShowcaseParam)
                } else {
                    if (isSubmitEdit) {
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
        intent.putParcelableArrayListExtra(EXCLUDED_SHOWCASE_PRODUCT, ArrayList(excludedProduct))
        intent.putParcelableArrayListExtra(NEW_APPENDED_SHOWCASE_PRODUCT, showcaseAddAdapter?.getAppendedProductList())
        intent.putExtra(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT, isActionEdit)
        intent.putExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID, showcaseId)
        startActivityForResult(intent, START_PRODUCT_SHOWCASE_ACTIVITY)
    }

    private fun showChooseProductConfirmDialog() {
        activity?.also {
            if (getDeletedProductSize() > 0 && productCounter?.visibility == View.VISIBLE) {
                DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(getString(R.string.text_confirm_dialog_title))
                    setDescription(getString(R.string.text_confirm_dialog_description))
                    setPrimaryCTAText(getString(R.string.text_cancel_button))
                    setPrimaryCTAClickListener {
                        this.dismiss()
                    }
                    setSecondaryCTAText(getString(R.string.text_agree_button))
                    setSecondaryCTAClickListener {
                        this.dismiss()
                        showcaseAddAdapter?.undoDeleteSelectedProduct()
                        goToChooseProduct()
                        showSelectedProductList()
                    }
                    show()
                }
            } else {
                goToChooseProduct()
            }
        }
    }

    private fun showSelectedProductList() {
        showcaseAddAdapter?.itemCount?.let {
            if (it > 0) {
                selectedProductText?.visible()
                chooseProductText?.visible()
                selectedProductListRecyclerView?.visible()
                emptyStateProduct?.invisible()
                headerUnify?.actionTextView?.isEnabled = true
            } else {
                showChooseProduct()
            }
        }
    }

    private fun hideSelectedProductList() {
        selectedProductText?.gone()
        chooseProductText?.gone()
        selectedProductListRecyclerView?.gone()
    }

    private fun showLoader() {
        loaderUnify?.visible()
        tvShowcaseTitle?.gone()
        textFieldShowcaseName?.gone()
        selectedProductText?.gone()
        chooseProductText?.gone()
        selectedProductListRecyclerView?.gone()
        headerUnify?.actionTextView?.gone()
        hideDeleteCounter()
    }

    private fun hideLoader() {
        loaderUnify?.gone()
        tvShowcaseTitle?.visible()
        chooseProductText?.visible()
        selectedProductText?.visible()
        selectedProductListRecyclerView?.visible()
        textFieldShowcaseName?.visible()
        headerUnify?.actionTextView?.visible()
        showDeleteCounter()
    }

    private fun setCurrentlyShowcaseData(showcaseName: String?) {
        loaderUnify?.gone()
        tvShowcaseTitle?.visible()
        textFieldShowcaseName?.visible()
        textFieldShowcaseName?.textFieldInput?.setText(showcaseName)
        textFieldShowcaseName?.setMessage(resources.getString(R.string.showcase_name_hint))
    }

    private fun showUnifyToaster(message: String) {
        view?.run {
            Toaster.make(this, message, Snackbar.LENGTH_SHORT, ERROR_TOASTER)
        }
    }

    private fun observeCreateShopShowcase() {
        observe(shopShowcaseAddViewModel.createShopShowcase) {
            when (it) {
                is Success -> {
                    val responseData = it.data
                    if (responseData.success) {
                        // navigate back to origin create showcase entry point
                        tracking.addShowcaseIsCreatedSuccessfully(shopId, shopType, true)
                        activity?.setResult(Activity.RESULT_OK)
                        activity?.finish()
                    } else {
                        tracking.addShowcaseIsCreatedSuccessfully(shopId, shopType)
                        showUnifyToaster(responseData.message)
                    }
                }
            }
        }
    }

    private fun observeUpdateShopShowcase() {
        observe(shopShowcaseAddViewModel.listOfResponse) {
            val responseList = it
            if (responseList.size > 2) {

                val updateShowcaseNameResult = responseList[0] as Result<UpdateShopShowcaseResponse>
                val appendShowcaseProductResult = responseList[1] as Result<AppendShowcaseProductResponse>
                val removeShowcaseProductResult = responseList[2] as Result<RemoveShowcaseProductResponse>

                if (updateShowcaseNameResult is Success) {

                    if(updateShowcaseNameResult.data.success == true) {

                        if (appendShowcaseProductResult is Success) {

                            // check if append new showcase product is success
                            if (appendShowcaseProductResult.data.status) {

                                if (removeShowcaseProductResult is Success) {

                                    // check if remove showcase product is success
                                    if (removeShowcaseProductResult.data.status) {

                                        // everything is fine, navigate back to showcase list
                                        val intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                                        intent.putExtra(ShopShowcaseParamConstant.EXTRA_EDIT_SHOWCASE_RESULT, SUCCESS_EDIT_SHOWCASE)
                                        activity?.setResult(Activity.RESULT_OK, intent)
                                        activity?.finish()

                                    } else {
                                        // Show error remove showcase product
                                        showUnifyToaster(removeShowcaseProductResult.data.header.reason)
                                    }

                                }

                            } else {
                                // Show error append new showcase product
                                showUnifyToaster(appendShowcaseProductResult.data.header.reason)
                            }
                        }

                    } else {
                        // Show error update name failed
                        showUnifyToaster(updateShowcaseNameResult.data.message ?: getString(R.string.error_happens))
                    }
                } else {
                    // Show error use case Fail result
                    (updateShowcaseNameResult as Fail).throwable.message?.let { message -> showUnifyToaster(message) }
                }
            }
        }
    }

    private fun observeGetSelectedProductList() {
        observe(shopShowcaseAddViewModel.selectedProductList) {
            when (it) {
                is Success -> {
                    excludedProduct = it.data
                    setCurrentlyShowcaseData(showcaseName)
                    updateSelectedProduct(showcaseAddAdapter, ArrayList(it.data))
                    showSelectedProductList()
                }
            }
        }
    }

    private fun observeLoaderState() {
        observe(shopShowcaseAddViewModel.loaderState) {
            if (it) showLoader()
            else hideLoader()
        }
    }

    private fun createShopShowcase(addShopShowcase: AddShopShowcaseParam) {
        shopShowcaseAddViewModel.addShopShowcase(addShopShowcase)
    }

    private fun updateShopShowcase(updateShopShowcaseParam: UpdateShopShowcaseParam) {
        val appendShowcaseProductParam = AppendShowcaseProductParam()
        val removeShowcaseProductParam = RemoveShowcaseProductParam()

        val appendedProductList = showcaseAddAdapter?.getAppendedProductList()?.let {
            ArrayList(appendedProductMapper.mapToGqlModel(it, showcaseId))
        }

        val removedProductList = showcaseAddAdapter?.getDeletedProductList()?.let {
            ArrayList(deletedProductMapper.mapToGqlModel(it, showcaseId))
        }

        appendedProductList?.let { appendShowcaseProductParam.listAppended = it }
        removedProductList?.let { removeShowcaseProductParam.listRemoved = it }
        shopShowcaseAddViewModel.updateShopShowcase(updateShopShowcaseParam, appendShowcaseProductParam, removeShowcaseProductParam)
    }

    private fun getSelectedProductList(filter: GetProductListFilter) {
        shopShowcaseAddViewModel.getSelectedProductList(filter)
    }

    private fun updateSelectedProduct(showcaseAddAdapter: ShopShowcaseAddAdapter?, selectedProductList: ArrayList<ShowcaseProduct>?) {
        showcaseAddAdapter?.updateSelectedDataSet(selectedProductList)
    }

    private fun updateAppendedSelectedProduct(showcaseAddAdapter: ShopShowcaseAddAdapter?, newSelectedProductList: ArrayList<ShowcaseProduct>?) {
        showcaseAddAdapter?.updateAppendedDataSet(newSelectedProductList)
    }

    private fun updateDeletedProduct(showcaseAddAdapter: ShopShowcaseAddAdapter?, newDeletedProduct: ArrayList<ShowcaseProduct>?) {
        showcaseAddAdapter?.updateDeletedDataSet(newDeletedProduct)
    }

    private fun getDeletedProductSize(): Int {
        return showcaseAddAdapter?.getDeletedProductList()?.size ?: 0
    }

    private fun getAppendedProductSize(): Int {
        return showcaseAddAdapter?.getAppendedProductList()?.size ?: 0
    }

    private fun getCurrentShowcaseName(): String {
        return textFieldShowcaseName?.textFieldInput?.text.toString()
    }

    private fun isShowcaseDataChanged(): Boolean {
        return getDeletedProductSize() > 0 || getAppendedProductSize() > 0 || getCurrentShowcaseName() != showcaseName
    }

    private fun showSoftKeyboard() {
        activity?.window?.run {
            if (!isActionEdit) {
                textFieldShowcaseName?.textFieldInput?.requestFocus()
                setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            }
        }
    }

    private fun hideSoftKeyboard() {
        KeyboardHandler.hideSoftKeyboard(activity)
        textFieldShowcaseName?.textFieldInput?.clearFocus()
    }

}