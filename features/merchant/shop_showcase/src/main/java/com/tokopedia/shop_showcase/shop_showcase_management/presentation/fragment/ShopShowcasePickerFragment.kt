package com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.data.model.ShowcaseItemPickerProduct
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.*
import com.tokopedia.shop_showcase.shop_showcase_add.data.model.AddShopShowcaseParam
import com.tokopedia.shop_showcase.shop_showcase_management.di.DaggerShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementModule
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity.ShopShowcaseListActivity
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter.ShopShowcasePickerAdapter
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcasePickerViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.add_showcase_bottomsheet_picker.view.*
import kotlinx.android.synthetic.main.fragment_shop_showcase_picker.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by Rafli Syam 19/09/2020
 */
class ShopShowcasePickerFragment: BaseDaggerFragment(),
        HasComponent<ShopShowcaseManagementComponent>,
        ShopShowcasePickerAdapter.PickerClickListener {

    companion object {

        @JvmStatic
        fun createInstance(
                shopId: String?,
                isMyShop: Boolean,
                shopType: String,
                pickerType: String,
                preSelectionShowcaseList: ArrayList<ShowcaseItemPicker>?,
                productId: String,
                productName: String
        ): ShopShowcasePickerFragment {
            val fragment = ShopShowcasePickerFragment()
            val extraData = Bundle()
            extraData.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
            extraData.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_MY_SHOP, isMyShop)
            extraData.putString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, shopType)
            extraData.putString(ShopShowcaseParamConstant.EXTRA_PICKER_TYPE, pickerType)
            extraData.putParcelableArrayList(ShopShowcaseParamConstant.EXTRA_PRE_SELECTED_SHOWCASE_PICKER, preSelectionShowcaseList)
            extraData.putString(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT_ID, productId)
            extraData.putString(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT_NAME, productName)
            fragment.arguments = extraData
            return fragment
        }

        private const val HEADER_HAS_ELEVATION = 16.0f
        private const val HEADER_NO_ELEVATION = 0.0f
        private const val DEFAULT_SHOWCASE_PAGE = 1
        private const val DEFAULT_SHOWCASE_NAME = "etalase"
        private const val DEFAULT_SHOWCASE_SORT = 1

    }

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val rvPicker by lazy {
        view?.findViewById<RecyclerView>(R.id.rv_list_etalase_picker)
    }

    private val rvScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    headerLayout?.cardElevation = HEADER_NO_ELEVATION
                }
                else {
                    headerLayout?.cardElevation = HEADER_HAS_ELEVATION
                }
            }
        }
    }

    private val headerLayout by lazy {
        view?.findViewById<CardView>(R.id.header_layout)
    }

    private val headerUnify by lazy {
        view?.findViewById<HeaderUnify>(R.id.showcase_picker_toolbar)?.apply {
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private val searchBar by lazy {
        view?.findViewById<SearchBarUnify>(R.id.searchbar)
    }

    private val emptyState by lazy {
        view?.findViewById<LinearLayout>(R.id.empty_state)
    }

    private val emptyStatePicker by lazy {
        view?.findViewById<EmptyStateUnify>(R.id.picker_checkbox_empty_state)
    }

    private val emptyStateHint by lazy {
        view?.findViewById<ConstraintLayout>(R.id.empty_state_picker_checkbox_hint)
    }

    private val emptyStateImage by lazy {
        view?.findViewById<ImageView>(R.id.img_empty_state)
    }

    private val tvEmptyStateHint by lazy {
        view?.findViewById<TextView>(R.id.tv_empty_state_hint)
    }

    private val savePickerButton by lazy {
        view?.findViewById<UnifyButton>(R.id.btn_picker_save)
    }

    private val addShowcaseButton by lazy {
        view?.findViewById<UnifyButton>(R.id.btn_add_etalase)
    }

    private val loaderUnify by lazy {
        view?.findViewById<LoaderUnify>(R.id.loading)
    }

    private val footer by lazy {
        view?.findViewById<CardView>(R.id.cv_picker_footer)
    }

    private val globalError by lazy {
        view?.findViewById<GlobalError>(R.id.globalError)
    }

    @Inject
    lateinit var shopShowcasePickerViewModel: ShopShowcasePickerViewModel
    private var showcasePickerAdapter: ShopShowcasePickerAdapter? = null
    private var showcaseList: List<ShopEtalaseModel> = listOf()
    private var isMyShop = false
    private var shopId: String = ""
    private var shopType: String = ""
    private var pickerType: String = ""
    private var newCreatedShowcaseId: String = ""
    private var selectedShowcase: ShowcaseItemPicker? = null
    private var selectedProduct: ShowcaseItemPickerProduct? = null
    private var _productId: String = ""
    private var _productName: String = ""
    private var selectedShowcaseList: ArrayList<ShowcaseItemPicker>? = null
    private var preSelectedShowcaseList: ArrayList<ShowcaseItemPicker>? = null
    private var addShowcaseBottomSheet: BottomSheetUnify? = null
    private var textFieldAddShowcaseBottomSheet: TextFieldUnify? = null
    private var buttonAddShowcaseBottomSheet: UnifyButton? = null
    private var showcaseNameHint: String = ""
    private var totalCheckedShowcase: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopId = it.getString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, "")
            isMyShop = it.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_MY_SHOP)
            shopType = it.getString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, "")
            _productId = it.getString(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT_ID, "")
            _productName = it.getString(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT_NAME, "")
            pickerType = it.getString(ShopShowcaseParamConstant.EXTRA_PICKER_TYPE, ShowcasePickerType.RADIO)
            if(pickerType == ShowcasePickerType.CHECKBOX) {
                selectedShowcaseList = arrayListOf()
                preSelectedShowcaseList = it.getParcelableArrayList(ShopShowcaseParamConstant.EXTRA_PRE_SELECTED_SHOWCASE_PICKER)
                if(preSelectedShowcaseList == null)
                    preSelectedShowcaseList = arrayListOf()
            }
        }

        ShowcaseItemPickerProduct().apply {
            productId = _productId
            productName = _productName
            selectedProduct = this
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_showcase_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        loadShowcaseList()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers(shopShowcasePickerViewModel.getListSellerShopShowcaseResponse)
        removeObservers(shopShowcasePickerViewModel.getShopProductResponse)
        removeObservers(shopShowcasePickerViewModel.createShopShowcase)
    }

    override fun getComponent(): ShopShowcaseManagementComponent? {
        return activity?.run {
            DaggerShopShowcaseManagementComponent
                    .builder()
                    .shopShowcaseManagementModule(ShopShowcaseManagementModule(this))
                    .shopShowcaseComponent(ShopShowcaseInstance.getComponent(application))
                    .build()
        }
    }

    override fun getScreenName(): String {
        return AppScreen.SHOP_SHOWCASE_PICKER_SCREEN
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onPickerItemClicked(item: ShopEtalaseModel, totalCheckedItem: Int) {
        val itemPicker = ShowcaseItemPicker().apply {
            showcaseId = item.id
            showcaseName = item.name
        }
        if(pickerType == ShowcasePickerType.RADIO) {
            selectedShowcase = itemPicker
        } else {
            if(item.isChecked) {
                selectedShowcaseList?.add(itemPicker)
            } else {
                selectedShowcaseList?.remove(itemPicker)
            }
            totalCheckedShowcase = totalCheckedItem
            renderButtonCounter(selectedShowcaseList?.size)
        }
    }

    override fun onPickerMaxSelectedShowcase() {
        showToaster(getString(R.string.max_selected_showcase_text), Toaster.TYPE_NORMAL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ShopShowcaseListActivity.REQUEST_CODE_ADD_ETALASE) {
            loadShowcaseList()
        }
    }

    private fun initView() {
        initRecyclerView()
        setupPickerLayout()
        loadShowcaseList()
        observeGetShowcaseList()
        observeGetTotalProduct()
        observeCreateShowcase()
    }

    private fun initListener() {
        // set listener for showcase search
        searchBar?.searchBarTextField?.addTextChangedListener(object: AfterTextWatcher() {
            override fun afterTextChanged(keyword: Editable?) {
                if(showcaseList.size.isMoreThanZero()) {
                    val filteredList = showcaseList.filter {
                        it.name.toLowerCase(Locale.ROOT).contains(keyword.toString().toLowerCase(Locale.ROOT))
                    }
                    if(filteredList.size.isMoreThanZero()) {
                        showSearchEmptyStateRadioPicker(false)
                        showSearchEmptyStateHint(false)
                        showcasePickerAdapter?.updateDataSet(ArrayList(filteredList), totalChecked = totalCheckedShowcase)
                    } else {
                        if(pickerType == ShowcasePickerType.RADIO)
                            showSearchEmptyStateRadioPicker(true)
                        else {
                            showcaseNameHint = keyword.toString()
                            showSearchEmptyStateHint(true, showcaseNameHint)
                        }
                    }
                }
            }
        })

        // set listener for save picker button
        savePickerButton?.setOnClickListener {
            val intent = Intent()
            if(pickerType == ShowcasePickerType.RADIO) {
                intent.putExtra(ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE, selectedShowcase)
                intent.putExtra(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT, selectedProduct)
            } else {
                intent.putExtra(ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE, selectedShowcaseList)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

        // set listener for add showcase button
        addShowcaseButton?.setOnClickListener {
            checkTotalProduct()
        }

        // set listener for add showcase (for checkbox version)
        headerUnify?.actionTextView?.setOnClickListener {
            showAddShowcaseBottomSheet()
        }

        // set listener for empty state picker
        emptyStatePicker?.setPrimaryCTAClickListener {
            if(pickerType == ShowcasePickerType.RADIO) {
                checkTotalProduct()
            } else {
                showAddShowcaseBottomSheet()
            }
        }

        // set listener for hint
        emptyStateHint?.setOnClickListener {
            if(pickerType == ShowcasePickerType.CHECKBOX) {
                showAddShowcaseBottomSheet(showcaseNameHint)
            }
        }
    }


    private fun setupAddShowcaseBottomSheet(ctx: Context) {
        addShowcaseBottomSheet = BottomSheetUnify().apply {
            setTitle(ctx.getString(R.string.button_tambah_etalase))
            setChild(View.inflate(ctx, R.layout.add_showcase_bottomsheet_picker, null).apply {
                textFieldAddShowcaseBottomSheet = textfield_add_showcase_name
                buttonAddShowcaseBottomSheet = btn_add_showcase_save

                // set text change listener for add showcase name textfield
                textFieldAddShowcaseBottomSheet?.textFieldInput?.addTextChangedListener(object : AfterTextWatcher() {
                    override fun afterTextChanged(name: Editable?) {
                        textFieldAddShowcaseBottomSheet?.setError(false)
                        textFieldAddShowcaseBottomSheet?.setMessage(ctx.getString(R.string.bottomsheet_add_showcase_textfield_hint_text))
                        buttonAddShowcaseBottomSheet?.isEnabled = !name?.length.isZero()
                    }
                })

                // set listener for add showcase button on bottomsheet
                buttonAddShowcaseBottomSheet?.setOnClickListener {
                    buttonAddShowcaseBottomSheet?.isLoading = true
                    createShowcase(textFieldAddShowcaseBottomSheet?.textFieldInput?.text.toString())
                }

                // on dismiss bottomsheet
                setOnDismissListener {
                    activity?.let {
                        KeyboardHandler.hideSoftKeyboard(it)
                    }
                }
            })
            isKeyboardOverlap = false
        }
    }

    private fun showAddShowcaseBottomSheet(showcaseNameHint: String = "") {
        if(shopType == ShopType.REGULAR && showcaseList.size >= MAX_TOTAL_SHOWCASE_REGULAR_MERCHANT) {
            showToaster(getString(R.string.max_total_showcase_error_text), Toaster.TYPE_NORMAL)
        }
        else if((shopType == ShopType.GOLD_MERCHANT || shopType == ShopType.OFFICIAL_STORE)
                && (showcaseList.size >= MAX_TOTAL_SHOWCASE_PM_AND_OS)) {
            showToaster(getString(R.string.max_total_showcase_error_text), Toaster.TYPE_NORMAL)
        }
        else if (totalCheckedShowcase >= ShopShowcasePickerAdapter.MAX_SELECTED_SHOWCASE) {
            showToaster(getString(R.string.max_selected_showcase_text), Toaster.TYPE_NORMAL)
        }
        else {
            context?.let {
                setupAddShowcaseBottomSheet(it)
            }
            if(showcaseNameHint.isNotEmpty()) {
                // set hint to bottomsheet textfield
                textFieldAddShowcaseBottomSheet?.textFieldInput?.text = Editable.Factory.getInstance().newEditable(showcaseNameHint)
            }
            fragmentManager?.let {
                addShowcaseBottomSheet?.show(it, "")
                activity?.let { activity ->
                    KeyboardHandler.showSoftKeyboard(activity)
                }
            }
        }
    }

    private fun initRecyclerView() {
        showcasePickerAdapter = ShopShowcasePickerAdapter(this, pickerType)
        if(preSelectedShowcaseList?.size.isMoreThanZero()) {
            preSelectedShowcaseList?.size?.let { totalCheckedShowcase += it }
            showcasePickerAdapter?.updateDataSet(preSelectedShowcase = preSelectedShowcaseList?.toList(), totalChecked = totalCheckedShowcase)
            preSelectedShowcaseList?.let { selectedShowcaseList?.addAll(it) }
            renderButtonCounter(selectedShowcaseList?.size)
        }
        rv_list_etalase_picker?.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = showcasePickerAdapter
            addOnScrollListener(rvScrollListener)
        }
    }

    private fun loadShowcaseList() {
        if(isMyShop) {
            showLoading(true)
            shopShowcasePickerViewModel.getShopShowcaseListAsSeller()
        }
    }

    private fun checkTotalProduct() {
        showLoading(true)
        shopShowcasePickerViewModel.getTotalProduct(
                shopId,
                DEFAULT_SHOWCASE_PAGE,
                DEFAULT_SHOWCASE_PAGE,
                DEFAULT_SHOWCASE_SORT,
                DEFAULT_SHOWCASE_NAME,
                ""
        )
    }

    private fun createShowcase(showcaseName: String) {
        shopShowcasePickerViewModel.addShopShowcase(AddShopShowcaseParam(name = showcaseName))
    }

    private fun observeCreateShowcase() {
        observe(shopShowcasePickerViewModel.createShopShowcase) {
            when(it) {
                is Success -> {
                    if(it.data.success) {
                        addShowcaseBottomSheet?.dismiss()
                        searchBar?.searchBarTextField?.text?.clear()
                        newCreatedShowcaseId = it.data.createdId
                        loadShowcaseList()
                    } else {
                        buttonAddShowcaseBottomSheet?.isLoading = false
                        textFieldAddShowcaseBottomSheet?.setError(true)
                        textFieldAddShowcaseBottomSheet?.setMessage(it.data.message)
                    }
                }
                is Fail -> {
                    addShowcaseBottomSheet?.dismiss()
                    showGlobalError(true)
                    showToaster(it.throwable.message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeGetShowcaseList() {
        observe(shopShowcasePickerViewModel.getListSellerShopShowcaseResponse) {
            when(it) {
               is Success -> {
                   showLoading(false)
                   val errorMessage = it.data.shopShowcases.error.message
                   showcaseList = it.data.shopShowcases.result
                   if(errorMessage.isNotEmpty()) {
                       showToaster(errorMessage, Toaster.TYPE_ERROR)
                   } else {
                       showGlobalError(false)
                       if(showcaseList.size.isMoreThanZero()) {
                           showEmptyStatePickerList(false)
                           // checked new created showcase, if showcaseId is not equal to zero
                           if(totalCheckedShowcase < ShopShowcasePickerAdapter.MAX_SELECTED_SHOWCASE && newCreatedShowcaseId.isNotEmpty()) {
                               showcaseList.map { showcaseItem ->
                                   if(showcaseItem.id == newCreatedShowcaseId) {
                                       showcaseItem.isChecked = true
                                       selectedShowcaseList?.add(ShowcaseItemPicker(showcaseItem.id, showcaseItem.name))
                                       renderButtonCounter(selectedShowcaseList?.size)
                                       totalCheckedShowcase += 1
                                   }
                               }
                           }
                           // check previous selected showcase
                           if(selectedShowcaseList?.size.isMoreThanZero()) {
                               showcaseList.forEach { item ->
                                   selectedShowcaseList?.forEach { selectedItem ->
                                       if(item.id == selectedItem.showcaseId) {
                                           // assign new showcase name from cloud, to prevent empty showcase from edit product
                                           selectedItem.showcaseName = item.name
                                           item.isChecked = true
                                       }
                                   }
                               }
                           }
                           showcasePickerAdapter?.updateDataSet(newList = showcaseList, totalChecked = totalCheckedShowcase)
                       } else {
                           showEmptyStatePickerList(true)
                       }
                   }
               }
                is Fail -> {
                    showLoading(false)
                    showEmptyStatePickerList(false)
                    showGlobalError(true)
                    showToaster(it.throwable.message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun observeGetTotalProduct() {
        observe(shopShowcasePickerViewModel.getShopProductResponse) {
            when (it) {
                is Success -> {
                    val error = it.data.getShopProduct.errors
                    val totalProduct = it.data.getShopProduct.totalData

                    if (error.isNotEmpty()) {
                        showLoading(false)
                        showToaster(error, Toaster.TYPE_ERROR)
                    } else {
                        if (totalProduct.isMoreThanZero()) {
                            goToAddShowcase()
                        } else {
                            showLoading(false)
                            showToaster(getString(R.string.error_product_less_than_one), Toaster.TYPE_ERROR)
                        }
                    }
                }
                is Fail -> {
                    showLoading(false)
                    showGlobalError(true)
                    showToaster(it.throwable.message, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun setupPickerLayout() {
        if(pickerType == ShowcasePickerType.CHECKBOX) {
            addShowcaseButton?.gone()
            headerUnify?.actionTextView?.visible()
            renderButtonCounter(selectedShowcaseList?.size)
        } else {
            addShowcaseButton?.visible()
            headerUnify?.actionTextView?.gone()
        }
    }

    private fun renderButtonCounter(total: Int?) {
        if(total.isMoreThanZero()) {
            savePickerButton?.text = getString(R.string.picker_apply_showcase_counter_text, total.toString())
            savePickerButton?.isEnabled = true
        } else {
            savePickerButton?.text = getString(R.string.picker_apply_showcase_text)
            savePickerButton?.isEnabled = false
        }
    }

    private fun showSearchEmptyStateHint(state: Boolean, hint: String = "") {
        if(state) {
            tvEmptyStateHint?.text = MethodChecker.fromHtml(String.format(getString(R.string.empty_state_hint_text), hint))
            emptyStateHint?.visible()
            rvPicker?.gone()
        } else {
            emptyStateHint?.gone()
            rvPicker?.visible()
        }
    }

    private fun showSearchEmptyStateRadioPicker(state: Boolean) {
        if(state) {
            ImageHandler.loadImage(context, emptyStateImage, ImageAssets.SEARCH_SHOWCASE_NOT_FOUND, null)
            emptyState?.visible()
            rvPicker?.gone()
        } else {
            emptyState?.gone()
            rvPicker?.visible()
        }
    }

    private fun showEmptyStatePickerList(state: Boolean) {
        if(state) {
            rvPicker?.gone()
            searchBar?.gone()
            headerUnify?.actionTextView?.gone()
            footer?.gone()
            emptyStatePicker?.setImageUrl(ImageAssets.PICKER_LIST_EMPTY)
            emptyStatePicker?.visible()
        } else {
            emptyStatePicker?.gone()
            rvPicker?.visible()
            searchBar?.visible()
            footer?.visible()
            if(pickerType == ShowcasePickerType.CHECKBOX) {
                headerUnify?.actionTextView?.visible()
            } else {
                addShowcaseButton?.visible()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            loaderUnify?.visible()
            rvPicker?.gone()
            footer?.gone()
            headerUnify?.actionTextView?.gone()
            searchBar?.gone()
            emptyState?.gone()
            emptyStatePicker?.gone()
            emptyStateHint?.gone()
            addShowcaseButton?.gone()
        } else {
            loaderUnify?.gone()
            rvPicker?.visible()
            footer?.visible()
            searchBar?.visible()
            if(pickerType == ShowcasePickerType.CHECKBOX) {
                headerUnify?.actionTextView?.visible()
            } else {
                addShowcaseButton?.visible()
            }
        }
    }

    private fun showToaster(msg: String?, type: Int) {
        hideSoftKeyboard()
        view?.let { view ->
            Toaster.make(view, msg ?: "", Snackbar.LENGTH_LONG, type)
        }
    }

    private fun showGlobalError(state: Boolean) {
        if(state) {
            globalError?.setType(GlobalError.SERVER_ERROR)
            globalError?.visible()
            globalError?.setActionClickListener {
                loadShowcaseList()
            }
            rvPicker?.gone()
            footer?.gone()
            loaderUnify?.gone()
            headerLayout?.gone()
        } else {
            globalError?.gone()
            rvPicker?.visible()
            footer?.visible()
            headerLayout?.visible()
        }
    }

    private fun hideSoftKeyboard() {
        activity?.run {
            KeyboardHandler.hideSoftKeyboard(this)
        }
    }

    private fun goToAddShowcase() {
        val addShowcaseIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_ADD)
        startActivityForResult(addShowcaseIntent, ShopShowcaseListActivity.REQUEST_CODE_ADD_ETALASE)
    }
}