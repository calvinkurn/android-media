package com.tokopedia.sdui.extention

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.TextView
import com.yandex.div.core.extension.DivExtensionHandler
import com.yandex.div.core.view2.Div2View
import com.yandex.div2.DivBase
import com.yandex.div2.DivText

class HTMLHandler : DivExtensionHandler {
    override fun bindView(divView: Div2View, view: View, div: DivBase) {
        val textView = view as TextView
        val textHtml =  textView.text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(textHtml.toString(), Html.FROM_HTML_MODE_LEGACY)
        }else{
            textView.text = Html.fromHtml(textHtml.toString())
        }
    }

    override fun matches(div: DivBase): Boolean {
        return div is DivText
    }

    override fun unbindView(divView: Div2View, view: View, div: DivBase) {
    }
}
