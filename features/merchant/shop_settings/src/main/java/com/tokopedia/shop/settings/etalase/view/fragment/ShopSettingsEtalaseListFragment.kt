@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.etalase.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.*
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
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.gm.common.constant.URL_POWER_MERCHANT_SCORE_TIPS
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.etalase.data.BaseShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.data.TickerReadMoreViewModel
import com.tokopedia.shop.settings.etalase.view.activity.ShopSettingsEtalaseAddEditActivity
import com.tokopedia.shop.settings.etalase.view.adapter.ShopEtalaseAdapter
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseFactory
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingEtalaseListPresenter
import com.tokopedia.shop.settings.etalase.view.viewholder.ShopEtalaseViewHolder
import com.tokopedia.shop.settings.etalase.view.viewholder.TickerReadMoreViewHolder
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject


class ShopSettingsEtalaseListFragment :
        BaseSearchListFragment<BaseShopEtalaseViewModel, ShopEtalaseFactory>(),
        ShopSettingEtalaseListPresenter.View,
        ShopEtalaseViewHolder.OnShopEtalaseViewHolderListener,
        TickerReadMoreViewHolder.TickerViewHolderViewHolderListener
{
    @Inject
    lateinit var shopSettingEtalaseListPresenter: ShopSettingEtalaseListPresenter
    private var shopEtalaseViewModels: ArrayList<ShopEtalaseViewModel>? = null
    private var shopEtalaseAdapter: ShopEtalaseAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var shopEtalaseIdToDelete: String? = null
    private var shopEtalaseNameToDelete: String? = null
    private var needReload: Boolean = false
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var userSession: UserSessionInterface? = null


    private var onShopSettingsEtalaseFragmentListener: OnShopSettingsEtalaseFragmentListener? = null

    override val keyword: String
        get() = searchInputView.searchText

    interface OnShopSettingsEtalaseFragmentListener {
        fun goToReorderFragment(shopEtalaseViewModelsDefault: ArrayList<ShopEtalaseViewModel>,
                                shopEtalaseViewModels: ArrayList<ShopEtalaseViewModel>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_search_list, container, false)
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        shopSettingEtalaseListPresenter.attachView(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        GraphqlClient.init(context!!)
        shopSettingEtalaseListPresenter.getShopEtalase()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideSearchInputView()
        userSession = UserSession(activity)
        searchInputView.setSearchHint(getString(R.string.search_etalase))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (shopEtalaseViewModels == null) {
            menu!!.clear()
        } else if (shopEtalaseViewModels!!.size == 0 || !hasCustomEtalaseAtLeast2(shopEtalaseViewModels!!)) {
            // if not etalase for reorder, show add only
            inflater!!.inflate(R.menu.menu_shop_etalase_list_no_data, menu)
        } else { // if there is etalase with reorder, will show reorder icon.
            inflater!!.inflate(R.menu.menu_shop_etalase_list, menu)
        }
    }

    private fun hasCustomEtalaseAtLeast2(shopEtalaseViewModelList: List<ShopEtalaseViewModel>): Boolean {
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        KeyboardHandler.DropKeyboard(context, view)
        if (item!!.itemId == R.id.menu_add) {
            onAddEtalaseButtonClicked()
            return true
        } else if (item.itemId == R.id.menu_reorder) {
            if (shopEtalaseViewModels == null || shopEtalaseViewModels!!.size == 0) {
                return true
            }
            val shopEtalaseViewModelListDefaultOnly = ArrayList<ShopEtalaseViewModel>()
            val shopEtalaseViewModelListCustomOnly = ArrayList<ShopEtalaseViewModel>()
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

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        swipeRefreshLayout = super.getSwipeRefreshLayout(view)
        return swipeRefreshLayout
    }

    override fun onSwipeRefresh() {
        super.onSwipeRefresh()
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        recyclerView = super.getRecyclerView(view)
        return recyclerView
    }

    private fun onAddEtalaseButtonClicked() {
        goToAddEtalase()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val searchText = searchInputView.searchText

        if (TextUtils.isEmpty(searchText)) {
            val emptyModel = EmptyModel()
            emptyModel.iconRes = R.drawable.ic_empty_state
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
            emptyModel.iconRes = R.drawable.ic_empty_search
            emptyModel.title = getString(R.string.shop_has_no_etalase_search, searchText)
            emptyModel.content = getString(R.string.change_your_keyword)
            return emptyModel
        }

    }

    override fun loadData(page: Int) {
        shopSettingEtalaseListPresenter.getShopEtalase()
    }

    override fun createAdapterInstance(): BaseListAdapter<BaseShopEtalaseViewModel, ShopEtalaseFactory> {
        shopEtalaseAdapter = ShopEtalaseAdapter(adapterTypeFactory, this)
        return shopEtalaseAdapter as ShopEtalaseAdapter
    }

    override fun onItemClicked(baseShopEtalaseViewModel: BaseShopEtalaseViewModel) {
        //no-op
    }

    private fun goToEditEtalase(shopEtalaseViewModel: ShopEtalaseViewModel) {
        val intent = ShopSettingsEtalaseAddEditActivity.createIntent(context!!, true, shopEtalaseViewModel)
        startActivityForResult(intent, REQUEST_CODE_EDIT_ETALASE)
    }

    private fun goToAddEtalase() {
        val intent = ShopSettingsEtalaseAddEditActivity.createIntent(context!!,
                false, ShopEtalaseViewModel())
        startActivityForResult(intent, REQUEST_CODE_ADD_ETALASE)
    }

    override fun getAdapterTypeFactory(): ShopEtalaseFactory {
        return ShopEtalaseFactory(this, this)
    }

    override fun onIconMoreClicked(shopEtalaseViewModel: ShopEtalaseViewModel) {
        val menus = Menus(context!!)
        if (shopEtalaseViewModel.count > 0) {
            menus.setItemMenuList(resources.getStringArray(R.array.shop_etalase_menu_more_change))
        } else {
            menus.setItemMenuList(resources.getStringArray(R.array.shop_etalase_menu_more_change_delete))
        }
        menus.setActionText(getString(R.string.close))
        menus.setOnActionClickListener { menus.dismiss() }
        menus.setOnItemMenuClickListener { itemMenus, _ ->
            if (itemMenus.title.equals(getString(R.string.label_change), ignoreCase = true)) {
                goToEditEtalase(shopEtalaseViewModel)
            } else {
                activity?.let { it ->
                    Dialog(it, Dialog.Type.PROMINANCE).apply {
                        setTitle(getString(R.string.title_dialog_delete_shop_etalase))
                        setDesc(getString(R.string.desc_dialog_delete_shop_etalase, shopEtalaseViewModel.name))
                        setBtnOk(getString(R.string.action_delete))
                        setBtnCancel(getString(R.string.cancel))
                        setOnOkClickListener {
                            shopEtalaseIdToDelete = shopEtalaseViewModel.id
                            shopEtalaseNameToDelete = shopEtalaseViewModel.name
                            showSubmitLoading(getString(R.string.title_loading))
                            shopSettingEtalaseListPresenter.deleteShopEtalase(shopEtalaseIdToDelete!!)
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

    override fun onSuccessGetShopEtalase(shopEtalaseViewModels: ArrayList<ShopEtalaseViewModel>) {
        this.shopEtalaseViewModels = shopEtalaseViewModels
        onSearchSubmitted(keyword)
        activity!!.invalidateOptionsMenu()
        if (isIdlePowerMerchant()) {
            addIdlePowerMerchantTicker()
        }
    }

    private fun isIdlePowerMerchant(): Boolean {
        return userSession!!.isGoldMerchant && userSession!!.isPowerMerchantIdle
    }

    private fun addIdlePowerMerchantTicker() {
        val model = TickerReadMoreViewModel(
                getString(R.string.ticker_etalase_title),
                getString(R.string.ticker_etalase_description),
                getString(R.string.ticker_etalase_read_more)
        )
        adapter.addElement(0, model)
    }

    override fun onErrorGetShopEtalase(throwable: Throwable) {
        showGetListError(throwable)
    }

    override fun onSearchSubmitted(text: String) {
        shopEtalaseAdapter!!.clearAllElements()
        isLoadingInitialData = true
        val tempShopEtalaseViewModels = ArrayList<BaseShopEtalaseViewModel>()
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
                if (requestCode == REQUEST_CODE_ADD_ETALASE) {
                    ToasterNormal.showClose(activity!!,
                            getString(R.string.success_add_etalase))
                } else if (requestCode == REQUEST_CODE_EDIT_ETALASE) {
                    ToasterNormal.showClose(activity!!,
                            getString(R.string.success_edit_etalase))
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

    override fun onSuccessDeleteShopEtalase(successMessage: String) {
        hideSubmitLoading()
        val deleteMessage = if (TextUtils.isEmpty(shopEtalaseNameToDelete))
            getString(R.string.etalase_success_delete)
        else
            getString(R.string.etalase_x_success_delete, shopEtalaseNameToDelete)
        ToasterNormal.showClose(activity!!,
                deleteMessage)
        loadInitialData()
    }

    fun refreshData() {
        loadInitialData()
    }

    override fun onErrorDeleteShopEtalase(throwable: Throwable) {
        hideSubmitLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        ToasterError.showClose(activity!!, message)
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

    override fun onDestroy() {
        super.onDestroy()
        shopSettingEtalaseListPresenter.detachView()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopSettingsEtalaseFragmentListener = context as OnShopSettingsEtalaseFragmentListener
    }

    override fun onReadMoreClicked() {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, URL_POWER_MERCHANT_SCORE_TIPS)
    }

    companion object {

        private val REQUEST_CODE_ADD_ETALASE = 605
        private val REQUEST_CODE_EDIT_ETALASE = 606

        @JvmStatic
        fun newInstance(): ShopSettingsEtalaseListFragment {
            return ShopSettingsEtalaseListFragment()
        }
    }
}
