package com.tokopedia.ovop2p.view.adapters

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.util.OvoP2pUtil

class ContactsCursorAdapter(context: Context, cursor: Cursor, private val mPartialMatch: String,
                            contactsSelectionCall: (String, String) -> Unit) : CursorAdapter(context, cursor, false) {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var contactsDataSetFunction: (String, String) -> Unit = contactsSelectionCall

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        var view = mLayoutInflater.inflate(R.layout.search_suggestion_item_layout, parent, false)
        view.tag = ViewHolder(view)
        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor?) {
        var name: String? = ""
        var phoneNum: String? = ""
        var prettyPhnNo = ""
        if (cursor != null && cursor.count > 0) {
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            phoneNum = OvoP2pUtil.extractNumbersFromString(phoneNum)
            prettyPhnNo = phoneNum.replaceFirst(mPartialMatch.toRegex(), "<font color='#06b3ba'>$mPartialMatch</font>") + " - "
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
        }
        var viewHolder = view.tag as ViewHolder
        viewHolder.nameTxTv.text = name ?: ""

        viewHolder.numberTxTv.text = MethodChecker.fromHtml(prettyPhnNo)
        view.setOnClickListener { view ->
            name?.let { phoneNum?.let { it1 -> contactsDataSetFunction(it, it1) } }
        }
    }

    class ViewHolder {
        var nameTxTv: TextView
        var numberTxTv: TextView

        constructor(view: View) {
            this.nameTxTv = view.findViewById(R.id.tv_name)
            this.numberTxTv = view.findViewById(R.id.tv_phone)
        }
    }
}
