package com.tokopedia.ovop2p.view.adapters

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import android.widget.Toast

import com.tokopedia.ovop2p.R

class ContactsCursorAdapter(context: Context, cursor: Cursor, private val mPartialMatch: String) : CursorAdapter(context, cursor, false) {

    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = LayoutInflater.from(context)
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        return mLayoutInflater.inflate(R.layout.search_suggestion_item_layout, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor?) {
        var name = ""
        var phoneNum = ""
        var imageUri = ""
        if (cursor != null && cursor.count > 0) {
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            phoneNum = phoneNum.replaceFirst(mPartialMatch.toRegex(), "<font color='#06b3ba'>$mPartialMatch</font>")
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            if(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)) != null) {
                imageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
            }
        }

        val nameTv = view.findViewById<TextView>(R.id.tv_name)
        nameTv.text = name

        val phoneNo = view.findViewById<TextView>(R.id.tv_phone)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            phoneNo.text = Html.fromHtml(phoneNum, Html.FROM_HTML_MODE_LEGACY)
        } else {
            phoneNo.text = Html.fromHtml(phoneNum)
        }
        view.setOnClickListener { view ->
            val pName = view.findViewById<View>(R.id.tv_name) as TextView
            Toast.makeText(context, "Selected Contact " + pName.text,
                    Toast.LENGTH_LONG).show()
        }
    }
}
