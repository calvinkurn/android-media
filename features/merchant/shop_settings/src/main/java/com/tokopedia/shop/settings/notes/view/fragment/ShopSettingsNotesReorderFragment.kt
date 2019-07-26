@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.notes.view.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.touchhelper.OnStartDragListener
import com.tokopedia.design.touchhelper.SimpleItemTouchHelperCallback
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteReorderAdapter
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteReorderFactory
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListReorderPresenter

import java.util.ArrayList

import javax.inject.Inject


class ShopSettingsNotesReorderFragment : BaseListFragment<ShopNoteViewModel, ShopNoteReorderFactory>(), ShopSettingNoteListReorderPresenter.View, OnStartDragListener {

    @Inject
    lateinit var shopSettingNoteListReorderPresenter: ShopSettingNoteListReorderPresenter
    private var shopNoteModels: ArrayList<ShopNoteViewModel>? = null
    private var shopNoteModelsWithoutTerms: List<ShopNoteViewModel>? = null
    private var progressDialog: ProgressDialog? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewTerms: RecyclerView? = null
    private var adapter: ShopNoteReorderAdapter? = null
    private var adapterTerms: ShopNoteReorderAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    private var listener: OnShopSettingsNotesReorderFragmentListener? = null

    interface OnShopSettingsNotesReorderFragmentListener {
        fun onSuccessReorderNotes()
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        shopSettingNoteListReorderPresenter.attachView(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<ShopNoteViewModel, ShopNoteReorderFactory> {
        adapter = ShopNoteReorderAdapter(adapterTypeFactory)
        return adapter as ShopNoteReorderAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopNoteModels = arguments!!.getParcelableArrayList(EXTRA_NOTE_LIST)
        super.onCreate(savedInstanceState)
        GraphqlClient.init(context!!)
        adapterTerms = ShopNoteReorderAdapter(ShopNoteReorderFactory(null))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note_reorder_list, container, false)
        recyclerViewTerms = view.findViewById(R.id.recyclerViewTerms)
        recyclerViewTerms!!.adapter = adapterTerms
        return view
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun getRecyclerView(view: View): RecyclerView {
        recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView?
        return view.findViewById<View>(R.id.recycler_view) as RecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemTouchHelperCallback = SimpleItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    override fun loadData(page: Int) {
        val shopNoteModelsTerms = ArrayList<ShopNoteViewModel>()
        if (shopNoteModels != null && shopNoteModels!!.size > 0) {
            if (shopNoteModels!![0].terms) {
                shopNoteModelsWithoutTerms = shopNoteModels!!.subList(1, shopNoteModels!!.size)
                shopNoteModelsTerms.add(shopNoteModels!![0])
            } else {
                shopNoteModelsWithoutTerms = shopNoteModels
            }
        }
        renderList(shopNoteModelsWithoutTerms!!, false)

        //render shop note with terms
        if (shopNoteModelsTerms.size == 0) {
            recyclerViewTerms!!.visibility = View.GONE
        } else {
            adapterTerms!!.clearAllElements()
            adapterTerms!!.addElement(shopNoteModelsTerms)
            recyclerViewTerms!!.visibility = View.VISIBLE
        }
    }

    override fun getAdapterTypeFactory(): ShopNoteReorderFactory {
        return ShopNoteReorderFactory(this)
    }

    fun saveReorder() {
        showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
        val shopNoteList = ArrayList<String>()
        val sortDataList = getAdapter().data
        for (shopNoteViewModel in sortDataList) {
            shopNoteViewModel.id?.let { shopNoteList.add(it) }

        }
        shopSettingNoteListReorderPresenter.reorderShopNotes(shopNoteList)
    }

    override fun onSuccessReorderShopNote(successMessage: String) {
        hideSubmitLoading()
        ToasterNormal.make(activity!!.findViewById(android.R.id.content),
                getString(R.string.note_success_reorder), BaseToaster.LENGTH_LONG)
                .setAction(getString(com.tokopedia.abstraction.R.string.close)) {
                    // no-op
                }.show()
        listener!!.onSuccessReorderNotes()
    }

    override fun onErrorReorderShopNote(throwable: Throwable) {
        hideSubmitLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        ToasterError.make(activity!!.findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_LONG)
                .setAction(getString(com.tokopedia.abstraction.R.string.close)) {
                    // no-op
                }.show()
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
        shopSettingNoteListReorderPresenter.detachView()
    }

    override fun onItemClicked(shopNoteViewModel: ShopNoteViewModel) {
        // no-op
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper!!.startDrag(viewHolder)
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        listener = context as OnShopSettingsNotesReorderFragmentListener
    }

    companion object {

        val TAG = ShopSettingsNotesReorderFragment::class.java.simpleName
        val EXTRA_NOTE_LIST = "note_list"

        @JvmStatic
        fun newInstance(shopNoteViewModels: ArrayList<ShopNoteViewModel>): ShopSettingsNotesReorderFragment {

            val args = Bundle()
            args.putParcelableArrayList(EXTRA_NOTE_LIST, shopNoteViewModels)
            val fragment = ShopSettingsNotesReorderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
