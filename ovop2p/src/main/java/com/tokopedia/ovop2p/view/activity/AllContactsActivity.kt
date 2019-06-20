package com.tokopedia.ovop2p.view.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.view.adapters.AllContactsListCursorAdapter

class AllContactsActivity : BaseSimpleActivity(){
    override fun getNewFragment(): Fragment {
        return Fragment()
    }

}
