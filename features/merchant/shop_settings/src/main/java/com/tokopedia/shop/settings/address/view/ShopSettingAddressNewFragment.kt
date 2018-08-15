package com.tokopedia.shop.settings.address.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import com.tokopedia.shop.settings.address.di.component.ShopLocationComponent
import com.tokopedia.shop.settings.address.presenter.ShopLocationPresenter
import com.tokopedia.shop.settings.address.view.adapter.ShopLocationTypeFactory
import com.tokopedia.shop.settings.address.view.listener.ShopLocationView
import javax.inject.Inject
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.address.view.viewholder.ShopLocationViewHolder

class ShopSettingAddressNewFragment : BaseListFragment<ShopLocationViewModel, ShopLocationTypeFactory>(),
        ShopLocationView, ShopLocationViewHolder.OnIconMoreClicked {

    @Inject lateinit var presenter: ShopLocationPresenter

    companion object {
        private const val REQUEST_CODE_ADD_ADDRESS = 1
        private const val REQUEST_CODE_EDIT_ADDRESS = 2
        fun createInstance() = ShopSettingAddressNewFragment()
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ShopLocationComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { GraphqlClient.init(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_setting_address_list, container, false)
    }

    override fun getAdapterTypeFactory() = ShopLocationTypeFactory(this)

    override fun onItemClicked(t: ShopLocationViewModel?) {

    }

    override fun isLoadMoreEnabledByDefault() = false

    override fun loadData(page: Int) {
        presenter.getShopAddress()
    }

    override fun onSuccessLoadAddresses(addresses: List<ShopLocationViewModel>?) {
        super.renderList(addresses ?: listOf())
    }

    override fun onErrorLoadAddresses(throwable: Throwable?) {
    }

    override fun onSuccessDeleteAddress(string: String?) {
        ToasterNormal.make(view, getString(R.string.success_delete_shop_address), BaseToaster.LENGTH_SHORT)
                .setAction(R.string.close){}.show()
        presenter.getShopAddress()
    }

    override fun onErrorDeleteAddress(throwable: Throwable?) {
        throwable?.let {ToasterError.make(view, it.localizedMessage, BaseToaster.LENGTH_SHORT)
                .setAction(R.string.close){}.show()}
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_add_shop_address, menu)
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_add){
            if (isValidToAdd()){
                activity?.run {startActivityForResult(ShopSettingAddressAddEditActivity
                        .createIntent(this, null, true), REQUEST_CODE_ADD_ADDRESS)}
            } else {
                Toast.makeText(activity, getString(R.string.error_max_shop_address), Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onIconClicked(item: ShopLocationViewModel, pos: Int) {
        activity?.let {
            Menus(it).apply {
                setItemMenuList(resources.getStringArray(R.array.shop_address_menu_more))
                setActionText(getString(R.string.close))
                setOnActionClickListener { dismiss() }
                setOnItemMenuClickListener { itemMenus, pos -> when(pos){
                    0 -> {editShopAddress(item)
                            dismiss()}
                    1 -> {deleteShopAddress(item)
                            dismiss()}
                } }
                show()
            }
        }
    }

    private fun deleteShopAddress(item: ShopLocationViewModel) {
        activity?.let {
            Dialog(it, Dialog.Type.PROMINANCE).apply {
                setTitle(getString(R.string.title_dialog_delete_shop_address))
                setDesc(getString(R.string.desc_dialog_delete_shop_address, item.name))
                setBtnOk(getString(R.string.action_delete))
                setBtnCancel(getString(R.string.cancel))
                setOnOkClickListener { presenter.deleteItem(item); dismiss()}
                setOnCancelClickListener { dismiss() }
                show()
            }
        }
    }

    private fun editShopAddress(item: ShopLocationViewModel) {
        activity?.run {
            startActivityForResult(ShopSettingAddressAddEditActivity
                    .createIntent(this, item, false), REQUEST_CODE_EDIT_ADDRESS)
        }
    }

    private fun isValidToAdd() = adapter.dataSize < 3
}