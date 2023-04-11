package com.tokopedia.fakeresponse

import android.content.Context
import android.content.Intent
import com.tokopedia.fakeresponse.chuck.presentation.activities.SearchActivity
import com.tokopedia.fakeresponse.presentation.activities.AddGqlActivity
import com.tokopedia.fakeresponse.presentation.activities.AddRestResponseActivity
import com.tokopedia.fakeresponse.presentation.activities.ExportActivity
import com.tokopedia.fakeresponse.presentation.activities.PasteTextActivity

class Router {

    companion object {

        const val BUNDLE_ARGS_ID = "id"
        const val BUNDLE_ARGS_FROM_CHUCK = "fromChuck"

        fun routeToAddGqlFromNotification(
            context: Context?,
            id: Int? = null,
            fromChuck: Boolean = false
        ): Intent {
            val intent = Intent(context, AddGqlActivity::class.java)
            intent.putExtra(BUNDLE_ARGS_ID, id)
            intent.putExtra(BUNDLE_ARGS_FROM_CHUCK, fromChuck)
            return intent
        }

        fun routeToAddGql(context: Context?, id: Int? = null, fromChuck: Boolean = false) {
            val intent = Intent(context, AddGqlActivity::class.java)
            intent.putExtra(BUNDLE_ARGS_ID, id)
            intent.putExtra(BUNDLE_ARGS_FROM_CHUCK, fromChuck)
            context?.startActivity(intent)
        }

        fun routeToAddRest(context: Context?, id: Int? = null, fromChuck: Boolean = false) {
            val intent = Intent(context, AddRestResponseActivity::class.java)
            intent.putExtra(BUNDLE_ARGS_ID, id)
            intent.putExtra(BUNDLE_ARGS_FROM_CHUCK, fromChuck)
            context?.startActivity(intent)
        }

        fun routeToSearch(context: Context?) {
            val intent = Intent(context, SearchActivity::class.java)
            context?.startActivity(intent)
        }

        fun routeToExportActivity(context: Context?) {
            val intent = Intent(context, ExportActivity::class.java)
            context?.startActivity(intent)
        }

        fun routeToPasteTextActivity(context: Context?) {
            val intent = Intent(context, PasteTextActivity::class.java)
            context?.startActivity(intent)
        }
    }
}