package com.tokopedia.digital_checkout.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.webview.TkpdWebView
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * created by @bayazidnasir on 30/6/2022
 */

object GoToPlusUtil {
    fun getWebViewLoaded(
        parentView: LinearLayout,
        context: Context,
        filename: String,
        onButtonClick: () -> Unit
    ) {
        parentView.apply {
            addView(getWebView(context, filename))
            addView(getButton(context, onButtonClick))
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun getWebView(context: Context, filename: String): TkpdWebView{
        return TkpdWebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            loadPartialWebView(openHTMLFile(context, filename))
        }
    }

    private fun getButton(context: Context, onButtonClick: () -> Unit): UnifyButton{
        return UnifyButton(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "Oke"
            setOnClickListener { onButtonClick.invoke() }
        }
    }

    private fun openHTMLFile(context: Context?, filename: String): String{
        val inputStream = context?.assets?.open(filename)
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        bufferReader.forEachLine {
            stringBuilder.append(it)
        }
        return stringBuilder.toString()
    }
}