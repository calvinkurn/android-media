package com.tokopedia.logintest.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.logintest.R
import com.tokopedia.logintest.common.adapter.EmailExtensionTestAppAdapter

class EmailExtension @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : BaseCustomView(context, attributeSet, defStyleAttr) {

    private var view: View = View.inflate(context, R.layout.layout_testapp_email_extension, this)

    private lateinit var emailExtensionList: List<String>
    private lateinit var emailExtensionListFiltered: List<String>
    private var recyclerViewEmailExtension: RecyclerView? = null
    private var emailExtensionTestAppAdapter: EmailExtensionTestAppAdapter? = null
    private var listener: EmailExtensionTestAppAdapter.ClickListener? = null

    init {
        recyclerViewEmailExtension = view.findViewById(R.id.recyclerViewEmailExtension)
    }

    fun setExtensions(emailExtensionList: List<String>, listener: EmailExtensionTestAppAdapter.ClickListener) {
        this.emailExtensionList = emailExtensionList
        this.listener = listener

        emailExtensionTestAppAdapter = EmailExtensionTestAppAdapter(this.emailExtensionList, listener, 7)

        val space = context.resources.getDimension(R.dimen.dp_18).toInt()
        recyclerViewEmailExtension?.addItemDecoration(EmailExtensionDecoration(space))
        recyclerViewEmailExtension?.adapter = emailExtensionTestAppAdapter
        emailExtensionTestAppAdapter?.notifyDataSetChanged()
    }

    fun updateExtensions(emailExtensionList: List<String>) {

        if (emailExtensionList.isEmpty()) {
            recyclerViewEmailExtension?.visibility = View.GONE
        } else {
            recyclerViewEmailExtension?.visibility = View.VISIBLE
        }

        this.emailExtensionList = emailExtensionList
        emailExtensionTestAppAdapter?.updateList(emailExtensionList)
        emailExtensionTestAppAdapter?.notifyDataSetChanged()
    }

    fun filterExtensions(filter: String) {
        emailExtensionListFiltered = emailExtensionList.filter {
            it.startsWith(filter)
        }

        if (emailExtensionListFiltered.isEmpty()) {
            recyclerViewEmailExtension?.visibility = View.GONE
        } else {
            recyclerViewEmailExtension?.visibility = View.VISIBLE
        }

        emailExtensionListFiltered.let {
            recyclerViewEmailExtension?.removeAllViews()
            emailExtensionTestAppAdapter?.updateList(emailExtensionListFiltered)
            emailExtensionTestAppAdapter?.notifyDataSetChanged()
        }
    }

    fun gelistCount(): Int? = emailExtensionTestAppAdapter?.itemCount
}