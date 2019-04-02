package com.tokopedia.chatbot

import android.content.Context
import java.util.ArrayList

/**
 * @author by nisie on 10/01/19.
 */
interface ChatbotRouter{

    fun openImagePreviewFromChat(context: Context, listImage: ArrayList<String>, imageDesc: ArrayList<String>, title: String, date: String)
}