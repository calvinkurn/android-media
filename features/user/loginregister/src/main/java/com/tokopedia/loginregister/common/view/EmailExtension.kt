package com.tokopedia.loginregister.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.adapter.EmailExtensionAdapter

class EmailExtension @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : BaseCustomView(context, attributeSet, defStyleAttr) {

    private lateinit var emailExtensionList: List<String>
    private lateinit var emailExtensionListFiltered: List<String>
    private var recyclerViewEmailExtension: RecyclerView? = null
    private var emailExtensionAdapter: EmailExtensionAdapter? = null
    private var listener: EmailExtensionAdapter.ClickListener? = null

    init {
        val view = View.inflate(context, R.layout.layout_email_extension, this)
        recyclerViewEmailExtension = view.findViewById(R.id.recyclerViewEmailExtension)
    }

    fun setExtensions(emailExtensionList: List<String>, listener: EmailExtensionAdapter.ClickListener) {
        this.emailExtensionList = emailExtensionList
        this.listener = listener

        emailExtensionAdapter = EmailExtensionAdapter(this.emailExtensionList, listener)

        val space = context.resources.getDimension(R.dimen.dp_18).toInt()
        recyclerViewEmailExtension?.addItemDecoration(EmailExtensionDecoration(space))
        recyclerViewEmailExtension?.adapter = emailExtensionAdapter
        emailExtensionAdapter?.notifyDataSetChanged()
    }

    fun updateExtensions(emailExtensionList: List<String>) {

        if (emailExtensionList.isEmpty()) {
            recyclerViewEmailExtension?.visibility = View.GONE
        }

        this.emailExtensionList = emailExtensionList
        emailExtensionAdapter?.updateList(emailExtensionList)
        emailExtensionAdapter?.notifyDataSetChanged()
    }

    fun filterExtensions(filter: String) {
        emailExtensionListFiltered = emailExtensionList.filter {
            it.startsWith(filter)
        }

        if (emailExtensionListFiltered.isEmpty()) {
            recyclerViewEmailExtension?.visibility = View.GONE
        }

        emailExtensionListFiltered.let {
            recyclerViewEmailExtension?.removeAllViews()
            emailExtensionAdapter?.updateList(emailExtensionListFiltered)
            emailExtensionAdapter?.notifyDataSetChanged()
        }
    }
}