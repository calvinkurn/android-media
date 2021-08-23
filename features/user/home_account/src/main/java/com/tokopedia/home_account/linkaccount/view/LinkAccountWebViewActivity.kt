package com.tokopedia.home_account.linkaccount.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.webview.BaseSessionWebViewFragment
import com.tokopedia.webview.BaseSimpleWebViewActivity

/**
 * Created by Yoris on 10/08/21.
 */

class LinkAccountWebViewActivity: BaseSimpleWebViewActivity() {

    companion object {
        const val KEY_URL = "webview_url"

        fun newInstance(context: Context?, url: String?): Intent {
            val intent = Intent(context, LinkAccountWebViewActivity::class.java)
            intent.putExtra(KEY_URL, url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.menu_link_account_skip) {
//
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_link_account, menu)
//
//        val item = menu.findItem(R.id.menu_link_account_skip)
//        val s = SpannableString("Lewati")
//        s.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.unify_G500)), 0, s.length, 0)
//        item.title = s
//
//        return true
//    }

    override fun getNewFragment(): Fragment {
        val mUrl = intent.getStringExtra(KEY_URL)
        println(mUrl)
        return BaseSessionWebViewFragment.newInstance(mUrl)
    }
}