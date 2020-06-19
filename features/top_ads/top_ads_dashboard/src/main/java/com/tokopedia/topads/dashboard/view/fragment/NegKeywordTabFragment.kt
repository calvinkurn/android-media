package com.tokopedia.topads.dashboard.view.fragment

import android.content.Context
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
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION_DELETE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TOASTER_DURATION
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.activity.TopAdsGroupDetailViewActivity
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapter
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.NegKeywordAdapterTypeFactoryImpl
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordEmptyViewModel
import com.tokopedia.topads.dashboard.view.adapter.negkeyword.viewmodel.NegKeywordItemViewModel
import com.tokopedia.topads.dashboard.view.model.GroupDetailViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.topads_dash_fragment_neg_keyword_list.*
import kotlinx.android.synthetic.main.topads_dash_fragment_non_group_list.actionbar
import kotlinx.android.synthetic.main.topads_dash_layout_common_action_bar.*
import kotlinx.android.synthetic.main.topads_dash_layout_common_searchbar_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Pika on 15/5/20.
 */
class NegKeywordTabFragment : BaseDaggerFragment() {

    private lateinit var adapter: NegKeywordAdapter

    companion object {

        fun createInstance(bundle: Bundle): NegKeywordTabFragment {
            val frag = NegKeywordTabFragment()
            frag.arguments = bundle
            return frag
        }
    }

    private var deleteCancel = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }
    private val viewModel by lazy {
        viewModelProvider.get(GroupDetailViewModel::class.java)
    }

    override fun getScreenName(): String {
        return NegKeywordTabFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = NegKeywordAdapter(NegKeywordAdapterTypeFactoryImpl(::onCheckedChange, ::setSelectMode))

    }

    private fun onCheckedChange(pos: Int, isChecked: Boolean) {
        val actionActivate: String = if (isChecked)
            TopAdsDashboardConstant.ACTION_ACTIVATE
        else
            TopAdsDashboardConstant.ACTION_DEACTIVATE

        viewModel.setKeywordAction(actionActivate,
                listOf((adapter.items[pos] as NegKeywordItemViewModel).result.keywordId.toString()), resources, ::onSuccessAction)
    }

    private fun onSuccessAction() {
        setSelectMode(false)
        fetchData()
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
        return inflater.inflate(resources.getLayout(R.layout.topads_dash_fragment_neg_keyword_list), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        close_butt?.setOnClickListener { setSelectMode(false) }
        activate.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_ACTIVATE) }
        deactivate.setOnClickListener { performAction(TopAdsDashboardConstant.ACTION_DEACTIVATE) }
        setSearchBar()
        fetchData()
        delete.setOnClickListener {
            showConfirmationDialog(context!!)
        }
        btnAddItem.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(TopAdsDashboardConstant.groupId, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID).toString())
            bundle.putString(TopAdsDashboardConstant.groupName, arguments?.getString(TopAdsDashboardConstant.GROUP_NAME))
            bundle.putString(TopAdsDashboardConstant.groupStatus, arguments?.getString(TopAdsDashboardConstant.GROUP_STATUS))
            RouteManager.route(context, bundle, ApplinkConstInternalTopAds.TOPADS_EDIT_ADS)
        }
        Utils.setSearchListener(view, ::onSuccessSearch, ::onSearchClear)
        neg_key_list?.adapter = adapter
        neg_key_list?.layoutManager = LinearLayoutManager(context)

    }

    private fun setSearchBar() {
        movetogroup.visibility = View.GONE
        btnFilter.visibility = View.GONE
        btnAddItem.visibility = View.VISIBLE

    }

    private fun performAction(actionActivate: String) {
        if (actionActivate == ACTION_DELETE) {
            view.let {
                Toaster.make(it!!, String.format(getString(R.string.topads_neg_keyword_del_toaster), getAdIds().size),
                        TOASTER_DURATION.toInt(), Toaster.TYPE_NORMAL, getString(R.string.topads_common_batal), View.OnClickListener {
                    deleteCancel = true
                })
            }
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                delay(TOASTER_DURATION)
                if (!deleteCancel)
                    viewModel.setKeywordAction(actionActivate, getAdIds(), resources, ::onSuccessAction)
                deleteCancel = false
                setSelectMode(false)
            }
        } else {
            viewModel.setKeywordAction(actionActivate, getAdIds(), resources, ::onSuccessAction)
        }
    }

    private fun fetchData() {
        adapter.items.clear()
        adapter.notifyDataSetChanged()
        viewModel.getGroupKeywordData(resources, 0, arguments?.getInt(TopAdsDashboardConstant.GROUP_ID)
                ?: 0, searchBar.searchBarTextField.text.toString(), null, null, ::onSuccessKeyword, ::onEmpty)
    }

    private fun onSuccessSearch(search: String) {
        fetchData()
    }

    private fun onSearchClear() {
        fetchData()
    }

    private fun showConfirmationDialog(context: Context) {
        val dialog = DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setDescription(context.getString(R.string.topads_dash_confirm_delete_negkey_desc))
        dialog.setTitle(String.format(context.getString(R.string.topads_dash_confirm_delete_negkey), adapter.getSelectedItems().size))
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

    private fun getAdIds(): MutableList<String> {
        val ads: MutableList<String> = mutableListOf()
        adapter.getSelectedItems().forEach {
            ads.add(it.result.keywordId.toString())
        }
        return ads
    }

    private fun onEmpty() {
        adapter.items.add(NegKeywordEmptyViewModel())
        if (searchBar?.searchBarTextField?.text.toString().isEmpty()) {
            adapter.setEmptyView(!TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        } else {
            adapter.setEmptyView(TopAdsDashboardConstant.EMPTY_SEARCH_VIEW)
        }
        (activity as TopAdsGroupDetailViewActivity).setNegKeywordCount(0)

    }

    private fun onSuccessKeyword(data: List<KeywordsResponse.GetTopadsDashboardKeywords.DataItem>) {
        data.forEach { result ->
            adapter.items.add(NegKeywordItemViewModel(result))
        }
        adapter.notifyDataSetChanged()
        (activity as TopAdsGroupDetailViewActivity).setNegKeywordCount(adapter.itemCount)
    }
}