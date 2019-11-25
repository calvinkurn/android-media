package com.tokopedia.filter.newdynamicfilter

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Sort

import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Erry on 7/12/2016.
 */
@SuppressWarnings("unchecked")
class SortProductActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private var buttonClose: View? = null
    private var topBarTitle: TextView? = null
    private var adapter: ListAdapter? = null
    private var data: ArrayList<Sort>? = null
    private var selectedKey: String? = null
    private var selectedValue: String? = null

    override fun getScreenName(): String {
        return SCREEN_SORT_PRODUCT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_sort)
        topBarTitle = findViewById(R.id.top_bar_title)
        topBarTitle?.text = getString(R.string.title_sort_but)
        recyclerView = findViewById(R.id.list)
        buttonClose = findViewById(R.id.top_bar_close_button)
        buttonClose?.setOnClickListener { onBackPressed() }
        data = intent.extras.getParcelableArrayList(EXTRA_SORT_DATA)
        generateSelectedKeyValue(intent.getSerializableExtra(EXTRA_SELECTED_SORT) as HashMap<String, String>)
        adapter = ListAdapter(data, selectedKey, selectedValue, object : OnItemClickListener {
            override fun onItemClicked(sortItem: Sort) {
                val intent = Intent()
                val params = hashMapOf<String, String>()
                params.put(sortItem.key, sortItem.value)
                intent.putExtra(EXTRA_SELECTED_SORT, params)
                intent.putExtra(EXTRA_AUTO_APPLY_FILTER, sortItem.applyFilter)
                intent.putExtra(EXTRA_SELECTED_SORT_NAME, sortItem.name)
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this))
        recyclerView.adapter = adapter

    }

    private fun generateSelectedKeyValue(selectedSort: HashMap<String, String>?) {
        if (selectedSort == null) {
            return
        }

        for (entry in selectedSort.entries) {
            selectedKey = entry.key
            selectedValue = entry.value
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filter_menu_sort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_close) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_down)
    }

    private inner class ListAdapter(sortList: List<Sort>?, private var selectedKey: String?, private var selectedValue: String?, internal var clickListener: OnItemClickListener?) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        private var sortList: List<Sort>? = null

        init {
            if (sortList == null) {
                this.sortList = ArrayList()
            } else {
                this.sortList = sortList
            }
        }

        override fun getItemCount(): Int {
            return sortList!!.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.filter_sort_list_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = sortList!![position].name
            holder.title.tag = sortList!![position].value
            if (selectedKey == null && selectedValue == null) {
                if (position == 0) {
                    holder.title.isSelected = true
                }
            } else {
                holder.title.isSelected = sortList!![position].key.equals(selectedKey) && sortList!![position].value.equals(selectedValue)
            }
            holder.title.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    if (holder.adapterPosition == RecyclerView.NO_POSITION) {
                        return
                    }
                    selectedKey = sortList!![holder.adapterPosition].key
                    selectedValue = sortList!![holder.adapterPosition].value

                    clickListener?.onItemClicked(sortList!![holder.adapterPosition])

                    notifyDataSetChanged()
                }
            })
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
            // each data item is just a string in this case
            var title: TextView = v.findViewById(R.id.title)

            init {
                title.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                v.isSelected = true
                clickListener?.onItemClicked(sortList!![adapterPosition])
                notifyDataSetChanged()
            }

        }

    }

    private interface OnItemClickListener {
        fun onItemClicked(sortItem: Sort)
    }

    companion object {

        const val EXTRA_SORT_DATA = "EXTRA_SORT_DATA"
        const val EXTRA_SELECTED_SORT = "EXTRA_SELECTED_SORT"
        const val EXTRA_AUTO_APPLY_FILTER = "EXTRA_AUTO_APPLY_FILTER"
        const val EXTRA_SELECTED_SORT_NAME = "EXTRA_SELECTED_SORT_NAME"
        const val SCREEN_SORT_PRODUCT = "Sort Produk Activity"
    }
}
