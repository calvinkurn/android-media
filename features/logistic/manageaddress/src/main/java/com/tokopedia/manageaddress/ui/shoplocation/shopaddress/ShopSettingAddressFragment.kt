package com.tokopedia.manageaddress.ui.shoplocation.shopaddress

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.ShopLocationComponent
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.adapter.ShopLocationOldTypeFactory
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.viewholder.ShopLocationViewHolder
import com.tokopedia.shop.settings.address.presenter.ShopLocationOldPresenter
import com.tokopedia.shop.settings.address.view.listener.ShopLocationOldView
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class ShopSettingAddressFragment : BaseListFragment<ShopLocationOldUiModel, ShopLocationOldTypeFactory>(),
        ShopLocationOldView, ShopLocationViewHolder.OnIconMoreClicked, BaseEmptyViewHolder.Callback {

    @Inject
    lateinit var oldPresenter: ShopLocationOldPresenter

    var shopLocationOldUiModelList: List<ShopLocationOldUiModel>? = null

    companion object {
        private const val REQUEST_CODE_ADD_ADDRESS = 1
        private const val REQUEST_CODE_EDIT_ADDRESS = 2

        private const val PARAM_EXTRA_IS_SUCCESS = "is_success"
        private const val PARAM_EXTRA_IS_ADD_NEW = "is_add_new"

        fun createInstance() = ShopSettingAddressFragment()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ShopLocationComponent::class.java).inject(this)
        oldPresenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context?.let { GraphqlClient.init(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_setting_address_list, container, false)
    }

    override fun getAdapterTypeFactory() = ShopLocationOldTypeFactory(this)

    override fun onItemClicked(t: ShopLocationOldUiModel?) {
        //no-op
    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun loadData(page: Int) {
        oldPresenter.getShopAddress()
    }

    override fun getEmptyDataViewModel() = EmptyModel().apply {
        iconRes = R.drawable.ic_shop_settings_empty_address
        title = getString(R.string.title_shop_settings_empty_address)
        contentRes = R.string.content_shop_settings_empty_address
        buttonTitleRes = R.string.button_shop_settings_empty_address
        callback = this@ShopSettingAddressFragment
    }

    override fun onSuccessLoadAddresses(addresses: List<ShopLocationOldUiModel>?) {
        shopLocationOldUiModelList = addresses
        super.renderList(addresses ?: listOf())
        activity?.invalidateOptionsMenu()
    }

    override fun onErrorLoadAddresses(throwable: Throwable?) {
        hideLoading()
        super.showGetListError(throwable);
    }

    override fun onSuccessDeleteAddress(string: String?) {
        view?.let {
            Toaster.make(it, getString(R.string.success_delete_shop_address), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.abstraction.R.string.close), View.OnClickListener {  })
        }
        loadInitialData()
    }

    override fun onErrorDeleteAddress(throwable: Throwable?) {
        throwable?.let {
            Toaster.make(requireView(), ErrorHandler.getErrorMessage(activity, it), Snackbar.LENGTH_SHORT, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.close), View.OnClickListener {  })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (shopLocationOldUiModelList == null) {
            menu?.clear()
        } else {
            inflater?.inflate(R.menu.menu_add_shop_address, menu)
        }
    }

    override fun onDestroyView() {
        oldPresenter.detachView()
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.menu_add) {
            createAddress()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createAddress() {
        if (isValidToAdd()) {
            context?.run {
                startActivityForResult(ShopSettingAddressAddEditActivity
                        .createIntent(this, null, true), REQUEST_CODE_ADD_ADDRESS)
            }
        } else {
            Toast.makeText(activity, getString(R.string.error_max_shop_address), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onIconClicked(item: ShopLocationOldUiModel, pos: Int) {
        activity?.let {
            Menus(it).apply {
                setItemMenuList(resources.getStringArray(R.array.shop_address_menu_more))
                setActionText(getString(com.tokopedia.abstraction.R.string.close))
                setOnActionClickListener { dismiss() }
                setOnItemMenuClickListener { _, pos ->
                    when (pos) {
                        0 -> {
                            editShopAddress(item)
                            dismiss()
                        }
                        1 -> {
                            deleteShopAddress(item)
                            dismiss()
                        }
                    }
                }
                show()
            }
        }
    }

    private fun deleteShopAddress(item: ShopLocationOldUiModel) {
        activity?.let {
            Dialog(it, Dialog.Type.PROMINANCE).apply {
                setTitle(getString(R.string.title_dialog_delete_shop_address))
                setDesc(getString(R.string.desc_dialog_delete_shop_address, item.name))
                setBtnOk(getString(R.string.action_delete))
                setBtnCancel(getString(R.string.action_cancel))
                setOnOkClickListener { oldPresenter.deleteItem(item); dismiss() }
                setOnCancelClickListener { dismiss() }
                show()
            }
        }
    }

    private fun editShopAddress(item: ShopLocationOldUiModel) {
        context?.let {
            startActivityForResult(ShopSettingAddressAddEditActivity
                    .createIntent(it, item, false), REQUEST_CODE_EDIT_ADDRESS)
        }
    }

    private fun isValidToAdd() = adapter.dataSize < 3

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && (requestCode == REQUEST_CODE_ADD_ADDRESS || requestCode == REQUEST_CODE_EDIT_ADDRESS)) {
            val isSuccess = data.getBooleanExtra(PARAM_EXTRA_IS_SUCCESS, false)
            val isNew = data.getBooleanExtra(PARAM_EXTRA_IS_ADD_NEW, false)
            if (isSuccess) {
                loadInitialData()
                view?.let {
                    Toaster.make(it, getString(if (isNew) R.string.success_add_address else R.string.success_edit_address), Snackbar.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
            }
        }
    }

    override fun onEmptyContentItemTextClicked() {}

    override fun onEmptyButtonClicked() {
        createAddress()
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById<View>(R.id.recycler_view) as RecyclerView
    }
}