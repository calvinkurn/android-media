package com.tokopedia.contactus.inboxticket2.handlingListTags

import android.text.Editable
import android.text.Html
import com.tokopedia.contactus.inboxticket2.handlingListTags.HtmlUtil.LI_TAG
import com.tokopedia.contactus.inboxticket2.handlingListTags.HtmlUtil.OL_TAG
import com.tokopedia.contactus.inboxticket2.handlingListTags.HtmlUtil.UL_TAG
import org.xml.sax.XMLReader
import java.util.*

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



