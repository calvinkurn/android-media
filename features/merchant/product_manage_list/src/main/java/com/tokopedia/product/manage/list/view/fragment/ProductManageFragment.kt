package com.tokopedia.product.manage.list.view.fragment

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener
import com.github.rubensousa.bottomsheetbuilder.custom.CheckedBottomSheetBuilder
import com.google.android.material.appbar.AppBarLayout
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.abstraction.constant.TkpdState
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.button.BottomActionView
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.gm.common.constant.IMG_URL_POWER_MERCHANT_IDLE_POPUP
import com.tokopedia.gm.common.constant.IMG_URL_REGULAR_MERCHANT_POPUP
import com.tokopedia.gm.common.constant.URL_POWER_MERCHANT_SCORE_TIPS
import com.tokopedia.gm.common.widget.MerchantCommonBottomSheet
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.RESULT_IMAGE_DESCRIPTION_LIST
import com.tokopedia.product.manage.item.common.util.CurrencyTypeDef
import com.tokopedia.product.manage.item.common.util.ViewUtils
import com.tokopedia.product.manage.item.imagepicker.imagepickerbuilder.AddProductImagePickerBuilder
import com.tokopedia.product.manage.item.main.add.view.activity.ProductAddNameCategoryActivity
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK
import com.tokopedia.product.manage.item.main.duplicate.activity.ProductDuplicateActivity
import com.tokopedia.product.manage.item.main.edit.view.activity.ProductEditActivity
import com.tokopedia.product.manage.item.stock.view.activity.ProductBulkEditStockActivity
import com.tokopedia.product.manage.item.utils.constant.ProductExtraConstant
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.ERROR_CODE_LIMIT_CASHBACK
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.ETALASE_PICKER_REQUEST_CODE
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_SORT_SELECTED
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.HASSHOWNBTN
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.INSTAGRAM_SELECT_REQUEST_CODE
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.REQUEST_CODE_FILTER
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.REQUEST_CODE_SORT
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.STOCK_EDIT_REQUEST_CODE
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.URL_TIPS_TRICK
import com.tokopedia.product.manage.list.constant.option.CashbackOption
import com.tokopedia.product.manage.list.constant.option.SortProductOption
import com.tokopedia.product.manage.list.constant.option.StatusProductOption
import com.tokopedia.product.manage.list.data.ConfirmationProductData
import com.tokopedia.product.manage.list.data.model.BulkBottomSheetType
import com.tokopedia.product.manage.list.data.model.ProductManageFilterModel
import com.tokopedia.product.manage.list.data.model.ProductManageSortModel
import com.tokopedia.product.manage.list.data.model.mutationeditproduct.ProductUpdateV3SuccessFailedResponse
import com.tokopedia.product.manage.list.di.DaggerProductManageComponent
import com.tokopedia.product.manage.list.utils.ProductManageTracking
import com.tokopedia.product.manage.list.view.activity.ProductManageFilterActivity
import com.tokopedia.product.manage.list.view.activity.ProductManageSortActivity
import com.tokopedia.product.manage.list.view.adapter.ProductManageListAdapter
import com.tokopedia.product.manage.list.view.adapter.ProductManageListViewHolder
import com.tokopedia.product.manage.list.view.bottomsheets.ConfirmationUpdateProductBottomSheet
import com.tokopedia.product.manage.list.view.bottomsheets.EditProductBottomSheet
import com.tokopedia.product.manage.list.view.factory.ProductManageFragmentFactoryImpl
import com.tokopedia.product.manage.list.view.listener.ProductManageView
import com.tokopedia.product.manage.list.view.model.ProductManageViewModel
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenter
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.seller.common.utils.KMNumbers
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.data.model.FreeDeposit.CREATOR.DEPOSIT_ACTIVE
import com.tokopedia.topads.freeclaim.data.constant.TOPADS_FREE_CLAIM_URL
import com.tokopedia.topads.freeclaim.view.widget.TopAdsWidgetFreeClaim
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

open class ProductManageFragment : BaseSearchListFragment<ProductManageViewModel, ProductManageFragmentFactoryImpl>(),
        ProductManageView, BaseListCheckableAdapter.OnCheckableAdapterListener<ProductManageViewModel>,
        MerchantCommonBottomSheet.BottomSheetListener,
        BaseCheckableViewHolder.CheckableInteractionListener,
        ProductManageListViewHolder.ProductManageViewHolderListener,
        EditProductBottomSheet.EditProductInterface {

    @Inject
    lateinit var productManagePresenter: ProductManagePresenter
    @Inject
    lateinit var userSession: UserSessionInterface

    lateinit var bottomActionView: BottomActionView
    lateinit var progressDialog: ProgressDialog
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var topAdsWidgetFreeClaim: TopAdsWidgetFreeClaim
    lateinit var btnSubmit: Button
    lateinit var btnGoToPdp: Button
    lateinit var txtTipsTrick: TextView
    lateinit var dialog: Dialog
    lateinit var bulkCheckBox: CheckBox
    lateinit var bulkCountTxt: TextView
    lateinit var btnBulk: Button
    lateinit var containerBtnBulk: LinearLayout
    lateinit var containerChechBoxBulk: LinearLayout
    lateinit var checkBoxView: View
    lateinit var bulkBottomSheet: CloseableBottomSheetDialog
    lateinit var editProductBottomSheet: EditProductBottomSheet

    @SortProductOption
    private var sortProductOption: String = SortProductOption.POSITION
    private var goldMerchant: Boolean = false
    private var isOfficialStore: Boolean = false
    private var shopDomain: String = ""
    private var productManageFilterModel: ProductManageFilterModel = ProductManageFilterModel()
    lateinit var productManageListAdapter: ProductManageListAdapter

    private var productManageViewModels: MutableList<ProductManageViewModel> = mutableListOf()
    private var etalaseType = BulkBottomSheetType.EtalaseType("", 0)
    private var stockType = BulkBottomSheetType.StockType()
    private var confirmationProductDataList: ArrayList<ConfirmationProductData> = arrayListOf()
    private var itemsChecked: MutableList<ProductManageViewModel> = mutableListOf()


    lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_manage, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefKey = this.javaClass.name + ".pref"
        context?.let {
            prefs = it.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (GlobalConfig.isCustomerApp()) {
            inflater.inflate(R.menu.menu_product_manage_dark, menu)
        } else {
            inflater.inflate(R.menu.menu_product_manage, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if (itemId == R.id.add_product_menu) {
            item.subMenu.findItem(R.id.label_view_add_image).setOnMenuItemClickListener { item ->
                startActivity(ProductAddNameCategoryActivity.createInstance(activity))
                ProductManageTracking.eventProductManageTopNav(item.title.toString())
                true
            }
            item.subMenu.findItem(R.id.label_view_import_from_instagram).setOnMenuItemClickListener { item ->
                val intent = AddProductImagePickerBuilder.createPickerIntentInstagramImport(context)
                startActivityForResult(intent, INSTAGRAM_SELECT_REQUEST_CODE)
                ProductManageTracking.eventProductManageTopNav(item.title.toString())
                false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sortProductOption = SortProductOption.POSITION
        productManageFilterModel = ProductManageFilterModel()
        productManageFilterModel.reset()

        super.onViewCreated(view, savedInstanceState)
        searchInputView.clearFocus()
        initView(view)
        setupBottomSheet()
        renderCheckedView()

        bottomActionView.setButton1OnClickListener {
            context?.let {
                val intent = ProductManageSortActivity.createIntent(it, sortProductOption)
                startActivityForResult(intent, REQUEST_CODE_SORT)
            }
        }

        bottomActionView.setButton2OnClickListener {
            context?.let {
                val intent = ProductManageFilterActivity.createIntent(it, productManageFilterModel)
                startActivityForResult(intent, REQUEST_CODE_FILTER)
            }
        }

        bulkCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (!isListEmpty) {
                adapter.data.forEachIndexed { index, _ ->
                    if (!isChecked || !productManageListAdapter.isChecked(index)) {
                        productManageListAdapter.updateListByCheck(isChecked, index)
                    }
                }
            }
        }

        btnBulk.setOnClickListener { bulkBottomSheet.show() }

        searchInputView.closeImageButton.setOnClickListener {
            searchInputView.searchText = ""
            loadInitialData()
        }

        displayOnBoardingCheck()
    }

    private fun initView(view: View) {
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle(R.string.title_loading)
        bulkCheckBox = view.findViewById(R.id.bulk_check_box)
        coordinatorLayout = view.findViewById(R.id.coordinator_layout)
        bottomActionView = view.findViewById(R.id.bottom_action_view)
        bulkCountTxt = view.findViewById(R.id.bulk_count_txt)
        topAdsWidgetFreeClaim = view.findViewById(R.id.topads_free_claim_widget)
        btnBulk = view.findViewById(R.id.btn_bulk_edit)
        containerBtnBulk = view.findViewById(R.id.container_btn_bulk)
        containerChechBoxBulk = view.findViewById(R.id.container_bulk_check_box)
        checkBoxView = view.findViewById(R.id.line_check_box)
    }

    private fun setupBottomSheet() {
        bulkBottomSheet = CloseableBottomSheetDialog.createInstanceCloseableRounded(context) {
            editProductBottomSheet.clearAllData()
            clearEtalaseAndStockData()
        }
        context?.let { context ->
            fragmentManager?.let {
                editProductBottomSheet = EditProductBottomSheet(context, this, it)
            }
        }
        bulkBottomSheet.setCustomContentView(editProductBottomSheet, getString(R.string.product_bs_title), true)
    }

    private fun clearEtalaseAndStockData() {
        stockType = BulkBottomSheetType.StockType()
        etalaseType = BulkBottomSheetType.EtalaseType()
    }

    private fun renderCheckedView() {
        if (itemsChecked.size > 0) {
            containerBtnBulk.visibility = View.VISIBLE
            bulkCountTxt.visibility = View.VISIBLE
            checkBoxView.visibility = View.VISIBLE
            bulkCountTxt.text = getString(R.string.product_manage_bulk_count, itemsChecked.size.toString())
            bottomActionView.visibility = View.GONE
        } else {
            containerBtnBulk.visibility = View.GONE
            checkBoxView.visibility = View.GONE
            bulkCountTxt.visibility = View.GONE
            bottomActionView.visibility = View.VISIBLE
        }
    }

    private val addProductReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == TkpdState.ProductService.BROADCAST_ADD_PRODUCT &&
                    intent.hasExtra(TkpdState.ProductService.STATUS_FLAG) &&
                    intent.getIntExtra(TkpdState.ProductService.STATUS_FLAG, 0) == TkpdState.ProductService.STATUS_DONE) {
                activity?.run {
                    runOnUiThread {
                        val productId = intent.extras?.getString(TkpdState.ProductService.PRODUCT_ID)
                                ?: ""
                        productManagePresenter.getPopupsInfo(productId)
                        loadInitialData()
                    }
                }
            }
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<ProductManageViewModel, ProductManageFragmentFactoryImpl> {
        productManageListAdapter = ProductManageListAdapter(adapterTypeFactory, this)
        return productManageListAdapter
    }

    override fun getAdapterTypeFactory(): ProductManageFragmentFactoryImpl = ProductManageFragmentFactoryImpl(this, this)

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            GraphqlClient.init(it)
            val appComponent = (it.application as BaseMainApplication).baseAppComponent
            DaggerProductManageComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
            productManagePresenter.attachView(this)
        }
    }

    override fun loadData(page: Int) {
        productManagePresenter.getFreeClaim(GraphqlHelper.loadRawString(resources, R.raw.gql_get_deposit), userSession.shopId)
        productManagePresenter.getGoldMerchantStatus()
        productManagePresenter.getProductList(page, searchInputView.searchText,
                productManageFilterModel.catalogProductOption, productManageFilterModel.conditionProductOption,
                productManageFilterModel.etalaseProductOption, productManageFilterModel.pictureStatusOption,
                sortProductOption, productManageFilterModel.categoryId)
    }

    override fun renderList(list: MutableList<ProductManageViewModel>, hasNextPage: Boolean) {
        super.renderList(list, hasNextPage)
        if (list.isEmpty()) {
            containerChechBoxBulk.visibility = View.GONE
        } else {
            containerChechBoxBulk.visibility = View.VISIBLE
        }
        /**
         * Keep checklist after user search or filter
         */

        if (list.isEmpty()) {
            containerChechBoxBulk.visibility = View.GONE
        } else {
            containerChechBoxBulk.visibility = View.VISIBLE
        }
        renderCheckedView()
    }

    override fun onSearchSubmitted(text: String) {
        ProductManageTracking.eventProductManageSearch()
        loadInitialData()
    }

    override fun onSearchTextChanged(text: String?) {
        // NO OP
    }

    override fun onLoadListEmpty() {
        renderList(ArrayList())
    }

    override fun onSuccessGetProductList(list: MutableList<ProductManageViewModel>, totalItem: Int, hasNextPage: Boolean) {
        productManageViewModels = list
        renderList(list, hasNextPage)
    }

    override fun onSuccessGetShopInfo(goldMerchant: Boolean, officialStore: Boolean, shopDomain: String) {
        this.goldMerchant = goldMerchant
        isOfficialStore = officialStore
        this.shopDomain = shopDomain
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
        bulkCheckBox.isChecked = false
        productManageListAdapter.resetCheckedItemSet()
        itemsChecked.clear()
        renderCheckedView()
    }

    override fun onErrorEditPrice(t: Throwable?, productId: String?, price: String?, currencyId: String?, currencyText: String?) {
        context?.let {
            showSnackBarWithAction(ViewUtils.getErrorMessage(it, t)) {
                productManagePresenter.editPrice(
                        productId ?: "",
                        price ?: "",
                        currencyId ?: "",
                        currencyText ?: "")
            }
        }
    }

    override fun onSuccessEditPrice(productId: String?, price: String?, currencyId: String?, currencyText: String?) {
        productManageListAdapter.updatePrice(
                productId ?: "",
                price ?: "",
                currencyId ?: "",
                currencyText ?: ""
        )
    }

    override fun onErrorSetCashback(t: Throwable?, productId: String?, cashback: Int) {
        context?.let {
            if (t is MessageErrorException && t.errorCode == ERROR_CODE_LIMIT_CASHBACK) {
                if (productManagePresenter.isIdlePowerMerchant()) {
                    showIdlePowerMerchantBottomSheet(getString(R.string.product_manage_feature_name_cashback))
                } else if (!productManagePresenter.isPowerMerchant()) {
                    showRegularMerchantBottomSheet(getString(R.string.product_manage_feature_name_cashback))
                } else {
                    showSnackBarWithAction(ViewUtils.getErrorMessage(it, t)) {
                        productManagePresenter.setCashback(productId ?: "", cashback)
                    }
                }
            } else {
                showSnackBarWithAction(ViewUtils.getErrorMessage(it, t)) {
                    productManagePresenter.setCashback(productId ?: "", cashback)
                }
            }
        }
    }

    private fun showRegularMerchantBottomSheet(featureName: String) {
        val title = getString(R.string.bottom_sheet_regular_title, featureName)
        val description = getString(R.string.bottom_sheet_regular_desc, featureName)
        val buttonName = getString(R.string.bottom_sheet_regular_btn)
        showBottomSheet(title, IMG_URL_REGULAR_MERCHANT_POPUP, description, buttonName)
    }

    private fun showIdlePowerMerchantBottomSheet(featureName: String) {
        val title = getString(R.string.bottom_sheet_idle_title, featureName)
        val description = getString(R.string.bottom_sheet_idle_desc, featureName)
        val buttonName = getString(R.string.bottom_sheet_idle_btn)
        showBottomSheet(title, IMG_URL_POWER_MERCHANT_IDLE_POPUP, description, buttonName)
    }

    private fun showBottomSheet(title: String, imageUrl: String, description: String, buttonName: String) {
        val model = MerchantCommonBottomSheet.BottomSheetModel(
                title,
                description,
                imageUrl,
                buttonName,
                ""
        )
        val bottomSheet = MerchantCommonBottomSheet.newInstance(model)
        bottomSheet.setListener(this)
        bottomSheet.show(childFragmentManager, "merchant_warning_bottom_sheet")
    }

    override fun onSuccessSetCashback(productId: String, cashback: Int) {
        productManageListAdapter.updateCashback(productId, cashback)
    }

    override fun onErrorMultipleDeleteProduct(e: Throwable?, listOfResponse: ProductUpdateV3SuccessFailedResponse?) {
        activity?.let {
            showToasterError(ViewUtils.getErrorMessage(it, e), getString(R.string.close)) {}
        }
    }

    override fun onSuccessMultipleDeleteProduct() {
        showToasterNormal(getString(R.string.product_manage_bulk_snackbar_sucess_delete))
        loadInitialData()
    }

    private fun showToasterNormal(message: String) {
        ToasterNormal.make(view, message, ToasterNormal.LENGTH_LONG)
                .setAction(R.string.close) {
                    //NO OP
                }.show()
    }

    private fun showToasterError(message: String, actionLabel: String, listener: () -> Unit) {
        ToasterError.make(view, message, ToasterError.LENGTH_LONG)
                .setAction(actionLabel) {
                    listener.invoke()
                }.show()
    }

    private fun showSnackBarWithAction(message: String, listener: () -> Unit) {
        NetworkErrorHelper.createSnackbarWithAction(activity, message) { listener.invoke() }
                .showRetrySnackbar()

    }

    override fun showLoadingProgress() {
        progressDialog.show()
    }

    override fun hideLoadingProgress() {
        progressDialog.hide()
    }

    override fun onErrorGetFreeClaim(throwable: Throwable?) {
        topAdsWidgetFreeClaim.visibility = View.GONE
    }

    override fun onSuccessGetFreeClaim(dataDeposit: DataDeposit) {
        val freeDeposit = dataDeposit.freeDeposit

        if (freeDeposit.nominal > 0 && freeDeposit.status == DEPOSIT_ACTIVE) {
            topAdsWidgetFreeClaim.setContent(MethodChecker.fromHtml(getString(R.string.free_claim_template, freeDeposit.nominalFmt,
                    freeDeposit.remainingDays.toString() + "", TOPADS_FREE_CLAIM_URL)))
            topAdsWidgetFreeClaim.visibility = View.VISIBLE
        } else {
            topAdsWidgetFreeClaim.visibility = View.GONE
        }
    }

    override fun onSuccessGetPopUp(isShowPopup: Boolean, productId: String) {
        if (isShowPopup) {
            initPopUpDialog(productId).show()
        }
    }

    private fun initPopUpDialog(productId: String): Dialog {
        context?.let { context ->
            activity?.let { activity ->
                dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_product_add)

                btnSubmit = dialog.findViewById(R.id.btn_submit)
                btnGoToPdp = dialog.findViewById(R.id.btn_product_list)
                txtTipsTrick = dialog.findViewById(R.id.txt_tips_trick)

                btnSubmit.setOnClickListener {
                    ProductManageTracking.trackerManageCourierButton()
                    RouteManager.route(context, ApplinkConst.SELLER_SHIPPING_EDITOR)
                    activity.finish()
                }

                btnGoToPdp.setOnClickListener {
                    ProductManageTracking.trackerSeeProduct()
                    goToPDP(productId)
                    dialog.dismiss()
                }
                val backgroundColor = MethodChecker.getColor(context, R.color.tkpd_main_green)

                val spanText = SpannableString(getString(R.string.popup_tips_trick_clickable))
                spanText.setSpan(StyleSpan(Typeface.BOLD),
                        5, spanText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spanText.setSpan(ForegroundColorSpan(backgroundColor),
                        5, spanText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                val cs = object : ClickableSpan() {
                    override fun onClick(v: View) {
                        ProductManageTracking.trackerLinkClick()
                        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TIPS_TRICK))
                        activity.finish()
                    }
                }
                spanText.setSpan(cs, 5, spanText.length - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                txtTipsTrick.movementMethod = LinkMovementMethod.getInstance()
                txtTipsTrick.text = spanText
                return dialog
            }
        }
        return Dialog(context!!)
    }

    override fun onErrorGetPopUp(e: Throwable) {
        onSuccessGetPopUp(false, "")
    }

    override fun onSuccessBulkUpdateProduct(listOfResponse: ProductUpdateV3SuccessFailedResponse) {
        hideLoadingProgress()
        bulkCheckBox.isChecked = false

        /**
         * When bulk update products, there's possibility one of the product failed to update from server so
         * this logic use for catch failed update products
         */
        if (listOfResponse.failedResponse.isNotEmpty()) {
            showToasterError(getString(R.string.product_manage_bulk_snackbar, listOfResponse.successResponse.size.toString(), listOfResponse.failedResponse.size.toString()),
                    getString(R.string.retry_label)) {
                productManagePresenter.bulkUpdateProduct(productManagePresenter.failedBulkDataMapper(listOfResponse.failedResponse, confirmationProductDataList))
            }
        } else {
            confirmationProductDataList.clear()
            editProductBottomSheet.clearAllData()
            clearEtalaseAndStockData()
            showToasterNormal(getString(R.string.product_manage_bulk_snackbar_sucess, listOfResponse.successResponse.size.toString()))
        }

        productManageListAdapter.resetCheckedItemSet()
        itemsChecked.clear()
        renderCheckedView()
        loadInitialData()
    }

    override fun onErrorBulkUpdateProduct(e: Throwable) {
        activity?.let {
            showToasterError(ViewUtils.getErrorMessage(it, e), getString(R.string.close)) {
                //No OP
            }
        }
    }

    override fun onItemChecked(data: ProductManageViewModel, isChecked: Boolean) {
        if (isChecked) {
            itemsChecked.add(data)
        } else {
            /**
             * When refresh the data , it will keept the check
             * but the id *ex:ProductManageViewModel@12xxx will also update
             * then we cant remove it from itemsChecked because the id is different.
             */
            val checkedData = itemsChecked.find {
                it.id.contains(data.id)
            }
            itemsChecked.remove(checkedData)
        }
        updateBulkLayout()
        renderCheckedView()
    }

    private fun updateBulkLayout() {
        val containerFlags = containerChechBoxBulk.layoutParams as AppBarLayout.LayoutParams
        when {
            itemsChecked.size == 1 -> {
                containerBtnBulk.visibility = View.VISIBLE
                btnBulk.visibility = View.VISIBLE
                checkBoxView.visibility = View.VISIBLE
                btnBulk.text = getString(R.string.product_manage_change_btn)
                containerFlags.scrollFlags = 0
            }
            itemsChecked.size > 1 -> {
                containerBtnBulk.visibility = View.VISIBLE
                btnBulk.visibility = View.VISIBLE
                checkBoxView.visibility = View.VISIBLE
                btnBulk.text = getString(R.string.product_manage_bulk_change_btn)
                containerFlags.scrollFlags = 0
                displayOnBoardingButton()
            }
            else -> {
                containerBtnBulk.visibility = View.GONE
                checkBoxView.visibility = View.GONE
                btnBulk.visibility = View.GONE
                containerFlags.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            }
        }
    }

    private fun displayOnBoardingButton() {
        if (!prefs.getBoolean(HASSHOWNBTN, false)) {
            val coachMark = CoachMarkBuilder().build()
            val coachMarkItem = ArrayList<CoachMarkItem>()
            coachMarkItem.add(CoachMarkItem(btnBulk,
                    getString(R.string.coachmark_title_btn),
                    getString(R.string.coachmark_desc_btn)))
            prefs.edit().putBoolean(HASSHOWNBTN, true).apply()
            coachMark.show(activity, "SampleCoachMark", coachMarkItem)
        }
    }

    private fun displayOnBoardingCheck() {
        if (!prefs.getBoolean(ProductManageListConstant.HASSHOWNCHECKED, false)) {
            val coachMark = CoachMarkBuilder().build()
            val coachMarkItem = ArrayList<CoachMarkItem>()
            coachMarkItem.add(CoachMarkItem(bulkCheckBox,
                    getString(R.string.coachmark_title_checkbox),
                    getString(R.string.coachmark_desc_checkbox)))
            prefs.edit().putBoolean(ProductManageListConstant.HASSHOWNCHECKED, true).apply()
            coachMark.show(activity, "SampleCoachMark", coachMarkItem)
        }
    }

    override fun onBottomSheetButtonClicked() {
        if (productManagePresenter.isIdlePowerMerchant()) {
            RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_POWER_MERCHANT_SCORE_TIPS)
        } else if (!productManagePresenter.isPowerMerchant()) {
            RouteManager.route(getContext(), ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE);
        }
    }

    override fun isChecked(position: Int): Boolean = productManageListAdapter.isChecked(position)

    override fun updateListByCheck(isChecked: Boolean, position: Int) = productManageListAdapter.updateListByCheck(isChecked, position)

    override fun onClickOptionItem(productManageViewModel: ProductManageViewModel) {
        showActionProductDialog(productManageViewModel)
    }

    private fun showActionProductDialog(productManageViewModel: ProductManageViewModel) {

        activity?.let {
            CommonUtils.hideKeyboard(it, it.currentFocus)
        }

        val bottomSheetBuilder = BottomSheetBuilder(activity)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(productManageViewModel.productName)
        if (productManageViewModel.productStatus == StatusProductOption.EMPTY) {
            bottomSheetBuilder.setMenu(R.menu.menu_product_manage_action_item_no_topads)
        } else {
            bottomSheetBuilder.setMenu(R.menu.menu_product_manage_action_item)
        }
        val bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionBottomSheetClicked(productManageViewModel))
                .createDialog()
        bottomSheetDialog.show()
    }

    private fun onOptionBottomSheetClicked(productManageViewModel: ProductManageViewModel): BottomSheetItemClickListener {
        return BottomSheetItemClickListener {
            if (productManageViewModel.productStatus == StatusProductOption.UNDER_SUPERVISION) {
                NetworkErrorHelper.showSnackbar(activity, getString(R.string.product_manage_desc_product_on_supervision, productManageViewModel.productName))
                return@BottomSheetItemClickListener
            }
            val itemId = it.itemId
            if (itemId == R.id.edit_product_menu) {
                goToEditProduct(productManageViewModel.id)
                ProductManageTracking.eventProductManageOverflowMenu(it.title.toString())
            } else if (itemId == R.id.duplicat_product_menu) {
                goToDuplicateProduct(productManageViewModel.id)
                ProductManageTracking.eventProductManageOverflowMenu(it.title.toString())
            } else if (itemId == R.id.delete_product_menu) {
                val productIdList = ArrayList<String>()

                showDialogActionDeleteProduct(productIdList,

                        DialogInterface.OnClickListener { _, _ ->
                            ProductManageTracking.eventProductManageOverflowMenu(it.title.toString() + " - " + getString(R.string.label_delete))
                            productManagePresenter.deleteSingleProduct(productManageViewModel.id)
                        },

                        DialogInterface.OnClickListener { dialog, _ ->
                            ProductManageTracking.eventProductManageOverflowMenu(it.title.toString() + " - " + getString(R.string.title_cancel))
                            dialog.dismiss()
                        })

            } else if (itemId == R.id.change_price_product_menu) {
                if (productManageViewModel.isProductVariant) {
                    showDialogVariantPriceLocked()
                } else {
                    showDialogChangeProductPrice(productManageViewModel.productId, productManageViewModel.productPricePlain, productManageViewModel.productCurrencyId)
                }
            } else if (itemId == R.id.share_product_menu) {
                downloadBitmap(productManageViewModel)
            } else if (itemId == R.id.set_cashback_product_menu) {
                onSetCashbackClicked(productManageViewModel)
            } else if (itemId == R.id.set_promo_ads_product_menu) {
                onPromoTopAdsClicked(productManageViewModel)
            }
        }
    }

    private fun onPromoTopAdsClicked(productManageViewModel: ProductManageViewModel) {
        context?.let {
            val uri = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                    .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, userSession.shopId)
                    .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, productManageViewModel.itemId)
                    .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                            if (GlobalConfig.isSellerApp())
                                TopAdsSourceOption.SA_MANAGE_LIST_PRODUCT
                            else
                                TopAdsSourceOption.MA_MANAGE_LIST_PRODUCT).build().toString()

            RouteManager.route(it, uri)
        }
    }

    private fun onSetCashbackClicked(productManageViewModel: ProductManageViewModel) {
        activity?.let {
            if (!GlobalConfig.isSellerApp()) {
                RouteManager.route(context, ApplinkConst.SellerApp.POWER_MERCHANT_SUBSCRIBE)
                return
            }
            showOptionCashback(productManageViewModel.productId, productManageViewModel.productPricePlain,
                    productManageViewModel.productCurrencySymbol, productManageViewModel.productCashback)
        }
    }


    private fun showOptionCashback(productId: String, productPrice: String, productPriceSymbol: String, productCashback: Int) {
        val bottomSheetBuilder = CheckedBottomSheetBuilder(activity)
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem(getString(R.string.product_manage_cashback_title))

        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_3)
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_4)
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_5)
        addCashbackBottomSheetItemMenu(bottomSheetBuilder, productPrice, productPriceSymbol, productCashback, CashbackOption.CASHBACK_OPTION_NONE)

        val bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(onOptionCashbackClicked(productId))
                .createDialog()
        bottomSheetDialog.show()
    }

    private fun addCashbackBottomSheetItemMenu(bottomSheetBuilder: BottomSheetBuilder,
                                               productPrice: String, productPriceSymbol: String, productCashback: Int, @CashbackOption cashbackOption: Int) {
        if (bottomSheetBuilder is CheckedBottomSheetBuilder) {
            val productPricePlain = java.lang.Double.parseDouble(productPrice)
            bottomSheetBuilder.addItem(cashbackOption,
                    getCashbackMenuText(cashbackOption, productPriceSymbol, productPricePlain), null, productCashback == cashbackOption)
        }
    }

    private fun onOptionCashbackClicked(productId: String): BottomSheetItemClickListener {
        return BottomSheetItemClickListener {
            when (it.itemId) {
                CashbackOption.CASHBACK_OPTION_3 -> productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_3)
                CashbackOption.CASHBACK_OPTION_4 -> productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_4)
                CashbackOption.CASHBACK_OPTION_5 -> productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_5)
                CashbackOption.CASHBACK_OPTION_NONE -> productManagePresenter.setCashback(productId, CashbackOption.CASHBACK_OPTION_NONE)
                else -> {
                }
            }
            ProductManageTracking.eventProductManageOverflowMenu(getString(R.string.product_manage_cashback_title) + " - " + it.title)
        }
    }

    private fun getCashbackMenuText(cashback: Int, productPriceSymbol: String, productPricePlain: Double): String {
        var cashbackText = getString(R.string.product_manage_cashback_option_none)
        if (cashback > 0) {
            cashbackText = getString(R.string.product_manage_cashback_option, cashback.toString(),
                    productPriceSymbol,
                    KMNumbers.formatDouble2PCheckRound(cashback.toDouble() * productPricePlain / 100f, productPriceSymbol != "Rp"))
        }
        return cashbackText
    }

    fun downloadBitmap(productManageViewModel: ProductManageViewModel) {
        activity?.let {
            val productShare = ProductShare(it, ProductShare.MODE_IMAGE)

            val price = if (productManageViewModel.productCurrencyId == CurrencyTypeDef.TYPE_USD) productManageViewModel.productPricePlain else productManageViewModel.productPrice
            val data = ProductData()
            data.priceText = productManageViewModel.productCurrencySymbol + " " + price
            data.cashbacktext = if (productManageViewModel.productCashback > 0) getString(R.string.pml_sticker_cashback, productManageViewModel.productCashback) else ""
            data.currencySymbol = productManageViewModel.productCurrencySymbol
            data.productId = productManageViewModel.productId
            data.productName = productManageViewModel.productName
            data.productUrl = productManageViewModel.productUrl
            data.productImageUrl = productManageViewModel.imageFullUrl
            data.shopUrl = getString(R.string.pml_sticker_shop_link, shopDomain)

            productShare.share(data, { showLoadingProgress() }, { hideLoadingProgress() })
        }
    }

    private fun showDialogVariantPriceLocked() {
        activity?.let {
            val alertDialogBuilder = AlertDialog.Builder(it, R.style.AppCompatAlertDialogStyle)
                    .setTitle(getString(R.string.product_price_locked))
                    .setMessage(getString(R.string.product_price_locked_manage_desc))
                    .setPositiveButton(getString(R.string.close)) { dialogInterface, i ->
                        // no op, just dismiss
                    }
            val dialog = alertDialogBuilder.create()
            dialog.show()
        }
    }

    private fun showDialogChangeProductPrice(productId: String, productPrice: String, @CurrencyTypeDef productCurrencyId: Int) {
        if (!isAdded) {
            return
        }

        val productManageEditPriceDialogFragment = ProductManageEditPriceDialogFragment.createInstance(productId, productPrice, productCurrencyId, goldMerchant, isOfficialStore)
        productManageEditPriceDialogFragment.setListenerDialogEditPrice(object : ProductManageEditPriceDialogFragment.ListenerDialogEditPrice {
            override fun onSubmitEditPrice(productId: String, price: String, currencyId: String, currencyText: String) {
                productManagePresenter.editPrice(productId, price, currencyId, currencyText)
            }
        })
        activity?.let {
            fragmentManager?.let {
                productManageEditPriceDialogFragment.show(it, "")
            }
        }
    }

    private fun goToDuplicateProduct(productId: String) {
        activity?.let {
            val intent = ProductDuplicateActivity.createInstance(it, productId)
            startActivity(intent)
        }
    }

    private fun goToEditProduct(productId: String) {
        val intent = ProductEditActivity.createInstance(activity, productId)
        startActivity(intent)
    }

    private fun showDialogActionDeleteProduct(productIdList: List<String>, onClickListener: DialogInterface.OnClickListener, onCancelListener: DialogInterface.OnClickListener) {
        activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setTitle(R.string.label_delete)
            alertDialog.setMessage(R.string.dialog_delete_product)
            alertDialog.setPositiveButton(R.string.label_delete, onClickListener)
            alertDialog.setNegativeButton(R.string.title_cancel, onCancelListener)
            alertDialog.show()
        }
    }

    override fun onProductClicked(productManageViewModel: ProductManageViewModel) {
        productManageListAdapter.notifyDataSetChanged()
        goToPDP(productManageViewModel.productId)
        ProductManageTracking.eventProductManageClickDetail()
    }

    /**
     * This function is temporary for testing to avoid router and applink
     * For Dynamic Feature Support
     */
    private fun goToPDP(productId: String?) {
        if (productId != null) {
            RouteManager.route(context, ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        }
    }

    override fun goToEtalasePicker(etalaseId: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_ETALASE_PICKER,
                etalaseId.toString())
        startActivityForResult(intent, ETALASE_PICKER_REQUEST_CODE)
    }

    override fun goToEditStock() {
        activity?.let {
            startActivityForResult(ProductBulkEditStockActivity.createIntent(it), STOCK_EDIT_REQUEST_CODE)
        }
    }

    override fun goToConfirmationBottomSheet(isActionDelete: Boolean) {
        confirmationProductDataList = productManagePresenter.mapToProductConfirmationData(isActionDelete, stockType, etalaseType, itemsChecked)
        val confirmationUpdateProductBottomSheet = ConfirmationUpdateProductBottomSheet.newInstance(confirmationProductDataList)
        confirmationUpdateProductBottomSheet.setListener(this)
        fragmentManager?.let {
            confirmationUpdateProductBottomSheet.show(it, "bs_update_product")
        }
    }

    override fun updateProduct() {
        bulkBottomSheet.dismiss()
        productManagePresenter.bulkUpdateProduct(confirmationProductDataList)
    }

    override fun onItemClicked(t: ProductManageViewModel?) {
        // NO OP
    }

    override fun onPause() {
        super.onPause()
        activity?.let {
            activity?.unregisterReceiver(addProductReceiver)
        }
    }


    override fun onResume() {
        super.onResume()
        activity?.let {
            val intentFilter = IntentFilter()
            intentFilter.addAction(TkpdState.ProductService.BROADCAST_ADD_PRODUCT)
            it.registerReceiver(addProductReceiver, intentFilter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        productManagePresenter.detachView()
        activity?.let {
            if (addProductReceiver.isOrderedBroadcast) {
                it.unregisterReceiver(addProductReceiver)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        intent?.let {
            when (requestCode) {
                INSTAGRAM_SELECT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val imageUrls = it.getStringArrayListExtra(PICKER_RESULT_PATHS)
                    val imageDescList = it.getStringArrayListExtra(RESULT_IMAGE_DESCRIPTION_LIST)
                    if (imageUrls != null && imageUrls.size > 0) {
                        ProductDraftListActivity.startInstagramSaveBulkFromLocal(context, imageUrls, imageDescList)
                    }
                }
                REQUEST_CODE_FILTER -> if (resultCode == Activity.RESULT_OK) {
                    productManageFilterModel = it.getParcelableExtra(EXTRA_FILTER_SELECTED)
                    loadInitialData()
                    ProductManageTracking.trackingFilter(productManageFilterModel)
                }
                ETALASE_PICKER_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val etalaseId = it.getIntExtra(ProductExtraConstant.EXTRA_ETALASE_ID, -1)
                    val etalaseNameString = it.getStringExtra(ProductExtraConstant.EXTRA_ETALASE_NAME)
                    etalaseType.etalaseId = etalaseId
                    etalaseType.etalaseValue = etalaseNameString
                    editProductBottomSheet.setResultValue(etalaseType, null)
                }
                STOCK_EDIT_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                    val isActive = it.getBooleanExtra(EXTRA_STOCK, false)
                    val productStock: Int
                    productStock = if (isActive) 1 else 0
                    stockType.stockStatus = productStock
                    editProductBottomSheet.setResultValue(null, stockType)
                }
                REQUEST_CODE_SORT -> if (resultCode == Activity.RESULT_OK) {
                    val productManageSortModel: ProductManageSortModel = it.getParcelableExtra(EXTRA_SORT_SELECTED)
                    sortProductOption = productManageSortModel.sortId
                    loadInitialData()
                    ProductManageTracking.eventProductManageSortProduct(productManageSortModel.titleSort)
                }
                else -> super.onActivityResult(requestCode, resultCode, it)
            }
        }
    }

}