package com.rahullohra.fakeresponse

import android.content.Context
import android.content.Intent
import com.rahullohra.fakeresponse.presentaiton.activities.AddGqlActivity
import com.rahullohra.fakeresponse.presentaiton.activities.AddRestResponseActivity

class Router {

    companion object {

        fun routeToAddGql(context: Context?) {
            context?.startActivity(Intent(context, AddGqlActivity::class.java))
        }

        fun routeToAddRest(context: Context?) {
            context?.startActivity(Intent(context, AddRestResponseActivity::class.java))
        }
    }
}