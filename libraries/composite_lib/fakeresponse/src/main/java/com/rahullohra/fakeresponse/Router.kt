package com.rahullohra.fakeresponse

import android.content.Context
import android.content.Intent
import com.rahullohra.fakeresponse.presentaiton.activities.AddGqlActivity
import com.rahullohra.fakeresponse.presentaiton.activities.AddRestResponseActivity

class Router {

    companion object {

        const val BUNDLE_ARGS_ID = "id"

        fun routeToAddGql(context: Context?, id: Int? = null) {
            val intent = Intent(context, AddGqlActivity::class.java)
            intent.putExtra(BUNDLE_ARGS_ID, id)
            context?.startActivity(intent)
        }

        fun routeToAddRest(context: Context?, id: Int? = null) {
            val intent = Intent(context, AddRestResponseActivity::class.java)
            intent.putExtra(BUNDLE_ARGS_ID, id)
            context?.startActivity(intent)
        }
    }
}