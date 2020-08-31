package com.tokopedia.utils.htmltags

import android.text.Editable
import android.text.Html
import com.tokopedia.utils.htmltags.HtmlUtil.LI_TAG
import com.tokopedia.utils.htmltags.HtmlUtil.OL_TAG
import com.tokopedia.utils.htmltags.HtmlUtil.UL_TAG
import org.xml.sax.XMLReader
import java.util.Stack

class ListTagHandler : Html.TagHandler {

    private val list = Stack<IListTags>()

    override fun handleTag(
            opening: Boolean,
            tag: String,
            output: Editable,
            xmlReader: XMLReader
    ) {
        val listTagHelper = ListTagHelper()
        when (tag) {
            UL_TAG -> if (opening)
                list.push(BulletedList(listTagHelper))
            else
                list.pop()

            OL_TAG -> if (opening)
                list.push(NumberedList(listTagHelper))
            else
                list.pop()

            LI_TAG -> if (opening)

                list.peek().openTag(output)
            else
                list.peek().closeTag(output)

        }
    }
}



