@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.etalase.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.EmptyResultViewModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.gm.common.constant.URL_POWER_MERCHANT_SCORE_TIPS
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.BaseShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreUiModel
import com.tokopedia.shop.settings.etalase.view.activity.ShopSettingsEtalaseAddEditActivity
import com.tokopedia.shop.settings.etalase.view.adapter.ShopEtalaseAdapter
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseFactory
import com.tokopedia.shop.settings.etalase.view.viewmodel.ShopSettingsEtalaseListViewModel
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseViewHolder
import com.tokopedia.shop.settings.etalase.view.viewholder.TickerReadMoreEtalaseViewHolder
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject


class ShopSettingsEtalaseListFragment :
        BaseSearchListFragment<BaseShopEtalaseUiModel, ShopEtalaseFactory>(),
        ShopEtalaseViewHolder.OnShopEtalaseViewHolderListener,
        TickerReadMoreEtalaseViewHolder.TickerReadMoreListener
{
    @Inject
    lateinit var viewModel: ShopSettingsEtalaseListViewModel
    @Inject
    lateinit var userSession: UserSessionInterface
    private var shopEtalaseViewModels: ArrayList<ShopEtalaseUiModel>? = null
    private var shopEtalaseAdapter: ShopEtalaseAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var shopEtalaseIdToDelete: String? = null
    private var shopEtalaseNameToDelete: String? = null
    private var needReload: Boolean = false

    private var onShopSettingsEtalaseFragmentListener: OnShopSettingsEtalaseFragmentListener? = null

    override val keyword: String
        get() = searchInputView.searchText

    interface OnShopSettingsEtalaseFragmentListener {
        fun goToReorderFragment(shopEtalaseViewModelsDefault: ArrayList<ShopEtalaseUiModel>,
                                shopEtalaseViewModels: ArrayList<ShopEtalaseUiModel>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_search_list, container, false)
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        GraphqlClient.init(requireContext())
        viewModel.getShopEtalase()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSearchInputView()
        searchInputView.setSearchHint(getString(R.string.search_etalase))
        observeData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (shopEtalaseViewModels == null) {
            menu.clear()
        } else if (shopEtalaseViewModels!!.size == 0 || !hasCustomEtalaseAtLeast2(shopEtalaseViewModels!!)) {
            // if not etalase for reorder, show add only
            inflater.inflate(R.menu.menu_shop_etalase_list_no_data, menu)
        } else { // if there is etalase with reorder, will show reorder icon.
            inflater.inflate(R.menu.menu_shop_etalase_list, menu)
        }
    }

    override fun getSearchInputView(view: View): SearchInputView {
        return view.findViewById(R.id.search_input_view)
    }

    override fun getSwipeRefreshLayoutResourceId() = R.id.swipe_refresh_layout

    override fun getRecyclerViewResourceId() = R.id.recycler_view

    private fun hasCustomEtalaseAtLeast2(shopEtalaseViewModelList: List<ShopEtalaseUiModel>): Boolean {
        var count = 0
        for (shopEtalaseViewModel in shopEtalaseViewModelList) {
            if (shopEtalaseViewModel.type == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
                count++
                if (count >= 2) {
                    return true
                }
            }
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        KeyboardHandler.DropKeyboard(context, view)
        if (item.itemId == R.id.menu_add) {
            onAddEtalaseButtonClicked()
            return true
        } else if (item.itemId == R.id.menu_reorder) {
            if (shopEtalaseViewModels == null || shopEtalaseViewModels!!.size == 0) {
                return true
            }
            val shopEtalaseViewModelListDefaultOnly = ArrayList<ShopEtalaseUiModel>()
            val shopEtalaseViewModelListCustomOnly = ArrayList<ShopEtalaseUiModel>()
            for (shopEtalaseViewModel in shopEtalaseViewModels!!) {
                if (shopEtalaseViewModel.type == ShopEtalaseTypeDef.ETALASE_CUSTOM) {
                    shopEtalaseViewModelListCustomOnly.add(shopEtalaseViewModel)
                } else {
                    shopEtalaseViewModelListDefaultOnly.add(shopEtalaseViewModel)
                }
            }
            if (shopEtalaseViewModelListCustomOnly.size == 0) {
                return true
            }
            onShopSettingsEtalaseFragmentListener!!.goToReorderFragment(shopEtalaseViewModelListDefaultOnly,
                    shopEtalaseViewModelListCustomOnly)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAddEtalaseButtonClicked() {
        goToAddEtalase()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val searchText = searchInputView.searchText

        if (TextUtils.isEmpty(searchText)) {
            val emptyModel = EmptyModel()
            emptyModel.iconRes = com.tokopedia.design.R.drawable.ic_empty_state
            emptyModel.title = getString(R.string.shop_has_no_etalase)
            emptyModel.content = getString(R.string.shop_etalase_info)
            emptyModel.buttonTitleRes = R.string.shop_settings_add_etalase
            emptyModel.callback = object : BaseEmptyViewHolder.Callback {
                override fun onEmptyContentItemTextClicked() {

                }

                override fun onEmptyButtonClicked() {
                    onAddEtalaseButtonClicked()
                }
            }
            return emptyModel
        } else {
            val emptyModel = EmptyResultViewModel()
            emptyModel.iconRes = com.tokopedia.design.R.drawable.ic_empty_search
            emptyModel.title = getString(R.string.shop_has_no_etalase_search, searchText)
            emptyModel.content = getString(R.string.change_your_keyword)
            return emptyModel
        }

    }

    override fun loadData(page: Int) {
        viewModel.getShopEtalase()
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopEtalaseUiModel, ShopEtalaseFactory> {
        shopEtalaseAdapter = ShopEtalaseAdapter(adapterTypeFactory, this)
        return shopEtalaseAdapter as ShopEtalaseAdapter
    }

    override fun onItemClicked(baseShopEtalaseUiModel: BaseShopEtalaseUiModel) {
        //no-op
    }

    private fun observeData() {
        observe(viewModel.shopEtalase) { result ->
            when(result) {
                is Success -> onSuccessGetShopEtalase(result.data)
                is Fail -> onErrorGetShopEtalase(result.throwable)
            }
        }

        observe(viewModel.deleteMessage) { result ->
            when(result) {
                is Success -> onSuccessDeleteShopEtalase(result.data)
                is Fail -> onErrorDeleteShopEtalase(result.throwable)
            }
        }
    }

    private fun goToEditEtalase(shopEtalaseViewModel: ShopEtalaseUiModel) {
        val intent = ShopSettingsEtalaseAddEditActivity.createIntent(requireContext(), true, shopEtalaseViewModel)
        startActivityForResult(intent, REQUEST_CODE_EDIT_ETALASE)
    }

    private fun goToAddEtalase() {
        val intent = ShopSettingsEtalaseAddEditActivity.createIntent(requireContext(),
                false, ShopEtalaseUiModel())
        startActivityForResult(intent, REQUEST_CODE_ADD_ETALASE)
    }

    override fun getAdapterTypeFactory(): ShopEtalaseFactory {
        return ShopEtalaseFactory(this, this)
    }

    override fun onIconMoreClicked(shopEtalaseViewModel: ShopEtalaseUiModel) {
        val menus = Menus(requireContext())
        if (shopEtalaseViewModel.count > 0) {
            menus.setItemMenuList(resources.getStringArray(R.array.shop_etalase_menu_more_change))
        } else {
            menus.setItemMenuList(resources.getStringArray(R.array.shop_etalase_menu_more_change_delete))
        }
        menus.setActionText(getString(com.tokopedia.abstraction.R.string.close))
        menus.setOnActionClickListener { menus.dismiss() }
        menus.setOnItemMenuClickListener { itemMenus, _ ->
            if (itemMenus.title.equals(getString(com.tokopedia.design.R.string.label_change), ignoreCase = true)) {
                goToEditEtalase(shopEtalaseViewModel)
            } else {
                activity?.let { it ->
                    Dialog(it, Dialog.Type.PROMINANCE).apply {
                        setTitle(getString(R.string.title_dialog_delete_shop_etalase))
                        setDesc(getString(R.string.desc_dialog_delete_shop_etalase, shopEtalaseViewModel.name))
                        setBtnOk(getString(R.string.action_delete))
                        setBtnCancel(getString(com.tokopedia.imagepicker.R.string.cancel))
                        setOnOkClickListener {
                            shopEtalaseIdToDelete = shopEtalaseViewModel.id
                            shopEtalaseNameToDelete = shopEtalaseViewModel.name
                            showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
                            shopEtalaseIdToDelete?.let {
                                viewModel.deleteShopEtalase(it)
                            }
                            dismiss()
                        }
                        setOnCancelClickListener { dismiss() }
                        show()
                    }
                }
            }
            menus.dismiss()
        }
        menus.show()
    }

    private fun onSuccessGetShopEtalase(shopEtalaseViewModels: ArrayList<ShopEtalaseUiModel>) {
        this.shopEtalaseViewModels = shopEtalaseViewModels
        onSearchSubmitted(keyword)
        activity?.invalidateOptionsMenu()
        if (isIdlePowerMerchant()) {
            addIdlePowerMerchantTicker()
        }
    }

    private fun isIdlePowerMerchant(): Boolean {
        return userSession.isPowerMerchantIdle
    }

    private fun addIdlePowerMerchantTicker() {
        val model = TickerReadMoreUiModel(
                getString(R.string.ticker_etalase_title),
                getString(R.string.ticker_etalase_description),
                getString(R.string.ticker_etalase_read_more)
        )
        adapter.addElement(0, model)
    }

    private fun onErrorGetShopEtalase(throwable: Throwable) {
        showGetListError(throwable)
    }

    override fun onSearchSubmitted(text: String) {
        shopEtalaseAdapter!!.clearAllElements()
        isLoadingInitialData = true
        val tempShopEtalaseViewModels = ArrayList<BaseShopEtalaseUiModel>()
        if (this.shopEtalaseViewModels != null &&
                this.shopEtalaseViewModels!!.size > 0) {
            val textLowerCase = text.toLowerCase()
            for (shopEtalaseViewModel in this.shopEtalaseViewModels!!) {
                if (shopEtalaseViewModel.name.toLowerCase().contains(textLowerCase)) {
                    tempShopEtalaseViewModels.add(shopEtalaseViewModel)
                }
            }
        }
        renderList(tempShopEtalaseViewModels, false)
        showSearchViewWithDataSizeCheck()
    }


    override fun onSearchTextChanged(text: String) {
        onSearchSubmitted(text)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_EDIT_ETALASE,
            REQUEST_CODE_ADD_ETALASE -> if (resultCode == Activity.RESULT_OK) {
                needReload = true
                view?.let {
                    if (requestCode == REQUEST_CODE_ADD_ETALASE) {
                        Toaster.make(it, getString(R.string.success_add_etalase), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                    } else if (requestCode == REQUEST_CODE_EDIT_ETALASE) {
                        Toaster.make(it, getString(R.string.success_edit_etalase), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (needReload) {
            loadInitialData()
            needReload = false
        }
    }

    private fun onSuccessDeleteShopEtalase(successMessage: String) {
        hideSubmitLoading()
        val deleteMessage = if (TextUtils.isEmpty(shopEtalaseNameToDelete))
            getString(R.string.etalase_success_delete)
        else
            getString(R.string.etalase_x_success_delete, shopEtalaseNameToDelete)
        view?.let {
            Toaster.make(it, deleteMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
        }
        loadInitialData()
    }

    fun refreshData() {
        loadInitialData()
    }

    private fun onErrorDeleteShopEtalase(throwable: Throwable) {
        hideSubmitLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    fun showSubmitLoading(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
        }
        if (!progressDialog!!.isShowing) {
            progressDialog!!.setMessage(message)
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
    }

    fun hideSubmitLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog = null
        }
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopSettingsEtalaseFragmentListener = context as OnShopSettingsEtalaseFragmentListener
    }

    override fun onReadMoreClicked() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_POWER_MERCHANT_SCORE_TIPS)
    }

    companion object {

        private const val REQUEST_CODE_ADD_ETALASE = 605
        private const val REQUEST_CODE_EDIT_ETALASE = 606
        const val PRIMARY_ETALASE_LIMIT = 5

        @JvmStatic
        fun newInstance(): ShopSettingsEtalaseListFragment {
            return ShopSettingsEtalaseListFragment()
        }
    }
}
