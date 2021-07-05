@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.notes.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.BaseEmptyViewHolder
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.view.adapter.viewholder.MenuViewHolder
import com.tokopedia.shop.settings.common.view.bottomsheet.MenuBottomSheet
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.activity.ShopSettingsNotesAddEditActivity
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteAdapter
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteFactory
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListPresenter
import com.tokopedia.shop.settings.notes.view.viewholder.ShopNoteViewHolder
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject


class ShopSettingsNotesListFragment : BaseListFragment<ShopNoteUiModel, ShopNoteFactory>(), ShopSettingNoteListPresenter.View,
        ShopNoteViewHolder.OnShopNoteViewHolderListener {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(com.tokopedia.baselist.R.layout.fragment_base_list, container, false)
    }

    override fun getRecyclerViewResourceId() = com.tokopedia.baselist.R.id.recycler_view

    override fun getSwipeRefreshLayoutResourceId() = com.tokopedia.baselist.R.id.swipe_refresh_layout

    @Inject
    lateinit var shopSettingNoteListPresenter: ShopSettingNoteListPresenter
    private var shopNoteModels: ArrayList<ShopNoteUiModel>? = null
    private var shopNoteAdapter: ShopNoteAdapter? = null
    private var progressDialog: ProgressDialog? = null
    private var addNoteBottomSheet: MenuBottomSheet? = null
    private var iconMoreBottomSheet: MenuBottomSheet? = null
    private var shopNoteIdToDelete: String? = null
    private var needReload: Boolean = false

    private var onShopSettingsNoteFragmentListener: OnShopSettingsNoteFragmentListener? = null

    interface OnShopSettingsNoteFragmentListener {
        fun goToReorderFragment(shopNoteUiModels: ArrayList<ShopNoteUiModel>)
    }

    override fun initInjector() {
        activity?.let {
            DaggerShopSettingsComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
        shopSettingNoteListPresenter.attachView(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
        context?.let {
            GraphqlClient.init(it)
        }
        shopSettingNoteListPresenter.getShopNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (shopNoteModels == null) {
            menu.clear()
        } else if (shopNoteModels!!.size == 0 || getNonTermsCount(shopNoteModels!!) < MIN_DATA_TO_REORDER) {
            inflater.inflate(R.menu.menu_shop_note_list_no_data, menu)
        } else {
            inflater.inflate(R.menu.menu_shop_note_list, menu)
        }
    }

    private fun getNonTermsCount(shopNoteUiModels: List<ShopNoteUiModel>): Int {
        var count = 0
        for (shopNoteViewModel in shopNoteUiModels) {
            if (!shopNoteViewModel.terms) {
                count++
            }
        }
        return count
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        KeyboardHandler.DropKeyboard(context, view)
        if (item.itemId == R.id.menu_add) {
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

    private fun onAddNoteButtonClicked() {
        context?.let {
            val itemList = arrayListOf<String>()
            itemList.addAll(resources.getStringArray(R.array.shop_note_type))
            addNoteBottomSheet = MenuBottomSheet.newInstance(itemList)
            addNoteBottomSheet?.setListener(object : MenuViewHolder.ItemMenuListener {
                override fun onItemMenuClicked(text: String, position: Int) {
                    if (position == 0) {
                        goToAddNote(false)
                    } else {
                        goToAddNote(true)
                    }
                    addNoteBottomSheet?.dismiss()
                }
                override fun itemMenuSize(): Int = itemList.size
            })
            addNoteBottomSheet?.showHeader = false
            addNoteBottomSheet?.show(childFragmentManager, "menu_bottom_sheet")
        }
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyModel()
        emptyModel.iconRes = R.drawable.ic_tree_empty_state
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

    override fun createAdapterInstance(): BaseListAdapter<ShopNoteUiModel, ShopNoteFactory> {
        shopNoteAdapter = ShopNoteAdapter(adapterTypeFactory, this)
        return shopNoteAdapter as ShopNoteAdapter
    }

    override fun onItemClicked(shopNoteUiModel: ShopNoteUiModel) {
        //no-op
    }

    private fun goToEditNote(shopNoteUiModel: ShopNoteUiModel) {
        context?.let {
            val intent = ShopSettingsNotesAddEditActivity.createIntent(it,
                    shopNoteUiModel.terms, true, shopNoteUiModel)
            startActivityForResult(intent, REQUEST_CODE_EDIT_NOTE)
        }
    }

    private fun goToAddNote(isTerms: Boolean) {
        if (isTerms) { // can only has 1 term
            if (shopNoteModels != null && shopNoteModels!!.size > 0 && shopNoteModels!![0].terms) {
                view?.let {
                    Toaster.make(it, getString(R.string.can_only_have_one_term), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
                return
            }
        } else { // can only has 3 notes maks
            if (shopNoteModels != null && getNonTermsCount(shopNoteModels!!) >= 3) {
                view?.let {
                    Toaster.make(it, getString(R.string.can_only_have_three_note), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR)
                }
                return
            }
        }
        context?.let {
            val intent = ShopSettingsNotesAddEditActivity.createIntent(it,
                    isTerms, false, ShopNoteUiModel())
            startActivityForResult(intent, REQUEST_CODE_ADD_NOTE)
        }
    }

    override fun getAdapterTypeFactory(): ShopNoteFactory {
        return ShopNoteFactory(this)
    }

    override fun onIconMoreClicked(shopNoteUiModel: ShopNoteUiModel) {
        val itemList = arrayListOf<String>()
        itemList.addAll(resources.getStringArray(R.array.shop_note_menu_more))
        iconMoreBottomSheet = MenuBottomSheet.newInstance(itemList)
        iconMoreBottomSheet?.setListener(object : MenuViewHolder.ItemMenuListener {
            override fun onItemMenuClicked(text: String, position: Int) {
                if (position == 0) {
                    goToEditNote(shopNoteUiModel)
                } else {
                    activity?.let { it ->
                        DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                            setTitle(getString(R.string.title_dialog_delete_shop_note))
                            setDescription(getString(R.string.desc_dialog_delete_shop_note, text))
                            setPrimaryCTAText(getString(R.string.action_delete))
                            setSecondaryCTAText(getString(com.tokopedia.resources.common.R.string.general_label_cancel))
                            setPrimaryCTAClickListener {
                                shopNoteIdToDelete = shopNoteUiModel.id
                                showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
                                shopSettingNoteListPresenter.deleteShopNote(shopNoteIdToDelete!!)
                                dismiss()
                            }
                            setSecondaryCTAClickListener { dismiss() }
                            show()
                        }
                    }
                }
                iconMoreBottomSheet?.dismiss()
            }
            override fun itemMenuSize(): Int = itemList.size
        })
        iconMoreBottomSheet?.showHeader = false
        iconMoreBottomSheet?.show(childFragmentManager, "menu_bottom_sheet")
    }

    override fun onSuccessGetShopNotes(shopNoteModels: ArrayList<ShopNoteUiModel>) {
        this.shopNoteModels = shopNoteModels
        renderList(shopNoteModels, false)
        activity?.invalidateOptionsMenu()
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
                    view?.let {
                        Toaster.make(it, getString(R.string.success_add_note), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
                    }
                } else if (requestCode == REQUEST_CODE_EDIT_NOTE){
                    view?.let {
                        Toaster.make(it, getString(R.string.success_edit_note), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
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

    override fun onSuccessDeleteShopNote(successMessage: String) {
        hideSubmitLoading()
        view?.let {
            Toaster.make(it, getString(R.string.note_success_delete), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL)
        }
        loadInitialData()
    }

    fun refreshData() {
        loadInitialData()
    }

    override fun onErrorDeleteShopNote(throwable: Throwable) {
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
