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
        return mLayoutInflater.inflate(R.layout.all_contacts_item_layout, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor?) {
        var name = ""
        var phoneNum = ""
        var imageUri = ""
        if (cursor != null && cursor.count > 0) {
            phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            if(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI)) != null) {
                imageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI))
            }
        }
        val nameTxtv = view.findViewById<TextView>(R.id.user_name)
        nameTxtv.text = name
        val numberTxtv = view.findViewById<TextView>(R.id.user_num)
        numberTxtv.text = phoneNum
        val usrImgV = view.findViewById<ImageView>(R.id.user_image)
        if (!TextUtils.isEmpty(imageUri)) {
            usrImgV.setImageURI(Uri.parse(imageUri))
        }
    }
}
