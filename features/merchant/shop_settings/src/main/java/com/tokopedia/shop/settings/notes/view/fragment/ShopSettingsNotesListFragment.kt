@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.notes.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.Menus
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel
import com.tokopedia.shop.settings.notes.view.activity.ShopSettingNotesAddEditActivity
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteAdapter
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteFactory
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListPresenter
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder
import javax.inject.Inject


class ShopSettingsNotesListFragment : BaseListFragment<ShopNoteViewModel, ShopNoteFactory>(), ShopSettingNoteListPresenter.View,
        ShopNoteViewHolder.OnShopNoteViewHolderListener {
    @Inject
    lateinit var shopSettingNoteListPresenter: ShopSettingNoteListPresenter
    private var shopNoteModels: ArrayList<ShopNoteViewModel>? = null
    private var shopNoteAdapter: ShopNoteAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var shopNoteIdToDelete: String? = null
    private var needReload: Boolean = false
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var onShopSettingsNoteFragmentListener: OnShopSettingsNoteFragmentListener? = null

    interface OnShopSettingsNoteFragmentListener {
        fun goToReorderFragment(shopNoteViewModels: ArrayList<ShopNoteViewModel>)
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        shopSettingNoteListPresenter.attachView(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        GraphqlClient.init(context!!)
        shopSettingNoteListPresenter.getShopNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (shopNoteModels == null) {
            menu!!.clear()
        } else if (shopNoteModels!!.size == 0 || getNonTermsCount(shopNoteModels!!) < MIN_DATA_TO_REORDER) {
            inflater!!.inflate(R.menu.menu_shop_note_list_no_data, menu)
        } else {
            inflater!!.inflate(R.menu.menu_shop_note_list, menu)
        }
    }

    private fun getNonTermsCount(shopNoteViewModels: List<ShopNoteViewModel>): Int {
        var count = 0
        for (shopNoteViewModel in shopNoteViewModels) {
            if (!shopNoteViewModel.terms) {
                count++
            }
        }
        return count
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        KeyboardHandler.DropKeyboard(context, view)
        if (item!!.itemId == R.id.menu_add) {
            onAddNoteButtonClicked()
            return true
        } else if (item.itemId == R.id.menu_reorder) {
            if (shopNoteModels == null || shopNoteModels!!.size == 0) {
                return true
            }
            onShopSettingsNoteFragmentListener!!.goToReorderFragment(shopNoteModels!!)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        swipeRefreshLayout = super.getSwipeRefreshLayout(view)
        return swipeRefreshLayout
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        recyclerView = super.getRecyclerView(view)
        return recyclerView
    }

    private fun onAddNoteButtonClicked() {
        val menus = Menus(context!!)
        menus.setItemMenuList(resources.getStringArray(R.array.shop_note_type))
        menus.setOnItemMenuClickListener { _, pos ->
            if (pos == 0) {
                goToAddNote(false)
            } else {
                goToAddNote(true)
            }
            menus.dismiss()
        }
        menus.show()
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = com.tokopedia.design.R.drawable.ic_empty_state
        emptyModel.title = getString(R.string.shop_has_no_notes)
        emptyModel.content = getString(R.string.shop_notes_info)
        emptyModel.buttonTitleRes = R.string.add_note
        emptyModel.callback = object : BaseEmptyViewHolder.Callback {
            override fun onEmptyContentItemTextClicked() {

            }

            override fun onEmptyButtonClicked() {
                onAddNoteButtonClicked()
            }
        }
        return emptyModel
    }

    override fun loadData(page: Int) {
        shopSettingNoteListPresenter.getShopNotes()
    }

    override fun createAdapterInstance(): BaseListAdapter<ShopNoteViewModel, ShopNoteFactory> {
        shopNoteAdapter = ShopNoteAdapter(adapterTypeFactory, this)
        return shopNoteAdapter as ShopNoteAdapter
    }

    override fun onItemClicked(shopNoteViewModel: ShopNoteViewModel) {
        //no-op
    }

    private fun goToEditNote(shopNoteViewModel: ShopNoteViewModel) {
        val intent = ShopSettingNotesAddEditActivity.createIntent(context!!,
                shopNoteViewModel.terms, true, shopNoteViewModel)
        startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE)
    }

    private fun goToAddNote(isTerms: Boolean) {
        if (isTerms) { // can only has 1 term
            if (shopNoteModels != null && shopNoteModels!!.size > 0 && shopNoteModels!![0].terms) {
                ToasterError.showClose(activity!!, getString(R.string.can_only_have_one_term))
                return
            }
        } else { // can only has 3 notes maks
            if (shopNoteModels != null && getNonTermsCount(shopNoteModels!!) >= 3) {
                ToasterError.showClose(activity!!, getString(R.string.can_only_have_three_note))
                return
            }
        }
        val intent = ShopSettingNotesAddEditActivity.createIntent(context!!,
                isTerms, false, ShopNoteViewModel())
        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE)
    }

    override fun getAdapterTypeFactory(): ShopNoteFactory {
        return ShopNoteFactory(this)
    }

    override fun onIconMoreClicked(shopNoteViewModel: ShopNoteViewModel) {
        val menus = Menus(context!!)
        menus.setItemMenuList(resources.getStringArray(R.array.shop_note_menu_more))
        menus.setActionText(getString(com.tokopedia.abstraction.R.string.close))
        menus.setOnActionClickListener { menus.dismiss() }
        menus.setOnItemMenuClickListener { _, pos ->
            if (pos == 0) {
                goToEditNote(shopNoteViewModel)
            } else {
                activity?.let { it ->
                    Dialog(it, Dialog.Type.PROMINANCE).apply {
                        setTitle(getString(R.string.title_dialog_delete_shop_note))
                        setDesc(getString(R.string.desc_dialog_delete_shop_note, shopNoteViewModel.title))
                        setBtnOk(getString(R.string.action_delete))
                        setBtnCancel(getString(com.tokopedia.imagepicker.R.string.cancel))
                        setOnOkClickListener {
                            shopNoteIdToDelete = shopNoteViewModel.id
                            showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
                            shopSettingNoteListPresenter.deleteShopNote(shopNoteIdToDelete!!)
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

    override fun onSuccessGetShopNotes(shopNoteModels: ArrayList<ShopNoteViewModel>) {
        this.shopNoteModels = shopNoteModels
        renderList(shopNoteModels, false)
        activity!!.invalidateOptionsMenu()
    }

    override fun onErrorGetShopNotes(throwable: Throwable) {
        showGetListError(throwable)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_EDIT_NOTE, REQUEST_CODE_ADD_NOTE -> if (resultCode == Activity.RESULT_OK) {
                needReload = true
                if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    ToasterNormal.showClose(activity!!,
                            getString( R.string.success_add_note ))
                } else if (requestCode == REQUEST_CODE_EDIT_NOTE){
                    ToasterNormal.showClose(activity!!,
                            getString( R.string.success_edit_note ))
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

    override fun onSuccessDeleteShopNote(successMessage: String) {
        hideSubmitLoading()
        ToasterNormal.showClose(activity!!, getString(R.string.note_success_delete))
        loadInitialData()
    }

    fun refreshData() {
        loadInitialData()
    }

    override fun onErrorDeleteShopNote(throwable: Throwable) {
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
        shopSettingNoteListPresenter.detachView()
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        onShopSettingsNoteFragmentListener = context as OnShopSettingsNoteFragmentListener
    }

    companion object {

        private val REQUEST_CODE_ADD_NOTE = 818
        private val REQUEST_CODE_EDIT_NOTE = 819
        val MIN_DATA_TO_REORDER = 2

        @JvmStatic
        fun newInstance(): ShopSettingsNotesListFragment {
            return ShopSettingsNotesListFragment()
        }
    }
}
