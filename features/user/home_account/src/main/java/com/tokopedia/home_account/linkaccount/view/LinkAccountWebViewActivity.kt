package com.tokopedia.home_account.linkaccount.view

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.home_account.R
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity


/**
 * Created by Yoris on 10/08/21.
 */

class LinkAccountWebViewActivity: BaseSimpleWebViewActivity() {

    companion object {
        const val KEY_URL = ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_link_account_skip) {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_link_account, menu)

        val item = menu.findItem(R.id.menu_link_account_skip)
        val s = SpannableString("Lewati")
        s.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.unify_G500)), 0, s.length, 0)
        item.title = s

        return true
    }

    override fun getNewFragment(): Fragment {
        val mUrl = intent.getStringExtra(KEY_URL)
        return BaseSessionWebViewFragment.newInstance(mUrl)
    }
}