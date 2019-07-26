@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.etalase.view.fragment

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
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel
import com.tokopedia.shop.settings.etalase.view.adapter.ShopEtalaseReorderAdapter
import com.tokopedia.shop.settings.etalase.view.adapter.factory.ShopEtalaseReorderFactory
import com.tokopedia.shop.settings.etalase.view.presenter.ShopSettingEtalaseListReorderPresenter

import java.util.ArrayList

import javax.inject.Inject

class ShopSettingsEtalaseReorderFragment : BaseListFragment<ShopEtalaseViewModel, ShopEtalaseReorderFactory>(), ShopSettingEtalaseListReorderPresenter.View, OnStartDragListener {

    @Inject
    lateinit var shopSettingEtalaseListReorderPresenter: ShopSettingEtalaseListReorderPresenter
    private var shopEtalaseModels: ArrayList<ShopEtalaseViewModel>? = null
    private var shopEtalaseModelsDefault: ArrayList<ShopEtalaseViewModel>? = null
    private var progressDialog: ProgressDialog? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: ShopEtalaseReorderAdapter? = null
    private var adapterDefault: ShopEtalaseReorderAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    private var listener: OnShopSettingsEtalaseReorderFragmentListener? = null
    private var recyclerViewDefault: RecyclerView? = null

    interface OnShopSettingsEtalaseReorderFragmentListener {
        fun onSuccessReorderEtalase()
    }

    override fun initInjector() {
        DaggerShopSettingsComponent.builder()
                .baseAppComponent((activity!!.application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        shopSettingEtalaseListReorderPresenter.attachView(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<ShopEtalaseViewModel, ShopEtalaseReorderFactory> {
        adapter = ShopEtalaseReorderAdapter(adapterTypeFactory)
        return adapter as ShopEtalaseReorderAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_etalase_reorder_list, container, false)
        recyclerViewDefault = view.findViewById(R.id.recyclerViewDefault)
        recyclerViewDefault!!.adapter = adapterDefault
        return view
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopEtalaseModelsDefault = arguments!!.getParcelableArrayList(EXTRA_DEFAULT_ETALASE_LIST)
        shopEtalaseModels = arguments!!.getParcelableArrayList(EXTRA_ETALASE_LIST)
        super.onCreate(savedInstanceState)
        GraphqlClient.init(context!!)
        adapterDefault = ShopEtalaseReorderAdapter(ShopEtalaseReorderFactory(null))
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

        if (shopEtalaseModelsDefault == null || shopEtalaseModelsDefault!!.size == 0) {
            recyclerViewDefault!!.visibility = View.GONE
        } else {
            adapterDefault!!.clearAllElements()
            adapterDefault!!.addElement(shopEtalaseModelsDefault)
            recyclerViewDefault!!.visibility = View.VISIBLE
        }
    }

    override fun loadData(page: Int) {
        renderList(shopEtalaseModels!!, false)
    }

    override fun getAdapterTypeFactory(): ShopEtalaseReorderFactory {
        return ShopEtalaseReorderFactory(this)
    }

    fun saveReorder() {
        showSubmitLoading(getString(com.tokopedia.abstraction.R.string.title_loading))
        val shopNoteList = ArrayList<String>()
        val sortDataList = adapter!!.data
        for (shopEtalaseViewModel in sortDataList) {
            shopNoteList.add(shopEtalaseViewModel.id)
        }
        shopSettingEtalaseListReorderPresenter.reorderShopNotes(shopNoteList)
    }

    override fun onSuccessReorderShopEtalase(successMessage: String) {
        hideSubmitLoading()
        ToasterNormal.make(activity!!.findViewById(android.R.id.content),
                getString(R.string.etalase_success_reorder), BaseToaster.LENGTH_LONG)
                .setAction(getString(com.tokopedia.abstraction.R.string.close)) {
                    // no-op
                }.show()
        listener!!.onSuccessReorderEtalase()
    }

    override fun onErrorReorderShopEtalase(throwable: Throwable) {
        hideSubmitLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        ToasterError.make(activity!!.findViewById(android.R.id.content),
                message, BaseToaster.LENGTH_LONG)
                .setAction(getString(com.tokopedia.design.R.string.close)) {
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
        shopSettingEtalaseListReorderPresenter.detachView()
    }

    override fun onItemClicked(shopEtalaseViewModel: ShopEtalaseViewModel) {
        // no-op
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper!!.startDrag(viewHolder)
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        listener = context as OnShopSettingsEtalaseReorderFragmentListener
    }

    companion object {

        val TAG = ShopSettingsEtalaseReorderFragment::class.java.simpleName
        val EXTRA_ETALASE_LIST = "etalase_list"
        val EXTRA_DEFAULT_ETALASE_LIST = "def_etalase_list"

        @JvmStatic
        fun newInstance(shopEtalaseViewModelsDefault: ArrayList<ShopEtalaseViewModel>,
                        shopEtalaseViewModels: ArrayList<ShopEtalaseViewModel>): ShopSettingsEtalaseReorderFragment {

            val args = Bundle()
            args.putParcelableArrayList(EXTRA_DEFAULT_ETALASE_LIST, shopEtalaseViewModelsDefault)
            args.putParcelableArrayList(EXTRA_ETALASE_LIST, shopEtalaseViewModels)
            val fragment = ShopSettingsEtalaseReorderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
