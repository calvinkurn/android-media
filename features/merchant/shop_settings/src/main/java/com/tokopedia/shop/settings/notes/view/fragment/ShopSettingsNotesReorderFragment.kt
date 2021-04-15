@file:Suppress("DEPRECATION")

package com.tokopedia.shop.settings.notes.view.fragment

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.common.di.DaggerShopSettingsComponent
import com.tokopedia.shop.settings.common.util.FORMAT_DATE_TIME
import com.tokopedia.shop.settings.common.util.OnStartDragListener
import com.tokopedia.shop.settings.common.util.SimpleItemTouchHelperCallback
import com.tokopedia.shop.settings.common.util.toReadableString
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import com.tokopedia.shop.settings.notes.view.adapter.ShopNoteReorderAdapter
import com.tokopedia.shop.settings.notes.view.adapter.factory.ShopNoteReorderFactory
import com.tokopedia.shop.settings.notes.view.presenter.ShopSettingNoteListReorderPresenter
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import java.util.*
import javax.inject.Inject


class ShopSettingsNotesReorderFragment : BaseListFragment<ShopNoteUiModel, ShopNoteReorderFactory>(), ShopSettingNoteListReorderPresenter.View, OnStartDragListener {

    @Inject
    lateinit var shopSettingNoteListReorderPresenter: ShopSettingNoteListReorderPresenter
    private var shopNoteModels: ArrayList<ShopNoteUiModel>? = null
    private var shopNoteModelsWithoutTerms: List<ShopNoteUiModel>? = null
    private var progressDialog: ProgressDialog? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: ShopNoteReorderAdapter? = null
    private var adapterTerms: ShopNoteReorderAdapter? = null
    private var itemTouchHelper: ItemTouchHelper? = null

    private var listener: OnShopSettingsNotesReorderFragmentListener? = null

    interface OnShopSettingsNotesReorderFragmentListener {
        fun onSuccessReorderNotes()
    }

    override fun initInjector() {
        activity?.let {
            DaggerShopSettingsComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
        shopSettingNoteListReorderPresenter.attachView(this)
    }

    override fun createAdapterInstance(): BaseListAdapter<ShopNoteUiModel, ShopNoteReorderFactory> {
        adapter = ShopNoteReorderAdapter(adapterTypeFactory)
        return adapter as ShopNoteReorderAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        shopNoteModels = arguments?.getParcelableArrayList(EXTRA_NOTE_LIST)
        super.onCreate(savedInstanceState)
        context?.let {
            GraphqlClient.init(it)
        }
        adapterTerms = ShopNoteReorderAdapter(ShopNoteReorderFactory(null))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_reorder_list, container, false)
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
        adapter?.let {
            val itemTouchHelperCallback = SimpleItemTouchHelperCallback(it)
            itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper?.attachToRecyclerView(recyclerView)
        }
    }

    override fun loadData(page: Int) {
        if (shopNoteModels != null && shopNoteModels?.size.toZeroIfNull() > 0) {
            shopNoteModels?.get(0)?.apply {
                val itemNoteReorder = view?.findViewById<LinearLayout>(R.id.item_note_reorder)
                val tpNoteName = view?.findViewById<Typography>(R.id.tvNoteName)
                val tpNoteLastUpdated = view?.findViewById<Typography>(R.id.tvLastUpdate)
                val ivReorder = view?.findViewById<ImageView>(R.id.ivReorder)
                val divider = view?.findViewById<View>(R.id.divider)
                itemNoteReorder?.background = context?.getDrawable(com.tokopedia.unifyprinciples.R.color.Unify_N700_20)

                if (terms) {
                    shopNoteModels?.size?.let {
                        shopNoteModelsWithoutTerms = shopNoteModels?.subList(1, it)
                        itemNoteReorder?.show()
                        tpNoteName?.text = title
                        tpNoteLastUpdated?.text = toReadableString(FORMAT_DATE_TIME, updateTimeUTC)
                        divider?.show()
                        ivReorder?.gone()
                    }
                } else {
                    divider?.gone()
                    itemNoteReorder?.gone()
                    shopNoteModelsWithoutTerms = shopNoteModels
                }
            }
        }
        shopNoteModelsWithoutTerms?.let {
            renderList(it, false)
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
        view?.let {
            Toaster.make(it, getString(R.string.note_success_reorder), Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL,
                    getString(com.tokopedia.abstraction.R.string.close))
        }
        listener!!.onSuccessReorderNotes()
    }

    override fun onErrorReorderShopNote(throwable: Throwable) {
        hideSubmitLoading()
        val message = ErrorHandler.getErrorMessage(context, throwable)
        view?.let {
            Toaster.make(it, message, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR,
                    getString(com.tokopedia.abstraction.R.string.close))
        }
    }

    fun showSubmitLoading(message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
        }
        if (progressDialog?.isShowing != true) {
            progressDialog?.setMessage(message)
            progressDialog?.isIndeterminate = true
            progressDialog?.setCancelable(false)
            progressDialog?.show()
        }
    }

    fun hideSubmitLoading() {
        if (progressDialog != null && progressDialog?.isShowing == true) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shopSettingNoteListReorderPresenter.detachView()
    }

    override fun onItemClicked(shopNoteUiModel: ShopNoteUiModel) {
        // no-op
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            itemTouchHelper?.startDrag(it)
        }
    }

    override fun onAttachActivity(context: Context) {
        super.onAttachActivity(context)
        listener = context as OnShopSettingsNotesReorderFragmentListener
    }

    companion object {

        val TAG = ShopSettingsNotesReorderFragment::class.java.simpleName
        val EXTRA_NOTE_LIST = "note_list"

        @JvmStatic
        fun newInstance(shopNoteUiModels: ArrayList<ShopNoteUiModel>): ShopSettingsNotesReorderFragment {

            val args = Bundle()
            args.putParcelableArrayList(EXTRA_NOTE_LIST, shopNoteUiModels)
            val fragment = ShopSettingsNotesReorderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
