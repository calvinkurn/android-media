package com.tokopedia.ovop2p.view.adapters

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.ovop2p.R

class AllContactsListCursorAdapter(context: Context, c: Cursor, autoRequery: Boolean) : CursorAdapter(context, c, autoRequery) {

    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        var view = mLayoutInflater.inflate(R.layout.all_contacts_item_layout, parent, false)
        if(view.tag == null) {
            view.tag = ViewHolder(view)
        }
        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor?) {
        var name: String? = ""
        var phoneNum: String? = ""
        var imageUri: String? = ""
        if (cursor != null && cursor.count > 0) {
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            if(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)) != null) {
                imageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
            }
        }
        var viewHolder = view.tag as ViewHolder
        viewHolder.nameTxtv.text = name ?: ""
        viewHolder.numberTxTv.text = phoneNum ?: ""
        if (!TextUtils.isEmpty(imageUri)) {
            viewHolder.usrIv.setImageURI(Uri.parse(imageUri))
        } else {
            viewHolder.usrIv.setImageURI(Uri.EMPTY)
        }
    }

    class ViewHolder {
        var nameTxtv: TextView
        var numberTxTv: TextView
        var usrIv: ImageView

        constructor(view: View) {
            this.nameTxtv = view.findViewById(R.id.user_name)
            this.numberTxTv = view.findViewById(R.id.user_num)
            this.usrIv = view.findViewById(R.id.user_image)
        }
    }
}
