package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EMPTY_SEARCH_VIEW
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUP_ID
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapter
import com.tokopedia.topads.dashboard.view.adapter.keyword.KeywordAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.topads.dashboard.view.sheet.TopadsGroupFilterSheet
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_keyword_list.*
import kotlinx.android.synthetic.main.topads_dash_fragment_non_group_list.actionbar
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 7/6/20.
 */
class KeywordTabFragment : BaseDaggerFragment() {

    private lateinit var adapter: KeywordAdapter
    private var deleteCancel = false
    private var singleAction = false


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    companion object {

        fun createInstance(bundle: Bundle): KeywordTabFragment {
            val frag = KeywordTabFragment()
            frag.arguments = bundle
            return frag
        }
    }

    private val groupFilterSheet: TopadsGroupFilterSheet by lazy {
        context.run {
            TopadsGroupFilterSheet.newInstance(context!!)
        }
    }

    override fun getScreenName(): String {
        return KeywordTabFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = KeywordAdapter(KeywordAdapterTypeFactoryImpl(::onCheckedChange, ::setSelectMode, ::startEditActivity))
    }

    private fun onCheckedChange(pos: Int, isChecked: Boolean) {
        singleAction = true
        val actionActivate: String = if (isChecked)
            TopAdsDashboardConstant.ACTION_ACTIVATE
        else
            TopAdsDashboardConstant.ACTION_DEACTIVATE
        viewModel.setKeywordAction(actionActivate,
                listOf((adapter.items[pos] as KeywordItemViewModel).result.keywordId.toString()), resources, ::onSuccessAction)
    }

    private fun onSuccessAction() {
        if (!singleAction) {
            setSelectMode(false)
            fetchData()
        }
        singleAction = false
    }

    private fun setSelectMode(select: Boolean) {
        if (select) {
            adapter.setSelectMode(true)
            actionbar.visibility = View.VISIBLE
        } else {
            adapter.setSelectMode(false)
            actionbar.visibility = View.GONE
        }
    }

    override fun initInjector() {
        getComponent(TopAdsDashboardComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_keyword_list), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFilter.setOnClickListener {
            groupFilterSheet.show()
            groupFilterSheet.onSubmitClick = { fetchData() }
        }
        fetchData()
        setSearchBar()
        close_butt?.setOnClickListener { setSelectMode(false) }
        activate.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_ACTIVATE) }
        deactivate.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE) }
        delete.setOnClickListener {
            showConfirmationDialog(context!!)
        }
        Utils.setSearchListener(context, view, ::fetchData)
        btnAddItem.setOnClickListener {
            startEditActivity()

        }
        key_list?.adapter = adapter
        key_list?.layoutManager = LinearLayoutManager(context)
    }

    private fun startEditActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)?.apply {
            putExtra(TopAdsDashboardConstant.GROUPID, arguments?.getInt(GROUP_ID).toString())
            putExtra(TopAdsDashboardConstant.GROUPNAME, arguments?.getString(TopAdsDashboardConstant.GROUP_NAME))
        }
        startActivityForResult(intent, TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TopAdsDashboardConstant.EDIT_GROUP_REQUEST_CODE) {
            fetchData()
        }
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(context.getString(R.string.topads_dash_confirm_delete_key_desc))
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_confirm_delete_key), adapter.getSelectedItems().size))
        dialog.setPrimaryCTAText(context.getString(R.string.topads_common_cancel_btn))
        dialog.setSecondaryCTAText(context.getString(R.string.topads_dash_ya_hapus))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            performAction(TopAdsDashboardConstant.ACTION_DELETE)
        }
        dialog.show()
    }

    private fun setSearchBar() {
        divider.visibility = View.VISIBLE
        btnAddItem.visibility = View.VISIBLE
        movetogroup.visibility = View.GONE
    }

    private fun fetchData() {
        loader.visibility = View.VISIBLE
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        viewModel.getGroupKeywordData(resources, 1, arguments?.getInt(GROUP_ID)
                ?: 0, searchBar.searchBarTextField.text.toString(), groupFilterSheet.getSelectedSortId(),
                groupFilterSheet.getSelectedStatusId(), ::onSuccessKeyword, ::onEmpty)
    }

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.result.keywordId.toString())
        }
        return ads
    }

    private fun onSuccessKeyword(data: List<KeywordsResponse.GetTopadsDashboardKeywords.DataItem>) {
        loader.visibility = View.GONE
        data.forEach { result ->
            adapter.items.add(KeywordItemViewModel(result))
        }
        adapter.notifyDataSetChanged()
        setFilterCount()
        (activity as TopAdsGroupDetailViewActivity).setKeywordCount(adapter.itemCount)
    }

    private fun setFilterCount() {
        if (!groupFilterSheet.getFilterCount().isZero()) {
            filterCount.visibility = View.VISIBLE
            filterCount.text = groupFilterSheet.getFilterCount().toString()
        } else
            filterCount.visibility = View.GONE
    }

    private fun onEmpty() {
        adapter.items.add(KeywordEmptyViewModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(EMPTY_SEARCH_VIEW)
        }
        setFilterCount()
        (activity as TopAdsGroupDetailViewActivity).setKeywordCount(0)
    }

    private fun performAction(actionActivate: String) {

        if (actionActivate == TopAdsDashboardConstant.ACTION_DELETE) {
            view.let {
                Toaster.make(it!!, String.format(getString(R.string.topads_keyword_del_toaster), getAdIds().size), TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(R.string.topads_common_batal), View.OnClickListener {
                    deleteCancel = true

                })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
                if (!deleteCancel) {
                    viewModel.setKeywordAction(actionActivate, getAdIds(), resources, ::onSuccessAction)
                    activity?.setResult(TopAdsDashboardConstant.GROUP_UPDATED)
                }
                deleteCancel = false
                setSelectMode(false)
            }
        } else {
            viewModel.setKeywordAction(actionActivate, getAdIds(), resources, ::onSuccessAction)
        }
    }

}