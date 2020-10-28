package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.model.insightkey.KeywordInsightDataMain
import com.tokopedia.topads.dashboard.data.utils.ListUnifyUtils.setSelectedItem
import com.tokopedia.topads.dashboard.data.utils.Utils
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import kotlinx.android.synthetic.main.topads_dash_move_group_insight_sheet.*

/**
 * Created by Pika on 22/7/20.
 */

class GroupSelectInsightSheet(var response: InsightKeyData, private val groupId: String) : BottomSheetUnify() {

    private var contentView: View? = null
    var selectedGroup: ((pos: Int, groupId: String) -> Unit)? = null
    var index = 0
    private var listOfKeys: MutableList<String> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        Utils.setSearchListener(context, view, ::setList)
    }

    private fun initView(view: View) {
        setCloseClickListener {
            dismiss()
        }
        view.run {
            setList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_dash_move_group_insight_sheet, null)
        isFullpage = false
        setChild(contentView)
        context?.getString(R.string.topads_insight_pilih_grup_iklan)?.let { setTitle(it) }
    }

    private fun setList() {
        val listUnify = ArrayList<ListItemUnify>()
        val data: HashMap<String, KeywordInsightDataMain> = response.data
        var count = 0
        data.forEach {
            if (it.value.name.contains(searchBar.searchBarTextField.text.toString())) {
                listOfKeys.add(it.key)
                count++
                if (groupId == it.key) {
                    index = count - 1
                }
                val list = ListItemUnify(it.value.name + " (${it.value.count})", "")
                list.isBold = true
                list.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
                listUnify.add(list)
            }
        }
        if (listUnify.isNotEmpty()) {
            listGroup?.setData(listUnify)
            listGroup.visibility = View.VISIBLE
            txtSearch.visibility = View.GONE
        } else {
            listGroup.visibility = View.GONE
            txtSearch.text = String.format(resources.getString(R.string.topads_insight_search_text), searchBar.searchBarTextField.text.toString())
            txtSearch.visibility = View.VISIBLE
        }

        listGroup?.run {
            onLoadFinish {
                this.setOnItemClickListener { _, _, position, _ ->
                    setSelectedItem(listUnify, position) {
                        selectedGroup?.invoke(position, listOfKeys[position])
                    }
                }
                listUnify.forEachIndexed { position, it ->
                    it.listRightRadiobtn?.setOnClickListener {
                        this.setSelectedItem(listUnify, position) {
                            selectedGroup?.invoke(position, listOfKeys[position])
                        }
                    }
                }
                if (index >= listUnify.size) {
                    index = 0
                }
                this.setSelectedItem(listUnify, index) {}
            }
        }
    }
}