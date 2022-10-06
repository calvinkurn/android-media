package com.tokopedia.loginregister.common.view.emailextension

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.loginregister.databinding.LayoutEmailExtensionBinding
import com.tokopedia.unifycomponents.BaseCustomView

class EmailExtension @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseCustomView(context, attributeSet, defStyleAttr) {

    private lateinit var emailExtensionList: List<String>
    private lateinit var emailExtensionListFiltered: List<String>
    private var emailExtensionAdapter: EmailExtensionAdapter? = null
    private var listener: EmailExtensionAdapter.ClickListener? = null

    private val viewBinding: LayoutEmailExtensionBinding = LayoutEmailExtensionBinding
        .inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    fun setExtensions(
        emailExtensionList: List<String>,
        listener: EmailExtensionAdapter.ClickListener,
    ) {
        this.emailExtensionList = emailExtensionList
        this.listener = listener

        emailExtensionAdapter = EmailExtensionAdapter(
            this.emailExtensionList,
            listener,
            MAX_SHOWING_LIST
        )

        val space =
            context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .toInt()
        viewBinding.recyclerViewEmailExtension.addItemDecoration(EmailExtensionDecoration(space))
        viewBinding.recyclerViewEmailExtension.adapter = emailExtensionAdapter
        emailExtensionAdapter?.notifyDataSetChanged()
    }

    fun updateExtensions(emailExtensionList: List<String>) {

        if (emailExtensionList.isEmpty()) {
            viewBinding.recyclerViewEmailExtension.visibility = View.GONE
        } else {
            viewBinding.recyclerViewEmailExtension.visibility = View.VISIBLE
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
            viewBinding.recyclerViewEmailExtension.visibility = View.GONE
        } else {
            viewBinding.recyclerViewEmailExtension.visibility = View.VISIBLE
        }

        emailExtensionListFiltered.let {
            viewBinding.recyclerViewEmailExtension.removeAllViews()
            emailExtensionAdapter?.updateList(emailExtensionListFiltered)
            emailExtensionAdapter?.notifyDataSetChanged()
        }
    }

    companion object {
        private const val MAX_SHOWING_LIST = 7
    }
}