package com.tokopedia.contactus.createticket.router

import android.content.Context
import android.content.Intent

interface ContactUsCreateTicketRouter {

    fun getHomeIntent(context: Context): Intent

}