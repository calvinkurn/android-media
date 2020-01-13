package com.rahullohra.fakeresponse

import android.content.Context
import android.content.Intent
import com.rahullohra.fakeresponse.presentaiton.activities.AddGqlActivity

class Router {

    companion object {

        fun routeToAddGql(context: Context?) {
            context?.startActivity(Intent(context, AddGqlActivity::class.java))
        }
    }
}