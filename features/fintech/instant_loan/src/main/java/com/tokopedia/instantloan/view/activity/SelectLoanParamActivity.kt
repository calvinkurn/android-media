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
import com.tokopedia.instantloan.R
import com.tokopedia.instantloan.data.model.response.LoanPeriodType
import java.util.*

class SelectLoanParamActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonClose: View
    private var topBarTitle: TextView? = null
    private var adapter: ListAdapter? = null
    private var data: ArrayList<LoanPeriodType>? = null
    private var selectedKey: Int = 0
    private var selectedValue: String? = null

    override fun getScreenName(): String? {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_loan_param)
        topBarTitle = findViewById<View>(R.id.top_bar_title) as TextView
        topBarTitle!!.text = getString(R.string.title_sort_but)
        recyclerView = findViewById<View>(R.id.list) as RecyclerView
        buttonClose = findViewById(R.id.top_bar_close_button)
        buttonClose.setOnClickListener { onBackPressed() }
        data = intent.extras!!.getParcelableArrayList(EXTRA_DATA)
        adapter = ListAdapter(data, selectedKey, selectedValue, object : OnItemClickListener {
            override fun onItemClicked(loanType: LoanPeriodType) {
                val intent = Intent()
                intent.putExtra(EXTRA_SELECTED_NAME, loanType)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this))
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sort, menu)
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

    private inner class ListAdapter(sortList: List<LoanPeriodType>?, private var selectedKey: Int, private var selectedValue: String?, internal var clickListener: OnItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
        internal var sortList: List<LoanPeriodType>

        init {
            if (sortList == null) {
                this.sortList = ArrayList()
            } else {
                this.sortList = sortList
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.sort_list_item, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.text = sortList[position].label
            holder.title.tag = sortList[position].value

            holder.title.isSelected = sortList[position].isSelected
            holder.title.setOnClickListener(View.OnClickListener {
                if (holder.adapterPosition == RecyclerView.NO_POSITION) {
                    return@OnClickListener
                }
                selectedKey = sortList[holder.adapterPosition].id
                selectedValue = sortList[holder.adapterPosition].value
                sortList[holder.adapterPosition].isSelected = true
                clickListener.onItemClicked(sortList[holder.adapterPosition])

                notifyDataSetChanged()
            })
        }

        override fun getItemCount(): Int {
            return sortList.size
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
            // each data item is just a string in this case
            var title: TextView

            init {
                title = v.findViewById<View>(R.id.title) as TextView
                title.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                val textView = v as TextView
                textView.isSelected = true
                sortList[adapterPosition].isSelected = true
                clickListener.onItemClicked(sortList[adapterPosition])
                notifyDataSetChanged()
            }

        }

    }

    private interface OnItemClickListener {
        fun onItemClicked(loanType: LoanPeriodType)
    }

    companion object {

        val EXTRA_DATA = "EXTRA_DATA"
        val EXTRA_SELECTED_NAME = "EXTRA_SELECTED_NAME"

        fun createInstance(context: Context, sort: ArrayList<LoanPeriodType>, selectedSort: HashMap<String, String>?): Intent {
            val intent = Intent(context, SelectLoanParamActivity::class.java)
            intent.putParcelableArrayListExtra(EXTRA_DATA, sort)
            return intent
        }
    }
}