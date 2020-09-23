package com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShowcasePickerType
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.ShopShowcaseInstance
import com.tokopedia.shop_showcase.common.AppScreen
import com.tokopedia.shop_showcase.common.ImageAssets
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem
import com.tokopedia.shop_showcase.shop_showcase_management.di.DaggerShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementComponent
import com.tokopedia.shop_showcase.shop_showcase_management.di.ShopShowcaseManagementModule
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity.ShopShowcaseListActivity
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.adapter.ShopShowcasePickerAdapter
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.viewmodel.ShopShowcasePickerViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
        fun createInstance(shopId: String?, isMyShop: Boolean, pickerType: String): ShopShowcasePickerFragment {
            val fragment = ShopShowcasePickerFragment()
            val extraData = Bundle()
            extraData.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
            extraData.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_MY_SHOP, isMyShop)
            extraData.putString(ShopShowcaseParamConstant.EXTRA_PICKER_TYPE, pickerType)
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

    private val searchBar by lazy {
        view?.findViewById<SearchBarUnify>(R.id.searchbar)
    }

    private val emptyState by lazy {
        view?.findViewById<LinearLayout>(R.id.empty_state)
    }

    private val emptyStateImage by lazy {
        view?.findViewById<ImageView>(R.id.img_empty_state)
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
    private var showcaseList: List<ShowcaseItem> = listOf()
    private var isMyShop = false
    private var shopId: String = ""
    private var pickerType: String = ""
    private var selectedShowcase: ShowcaseItemPicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shopId = it.getString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, "")
            isMyShop = it.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_MY_SHOP)
            pickerType = it.getString(ShopShowcaseParamConstant.EXTRA_PICKER_TYPE, ShowcasePickerType.RADIO)
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
        removeObservers(shopShowcasePickerViewModel.getListBuyerShopShowcaseResponse)
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

    override fun onPickerItemClicked(item: ShowcaseItem) {
        ShowcaseItemPicker().apply {
            showcaseId = item.id
            showcaseName = item.name
            selectedShowcase = this
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == ShopShowcaseListActivity.REQUEST_CODE_ADD_ETALASE) {
            loadShowcaseList()
        }
    }

    private fun initView() {
        setBackgroundColor()
        initRecyclerView()
        loadShowcaseList()
        observeGetShowcaseList()
        observeGetTotalProduct()
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
                        showShowcasePickerEmptyState(false)
                        showcasePickerAdapter?.updateDataSet(ArrayList(filteredList))
                    } else {
                        showShowcasePickerEmptyState(true)
                    }
                }
            }
        })

        // set listener for save picker button
        savePickerButton?.setOnClickListener {
            activity?.setResult(Activity.RESULT_OK, Intent().putExtra(ShopShowcaseParamConstant.EXTRA_PICKER_SELECTED_SHOWCASE, selectedShowcase))
            activity?.finish()
        }

        // set listener for add showcase button
        addShowcaseButton?.setOnClickListener {
            checkTotalProduct()
        }
    }

    private fun initRecyclerView() {
        showcasePickerAdapter = ShopShowcasePickerAdapter(this, pickerType)
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
            shopShowcasePickerViewModel.getShopShowcaseListAsBuyer(shopId, isOwner = isMyShop)
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

    private fun observeGetShowcaseList() {
        observe(shopShowcasePickerViewModel.getListBuyerShopShowcaseResponse) {
            when(it) {
               is Success -> {
                   showLoading(false)
                   val errorMessage = it.data.shopShowcasesByShopID.error.message
                   showcaseList = it.data.shopShowcasesByShopID.result
                   if(errorMessage.isNotEmpty()) {
                       showErrorToaster(errorMessage)
                   } else {
                       showGlobalError(false)
                       showcasePickerAdapter?.updateDataSet(showcaseList)
                   }
               }
                is Fail -> {
                    showLoading(false)
                    showGlobalError(true)
                    showErrorToaster(it.throwable.message)
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
                        showErrorToaster(error)
                    } else {
                        if (totalProduct.isMoreThanZero()) {
                            goToAddShowcase()
                        } else {
                            showLoading(false)
                            showErrorToaster(getString(R.string.error_product_less_than_one))
                        }
                    }
                }
                is Fail -> {
                    showLoading(false)
                    showGlobalError(true)
                    showErrorToaster(it.throwable.message)
                }
            }
        }
    }

    private fun showShowcasePickerEmptyState(state: Boolean) {
        if(state) {
            ImageHandler.loadImage(context, emptyStateImage, ImageAssets.SEARCH_SHOWCASE_NOT_FOUND, null)
            emptyState?.visible()
            rv_list_etalase_picker?.gone()
        } else {
            emptyState?.gone()
            rv_list_etalase_picker?.visible()
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            loaderUnify?.visible()
            rvPicker?.gone()
            footer?.gone()
        } else {
            loaderUnify?.gone()
            rvPicker?.visible()
            footer?.visible()
        }
    }

    private fun showErrorToaster(msg: String?) {
        view?.let { view ->
            Toaster.make(view, msg ?: "", Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
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

    private fun setBackgroundColor() {
        view?.setBackgroundColor(Color.WHITE)
    }

    private fun goToAddShowcase() {
        val addShowcaseIntent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_ADD)
        startActivityForResult(addShowcaseIntent, ShopShowcaseListActivity.REQUEST_CODE_ADD_ETALASE)
    }
}