package com.tokopedia.instantloan.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.instantloan.data.model.response.GqlLendingCategoryData
import java.util.*

class SelectLoanCategoryActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonClose: View
    private lateinit var topBarTitle: TextView
    private var adapter: ListAdapter? = null
    private var data: ArrayList<GqlLendingCategoryData>? = null
    private var selectedKey: Int = 0
    private var selectedValue: String? = null

    override fun getScreenName(): String? {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tokopedia.instantloan.R.layout.activity_select_loan_param)
        topBarTitle = findViewById<View>(com.tokopedia.instantloan.R.id.top_bar_title) as TextView
        recyclerView = findViewById<View>(com.tokopedia.instantloan.R.id.il_list) as RecyclerView
        buttonClose = findViewById(com.tokopedia.instantloan.R.id.top_bar_close_button)
        buttonClose.setOnClickListener { onBackPressed() }
        topBarTitle.text = getString(com.tokopedia.instantloan.R.string.il_loan_category)

        intent.extras?.let {
            data = it.getParcelableArrayList(EXTRA_DATA)
        }

        adapter = ListAdapter(data, selectedKey, selectedValue, object : OnItemClickListener {
            override fun onItemClicked(categoryType: GqlLendingCategoryData) {
                val intent = Intent()
                intent.putExtra(EXTRA_SELECTED_NAME, categoryType)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this))
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.tokopedia.instantloan.R.menu.instant_loan_menu_sort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == com.tokopedia.instantloan.R.id.il_action_close) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, com.tokopedia.instantloan.R.anim.instant_loan_push_down)
    }

    private inner class ListAdapter(sortList: List<GqlLendingCategoryData>?, private var selectedKey: Int, private var selectedValue: String?, internal var clickListener: OnItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        internal var sortList: List<GqlLendingCategoryData>

        init {
            if (sortList == null) {
                this.sortList = ArrayList()
            } else {
                this.sortList = sortList
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(com.tokopedia.instantloan.R.layout.instant_loan_sort_list_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = sortList[position].categoryName
            holder.title.tag = sortList[position].categoryId

            holder.title.isSelected = sortList[position].isSelected
            holder.title.setOnClickListener(View.OnClickListener {
                if (holder.adapterPosition == RecyclerView.NO_POSITION) {
                    return@OnClickListener
                }
                selectedKey = sortList[holder.adapterPosition].categoryId
                selectedValue = sortList[holder.adapterPosition].categoryName
                sortList[holder.adapterPosition].isSelected = true
                clickListener.onItemClicked(sortList[holder.adapterPosition])

                notifyItemChanged(holder.adapterPosition)
            })
        }

        override fun getItemCount(): Int {
            return sortList.size
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
            // each data item is just a string in this case
            var title: TextView

            init {
                title = v.findViewById<View>(com.tokopedia.design.R.id.title) as TextView
                title.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                val textView = v as TextView
                textView.isSelected = true
                sortList[adapterPosition].isSelected = true
                clickListener.onItemClicked(sortList[adapterPosition])
                notifyItemChanged(adapterPosition)
            }

        }

    }

    private interface OnItemClickListener {
        fun onItemClicked(categoryType: GqlLendingCategoryData)
    }

    companion object {

        val EXTRA_DATA = "EXTRA_DATA"
        val EXTRA_SELECTED_NAME = "EXTRA_SELECTED_NAME"

        fun createInstance(context: Context, sort: ArrayList<GqlLendingCategoryData>): Intent {
            val intent = Intent(context, SelectLoanCategoryActivity::class.java)
            intent.putParcelableArrayListExtra(EXTRA_DATA, sort)
            return intent
        }
    }
}